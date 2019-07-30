package com.bt_121shoppe.lucky_app.models;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private boolean isseen;

    public Chat(){}

    public Chat(String sender,String receiver,String message,boolean isseen){
        this.sender=sender;
        this.receiver=receiver;
        this.message=message;
        this.isseen=isseen;
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
}
