package com.dhruvam.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.activity.MovieDescriptionActivity;
import com.dhruvam.popularmovies.fragments.BottomSheetFragment;
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * Created by dell on 05-06-2018.
 */

public class MainGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MovieResponse mResponse;
    private Context mContext;
    private String mImageQuality;
    private static BottomSheetFragment bottomSheetDialogFragment;

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

    class MovieAdapterViewHolderGrid extends RecyclerView.ViewHolder{
        ImageView mThumbnail;

        MovieAdapterViewHolderGrid(View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.main_thumbnail_iv);
            mThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    newActivity(getAdapterPosition());
                }
            });
        }

    }

    class MovieAdapterViewHolderHeader extends RecyclerView.ViewHolder{

        ImageView mThumbnail;
        ImageButton mAdd;

        MovieAdapterViewHolderHeader(View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.main_image_backdrop);
            mThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newActivity(getAdapterPosition());
                }
            });

            mAdd = itemView.findViewById(R.id.add_to_list_ib);
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialog(v);
                }
            });
        }


    }

    /* Helper Methods */

    public void switchAdapter(MovieResponse response) {
        mResponse = response;
        notifyDataSetChanged();
    }

    private void newActivity(int position) {

        /* Parcelable code */
        Parcelable parcelable = Parcels.wrap(mResponse.getResults().get(position));
        Bundle bundle = new Bundle();
        bundle.putParcelable(mContext.getPackageName(), parcelable);

        /* Intent code */

        Intent intent = new Intent(mContext, MovieDescriptionActivity.class);
        intent.putExtra(mContext.getPackageName(), bundle);
        mContext.startActivity(intent);

    }

    public void showBottomSheetDialog(View v) {

        bottomSheetDialogFragment = new BottomSheetFragment();
        bottomSheetDialogFragment.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }

    public static void hideBottomSheetDialog(View v) {
        bottomSheetDialogFragment.dismiss();
    }


}
