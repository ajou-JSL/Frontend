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
import com.example.moum.view.chat.ChatUpdateChatroomActivity;

public class ChatroomUpdateDialog extends Dialog {
    private ChatUpdateChatroomActivity chatUpdateChatroomActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String chatroomName;
    private final String TAG = getClass().toString();

    public ChatroomUpdateDialog(@NonNull Context context, String chatroomName) {
        super(context);
        this.chatUpdateChatroomActivity = (ChatUpdateChatroomActivity) context;
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

        textviewMain.setText(String.format("'%s'\n모음톡을 수정하시겠어요?", chatroomName));
        textViewSub.setVisibility(View.GONE);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatUpdateChatroomActivity.onChatroomUpdateDialogYesClicked();
                dismiss();
            }
        });
    }
}
