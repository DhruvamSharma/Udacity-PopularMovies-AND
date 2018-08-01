package com.dhruvam.popularmovies.activity;

import android.app.ActionBar;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dhruvam.popularmovies.view_model.FavouriteMoviesViewModel;
import com.dhruvam.popularmovies.view_model.MainActivityViewModel;
import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.MainGridAdapter;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;
import com.dhruvam.popularmovies.databinding.ActivityMainBinding;
import com.dhruvam.popularmovies.fragments.BottomSheetFragment;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;


import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;


/**
 * MovieGrid activity acts as a starting point for PopularMovies (Anflix)
 */
public class MovieGridActivity extends AppCompatActivity {

    //mResponse is a reference of MovieResponse which is a POJO for MovieObjects


    //It is declared static because is was needed to be used in static method
    //TODO(6) Is this a right reason to declare the reference static: A variable to be used inside a static method so declared static?
    static MovieResponse mResponse;

    //ActivityBinding for avoiding various findViewById calls
    private static ActivityMainBinding mBinding;

    //Adapter reference for settingdata onto the main Activity
    //declared static for the reason of using it in the static methods
    static MainGridAdapter adapter;

    //Context declared static. Leaks possible.
    //TODO (7) how to avoid this: making a context static?
    static Context context;

    //Bottomsheet reference for showing and hiding the bottomsheet in the main activity (MovieGridActivity)
    private static BottomSheetFragment bottomSheetDialogFragment;

    //mResponse is a reference of MovieResponse which is a POJO for MovieObjects
    FavouriteMoviesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(getPackageName(),"I am in onCreate");

        //setting up the view for the activity and the binding reference
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        //Setting color to the action bar
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));




        //storing context for further use in static methods
        context = this;

        if(savedInstanceState == null) {
            //fetching movie list from the network and storing in the database for the first time
            fetchMovieList();
        }



        /* Setting up recycler View */
        //Creating the adapter and passing in the context as an argument
        adapter = new MainGridAdapter(this);

        //Creating Grid layout manager for the RecyclerView and setting 3 columns per row
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),3);

        //changing the span size dynamically to show the first movie object as the main
        //header movie and then changing the san size back to 3 for rest of the views
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

        //setting the adapter
        mBinding.movieListRv.setAdapter(adapter);

        //setting the layout manager
        mBinding.movieListRv.setLayoutManager(manager);


        //Setting up ViewModel
        //        setUpViewModel();


        //Getting instance of view model of this activity for saving the configuration changes
        viewModel = ViewModelProviders.of(this).get(FavouriteMoviesViewModel.class);
        viewModel.init(getApplicationContext());

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isIntantiated", true);
        Parcelable parcel = Parcels.wrap(mResponse.getResults());
        outState.putParcelable("adapterData", parcel);
        Log.e("onSaveInstanceState", "in onCreate");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Parcelable parcelable = savedInstanceState.getParcelable("adapterData");
        List<MovieResponse.Result> data= Parcels.unwrap(parcelable);
        MovieResponse response = new MovieResponse();
        response.setResults(data);
        adapter.switchAdapter(response);
        Log.e("onRestoreInstanceState", "here");
    }

    /**
     * A method to fetch movies from the database in the view model and recieving the object of LiveData type
     * This is done so as to save the configuration changes and notify whenever there is a change also in the view model so that there is
     * no memory leak even if we rotate the screen while there were changes in the database.
     */
    private void setUpViewModel() {


        //Logging for debugging issues
        Log.e(getPackageName(), "data from database");

        //new Movie response object created for converting Movie Response POJO into MovieEntity
        final MovieResponse response = new MovieResponse();

        //Getting an instance of ViewModel from ViewModel providers such that we can access the data
        //from the view model and avoiding excess database calls on configuration changes.
        MainActivityViewModel viewModelProvider = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        //A final list created to store all the data fetched from the view model. This data is stored as
        //cache of the view model and thus is saved throughout the lifecycle of the activity
        final LiveData<List<MovieEntity>> list = viewModelProvider.getAllMovieList();

        //observe method on the live data list to be observed. And thus calling method onChanged only when there is a change in the database
        //this is saving us from unnecessary database calls.
        list.observe( this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntity> movieEntities) {
                // logging done for debug issues
                Log.e(getPackageName(), " updated data from database");

                //converting entity back to pojo to be set in the adapter.
                 List<MovieResponse.Result> data = MovieResponse.Result.getObjectModelFromAllMoviesData(movieEntities);
                response.setResults(data);
                mResponse = response;
                // changing adapter data and notifying the adapter to refresh.
                adapter.switchAdapter(mResponse);
            }
        });

    }



    /* -------- HELPER METHODS --------- */

    /** Menu Inflating and methods
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Acquiring a menu inflater for inflating the menu
        MenuInflater inflater = getMenuInflater();

        //inflating the menu using the menu file we created and the menu object
        inflater.inflate(R.menu.filter, menu);

        //returning true for acceptance that we have inflated the menu
        return true;
    }

    /**
     * Overriden method for actions to be performed for retrieving the favourites movie list
     * from the database and to show the bottomsheet dialog
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //switch case for selecting the action on the buttons on the action bar
        switch (item.getItemId()) {
            case R.id.sort_menu: {

                //removing the observer when the observer has been set for the favourites
                //this is because, once the favourites button is clicked,the observer is triggered.
                //Then whenever there is a change in the favourites collection, the previous page(The movie grid activity) is provided with the
                //Favourites collection data and the previous data is removed.
                if( viewModel != null)
                removeFavouriteObserver();

                //showing bottomsheet by passing in the context of the bottom sheet
                showBottomSheetDialog();
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

    /**
     * Method created for recieving data from the NetworkUtils class
     * And is declared static because it needs to recieve data from an another class
     * @param response
     */
    public static void receiveData(MovieResponse response) {

        mResponse = response;

        if(response.getResults().size() == 0 || response ==null) {
            mBinding.movieListRv.setVisibility(View.GONE);
            mBinding.errorTextTv.setVisibility(View.VISIBLE);
        } else {

            mBinding.movieListRv.setVisibility(View.VISIBLE);
            mBinding.errorTextTv.setVisibility(View.GONE);
            adapter.switchAdapter(mResponse);
        }

    }





    /**
     * method to setup the Fast Android Networking library
     * and call to the API for fetching the movie list and storing it in the database
     */
    public void fetchMovieList() {
        /* Network setup and call */

        //logging done for debug purposes
        Log.e(getPackageName(), "from network");

        //initialising the FAN library
        NetworkUtils.init(this);

        //Making a GET request to the server for Movie list and storing it in the database
        NetworkUtils.getHttpResponse();
    }

    /**
     * Method to show or hide the skeleton loading screen animation that
     * appears at the point when there is a network request for movie list
     * @param flag
     */
    public static void setLoadingScreenVisibility(String flag) {
        //starting point
        if(flag.equals(context.getResources().getString(R.string.network_request_started))) {

            //hiding the recycler view when the network request is started
            mBinding.movieListRv.setVisibility(View.GONE);

            //showing the skeleton loader
            mBinding.shimmerText.setVisibility(View.VISIBLE);

            //starting the animation to show skeleton loading
            mBinding.shimmerText.startShimmerAnimation();
        }
        else if(flag.equals(context.getResources().getString(R.string.network_request_finished))) {
            //end point

            //showing the recycler view
            mBinding.movieListRv.setVisibility(View.VISIBLE);

            //hiding the shimmer text
            mBinding.shimmerText.setVisibility(View.GONE);

            //stopping the animation when the network request is done
            mBinding.shimmerText.stopShimmerAnimation();
        }
    }


    /**
     * getAllFavourites method to get the favourites movies with the help of Executors
     * and then calling the switch adapter method to change the data in the activity
     */
    public void getAllFavourites() {


        //using the viewmodel instance to get a livedata object of list of favourite movies
        //observing this list for any changes and then running the onChanged method on the UI thread
        viewModel.getFavouriteMovieList().observe(this, (List<FavouriteMovies> favouriteMovies) -> {

            //TODO (4) Notice the change in the database when deleting out of the favourites
            //Logging done for debug issues
            Log.e(getPackageName(), "fetching favourites from live data");
            //viewModel.getFavouriteMovieList().removeObserver(this);

            //converting the list of Favourite Movie enttity to Movie Response Object
            //TODO (5) To be done outside the UI thread and then notice the CPU and memory usage
            if(favouriteMovies != null) {

                if(favouriteMovies.size() == 0) {
                    mBinding.movieListRv.setVisibility(View.GONE);
                    mBinding.errorTextTv.setVisibility(View.VISIBLE);
                } else {


                }

            }
            List<MovieResponse.Result> list = MovieResponse.Result.getObjectModelFromFavouritesData(favouriteMovies);

            //Creating a movie Response object to store the results of the data fetched from the database and
            //creating a similar path for representing the UI
            MovieResponse response = new MovieResponse();
            response.setResults(list);

            mResponse = response;

            //Calling the switch adapter method of the Main Grid Adapter to change the data and
            //calling teh notifyDataSetChanged method to refresh the data on screen displayed through the adapter
            adapter.switchAdapter(response);

        });




    }


    /**
     * This method removes teh favourite Observer because it is triggered once it is called.
     * This observable then keeps looking for change. When it receives it, it will update the adapter and
     * All of the data gets corrupted.
     */
    public void removeFavouriteObserver(){
        final LiveData<List<FavouriteMovies>> observable =  viewModel.getFavouriteMovieList();
        if (observable != null && observable.hasObservers())
        observable.removeObservers(this);
    }





    /**
     *  BottomSheet Opening and Closing methods
     */
    public void showBottomSheetDialog() {

        //creating a bottom sheet everytime when we open the bottomsheet
        //bottomSheetDialogFragment =  new BottomSheetFragment();
        bottomSheetDialogFragment = BottomSheetFragment.getBottomSheetInstance();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }

    public static void hideBottomSheetDialog() {

        // hiding the bottom sheet when necessary
        bottomSheetDialogFragment.dismiss();


    }
}
