package com.dhruvam.popularmovies.fragments;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.activity.MovieDescriptionActivity;
import com.dhruvam.popularmovies.adapter.SimilarListAdapter;
import com.dhruvam.popularmovies.database.entity.MovieEntity;
import com.dhruvam.popularmovies.databinding.FragmentTabFragmentTwoBinding;
import com.dhruvam.popularmovies.network.NetworkUtils;
import com.dhruvam.popularmovies.pojo.MovieTrailors;

public class TabFragmentTwo extends Fragment {

    static SimilarListAdapter adapter;
    static MovieEntity movieEntity;
    static MovieDescriptionActivity activity;
    private static Context mContext;

    private static FragmentTabFragmentTwoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_tab_fragment_two, container,false);

        View view = binding.getRoot();

        mContext = getContext();

        activity = (MovieDescriptionActivity) getActivity();


        //TODO (9) Use this instead of ApplicationContext producing error. Why?
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new SimilarListAdapter(view.getContext());

        binding.similarListRv.setAdapter(adapter);
        binding.similarListRv.setLayoutManager(manager);




        //Adding Snapping functionality to the recycler view
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.similarListRv);





        return view;
    }

    /**
     * Recieve trailors for the current movie displayed
     * @param response
     */
    public static void recieveTrailors(MovieTrailors response) {

        movieEntity = activity.getMovieEntity();

        if(response.getResults().size() == 0 || response == null) {
            binding.similarListRv.setVisibility(View.GONE);
            binding.errorTextFragmentTwoTv.setVisibility(View.VISIBLE);
        } else {
            binding.errorTextFragmentTwoTv.setVisibility(View.GONE);
            binding.similarListRv.setVisibility(View.VISIBLE);
            if (movieEntity != null)
                adapter.switchAdapter(response, movieEntity.getBackdropPath());
        }

    }


}
