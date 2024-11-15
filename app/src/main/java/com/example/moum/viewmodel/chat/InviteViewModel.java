package com.example.moum.viewmodel.chat;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Validation;

import java.util.List;

public class InviteViewModel extends AndroidViewModel {
    private TeamRepository teamRepository;
    private final MutableLiveData<Result<List<Team>>> isLoadTeamsAsMemberSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Member>> isInviteMemberToTeamSuccess = new MutableLiveData<>();
    private Team selectedTeam;

    public InviteViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Team>>> getIsLoadTeamsAsMemberSuccess() {
        return isLoadTeamsAsMemberSuccess;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Member>> getIsInviteMemberToTeamSuccess() {
        return isInviteMemberToTeamSuccess;
    }

    public void setIsLoadTeamsAsMemberSuccess(Result<List<Team>> isLoadTeamsAsMemberSuccess){
        this.isLoadTeamsAsMemberSuccess.setValue(isLoadTeamsAsMemberSuccess);
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess){
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsInviteMemberToTeamSuccess(Result<Member> isInviteMemberToTeamSuccess){
        this.isInviteMemberToTeamSuccess.setValue(isInviteMemberToTeamSuccess);
    }

    public void loadTeamsAsMember(Integer memberId){
        teamRepository.loadTeamsAsMember(memberId, this::setIsLoadTeamsAsMemberSuccess);
    }

    public void validCheck(Integer memberId, Team selectedTeam){
        /*null check*/
        if(memberId == -1){
            setIsValidCheckSuccess(Validation.MEMBER_NOT_EXIST);
            return;
        }
        else if(selectedTeam == null || selectedTeam.getTeamId() == null){
            setIsValidCheckSuccess(Validation.TEAM_NOT_FOUND);
            return;
        }

        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void inviteMemberToTeam(Integer memberId, Integer teamId){
        /*goto repository*/
        teamRepository.inviteMemberToTeam(teamId, memberId, this::setIsInviteMemberToTeamSuccess);
    }
}
