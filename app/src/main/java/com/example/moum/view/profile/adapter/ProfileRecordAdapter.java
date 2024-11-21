package com.example.moum.view.profile.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Record;
import com.example.moum.view.chat.ChatActivity;
import com.example.moum.view.chat.adapter.ChatroomAdapter;

import java.util.ArrayList;

public class ProfileRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Record> records;

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_record, parent, false);
        return new ProfileRecordAdapter.ProfileRecordViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Record record = records.get(position);
        ((ProfileRecordAdapter.ProfileRecordViewHolder) holder).bind(record);

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ProfileRecordViewHolder extends RecyclerView.ViewHolder{
        private Record record;
        private TextView recordName;
        private TextView recordStartDate;
        private TextView recordEndDate;
        private TextView recordCenterWave;

        public ProfileRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            recordName = itemView.findViewById(R.id.record_name);
            recordStartDate = itemView.findViewById(R.id.record_start_date);
            recordEndDate = itemView.findViewById(R.id.record_end_date);
            recordCenterWave = itemView.findViewById(R.id.record_center_wave);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Record record){
            this.record = record;
            recordName.setText(record.getRecordName());
            if(record.getStartDate() != null && !record.getStartDate().isEmpty()) recordStartDate.setText(record.getStartDate());
            if(record.getEndDate() == null || record.getEndDate().isEmpty()){
                recordCenterWave.setText("");
                recordEndDate.setText("");
            }
            else{
                recordEndDate.setText(record.getStartDate());
            }
        }
    }
}
