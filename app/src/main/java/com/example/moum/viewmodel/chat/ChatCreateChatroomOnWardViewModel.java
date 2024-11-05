package com.example.moum.viewmodel.chat;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.User;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatCreateChatroomOnWardViewModel extends AndroidViewModel {
    private ChatRepository chatRepository;
    private TeamRepository teamRepository;
    private Integer groupId;
    private String chatroomName;
    private ArrayList<User> members;
    private ArrayList<Boolean> isParticipates;
    private final MutableLiveData<Uri> chatroomProfile = new MutableLiveData<>();
    private final MutableLiveData<Result<List<User>>> isloadMembersOfGroupSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isCreateChatroomSuccess = new MutableLiveData<>();


    public ChatCreateChatroomOnWardViewModel(Application application){
        super(application);
        chatRepository = ChatRepository.getInstance(application);
        teamRepository = TeamRepository.getInstance();
    }

    public List<User> getMembers() {
        return members;
    }

    public MutableLiveData<Uri> getChatroomProfile() {
        return chatroomProfile;
    }

    public MutableLiveData<Result<List<User>>> getIsloadMembersOfGroupSuccess() {
        return isloadMembersOfGroupSuccess;
    }

    public MutableLiveData<Validation> getIsCreateChatroomSuccess() {
        return isCreateChatroomSuccess;
    }

    public void setChatroomProfile(Uri uri){
        chatroomProfile.setValue(uri);
    }

    private void setIsloadMembersOfGroupSuccess(Result<List<User>> isloadMembersOfGroupSuccess){
        this.isloadMembersOfGroupSuccess.setValue(isloadMembersOfGroupSuccess);
    }

    private void setIsCreateChatroomSuccess(Validation validation){
        this.isCreateChatroomSuccess.setValue(validation);
    }

    public void setInfo(Integer groupId, String chatroomName, ArrayList<User> members, ArrayList<Boolean> isParticipates){
        this.groupId = groupId;
        this.chatroomName = chatroomName;
        this.members = members;
        this.isParticipates = isParticipates;
    }

    public void loadMembersOfGroup(Integer groupId){

        /*valid check*/
        if(groupId == -1){
            Result<List<User>> result = new Result<>(Validation.CHATROOM_GROUP_NOT_FOUND);
            setIsloadMembersOfGroupSuccess(result);
            return;
        }

        /*goto repository*/
        teamRepository.loadMembersOfGroup(groupId, this::setIsloadMembersOfGroupSuccess);
    }

    public void createChatroom(Context context){

        /*valid check*/
        Result<List<User>> isLoadMembersOfGroupSuccessVal = isloadMembersOfGroupSuccess.getValue();
        if(isLoadMembersOfGroupSuccessVal == null || isLoadMembersOfGroupSuccessVal.getValidation() != Validation.VALID_ALL){
            setIsCreateChatroomSuccess(Validation.CHATROOM_NOT_LOADED);
            return;
        }
        else if(members == null || isParticipates == null) {
            setIsCreateChatroomSuccess(Validation.CHATROOM_NOT_LOADED);
            return;
        }
        else if(chatroomName == null || chatroomName.isEmpty()) {
            setIsCreateChatroomSuccess(Validation.CHATROOM_NAME_EMPTY);
            return;
        }

        /*processing*/
        ArrayList<User> participants = new ArrayList<>();
        File chatroomProfileFile = null;
        for (int i = 0; i < members.size(); i++) {
            if (isParticipates.get(i))
                participants.add(members.get(i));
        }
        if(chatroomProfile.getValue() != null){
            Uri uri = chatroomProfile.getValue();
            ImageManager imageManager = new ImageManager(context);
            chatroomProfileFile = imageManager.convertUriToFile(uri);
        }
        if(participants.isEmpty()){
            setIsCreateChatroomSuccess(Validation.PARTICIPATE_AT_LEAST_TWO);
            return;
        }

        /*goto repository*/
        chatRepository.createChatroom(groupId, chatroomName, chatroomProfileFile, participants, result -> {
            setIsCreateChatroomSuccess(result.getValidation());
        });
    }

}
