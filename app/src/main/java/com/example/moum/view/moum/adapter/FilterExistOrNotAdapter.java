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

public class FilterExistOrNotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> items;
    private ArrayList<Boolean> isSelecteds;
    private Boolean existOrNot;
    private Context context;

    public void setItems(ArrayList<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public ArrayList<Boolean> getIsSelecteds() {
        Log.e(TAG, isSelecteds.isEmpty()? "empty" : "not");
        return isSelecteds;
    }

    public void setIsSelected(int pos, Boolean isSelected) {
        if(!isSelecteds.isEmpty()){
            isSelecteds.set(pos, isSelected);
        }
    }

    public void setExistOrNot(Boolean existOrNot){
        this.existOrNot = existOrNot;
    }

    public Boolean getIsSelected(int pos){
        if(!isSelecteds.isEmpty()){
            return isSelecteds.get(pos);
        }
        return false;
    }

    public Boolean getExistOrNot() {
        return existOrNot;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isSelecteds == null) isSelecteds = new ArrayList<>(Collections.nCopies(items.size(), false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new FilterExistOrNotAdapter.FilterSortByViewHolder(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String item = items.get(position);
        ((FilterExistOrNotAdapter.FilterSortByViewHolder) holder).bind(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FilterSortByViewHolder extends RecyclerView.ViewHolder{
        private FilterExistOrNotAdapter adapter;
        private String item;
        private TextView genreItem;
        private Context context;

        public FilterSortByViewHolder(@NonNull View itemView, Context context, FilterExistOrNotAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            genreItem = itemView.findViewById(R.id.genre_item);
            this.context = context;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(String item){
            int pos = getAbsoluteAdapterPosition();
            this.item = item;
            if(item.equals("don't-care"))
                genreItem.setText("상관없음");
            else if(item.equals("exist"))
                genreItem.setText("있음");
            else if(item.equals("not-exist"))
                genreItem.setText("없음");

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
                        for(int i = 0; i < isSeltecteds.size(); i++){
                            if(pos == i){
                                adapter.setIsSelected(pos, true);
                                if(pos == 0)
                                    adapter.setExistOrNot(null);
                                else if(pos == 1)
                                    adapter.setExistOrNot(true);
                                else if(pos == 2)
                                    adapter.setExistOrNot(false);
                                adapter.notifyItemChanged(pos);
                            }
                            else{
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
