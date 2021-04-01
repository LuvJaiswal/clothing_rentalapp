package com.example.islingtonclothingapplication.Database.DataSource;

import com.example.islingtonclothingapplication.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {

    Flowable<List<Cart>> getCartItems();

    Flowable<List<Cart>> getCartItemByID(int cartItemId);

    int countCartItems();

    float sumPrice();

    void emptyCart();

    void insertToCart(Cart... carts);

    void updateCart(Cart... carts);

    void deletecartItem(Cart cart);




}
