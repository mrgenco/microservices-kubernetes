package com.twitter.profile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/profile")
public class ProfileService {


    @GetMapping("/{id}")
    public UserProfile findUser(@PathVariable Long id) throws IOException, URISyntaxException {
        return findUserById(id);
    }

    private UserProfile findUserById(Long id) throws IOException, URISyntaxException {
        UserProfile user = new UserProfile();
        File file = new File("D:\\projects\\upwork\\Akshay Jadhav\\Microservices (Java) Development\\source code\\db\\profiles");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;

        Long currentId = null;
        while ((str = br.readLine()) != null){
            if(str.startsWith("ID:")){
                currentId = Long.parseLong(str.substring(str.lastIndexOf("ID:") + 3));
            }
            if(currentId == id){
                user.setId(currentId);
                if(str.startsWith("EMAIL:"))
                    user.setEmail(str.substring(str.lastIndexOf("EMAIL:") + 6));
                else if(str.startsWith("NAME:"))
                    user.setName(str.substring(str.lastIndexOf("NAME:") + 5));
                else if(str.startsWith("USERNAME:"))
                    user.setUsername(str.substring(str.lastIndexOf("USERNAME:") + 9));
                else if(str.startsWith("PICTURE:"))
                    user.setPicture(str.substring(str.lastIndexOf("PICTURE:") + 8));
            }

        }

        return user;
    }

}
