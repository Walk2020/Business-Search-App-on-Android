package com.example.toyyelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    private String name;
    private String businessId;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TabPageAdapter tabPageAdapter;

    //parameters to fragment
    private String address;
    private String price;
    private String phoneNumber;
    private String status;
    private String category;
    private String yelpLink;
    private String lat;
    private String lng;
    private ArrayList<String> photosUrl;
    private JSONArray reviews;



    private RequestQueue httpQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        httpQueue = Volley.newRequestQueue(this);
        name = getIntent().getStringExtra("NAME");
        businessId = getIntent().getStringExtra("ID");

        String title = "";
        if(name.length() <= 24){
            title = name;
        }
        else{
            title = name.substring(0,22) + "...";
        }
        setTitle(title);

        callDetailApi();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.facebookIcon:
            String facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=" + yelpLink;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            return(true);
        case R.id.twitterIcon:
            String twitterUrl = "https://twitter.com/intent/tweet?text=" + "Check " + name + " on Yelp. \n" + yelpLink;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl)));
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    private void callDetailApi(){
        String url = "https://nodejs-project-yangz673.wl.r.appspot.com/detail?id="+businessId;
        JsonObjectRequest detailRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get parameters
                            address = "N/A";
                            price = "N/A";
                            phoneNumber = "N/A";
                            status = "N/A";
                            category = "N/A";
                            yelpLink = "N/A";
                            photosUrl = new ArrayList<>();

                            if(response.has("location")){
                                JSONArray display_address = response.getJSONObject("location").getJSONArray("display_address");
                                address = display_address.getString(0);
                                for(int i = 1; i < display_address.length(); i++){
                                    address = address + " " + display_address.get(i);
                                }
                            }

                            if(response.has("price")){
                                price = response.getString("price");
                            }

                            if(response.has("display_phone")){
                                String temp = response.getString("display_phone");
                                if(temp.length() != 0){
                                    phoneNumber = temp;
                                }
                            }

                            if(response.has("hours")){
                                JSONObject hours = response.getJSONArray("hours").getJSONObject(0);
                                if(hours.has("is_open_now")){
                                    if(hours.getBoolean("is_open_now")){
                                        status = "Open Now";
                                    }
                                    else{
                                        status = "Closed";
                                    }
                                }
                            }

                            if(response.has("categories")){
                                JSONArray categories = response.getJSONArray("categories");
                                category = categories.getJSONObject(0).getString("title");
                                for(int i = 1; i < categories.length(); i++){
                                    category = category + " | " + categories.getJSONObject(i).getString("title");
                                }
                            }

                            if(response.has("url")){
                                yelpLink = response.getString("url");
                            }

                            if(response.has("photos")){
                                JSONArray photos = response.getJSONArray("photos");
                                for(int i = 0; i < photos.length(); i++){
                                    photosUrl.add(photos.getString(i));
                                }
                            }

                            if(response.has("coordinates")){
                                lat = response.getJSONObject("coordinates").getString("latitude");
                                lng = response.getJSONObject("coordinates").getString("longitude");
                            }

//                            String text = address + "\n" + price + "\n" + phoneNumber + "\n" + status + "\n" + category + "\n" + yelpLink + "\n" + photosUrl.get(0) + "\n" + photosUrl.get(1) + "\n" + photosUrl.get(2);
//                            testView = findViewById(R.id.testTextView);
//                            testView.setText(text);
                            //call review api and set up tab layout
                            callReviewApi();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //error toast
                Context context = getApplicationContext();
                CharSequence text = "geo request error";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();
            }
        });
        httpQueue.add(detailRequest);
    }

    private void callReviewApi(){
        String url = "https://nodejs-project-yangz673.wl.r.appspot.com/review?id=" + businessId;
        JsonObjectRequest detailRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reviews = response.getJSONArray("reviews");
                            setUpTabLayout();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //error toast
                Context context = getApplicationContext();
                CharSequence text = "ipinfo request error";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();
            }
        });
        httpQueue.add(detailRequest);

    }

    private void setUpTabLayout(){
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager2 = findViewById(R.id.myViewPager);
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("id", businessId);
        parameters.put("address", address);
        parameters.put("price", price);
        parameters.put("phoneNumber", phoneNumber);
        parameters.put("status", status);
        parameters.put("category", category);
        parameters.put("yelpLink", yelpLink);
        parameters.put("lat", lat);
        parameters.put("lng", lng);


        tabPageAdapter = new TabPageAdapter(this, parameters, photosUrl, reviews);
        viewPager2.setAdapter(tabPageAdapter);
        viewPager2.setUserInputEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }
}