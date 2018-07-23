package com.dhruvam.popularmovies.adapter;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.activity.MovieDescriptionActivity;
import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.dhruvam.popularmovies.pojo.MovieTrailors;
import com.dhruvam.popularmovies.tools.BlurBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.parceler.Parcels;

/**
 * Created by dell on 18-06-2018.
 */

public class SimilarListAdapter extends RecyclerView.Adapter<SimilarListAdapter.MovieAdapter> {

    private MovieTrailors mResponse;
    private String mImageQuality;
    private Context mContext;
    private String mImagePosterPath;

    public SimilarListAdapter(Context context) {
        mContext = context;
        mImageQuality = mContext.getResources().getString(R.string.thumbnail_quality_3);
    }


    @NonNull
    @Override
    public MovieAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_view_holder,parent,false);
        return new MovieAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter holder, int position) {
        String image_url = mContext.getResources().getString(R.string.thumbnail_url);

        holder.mTrailerName.setText(mResponse.getResults().get(position).getName());
        holder.mTrailerSource.setText(mResponse.getResults().get(position).getSite());
        holder.animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlayAnimation(holder.animationView);
            }
        });


        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.mImagePoster.setBackground(new BitmapDrawable(mContext.getResources(), BlurBuilder.blur(mContext, bitmap)));

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(mContext).load(image_url+mImageQuality+mImagePosterPath).into(target);
    }

    private void startPlayAnimation(final LottieAnimationView lottieAnimationView) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lottieAnimationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        if (lottieAnimationView.getProgress() == 0f) {
            animator.start();
        } else {
            lottieAnimationView.setProgress(0f);
        }
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
        TextView mTrailerName;
        TextView mTrailerSource;
        LottieAnimationView animationView;

        public MovieAdapter(View itemView) {
            super(itemView);
            mImagePoster = itemView.findViewById(R.id.main_poster_iv);
            mTrailerName = itemView.findViewById(R.id.trailer_name_tv);
            mTrailerSource = itemView.findViewById(R.id.trailer_source_tv);
            animationView = itemView.findViewById(R.id.lottieAnimationViewPlay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //newActivity(getAdapterPosition());
                }
            });
        }
    }

    /* Helper Methods */

    public void switchAdapter(MovieTrailors response, String posterPath) {
        mResponse = response;
        this.mImagePosterPath = posterPath;
        notifyDataSetChanged();
    }

    private void newActivity(int position) {


        /* Intent code */

        Intent intent = new Intent(mContext, MovieDescriptionActivity.class);
        intent.putExtra(mContext.getPackageName(), mResponse.getResults().get(position).getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
