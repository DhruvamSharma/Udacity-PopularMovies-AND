package com.dhruvam.popularmovies.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.MainGridAdapter;
import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.databinding.ActivityMainBinding;
import com.dhruvam.popularmovies.executor.AppExecutor;
import com.dhruvam.popularmovies.fragments.BottomSheetFragment;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;

import java.util.ArrayList;
import java.util.List;

public class MovieGridActivity extends AppCompatActivity {


    static MovieResponse mResponse;
    private static ActivityMainBinding mBinding;
    static MainGridAdapter adapter;
    static Context context;
    private static BottomSheetFragment bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        context = this;

        /* Network setup and call */
        NetworkUtils.init(this);
        NetworkUtils.getHttpResponse();

        /* Setting up recycler View */
        adapter = new MainGridAdapter(this);
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

    /* -------- HELPER METHODS --------- */

    /* Menu Inflating and methods */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_menu: {
                showBottomSheetDialog(BottomSheetFragment.getContextForSheet());
                return true;
            }
            case R.id.favourite_list : {
                //On click of Favourite Movies
                getAllFavourites();
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /* Utitlity Methods */

    public static void receiveData(MovieResponse response) {
        mResponse = response;
        adapter.switchAdapter(response);
    }

    /**
     * Method to show or hide the skeleton loading screen that
     * appears at the entry point
     * @param flag
     */
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


    /**
     * getAllFavourites method to get the favourites movies with the help of Executors
     * and then calling the switch adapter method to change the data in the activity
     */
    public void getAllFavourites() {
        //Get list of movies
        final ArrayList<List<MovieResponse.Result>> entityList = new ArrayList<List<MovieResponse.Result>>();
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                entityList.add(OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getDao().getFavouriteMovieList());
            }
        });

        //creating movie response object
        MovieResponse movieResponse = new MovieResponse();
        List<MovieResponse.Result> results = new ArrayList<>();
        //adding movies data to the list
        if(entityList.size() != 0) {
            entityList.get(0).addAll(results);
            movieResponse.setResults(results);
            //switching adapter to reset data in the activity
            adapter.switchAdapter(movieResponse);
        } else {
            //TODO Handle empty screen here!
            adapter.switchAdapter(null);
        }


    }



    /**
     *  BottomSheet Opening and Closing methods
     *  @param v
     */
    public void showBottomSheetDialog(View v) {
        bottomSheetDialogFragment =  new BottomSheetFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }

    public static void hideBottomSheetDialog(View v) {
        bottomSheetDialogFragment.dismiss();
    }
}
