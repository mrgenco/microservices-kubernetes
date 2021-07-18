package com.twitter.stream;

import java.util.Date;

public class Tweet {

    private Long id;
    private Long userId;
    private String tweetbody;
    private Date tweetDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
