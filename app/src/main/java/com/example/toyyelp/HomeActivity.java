package com.example.toyyelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ClickResultInterface {
    private AutoCompleteTextView keyword;
    private EditText distance;
    private EditText location;
    private Spinner category;
    private CheckBox autoLocation;
    private TextView noResultFound;
    private RecyclerView resultTable;

    private RequestQueue httpQueue;

    private Context homeActivityContext;
    private ClickResultInterface clickResultInterface;



    //prameter
    private String term;
    private String cate;
    private String dis;
    private String lat;
    private String lng;

    //search result
    ArrayList<ResultRowModel> resultRowModel;

    //auto complete array
    ArrayList<String> autoCompleteOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        keyword = findViewById(R.id.keyword);
        distance = findViewById(R.id.distance);
        location = findViewById(R.id.location);
        category = (Spinner) findViewById(R.id.category);
        autoLocation = (CheckBox) findViewById(R.id.autoLacation);
        noResultFound = findViewById(R.id.noResultsFound);
        resultTable = findViewById(R.id.resultRecyclerView);

        homeActivityContext = this;
        clickResultInterface = this;

        httpQueue = Volley.newRequestQueue(this);


        //initialization
        noResultFound.setVisibility(View.INVISIBLE);
        resultTable.setVisibility(View.INVISIBLE);


        //Category Spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.categoryOptions, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);
        category.setOnItemSelectedListener(this);

        //autocomplete
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                autoCompleteOptions = new ArrayList<>();

                String input = keyword.getText().toString();
                String url = "https://nodejs-project-yangz673.wl.r.appspot.com/autocomplete?keyword=" + input;
                JsonObjectRequest autoCompleteRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray options = response.getJSONArray("terms");
                                    if(options.length() != 0){
                                        for(int i = 0; i < options.length(); i++){
                                            String opt = options.getJSONObject(i).getString("text");
                                            autoCompleteOptions.add(opt);
                                        }
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(homeActivityContext, android.R.layout.simple_list_item_1, autoCompleteOptions);
                                    keyword.setAdapter(arrayAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error toast
                    }
                });
                httpQueue.add(autoCompleteRequest);
            }
        };
        keyword.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.reservationBtn:
            Intent intent = new Intent(this, ReservationActivity.class);
            startActivity(intent);
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    //checkBox auto location
    public void autoCheckBoxClicked(View v) {
        EditText location = findViewById(R.id.location);
        boolean checked = ((CheckBox) v).isChecked();
        if(checked){
            location.setText("");
            location.setVisibility(View.INVISIBLE);
        }
        else{
            location.setVisibility(View.VISIBLE);
        }
    }


    //reset form
    public void resetForm(View v){
        noResultFound.setVisibility(View.INVISIBLE);
        resultTable.setVisibility(View.INVISIBLE);
        keyword.setText("");
        distance.setText("");
        location.setText("");
        location.setVisibility(View.VISIBLE);
        category.setSelection(0);
        autoLocation.setChecked(false);
    }

    //submit form
    public void submitForm(View v){
        noResultFound.setVisibility(View.INVISIBLE);
        //error message
        if(keyword.getText().toString().length() == 0){
            keyword.setError("This field is required");
            return;
        }
        if(distance.getText().toString().length() == 0){
            distance.setError("This field is required");
            return;
        }
        if(!autoLocation.isChecked() && location.getText().toString().length() == 0){
            location.setError("This field is required");
            return;
        }

        //get parameters
        term = keyword.getText().toString();
        cate = category.getSelectedItem().toString();
        dis = distance.getText().toString();


        if(autoLocation.isChecked()){
            String ipTokean = "";
            String url = "https://ipinfo.io/?token=" + ipTokean;
            JsonObjectRequest ipinfoRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String[] locArray = response.getString("loc").split(",");
                                lat = locArray[0];
                                lng = locArray[1];
                                callYelpSearch();
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
            httpQueue.add(ipinfoRequest);

        }
        else{
            String loc = location.getText().toString();
            String geoKey = "";
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + loc + "&key=" + geoKey;
            JsonObjectRequest geoRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                if(status.equals("ZERO_RESULTS")){
                                    noResultFound.setVisibility(View.VISIBLE);
                                    resultTable.setVisibility(View.INVISIBLE);
                                }
                                else{
                                    JSONArray results = response.getJSONArray("results");
                                    JSONObject result = results.getJSONObject(0);
                                    JSONObject geoLoc = result.getJSONObject("geometry").getJSONObject("location");
                                    lat = geoLoc.getString("lat");
                                    lng = geoLoc.getString("lng");
                                    callYelpSearch();
                                }
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
            httpQueue.add(geoRequest);
        }
    }

    private void callYelpSearch(){
        String url = "https://nodejs-project-yangz673.wl.r.appspot.com/searchYelp?" + "lat=" + lat + "&lng=" + lng + "&term=" + term + "&category=" + cate + "&distance=" + dis;
        JsonObjectRequest yelpSearchRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int numOfResult = response.getInt("total");
                            if(numOfResult == 0){
                                noResultFound.setVisibility(View.VISIBLE);
                                resultTable.setVisibility(View.INVISIBLE);
                            }
                            else{
                                noResultFound.setVisibility(View.INVISIBLE);
                                //((TextView)findViewById(R.id.testResult)).setText(response.getString("businesses"));

                                // set up resultRowModel
                                resultRowModel = new ArrayList<>();

                                int numOfResultShown = Math.min(9, numOfResult);
                                JSONArray wholeResults = response.getJSONArray("businesses");

                                for(int i = 0; i < numOfResultShown; i++){
                                    JSONObject oneResult = wholeResults.getJSONObject(i);

                                    String serialNumber = i + 1 + "";
                                    //change images here;
                                    String businessImage = oneResult.getString("image_url");
                                    String businessName = oneResult.getString("name");
                                    String rating = oneResult.getString("rating");
                                    double temDis = oneResult.getDouble("distance") / 1609.344;
                                    String distance = ((int)temDis) + "";
                                    String id = oneResult.getString("id");

                                    //set model
                                    resultRowModel.add(new ResultRowModel(serialNumber, businessImage, businessName, rating, distance, id));

                                    ResultTableRowAdapter adapter = new ResultTableRowAdapter(homeActivityContext, resultRowModel, clickResultInterface);
                                    resultTable.setAdapter(adapter);
                                    resultTable.setLayoutManager(new LinearLayoutManager(homeActivityContext));
                                    resultTable.setVisibility(View.VISIBLE);
                                }

                            }

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
                CharSequence text = "yelpSearchRequest error";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();
            }
        });
        httpQueue.add(yelpSearchRequest);

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void goToDetailPage(int position) {
        String id = resultRowModel.get(position).getId();
        String name = resultRowModel.get(position).getBusinessName();

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("NAME", name);
        intent.putExtra("ID", id);
        startActivity(intent);

    }
}