package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.islingtonclothingapplication.Adapter.CartAdapter;
import com.example.islingtonclothingapplication.Adapter.FavouriteAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Common.RecyclerItemTouchHelper;
import com.example.islingtonclothingapplication.Common.RecyclerItemTouchHelperListener;
import com.example.islingtonclothingapplication.Database.ModelDB.Cart;
import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.APIResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recycler_cart;
    Button btn_place_order;
    CompositeDisposable compositeDisposable;

    List<Cart> cartList = new ArrayList<>();

    CartAdapter cartAdapter;

    RelativeLayout rootLayout;

    IMyAPI mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();

        recycler_cart = (RecyclerView) findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_order = (Button) findViewById(R.id.btn_place_order);

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });


//        loadCartItems();

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        loadCartItems();

    }

    private void placeOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");

        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

        final EditText edt_comment = (EditText) submit_order_layout.findViewById(R.id.order_comment);
        final EditText edt_other_address = (EditText) submit_order_layout.findViewById(R.id.edt_other_address);

        final RadioButton rdi_user_address = (RadioButton) submit_order_layout.findViewById(R.id.rdi_user_address);

        final RadioButton rdi_other_address = (RadioButton) submit_order_layout.findViewById(R.id.rdi_other_address);

        //Event

        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    edt_other_address.setEnabled(false);

            }
        });

        rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    edt_other_address.setEnabled(true);

            }
        });

        builder.setView(submit_order_layout);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String orderComment = edt_comment.getText().toString();
                final String orderAddress;
                if (rdi_user_address.isChecked())
                    // orderAddress=Common.user.getEmail().toString(); //take its address later
                    // Toast.makeText(CartActivity.this, "add address  manually", Toast.LENGTH_SHORT).show();

//                    orderAddress = edt_other_address.getText().toString();

                    orderAddress = edt_other_address.getText().toString();

                else if (rdi_other_address.isChecked())
                    orderAddress = edt_other_address.getText().toString();

                else
                    orderAddress = "";


                //submit  order

                compositeDisposable.add(
                        Common.cartRepository.getCartItems()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<List<Cart>>() {
                                    @Override
                                    public void accept(List<Cart> carts) throws Exception {

                                        if (!TextUtils.isEmpty(orderAddress))
                                            sendOrderToServer(Common.cartRepository.sumPrice(),
                                                    carts,
                                                    orderComment, orderAddress);

                                        else
                                            Toast.makeText(CartActivity.this, "Order Address cant be null", Toast.LENGTH_SHORT).show();
                                    }
                                })
                );

            }


        });

        builder.show();

    }

    private void sendOrderToServer(float sumPrice, List<Cart> carts, String orderComment, String orderAddress) {

        if (carts.size() > 0) {
            String orderDetail = new Gson().toJson(carts);

            mService.submitOrder(sumPrice, orderDetail, orderComment, orderAddress, Common.user.getPhone())
                    .enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                            Toast.makeText(CartActivity.this, "Order Submitted", Toast.LENGTH_SHORT).show();

                            //clear  cart
                            Common.cartRepository.emptyCart();
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {

                            Log.e("ERROR", t.getMessage());
                        }
                    });
        }
    }


    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {

                                displayCartItem(carts);
                            }
                        })
        );
    }

    private void displayCartItem(List<Cart> carts) {

        cartList = carts;

        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;
            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            //Dlete item from

            cartAdapter.removeItem(deletedIndex);

            //Delete from roomdatabase

            Common.cartRepository.deletecartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append("removed from Cart list").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cartAdapter.restoreItem(deletedItem, deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}