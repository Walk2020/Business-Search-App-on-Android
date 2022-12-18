package com.example.toyyelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;



public class ReservationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ConstraintLayout myConstraintLayout;
    private TextView noBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        recyclerView = findViewById(R.id.reservationRecyclerView);
        myConstraintLayout = findViewById(R.id.reservationConstraintLayout);
        noBookings = findViewById(R.id.noBookingsFound);
        noBookings.setVisibility(View.INVISIBLE);

        generateBookingsRow();


    }

    private void generateBookingsRow(){
        ArrayList<ReservationModel> reservationModels = new ArrayList<>();
        Map<String,?> content = sharedPreferences.getAll();
        int num = 1;
        for(String id : content.keySet()){
            String[] elements = content.get(id).toString().split("&");
            reservationModels.add(new ReservationModel(num, elements[1], elements[2], elements[3], elements[4], elements[0]));
            num++;
        }
        if(reservationModels.isEmpty()){
            noBookings.setVisibility(View.VISIBLE);
            return;
        }


        ReservationRowAdapter reservationRowAdapter = new ReservationRowAdapter(this, reservationModels);
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                sharedPreferences.edit().remove(reservationModels.get(position).getId()).commit();
                reservationModels.remove(position);
                reservationRowAdapter.notifyDataSetChanged();
                if(reservationModels.isEmpty()){
                    noBookings.setVisibility(View.VISIBLE);
                }
                Snackbar snackbar = Snackbar.make(myConstraintLayout, "Removing Existing Reservation", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive){
                    //draw background color
                    View itemView = viewHolder.itemView;
                    ColorDrawable bg = new ColorDrawable();
                    bg.setColor(Color.rgb(200,29,29));
                    bg.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    bg.draw(c);

                    //draw icon
                    Drawable icon = ActivityCompat.getDrawable(ReservationActivity.this, R.drawable.ic_delete);
                    int top = itemView.getHeight()/2 - icon.getIntrinsicHeight()/2 + itemView.getTop();
                    icon.setBounds(itemView.getRight() - icon.getIntrinsicWidth() - 80, top, itemView.getRight() - 80, top + icon.getIntrinsicHeight());
                    icon.draw(c);

                }
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(reservationRowAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

}
