package com.example.resource.controllers;

import com.example.resource.dto.CommandDTO;
import com.example.resource.entities.Label;
import com.example.resource.repositories.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("label")
@PreAuthorize("hasRole('ADMIN')")
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;


    @PostMapping("/add")
    public ResponseEntity<?> addLabel(@RequestParam String partName,@RequestParam String warehouseNumber,@RequestParam String carName){

        Label label = new Label(partName,carName,warehouseNumber);
        label = labelRepository.save(label);

        return new ResponseEntity<>(label, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getLabel(@RequestParam int id){

        Optional<Label> label = labelRepository.findById(id);

        if(!label.isPresent()){
            return new ResponseEntity<>("not_found", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(label.get(), HttpStatus.OK);
        }

    }

    @PostMapping("/update")
    public ResponseEntity<?> updateLabel(@RequestParam int id,@RequestParam String partName,@RequestParam String warehouseNumber,@RequestParam String carName,@RequestParam String warehouseNumberScanned){


        Optional<Label> labelOptional = labelRepository.findById(id);

        if(!labelOptional.isPresent()){
            return new ResponseEntity<>("not_found", HttpStatus.BAD_REQUEST);
        }

        Label label = labelOptional.get();

        label.setPartName(partName);
        label.setCarName(carName);
        label.setWarehouseNumber(warehouseNumber);
        label.setWarehouseNumberScanned(warehouseNumberScanned);

        labelRepository.save(label);

        return new ResponseEntity<>(label, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteLabel(@RequestParam int id){

        Optional<Label> labelOptional = labelRepository.findById(id);

        if(!labelOptional.isPresent()){
            return new ResponseEntity<>("not_found", HttpStatus.BAD_REQUEST);
        }

        Label label = labelOptional.get();

        labelRepository.delete(label);

        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }


    @PostMapping("/sync")
    public ResponseEntity<?>  sync(@RequestBody String commands){


        List<String> sda = Arrays.asList(commands.split("\\r?\\n"));
        List<CommandDTO> commandDTOS = new ArrayList<>();


        CommandDTO commandDTO = new CommandDTO();
        List<String> commandList = new ArrayList<>();

        for (String s : sda) {

            s = s.trim();

            if (s.chars().allMatch(Character::isDigit)) {
                commandList.add(s);
            }else if(s.startsWith("#")){

                commandList.add(s.substring(1));

            }else {

                commandDTO.setWarehouseNumber(s);
                commandDTO.setLabelNumbers(commandList);
                commandDTOS.add(commandDTO);

                commandDTO = new CommandDTO();
                commandList = new ArrayList<>();
            }

        }


        for (CommandDTO commandDTO1:commandDTOS){

            for (String labelId:commandDTO1.getLabelNumbers()){


                try {
                    Optional<Label> labelOptional = labelRepository.findById(Integer.valueOf(labelId));

                    if(labelOptional.isPresent()){

                        Label label = labelOptional.get();

                        label.setWarehouseNumberScanned(commandDTO1.getWarehouseNumber());

                        labelRepository.save(label);

                    }
                }catch (NumberFormatException ignored){

                }


            }

        }




        return new ResponseEntity<>("lewap",HttpStatus.OK);
    }

}
