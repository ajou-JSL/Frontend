package com.example.moum.view.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.view.home.HomeFragment;
import com.example.moum.view.moum.MoumManageActivity;
import com.example.moum.view.moum.adapter.MoumAdapter;

import java.util.ArrayList;

public class HomeMoumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Moum> moums;
    private Context context;

    public void setMoums(ArrayList<Moum> moums, Context context) {
        this.moums = moums;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_small, parent, false);
        return new HomeMoumAdapter.HomeMoumViewModel(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Moum moum = moums.get(position);
        ((HomeMoumAdapter.HomeMoumViewModel) holder).bind(moum);

    }

    @Override
    public int getItemCount() {
        return moums.size();
    }

    static class HomeMoumViewModel extends RecyclerView.ViewHolder{
        private Moum moum;
        private TextView moumName;
        private TextView moumPlace;
        private TextView moumDate;
        private TextView moumState;
        private ProgressBar moumProgress;
        private ConstraintLayout topView;
        private Context context;

        public HomeMoumViewModel(@NonNull View itemView, Context context) {
            super(itemView);
            moumName = itemView.findViewById(R.id.moum_name);
            moumPlace = itemView.findViewById(R.id.moum_place);
            moumDate = itemView.findViewById(R.id.moum_date);
            moumState = itemView.findViewById(R.id.moum_state);
            moumProgress = itemView.findViewById(R.id.moum_progress);
            topView = itemView.findViewById(R.id.constraint_item_moum_top);
            this.context = context;
        }

        @SuppressLint("DefaultLocale")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Moum moum) {
            this.moum = moum;
            moumName.setText(moum.getMoumName());
            if(moum.getPerformLocation() != null && !moum.getPerformLocation().isEmpty()) moumPlace.setText(moum.getPerformLocation());
            if(moum.getStartDate() != null && !moum.getStartDate().isEmpty() && moum.getEndDate() != null && !moum.getEndDate().isEmpty())
                moumDate.setText(String.format("%s ~ %s", moum.getStartDate(), moum.getEndDate()));
            else if(moum.getStartDate() != null && !moum.getStartDate().isEmpty() && (moum.getEndDate() == null || moum.getEndDate().isEmpty()))
                moumDate.setText(String.format("%s", moum.getStartDate()));
            else if(moum.getEndDate() != null && !moum.getEndDate().isEmpty() && (moum.getStartDate() == null || moum.getStartDate().isEmpty()))
                moumDate.setText(String.format("%s", moum.getEndDate()));
            else
                moumDate.setText("");
            if(moum.getProcess().getFinishStatus())
                moumState.setText("완료");
            else
                moumState.setText("진행중");
            moumProgress.setProgress(moum.getProcess().getProcessPercentage());
            topView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MoumManageActivity.class);
                    intent.putExtra("moumId", moum.getMoumId());
                    context.startActivity(intent);
                }
            });
        }

    }
}
