package com.dhruvam.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.SimilarListAdapter;
import com.dhruvam.popularmovies.databinding.ActivityMovieDescriptionBinding;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.dhruvam.popularmovies.tools.ResizableCustomView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class MovieDescriptionActivity extends AppCompatActivity {

    private static ActivityMovieDescriptionBinding binding;
    String mImageQuality;
    private static final int MAX_LINES =2;
    static MovieResponse mResponse;
    static SimilarListAdapter adapter;
    private String MOVIE_URL;
    MovieResponse.Result result = null;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_description);

        mImageQuality = getResources().getString(R.string.thumbnail_quality_6);
        context = this;
        Intent intent = getIntent();


        if(intent.hasExtra(getPackageName())) {

            result = Parcels.unwrap(intent.getBundleExtra(getPackageName()).getParcelable(getPackageName()));
        }

        setUpActivity(result);


    }

    private void setUpActivity(final MovieResponse.Result result) {


        String image_url = getResources().getString(R.string.thumbnail_url);

        Picasso.with(this).load(image_url + mImageQuality + result.getBackdropPath()).into(binding.headerLayout.mainImageBackdrop);
        binding.headerLayout.movieTitleTv.setText(result.getTitle());
        binding.movieDescriptionTv.setText(result.getOverview());
        binding.headerLayout.movieReleaseDateTv.setText(result.getReleaseDate());
        binding.headerLayout.movieRatingTv.setText(result.getVoteAverage()+"");
        binding.languageTv.setText(result.getOriginalLanguage());
        binding.voteCountTv.setText(result.getVoteCount()+"");

        ResizableCustomView.doResizeTextView(binding.movieDescriptionTv, MAX_LINES, "View More", true);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        adapter = new SimilarListAdapter(this);
        binding.similarListRv.setLayoutManager(manager);
        binding.similarListRv.setAdapter(adapter);

        /* setting upbase URL */
        MOVIE_URL = getResources().getString(R.string.base_url);


         /* Network setup and call */
        NetworkUtils.init(getApplicationContext());
        NetworkUtils.getHttpResponseForSimilarMovies(result.getId());
    }


    public void addToFavourites() {

    }





    /* ---------------- Helper Methods ---------------- */



    public static void receiveData(MovieResponse response) {
        mResponse = response;
        adapter.switchAdapter(response);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.e("here","here");
        if(intent.hasExtra(getPackageName())) {

            result = Parcels.unwrap(intent.getBundleExtra(getPackageName()).getParcelable(getPackageName()));
        }
        setUpActivity(result);
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
