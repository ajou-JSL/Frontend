package com.example.moum.view.moum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;
import com.example.moum.view.chat.adapter.ChatAdapter;
import com.example.moum.view.chat.adapter.ChatroomAdapter;
import com.example.moum.view.profile.adapter.ProfileTeamAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Team> teams;
    private ArrayList<ArrayList<Moum>> moumsOfTeams;
    private Context context;
    private static final int VIEW_TYPE_EXIST = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private final String TAG = getClass().toString();

    public void setTeamsNMoums(ArrayList<Team> teams, ArrayList<ArrayList<Moum>> moumsOfTeams, Context context) {
        this.teams = teams;
        this.moumsOfTeams = moumsOfTeams;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position == teams.size()-1? VIEW_TYPE_EMPTY : VIEW_TYPE_EXIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_empty, parent, false);
            return new TeamAdapter.TeamEmptyViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
            return new TeamAdapter.TeamExistViewHolder(view, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "position: " + position + " team: " + teams.toString());
        Team team = teams.get(position);
        ArrayList<Moum> moums = moumsOfTeams.get(position);
        if (holder instanceof TeamAdapter.TeamEmptyViewHolder) {
            ((TeamAdapter.TeamEmptyViewHolder) holder).bind(team, moums);
        } else if (holder instanceof TeamAdapter.TeamExistViewHolder) {
            ((TeamAdapter.TeamExistViewHolder) holder).bind(team, moums);
        }
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class TeamExistViewHolder extends RecyclerView.ViewHolder{
        private Team team;
        private ArrayList<Moum> moums;
        private ImageView teamProfile;
        private ImageView teamMembersProfile;
        private TextView teamMembers;
        private TextView teamUpdate;
        private TextView teamName;
        private TextView teamDescription;
        private RecyclerView moumRecycler;
        private Context context;

        public TeamExistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            teamProfile = itemView.findViewById(R.id.imageview_team_profile);
            teamMembersProfile = itemView.findViewById(R.id.imageview_members);
            teamMembers = itemView.findViewById(R.id.textview_team_members);
            teamUpdate = itemView.findViewById(R.id.textview_update);
            teamName = itemView.findViewById(R.id.textview_team_name);
            teamDescription = itemView.findViewById(R.id.textview_team_description);
            moumRecycler = itemView.findViewById(R.id.recycler_moum);
            this.context = context;
        }

        @SuppressLint("DefaultLocale")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Team team, ArrayList<Moum> moums) {
            this.team = team;
            this.moums = moums;
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.background_gray)
                    .error(R.drawable.background_gray))
                    .load(team.getFileUrl()).into(teamProfile);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.background_circle_gray)
                    .error(R.drawable.background_circle_gray))
                    .load(team.getMembers().get(0).getProfileImageUrl()).into(teamMembersProfile);
            teamMembers.setText(String.format("%s 외 %d명", team.getMembers().get(0).getName(), team.getMembers().size()));
            teamName.setText(team.getTeamName());
            teamDescription.setText(team.getDescription());

            /*모음 리사이클러뷰 연결*/
            MoumAdapter moumAdapter = new MoumAdapter();
            moumAdapter.setMoums(moums, context);
            moumRecycler.setLayoutManager(new LinearLayoutManager(context));
            moumRecycler.setAdapter(moumAdapter);
        }

    }

    static class TeamEmptyViewHolder extends RecyclerView.ViewHolder{
        private Team team;
        private ArrayList<Moum> moums;
        private Context context;

        public TeamEmptyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Team team, ArrayList<Moum> moums){
            this.team = team;
            this.moums = moums;
        }
    }
}
