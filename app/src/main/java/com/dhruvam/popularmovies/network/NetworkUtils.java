package com.dhruvam.popularmovies.network;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.dhruvam.popularmovies.BuildConfig;
import com.dhruvam.popularmovies.activity.MovieGridActivity;
import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.activity.MovieDescriptionActivity;
import com.dhruvam.popularmovies.fragments.BottomSheetFragment;
import com.dhruvam.popularmovies.pojo.MovieResponse;

/**
 * Created by dell on 05-06-2018.
 */

public class NetworkUtils {

    private static String MOVIE_URL;
    private static String API_Key;

    private static MovieResponse[] mResponse = new MovieResponse[1];
    private static Context mContext;


    public static void init(Context context) {
        AndroidNetworking.initialize(context);
        mContext = context;
         /* setting upbase URL */
        MOVIE_URL = context.getResources().getString(R.string.base_url);
        API_Key = BuildConfig.API_KEY;
    }

    public static void getHttpResponse() {

        MovieGridActivity.setLoadingScreenVisibility(mContext.getResources().getString(R.string.network_request_started));

        AndroidNetworking.get(MOVIE_URL+API_Key)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(MovieResponse.class, new ParsedRequestListener<MovieResponse>() {
                    @Override
                    public void onResponse(MovieResponse response) {
                        //MovieGridActivity.hideLoading();
                        mResponse[0] = response;
                        MovieGridActivity.receiveData(mResponse[0]);
                        MovieGridActivity.setLoadingScreenVisibility(mContext.getResources().getString(R.string.network_request_finished));
                    }

                    @Override
                    public void onError(ANError anError) {
                        /* handle error situation */
                        mResponse[0] = null;
                        MovieGridActivity.setLoadingScreenVisibility(mContext.getResources().getString(R.string.network_request_finished));
                    }

                });

    }

    public static void getHttpResponseForSimilarMovies(int id) {

        MovieDescriptionActivity.setProgressVsisiblity(mContext.getResources().getString(R.string.network_request_started));

        AndroidNetworking.get(mContext.getResources().getString(R.string.url_for_similar_movies)+id+"/similar?api_key="+API_Key)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(MovieResponse.class, new ParsedRequestListener<MovieResponse>() {
                    @Override
                    public void onResponse(MovieResponse response) {
                        mResponse[0] = response;
                        MovieDescriptionActivity.receiveData(mResponse[0]);
                        MovieDescriptionActivity.setProgressVsisiblity(mContext.getResources().getString(R.string.network_request_finished));
                    }

                    @Override
                    public void onError(ANError anError) {
                        /* handle error situation */
                        mResponse[0] = null;
                        MovieDescriptionActivity.setProgressVsisiblity(mContext.getResources().getString(R.string.network_request_finished));
                    }

                });

    }

    public static void getHTTPResponseForSorted(String tag) {

        BottomSheetFragment.setProgressVsisiblity(mContext.getResources().getString(R.string.network_request_started));
        MovieGridActivity.setLoadingScreenVisibility(mContext.getResources().getString(R.string.network_request_started));

        String movie_url = null;
        if( tag.equals(mContext.getResources().getString(R.string.top_rated_label))) {
            movie_url = mContext.getResources().getString(R.string.url_for_top_rated);
        } else if (tag.equals(mContext.getResources().getString(R.string.most_popular_label))) {
            movie_url = mContext.getResources().getString(R.string.url_for_most_popular);
        } else if (tag.equals(mContext.getResources().getString(R.string.upcoming_label))) {
            movie_url = mContext.getResources().getString(R.string.url_for_upcoming);
        } else if (tag.equals(mContext.getResources().getString(R.string.latest_label))) {
            movie_url = mContext.getResources().getString(R.string.url_for_latest);
        } else if (tag.equals(mContext.getResources().getString(R.string.now_playing_label))) {
            movie_url = mContext.getResources().getString(R.string.url_for_now_playing);
        }

        AndroidNetworking.get(movie_url+API_Key)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(MovieResponse.class, new ParsedRequestListener<MovieResponse>() {
                    @Override
                    public void onResponse(MovieResponse response) {
                        mResponse[0] = response;
                        MovieGridActivity.receiveData(mResponse[0]);
                        MovieGridActivity.setLoadingScreenVisibility(mContext.getResources().getString(R.string.network_request_finished));
                        BottomSheetFragment.setProgressVsisiblity(mContext.getResources().getString(R.string.network_request_finished));
                        BottomSheetFragment.hideBottomSheet(BottomSheetFragment.getContextForSheet());
                    }

                    @Override
                    public void onError(ANError anError) {
                        /* handle error situation */
                        mResponse[0] = null;
                        BottomSheetFragment.setProgressVsisiblity(mContext.getResources().getString(R.string.network_request_finished));
                        MovieGridActivity.setLoadingScreenVisibility(mContext.getResources().getString(R.string.network_request_finished));
                        BottomSheetFragment.hideBottomSheet(BottomSheetFragment.getContextForSheet());
                    }
                });
    }


}
