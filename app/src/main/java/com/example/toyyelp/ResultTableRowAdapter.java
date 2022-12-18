package com.example.toyyelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultTableRowAdapter extends RecyclerView.Adapter<ResultTableRowAdapter.ResultTableViewHolder> {

    private final ClickResultInterface clickResultInterface;

    Context context;
    ArrayList<ResultRowModel> resultRowModel;

    public ResultTableRowAdapter(Context context, ArrayList<ResultRowModel> resultRowModel, ClickResultInterface clickResultInterface){
        this.context = context;
        this.resultRowModel = resultRowModel;
        this.clickResultInterface = clickResultInterface;
    }


    @NonNull
    @Override
    public ResultTableRowAdapter.ResultTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.result_recycler_view_row, parent, false);


        return new ResultTableRowAdapter.ResultTableViewHolder(view, clickResultInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultTableRowAdapter.ResultTableViewHolder holder, int position) {
        holder.serNumView.setText(resultRowModel.get(position).getSerialNumber());

        String imageUrl = resultRowModel.get(position).getBusinessImageUrl();
        Picasso.get().load(imageUrl).into(holder.imageView);

        holder.nameView.setText(resultRowModel.get(position).getBusinessName());
        holder.rateView.setText(resultRowModel.get(position).getRating());
        holder.distanceView.setText(resultRowModel.get(position).getDistance());
    }

    @Override
    public int getItemCount() {
        return resultRowModel.size();
    }

    public static class ResultTableViewHolder extends RecyclerView.ViewHolder{
        TextView serNumView;
        ImageView imageView;
        TextView nameView;
        TextView rateView;
        TextView distanceView;

        public ResultTableViewHolder(@NonNull View itemView, ClickResultInterface clickResultInterface) {
            super(itemView);

            serNumView = itemView.findViewById(R.id.serialNumTextView);
            imageView = itemView.findViewById(R.id.businessImageView);
            nameView = itemView.findViewById(R.id.businessNameTextView);
            rateView = itemView.findViewById(R.id.rateTextView);
            distanceView = itemView.findViewById(R.id.distanceTextView);



            //this part of code is from a youtube video
            //https://www.youtube.com/watch?v=7GPUpvcU1FE
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickResultInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            clickResultInterface.goToDetailPage(position);
                        }
                    }
                }
            });

        }
    }
}
