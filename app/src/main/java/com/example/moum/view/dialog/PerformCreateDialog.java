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
import com.example.moum.view.moum.MoumUpdateActivity;

public class PerformCreateDialog extends Dialog {
    private PerformanceCreateOnwardActivity performanceCreateOnwardActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String performName;
    private final String TAG = getClass().toString();

    public PerformCreateDialog(@NonNull Context context, String performName){
        super(context);
        this.performanceCreateOnwardActivity = (PerformanceCreateOnwardActivity) context;
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

        textviewMain.setText(String.format("'%s'\n공연 게시글을 생성할까요?", performName));
        textViewSub.setText("나의 단체와 관련된 공연 게시글을 생성하세요!");
        buttonYes.setText("생성");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performanceCreateOnwardActivity.onDialogYesClicked();
                dismiss();
            }
        });
    }
}
