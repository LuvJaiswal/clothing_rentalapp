package com.example.islingtonclothingapplication.Remote;

import com.example.islingtonclothingapplication.model.APIResponse;
import com.example.islingtonclothingapplication.model.Banner;

import java.util.List;
//import io.reactivex.Observable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyAPI {

    @FormUrlEncoded
    @POST("login.php")
            Call<APIResponse> loginUser(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("register.php")
    Call<APIResponse> registerUser(@Field("name") String name,@Field("email") String email, @Field("password") String password,  @Field("phone") String phone);


    @GET("getbanner.php")
    Observable<List<Banner>> getBanners();



//    @GET("getcategory.php")
//    Observable<List<Categor>> getBanners();

}
