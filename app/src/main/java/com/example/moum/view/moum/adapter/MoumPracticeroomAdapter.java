package com.example.moum.view.moum.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.view.moum.MoumFindPracticeroomActivity;
import com.example.moum.view.moum.MoumUpdateActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MoumPracticeroomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Practiceroom> practicerooms;
    private Context context;

    public void setPracticerooms(ArrayList<Practiceroom> practicerooms, @NonNull Context context) {
        this.practicerooms = practicerooms;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_practiceroom, parent, false);
        return new MoumPracticeroomAdapter.MoumPracticeroomViewHolder(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Practiceroom practiceroom = practicerooms.get(position);
        ((MoumPracticeroomAdapter.MoumPracticeroomViewHolder) holder).bind(practiceroom);
    }

    @Override
    public int getItemCount() {
        return practicerooms.size();
    }

    static class MoumPracticeroomViewHolder extends RecyclerView.ViewHolder{
        private Practiceroom practiceroom;
        private TextView practiceroomName;
        private TextView practiceroomDescription;
        private TextView practiceroomAddress;
        private TextView practiceroomPrice;
        private ImageView practiceromProfile;
        private ConstraintLayout practiceroomTop;
        private MoumFindPracticeroomActivity activity;
        private Context context;

        public MoumPracticeroomViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            practiceroomName = itemView.findViewById(R.id.practiceroom_name);
            practiceroomDescription = itemView.findViewById(R.id.practiceroom_description);
            practiceroomAddress = itemView.findViewById(R.id.practiceroom_address);
            practiceroomPrice = itemView.findViewById(R.id.practiceroom_price);
            practiceromProfile = itemView.findViewById(R.id.practiceroom_image);
            practiceroomTop = itemView.findViewById(R.id.top_practiceroom);
            this.activity = (MoumFindPracticeroomActivity) context;
            this.context = context;
        }

        public void bind(Practiceroom practiceroom) {
            this.practiceroom = practiceroom;
            practiceroomName.setText(practiceroom.getName());
            practiceroomDescription.setText(practiceroom.getDetails());
            practiceroomAddress.setText(practiceroom.getAddress());
            practiceroomPrice.setText(practiceroom.getPrice());
            Glide.with(context)
                    .load(practiceroom.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                            .error(R.drawable.background_more_rounded_gray_size_fit))
                    .into(practiceromProfile);
            practiceroomTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onPracticeroomClicked(practiceroom.getId());
                }
            });
        }

    }
}
