package com.dhruvam.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.activity.MovieDescriptionActivity;
import com.dhruvam.popularmovies.database.entity.MovieResponseEntity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * Created by dell on 18-06-2018.
 */

public class SimilarListAdapter extends RecyclerView.Adapter<SimilarListAdapter.MovieAdapter> {

    private MovieResponseEntity mResponse;
    private String mImageQuality;
    private Context mContext;

    public SimilarListAdapter(Context context) {
        mContext = context;
        mImageQuality = mContext.getResources().getString(R.string.thumbnail_quality_3);
    }


    @NonNull
    @Override
    public MovieAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_grid_thumbnail,parent,false);
        return new MovieAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter holder, int position) {
        String image_url = mContext.getResources().getString(R.string.thumbnail_url);
        Picasso.with(mContext).load(image_url+mImageQuality+mResponse.getResults().get(position).getPosterPath()).into(holder.mImagePoster);
    }

    @Override
    public int getItemCount() {
        if(mResponse == null) {
            return 0;
        }
        return mResponse.getResults().size();
    }

    public class MovieAdapter extends RecyclerView.ViewHolder {

        ImageView mImagePoster;

        public MovieAdapter(View itemView) {
            super(itemView);
            mImagePoster = itemView.findViewById(R.id.main_thumbnail_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newActivity(getAdapterPosition());
                }
            });
        }
    }

    /* Helper Methods */

    public void switchAdapter(MovieResponseEntity response) {
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

        ((Activity)mContext).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
