package com.example.islingtonclothingapplication.Database.DataSource;

import com.example.islingtonclothingapplication.Database.Local.CartDAO;
import com.example.islingtonclothingapplication.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO) {

        if (instance == null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemByID(int cartItemId) {
        return null;
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public void emptyCart() {

        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {

        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {

        cartDAO.updateCart(carts);
    }

    @Override
    public void deletecartItem(Cart cart) {

        cartDAO.deletecartItem(cart);
    }
}
