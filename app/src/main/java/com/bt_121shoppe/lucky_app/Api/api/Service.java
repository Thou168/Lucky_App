package com.bt_121shoppe.lucky_app.Api.api;

import com.bt_121shoppe.lucky_app.Api.api.model.Item;
import com.bt_121shoppe.lucky_app.Api.api.model.Item_loan;
import com.bt_121shoppe.lucky_app.Api.api.model.change_status_delete;
import com.bt_121shoppe.lucky_app.Api.api.model.change_status_unlike;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("/api/v1/renewaldelete/{id}/")
    Call<change_status_delete> getputStatus(@Path("id") int id, @Body change_status_delete change_status, @Header("Authorization") String authorization);

    @PATCH("/api/v1/renewaldelete/{id}/")
    Call<change_status_delete> getpathStatus(@Path("id") int id, @Body change_status_delete change_status, @Header("Authorization") String authorization);

//    @GET("/allposts/?page=")
//    Call<AllResponse> getAllPost(@Query("page") String page);
//    @GET("/bestdeal/")
//    Call<AllResponse> getBestdeal();
    @GET("/countview/?post=")
    Call<AllResponse> getCount(@Query("post") String post, @Header("Authorization") String authorization);
    @GET("/detailposts/{id}/")
    Call<Item> getDetailpost(@Path("id") String id, @Header("Authorization") String authorization);

    @GET("/postbyuser/?status=")
    Call<AllResponse> getPostbyuser(@Header("Authorization") String authorization);
    @GET("/posybyuserhistory/?status=2")
    Call<AllResponse> getpostbyhistory(@Header("Authorization") String authorization);
    @GET("/likebyuser/")
    Call<AllResponse> getLikebyuser(@Header("Authorization") String authorization);
    @GET("/loanbyuser/?loan_status=9")
    Call<AllResponse> getLoanbyuser(@Header("Authorization") String authorization);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("/like/{id}/")
    Call<change_status_unlike> getputStatusUnlike(@Path("id") int id, @Body change_status_unlike change_status, @Header("Authorization") String authorization);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("/api/v1/loan/{id}/")
    Call<Item_loan> getputcancelloan(@Path("id") int id, @Body Item_loan item_loan, @Header("Authorization") String authorization);
    @GET("/loanbyuserhistory/")
    Call<AllResponse> getloanhistory(@Header("Authorization") String authorization);

    @GET("/api/v1/userfilter/?last_name=&username=")
    Call<AllResponse> getUsername(@Query("username") String username);
//Buy_Rent_Sell
    @GET("/relatedpost/?post_type=rent&category=2&modeling=&min_price=&max_price=")
    Call<AllResponse> getRent_vehicle();
    @GET("/relatedpost/?post_type=rent&category=1&modeling=&min_price=&max_price=")
    Call<AllResponse> getRent_eletronic();

    @GET("/relatedpost/?post_type=sell&category=2&modeling=&min_price=&max_price=")
    Call<AllResponse> getSell_vihicle();
    @GET("/relatedpost/?post_type=sell&category=1&modeling=&min_price=&max_price=")
    Call<AllResponse> getSell_eletronic();

    @GET("/relatedpost/?post_type=buy&category=2&modeling=&min_price=&max_price=")
    Call<AllResponse> getBuy_vihicle();
    @GET("/relatedpost/?post_type=buy&category=1&modeling=&min_price=&max_price=")
    Call<AllResponse> getBuy_eletronic();

    @GET("/api/v1/categories/")
    Call<AllResponse> getCategories();
    @GET("/api/v1/brands/")
    Call<AllResponse> getBrands();
    @GET("/api/v1/years/")
    Call<AllResponse> getYear();

}
