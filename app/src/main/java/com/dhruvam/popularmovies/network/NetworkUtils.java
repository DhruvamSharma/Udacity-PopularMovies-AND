package com.dhruvam.popularmovies.network;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.dhruvam.popularmovies.MainActivity;
import com.dhruvam.popularmovies.pojo.MovieResponse;

/**
 * Created by dell on 05-06-2018.
 */

public class NetworkUtils {

    private static MovieResponse[] mResponse = new MovieResponse[1];


    public static void init(Context context) {
        AndroidNetworking.initialize(context);
    }

    public static void getHttpResponse(String url) {
        AndroidNetworking.get(url)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(MovieResponse.class, new ParsedRequestListener<MovieResponse>() {
                    @Override
                    public void onResponse(MovieResponse response) {
                        mResponse[0] = response;
                        MainActivity.receiveData(mResponse[0]);
                    }

                    @Override
                    public void onError(ANError anError) {
                        /* handle error situation */
                    }

                });

    }


}
