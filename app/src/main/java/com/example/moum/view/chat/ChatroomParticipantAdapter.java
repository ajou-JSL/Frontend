package com.example.moum.view.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.moum.data.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatroomParticipantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<User> participants;
    private static ArrayList<Boolean> isParticipates;
    private Context context;

    public void setParticipants(ArrayList<User> participants, Context context) {
        this.participants = participants;
        isParticipates = new ArrayList<>(Collections.nCopies(participants.size(), false));
        this.context = context;
    }

    public static ArrayList<Boolean> getIsParticipates() {
        return isParticipates;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_participant, parent, false);
        return new ChatroomParticipantAdapter.ChatroomParticipantViewHolder(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User participant = participants.get(position);
        ((ChatroomParticipantAdapter.ChatroomParticipantViewHolder) holder).bind(participant);

    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    static class ChatroomParticipantViewHolder extends RecyclerView.ViewHolder{
        private User participant;
        private TextView participantName;
        private CircleImageView participantProfile;
        private Context context;

        public ChatroomParticipantViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            participantName = itemView.findViewById(R.id.participant_name);
            participantProfile = itemView.findViewById(R.id.participant_profile);
            this.context = context;

            /*클릭 이벤트*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Boolean isParticipate = isParticipates.get(pos);
                        if(isParticipate){
                            participantProfile.setBorderWidth(0);
                            isParticipates.set(pos, false);
                        }
                        else{
                            participantProfile.setBorderWidth(3);
                            participantProfile.setBorderColor(Color.rgb(42, 200, 189));
                            isParticipates.set(pos, true);
                        }

                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(User participant){
            this.participant = participant;
            participantName.setText(participant.getNickname());
            Glide.with(context).load(participant.getProfileImage()).into(participantProfile);
        }
    }
}
