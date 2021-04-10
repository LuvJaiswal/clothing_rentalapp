package com.example.islingtonclothingapplication.Common;

import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.Remote.RetrofitClient;

public class CommonServer {
    public static final String BASE_URL = "http://10.0.2.2/clothrental_phpbackend/";

    public static IMyAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }
}
