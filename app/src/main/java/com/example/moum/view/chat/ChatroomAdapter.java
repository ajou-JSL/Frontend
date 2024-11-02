package com.example.moum.view.chat;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatroomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Chatroom> chatrooms;
    private Context context;

    public void setChatrooms(ArrayList<Chatroom> chatrooms, Context context) {
        this.chatrooms = chatrooms;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom, parent, false);
        return new ChatroomAdapter.ChatroomViewHolder(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chatroom chatroom = chatrooms.get(position);
        ((ChatroomAdapter.ChatroomViewHolder) holder).bind(chatroom);

    }

    @Override
    public int getItemCount() {
        return chatrooms.size();
    }

    static class ChatroomViewHolder extends RecyclerView.ViewHolder{
        private Chatroom chatroom;
        private TextView chatroomName;
        private TextView chatroomContent;
        private TextView chatroomLastTime;
        private ImageView chatroomProfile;
        private Context context;

        public ChatroomViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            chatroomName = itemView.findViewById(R.id.chatroom_name);
            chatroomContent = itemView.findViewById(R.id.chatroom_content);
            chatroomLastTime = itemView.findViewById(R.id.chatroom_lasttime);
            chatroomProfile = itemView.findViewById(R.id.chatroom_profile);
            this.context = context;

            /*클릭 이벤트*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("chatroomId", chatroom.getChatroomId());
                        intent.putExtra("chatroomName", chatroom.getChatroomName());
                        intent.putExtra("chatroomLastTime", chatroomLastTime.getText());
                        intent.putExtra("chatroomProfile", chatroom.getChatroomProfile());
                        intent.putExtra("chatroomType", chatroom.getChatroomType().getValue());
                        intent.putExtra("chatroomLeader", chatroom.getChatroomLeader());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Chatroom chatroom){
            this.chatroom = chatroom;
            chatroomName.setText(chatroom.getChatroomName());
            chatroomContent.setText(chatroom.getChatroomContent());
            String formatTime = chatroom.getChatroomLastTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            chatroomLastTime.setText(formatTime);
            Uri profileUri = chatroom.getChatroomProfile();
            if(profileUri != null)
                Glide.with(context).load(profileUri).into(chatroomProfile);
        }
    }
}
