package com.example.moum.view.moum.adapter;

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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.view.moum.MoumCreateActivity;
import com.example.moum.view.moum.MoumManageActivity;

import java.util.ArrayList;

public class MoumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Moum> moums;
    private Integer teamId;
    private Context context;
    private ActivityResultLauncher<Intent> launcher;
    private static final int VIEW_TYPE_EXIST = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    public void setMoums(ArrayList<Moum> moums, Integer teamId, Context context, ActivityResultLauncher<Intent> launcher) {
        this.moums = moums;
        this.teamId = teamId;
        this.context = context;
        this.launcher = launcher;
    }

    @Override
    public int getItemViewType(int position) {
        return position == moums.size() - 1 ? VIEW_TYPE_EMPTY : VIEW_TYPE_EXIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_empty, parent, false);
            return new MoumAdapter.MoumEmptyViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum, parent, false);
            return new MoumAdapter.MoumExistViewHolder(view, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Moum moum = moums.get(position);
        if (holder instanceof MoumAdapter.MoumEmptyViewHolder) {
            ((MoumAdapter.MoumEmptyViewHolder) holder).bind(moum, teamId);
        } else if (holder instanceof MoumAdapter.MoumExistViewHolder) {
            ((MoumAdapter.MoumExistViewHolder) holder).bind(moum, teamId, launcher);
        }
    }

    @Override
    public int getItemCount() {
        return moums.size();
    }

    static class MoumExistViewHolder extends RecyclerView.ViewHolder {
        private Moum moum;
        private Integer teamId;
        private TextView moumName;
        private TextView moumPlace;
        private TextView moumDate;
        private TextView moumState;
        private ProgressBar moumProgress;
        private ConstraintLayout topView;
        private Context context;

        public MoumExistViewHolder(@NonNull View itemView, Context context) {
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
        public void bind(Moum moum, Integer teamId, ActivityResultLauncher<Intent> launcher) {
            this.moum = moum;
            this.teamId = teamId;
            moumName.setText(moum.getMoumName());
            if (moum.getPerformLocation() != null && !moum.getPerformLocation().isEmpty()) moumPlace.setText(moum.getPerformLocation());
            if (moum.getStartDate() != null && !moum.getStartDate().isEmpty() && moum.getEndDate() != null && !moum.getEndDate().isEmpty()) {
                moumDate.setText(String.format("%s ~ %s", moum.getStartDate(), moum.getEndDate()));
            } else if (moum.getStartDate() != null && !moum.getStartDate().isEmpty() && (moum.getEndDate() == null || moum.getEndDate().isEmpty())) {
                moumDate.setText(String.format("%s", moum.getStartDate()));
            } else if (moum.getEndDate() != null && !moum.getEndDate().isEmpty() && (moum.getStartDate() == null || moum.getStartDate().isEmpty())) {
                moumDate.setText(String.format("%s", moum.getEndDate()));
            } else {
                moumDate.setText("");
            }
            if (moum.getProcess().getFinishStatus()) {
                moumState.setText("완료");
                topView.setBackground(AppCompatResources.getDrawable(context, R.drawable.background_more_rounded_gray2));
            } else {
                moumState.setText("진행중");
            }
            moumProgress.setProgress(moum.getProcess().getProcessPercentage());
            topView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MoumManageActivity.class);
                    intent.putExtra("moumId", moum.getMoumId());
                    launcher.launch(intent);
                }
            });
        }

    }

    static class MoumEmptyViewHolder extends RecyclerView.ViewHolder {
        private Moum moum;
        private Integer teamId;
        private Context context;
        private ConstraintLayout moumCreateButton;

        public MoumEmptyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            moumCreateButton = itemView.findViewById(R.id.button_moum_create);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Moum moum, Integer teamId) {
            this.moum = moum;
            this.teamId = teamId;
            moumCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MoumCreateActivity.class);
                    intent.putExtra("teamId", teamId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
