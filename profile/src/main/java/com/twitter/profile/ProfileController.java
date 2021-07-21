package com.twitter.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private ProfileService service;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.service = profileService;
    }

    @GetMapping(("/all"))
    public ResponseEntity<?> findAll(){
        try{
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserProfile(@PathVariable Long id)  {
        try{
            UserProfile userProfile = service.findUserById(id);
            if(userProfile.getId() != null)
                return new ResponseEntity<>(userProfile, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfile userProfile) {
        try{
            if(service.updateUser(userProfile))
                return new ResponseEntity<>(userProfile, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
