package com.example.moum.viewmodel.chat;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Team;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.TeamRepository;

import java.util.List;

public class ChatCreateChatroomViewModel extends AndroidViewModel {
    private TeamRepository teamRepository;
    private MutableLiveData<Result<List<Team>>> isLoadTeamsAsMemberSuccess = new MutableLiveData<>();
    private Team selectedTeam;

    public MutableLiveData<Result<List<Team>>> getIsLoadTeamsAsMemberSuccess() {
        return isLoadTeamsAsMemberSuccess;
    }

    public Team getSelectedGroup() {
        return selectedTeam;
    }

    public void setIsLoadTeamsAsMemberSuccess(Result<List<Team>> isLoadTeamsAsLeaderSuccess) {
        this.isLoadTeamsAsMemberSuccess.setValue(isLoadTeamsAsLeaderSuccess);
    }

    public void setSelectedGroup(Team team) {
        this.selectedTeam = team;
    }

    public ChatCreateChatroomViewModel(Application application){
        super(application);
        this.teamRepository = TeamRepository.getInstance(application);
    }

    public void loadTeamsAsMember(Integer id){
        teamRepository.loadTeamsAsMember(id, this::setIsLoadTeamsAsMemberSuccess);
    }

}
