package com.dhruvam.popularmovies.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.adapter.ReviewAdapter;
import com.dhruvam.popularmovies.databinding.FragmentTabFragmentOneBinding;
import com.dhruvam.popularmovies.pojo.MovieReviews;

public class TabFragmentOne extends Fragment {

    static ReviewAdapter mAdapter;
    static Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentTabFragmentOneBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_fragment_one,container, false);
        View view = binding.getRoot();

        mContext = getContext();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter = new ReviewAdapter();

        binding.reviewRv.setLayoutManager(manager);
        binding.reviewRv.setAdapter(mAdapter);

        binding.reviewRv.setHasFixedSize(true);


        return view;
    }

    /**
     * Method called when the activity loads and
     * Recieves MovieResponse for display
     * @param reviews
     */
    public static void recieveReviews(MovieReviews reviews) {

        mAdapter.switchAdapter(reviews);

    }
}
