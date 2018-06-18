package com.dhruvam.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.dhruvam.popularmovies.adapter.MainGridAdapter;
import com.dhruvam.popularmovies.databinding.ActivityMainBinding;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;

public class MainActivity extends AppCompatActivity {

    private String MOVIE_URL;
    static MovieResponse mResponse;
    private ActivityMainBinding mBinding;
    static MainGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar myToolbar = getSupportActionBar();

        /* setting upbase URL */
        MOVIE_URL = getResources().getString(R.string.base_url);

        /* Network setup and call */
        NetworkUtils.init(getApplicationContext());
        NetworkUtils.getHttpResponse(MOVIE_URL+getResources().getString(R.string.api_key));

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

}
