package com.bt_121shoppe.lucky_app.Api;

import okhttp3.MediaType;

public class ConsumeAPI {
    //testing server
//    public static final String BASE_URL="http://103.205.26.103:8000/";
//    public static final String IMAGE_STRING_PATH=BASE_URL+"media/post_images/";
//    public static final boolean IS_PRODUCTION=false;
//    public static final String FB_POST="postssit";
//    public static final String FB_CHAT="chatssit";

    //productin server
    public static final String BASE_URL="http://121shoppe.com/";
    public static final String IMAGE_STRING_PATH=BASE_URL+"static/media/post_images/";
    public static final boolean IS_PRODUCTION=true;
    public static final String FB_POST="posts";
    public static final String FB_CHAT="chats";

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
}
