package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Like;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class PerformanceCreateViewModel extends AndroidViewModel {
    private TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Result<List<Team>>> isLoadTeamsAsLeaderSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Moum>>> isLoadMoumsOfTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Team> teamSelected = new MutableLiveData<>();
    private final MutableLiveData<Moum> moumSelected = new MutableLiveData<>();

    public PerformanceCreateViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Team>>> getIsLoadTeamsAsLeaderSuccess() {
        return isLoadTeamsAsLeaderSuccess;
    }

    public MutableLiveData<Result<List<Moum>>> getIsLoadMoumsOfTeamSuccess() {
        return isLoadMoumsOfTeamSuccess;
    }

    public MutableLiveData<Team> getTeamSelected() {
        return teamSelected;
    }

    public Team getTeamSelectedValue(){
        return teamSelected.getValue();
    }

    public Moum getMoumSelectedValue(){
        return  moumSelected.getValue();
    }

    public void setIsLoadTeamsAsLeaderSuccess(Result<List<Team>> isLoadTeamsAsLeaderSuccess){
        this.isLoadTeamsAsLeaderSuccess.setValue(isLoadTeamsAsLeaderSuccess);
    }

    public void setIsLoadMoumsOfTeamSuccess(Result<List<Moum>> isLoadMoumsOfTeamSuccess){
        this.isLoadMoumsOfTeamSuccess.setValue(isLoadMoumsOfTeamSuccess);
    }

    public void setTeamSelected(Team teamSelected){
        this.teamSelected.setValue(teamSelected);
    }

    public void setMoumSelected(Moum moumSelected){
        this.moumSelected.setValue(moumSelected);
    }

    public void loadTeamsAsLeader(Integer memberId){
        teamRepository.loadTeamsAsMember(memberId, result -> {
            Validation validation = result.getValidation();
            List<Team> loadedTeams = result.getData();
            List<Team> teamsAsLeader = new ArrayList<>();
            if(validation == Validation.GET_TEAM_LIST_SUCCESS && !loadedTeams.isEmpty()) {
                for (Team team : loadedTeams)
                    if (team.getLeaderId().equals(memberId))
                        teamsAsLeader.add(team);
                Result<List<Team>> newResult = new Result<>(validation, teamsAsLeader);
                setIsLoadTeamsAsLeaderSuccess(newResult);
            }
            else{
                setIsLoadTeamsAsLeaderSuccess(result);
            }
        });
    }

    public void loadMoumsOfTeam(Integer teamId){
        //valid check
        if(isLoadTeamsAsLeaderSuccess.getValue() == null || isLoadTeamsAsLeaderSuccess.getValue().getValidation() != Validation.GET_TEAM_LIST_SUCCESS){
            Result<List<Moum>> result = new Result<>(Validation.TEAM_NOT_FOUND);
            setIsLoadMoumsOfTeamSuccess(result);
            return;
        }

        //goto repository
        moumRepository.loadMoumsOfTeam(teamId, this::setIsLoadMoumsOfTeamSuccess);
    }
}
