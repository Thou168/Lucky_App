package com.bt_121shoppe.motorbike.models;

import com.google.gson.annotations.SerializedName;

public class NotificationViewModel {

    @SerializedName("id")
    private int id;
    @SerializedName("notify_type")
    private String notify;
    @SerializedName("reject_reason")
    private String reject_reason;
    @SerializedName("title")
    private String title;
    @SerializedName("is_seen")
    private boolean isSeen;
    @SerializedName("ref_id")
    private int ref_id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("created")
    private String datetime;

    public NotificationViewModel(){ }

    public NotificationViewModel(String notify,String reject_reason,String title, Boolean isSeen, int ref_id,String userId,String datetime){
        this.notify=notify;
        this.reject_reason=reject_reason;
        this.title=title;
        this.isSeen=isSeen;
        this.ref_id=ref_id;
        this.userId=userId;
        this.datetime=datetime;
    }

    public String getNotify() {
        return notify;
    }

    public int getRef_id() {
        return ref_id;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public String getDatatime() {
        return datetime;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public boolean getisSeen() {
        return isSeen;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setisSeen(boolean seen) {
        isSeen = seen;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public void setRef_id(int ref_id) {
        this.ref_id = ref_id;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public void setDatatime(String datetime) {
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
