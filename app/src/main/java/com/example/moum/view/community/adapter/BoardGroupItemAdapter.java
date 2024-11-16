package com.example.moum.view.community.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.BoardGroupItem;
import com.example.moum.view.profile.TeamProfileFragment;

import java.util.ArrayList;

public class BoardGroupItemAdapter extends RecyclerView.Adapter<BoardGroupItemAdapter.CustomViewHolder> {
    private ArrayList<BoardGroupItem> itemList;
    private AdapterView.OnItemClickListener onItemClickListener;

    public BoardGroupItemAdapter(ArrayList<BoardGroupItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_board_group;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bind(itemList.get(position));

        holder.itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("BoardGroupItemAdapter", "Item clicked at position: " + position);

                // Bundle에 데이터 추가
                Bundle bundle = new Bundle();
                bundle.putInt("targetTeamId", itemList.get(position).getTeamId());

                // TeamProfileFragment 생성 및 데이터 설정
                TeamProfileFragment fragment = new TeamProfileFragment(v.getContext());
                fragment.setArguments(bundle);

                fragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), fragment.getTag());
            }
        };

    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void updateItemList(ArrayList<BoardGroupItem> boarditemList) {
        this.itemList = boarditemList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener{
        void onItemClick(View v, int position);
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView content, writer;
        private ImageView image;
        ItemClickListener itemClickListener;

        public CustomViewHolder(View itemView) {
            super(itemView);
            writer = itemView.findViewById(R.id.item_board_group_writer);
            content = itemView.findViewById(R.id.item_board_group_content);
            image = itemView.findViewById(R.id.item_board_group_image_view);

            itemView.setOnClickListener(this);
        }

        public void bind(BoardGroupItem item) {
            content.setText(item.getContent());
            writer.setText(item.getWriter());

            if (item.hasImage() && image != null) {
                Glide.with(itemView.getContext())
                        .load(item.getImage())
                        .into(image);
                image.setCropToPadding(true);
            } else {
                image.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getLayoutPosition());
        }

    }
}
