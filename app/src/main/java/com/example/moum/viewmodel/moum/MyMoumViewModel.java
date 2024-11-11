package com.example.moum.viewmodel.moum;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyMoumViewModel extends AndroidViewModel {
    private final TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Result<List<Team>>> isLoadTeamsAsMemberSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Moum>>> isLoadMoumsOfTeamSuccess = new MutableLiveData<>();

    public MyMoumViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Team>>> getIsLoadTeamsAsMemberSuccess() {
        return isLoadTeamsAsMemberSuccess;
    }

    public MutableLiveData<Result<List<Moum>>> getIsLoadMoumsOfTeamSuccess() {
        return isLoadMoumsOfTeamSuccess;
    }

    public void setIsLoadTeamsAsMemberSuccess(Result<List<Team>> isLoadTeamsAsMemberSuccess){
        this.isLoadTeamsAsMemberSuccess.setValue(isLoadTeamsAsMemberSuccess);
    }

    public void setIsLoadMoumsOfTeamSuccess(Result<List<Moum>> isLoadMoumsOfTeamSuccess){
        this.isLoadMoumsOfTeamSuccess.setValue(isLoadMoumsOfTeamSuccess);
    }

    public void loadTeamsAsMember(Integer memberId){
        // goto repository
        teamRepository.loadTeamsAsMember(memberId, this::setIsLoadTeamsAsMemberSuccess);
    }

    public void loadMoumsOfTeam(Integer teamId){
        //valid check
        if(isLoadTeamsAsMemberSuccess.getValue() != null || isLoadTeamsAsMemberSuccess.getValue().getValidation() != Validation.GET_MY_TEAM_LIST_SUCCESS){
            Result<List<Moum>> result = new Result<>(Validation.TEAM_NOT_FOUND);
            setIsLoadMoumsOfTeamSuccess(result);
        }

        //goto repository
        moumRepository.loadMoumsOfTeam(teamId, this::setIsLoadMoumsOfTeamSuccess);
    }

}