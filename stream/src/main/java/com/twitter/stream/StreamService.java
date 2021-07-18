package com.twitter.stream;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/stream")
public class StreamService {

    private List<Tweet> tweetList = new ArrayList<>();

    @PostConstruct
    public void createInitialData(){
        Tweet newTweet1 = new Tweet();
        newTweet1.setId((long) 1);
        newTweet1.setUserId((long) 1);
        newTweet1.setTweetbody("This is an example tweet 1");
        newTweet1.setTweetDate(new Date());

        Tweet newTweet2 = new Tweet();
        newTweet2.setId((long) 2);
        newTweet2.setUserId((long) 1);
        newTweet2.setTweetbody("This is an example tweet 2");
        newTweet2.setTweetDate(new Date());

        Tweet newTweet3 = new Tweet();
        newTweet3.setId((long) 3);
        newTweet3.setUserId((long) 1);
        newTweet3.setTweetbody("This is an example tweet 3");
        newTweet3.setTweetDate(new Date());

        tweetList.add(newTweet1);
        tweetList.add(newTweet2);
        tweetList.add(newTweet3);
    }

    @GetMapping("/all/")
    public List<Tweet> findAll() {

        return tweetList;

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tweet create(@RequestBody Tweet tweet) {
        Tweet newTweet = new Tweet();
        newTweet.setUserId(tweet.getUserId());
        newTweet.setId((long) tweetList.size() + 1);
        newTweet.setTweetDate(new Date());
        newTweet.setTweetbody(tweet.getTweetbody());
        tweetList.add(newTweet);
        return newTweet;
    }


}
