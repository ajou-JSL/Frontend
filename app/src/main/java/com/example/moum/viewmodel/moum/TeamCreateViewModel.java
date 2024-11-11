package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;

import java.util.List;

public class TeamCreateViewModel extends AndroidViewModel {
    private final TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Team> team = new MutableLiveData<>(new Team());

    public MutableLiveData<Team> getTeam() {
        return team;
    }

    public void setTeam(Team team) { this.team.setValue(team);}

    public TeamCreateViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }
}
