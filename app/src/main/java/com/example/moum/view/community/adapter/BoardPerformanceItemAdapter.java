package com.example.moum.view.community.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Performance;
import com.example.moum.utils.TimeManager;
import com.example.moum.view.community.BoardPerformanceFragment;

import java.util.ArrayList;

public class BoardPerformanceItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Performance> performances;
    private Context context;
    private BoardPerformanceFragment boardPerformanceFragment;

    public void setPerformances(ArrayList<Performance> performances, Context context, BoardPerformanceFragment boardPerformanceFragment) {
        this.performances = performances;
        this.context = context;
        this.boardPerformanceFragment = boardPerformanceFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_performance, parent, false);
        return new BoardPerformanceItemAdapter.BoardPerformanceViewHolder(view, context, boardPerformanceFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Performance performance = performances.get(position);
        ((BoardPerformanceItemAdapter.BoardPerformanceViewHolder) holder).bind(performance);

    }

    @Override
    public int getItemCount() {
        return performances.size();
    }

    static class BoardPerformanceViewHolder extends RecyclerView.ViewHolder {
        private Performance performance;
        private TextView performanceName;
        private TextView performanceDescription;
        private TextView performanceTeam;
        private TextView performanceTime;
        private ImageView performanceImage;
        private Context context;
        private BoardPerformanceFragment boardPerformanceFragment;

        public BoardPerformanceViewHolder(@NonNull View itemView, Context context, BoardPerformanceFragment boardPerformanceFragment) {
            super(itemView);
            performanceName = itemView.findViewById(R.id.performance_name);
            performanceDescription = itemView.findViewById(R.id.performance_description);
            performanceTeam = itemView.findViewById(R.id.performance_team);
            performanceTime = itemView.findViewById(R.id.performance_time);
            performanceImage = itemView.findViewById(R.id.performance_image);
            this.boardPerformanceFragment = boardPerformanceFragment;
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Performance performance) {
            this.performance = performance;
            if (performance.getPerformanceName() != null) performanceName.setText(performance.getPerformanceName());
            if (performance.getPerformanceDescription() != null) performanceDescription.setText(performance.getPerformanceDescription());
            String timeStr = "";
            if (performance.getPerformanceStartDate() != null) {
                timeStr = timeStr.concat(TimeManager.strToDate(performance.getPerformanceStartDate()));
            }
            if (performance.getPerformanceEndDate() != null) {
                timeStr = timeStr.concat(" ~ ");
                timeStr = timeStr.concat(TimeManager.strToDate(performance.getPerformanceEndDate()));
            }
            performanceTime.setText(timeStr);
            if (performance.getTeamName() != null) performanceTeam.setText(performance.getTeamName());
            if (performance.getPerformanceImageUrl() != null) {
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                                .error(R.drawable.background_more_rounded_gray_size_fit))
                        .load(performance.getPerformanceImageUrl()).into(performanceImage);
            }
            performanceImage.setClipToOutline(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boardPerformanceFragment.onPerformanceClicked(performance.getId());
                }
            });
        }
    }
}
