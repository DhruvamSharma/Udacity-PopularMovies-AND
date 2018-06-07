package com.dhruvam.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.Movie;

public class MainActivity extends AppCompatActivity {

    private String MOVIE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkUtils.init(getApplicationContext());
        Movie data = NetworkUtils.getHttpResponse(MOVIE_URL+"5d5b4588dc71d0e50c29f2caa9221fa0");
        if (data != null) {
            Toast.makeText(getApplicationContext(), data.getPage().toString(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_SHORT).show();
        }

    }
}
