package com.dhruvam.popularmovies.activity;

import android.animation.ValueAnimator;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.BottomSheetTabAdapter;
import com.dhruvam.popularmovies.adapter.SimilarListAdapter;
import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.dhruvam.popularmovies.databinding.ActivityMovieDescriptionBinding;
import com.dhruvam.popularmovies.executor.AppExecutor;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.tools.ResizableCustomView;
import com.dhruvam.popularmovies.view_model.FavouriteMovieByIdViewModelFactory;
import com.dhruvam.popularmovies.view_model.FavouriteMovieViewModelById;
import com.dhruvam.popularmovies.view_model.OfflineMovieByIdViewModel;
import com.dhruvam.popularmovies.view_model.OfflineMovieByIdViewModelFactory;
import com.squareup.picasso.Picasso;


public class MovieDescriptionActivity extends AppCompatActivity {

    private static ActivityMovieDescriptionBinding binding;
    String mImageQuality;
    private static final int MAX_LINES =2;
    static MovieResponse mResponse;
    static SimilarListAdapter adapter;
    private String MOVIE_URL;
    private int movieId;
    static Context context;
    static MovieEntity movieEntity;
    FloatingActionButton button;

    private boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_description);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));


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

        final String image_url = getResources().getString(R.string.thumbnail_url);
        binding.lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLikeAnimation();
            }
        });

        button = binding.addToFavouritesBtn;


        assert binding.trailerReviewBs != null;
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(binding.trailerReviewBs.bottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //binding.trailerReviewBs.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });



        binding.trailerReviewBs.tabLayout.addTab(binding.trailerReviewBs.tabLayout.newTab().setText("Reviews"));
        binding.trailerReviewBs.tabLayout.addTab(binding.trailerReviewBs.tabLayout.newTab().setText("Trailers"));
        binding.trailerReviewBs.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        binding.trailerReviewBs.tabLayout.setBackgroundResource(R.drawable.cut_shape_background);

        PagerAdapter adapter = new BottomSheetTabAdapter(getSupportFragmentManager(), binding.trailerReviewBs.tabLayout.getTabCount());
        binding.trailerReviewBs.pager.setAdapter(adapter);
        binding.trailerReviewBs.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener( binding.trailerReviewBs.tabLayout));
        binding.trailerReviewBs.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.trailerReviewBs.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        Log.d("movieID", movieId+"");



        final OfflineMovieByIdViewModelFactory factory = new OfflineMovieByIdViewModelFactory(OfflineMovieAccessDatabase.getInstance(this), movieId);
        final OfflineMovieByIdViewModel model = ViewModelProviders.of(this, factory).get(OfflineMovieByIdViewModel.class);

        final LiveData<MovieEntity> entity = model.getMovieById();


            entity.observe(this, new Observer<MovieEntity>() {
                @Override
                public void onChanged(@Nullable MovieEntity result) {

                    entity.removeObserver(this);

                    movieEntity = result;

                    if (result == null ) {
                        GetFavouritesByIdAndSetUp();
                    } else {

                        setUpActivityMore(movieEntity);
                    }



                }
            });



    }


    private void GetFavouritesByIdAndSetUp() {

        FavouriteMovieByIdViewModelFactory modelFactory = new FavouriteMovieByIdViewModelFactory(OfflineMovieAccessDatabase.getInstance(getApplicationContext()), movieId);
        FavouriteMovieViewModelById viewModelById = ViewModelProviders.of(this, modelFactory).get(FavouriteMovieViewModelById.class);

        final LiveData<FavouriteMovies> movies = viewModelById.getFavouriteMovieById();
        movies.observe(this, new Observer<FavouriteMovies>() {
            @Override
            public void onChanged(@Nullable FavouriteMovies favouriteMovies) {

                movies.removeObserver(this);

                movieEntity = MovieEntity.getMovieEntityFromFavourite(favouriteMovies);

                setUpActivityMore(movieEntity);


            }
        });

    }


    private void setUpActivityMore(MovieEntity result) {
        checkIfFavouriteAndSetupActivity();

        String image_url = getResources().getString(R.string.thumbnail_url);
        Picasso.with(getApplicationContext()).load(image_url + mImageQuality + movieEntity.getBackdropPath()).into(binding.posterImageIv);

        binding.movieTitleTv.setText(movieEntity.getTitle());

        binding.movieReleaseDateTv.setText(movieEntity.getReleaseDate());
        binding.movieRatingTv.setText(movieEntity.getVoteAverage()+"");

        checkIfFavouriteAndSetupActivity();


        // Network setup and call
        NetworkUtils.init(getApplicationContext());
        NetworkUtils.getHttpResponseForSimilarMovies(movieEntity.getId());

        NetworkUtils.getReviewsForMovie(movieEntity.getId());
        NetworkUtils.getTrailorsForMovie(movieEntity.getId());


        binding.movieDescriptionTv.setText(result.getOverview());
        binding.languageTv.setText(result.getOriginalLanguage());
        binding.voteCountTv.setText(result.getVoteCount()+"");


        ResizableCustomView.doResizeTextView(binding.movieDescriptionTv, MAX_LINES, "View More", true);



        binding.addToFavouritesBtn.setOnClickListener( (view) -> {
            if( !isFavourite ) {

                addToFavourites();

            } else {
                FavouriteMovies movies = FavouriteMovies.getObjectModelFromData(movieEntity);
                deleteFromFavourites(movies);
            }
        });


        /* setting upbase URL */
        MOVIE_URL = getResources().getString(R.string.base_url);

                /*Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        binding.backgroundForDetailViewSv.setBackground(new BitmapDrawable(getResources(), BlurBuilder.blur(context, bitmap)));

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };*/

        //Picasso.with(context).load(image_url + mImageQuality + movieEntity.getPosterPath()).into(target);






    }


    private void startLikeAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
        animator.addUpdateListener((valueAnimator -> {
            binding.lottieAnimationView.setProgress((Float) valueAnimator.getAnimatedValue());
        }));

        if (binding.lottieAnimationView.getProgress() == 0f) {
            animator.start();
        } else {
            binding.lottieAnimationView.setProgress(0f);
        }
    }



    private void checkIfFavouriteAndSetupActivity() {


        AppExecutor.getInstance().diskIO().execute(() -> {
            FavouriteMovies movie = OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getDao().getMovieById(movieId);
            if(movie == null) {
                isFavourite = false;
            } else
                isFavourite = true;

            runOnUiThread(() -> {
                if( isFavourite ) {
                    button.setImageResource(R.drawable.heart_selected);
                } else
                    button.setImageResource(R.drawable.heart_unselected);
            });
        } );
    }




    /**
     * Method for conversion of object model to data model and then passing to
     * the favourite movie database.
     */
    public void addToFavourites() {
        //Acquiring database instance and passing context.
        //Then through the abstract method moviesDAO(), adding a movie on button click.


        AppExecutor.getInstance().diskIO().execute( () -> {

            FavouriteMovies favouriteMovie = FavouriteMovies.getObjectModelFromData(movieEntity);
            OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getDao().addMovie(favouriteMovie);

            runOnUiThread( () -> {

                button.setImageResource(R.drawable.heart_selected);
                isFavourite = true;

                Log.e("checking for fab", "added to favourites");
            });

        });


    }


    /**
     * Deleting a movie from the database
     * @param movie
     */
    public void deleteFromFavourites(final FavouriteMovies movie) {

        AppExecutor.getInstance().diskIO().execute(() -> {
            OfflineMovieAccessDatabase.getInstance(getApplicationContext()).getDao().deleteMovie(movie);
            runOnUiThread( () -> {
                button.setImageResource(R.drawable.heart_unselected);
                isFavourite = false;

                Log.e("checking for fab", "added to favourites");
            });
        }


        );

    }


    /* ---------------- Helper Methods ---------------- */


    /**
     * Recieve movie data from network request from NetworkUtils Class
     * @param response
     */
    public static void receiveMovies(MovieResponse response) {
        mResponse = response;
        //adapter.switchAdapter(response);
    }







    public MovieEntity getMovieEntity() {
        return movieEntity;
    }

    public static void setProgressVsisiblity(String flag) {
        /*if(flag.equals(context.getResources().getString(R.string.network_request_started))) {
            binding.loadingPb.setVisibility(View.VISIBLE);
        }
        else if(flag.equals(context.getResources().getString(R.string.network_request_finished))) {
            binding.loadingPb.setVisibility(View.GONE);
        }*/
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
