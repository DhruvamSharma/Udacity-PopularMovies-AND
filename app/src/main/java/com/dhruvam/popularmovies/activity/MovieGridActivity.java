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
import com.dhruvam.popularmovies.databinding.ActivityMainBinding;
import com.dhruvam.popularmovies.fragments.BottomSheetFragment;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieResponse;

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

    public static void receiveData(MovieResponse response) {
        mResponse = response;
        adapter.switchAdapter(response);
    }

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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showBottomSheetDialog(View v) {
        bottomSheetDialogFragment =  new BottomSheetFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }

    public static void hideBottomSheetDialog(View v) {
        bottomSheetDialogFragment.dismiss();
    }
}
