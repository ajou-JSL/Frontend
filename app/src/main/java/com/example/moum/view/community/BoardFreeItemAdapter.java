package com.example.moum.view.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moum.data.entity.BoardFreeItem;
import com.example.moum.R;
import java.util.ArrayList;

public class BoardFreeItemAdapter extends RecyclerView.Adapter<BoardFreeItemAdapter.CustomViewHolder> {
    private ArrayList<BoardFreeItem> itemList;

    public BoardFreeItemAdapter(ArrayList<BoardFreeItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).hasImage() ? R.layout.item_board_free_image : R.layout.item_board_free_image_no;
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
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView title, content, writer, time;
        private ImageView image;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_board_free_title);
            content = itemView.findViewById(R.id.item_board_free_content);
            writer = itemView.findViewById(R.id.item_board_free_writer);
            time = itemView.findViewById(R.id.item_board_free_time);
            image = itemView.findViewById(R.id.item_board_free_image_view);
        }

        public void bind(BoardFreeItem item) {
            title.setText(item.getTitle());
            content.setText(item.getContent());
            writer.setText(item.getWriter());
            time.setText(item.getTime());

            if (item.hasImage() && image != null) {
                image.setImageResource(item.getImage());
            }
        }
    }
}
