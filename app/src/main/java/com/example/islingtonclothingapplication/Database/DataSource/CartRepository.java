package com.example.islingtonclothingapplication.Database.DataSource;

import com.example.islingtonclothingapplication.Database.Local.CartDAO;
import com.example.islingtonclothingapplication.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {
    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource) {

        if (instance == null)
            instance = new CartRepository(iCartDataSource);
        return instance;
    }


    @Override
    public Flowable<List<Cart>> getCartItems() {
        return iCartDataSource.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemByID(int cartItemId) {
        return iCartDataSource.getCartItemByID(cartItemId);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public float sumPrice() {
        return iCartDataSource.sumPrice();
    }

    @Override
    public void emptyCart() {

        iCartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        iCartDataSource.updateCart(carts);
    }

    @Override
    public void deletecartItem(Cart cart) {

        iCartDataSource.deletecartItem(cart);
    }
}
