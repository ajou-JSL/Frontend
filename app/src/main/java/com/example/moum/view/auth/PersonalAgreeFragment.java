package com.example.moum.view.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.FragmentMemberProfileBinding;
import com.example.moum.databinding.FragmentPersonalAgreeBinding;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.YoutubeManager;
import com.example.moum.view.chat.ChatActivity;
import com.example.moum.view.profile.TeamProfileFragment;
import com.example.moum.view.profile.adapter.ProfileGenreAdapter;
import com.example.moum.view.profile.adapter.ProfileRecordAdapter;
import com.example.moum.view.profile.adapter.ProfileTeamAdapter;
import com.example.moum.view.report.ReportMemberFragment;
import com.example.moum.viewmodel.profile.MemberProfileViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class PersonalAgreeFragment extends BottomSheetDialogFragment {
    private FragmentPersonalAgreeBinding binding;
    private Context context;

    public PersonalAgreeFragment(Context context){
        this.context = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonalAgreeBinding.inflate(inflater,container, false);
        context = requireContext();
        View view = binding.getRoot();
        view.setBackground(context.getDrawable(R.drawable.background_top_rounded_white));

        /*위가 둥근 형태로 만들기*/
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);

        return view;
    }
}
