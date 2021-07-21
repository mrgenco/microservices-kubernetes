package com.twitter.stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class StreamService {

    // constant variables for streams data
    private static final String STREAMS = "streams";
    private static final String ID = "ID:";
    private static final String USERID = "USERID:";
    private static final String TWEETBODY = "TWEETBODY:";
    private static final String TWEETDATE = "TWEETDATE:";

    // datasource destination as environment variables
    @Value("${datasource.path}")
    private String dataSourcePath;

    Tweet createStream(Tweet tweet) throws IOException {
        StringBuffer inputBuffer = new StringBuffer();
        inputBuffer.append(USERID).append(tweet.getUserId());
        inputBuffer.append('\n');
        tweet.setId(UUID.randomUUID());
        inputBuffer.append(ID).append(tweet.getId());
        inputBuffer.append('\n');
        inputBuffer.append(TWEETBODY).append(tweet.getTweetbody());
        inputBuffer.append('\n');
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        Date newDate = new Date();
        String tweetDate = sdf.format(newDate);
        tweet.setTweetDate(newDate);
        inputBuffer.append(TWEETDATE).append(tweetDate);
        inputBuffer.append('\n');
        inputBuffer.append("-----------------------------");
        inputBuffer.append('\n');
        // append new tweet to streams file
        FileOutputStream fileOut = new FileOutputStream(dataSourcePath + STREAMS, true);
        fileOut.write(inputBuffer.toString().getBytes(), 0, inputBuffer.toString().length());
        fileOut.close();

        return tweet;

    }

    List<Tweet> findAllByUserId(Long userid) throws Exception {
        List<Tweet> tweetList = new ArrayList<>();
        BufferedReader br = getBufferedReaderForStreams();
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
            if(currentUserId == userid){
                if(line.startsWith(ID))
                    tweet.setId(UUID.fromString(line.substring(line.lastIndexOf(ID) + 3)));
                else if(line.startsWith(TWEETBODY))
                    tweet.setTweetbody(line.substring(line.lastIndexOf(TWEETBODY) + 10));
                else if(line.startsWith(TWEETDATE)){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
                    Date tweetDate = sdf.parse(line.substring(line.lastIndexOf(TWEETDATE) + 10));
                    tweet.setTweetDate(tweetDate);
                }
            }
        }
        br.close();
        return tweetList;
    }

    List<Tweet> findAllTweets() throws Exception {
        List<Tweet> tweetList = new ArrayList<>();
        BufferedReader br = getBufferedReaderForStreams();
        String line;
        Tweet tweet = null;
        while ((line = br.readLine()) != null){
            if(line.startsWith(USERID)){
                tweet = new Tweet();
                tweet.setUserId(Long.parseLong(line.substring(line.lastIndexOf(ID) + 3)));
                tweetList.add(tweet);
            }
            else if(line.startsWith(ID))
                tweet.setId(UUID.fromString(line.substring(line.lastIndexOf(ID) + 3)));
            else if(line.startsWith(TWEETBODY))
                tweet.setTweetbody(line.substring(line.lastIndexOf(TWEETBODY) + 10));
            else if(line.startsWith(TWEETDATE)){
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
                String tweetDateStr = line.substring(line.lastIndexOf(TWEETDATE) + 10);
                tweet.setTweetDateStr(tweetDateStr);
            }
        }
        br.close();
        return tweetList;
    }

    private BufferedReader getBufferedReaderForStreams() throws FileNotFoundException {
        File file = new File(dataSourcePath + STREAMS);
        return new BufferedReader(new FileReader(file));
    }
}
