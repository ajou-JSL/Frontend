package com.example.moum.view.moum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MoumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Moum> moums;
    private Context context;
    private static final int VIEW_TYPE_EXIST = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    public void setMoums(ArrayList<Moum> moums, Context context) {
        this.moums = moums;
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
            ((MoumAdapter.MoumEmptyViewHolder) holder).bind(moum);
        } else if (holder instanceof MoumAdapter.MoumExistViewHolder) {
            ((MoumAdapter.MoumExistViewHolder) holder).bind(moum);
        }
    }

    @Override
    public int getItemCount() {
        return moums.size();
    }

    static class MoumExistViewHolder extends RecyclerView.ViewHolder{
        private Moum moum;
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
        public void bind(Moum moum) {
            this.moum = moum;
            moumName.setText(moum.getMoumName());
            moumPlace.setText(moum.getPerformLocation());
            moumDate.setText(String.format("%s ~ %s", moum.getStartDate(), moum.getEndDate()));
            moumState.setText("진행중"); //TODO 모음 진행도가 업뎃 되면 수정 필요
            moumProgress.setProgress(0);
        }

    }

    static class MoumEmptyViewHolder extends RecyclerView.ViewHolder{
        private Moum moum;
        private Context context;

        public MoumEmptyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Moum moum){
            this.moum = moum;
        }
    }
}
