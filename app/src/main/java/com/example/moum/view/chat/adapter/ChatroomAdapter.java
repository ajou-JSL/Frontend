package com.example.moum.view.chat.adapter;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.view.chat.ChatActivity;

import java.time.LocalDateTime;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatroom, parent, false);
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
                        intent.putExtra("chatroomId", chatroom.getId());
                        intent.putExtra("chatroomName", chatroom.getName());
                        intent.putExtra("chatroomType", chatroom.getType().getValue());
                        intent.putExtra("teamId", chatroom.getTeamId());
                        intent.putExtra("leaderId", chatroom.getLeaderId());
                        intent.putExtra("lastChat", chatroom.getLastChat());
                        intent.putExtra("lastTimestamp", chatroom.getLastTimestamp());
                        intent.putExtra("fileUrl", chatroom.getFileUrl());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Chatroom chatroom){
            this.chatroom = chatroom;
            chatroomName.setText(chatroom.getName());
            if(chatroom.getLastChat() != null) chatroomContent.setText(chatroom.getLastChat());
            else chatroomContent.setText(" ");
            if(chatroom.getLastTimestamp() != null && !chatroom.getLastTimestamp().isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(chatroom.getLastTimestamp());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = dateTime.format(formatter);
                chatroomLastTime.setText(formattedDate);
            }
            else{
                chatroomLastTime.setText(" ");
            }
            if(chatroom.getFileUrl() != null)
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.background_circle_gray_size_fit)
                        .error(R.drawable.background_circle_gray_size_fit))
                        .load(chatroom.getFileUrl()).into(chatroomProfile);
        }
    }
}
