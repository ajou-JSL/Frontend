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
import com.example.moum.view.moum.MoumPaymentAddActivity;

public class SettlementCreateDialog extends Dialog {
    private MoumPaymentAddActivity moumPaymentAddActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String settlementName;
    private final String TAG = getClass().toString();
    private Context context;

    public SettlementCreateDialog(@NonNull Context context, String settlementName) {
        super(context);
        this.moumPaymentAddActivity = (MoumPaymentAddActivity) context;
        this.settlementName = settlementName;
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

        textviewMain.setText(String.format("'%s'\n정산 내역을 생성하시겠습니까?", settlementName));
        textViewSub.setText("정산 내역을 생성하세요!");
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
                moumPaymentAddActivity.onSettlementCreateDialogYesClicked();
                dismiss();
            }
        });
    }
}
