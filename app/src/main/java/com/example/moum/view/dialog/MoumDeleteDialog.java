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
import com.example.moum.view.moum.MoumUpdateActivity;

public class MoumDeleteDialog extends Dialog {
    private MoumManageActivity moumManageActivity;
    private TextView textviewMain;
    private TextView textViewSub;
    private AppCompatButton buttonNo;
    private AppCompatButton buttonYes;
    private String moumName;
    private final String TAG = getClass().toString();
    private Context context;

    public MoumDeleteDialog(@NonNull Context context, String moumName){
        super(context);
        this.moumManageActivity = (MoumManageActivity) context;
        this.moumName = moumName;
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

        textviewMain.setText(String.format("'%s'\n모음을 삭제하시겠습니까?", moumName));
        textViewSub.setText("※ 삭제된 모음은 다시 복구할 수 없습니다.");
        textViewSub.setTextColor(context.getColor(R.color.red));
        buttonYes.setText("삭제");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moumManageActivity.onMoumDeleteDialogYesClicked();
                dismiss();
            }
        });
    }
}
