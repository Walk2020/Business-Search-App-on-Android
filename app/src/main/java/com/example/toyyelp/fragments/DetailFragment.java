package com.example.toyyelp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.toyyelp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class DetailFragment extends Fragment {

    private String address;
    private String price;
    private String phoneNumber;
    private String status;
    private String category;
    private String yelpLink;
    private String id;
    private String name;
    private ArrayList<String> photosUrl;

    private TextView addressTextView;
    private TextView priceTextView;
    private TextView phoneTextView;
    private TextView statusTextView;
    private TextView categoryTextView;
    private TextView yelpTextView;
    private Button reserveButton;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //get parameter
        Bundle args = getArguments();
        id = args.getString("id");
        name = args.getString("name");
        address = args.getString("address");
        price = args.getString("price");
        phoneNumber = args.getString("phoneNumber");
        status = args.getString("status");
        category = args.getString("category");
        yelpLink = args.getString("yelpLink");
        photosUrl = args.getStringArrayList("photosUrl");

        context = getActivity();

        //get views
        addressTextView = view.findViewById(R.id.addressTextView);
        priceTextView = view.findViewById(R.id.priceTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);
        statusTextView = view.findViewById(R.id.statusTextView);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        yelpTextView = view.findViewById(R.id.yelpTextView);
        reserveButton = view.findViewById(R.id.reserveButton);
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);

        setContent();
        setClickListenerForYelpLink();
        setClickListenerForReserveButton();
        setImages();

        return view;
    }

    private void setImages(){
        int length = photosUrl.size();
        if(length == 3){
            Picasso.get().load(photosUrl.get(0)).into(imageView1);
            Picasso.get().load(photosUrl.get(1)).into(imageView2);
            Picasso.get().load(photosUrl.get(2)).into(imageView3);
        }
        if(length == 2){
            Picasso.get().load(photosUrl.get(0)).into(imageView1);
            Picasso.get().load(photosUrl.get(1)).into(imageView2);
        }
        if(length == 1){
            Picasso.get().load(photosUrl.get(0)).into(imageView1);
        }
    }

    private void setContent(){
        addressTextView.setText(address);
        priceTextView.setText(price);
        phoneTextView.setText(phoneNumber);
        statusTextView.setText(status);
        if(status.equals("Closed")){
            statusTextView.setTextColor(Color.parseColor("#D31305"));
        }
        else if(status.equals("Open Now")){
            statusTextView.setTextColor(Color.parseColor("#5AE629"));
        }
        else{
            statusTextView.setTextColor(Color.parseColor("#000000"));
        }

        categoryTextView.setText(category);
        //yelpTextView.setText(yelpLink);
    }

    private void setClickListenerForYelpLink(){
        yelpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(yelpLink)));

            }
        });
    }

    private void setClickListenerForReserveButton(){
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReserveDialog();
            }
        });
    }

    private void openReserveDialog(){
        // custom dialog
        Dialog reserveDialog = new Dialog(context);
        reserveDialog.setContentView(R.layout.reserve_dialog);
        reserveDialog.setTitle(name);

        // set the title
        TextView title = (TextView) reserveDialog.findViewById(R.id.reserveDialogTitle);
        title.setText(name);


        final Calendar date = Calendar.getInstance();
        //set date picker
        EditText dateEditTextView  = reserveDialog.findViewById(R.id.editTextDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar updateDate = Calendar.getInstance();
                updateDate.set(year, month, day);
                dateEditTextView.setText(String.format(Locale.getDefault(), "%02d-%02d-%04d", month, day, year));
            }
        },date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        dateEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        //set time picker
        EditText timeEditTextView  = reserveDialog.findViewById(R.id.editTextTime);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeEditTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        },date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE),false);

        timeEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        //set cancel button
        TextView cancelButton = reserveDialog.findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveDialog.dismiss();
            }
        });

        //set submit button
        TextView submitButton = reserveDialog.findViewById(R.id.submitreserveBtn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check validation
                String emailString = ((EditText)reserveDialog.findViewById(R.id.editTextTextEmailAddress)).getText().toString();
                if(emailString.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
                    Toast toast = Toast.makeText(context, "InValid Email Address", Toast.LENGTH_SHORT);
                    toast.show();
                    reserveDialog.dismiss();
                    return;
                }

                String hourAndminute = timeEditTextView.getText().toString();
                if(hourAndminute.length() == 0){
                    Toast toast = Toast.makeText(context, "Time should be between 10AM AND 5PM", Toast.LENGTH_SHORT);
                    toast.show();
                    reserveDialog.dismiss();
                    return;
                }
                int hour =  Integer.parseInt(hourAndminute.split(":")[0]);
                int minute =  Integer.parseInt(hourAndminute.split(":")[1]);
                if(hour < 10 || hour > 17 || (hour == 17 && minute > 0)){
                    Toast toast = Toast.makeText(context, "Time should be between 10AM AND 5PM", Toast.LENGTH_SHORT);
                    toast.show();
                    reserveDialog.dismiss();
                    return;
                }

                //store the reservation
                //use ID as key to identify if the business is the same
                String record = id + "&" + name + "&" + dateEditTextView.getText().toString() + "&" + hourAndminute + "&" + emailString;
                SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(id, record);
                editor.apply();

                Toast toast = Toast.makeText(context, "Reservation Booked", Toast.LENGTH_SHORT);
                toast.show();
                reserveDialog.dismiss();
            }
        });


        reserveDialog.show();
    }

}