package com.example.resource.controllers;

import com.example.resource.elasticSearchEntities.PartElastic;
import com.example.resource.elasticSearchEntities.ReturnElastic;
import com.example.resource.entities.Return;
import com.example.resource.repositories.PartElasticRepository;
import com.example.resource.repositories.PartRepository;
import com.example.resource.repositories.ReturnElasticRepository;
import com.example.resource.repositories.ReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ADMIN')")
public class SyncController {

    @Autowired
    private ReturnRepository returnRepository;

    @Autowired
    private ReturnElasticRepository returnElasticRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartElasticRepository partElasticRepository;

    @GetMapping("/sync")
    @Transactional
    public ResponseEntity<?> sync(){

        this.returnElasticRepository.deleteAll();

        List<ReturnElastic> returnList = this.returnRepository.findAll().stream().map(ReturnElastic::new).collect(Collectors.toList());

        this.returnElasticRepository.saveAll(returnList);


        this.partElasticRepository.deleteAll();

        List<PartElastic> partList = this.partRepository.findAll().stream().map(part -> new PartElastic(part.getId(),part.getPartName(),part.getWarehouseNumber())).collect(Collectors.toList());

        this.partElasticRepository.saveAll(partList);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

}
