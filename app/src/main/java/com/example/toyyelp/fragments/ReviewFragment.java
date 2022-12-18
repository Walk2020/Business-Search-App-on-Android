package com.example.toyyelp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toyyelp.R;


public class ReviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        Bundle args = getArguments();
        Bundle review0 = args.getBundle("review0");
        Bundle review1 = args.getBundle("review1");
        Bundle review2 = args.getBundle("review2");

        TextView name1 = view.findViewById(R.id.name1TextView);
        TextView rating1 = view.findViewById(R.id.rating1TextView);
        TextView content1 = view.findViewById(R.id.content1TextView);
        TextView time1 = view.findViewById(R.id.time1TextView);

        TextView name2 = view.findViewById(R.id.name2TextView);
        TextView rating2 = view.findViewById(R.id.rating2TextView);
        TextView content2 = view.findViewById(R.id.content2TextView);
        TextView time2 = view.findViewById(R.id.time2TextView);

        TextView name3 = view.findViewById(R.id.name3TextView);
        TextView rating3 = view.findViewById(R.id.rating3TextView);
        TextView content3 = view.findViewById(R.id.content3TextView);
        TextView time3 = view.findViewById(R.id.time3TextView);

        name1.setText(review0.getString("name"));
        name2.setText(review1.getString("name"));
        name3.setText(review2.getString("name"));

        rating1.setText("Rating :" + review0.getString("rating") + "/5");
        rating2.setText("Rating :" + review1.getString("rating") + "/5");
        rating3.setText("Rating :" + review2.getString("rating") + "/5");

        content1.setText(review0.getString("text"));
        content2.setText(review1.getString("text"));
        content3.setText(review2.getString("text"));

        time1.setText(review0.getString("time_created").split(" ")[0]);
        time2.setText(review1.getString("time_created").split(" ")[0]);
        time3.setText(review2.getString("time_created").split(" ")[0]);

        return view;
    }
}