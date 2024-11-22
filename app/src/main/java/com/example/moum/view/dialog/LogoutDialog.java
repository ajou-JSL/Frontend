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
import com.example.moum.view.moum.MoumManageActivity;
import com.example.moum.view.myinfo.MyInfoLogoutNSignoutActivity;

public class LogoutDialog extends Dialog {
    private MyInfoLogoutNSignoutActivity myInfoLogoutNSignoutActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private final String TAG = getClass().toString();
    private Context context;

    public LogoutDialog(@NonNull Context context){
        super(context);
        this.myInfoLogoutNSignoutActivity = (MyInfoLogoutNSignoutActivity) context;
        this.context = context;
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

        textviewMain.setText("로그아웃 하시겠습니까?");
        textViewSub.setVisibility(View.GONE);
        textViewSub.setTextColor(context.getColor(R.color.red));
        buttonYes.setText("로그아웃");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myInfoLogoutNSignoutActivity.onLogoutDialogYesClicked();
                dismiss();
            }
        });
    }
}
