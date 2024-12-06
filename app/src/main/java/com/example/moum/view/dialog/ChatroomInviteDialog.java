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
import com.example.moum.data.entity.Member;
import com.example.moum.view.chat.ChatInviteMemberActivity;
import com.example.moum.view.chat.ChatUpdateChatroomActivity;

import java.util.ArrayList;

public class ChatroomInviteDialog extends Dialog {
    private ChatInviteMemberActivity activity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String chatroomName;
    private ArrayList<Member> members;
    private final String TAG = getClass().toString();

    public ChatroomInviteDialog(@NonNull Context context, String chatroomName, ArrayList<Member> members) {
        super(context);
        this.activity = (ChatInviteMemberActivity) context;
        this.chatroomName = chatroomName;
        this.members = members;
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

        textviewMain.setText(String.format("'%s'\n채팅방에 멤버를 초대하시겠어요?", chatroomName));
        ArrayList<String> memberNames = new ArrayList<>();
        for (Member member : members) {
            memberNames.add(member.getName());
        }
        textViewSub.setText(String.format("%s\n를 초대합니다.", memberNames.toString()));
        buttonYes.setText("초대");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onChatroomInviteDialogYesClicked();
                dismiss();
            }
        });
    }
}
