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
import com.example.moum.view.chat.InviteActivity;
import com.example.moum.view.moum.MoumCreateActivity;

public class TeamInviteDialog extends Dialog {
    private InviteActivity inviteActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String memberName;
    private String teamName;
    private final String TAG = getClass().toString();

    public TeamInviteDialog(@NonNull Context context, String memberName, String teamName){
        super(context);
        this.inviteActivity = (InviteActivity) context;
        this.memberName = memberName;
        this.teamName = teamName;
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

        textviewMain.setText(String.format("'%s'를\n'%s'에 초대할까요?", memberName, teamName));
        textViewSub.setText("");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteActivity.onDialogYesClicked();
                dismiss();
            }
        });
    }
}
