package com.twitter.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/stream")
public class StreamController {

    private StreamService service;

    @Autowired
    public StreamController(StreamService streamService){
        this.service = streamService;
    }

    /*
     * returns the most recent tweets: the last 10 tweets
     * */
    @GetMapping("/all")
    public ResponseEntity<?> findAllTweets() {
        try{
            List<Tweet> allTweets = service.findAllTweets();
            if(!CollectionUtils.isEmpty(allTweets)){
                return new ResponseEntity<>(allTweets, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
            List<Tweet> allTweets = service.findAllTweets();
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

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllByUserId(@PathVariable Long id) {
        try{
            List<Tweet> tweetList = service.findAllByUserId(id);
            if(!CollectionUtils.isEmpty(tweetList))
                return new ResponseEntity<>(tweetList, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody Tweet tweet) {
        try{
            Tweet newTweet = service.createStream(tweet);
            return new ResponseEntity<>(newTweet, HttpStatus.CREATED);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
