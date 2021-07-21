package com.twitter.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Value("${datasource.path}")
    private String dataSourcePath;

    // constant variables for streams data
    private static final String STREAMS = "streams";
    private static final String ID = "ID:";
    private static final String USERID = "USERID:";
    private static final String TWEETBODY = "TWEETBODY:";
    private static final String TWEETDATE = "TWEETDATE:";



    @GetMapping("/{id}")
    public ResponseEntity<?> findUserProfile(@PathVariable Long id)  {
        try{
            Status stats = findStatsByUserId(id);
            if(stats != null)
                return new ResponseEntity<>(stats, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Status findStatsByUserId(Long userid) throws Exception {
        List<Tweet> tweetList = new ArrayList<>();
        File file = new File(dataSourcePath + STREAMS);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Tweet tweet = null;
        Long currentUserId = null;
        while ((line = br.readLine()) != null){
            if(line.startsWith(USERID)){ // new tweet of given user found
                tweet = new Tweet();
                currentUserId = Long.parseLong(line.substring(line.lastIndexOf(ID) + 3));
                if(currentUserId == userid){
                    tweet.setUserId(currentUserId);
                    tweetList.add(tweet);
                }
            }
            if(userid.equals(currentUserId)){
                if(line.startsWith(ID))
                    tweet.setId(UUID.fromString(line.substring(line.lastIndexOf(ID) + 3)));
                else if(line.startsWith(TWEETBODY))
                    tweet.setTweetbody(line.substring(line.lastIndexOf(TWEETBODY) + 10));
                else if(line.startsWith(TWEETDATE)){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
                    String tweetDateStr = line.substring(line.lastIndexOf(TWEETDATE) + 10);
                    tweet.setTweetDateStr(tweetDateStr);
                }
            }
        }
        br.close();

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
}
