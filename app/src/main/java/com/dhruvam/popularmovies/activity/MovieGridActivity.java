package com.dhruvam.popularmovies.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.MainGridAdapter;
import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;
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
        fetchMovieList();
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
        fetchFromDatabase();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void fetchFromDatabase() {

        // TODO (3) List mapping to different type of objects through Stream API
        Log.e(getPackageName(), "data from database");
        final MovieResponse response = new MovieResponse();
        final LiveData<List<MovieEntity>> list = OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getMovieDao().getAllMovies();
        list.observe( this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntity> movieEntities) {
                Log.e(getPackageName(), " updated data from database");
                response.setResults(MovieResponse.Result.getObjectModelFromAllMoviesData(movieEntities));
                adapter.switchAdapter(response);
            }
        });

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


    public void fetchMovieList() {
        /* Network setup and call */
        Log.e(getPackageName(), "from network");
        NetworkUtils.init(this);
        NetworkUtils.getHttpResponse();
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

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<FavouriteMovies> entityList = new ArrayList<>();
                //final List<FavouriteMovies> synchronisedList = Collections.synchronizedList(entityList);
                entityList.addAll(OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getDao().getFavouriteMovieList());
                final List<MovieResponse.Result> list = MovieResponse.Result.getObjectModelFromFavouritesData(entityList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MovieResponse response = new MovieResponse();
                        response.setResults(list);
                        adapter.switchAdapter(response);
                    }
                });

            }
        });



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
