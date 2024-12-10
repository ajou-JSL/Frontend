package com.example.moum.view.moum.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;

import java.util.ArrayList;
import java.util.Collections;

public class FilterTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> types;
    private ArrayList<Boolean> isSelecteds;
    private Context context;

    public void setTypes(ArrayList<String> types, Context context) {
        this.types = types;
        this.context = context;
    }

    public ArrayList<Boolean> getIsSelecteds() {
        Log.e(TAG, isSelecteds.isEmpty() ? "empty" : "not");
        return isSelecteds;
    }

    public void setIsSelected(int pos, Boolean isSelected) {
        if (!isSelecteds.isEmpty()) {
            isSelecteds.set(pos, isSelected);
        }
    }

    public Boolean getIsSelected(int pos) {
        if (!isSelecteds.isEmpty()) {
            return isSelecteds.get(pos);
        }
        return false;
    }

    public Integer getSelectedItem() {
        if (!isSelecteds.isEmpty()) {
            for (String type : types) {
                int idx = types.indexOf(type);
                if (isSelecteds.get(idx)) {
                    if (idx == 0) {
                        return null;
                    } else {
                        return idx; //return int value(null or 1 or 2)
                    }
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isSelecteds == null) isSelecteds = new ArrayList<>(Collections.nCopies(types.size(), false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new FilterTypeAdapter.FilterTypeViewHolder(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String orderBy = types.get(position);
        ((FilterTypeAdapter.FilterTypeViewHolder) holder).bind(orderBy);

    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    static class FilterTypeViewHolder extends RecyclerView.ViewHolder {
        private FilterTypeAdapter adapter;
        private String type;
        private TextView genreItem;
        private Context context;

        public FilterTypeViewHolder(@NonNull View itemView, Context context, FilterTypeAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            genreItem = itemView.findViewById(R.id.genre_item);
            this.context = context;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(String type) {
            int pos = getAbsoluteAdapterPosition();
            this.type = type;
            genreItem.setText(type);

            if (pos != RecyclerView.NO_POSITION) {
                Boolean isSelected = adapter.getIsSelected(pos);
                if (isSelected) {
                    genreItem.setBackground(context.getDrawable(R.drawable.background_genre_item_mint));
                    genreItem.setTextColor(context.getColor(R.color.gray0));
                } else {
                    genreItem.setBackground(context.getDrawable(R.drawable.background_genre_item));
                    genreItem.setTextColor(context.getColor(R.color.gray7));
                }
            }
            genreItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pos != RecyclerView.NO_POSITION) {
                        ArrayList<Boolean> isSeltecteds = adapter.getIsSelecteds();
                        for (int i = 0; i < isSeltecteds.size(); i++) {
                            if (pos == i) {
                                adapter.setIsSelected(pos, true);
                                adapter.notifyItemChanged(pos);
                            } else {
                                adapter.setIsSelected(i, false);
                                adapter.notifyItemChanged(i);
                            }
                        }
                    }
                }
            });
        }
    }
}
