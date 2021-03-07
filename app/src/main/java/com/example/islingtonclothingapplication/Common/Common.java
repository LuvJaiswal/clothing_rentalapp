package com.example.islingtonclothingapplication.Common;

import com.example.islingtonclothingapplication.Database.DataSource.CartRepository;
import com.example.islingtonclothingapplication.Database.Local.CartDatabase;
import com.example.islingtonclothingapplication.Database.ModelDB.Cart;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.Remote.RetrofitClient;
import com.example.islingtonclothingapplication.model.Category;
import com.example.islingtonclothingapplication.model.Clothes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class Common {
    public static final String BASE_URL = "http://192.168.100.63/rentalcloth/";

    public static Category currentCategory=null;

    public static final String TOPPING_CLOTHES_ID = "4";
    public static List<Clothes> toppingList = new ArrayList<>();

    public static double topPrice = 0.0;
    public static List<String>toppingAdded = new ArrayList<>();

    //Hold field
    public static int daysfor_rent = -1;


    //Database
    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;




    public static IMyAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }
}
