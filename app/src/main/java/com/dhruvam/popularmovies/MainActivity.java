package com.dhruvam.popularmovies;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;

import com.dhruvam.popularmovies.adapter.MainGridAdapter;
import com.dhruvam.popularmovies.databinding.ActivityMainBinding;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;

public class MainActivity extends AppCompatActivity {


    static MovieResponse mResponse;
    private static ActivityMainBinding mBinding;
    static MainGridAdapter adapter;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupWindowAnimations();
        context = this;

        /* Network setup and call */
        NetworkUtils.init(this);
        NetworkUtils.getHttpResponse();

        /* Setting up recycler View */
        adapter = new MainGridAdapter(getApplicationContext());
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),3);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
           @Override
           public int getSpanSize(int position) {
               if(position == 0) {
                   return 3;
               }
               else {
                   return 1;
               }
           }
        });

        mBinding.movieListRv.setAdapter(adapter);
        mBinding.movieListRv.setLayoutManager(manager);


    }

    public static void receiveData(MovieResponse response) {
        mResponse = response;
        adapter.switchAdapter(response);
    }

    private void setupWindowAnimations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(slide);
        }
    }

    public static void setLoadingScreenVisibility(String flag) {
        if(flag.equals(context.getResources().getString(R.string.network_request_started))) {
            mBinding.movieListRv.setVisibility(View.GONE);
            mBinding.shimmerText.setVisibility(View.VISIBLE);
            mBinding.shimmerText.startShimmerAnimation();
        }
        else if(flag.equals(context.getResources().getString(R.string.network_request_finished))) {
            mBinding.movieListRv.setVisibility(View.VISIBLE);
            mBinding.shimmerText.setVisibility(View.GONE);
            mBinding.shimmerText.stopShimmerAnimation();
        }
    }



}
