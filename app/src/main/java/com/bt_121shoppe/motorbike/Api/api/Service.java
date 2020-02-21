package com.bt_121shoppe.motorbike.Api.api;

import com.bt_121shoppe.motorbike.Api.Profile;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.model.Brand;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.Api.api.model.Modeling;
import com.bt_121shoppe.motorbike.Api.api.model.Slider;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_delete;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_post;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_unlike;
import com.bt_121shoppe.motorbike.Api.api.model.dealershop;
import com.bt_121shoppe.motorbike.Api.api.model.detail_shop;
import com.bt_121shoppe.motorbike.Api.responses.APIShopResponse;
import com.bt_121shoppe.motorbike.Api.responses.APIStorePostResponse;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.loan.model.Province;
import com.bt_121shoppe.motorbike.loan.model.District;
import com.bt_121shoppe.motorbike.loan.model.Commune;
import com.bt_121shoppe.motorbike.loan.model.Village;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.loan.model.Draft;
import com.bt_121shoppe.motorbike.models.BrandViewModel;
import com.bt_121shoppe.motorbike.models.CategoryViewModel;
import com.bt_121shoppe.motorbike.models.DealerPostViewModel;
import com.bt_121shoppe.motorbike.models.FilterConditionViewModel;
import com.bt_121shoppe.motorbike.models.ModelingViewModel;
import com.bt_121shoppe.motorbike.models.NotificationViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.StorePostViewModel;
import com.bt_121shoppe.motorbike.models.UserProfileModel;
import com.bt_121shoppe.motorbike.models.NotificationDataViewModel;
import com.bt_121shoppe.motorbike.models.YearViewModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.*;

public interface Service {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("api/v1/renewaldelete/{id}/")
    Call<change_status_delete> getputStatus(@Path("id") int id, @Body change_status_delete change_status, @Header("Authorization") String authorization);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1/postdealershop/")
    Call<dealershop> pushdealershop (@Body dealershop change_status);

    @GET("api/v1/postdealershop/?post=&shop=66")
    Call<APIShopResponse> getAllShop();
    //@Query("post") int post,@Query("shop") int shop

    @GET("api/v1/shop/?shop_province")
    Call<APIShopResponse> getProvince_name(@Query("shop_province") String shop_province);
    //?shop_province=19

    @GET("api/v1/shop/{id}/")
    Call<ShopViewModel> getDealerShop(@Path("id") int id);

    @PATCH("api/v1/renewaldelete/{id}/")
    Call<change_status_delete> getpathStatus(@Path("id") int id, @Body change_status_delete change_status, @Header("Authorization") String authorization);

    @GET("api/v1/users/{pk}/")
    Call<AllResponse> getProfile(@Path("pk") int pk, @Header("Authorization") String authorization);

    @GET("api/v1/users/{pk}/")
    Call<User> getProfile1(@Path("pk") int pk, @Header("Authorization") String authorization);

    @GET("api/v1/users/{pk}/")
    Call<User_Detail> getDetailUser(@Path("pk") int pk, @Header("Authorization") String authorization);
    //    @GET("/bestdeal/")
//    Call<AllResponse> getBestdeal();
    @GET("countview/?post=")
    Call<AllResponse> getCount(@Query("post") String post, @Header("Authorization") String authorization);
    @GET("countview/?post=")
    Call<AllResponse> getCount(@Query("post") String post);
    @GET("detailposts/{id}/")
    Call<Item> getDetailpost(@Path("id") int id, @Header("Authorization") String authorization);
    @GET("detailposts/{id}/")
    Call<Item> getDetailpost(@Path("id") int id);

    @PATCH("postbyuser/{id}/")
    Call<change_status_post> getMovehistory(@Path("id") int id, @Body change_status_post changeStatusPost, @Header("Authorization") String authorization);

    @GET("postbyuser/?status=")
    Call<AllResponse> getPostbyuser(@Header("Authorization") String authorization);
    @GET("postbyuser/{id}/")
    Call<Item> getPostItem(@Path("id") int id,@Header("Authorization") String authorization);
//    @GET("posybyuserhistory/?status=2")
//    Call<AllResponse> getpostbyhistory(@Header("Authorization") String authorization);
    @GET("posybyuserhistory/")
    Call<AllResponse> getpostbyhistory(@Header("Authorization") String authorization);
    @GET("likebyuser/")
    Call<AllResponse> getLikebyuser(@Header("Authorization") String authorization);
    @GET("loanbyuser/?loan_status=9")
    Call<AllResponse> getLoanbyuser(@Header("Authorization") String authorization);

    @GET("get_loan_draft_by_user/")
    Call<Draft> getList_draft(@Header("Authorization") String authorization);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("like/{id}/")
    Call<change_status_unlike> getputStatusUnlike(@Path("id") int id, @Body change_status_unlike change_status, @Header("Authorization") String authorization);
    //create loan
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1/loan/")
    Call<loan_item> setCreateLoan(@Body loan_item loanItem, @Header("Authorization") String authorization);
    @GET("api/v1/loan/{id_loan}/")
    Call<loan_item> getDeailLoan(@Path("id_loan") int id_loan, @Header("Authorization") String authorization);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("api/v1/loan/{id_loan}/")
    Call<loan_item> getEditLoan(@Path("id_loan") int id, @Body loan_item loanItem, @Header("Authorization") String authorization);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("api/v1/loan/{id}/")
    Call<Item_loan> getputcancelloan(@Path("id") int id, @Body Item_loan item_loan, @Header("Authorization") String authorization);

    @GET("loanbyuserhistory/")
    Call<AllResponse> getloanhistory(@Header("Authorization") String authorization);

    @GET("api/v1/userfilter/?last_name=&username=")
    Call<AllResponse> getUsername(@Query("username") String username);
    //Buy_Rent_Sell
    @GET("relatedpost/?post_type=rent&category=2&modeling=&min_price=&max_price=")
    Call<AllResponse> getRent_vehicle();
    @GET("relatedpost/?post_type=rent&category=1&modeling=&min_price=&max_price=")
    Call<AllResponse> getRent_eletronic();

    @GET("relatedpost/?post_type=sell&category=2&modeling=&min_price=&max_price=")
    Call<AllResponse> getSell_vihicle();
    @GET("relatedpost/?post_type=sell&category=1&modeling=&min_price=&max_price=")
    Call<AllResponse> getSell_eletronic();

    @GET("relatedpost/?post_type=buy&category=2&modeling=&min_price=&max_price=")
    Call<AllResponse> getBuy_vihicle();
    @GET("relatedpost/?post_type=buy&category=1&modeling=&min_price=&max_price=")
    Call<AllResponse> getBuy_eletronic();

    @GET("api/v1/categories/")
    Call<AllResponse> getCategories();
    @GET("api/v1/brands/{id}/")
    Call<AllResponse> getBrands(@Path("id") int id);
    @GET("api/v1/years/")
    Call<AllResponse> getYear();
    @GET("api/v1/provinces/")
    Call<AllResponse> getProvince();
    @GET("api/v1/provinces/{id}/")
    Call<Province> getProvince(@Path("id") int id);

    @GET("api/v1/district/")
    Call<District> getDistrict();
    @GET("api/v1/district/{id}/")
    Call<District> getDistrict(@Path("id") int id);

    @GET("api/v1/commune/")
    Call<Commune> getCommune();
    @GET("api/v1/commune/{id}/")
    Call<Commune> getCommune(@Path("id") int id);

    @GET("api/v1/village/")
    Call<Village> getVillage();
    @GET("api/v1/village/{id}/")
    Call<Village> getVillage(@Path("id") int id);

    @GET("api/v1/users/{id}/")
    Call<User> getuser(@Path("id")int id);
    @GET("api/v1/models/{modeling}/")
    Call<Modeling> getModeling(@Path("modeling") int modeling);
    @GET("api/v1/brands/{brand}/")
    Call<Brand> getBrand(@Path("brand") int brand);

    //added by Rith oct 01 2019
    @GET("relatedpost/?post_type=&category=&modeling=&min_price=&max_price=&year=")
    Call<APIResponse> getFilterResult(@Query("post_type") String post_type,@Query("category") String category,@Query("min_price") String min_price,@Query("max_price") String max_price,@Query("year") String year);

    @GET("relatedpost/?post_type=&category=&modeling=&min_price=&max_price=&year=")
    Call<APIResponse> getFilterResultwithModel(@Query("post_type") String post_type,@Query("category") String category,@Query("modeling")int modeling,@Query("min_price") String min_price,@Query("max_price") String max_price,@Query("year") String year);
    @GET("bestdeal/")
    Call<APIResponse> getPostBestDeal();
    @GET("allposts/")
    Call<APIResponse> getAllPosts();
    @GET("api/v1/years/")
    Call<YearViewModel> getYearFilter();
    @GET("api/v1/years/{id}/")
    Call<YearViewModel> getYearDetail(@Path("id") int id);
    @GET("api/v1/categories/")
    Call<CategoryViewModel> getCategoryFilter();
    @GET("api/v1/categories/{id}/")
    Call<CategoryViewModel> getCategoryDetail(@Path("id") int id);
    @GET("api/v1/brands/")
    Call<BrandViewModel> getBrandFilter();
    @GET("api/v1/brands/{id}/")
    Call<BrandViewModel> getBrandDetail(@Path("id") int id);
    @GET("api/v1/users/{pk}/")
    Call<UserResponseModel> getUserProfile(@Path("pk") int pk);
    @GET("postbyuserfilter/?created_by=&approved_by=&rejected_by=&modified_by=")
    Call<AllResponse> getActivePostbyuser(@Query("created_by") int created_by);

    @GET("api/v1/wallpaper/")
    Call<AllResponse> getSliderImage();
    @GET("api/v1/models/{modeling}/")
    Call<ModelingViewModel> getModelDetail(@Path("modeling") int modeling);

    @GET("api/v1/shop/")
    Call<APIShopResponse> GetStoreList();
    @GET("api/v1/shop/?shop_province=")
    Call<APIShopResponse> GetStoreList(@Query("shop_province") int shop_province);

    @GET("api/v1/postdealershop/?record_status=1&shop=")
    Call<APIStorePostResponse> GetStoreActivePost(@Query("shop") int shop);

    @GET("api/v1/postdealershop/?record_status=2&shop=")
    Call<APIStorePostResponse> GetStoreHistoryPost(@Query("shop") int shop);

    @GET("api/v1/postdealershop/?record_status=&shop=&post=")
    Call<APIStorePostResponse> GetStorePostItembyPost(@Query("post") int post);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("api/v1/shop/{id}/")
    Call<ShopViewModel> updateShopCountView(@Path("id") int id, @Body ShopViewModel shopViewModel);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("api/v1/postdealershop/{id}/")
    Call<StorePostViewModel> updateDealerPostStatus(@Path("id") int id, @Body StorePostViewModel shopViewModel);

    @GET("api/v1/notification/?user_id=")
    Call<NotificationViewModel> getNotification(@Query("user_id") int user_id, @Header("Authorization") String authorization);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("api/v1/notification/{id}")
    Call<NotificationViewModel> updateNotification(@Path("id") int id, @Body NotificationViewModel notificationViewModel);

}
