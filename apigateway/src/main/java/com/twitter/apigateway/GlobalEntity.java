package com.twitter.apigateway;

import java.util.List;

public class GlobalEntity {
    private List<Tweet> stream;
    private UserProfile profile;
    private Status status;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Tweet> getStream() {
        return stream;
    }

    public void setStream(List<Tweet> stream) {
        this.stream = stream;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

}
