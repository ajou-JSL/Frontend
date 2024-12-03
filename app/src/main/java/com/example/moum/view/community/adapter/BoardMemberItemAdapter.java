package com.example.moum.view.community.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.dto.MemberProfileRankResponse;
import com.example.moum.data.entity.BoardGroupItem;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Performance;
import com.example.moum.utils.TimeManager;
import com.example.moum.view.community.BoardMemberFragment;
import com.example.moum.view.community.BoardPerformanceFragment;
import com.example.moum.view.profile.TeamProfileFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardMemberItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MemberProfileRankResponse> members;
    private Context context;
    private BoardMemberFragment fragment;

    public void setMembers(ArrayList<MemberProfileRankResponse> members, Context context, BoardMemberFragment fragment) {
        this.members = members;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_member, parent, false);
        return new BoardMemberItemAdapter.BoardMemberItemViewModel(view, context, fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MemberProfileRankResponse member = members.get(position);
        ((BoardMemberItemAdapter.BoardMemberItemViewModel) holder).bind(member);

    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class BoardMemberItemViewModel extends RecyclerView.ViewHolder{
        private MemberProfileRankResponse member;
        private TextView memberName;
        private TextView memberDescription;
        private CircleImageView memberImage;
        private Context context;
        private BoardMemberFragment fragment;

        public BoardMemberItemViewModel(@NonNull View itemView, Context context, BoardMemberFragment fragment) {
            super(itemView);
            memberName = itemView.findViewById(R.id.item_board_member_name);
            memberDescription = itemView.findViewById(R.id.item_board_member_content);
            memberImage = itemView.findViewById(R.id.item_board_member_image_view);
            this.fragment = fragment;
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(MemberProfileRankResponse member){
            this.member = member;
            if(member.getMemberName() != null) memberName.setText(member.getMemberName());
            if(member.getMemberUsername() != null) memberDescription.setText(member.getMemberUsername());
            if(member.getFileUrl() != null)
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.background_circle_gray_size_fit)
                        .error(R.drawable.background_circle_gray_size_fit))
                        .load(member.getFileUrl()).into(memberImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.onMemberClicked(member.getMemberId());
                }
            });
        }
    }
}
