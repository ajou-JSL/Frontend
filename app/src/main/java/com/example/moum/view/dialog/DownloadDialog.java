package com.example.moum.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.moum.R;
import com.example.moum.view.moum.MoumCreateActivity;
import com.example.moum.view.moum.MoumPromote2Activity;

public class DownloadDialog extends Dialog {
    private MoumPromote2Activity moumPromote2Activity;
    private TextView textViewSub;
    private TextView textviewMain;
    private ProgressBar progressBar;
    private final String TAG = getClass().toString();

    public DownloadDialog(@NonNull Context context){
        super(context);
        this.moumPromote2Activity = (MoumPromote2Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.drawable.background_rounded_white);
        setContentView(R.layout.dialog_progress);
        textviewMain = findViewById(R.id.textview_dialog_main);
        textViewSub = findViewById(R.id.textview_dialog_sub);
        progressBar = findViewById(R.id.progress_bar);

        textviewMain.setText("QR 다운로드중...");
        textViewSub.setText("최대 5초 정도 소요될 수 있어요!");
    }
}
