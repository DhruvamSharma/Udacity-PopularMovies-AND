package com.dhruvam.popularmovies.network;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dhruvam.popularmovies.pojo.Movie;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by dell on 05-06-2018.
 */

public class NetworkUtils {

    public static void init(Context context) {
        AndroidNetworking.initialize(context);
    }

    public static Movie getHttpResponse(String url) {
        final Movie[] result = new Movie[1];
        AndroidNetworking.get(url)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result[0] = parseJson(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        result[0] = null;
                    }

                });


        return result[0];

    }

    private static Movie parseJson(JSONObject object) {
        if (object == null) {
            Log.e("error_null","empty");
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(object.toString(), Movie.class);
    }

}
