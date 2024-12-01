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
import com.example.moum.view.community.PerformanceCreateOnwardActivity;
import com.example.moum.view.moum.MoumMapPerformanceHallActivity;

public class PerformOfMoumCreateDialog extends Dialog {
    private MoumMapPerformanceHallActivity activity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String performName;
    private final String TAG = getClass().toString();

    public PerformOfMoumCreateDialog(@NonNull Context context, String performName){
        super(context);
        this.activity = (MoumMapPerformanceHallActivity) context;
        this.performName = performName;
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

        textviewMain.setText(String.format("'%s'\n공연장을 나의 모음에 등록할까요?", performName));
        textViewSub.setText("나의 모음에 공연장을 등록하여, 멤버들과 정보를 공유할 수 있어요!");
        buttonYes.setText("등록");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onPerformOfMoumCreateDialogYesClicked();
                dismiss();
            }
        });
    }
}
