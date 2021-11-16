package com.example.resource.controllers;

import com.example.resource.dto.*;
import com.example.resource.elasticSearchEntities.PartElastic;
import com.example.resource.entities.Label;
import com.example.resource.entities.Part;
import com.example.resource.repositories.LabelRepository;
import com.example.resource.repositories.PartElasticRepository;
import com.example.resource.repositories.PartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("parts")
public class PartsController {


    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartElasticRepository partElasticRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private LabelRepository labelRepository;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String searchText, @RequestParam int pageNumber, @RequestParam int pageSize) {

        if (searchText.trim().equals("")) {
            List<PartElastic> partElastics = new ArrayList<>();

            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "partNameKeyword");

            partElasticRepository.findAll(pageRequest).forEach(partElastics::add);
            List<HighlightedPartDTO> highlightedParts = partElastics.stream().map(partElastic -> new HighlightedPartDTO(partElastic.getId(), partElastic.getPartName(), partElastic.getWarehouseNumber(), null)).collect(Collectors.toList());

            PartResponseDTO response = new PartResponseDTO(partElasticRepository.count(), highlightedParts);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("partName", searchText).fuzziness(Fuzziness.ZERO).fuzzyTranspositions(false);
        MatchQueryBuilder matchQueryBuilder1 = new MatchQueryBuilder("partNameNgram", searchText).fuzziness(Fuzziness.AUTO).fuzzyTranspositions(true);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder().should(matchQueryBuilder).should(matchQueryBuilder1);

        //NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQueryBuilder).withPageable(pageRequest)
        //.withHighlightFields(new HighlightBuilder.Field("partName").preTags("<span class='highlighted'>").postTags("</span>")).build();

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(pageRequest)
                .withHighlightFields(new HighlightBuilder.Field("partName").preTags("<b>").postTags("</b>"), new HighlightBuilder.Field("partNameNgram").preTags("<b>").postTags("</b>")).build();

        SearchHits<PartElastic> partSearchHits = elasticsearchOperations.search(query, PartElastic.class);
        //return new ResponseEntity<>(partSearchHits, HttpStatus.OK);

        List<HighlightedPartDTO> highlightedParts = partSearchHits.getSearchHits().stream().map(searchHit -> {
            PartElastic partElastic = searchHit.getContent();
            String highlightedField = "";

            if(searchHit.getHighlightField("partName").size() > 0 && searchHit.getHighlightField("partNameNgram").size() > 0){
                List<String> partNameList = Arrays.asList(searchHit.getHighlightField("partName").get(0).split(" "));
                List<String> partNameNgramList = Arrays.asList(searchHit.getHighlightField("partNameNgram").get(0).split(" "));

                for (int i = 0;i<partNameList.size();i++){

                    if(partNameList.get(i).startsWith("<b>")){
                        highlightedField = highlightedField+partNameList.get(i)+" ";
                    }else{
                        highlightedField = highlightedField+partNameNgramList.get(i)+" ";
                    }
                }



            }else if (searchHit.getHighlightField("partName").size() > 0) {
                highlightedField = searchHit.getHighlightField("partName").get(0);
            } else {
                highlightedField = searchHit.getHighlightField("partNameNgram").get(0);
            }

            return new HighlightedPartDTO(partElastic.getId(), partElastic.getPartName(), partElastic.getWarehouseNumber(), highlightedField);
        }).collect(Collectors.toList());


        PartResponseDTO response = new PartResponseDTO(partSearchHits.getTotalHits(), highlightedParts);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/loadParts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> loadPartsFromJson() throws IOException {

        partRepository.deleteAll();
        partElasticRepository.deleteAll();

        ObjectMapper mapper = new ObjectMapper();


        DataDTO partModels = mapper.readValue(new File("c:\\parts.json"), DataDTO.class);

        List<PartDTO> partsDTO = new ArrayList<>(partModels.getParts().values());

        List<Part> parts = partsDTO.stream().map(partDTO -> new Part(partDTO.getPart_name(), partDTO.getWarehouse_number())).collect(Collectors.toList());

        parts = partRepository.saveAll(parts);

        List<PartElastic> partElastics = parts.stream().map(part -> new PartElastic(part.getId(), part.getPartName(), part.getWarehouseNumber())).collect(Collectors.toList());

        partElasticRepository.saveAll(partElastics);


        List<IndexedPartDTO> indexedPartDTOS = new ArrayList<>(partModels.getIndexedParts().values());

        List<Label> labels = indexedPartDTOS.stream().map(indexedPartDTO -> {
            Label newLabel = new Label(indexedPartDTO.getPart_name(), indexedPartDTO.getCar_name(), indexedPartDTO.getWarehouse_number());
            newLabel.setId(indexedPartDTO.getIndex());
            return newLabel;
        }).collect(Collectors.toList());

        labelRepository.deleteAll();
        labelRepository.saveAll(labels);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPart(@RequestParam String partName, @RequestParam String warehouseNumber) {

        partName = partName.trim();
        warehouseNumber = warehouseNumber.trim();

        if (partName.length() == 0) {
            return new ResponseEntity<>("bad_parameters", HttpStatus.BAD_REQUEST);
        }

        Part part = new Part(partName, warehouseNumber);
        part = partRepository.save(part);
        PartElastic partElastic = new PartElastic(part.getId(), part.getPartName(), part.getWarehouseNumber());
        partElasticRepository.save(partElastic);

        return new ResponseEntity<>(part, HttpStatus.OK);
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPart(@RequestParam String id) {

        try {
            Part part = partRepository.findById(Integer.valueOf(id)).orElseThrow(Exception::new);
            return new ResponseEntity<>(part, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("wrong_id", HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePart(@RequestParam String id, @RequestParam String partName, @RequestParam String warehouseNumber) {

        partName = partName.trim();
        warehouseNumber = warehouseNumber.trim();

        if (partName.length() == 0) {
            return new ResponseEntity<>("bad_parameters", HttpStatus.BAD_REQUEST);
        }

        try {

            int idValue = Integer.parseInt(id);

            Part part = partRepository.findById(idValue).orElseThrow(Exception::new);
            PartElastic partElastic = partElasticRepository.findById(idValue).orElseThrow(Exception::new);

            part.setPartName(partName);
            part.setWarehouseNumber(warehouseNumber);

            partElastic.setPartName(partName);
            partElastic.setWarehouseNumber(warehouseNumber);

            partRepository.save(part);
            partElasticRepository.save(partElastic);

            return new ResponseEntity<>(part, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("wrong_id", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deletePart(@RequestParam String id) {
        try {

            int idValue = Integer.parseInt(id);

            partRepository.deleteById(idValue);
            partElasticRepository.deleteById(idValue);

            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("wrong_id", HttpStatus.BAD_REQUEST);
        }
    }

}
