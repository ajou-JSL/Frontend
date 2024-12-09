package com.example.moum.view.chat.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.utils.ImageManager;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatroomInviteMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Member> participants;
    private ArrayList<Boolean> isAlreadyParticipates;
    private ArrayList<Boolean> isParticipates;
    private Context context;
    private Integer leaderId;
    private final ArrayList<Member> membersToInvite = new ArrayList<>();

    public void setParticipants(ArrayList<Member> participants, Integer leaderId, Context context) {
        this.participants = participants;
        this.leaderId = leaderId;
        this.context = context;
    }

    public ArrayList<Boolean> getIsParticipates() {
        Log.e(TAG, isParticipates.isEmpty() ? "empty" : "not");
        return isParticipates;
    }

    public void setIsParticipate(int pos, Boolean isParticipate) {
        if (!isParticipates.isEmpty()) {
            isParticipates.set(pos, isParticipate);
        }
    }

    public Boolean getIsParticipate(int pos) {
        if (!isParticipates.isEmpty()) {
            return isParticipates.get(pos);
        }
        return false;
    }

    public ArrayList<Boolean> getIsAlreadyParticipates() {
        Log.e(TAG, isAlreadyParticipates.isEmpty() ? "empty" : "not");
        return isAlreadyParticipates;
    }

    public void setIsAlreadyParticipate(int pos, Boolean isAlreadyParticipate) {
        if (!isAlreadyParticipates.isEmpty()) {
            isAlreadyParticipates.set(pos, isAlreadyParticipate);
        }
    }

    public Boolean getIsAlreadyParticipate(int pos) {
        if (!isAlreadyParticipates.isEmpty()) {
            return isAlreadyParticipates.get(pos);
        }
        return false;
    }

    public ArrayList<Member> getMembersToInvite() {
        membersToInvite.clear();
        if (participants == null || isAlreadyParticipates == null || isParticipates == null) return membersToInvite;
        for (Member participant : participants) {
            int idx = participants.indexOf(participant);
            if (isParticipates.get(idx) && !isAlreadyParticipates.get(idx)) {
                membersToInvite.add(participant);
            }
        }
        return membersToInvite;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isParticipates == null) isParticipates = new ArrayList<>(Collections.nCopies(participants.size(), false));
        if (isAlreadyParticipates == null) isAlreadyParticipates = new ArrayList<>(Collections.nCopies(participants.size(), true));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ChatroomInviteMemberAdapter.ChatroomParticipantViewHolder(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Member participant = participants.get(position);
        ((ChatroomInviteMemberAdapter.ChatroomParticipantViewHolder) holder).bind(participant);

    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    static class ChatroomParticipantViewHolder extends RecyclerView.ViewHolder {
        private ChatroomInviteMemberAdapter adapter;
        private Member participant;
        private TextView participantName;
        private CircleImageView participantProfile;
        private Context context;

        public ChatroomParticipantViewHolder(@NonNull View itemView, Context context, ChatroomInviteMemberAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            participantName = itemView.findViewById(R.id.participant_name);
            participantProfile = itemView.findViewById(R.id.participant_profile);
            this.context = context;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Member participant) {
            this.participant = participant;
            int pos = getAbsoluteAdapterPosition();
            participantName.setText(participant.getName());
            if (ImageManager.isUrlValid(participant.getProfileImageUrl())) {
                Glide.with(context)
                        .load(participant.getProfileImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.background_circle_gray_size_fit)
                                .error(R.drawable.background_circle_gray_size_fit))
                        .into(participantProfile);
            }
            // 리더인 경우
            if (participant.getId().equals(adapter.leaderId)) {
                participantName.setTextColor(Color.rgb(42, 200, 189));
                participantProfile.setBorderWidth(8);
                participantProfile.setBorderColor(Color.rgb(167, 209, 206));
                if (pos != RecyclerView.NO_POSITION) {
                    adapter.setIsParticipate(pos, true);
                    adapter.setIsAlreadyParticipate(pos, true);
                }
            }
            //리더가 아닌 경우
            else {
                if (pos != RecyclerView.NO_POSITION) {
                    //이미 모음톡에 참여한 경우
                    if (adapter.getIsAlreadyParticipate(pos)) {
                        participantProfile.setBorderWidth(8);
                        participantProfile.setBorderColor(Color.rgb(167, 209, 206));
                        participantName.setTextColor(Color.rgb(42, 200, 189));
                        adapter.setIsParticipate(pos, true);
                        itemView.setOnClickListener(null);
                    }
                    //그렇지 않은 경우
                    else {
                        adapter.setIsParticipate(pos, false);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (adapter.getIsParticipate(pos)) {
                                    participantProfile.setBorderWidth(0);
                                    adapter.setIsParticipate(pos, false);
                                } else {
                                    participantProfile.setBorderWidth(8);
                                    participantProfile.setBorderColor(Color.rgb(167, 209, 206));
                                    adapter.setIsParticipate(pos, true);
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
