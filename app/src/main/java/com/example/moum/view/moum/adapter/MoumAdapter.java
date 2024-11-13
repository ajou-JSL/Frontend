package com.example.moum.view.moum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;
import com.example.moum.view.moum.MoumCreateActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MoumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Moum> moums;
    private Integer teamId;
    private Context context;
    private static final int VIEW_TYPE_EXIST = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    public void setMoums(ArrayList<Moum> moums, Integer teamId, Context context) {
        this.moums = moums;
        this.teamId = teamId;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position == moums.size()-1? VIEW_TYPE_EMPTY : VIEW_TYPE_EXIST;
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
            ((MoumAdapter.MoumExistViewHolder) holder).bind(moum, teamId);
        }
    }

    @Override
    public int getItemCount() {
        return moums.size();
    }

    static class MoumExistViewHolder extends RecyclerView.ViewHolder{
        private Moum moum;
        private Integer teamId;
        private TextView moumName;
        private TextView moumPlace;
        private TextView moumDate;
        private TextView moumState;
        private ProgressBar moumProgress;
        private Context context;

        public MoumExistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            moumName = itemView.findViewById(R.id.moum_name);
            moumPlace = itemView.findViewById(R.id.moum_place);
            moumDate = itemView.findViewById(R.id.moum_date);
            moumState = itemView.findViewById(R.id.moum_state);
            moumProgress = itemView.findViewById(R.id.moum_progress);
            this.context = context;
        }

        @SuppressLint("DefaultLocale")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Moum moum, Integer teamId) {
            this.moum = moum;
            this.teamId = teamId;
            moumName.setText(moum.getMoumName());
            if(!moum.getPerformLocation().isEmpty()) moumPlace.setText(moum.getPerformLocation());
            if(!moum.getStartDate().isEmpty() && !moum.getEndDate().isEmpty())
                moumDate.setText(String.format("%s ~ %s", moum.getStartDate(), moum.getEndDate()));
            else if(!moum.getStartDate().isEmpty() && moum.getEndDate().isEmpty())
                moumDate.setText(String.format("%s", moum.getStartDate()));
            else if(moum.getStartDate().isEmpty() && !moum.getEndDate().isEmpty())
                moumDate.setText(String.format("%s", moum.getEndDate()));
            else
                moumDate.setText("");
            if(moum.getProcess().getFinishStatus())
                moumState.setText("완료");
            else
                moumState.setText("진행중");
            moumProgress.setProgress(moum.getProcess().getProcessPercentage());
        }

    }

    static class MoumEmptyViewHolder extends RecyclerView.ViewHolder{
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
        public void bind(Moum moum, Integer teamId){
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
