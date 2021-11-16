package com.example.resource.controllers;

import com.example.resource.dto.NewCarDTO;
import com.example.resource.entities.Car;
import com.example.resource.entities.Image;
import com.example.resource.entities.NewCar;
import com.example.resource.repositories.ImageRepository;
import com.example.resource.repositories.NewCarRepository;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("newCar")
public class NewCarController {

    private List<String> imageExtensions = Arrays.asList("jpg","jpeg","png");

    @Autowired
    private NewCarRepository newCarRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private ImageRepository imageRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addCar(@RequestParam String carName){

        carName = carName.trim();

        if(carName.length()==0){
            return new ResponseEntity<>("empty_name", HttpStatus.BAD_REQUEST);
        }

        NewCar car = new NewCar(carName, new Date());

        car = newCarRepository.save(car);

        return new ResponseEntity<>(car,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){

        List<NewCar> cars = newCarRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));


        return new ResponseEntity<>(cars,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam int id){

        Optional<NewCar> car = newCarRepository.findById(id);

        if(!car.isPresent()){
            return new ResponseEntity<>("not_found",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(car.get(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam int id, @RequestBody NewCar carUpdated){


        Optional<NewCar> carOptional = newCarRepository.findById(id);

        if(!carOptional.isPresent()){
            return new ResponseEntity<>("not_found",HttpStatus.BAD_REQUEST);
        }

        NewCar car = carOptional.get();

        carUpdated.setId(car.getId());
        carUpdated.setDateAdded(car.getDateAdded());

        carUpdated = newCarRepository.save(carUpdated);



        return new ResponseEntity<>(carUpdated,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/uploadImage")
    @Transactional
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file,@RequestParam int carId) throws IOException {

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());


            if(!imageExtensions.contains(extension)){
                return new ResponseEntity<>("wrong_extension",HttpStatus.BAD_REQUEST);
            }

            Optional<NewCar> carOptional = newCarRepository.findById(carId);

            if(!carOptional.isPresent()){
                return new ResponseEntity<>("car_not_found",HttpStatus.BAD_REQUEST);
            }


            Image image = new Image(carId,extension,file.getContentType());
            image = imageRepository.save(image);

            File originalImage = getOriginalFile(image.getId());
            file.transferTo(originalImage);

            BufferedImage bufferedImage = ImageIO.read(originalImage);

            bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.QUALITY,200,150);

            ImageIO.write(bufferedImage,image.getExtension(),getResizedFile(image.getId()));


            return new ResponseEntity<>(image,HttpStatus.OK);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getImage")
    public ResponseEntity<?> getImage(@RequestParam int imageId,@RequestParam(defaultValue = "false") boolean thumbnail){

        try {
            Optional<Image> imageOptional = imageRepository.findById(imageId);

            if(!imageOptional.isPresent()){
                return new ResponseEntity<>("not_found",HttpStatus.BAD_REQUEST);
            }

            Image image = imageOptional.get();

            File imageFile;

            if(thumbnail){
                imageFile = getResizedFile(imageId);
            }else {
                imageFile = getOriginalFile(imageId);
            }


            //Create headers object
            HttpHeaders headers = new HttpHeaders();

            //Set filename
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + imageId+"."+image.getExtension());

            //Set file type
            headers.add(HttpHeaders.CONTENT_TYPE, image.getContentType());

            //Read file
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(imageFile.getAbsolutePath())));

            //Response
            return ResponseEntity
                    .ok() //Response status OK
                    .headers(headers) //Headers
                    .contentLength(imageFile.length()) //File size
                    .body(resource); //File

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllImages")
    public ResponseEntity<?> getAllImages(@RequestParam int carId){

        Optional<NewCar> carOptional = newCarRepository.findById(carId);

        if(!carOptional.isPresent()){
            return new ResponseEntity<>("car_not_found",HttpStatus.BAD_REQUEST);
        }

        List<Image> images = imageRepository.findAllByCarId(carId);

        return new ResponseEntity<>(images,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteImage")
    @Transactional
    public ResponseEntity<?> deleteImage(@RequestParam int imageId){

        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if(!optionalImage.isPresent()){
            return new ResponseEntity<>("image_not_found",HttpStatus.BAD_REQUEST);
        }

        Image image = optionalImage.get();

        imageRepository.delete(image);

        getOriginalFile(imageId).delete();
        getResizedFile(imageId).delete();

        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteCar")
    @Transactional
    public ResponseEntity<?> deleteCar(@RequestParam int carId){

        Optional<NewCar> carOptional = newCarRepository.findById(carId);

        if(!carOptional.isPresent()){
            return new ResponseEntity<>("car_not_found",HttpStatus.BAD_REQUEST);
        }

        NewCar car = carOptional.get();

        newCarRepository.delete(car);

        List<Image> images = imageRepository.findAllByCarId(carId);

        images.forEach(image -> {
            getOriginalFile(image.getId()).delete();
            getResizedFile(image.getId()).delete();
        });

        imageRepository.deleteAll(images);

        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }




    private File getOriginalFile(int id){
        String path = environment.getProperty("app.file.location") + "original/"+id;
        return new File(path);
    }

    private File getResizedFile(int id){
        String path = environment.getProperty("app.file.location") + "resized/"+id;
        return new File(path);
    }



}
