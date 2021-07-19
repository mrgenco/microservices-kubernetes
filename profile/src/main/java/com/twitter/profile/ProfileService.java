package com.twitter.profile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {


    // constant variables for profiles data
    private static final String PROFILES = "profiles";
    private static final String ID = "ID:";
    private static final String EMAIL = "EMAIL:";
    private static final String NAME = "NAME:";
    private static final String USERNAME = "USERNAME:";
    private static final String PICTURE = "PICTURE:";

    // datasource destination as environment variables
    @Value("${datasource.path}")
    private String dataSourcePath;

    List<UserProfile> findAll() throws Exception {
        List<UserProfile> userList = new ArrayList<>();
        File file = new File(dataSourcePath + PROFILES);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        UserProfile user = null;
        while ((line = br.readLine()) != null){
                if(line.startsWith(ID)){
                    user = new UserProfile();
                    user.setId(Long.parseLong(line.substring(line.lastIndexOf(ID) + 3)));
                    userList.add(user);
                }
                else if(line.startsWith(EMAIL))
                    user.setEmail(line.substring(line.lastIndexOf(EMAIL) + 6));
                else if(line.startsWith(NAME))
                    user.setName(line.substring(line.lastIndexOf(NAME) + 5));
                else if(line.startsWith(USERNAME))
                    user.setUsername(line.substring(line.lastIndexOf(USERNAME) + 9));
                else if(line.startsWith(PICTURE))
                    user.setPicture(line.substring(line.lastIndexOf(PICTURE) + 8));
        }
        br.close();
        return userList;
    }

    UserProfile findUserById(Long id) throws Exception {
        UserProfile user = new UserProfile();
        File file = new File(dataSourcePath + PROFILES);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Long currentId = null;
        while ((line = br.readLine()) != null){
            if(line.startsWith(ID)){
                currentId = Long.parseLong(line.substring(line.lastIndexOf(ID) + 3));
            }
            if(currentId == id){
                user.setId(currentId);
                if(line.startsWith(EMAIL))
                    user.setEmail(line.substring(line.lastIndexOf(EMAIL) + 6));
                else if(line.startsWith(NAME))
                    user.setName(line.substring(line.lastIndexOf(NAME) + 5));
                else if(line.startsWith(USERNAME))
                    user.setUsername(line.substring(line.lastIndexOf(USERNAME) + 9));
                else if(line.startsWith(PICTURE))
                    user.setPicture(line.substring(line.lastIndexOf(PICTURE) + 8));
            }
        }
        br.close();
        return user;
    }

    boolean updateUser(UserProfile userProfile) throws Exception {
        boolean isUpdated = false;
        File file = new File(dataSourcePath + PROFILES);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer inputBuffer = new StringBuffer();
        String line;
        Long currentId = null;
        while ((line = br.readLine()) != null){
            if(line.startsWith(ID)){
                currentId = Long.parseLong(line.substring(line.lastIndexOf(ID) + 3));
                inputBuffer.append(line);
                inputBuffer.append('\n');
                continue;
            }
            if(currentId == userProfile.getId()){
                if(line.startsWith(EMAIL))
                    line = line.replace(line, EMAIL +userProfile.getEmail());
                else if(line.startsWith(NAME))
                    line = line.replace(line, NAME +userProfile.getName());
                else if(line.startsWith(USERNAME))
                    line = line.replace(line, USERNAME +userProfile.getUsername());
                else if(line.startsWith(PICTURE))
                    line = line.replace(line, PICTURE +userProfile.getPicture());

                isUpdated = true;
            }
            inputBuffer.append(line);
            inputBuffer.append('\n');
        }
        br.close();
        // write the new string with the replaced line OVER the same file
        FileOutputStream fileOut = new FileOutputStream(dataSourcePath + PROFILES);
        fileOut.write(inputBuffer.toString().getBytes());
        fileOut.close();

        return isUpdated;
    }
}
