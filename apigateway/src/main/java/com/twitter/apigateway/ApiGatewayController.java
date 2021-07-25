package com.twitter.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api")
public class ApiGatewayController {


    private static final Logger LOG = Logger.getLogger(ApiGatewayController.class.getName());

    @Value("${service.profile}")
    private String profileServiceUrl;

    @Value("${service.stream}")
    private String streamServiceUrl;

    @Value("${service.stats}")
    private String statServiceUrl;


    @GetMapping()
    public ResponseEntity<?> serviceStatus()  {
        return new ResponseEntity<>("Hello from apigateway!", HttpStatus.OK);
    }


    /*
     *
     * Will return the profile, list of tweets and stats for the user as composite response
     *
     * */

    @GetMapping("/global/{id}")
    public ResponseEntity<?> globalPositionService(@PathVariable Long id)  {
        GlobalEntity globalEntity = new GlobalEntity();
        RestTemplate restTemplate = new RestTemplate();
        try{
            getUserProfileInfo(id, globalEntity, restTemplate);
            getStreamInfo(id, globalEntity, restTemplate);
            getStatsInfo(id, globalEntity, restTemplate);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(globalEntity, HttpStatus.OK);
    }


    @GetMapping("/streams/recent")
    public ResponseEntity<?> findMostRecentTweets()  {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(statServiceUrl + "recent", String.class);
    }

    private void getStatsInfo(@PathVariable Long id, GlobalEntity globalEntity, RestTemplate restTemplate) {
        try{
            Status status = restTemplate.getForObject(statServiceUrl + id, Status.class);
            globalEntity.setStatus(status);
        }catch(Exception ex){
            LOG.info("Error occured while retrieving status data" + ex.getMessage());
        }
    }

    private void getStreamInfo(@PathVariable Long id, GlobalEntity globalEntity, RestTemplate restTemplate) {
        try{
            ResponseEntity<Tweet[]> tweetStreams =restTemplate.getForEntity(streamServiceUrl + id, Tweet[].class);
            if(tweetStreams!=null && tweetStreams.getBody() != null){
                Tweet[] tweets = tweetStreams.getBody();
                globalEntity.setStream(Arrays.asList(tweets));
            }
        }catch (Exception ex){
            LOG.info("Error occured while retrieving profile data" + ex.getMessage());
        }
    }

    private void getUserProfileInfo(@PathVariable Long id, GlobalEntity globalEntity, RestTemplate restTemplate) {
        try{
            UserProfile profile = restTemplate.getForObject(profileServiceUrl + id, UserProfile.class);
            globalEntity.setProfile(profile);
        }catch(Exception ex){
            LOG.info("Error occured while retrieving profile data" + ex.getMessage());
        }
    }

    /*
     *
     * Routing for Profile Service
     *
     * */

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> findProfileByUserId(@PathVariable Long userId)  {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(profileServiceUrl + userId, String.class);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfile userProfile)  {
        try{
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<UserProfile> request = new HttpEntity<>(userProfile);
            UserProfile updatedProfile = restTemplate.postForObject(profileServiceUrl + "update", request, UserProfile.class);
            return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     *
     * Routing for Stream Service
     *
     * */

    @GetMapping("/streams/{userId}")
    public ResponseEntity<?> findTweetStreamByUserId(@PathVariable Long userId)  {
        try{
            List<Tweet> tweetList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Tweet[]> tweetStreams = restTemplate.getForEntity(streamServiceUrl + userId, Tweet[].class);
            if (tweetStreams != null && tweetStreams.getBody() != null) {
                Tweet[] tweets = tweetStreams.getBody();
                tweetList = Arrays.asList(tweets);
            }
            if(!CollectionUtils.isEmpty(tweetList))
                return new ResponseEntity<>(tweetList, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/streams")
    public ResponseEntity<?> createStream(@RequestBody Tweet tweet)  {
        try{
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Tweet> request = new HttpEntity<>(tweet);
            Tweet newTweet = restTemplate.postForObject(streamServiceUrl, request, Tweet.class);
            return new ResponseEntity<>(newTweet, HttpStatus.CREATED);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     *
     * Routing for Stats Service
     *
     * */
    @GetMapping("/stats/{userId}")
    public ResponseEntity<?> findTweetStatsByUserId(@PathVariable Long userId)  {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(statServiceUrl + userId, String.class);
    }

}
