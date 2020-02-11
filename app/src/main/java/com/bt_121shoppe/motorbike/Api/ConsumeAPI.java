package com.bt_121shoppe.motorbike.Api;

import okhttp3.MediaType;

public class ConsumeAPI {

    //testing server\

    public static final String BASE_URL="http://103.205.26.103:8000/";
    public static final String BASE_URL_IMG="http://103.205.26.103:8000";
    public static final String IMAGE_STRING_PATH=BASE_URL+"media/post_images/";
    public static final boolean IS_PRODUCTION=false;
    public static final String FB_POST="postssit";
    public static final String FB_CHAT="chatssit";
    public static final String FB_Notification="notificationssit";
    public static final String PREFIX_EMAIL="user121";

    //productin server
//    public static final String BASE_URL="http://121shoppe.com/";
//    public static final String BASE_URL_IMG="http://121shoppe.com";
//    public static final String IMAGE_STRING_PATH=BASE_URL+"static/media/post_images/";
//    public static final boolean IS_PRODUCTION=true;
//    public static final String FB_POST="posts";
//    public static final String FB_CHAT="chats";
//    public static final String FB_Notification="notifications";
//    public static final String PREFIX_EMAIL="produser121";

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    public static final String DEFAULT_FIREBASE_PASSWORD_ACC="123456";
}
