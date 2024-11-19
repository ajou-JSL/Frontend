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
import com.example.moum.view.moum.MoumCreateActivity;
import com.example.moum.view.moum.MoumUpdateActivity;

public class MoumUpdateDialog extends Dialog {
    private MoumUpdateActivity moumUpdateActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String moumName;
    private final String TAG = getClass().toString();

    public MoumUpdateDialog(@NonNull Context context, String moumName){
        super(context);
        this.moumUpdateActivity = (MoumUpdateActivity) context;
        this.moumName = moumName;
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

        textviewMain.setText(String.format("'%s'\n모음을 수정할까요?", moumName));
        textViewSub.setText("수정사항을 저장할 수 있어요.");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moumUpdateActivity.onDialogYesClicked();
                dismiss();
            }
        });
    }
}
