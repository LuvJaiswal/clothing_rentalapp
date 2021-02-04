package com.example.islingtonclothingapplication.model;

public class APIResponse {
        private boolean error;
        private String uid;
        private User user;
        private String error_msg;


    public APIResponse() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(
            boolean error) {
        this.error = error;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}

