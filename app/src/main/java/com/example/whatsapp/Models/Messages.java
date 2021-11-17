package com.example.whatsapp.Models;

public class Messages {
    String uID, message, messageId;
    Long timestamp;
    public Messages(String uID, String message) {
        this.uID = uID;
        this.message = message;
    }


    public Messages(String uID, String message, Long timestamp) {
        this.uID = uID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Messages(){

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
