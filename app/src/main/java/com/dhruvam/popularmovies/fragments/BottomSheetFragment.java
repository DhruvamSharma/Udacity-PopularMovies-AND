package com.dhruvam.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dhruvam.popularmovies.R;
import com.dhruvam.popularmovies.activity.MovieGridActivity;
import com.dhruvam.popularmovies.network.NetworkUtils;

/**
 * Created by dell on 20-06-2018.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    static View view;
    static ProgressBar progressBar;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup       container, @Nullable Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.fragment_bottom_sheet_dialiog,container,false);

        view = v;

        final RadioButton topRatedRB = v.findViewById(R.id.top_rated_rb);
        final RadioButton mostPopular = v.findViewById(R.id.most_popular_rb);
        final RadioButton upcoming = v.findViewById(R.id.upcoming_rb);
        final RadioButton now_playing = v.findViewById(R.id.now_playing_rb);
        progressBar = v.findViewById(R.id.loader_pb);

        final String[] query_parameter = {null};

        final RadioGroup radioGroup = v.findViewById(R.id.category_rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == topRatedRB.getId()) {
                    assert container != null;
                    query_parameter[0] = v.getContext().getResources().getString(R.string.top_rated_label);
                }
                else if( checkedId == mostPopular.getId()) {
                    assert container != null;
                    query_parameter[0] = v.getContext().getResources().getString(R.string.most_popular_label);
                }
                else if (checkedId == upcoming.getId()) {
                    assert container != null;
                    query_parameter[0] = v.getContext().getResources().getString(R.string.upcoming_label);
                }
                else if (checkedId == now_playing.getId()) {
                    assert container != null;
                    query_parameter[0] = v.getContext().getResources().getString(R.string.now_playing_label);
                }
            }
        });

        Button apply = v.findViewById(R.id.apply_btn);
        Button clearSelection = v.findViewById(R.id.clear_selection_btn);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (query_parameter[0] == null) {
                    hideBottomSheet(v);
                } else {
                    NetworkUtils.getHTTPResponseForSorted(query_parameter[0]);
                }


            }
        });
        clearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup.clearCheck();
                query_parameter[0] = null;
                NetworkUtils.getHttpResponse();
            }
        });
        return v;
    }

    public static void setProgressVsisiblity(String flag) {
        if(flag.equals(view.getContext().getResources().getString(R.string.network_request_started))) {
            progressBar.setVisibility(View.VISIBLE);
        }
        else if(flag.equals(view.getContext().getResources().getString(R.string.network_request_finished))) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public static void hideBottomSheet(View v) {
        MovieGridActivity.hideBottomSheetDialog(v);
    }

    public static View getContextForSheet() {
        return view;
    }
}

