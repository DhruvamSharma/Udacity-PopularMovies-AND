package com.dhruvam.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.activity.MovieDescriptionActivity;
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.squareup.picasso.Picasso;

/**
 * Created by dell on 05-06-2018.
 */

public class MainGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MovieResponse mResponse;
    private Context mContext;
    private String mImageQuality;

    private static final int LAYOUT_HEADER = 52456;
    private static final int LAYOUT_GRID = 84562;

    public MainGridAdapter(Context context) {
        mResponse = null;
        mContext = context;
        mImageQuality = mContext.getResources().getString(R.string.thumbnail_quality_3);
    }

    public MainGridAdapter(MovieResponse response) {
        mResponse = response;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        switch(viewType) {
            case LAYOUT_HEADER:
                View view_header = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_thumbnail, parent,false);
                holder = new MovieAdapterViewHolderHeader(view_header);
                break;

            case LAYOUT_GRID:
                View view_grid = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_grid_thumbnail, parent,false);
                holder = new MovieAdapterViewHolderGrid(view_grid);
                break;

            /* Something to do with the default case */
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String image_url = mContext.getResources().getString(R.string.thumbnail_url);
        String headerQuality = mContext.getResources().getString(R.string.thumbnail_quality_6);
        if(mResponse != null){
            switch (holder.getItemViewType()) {

                case LAYOUT_HEADER:

                    MovieAdapterViewHolderHeader viewHolderHeader = (MovieAdapterViewHolderHeader) holder;
                    Picasso.with(mContext).load(image_url+headerQuality+mResponse.getResults().get(position).getPosterPath()).into(viewHolderHeader.mThumbnail);
                    break;

                case LAYOUT_GRID:
                    MovieAdapterViewHolderGrid viewHolderGrid = (MovieAdapterViewHolderGrid) holder;
                    Picasso.with(mContext).load(image_url+mImageQuality+mResponse.getResults().get(position).getPosterPath()).into(viewHolderGrid.mThumbnail);
                    break;
            }

        }
    }

    @Override
    public int getItemCount() {
        if(mResponse == null) {
            return 0;
        }
        return mResponse.getResults().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return LAYOUT_HEADER;
        }
        else {
            return LAYOUT_GRID;
        }
    }

    class MovieAdapterViewHolderGrid extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mThumbnail;

        MovieAdapterViewHolderGrid(View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.main_thumbnail_iv);
        }

        @Override
        public void onClick(View v) {
            newActivity();
        }
    }

    class MovieAdapterViewHolderHeader extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mThumbnail;

        MovieAdapterViewHolderHeader(View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.main_image_backdrop);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MovieDescriptionActivity.class);
            Log.e("reach","here");
            mContext.startActivity(intent);
        }
    }

    /* Helper Methods */

    public void switchAdapter(MovieResponse response) {
        mResponse = response;
        notifyDataSetChanged();
    }

    private void newActivity() {
        Intent intent = new Intent(mContext, MovieDescriptionActivity.class);
        Log.e("reach","here");
        mContext.startActivity(intent);
    }
}
