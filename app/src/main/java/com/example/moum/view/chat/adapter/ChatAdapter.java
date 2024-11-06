package com.example.moum.view.chat.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Chat> chats;
    private Chatroom.ChatroomType chatroomType;
    private static final int VIEW_TYPE_LEFT = 1;
    private static final int VIEW_TYPE_RIGHT = 2;

    public void setChats(ArrayList<Chat> chats, Chatroom.ChatroomType chatroomType) {
        this.chats = chats;
        this.chatroomType = chatroomType;
    }

    @Override
    public int getItemViewType(int position){
        return chats.get(position).isSentByMe()? VIEW_TYPE_RIGHT : VIEW_TYPE_LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left, parent, false);
            return new ChatLeftViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right, parent, false);
            return new ChatRightViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        if (holder instanceof ChatLeftViewHolder) {
            ((ChatLeftViewHolder) holder).bind(chat);
        } else if (holder instanceof ChatRightViewHolder) {
            ((ChatRightViewHolder) holder).bind(chat);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class ChatLeftViewHolder extends RecyclerView.ViewHolder{
        private TextView leftName;
        private TextView leftContent;
        private TextView leftTime;

        public ChatLeftViewHolder(@NonNull View itemView) {
            super(itemView);
            leftName = itemView.findViewById(R.id.left_name);
            leftContent = itemView.findViewById(R.id.left_content);
            leftTime = itemView.findViewById(R.id.left_time);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Chat chat){
            leftName.setText(chat.getSender());
            leftContent.setText(chat.getMessage());
            String formatTime = chat.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm"));
            leftTime.setText(formatTime);
        }
    }

    static class ChatRightViewHolder extends RecyclerView.ViewHolder{
        private TextView rightContent;
        private TextView rightTime;

        public ChatRightViewHolder(@NonNull View itemView){
            super(itemView);
            rightContent = itemView.findViewById(R.id.right_content);
            rightTime = itemView.findViewById(R.id.right_time);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Chat chat){
            rightContent.setText(chat.getMessage());
            String formatTime = chat.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm"));
            rightTime.setText(formatTime);
        }
    }
}
