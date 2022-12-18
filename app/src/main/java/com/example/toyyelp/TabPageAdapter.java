package com.example.toyyelp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.toyyelp.fragments.DetailFragment;
import com.example.toyyelp.fragments.MapFragment;
import com.example.toyyelp.fragments.ReviewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabPageAdapter extends FragmentStateAdapter {
    HashMap<String,String> parameters;
    ArrayList<String> photosUrl;
    JSONArray reviews;

    public TabPageAdapter(@NonNull FragmentActivity fragmentActivity, HashMap<String,String> parameters, ArrayList<String> photosUrl, JSONArray reviews) {
        super(fragmentActivity);
        this.parameters = parameters;
        this.photosUrl = photosUrl;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 :
                Bundle argsForDetail = new Bundle();
                argsForDetail.putString("name", parameters.get("name"));
                argsForDetail.putString("id", parameters.get("id"));
                argsForDetail.putString("address", parameters.get("address"));
                argsForDetail.putString("price", parameters.get("price"));
                argsForDetail.putString("phoneNumber", parameters.get("phoneNumber"));
                argsForDetail.putString("status", parameters.get("status"));
                argsForDetail.putString("category", parameters.get("category"));
                argsForDetail.putString("yelpLink", parameters.get("yelpLink"));
                argsForDetail.putStringArrayList("photosUrl", photosUrl);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(argsForDetail);
                return  detailFragment;

            case 1:
                Bundle argsForMap = new Bundle();
                argsForMap.putString("lat", parameters.get("lat"));
                argsForMap.putString("lng", parameters.get("lng"));
                MapFragment mapFragment = new MapFragment();
                mapFragment.setArguments(argsForMap);
                return mapFragment;

            case 2:
                Bundle argsReview0 = new Bundle();
                Bundle argsReview1 = new Bundle();
                Bundle argsReview2 = new Bundle();
                try {
                    argsReview0.putString("name", reviews.getJSONObject(0).getJSONObject("user").getString("name"));
                    argsReview0.putString("rating", reviews.getJSONObject(0).getString("rating"));
                    argsReview0.putString("text", reviews.getJSONObject(0).getString("text"));
                    argsReview0.putString("time_created", reviews.getJSONObject(0).getString("time_created"));

                    argsReview1.putString("name", reviews.getJSONObject(1).getJSONObject("user").getString("name"));
                    argsReview1.putString("rating", reviews.getJSONObject(1).getString("rating"));
                    argsReview1.putString("text", reviews.getJSONObject(1).getString("text"));
                    argsReview1.putString("time_created", reviews.getJSONObject(1).getString("time_created"));

                    argsReview2.putString("name", reviews.getJSONObject(2).getJSONObject("user").getString("name"));
                    argsReview2.putString("rating", reviews.getJSONObject(2).getString("rating"));
                    argsReview2.putString("text", reviews.getJSONObject(2).getString("text"));
                    argsReview2.putString("time_created", reviews.getJSONObject(2).getString("time_created"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Bundle argsForReviews = new Bundle();
                argsForReviews.putBundle("review0", argsReview0);
                argsForReviews.putBundle("review1", argsReview1);
                argsForReviews.putBundle("review2", argsReview2);

                ReviewFragment reviewFragment = new ReviewFragment();
                reviewFragment.setArguments(argsForReviews);
                return reviewFragment;

            default:
                Bundle args = new Bundle();
                args.putString("address", parameters.get("address"));
                args.putString("price", parameters.get("price"));
                args.putString("phoneNumber", parameters.get("phoneNumber"));
                args.putString("status", parameters.get("status"));
                args.putString("category", parameters.get("category"));
                args.putString("yelpLink", parameters.get("yelpLink"));
                DetailFragment detailFragmentDefault = new DetailFragment();
                detailFragmentDefault.setArguments(args);
                return  detailFragmentDefault;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
