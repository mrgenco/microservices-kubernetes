package com.twitter.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Value("${service.stream}")
    private String streamServiceUrl;



    @GetMapping("/{id}")
    public ResponseEntity<?> getStatsByUser(@PathVariable Long id)  {
        try{
            Status stats = findStatsByUserId(id);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * returns the most recent tweets: the last 10 tweets
     * */
    @GetMapping("/recent")
    public ResponseEntity<?> findMostRecentTweets() {
        try{
            List<Tweet> allTweets = getTweetsFromTweetService(streamServiceUrl + "all");
            if(!CollectionUtils.isEmpty(allTweets)){
                if(allTweets.size() >= 10){
                    List<Tweet> mostRecentTweets = allTweets.subList(allTweets.size() - 10, allTweets.size());
                    return new ResponseEntity<>(mostRecentTweets, HttpStatus.OK);
                }
                else
                    return new ResponseEntity<>(allTweets, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Status findStatsByUserId(Long userid) throws Exception {
        List<Tweet> tweetList = getTweetsFromTweetService(streamServiceUrl + userid);
        Status status = new Status();
        status.setTotalTweetCount(tweetList.size());
        int tweetCountUpTo12AM = 0;
        int tweetCountUpTo12PM = 0;
        for(Tweet tw : tweetList){
            if(tw.getTweetDateStr().endsWith("AM"))
                tweetCountUpTo12AM++;
            if(tw.getTweetDateStr().endsWith("PM"))
                tweetCountUpTo12PM++;
        }
        status.setTweetCountUpTo12AM(tweetCountUpTo12AM);
        status.setTweetCountUpTo12PM(tweetCountUpTo12PM);

        return status;
    }


    // calling stream service to get all tweets
    private List<Tweet> getTweetsFromTweetService(String url) {
        List<Tweet> allTweets = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Tweet[]> tweetStreams = restTemplate.getForEntity(url, Tweet[].class);
        if (tweetStreams != null && tweetStreams.getBody() != null) {
            Tweet[] tweets = tweetStreams.getBody();
            allTweets = Arrays.asList(tweets);
        }
        return allTweets;
    }
}
