package com.istiaq66.chit.Models;

public class MessageModel {
    String Uid, message;
    Long timstamp;

    public MessageModel(String uid, String message, Long timstamp) {
        Uid = uid;
        this.message = message;
        this.timstamp = timstamp;
    }

    public MessageModel(String uid, String message) {
        Uid = uid;
        this.message = message;
    }

    public MessageModel()
    {

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimstamp() {
        return timstamp;
    }

    public void setTimstamp(Long timstamp) {
        this.timstamp = timstamp;
    }
}
