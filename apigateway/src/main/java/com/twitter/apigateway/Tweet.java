package com.twitter.apigateway;

import java.util.Date;
import java.util.UUID;

public class Tweet {

    private UUID id;
    private Long userId;
    private String tweetbody;
    private Date tweetDate;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTweetbody() {
        return tweetbody;
    }

    public void setTweetbody(String tweetbody) {
        this.tweetbody = tweetbody;
    }

    public Date getTweetDate() {
        return tweetDate;
    }

    public void setTweetDate(Date tweetDate) {
        this.tweetDate = tweetDate;
    }
}
