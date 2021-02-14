package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.daimajia.slider.library.SliderAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.Banner;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.daimajia.slider.library.SliderLayout;

public class HomeActivity extends AppCompatActivity {

    SliderLayout sliderLayout;
    IMyAPI mService;

    //Rxjjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mService = Common.getAPI();

        sliderLayout= findViewById(R.id.slider) ;

        //Get banner

        getBannerImage();
    }

    private void getBannerImage() {
       compositeDisposable.add(mService.getBanners()
       .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<List<Banner>>() {
                   @Override
                   public void accept(List<Banner> banners) throws Exception {

                       displayImage(banners);
                   }
               }));
    }

    //ctrl+o


    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {

        HashMap<String,String> bannerMap = new HashMap<>();
        for(Banner item:banners)
            bannerMap.put(item.getName(),item.getLink());

        for (String name:bannerMap.keySet())
        {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                            .image(bannerMap.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderLayout.addSlider(textSliderView);

        }
    }
}
