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

public class FilterOrderByAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> orderBys;
    private ArrayList<Boolean> isSelecteds;
    private Context context;

    public void setOrderBys(ArrayList<String> orderBys, Context context) {
        this.orderBys = orderBys;
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

    public String getSelectedItem() {
        if (!isSelecteds.isEmpty()) {
            for (String orderBy : orderBys) {
                int idx = orderBys.indexOf(orderBy);
                if (isSelecteds.get(idx)) {
                    return orderBy;
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isSelecteds == null) isSelecteds = new ArrayList<>(Collections.nCopies(orderBys.size(), false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new FilterOrderByAdapter.FilterSortByViewHolder(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String orderBy = orderBys.get(position);
        ((FilterOrderByAdapter.FilterSortByViewHolder) holder).bind(orderBy);

    }

    @Override
    public int getItemCount() {
        return orderBys.size();
    }

    static class FilterSortByViewHolder extends RecyclerView.ViewHolder {
        private FilterOrderByAdapter adapter;
        private String orderBy;
        private TextView genreItem;
        private Context context;

        public FilterSortByViewHolder(@NonNull View itemView, Context context, FilterOrderByAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            genreItem = itemView.findViewById(R.id.genre_item);
            this.context = context;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(String orderBy) {
            int pos = getAbsoluteAdapterPosition();
            this.orderBy = orderBy;
            if (orderBy.equals("asc")) {
                genreItem.setText("오름차순");
            } else if (orderBy.equals("desc")) {
                genreItem.setText("내림차순");
            }

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
