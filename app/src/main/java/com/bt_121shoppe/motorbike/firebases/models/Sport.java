package com.bt_121shoppe.motorbike.firebases.models;

import com.google.gson.annotations.SerializedName;

public class Sport {
    @SerializedName("imageUrl")
    private String mImageUrl;
    @SerializedName("info")
    private String mInfo;
    @SerializedName("subTitle")
    private String mSubTitle;
    @SerializedName("title")
    private String mTitle;

    public Sport(){}
    public Sport(String mImageUrl,String mInfo,String mSubTitle,String mTitle){
        this.mImageUrl=mImageUrl;
        this.mInfo=mInfo;
        this.mSubTitle=mSubTitle;
        this.mTitle=mTitle;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmInfo() {
        return mInfo;
    }

    public String getmSubTitle() {
        return mSubTitle;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }

    public void setmSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
