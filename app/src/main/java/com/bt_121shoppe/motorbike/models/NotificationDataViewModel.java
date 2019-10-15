package com.bt_121shoppe.motorbike.models;

public class NotificationDataViewModel {
    private int icon;
    private String userId;
    private String body;
    private String title;
    private String sentTo;
    private String notiType;
    private String datetime;
    private boolean isSeen;

    public NotificationDataViewModel(){}
    public NotificationDataViewModel(int icon,String userId,String body,String title,String notiType,String datetime,boolean isSeen){
        this.icon=icon;
        this.userId=userId;
        this.body=body;
        this.title=title;

        this.notiType=notiType;
        this.datetime=datetime;
        this.isSeen=isSeen;
    }

    public int getIcon() {
        return icon;
    }

    public String getBody() {
        return body;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getNotiType() {
        return notiType;
    }

    public String getSentTo() {
        return sentTo;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public String getUserId() {
        return userId;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
