package com.example.islingtonclothingapplication.Remote;

import com.example.islingtonclothingapplication.model.APIResponse;
import com.example.islingtonclothingapplication.model.Banner;
import com.example.islingtonclothingapplication.model.Category;
import com.example.islingtonclothingapplication.model.Clothes;

import java.sql.Struct;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IMyAPI {

    @FormUrlEncoded
    @POST("login.php")
    Call<APIResponse> loginUser(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("register.php")
    Call<APIResponse> registerUser(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("phone") String phone);


    @GET("getbanner.php")
    Observable<List<Banner>> getBanners();

    @GET("getcategory.php")
    Observable<List<Category>> getCategory();

    @FormUrlEncoded
    @POST("getclothes.php")
    Observable<List<Clothes>> getClothes(@Field("categoryid") String categoryID);

    @GET("getallclothes.php")
    Observable<List<Clothes>> getAllClothes();

    @FormUrlEncoded
    @POST("submitorder.php")
    Call<APIResponse> submitOrder(@Field("price") float orderPrice,
                                  @Field("orderDetail") String orderDetail,
                                  @Field("comment") String comment,
                                  @Field("address") String address,
                                  @Field("phone") String phone);

    @Multipart
    @POST("server/category/upload_category_img")
    Call<String>uploadCategoryFile(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("server/category/add_category.php")
    Observable<String> addNewCategory(@Field("name") String name, @Field("imgPath") String imgPath);


    @FormUrlEncoded
    @POST("server/category/update_category.php")
    Observable<String>updateCategory(@Field("id") String id,
                                      @Field("name") String name,
                                      @Field("imgPath") String imgPath);

    @FormUrlEncoded
    @POST("server/category/delete_category.php")
    Observable<String>deleteCategory(@Field("id") String id);


    @FormUrlEncoded
    @POST("server/product/add_product.php")
    Observable<String>addNewProduct(@Field("name") String name,
                                     @Field("imgPath") String imgPath,
                                     @Field("price")String price,
                                     @Field("categoryId")String categoryId);


    @FormUrlEncoded
    @POST("server/product/delete_clothes.php")
    Observable<String>deleteProduct(@Field("id") String id);


    @Multipart
    @POST("server/product/upload_product_img")
    Call<String>uploadProductFile(@Part MultipartBody.Part file);

}
