package com.bt_121shoppe.lucky_app.Api;

import okhttp3.MediaType;

public class ConsumeAPI {
    //testing server
    public static final String BASE_URL="http://103.205.26.103:8000/";
    //productin server
    //public static final String BASE_URL="http://121shoppe.com/";
    //public static final String BASE_URL="http://ec2-3-130-236-205.us-east-2.compute.amazonaws.com/";

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
}
