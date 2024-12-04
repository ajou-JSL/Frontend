package com.example.moum.view.moum.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.view.moum.MoumFindPerformanceHallActivity;
import com.example.moum.view.moum.MoumFindPracticeroomActivity;

import java.util.ArrayList;

public class MoumPerformanceHallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PerformanceHall> performanceHalls;
    private Context context;

    public void setPerformanceHalls(ArrayList<PerformanceHall> performanceHalls, @NonNull Context context) {
        this.performanceHalls = performanceHalls;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_performancehall, parent, false);
        return new MoumPerformanceHallAdapter.MoumPerformanceHallViewHolder(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PerformanceHall performanceHall = performanceHalls.get(position);
        ((MoumPerformanceHallAdapter.MoumPerformanceHallViewHolder) holder).bind(performanceHall);
    }

    @Override
    public int getItemCount() {
        return performanceHalls.size();
    }

    static class MoumPerformanceHallViewHolder extends RecyclerView.ViewHolder{
        private PerformanceHall performanceHall;
        private TextView performanceHallName;
        private TextView performanceHallDescription;
        private TextView performanceHallAddress;
        private TextView performanceHallPrice;
        private ImageView performanceHallProfile;
        private ConstraintLayout performanceHallTop;
        private MoumFindPerformanceHallActivity activity;
        private Context context;

        public MoumPerformanceHallViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            performanceHallName = itemView.findViewById(R.id.performance_hall_name);
            performanceHallDescription = itemView.findViewById(R.id.performance_hall_description);
            performanceHallAddress = itemView.findViewById(R.id.performance_hall_address);
            performanceHallPrice = itemView.findViewById(R.id.performance_hall_price);
            performanceHallProfile = itemView.findViewById(R.id.performance_hall_image);
            performanceHallTop = itemView.findViewById(R.id.top_performance_hall);
            this.activity = (MoumFindPerformanceHallActivity) context;
            this.context = context;
        }

        public void bind(PerformanceHall performanceHall) {
            this.performanceHall = performanceHall;
            if(performanceHall.getName() != null) performanceHallName.setText(performanceHall.getName());
            if(performanceHall.getDetails() != null) performanceHallDescription.setText(performanceHall.getDetails());
            if(performanceHall.getAddress() != null) performanceHallAddress.setText(performanceHall.getAddress());
            if(performanceHall.getPrice() != null) performanceHallPrice.setText(String.format("시간 당 %,d원", performanceHall.getPrice()));
            if(performanceHall.getImageUrls() != null && !performanceHall.getImageUrls().isEmpty())
                Glide.with(context)
                        .load(performanceHall.getImageUrls().get(0))
                        .apply(new RequestOptions()
                        .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                        .error(R.drawable.background_more_rounded_gray_size_fit))
                        .into(performanceHallProfile);
            performanceHallProfile.setClipToOutline(true);
            performanceHallTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onPerformanceHallClicked(performanceHall.getId());
                }
            });
        }

    }
}
