package com.dhruvam.popularmovies.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.SimilarListAdapter;
import com.dhruvam.popularmovies.databinding.ActivityMovieDescriptionBinding;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.dhruvam.popularmovies.tools.ResizableCustomView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class MovieDescriptionActivity extends AppCompatActivity {

    private ActivityMovieDescriptionBinding binding;
    String mImageQuality;
    private static final int MAX_LINES =1;
    static MovieResponse mResponse;
    static SimilarListAdapter adapter;
    private String MOVIE_URL;
    MovieResponse.Result result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_description);

        mImageQuality = getResources().getString(R.string.thumbnail_quality_6);

        Intent intent = getIntent();


        if(intent.hasExtra(getPackageName())) {

            result = Parcels.unwrap(intent.getBundleExtra(getPackageName()).getParcelable(getPackageName()));
        }

        setUpActivity(result);


    }

    private void setUpActivity(final MovieResponse.Result result) {


        String image_url = getResources().getString(R.string.thumbnail_url);

        Picasso.with(this).load(image_url + mImageQuality + result.getBackdropPath()).into(binding.mainImageBackdrop);
        binding.movieTitleTv.setText(result.getTitle());
        binding.movieDescriptionTv.setText(result.getOverview());
        binding.movieReleaseDateTv.setText(result.getReleaseDate());
        binding.movieRatingTv.setText(result.getVoteAverage()+"");

        ResizableCustomView.doResizeTextView(binding.movieDescriptionTv, MAX_LINES, "View More", true);

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
        adapter = new SimilarListAdapter(getApplicationContext());
        binding.similarListRv.setLayoutManager(manager);
        binding.similarListRv.setAdapter(adapter);

        /* setting upbase URL */
        MOVIE_URL = getResources().getString(R.string.base_url);

        String genre_ids = getGenreIdList(result.getGenreIds());

         /* Network setup and call */
        NetworkUtils.init(getApplicationContext());
        NetworkUtils.getHttpResponseForSimilarMovies(MOVIE_URL+getResources().getString(R.string.api_key)+"&with_genres="+genre_ids);
    }

    private String getGenreIdList(List<Integer> list) {
        return list.toString();
    }


    public static void receiveData(MovieResponse response) {
        mResponse = response;
        adapter.switchAdapter(response);
    }

}
