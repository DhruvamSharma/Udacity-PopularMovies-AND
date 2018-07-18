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
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.dhruvam.popularmovies.fragments.BottomSheetFragment;
import com.dhruvam.popularmovies.pojo.MovieReviews;
import com.dhruvam.popularmovies.pojo.MovieTrailors;

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

    /**
     * his method is used to generate list of movies to present on the first screen that
     * user sees.
     */
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

    /**
     * This method returns a list of movies that are similar to the particular movie id
     * This list of movie is shown in the MovieDescriptionActivity
     * @param id
     */
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
                        MovieDescriptionActivity.receiveMovies(mResponse[0]);
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

    /**
     * This method generates a list of movies after the option from the
     * Bottomsheet has been selected. There are various option for the
     * different movie sorting options and so there are various urls for the same.
     *
     * For that particular url, this method returns a list of various movies.
     * @param tag
     */
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
        } else if( tag.equals(mContext.getResources().getString(R.string.favourite_movie_label))) {
            //FavouriteMoviesDatabase.getDatabase(mContext).getFavouritesDao().getFavouriteMovieList();
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

    /**
     * This method recieves a movie id and search for the reviews of this
     * particular movie
     * @param movieId
     */
    public static void getReviewsForMovie(int movieId) {

        String url = mContext.getResources().getString(R.string.url_for_similar_movies);
        AndroidNetworking.get(url+movieId+"/reviews?api_key="+API_Key)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(MovieReviews.class, new ParsedRequestListener<MovieReviews>() {
                    @Override
                    public void onResponse(MovieReviews response) {
                        MovieDescriptionActivity.recieveReviews(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        /* handle error situation */
                    }
                });
    }


    /**
     * This methods recieves a movie id and search for the trailors of that
     * particular movie
     * @param movieId
     */
    public static void getTrailorsForMovie(int movieId) {

        String url = mContext.getResources().getString(R.string.url_for_similar_movies);
        AndroidNetworking.get(url+movieId+"/videos?api_key="+API_Key)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(MovieTrailors.class, new ParsedRequestListener<MovieTrailors>() {
                    @Override
                    public void onResponse(MovieTrailors response) {
                        MovieDescriptionActivity.recieveTrailors(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        /* handle error situation */
                    }
                });
    }


}
