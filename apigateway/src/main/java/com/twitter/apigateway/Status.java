package com.twitter.apigateway;

public class Status {

    private int totalTweetCount;
    private int tweetCountUpTo12AM;
    private int tweetCountUpTo12PM;

    public int getTotalTweetCount() {
        return totalTweetCount;
    }

    public void setTotalTweetCount(int totalTweetCount) {
        this.totalTweetCount = totalTweetCount;
    }

    public int getTweetCountUpTo12AM() {
        return tweetCountUpTo12AM;
    }

    public void setTweetCountUpTo12AM(int tweetCountUpTo12AM) {
        this.tweetCountUpTo12AM = tweetCountUpTo12AM;
    }

    public int getTweetCountUpTo12PM() {
        return tweetCountUpTo12PM;
    }

    public void setTweetCountUpTo12PM(int tweetCountUpTo12PM) {
        this.tweetCountUpTo12PM = tweetCountUpTo12PM;
    }
}
