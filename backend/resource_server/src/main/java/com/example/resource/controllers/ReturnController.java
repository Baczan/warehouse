package com.example.resource.controllers;

import com.example.resource.dto.ReturnDTO;
import com.example.resource.dto.ReturnSearchResponse;
import com.example.resource.elasticSearchEntities.AuctionElastic;
import com.example.resource.elasticSearchEntities.PartElastic;
import com.example.resource.elasticSearchEntities.ReturnElastic;
import com.example.resource.entities.Return;
import com.example.resource.repositories.ReturnElasticRepository;
import com.example.resource.repositories.ReturnRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("return")
@PreAuthorize("hasRole('ADMIN')")
public class ReturnController {

    @Autowired
    private ReturnRepository returnRepository;

    @Autowired
    private ReturnElasticRepository returnElasticRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @PostMapping("/add")
    public ResponseEntity<?> addReturn(@RequestBody ReturnDTO returnDTO) {

        Return returnEntity = new Return(returnDTO);
        returnEntity = returnRepository.save(returnEntity);

        ReturnElastic returnElastic = new ReturnElastic(returnEntity);
        returnElasticRepository.save(returnElastic);
        return new ResponseEntity<>(returnEntity, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getReturn(@RequestParam int id) {

        Optional<Return> returnOptional = returnRepository.findById(id);


        if (!returnOptional.isPresent()) {
            return new ResponseEntity<>("not_found", HttpStatus.BAD_REQUEST);
        }

        Return returnEntity = returnOptional.get();

        return new ResponseEntity<>(new ReturnDTO(returnEntity), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateReturn(@RequestBody ReturnDTO returnDTO, @RequestParam int id) {
        Optional<Return> returnOptional = returnRepository.findById(id);

        if (!returnOptional.isPresent()) {
            return new ResponseEntity<>("not_found", HttpStatus.BAD_REQUEST);
        }

        Return returnEntity = new Return(returnDTO);
        returnEntity.setId(id);

        returnEntity = returnRepository.save(returnEntity);
        ReturnElastic returnElastic = new ReturnElastic(returnEntity);
        returnElasticRepository.save(returnElastic);

        return new ResponseEntity<>(returnEntity, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReturn(@RequestParam int id) {
        Optional<Return> returnOptional = returnRepository.findById(id);

        if (!returnOptional.isPresent()) {
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        }

        returnRepository.deleteById(id);
        returnElasticRepository.deleteById(id);

        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String searchText, @RequestParam int pageNumber, @RequestParam int pageSize) throws IOException {

        searchText = searchText.trim();


        if (searchText.length() == 0) {
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "id");
            Page<Return> returnPage = this.returnRepository.findAll(pageRequest);

            return new ResponseEntity<>(new ReturnSearchResponse(returnPage.getTotalElements(), returnPage.getContent()), HttpStatus.OK);
        } else {


            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

            MatchQueryBuilder matchName = new MatchQueryBuilder("name", searchText).fuzziness(Fuzziness.AUTO).fuzzyTranspositions(true).boost(2);
            //MatchQueryBuilder matchNameNgram = new MatchQueryBuilder("nameNgram", searchText).fuzziness(Fuzziness.AUTO).fuzzyTranspositions(true).prefixLength(3);

            MatchQueryBuilder matchAuctionTitle = new MatchQueryBuilder("auctions.auctionTitle", searchText).fuzziness(Fuzziness.AUTO).fuzzyTranspositions(true);
            //MatchQueryBuilder matchAuctionTitleNgram = new MatchQueryBuilder("auctions.auctionTitleNgram", searchText).fuzziness(Fuzziness.AUTO).fuzzyTranspositions(true);


            //MatchQueryBuilder matchAuctionNumber = new MatchQueryBuilder("auctions.auctionNumber", searchText).fuzziness(Fuzziness.ZERO).fuzzyTranspositions(false).boost((float) 1.5);
            //MatchQueryBuilder matchAuctionNumberNgram = new MatchQueryBuilder("auctions.auctionNumberNgram", searchText).fuzziness(Fuzziness.ZERO).fuzzyTranspositions(false).boost((float) 1.5);


            String[] searchTextTerms = searchText.split(" ");

            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                    .should(matchName)
                    .should(matchAuctionTitle);
                    //.should(matchNameNgram);


            for (String searchTerm : searchTextTerms) {
                MatchPhrasePrefixQueryBuilder prefixAuctionTitle = new MatchPhrasePrefixQueryBuilder("auctions.auctionTitle", searchTerm);
                MatchPhrasePrefixQueryBuilder prefixName = new MatchPhrasePrefixQueryBuilder("name", searchTerm);
                MatchPhrasePrefixQueryBuilder prefixAuctionNumber = new MatchPhrasePrefixQueryBuilder("auctions.auctionNumber", searchTerm);
                boolQueryBuilder = boolQueryBuilder
                        .should(prefixAuctionTitle)
                        .should(prefixName)
                        .should(prefixAuctionNumber);
            }


            /*BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                    .should(prefixAuctionTitle)
                    .should(prefixAuctionNumber);
                   *//* .should(matchName)
                    .should(matchNameNgram)
                    //.should(matchAuctionNumber)
                    .should(matchAuctionTitle)
                    .should(matchAuctionTitleNgram)
                    .should(matchAuctionNumber)
                    .should(matchAuctionNumberNgram);*/

            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(boolQueryBuilder)
                    .withPageable(pageRequest)
                    .withTrackScores(true)
                    .build();


            SearchHits<ReturnElastic> partSearchHits = elasticsearchOperations.search(query, ReturnElastic.class);


            List<ReturnElastic> returnElasticList = partSearchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

            List<Integer> returnIds = returnElasticList.stream().map(ReturnElastic::getId).collect(Collectors.toList());

            Page<Return> returnPage = returnRepository.findByIds(returnIds, PageRequest.of(0, 25));

            List<Return> sortedReturns = returnIds.stream().map(integer -> returnPage.getContent().stream().filter(returnModel -> returnModel.getId() == integer).findFirst().orElse(null)).collect(Collectors.toList());

            //return new ResponseEntity<>(partSearchHits, HttpStatus.OK);
            return new ResponseEntity<>(new ReturnSearchResponse(returnPage.getTotalElements(), sortedReturns), HttpStatus.OK);
        }
    }


}
