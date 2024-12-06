package com.example.moum.view.home.adapter;

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
import com.example.moum.data.entity.Performance;
import com.example.moum.utils.TimeManager;
import com.example.moum.view.home.HomeFragment;

import java.util.ArrayList;

public class HomePerformHotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Performance> performances;
    private Context context;
    private HomeFragment homeFragment;

    public void setPerformances(ArrayList<Performance> performances, Context context, HomeFragment homeFragment) {
        this.performances = performances;
        this.context = context;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_performance, parent, false);
        return new HomePerformHotAdapter.HomePerformHotViewModel(view, context, homeFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Performance performance = performances.get(position);
        ((HomePerformHotAdapter.HomePerformHotViewModel) holder).bind(performance);

    }

    @Override
    public int getItemCount() {
        return performances.size();
    }

    static class HomePerformHotViewModel extends RecyclerView.ViewHolder {
        private Performance performance;
        private ImageView performImage;
        private TextView performName;
        private TextView performPlace;
        private TextView performTime;
        private ConstraintLayout performTop;
        private Context context;
        private HomeFragment homeFragment;

        public HomePerformHotViewModel(@NonNull View itemView, Context context, HomeFragment homeFragment) {
            super(itemView);
            performImage = itemView.findViewById(R.id.imageview_main_performance);
            performName = itemView.findViewById(R.id.textview_main_performance_name);
            performPlace = itemView.findViewById(R.id.textview_main_performance_place);
            performTime = itemView.findViewById(R.id.textview_main_performance_time);
            performTop = itemView.findViewById(R.id.performance_top);
            this.context = context;
            this.homeFragment = homeFragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Performance performance) {
            this.performance = performance;
            if (performance.getPerformanceImageUrl() != null) {
                Glide.with(context)
                        .load(performance.getPerformanceImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.background_top_more_rounded_gray_size_fit)
                                .error(R.drawable.background_top_more_rounded_gray_size_fit))
                        .into(performImage);
            }
            performImage.setClipToOutline(true);
            performName.setText(performance.getPerformanceName());
            performPlace.setText(performance.getPerformanceLocation());
            String performTimeStr = "";
            if (performance.getPerformanceStartDate() != null) {
                performTimeStr = performTimeStr.concat(TimeManager.strToDate(performance.getPerformanceStartDate()));
            }
            if (performance.getPerformanceEndDate() != null) {
                performTimeStr = performTimeStr.concat("\n~ " + TimeManager.strToDate(performance.getPerformanceEndDate()));
            }
            performTime.setText(performTimeStr);
            performTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homeFragment.onPerformClicked(performance.getId());
                }
            });
        }
    }
}
