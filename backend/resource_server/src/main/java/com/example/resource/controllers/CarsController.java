package com.example.resource.controllers;

import com.example.resource.entities.Car;
import com.example.resource.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("car")
@PreAuthorize("hasRole('ADMIN')")
public class CarsController {

    @Autowired
    private CarRepository carRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addCar(@RequestParam String carName){

        carName = carName.trim();

        if(carName.length()==0){
            return new ResponseEntity<>("empty_name", HttpStatus.BAD_REQUEST);
        }

        Car car = new Car(carName, new Date());

        car = carRepository.save(car);

        return new ResponseEntity<>(car,HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCar(@RequestParam int id){

        Optional<Car> car = carRepository.findById(id);

        if(car.isPresent()){
            return new ResponseEntity<>(car,HttpStatus.OK);
        }

        return new ResponseEntity<>("not_found",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCar(@RequestParam int id){

        if(!carRepository.existsById(id)){
            return new ResponseEntity<>("not_found",HttpStatus.BAD_REQUEST);
        }
        carRepository.deleteById(id);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        List<Car> cars = carRepository.findAll();
        return new ResponseEntity<>(cars,HttpStatus.OK);
    }

}
