package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DetailReviews extends Fragment {

    public DetailReviews() {
        // Required empty public constructor
    }

    public static DetailReviews newInstance() {
        DetailReviews fragment = new DetailReviews();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_reviews, container, false);
    }
}
