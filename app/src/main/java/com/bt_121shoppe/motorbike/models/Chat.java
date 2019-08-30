package com.bt_121shoppe.motorbike.models;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private boolean isseen;
    private String post;
    private String type;

    public Chat(){}

    public Chat(String sender,String receiver,String message,String post,String type,boolean isseen){
        this.sender=sender;
        this.receiver=receiver;
        this.message=message;
        this.isseen=isseen;
        this.post=post;
        this.type=type;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public String getType() {
        return type;
    }

    public String getPost() {
        return post;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
