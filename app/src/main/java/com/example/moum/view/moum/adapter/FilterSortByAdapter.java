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
import com.example.moum.data.entity.Genre;
import com.example.moum.view.profile.adapter.ProfileGenreAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FilterSortByAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> sortBys;
    private ArrayList<Boolean> isSelecteds;
    private Context context;

    public void setSortBys(ArrayList<String> sortBys, Context context) {
        this.sortBys = sortBys;
        this.context = context;
    }

    public ArrayList<Boolean> getIsSelecteds() {
        Log.e(TAG, isSelecteds.isEmpty()? "empty" : "not");
        return isSelecteds;
    }

    public void setIsSelected(int pos, Boolean isSelected) {
        if(!isSelecteds.isEmpty()){
            isSelecteds.set(pos, isSelected);}
    }

    public Boolean getIsSelected(int pos){
        if(!isSelecteds.isEmpty()){
            return isSelecteds.get(pos);
        }
        return false;
    }

    public String getSelectedItem(){
        if(!isSelecteds.isEmpty()){
            for(String sortBy : sortBys){
                int idx = sortBys.indexOf(sortBy);
                if(isSelecteds.get(idx)){
                    return sortBy;
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isSelecteds == null) isSelecteds = new ArrayList<>(Collections.nCopies(sortBys.size(), false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new FilterSortByAdapter.FilterSortByViewHolder(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String sortBy = sortBys.get(position);
        ((FilterSortByAdapter.FilterSortByViewHolder) holder).bind(sortBy);

    }

    @Override
    public int getItemCount() {
        return sortBys.size();
    }

    static class FilterSortByViewHolder extends RecyclerView.ViewHolder{
        private FilterSortByAdapter adapter;
        private String sortBy;
        private TextView genreItem;
        private Context context;

        public FilterSortByViewHolder(@NonNull View itemView, Context context, FilterSortByAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            genreItem = itemView.findViewById(R.id.genre_item);
            this.context = context;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(String sortBy){
            int pos = getAbsoluteAdapterPosition();
            this.sortBy = sortBy;
            if(sortBy.equals("distance"))
                genreItem.setText("거리순");
            else if(sortBy.equals("price"))
                genreItem.setText("가격순");
            else if(sortBy.equals("size"))
                genreItem.setText("크기순");
            else if(sortBy.equals("capacity"))
                genreItem.setText("수용인원순");
            else if(sortBy.equals("stand"))
                genreItem.setText("보면대 개수순");

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
