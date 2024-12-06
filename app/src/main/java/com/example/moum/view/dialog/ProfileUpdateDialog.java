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
import com.example.moum.view.myinfo.MyInformationUpdateActivity;

public class ProfileUpdateDialog extends Dialog {
    private MyInformationUpdateActivity myInformationUpdateActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String nickname;
    private final String TAG = getClass().toString();

    public ProfileUpdateDialog(@NonNull Context context, String nickname) {
        super(context);
        this.myInformationUpdateActivity = (MyInformationUpdateActivity) context;
        this.nickname = nickname;
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

        textviewMain.setText(String.format("'%s'\n의 프로필을 수정할까요?", nickname));
        textViewSub.setText("수정사항을 저장할 수 있어요.");
        buttonYes.setText("수정");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myInformationUpdateActivity.onDialogYesClicked();
                dismiss();
            }
        });
    }
}
