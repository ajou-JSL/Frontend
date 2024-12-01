package com.example.moum.viewmodel.chat;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatCreateChatroomOnWardViewModel extends AndroidViewModel {
    private ChatroomRepository chatroomRepository;
    private TeamRepository teamRepository;
    private Chatroom chatroom;
    private ArrayList<Member> members;
    private ArrayList<Boolean> isParticipates;
    private final MutableLiveData<Uri> chatroomProfile = new MutableLiveData<>();
    private final MutableLiveData<Result<Team>> isLoadTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isCreateChatroomSuccess = new MutableLiveData<>();


    public ChatCreateChatroomOnWardViewModel(Application application){
        super(application);
        chatroomRepository = ChatroomRepository.getInstance(application);
        teamRepository = TeamRepository.getInstance(application);
    }

    public List<Member> getMembers() {
        return members;
    }

    public MutableLiveData<Uri> getChatroomProfile() {
        return chatroomProfile;
    }

    public MutableLiveData<Result<Team>> getIsLoadTeamSuccess() {
        return isLoadTeamSuccess;
    }

    public MutableLiveData<Validation> getIsCreateChatroomSuccess() {
        return isCreateChatroomSuccess;
    }

    public void setChatroomProfile(Uri uri){
        chatroomProfile.setValue(uri);
    }

    private void setIsLoadTeamSuccess(Result<Team> isLoadTeamSuccess){
        this.isLoadTeamSuccess.setValue(isLoadTeamSuccess);
    }

    private void setIsCreateChatroomSuccess(Validation validation){
        this.isCreateChatroomSuccess.setValue(validation);
    }

    public void setInfo(Integer userId, String username, Integer teamId, String chatroomName, ArrayList<Member> members, ArrayList<Boolean> isParticipates){
        this.chatroom = new Chatroom(chatroomName, Chatroom.ChatroomType.MULTI_CHAT, teamId, userId);
        this.members = members;
        this.isParticipates = isParticipates;
    }

    public void loadTeam(Integer teamId){
        /*valid check*/
        if(teamId == -1){
            Result<Team> result = new Result<>(Validation.TEAM_NOT_FOUND);
            setIsLoadTeamSuccess(result);
            return;
        }

        /*goto repository*/
        teamRepository.loadTeam(teamId, this::setIsLoadTeamSuccess);
    }

    public void createChatroom(Context context){
        /*valid check*/
        Result<Team> isLoadMembersOfGroupSuccessVal = isLoadTeamSuccess.getValue();
        if(isLoadMembersOfGroupSuccessVal == null || isLoadMembersOfGroupSuccessVal.getValidation() != Validation.GET_TEAM_SUCCESS){
            setIsCreateChatroomSuccess(Validation.CHATROOM_NOT_LOADED);
            return;
        }
        else if(members == null || isParticipates == null) {
            setIsCreateChatroomSuccess(Validation.CHATROOM_NOT_LOADED);
            return;
        }
        else if(chatroom.getName() == null || chatroom.getName().isEmpty()) {
            setIsCreateChatroomSuccess(Validation.CHATROOM_NAME_EMPTY);
            return;
        }

        /*processing*/
        ArrayList<Integer> participants = new ArrayList<>();
        File chatroomProfileFile = null;
        for (int i = 0; i < members.size(); i++) {
            if (isParticipates.get(i))
                participants.add(members.get(i).getId());
        }
        if(chatroomProfile.getValue() != null){
            Uri uri = chatroomProfile.getValue();
            ImageManager imageManager = new ImageManager(context);
            chatroomProfileFile = imageManager.convertUriToFile(uri);
        }
        if(participants.isEmpty() || participants.size() < 2){
            setIsCreateChatroomSuccess(Validation.PARTICIPATE_AT_LEAST_TWO);
            return;
        }

        /*goto repository*/
        chatroomRepository.createChatroom(chatroom, chatroomProfileFile, participants, result -> {
            setIsCreateChatroomSuccess(result.getValidation());
        });
    }

}
