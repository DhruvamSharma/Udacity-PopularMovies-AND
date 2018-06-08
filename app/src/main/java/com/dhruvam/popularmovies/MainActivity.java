package com.dhruvam.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dhruvam.popularmovies.adapter.MainGridAdapter;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;

public class MainActivity extends AppCompatActivity {

    private String MOVIE_URL;
    static MovieResponse mResponse;
    RecyclerView mMovieList;
    static MainGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* setting upbase URL */
        MOVIE_URL = getResources().getString(R.string.base_url);

        /* Network setup and call */
        NetworkUtils.init(getApplicationContext());
        NetworkUtils.getHttpResponse(MOVIE_URL+getResources().getString(R.string.api_key));

        /* Setting up recycler View */
        mMovieList = findViewById(R.id.movie_list_rv);
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

        mMovieList.setAdapter(adapter);
        mMovieList.setLayoutManager(manager);
    }

    public static void receiveData(MovieResponse response) {
        mResponse = response;
        adapter.switchAdapter(response);
    }

}
