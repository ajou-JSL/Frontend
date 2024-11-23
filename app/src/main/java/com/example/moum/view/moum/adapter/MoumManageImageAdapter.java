package com.example.moum.view.moum.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.view.moum.MoumCreateActivity;
import com.example.moum.view.moum.MoumManageActivity;

import java.util.ArrayList;

public class MoumManageImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> uris;
    private Context context;

    public void setUris(ArrayList<String> uris, @NonNull Context context) {
        this.uris = uris;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_manage_image, parent, false);
        return new MoumManageImageAdapter.MoumManageImageViewHolder(view, context);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String uri = uris.get(position);
        if (holder instanceof MoumManageImageAdapter.MoumManageImageViewHolder) {
            ((MoumManageImageAdapter.MoumManageImageViewHolder) holder).bind(uri);
        }
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    static class MoumManageImageViewHolder extends RecyclerView.ViewHolder {
        private String uri;
        private ImageView moumManageImageview;
        private Context context;

        public MoumManageImageViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            moumManageImageview = itemView.findViewById(R.id.imageview_moum_manage);
            this.context = context;
        }

        public void bind(String uri) {
            this.uri = uri;
            Glide.with(context)
                    .load(uri)
                    .apply(new RequestOptions()
                    .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                    .error(R.drawable.background_more_rounded_gray_size_fit))
                    .into(moumManageImageview);
            moumManageImageview.setClipToOutline(true);
        }
    }
}

