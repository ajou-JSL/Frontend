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
import com.example.moum.view.moum.MoumListPerformanceHallActivity;

import java.util.ArrayList;

public class PerformOfMoumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PerformanceHall> performanceHalls;
    private Context context;
    private static final int VIEW_TYPE_EXIST = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private static final int VIEW_TYPE_EXIST_NOT_VERIFIED = 3;

    public void setPerformanceHalls(ArrayList<PerformanceHall> performanceHalls, Context context) {
        this.performanceHalls = performanceHalls;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (performanceHalls.get(position) == null || (performanceHalls.get(position).getId() == null && performanceHalls.get(position).getName()
                == null)) {
            return VIEW_TYPE_EMPTY;
        } else if (performanceHalls.get(position).getId() == null && performanceHalls.get(position).getName() != null) {
            return VIEW_TYPE_EXIST_NOT_VERIFIED;
        } else {
            return VIEW_TYPE_EXIST;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perform_of_moum_empty, parent, false);
            return new PerformOfMoumAdapter.PerformEmptyViewHolder(view, context);
        } else if (viewType == VIEW_TYPE_EXIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perform_of_moum, parent, false);
            return new PerformOfMoumAdapter.PerformExistViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_practice_of_moum, parent, false);
            return new PracticeOfMoumAdapter.PracticemExistNotVerifiedViewHolder(view, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PerformanceHall performanceHall = performanceHalls.get(position);
        if (holder instanceof PerformOfMoumAdapter.PerformEmptyViewHolder) {
            ((PerformOfMoumAdapter.PerformEmptyViewHolder) holder).bind();
        } else if (holder instanceof PerformOfMoumAdapter.PerformExistViewHolder) {
            ((PerformOfMoumAdapter.PerformExistViewHolder) holder).bind(performanceHall);
        } else if (holder instanceof PerformOfMoumAdapter.PerformExistNotVerifiedViewHolder) {
            ((PerformOfMoumAdapter.PerformExistNotVerifiedViewHolder) holder).bind(performanceHall);
        }
    }

    @Override
    public int getItemCount() {
        return performanceHalls.size();
    }

    static class PerformExistViewHolder extends RecyclerView.ViewHolder {
        private PerformanceHall performanceHall;
        private TextView performanceHallName;
        private TextView performanceHallDescription;
        private TextView performanceHallAddress;
        private TextView performanceHallPrice;
        private ImageView performanceHallProfile;
        private ConstraintLayout performanceHallTop;
        private MoumListPerformanceHallActivity activity;
        private Context context;

        public PerformExistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            performanceHallName = itemView.findViewById(R.id.performance_hall_name);
            performanceHallDescription = itemView.findViewById(R.id.performance_hall_description);
            performanceHallAddress = itemView.findViewById(R.id.performance_hall_address);
            performanceHallPrice = itemView.findViewById(R.id.performance_hall_price);
            performanceHallProfile = itemView.findViewById(R.id.performance_hall_image);
            performanceHallTop = itemView.findViewById(R.id.top_performance_hall);
            this.activity = (MoumListPerformanceHallActivity) context;
            this.context = context;
        }

        public void bind(PerformanceHall performanceHall) {
            this.performanceHall = performanceHall;
            if (performanceHall.getName() != null) performanceHallName.setText(performanceHall.getName());
            if (performanceHall.getDetails() != null) performanceHallDescription.setText(performanceHall.getDetails());
            if (performanceHall.getAddress() != null) performanceHallAddress.setText(performanceHall.getAddress());
            if (performanceHall.getPrice() != null) performanceHallPrice.setText(String.format("시간 당 %,d원", performanceHall.getPrice()));
            if (performanceHall.getImageUrls() != null && !performanceHall.getImageUrls().isEmpty()) {
                Glide.with(context)
                        .load(performanceHall.getImageUrls().get(0))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                                .error(R.drawable.background_more_rounded_gray_size_fit))
                        .into(performanceHallProfile);
            }
            performanceHallProfile.setClipToOutline(true);
            performanceHallTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onPerformanceHallClicked(performanceHall.getId());
                }
            });
        }

    }

    static class PerformEmptyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout performanceHallTop;
        private MoumListPerformanceHallActivity activity;
        private Context context;

        public PerformEmptyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            performanceHallTop = itemView.findViewById(R.id.top_performance_hall);
            this.activity = (MoumListPerformanceHallActivity) context;
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind() {
            performanceHallTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onPerformFindButtonClicked();
                }
            });
        }
    }

    static class PerformExistNotVerifiedViewHolder extends RecyclerView.ViewHolder {
        private PerformanceHall performanceHall;
        private TextView performanceHallName;
        private TextView performanceHallDescription;
        private TextView performanceHallAddress;
        private TextView performanceHallPrice;
        private ImageView performanceHallProfile;
        private ConstraintLayout performanceHallTop;
        private MoumListPerformanceHallActivity activity;
        private Context context;

        public PerformExistNotVerifiedViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            performanceHallName = itemView.findViewById(R.id.performance_hall_name);
            performanceHallDescription = itemView.findViewById(R.id.performance_hall_description);
            performanceHallAddress = itemView.findViewById(R.id.performance_hall_address);
            performanceHallPrice = itemView.findViewById(R.id.performance_hall_price);
            performanceHallProfile = itemView.findViewById(R.id.performance_hall_image);
            performanceHallTop = itemView.findViewById(R.id.top_performance_hall);
            this.activity = (MoumListPerformanceHallActivity) context;
            this.context = context;
        }

        public void bind(PerformanceHall performanceHall) {
            this.performanceHall = performanceHall;
            if (performanceHall.getName() != null) {
                performanceHallName.setText(performanceHall.getName());
            }
            performanceHallDescription.setVisibility(View.INVISIBLE);
            performanceHallAddress.setVisibility(View.INVISIBLE);
            performanceHallPrice.setVisibility(View.INVISIBLE);
            performanceHallProfile.setVisibility(View.GONE);

        }
    }

}
