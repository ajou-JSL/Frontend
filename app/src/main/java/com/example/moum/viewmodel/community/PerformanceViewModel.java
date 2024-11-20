package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.R;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.PerformRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Validation;

public class PerformanceViewModel extends AndroidViewModel {
    private PerformRepository performRepository;
    private TeamRepository teamRepository;
    private final MutableLiveData<Result<Performance>> isLoadPerformanceSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Team>> isLoadTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Performance>> isDeletePerformanceSuccess = new MutableLiveData<>();

    public PerformanceViewModel(Application application){
        super(application);
        performRepository = PerformRepository.getInstance(application);
        teamRepository = TeamRepository.getInstance(application);
    }

    public MutableLiveData<Result<Performance>> getIsLoadPerformanceSuccess() {
        return isLoadPerformanceSuccess;
    }

    public MutableLiveData<Result<Team>> getIsLoadTeamSuccess() {
        return isLoadTeamSuccess;
    }

    public MutableLiveData<Result<Performance>> getIsDeletePerformanceSuccess() {
        return isDeletePerformanceSuccess;
    }

    public void setIsLoadPerformanceSuccess(Result<Performance> isLoadPerformanceSuccess){
        this.isLoadPerformanceSuccess.setValue(isLoadPerformanceSuccess);
    }

    public void setIsLoadTeamSuccess(Result<Team> isLoadTeamSuccess){
        this.isLoadTeamSuccess.setValue(isLoadTeamSuccess);
    }

    public void setIsDeletePerformanceSuccess(Result<Performance> isDeletePerformanceSuccess){
        this.isDeletePerformanceSuccess.setValue(isDeletePerformanceSuccess);
    }

    public void loadPerformance(Integer performId){
        performRepository.loadPerform(performId, this::setIsLoadPerformanceSuccess);
    }

    public void loadTeam(Integer teamId){
        //condition check
        if(isLoadPerformanceSuccess.getValue() == null || isLoadPerformanceSuccess.getValue().getValidation() != Validation.PERFORMANCE_GET_SUCCESS){
            Result<Team> result = new Result<>(Validation.TEAM_NOT_FOUND);
            return;
        }

        teamRepository.loadTeam(teamId, this::setIsLoadTeamSuccess);
    }

    public void deletePerformance(Integer performId){
        performRepository.deletePerform(performId, this::setIsDeletePerformanceSuccess);
    }
}
