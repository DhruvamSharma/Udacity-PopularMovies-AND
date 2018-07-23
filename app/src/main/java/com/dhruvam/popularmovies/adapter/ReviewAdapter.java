package com.dhruvam.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.pojo.MovieReviews;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private MovieReviews mReviews;

    public ReviewAdapter(MovieReviews reviews) {
        mReviews = reviews;
    }

    public ReviewAdapter() {

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_view_holder, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        holder.reviewText.setText(mReviews.getResults().get(position).getContent());

    }

    @Override
    public int getItemCount() {
        if(mReviews != null)
        return mReviews.getResults().size();
        else return 0;
    }

    public void switchAdapter(MovieReviews reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView reviewText;

        ReviewViewHolder(View itemView) {
            super(itemView);

            reviewText = itemView.findViewById(R.id.review_text_tv);
        }
    }
}
