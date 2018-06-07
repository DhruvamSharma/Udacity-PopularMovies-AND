package com.dhruvam.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dell on 05-06-2018.
 */

public class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.MovieAdapter> {

    @NonNull
    @Override
    public MovieAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MovieAdapter extends RecyclerView.ViewHolder {
        public MovieAdapter(View itemView) {
            super(itemView);
        }
    }
}
