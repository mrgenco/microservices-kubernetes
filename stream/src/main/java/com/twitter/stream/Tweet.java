package com.twitter.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.UUID;

public class Tweet {

    private UUID id;
    private Long userId;
    private String tweetbody;
    @JsonIgnore
    private Date tweetDate;
    private String tweetDateStr;


    public String getTweetDateStr() {
        return tweetDateStr;
    }

    public void setTweetDateStr(String tweetDateStr) {
        this.tweetDateStr = tweetDateStr;
    }

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
