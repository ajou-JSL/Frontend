package com.example.moum.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.moum.R;
import com.example.moum.view.chat.ChatActivity;
import com.example.moum.view.moum.MoumCreateActivity;

public class ChatroomLeaveDialog extends Dialog {
    private ChatActivity chatActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String chatroomName;
    private final String TAG = getClass().toString();

    public ChatroomLeaveDialog(@NonNull Context context, String chatroomName){
        super(context);
        this.chatActivity = (ChatActivity) context;
        this.chatroomName = chatroomName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.drawable.background_rounded_white);
        setContentView(R.layout.dialog_basic);
        textviewMain = findViewById(R.id.textview_dialog_main);
        textViewSub = findViewById(R.id.textview_dialog_sub);
        buttonNo = findViewById(R.id.button_dialog_no);
        buttonYes = findViewById(R.id.button_dialog_yes);

        textviewMain.setText(String.format("'%s'\n모음톡을 나가시겠어요?", chatroomName));
        textViewSub.setText("나간 채팅방을 다시 복구할 수 없어요.");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatActivity.onChatroomLeaveDialogYesClicked();
                dismiss();
            }
        });
    }
}
