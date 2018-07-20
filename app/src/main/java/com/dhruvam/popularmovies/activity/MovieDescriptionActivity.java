package com.dhruvam.popularmovies.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.SimilarListAdapter;
import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.dhruvam.popularmovies.databinding.ActivityMovieDescriptionBinding;
import com.dhruvam.popularmovies.executor.AppExecutor;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieReviews;
import com.dhruvam.popularmovies.pojo.MovieTrailors;
import com.dhruvam.popularmovies.tools.ResizableCustomView;
import com.dhruvam.popularmovies.view_model.FavouriteMovieByIdViewModel;
import com.dhruvam.popularmovies.view_model.FavouriteMovieByIdViewModelFactory;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MovieDescriptionActivity extends AppCompatActivity {

    private static ActivityMovieDescriptionBinding binding;
    String mImageQuality;
    private static final int MAX_LINES =2;
    static MovieResponse mResponse;
    static SimilarListAdapter adapter;
    private String MOVIE_URL;
    private int movieId;
    static Context context;
    MovieEntity movieEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_description);

        mImageQuality = getResources().getString(R.string.thumbnail_quality_6);
        context = this;
        Intent intent = getIntent();

        //Extracting movie movieId from the intent from MovieGridActivity
        if(intent.hasExtra(getPackageName())) {

            movieId = intent.getIntExtra(getPackageName(), 0);
        }

        setUpActivity(movieId);


    }

    private void setUpActivity(int movieId) {

        final FavouriteMovieByIdViewModelFactory factory = new FavouriteMovieByIdViewModelFactory(OfflineMovieAccessDatabase.getInstance(this), movieId);
        final FavouriteMovieByIdViewModel model = ViewModelProviders.of(this, factory).get(FavouriteMovieByIdViewModel.class);

        final LiveData<MovieEntity> entity = model.getMovieById();
        entity.observe(this, new Observer<MovieEntity>() {
            @Override
            public void onChanged(@Nullable MovieEntity result) {

                //TODO (10) Activity is being created again and again on rotation even when there is ViewModel used
                Log.e(getPackageName(), "calling description from database");
                entity.removeObserver(this);

                movieEntity = result;

                String image_url = getResources().getString(R.string.thumbnail_url);
                Picasso.with(getApplicationContext()).load(image_url + mImageQuality + result.getBackdropPath()).into(binding.headerLayout.mainImageBackdrop);
                binding.headerLayout.movieTitleTv.setText(result.getTitle());
                binding.movieDescriptionTv.setText(result.getOverview());
                binding.headerLayout.movieReleaseDateTv.setText(result.getReleaseDate());
                binding.headerLayout.movieRatingTv.setText(result.getVoteAverage()+"");
                binding.languageTv.setText(result.getOriginalLanguage());
                binding.voteCountTv.setText(result.getVoteCount()+"");

                /* expandable textview */
                ResizableCustomView.doResizeTextView(binding.movieDescriptionTv, MAX_LINES, "View More", true);

                //TODO (9) Use this instead of ApplicationContext producing error. Why?
                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
                adapter = new SimilarListAdapter(getApplicationContext());
                binding.similarListRv.setLayoutManager(manager);
                binding.similarListRv.setAdapter(adapter);
                binding.addToFavouritesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addToFavourites();
                    }
                });


                /* setting upbase URL */
                MOVIE_URL = getResources().getString(R.string.base_url);
            }
        });





        // TODO(12) Manage these calls and save them on the view model such that they are executed inside view model

         /* Network setup and call */
        NetworkUtils.init(getApplicationContext());
        NetworkUtils.getHttpResponseForSimilarMovies(movieId);

        NetworkUtils.getReviewsForMovie(movieId);
        NetworkUtils.getTrailorsForMovie(movieId);
    }


    /**
     * Method for conversion of object model to data model and then passing to
     * the favourite movie database.
     */
    public void addToFavourites() {
        //Acquiring database instance and passing context.
        //Then through the abstract method moviesDAO(), adding a movie on button click.


        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavouriteMovies favouriteMovie = FavouriteMovies.getObjectModelFromData(movieEntity);
                OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getDao().addMovie(favouriteMovie);
            }
        });


    }


    /**
     * Deleting a movie from the database
     * @param movie
     */
    public void deleteFromFavourites(final FavouriteMovies movie) {

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getDao().deleteMovie(movie);
            }
        });
    }





    /* ---------------- Helper Methods ---------------- */


    /**
     * Recieve movie data from network request from NetworkUtils Class
     * @param response
     */
    public static void receiveMovies(MovieResponse response) {
        mResponse = response;
        adapter.switchAdapter(response);
    }

    /**
     * Method called when the activity loads and
     * Recieves MovieResponse for display
     * @param reviews
     */
    public static void recieveReviews(MovieReviews reviews) {
        Toast.makeText(context, reviews.toString(),Toast.LENGTH_SHORT).show();
    }


    /**
     * Recieve trailors for the current movie displayed
     * @param response
     */
    public static void recieveTrailors(MovieTrailors response) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.e("here","here");
        if(intent.hasExtra(getPackageName())) {

            movieId = Parcels.unwrap(intent.getBundleExtra(getPackageName()).getParcelable(getPackageName()));
        }
        setUpActivity(movieId);
    }

    public static void setProgressVsisiblity(String flag) {
        if(flag.equals(context.getResources().getString(R.string.network_request_started))) {
            binding.loadingPb.setVisibility(View.VISIBLE);
        }
        else if(flag.equals(context.getResources().getString(R.string.network_request_finished))) {
            binding.loadingPb.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                startActivity(new Intent(MovieDescriptionActivity.this, MovieGridActivity.class));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
