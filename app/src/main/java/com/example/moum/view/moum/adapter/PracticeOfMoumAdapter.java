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
import com.example.moum.view.moum.MoumFindPracticeroomActivity;
import com.example.moum.view.moum.MoumListPerformanceHallActivity;
import com.example.moum.view.moum.MoumListPracticeroomActivity;

import java.util.ArrayList;

public class PracticeOfMoumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Practiceroom> practicerooms;
    private Context context;
    private static final int VIEW_TYPE_EXIST = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private static final int VIEW_TYPE_EXIST_NOT_VERIFIED = 3;

    public void setPracticerooms(ArrayList<Practiceroom> practicerooms, Context context) {
        this.practicerooms = practicerooms;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(practicerooms.get(position) == null || (practicerooms.get(position).getId() == null && practicerooms.get(position).getName() == null))
            return VIEW_TYPE_EMPTY;
        else if(practicerooms.get(position).getId() == null && practicerooms.get(position).getName() != null)
            return VIEW_TYPE_EXIST_NOT_VERIFIED;
        else
            return VIEW_TYPE_EXIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_practice_of_moum_empty, parent, false);
            return new PracticeOfMoumAdapter.PracticeEmptyViewHolder(view, context);
        }
        else if (viewType == VIEW_TYPE_EXIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_practice_of_moum, parent, false);
            return new PracticeOfMoumAdapter.PracticemExistViewHolder(view, context);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_practice_of_moum, parent, false);
            return new PracticeOfMoumAdapter.PracticemExistNotVerifiedViewHolder(view, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Practiceroom practiceroom = practicerooms.get(position);
        if (holder instanceof PracticeOfMoumAdapter.PracticeEmptyViewHolder) {
            ((PracticeOfMoumAdapter.PracticeEmptyViewHolder) holder).bind();
        } else if (holder instanceof PracticeOfMoumAdapter.PracticemExistViewHolder) {
            ((PracticeOfMoumAdapter.PracticemExistViewHolder) holder).bind(practiceroom);
        }
        else if (holder instanceof PracticeOfMoumAdapter.PracticemExistNotVerifiedViewHolder) {
            ((PracticeOfMoumAdapter.PracticemExistNotVerifiedViewHolder) holder).bind(practiceroom);
        }
    }

    @Override
    public int getItemCount() {
        return practicerooms.size();
    }

    static class PracticemExistViewHolder extends RecyclerView.ViewHolder{
        private Practiceroom practiceroom;
        private TextView practiceroomName;
        private TextView practiceroomDescription;
        private TextView practiceroomAddress;
        private TextView practiceroomPrice;
        private ImageView practiceromProfile;
        private ConstraintLayout practiceroomTop;
        private MoumListPracticeroomActivity activity;
        private Context context;

        public PracticemExistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            practiceroomName = itemView.findViewById(R.id.practiceroom_name);
            practiceroomDescription = itemView.findViewById(R.id.practiceroom_description);
            practiceroomAddress = itemView.findViewById(R.id.practiceroom_address);
            practiceroomPrice = itemView.findViewById(R.id.practiceroom_price);
            practiceromProfile = itemView.findViewById(R.id.practiceroom_image);
            practiceroomTop = itemView.findViewById(R.id.top_practiceroom);
            this.activity = (MoumListPracticeroomActivity) context;
            this.context = context;
        }

        public void bind(Practiceroom practiceroom) {
            this.practiceroom = practiceroom;
            if(practiceroom.getName() != null) practiceroomName.setText(practiceroom.getName());
            if(practiceroom.getDetails() != null) practiceroomDescription.setText(practiceroom.getDetails());
            if(practiceroom.getAddress() != null) practiceroomAddress.setText(practiceroom.getAddress());
            if(practiceroom.getPrice() != null) practiceroomPrice.setText(String.format("시간 당 %s원", practiceroom.getPrice()));
            if(practiceroom.getImageUrls() != null && !practiceroom.getImageUrls().isEmpty())
                Glide.with(context)
                        .load(practiceroom.getImageUrls().get(0))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                                .error(R.drawable.background_more_rounded_gray_size_fit))
                        .into(practiceromProfile);
            else{
                practiceromProfile.setVisibility(View.GONE);
            }
            practiceromProfile.setClipToOutline(true);
            practiceroomTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onPracticeroomClicked(practiceroom.getId());
                }
            });
        }

    }

    static class PracticeEmptyViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout practiceroomTop;
        private MoumListPracticeroomActivity activity;
        private Context context;

        public PracticeEmptyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            practiceroomTop = itemView.findViewById(R.id.top_practiceroom);
            this.activity = (MoumListPracticeroomActivity) context;
            this.context = context;
        }

        public void bind(){
            practiceroomTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onPracticeFindButtonClicked();
                }
            });
        }
    }

    static class PracticemExistNotVerifiedViewHolder extends RecyclerView.ViewHolder{
        private Practiceroom practiceroom;
        private TextView practiceroomName;
        private TextView practiceroomDescription;
        private TextView practiceroomAddress;
        private TextView practiceroomPrice;
        private ImageView practiceromProfile;
        private ConstraintLayout practiceroomTop;
        private MoumListPracticeroomActivity activity;
        private Context context;

        public PracticemExistNotVerifiedViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            practiceroomName = itemView.findViewById(R.id.practiceroom_name);
            practiceroomDescription = itemView.findViewById(R.id.practiceroom_description);
            practiceroomAddress = itemView.findViewById(R.id.practiceroom_address);
            practiceroomPrice = itemView.findViewById(R.id.practiceroom_price);
            practiceromProfile = itemView.findViewById(R.id.practiceroom_image);
            practiceroomTop = itemView.findViewById(R.id.top_practiceroom);
            this.activity = (MoumListPracticeroomActivity) context;
            this.context = context;
        }

        public void bind(Practiceroom practiceroom) {
            this.practiceroom = practiceroom;
            if(practiceroom.getName() != null) practiceroomName.setText(practiceroom.getName());
            practiceroomDescription.setVisibility(View.INVISIBLE);
            practiceroomAddress.setVisibility(View.INVISIBLE);
            practiceroomPrice.setVisibility(View.INVISIBLE);
            practiceromProfile.setVisibility(View.GONE);
        }

    }
}
