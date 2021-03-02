package com.example.islingtonclothingapplication.Common;

import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.Remote.RetrofitClient;

import retrofit2.Retrofit;

public class Common {
    public static final String BASE_URL = "http://192.168.100.204/rentalcloth/";

    public static IMyAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }
}
