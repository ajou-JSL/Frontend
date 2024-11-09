package com.example.moum.view.chat.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Team;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatroomParticipantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Member> participants;
    private ArrayList<Boolean> isParticipates;
    private Context context;
    private Integer leaderId;

    public void setParticipants(ArrayList<Member> participants, Integer leaderId, Context context) {
        this.participants = participants;
        this.leaderId = leaderId;
        this.context = context;
    }

    public ArrayList<Boolean> getIsParticipates() {
        Log.e(TAG, isParticipates.isEmpty()? "empty" : "not");
        return isParticipates;
    }

    public void setIsParticipate(int pos, Boolean isParticipate) {
        if(!isParticipates.isEmpty()){
            isParticipates.set(pos, isParticipate);
        }
    }

    public Boolean getIsParticipate(int pos){
        if(!isParticipates.isEmpty()){
            return isParticipates.get(pos);
        }
        return false;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isParticipates == null) isParticipates = new ArrayList<>(Collections.nCopies(participants.size(), false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_participant, parent, false);
        return new ChatroomParticipantAdapter.ChatroomParticipantViewHolder(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Member participant = participants.get(position);
        ((ChatroomParticipantAdapter.ChatroomParticipantViewHolder) holder).bind(participant);

    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    static class ChatroomParticipantViewHolder extends RecyclerView.ViewHolder{
        private ChatroomParticipantAdapter adapter;
        private Member participant;
        private TextView participantName;
        private CircleImageView participantProfile;
        private TextView errorParticipants;
        private Context context;

        public ChatroomParticipantViewHolder(@NonNull View itemView, Context context, ChatroomParticipantAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            participantName = itemView.findViewById(R.id.participant_name);
            participantProfile = itemView.findViewById(R.id.participant_profile);
            errorParticipants = itemView.findViewById(R.id.error_moumtalk_participants);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Member participant){
            this.participant = participant;
            int pos = getAbsoluteAdapterPosition();

            // 리더인 경우
            if(participant.getId() == adapter.leaderId){
                participantName.setText(participant.getName());
                participantName.setTextColor(Color.rgb(42, 200, 189));
                Glide.with(context).load(participant.getProfileImageUrl()).into(participantProfile);
                participantProfile.setBorderWidth(3);
                participantProfile.setBorderColor(Color.rgb(167, 209, 206));
                if (pos != RecyclerView.NO_POSITION) {
                    adapter.setIsParticipate(pos, true);
                }
            }
            //리더가 아닌 경우
            else{
                participantName.setText(participant.getName());
                Glide.with(context).load(participant.getProfileImageUrl()).into(participantProfile);

                participantProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pos != RecyclerView.NO_POSITION) {
                            Boolean isParticipate = adapter.getIsParticipate(pos);
                            if(isParticipate){
                                participantProfile.setBorderWidth(0);
                                adapter.setIsParticipate(pos, false);
                            }
                            else{
                                participantProfile.setBorderWidth(3);
                                participantProfile.setBorderColor(Color.rgb(42, 200, 189));
                                adapter.setIsParticipate(pos, true);
                            }
                            errorParticipants.setText("");
                        }
                    }
                });
            }

        }
    }
}
