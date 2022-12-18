package com.example.toyyelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class ReservationRowAdapter extends RecyclerView.Adapter<ReservationRowAdapter.ReservationViewHolder>{
    private Context context;
    private ArrayList<ReservationModel> reservationModels;

    public ReservationRowAdapter(Context context, ArrayList<ReservationModel> reservationModels){
        this.context = context;
        this.reservationModels = reservationModels;
    }

    @NonNull
    @Override
    public ReservationRowAdapter.ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reservation_row, parent, false);
        return new ReservationRowAdapter.ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationRowAdapter.ReservationViewHolder holder, int position) {
        holder.order.setText(reservationModels.get(position).getOrderNum()+"");
        holder.name.setText(reservationModels.get(position).getName());
        holder.date.setText(reservationModels.get(position).getDate());
        holder.time.setText(reservationModels.get(position).getTime());
        holder.email.setText(reservationModels.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return reservationModels.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder{
        TextView order;
        TextView name;
        TextView date;
        TextView time;
        TextView email;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);

            order = itemView.findViewById(R.id.orderTextView);
            name = itemView.findViewById(R.id.nameTextView);
            date = itemView.findViewById(R.id.dateTextView);
            time = itemView.findViewById(R.id.timeTextView);
            email = itemView.findViewById(R.id.emailTextView);
        }
    }
}
