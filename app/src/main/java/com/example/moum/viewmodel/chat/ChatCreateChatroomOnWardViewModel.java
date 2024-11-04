package com.example.moum.viewmodel.chat;

import android.content.Context;
import android.net.Uri;

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

public class ChatCreateChatroomOnWardViewModel extends ViewModel {
    private ChatRepository chatRepository;
    private TeamRepository teamRepository;
    private String chatroomName;
    private ArrayList<User> members;
    private ArrayList<Boolean> isParticipates;
    private final MutableLiveData<Uri> chatroomProfile = new MutableLiveData<>();
    private final MutableLiveData<Result<List<User>>> isloadMembersOfGroupSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isCreateChatroomSuccess = new MutableLiveData<>();


    public ChatCreateChatroomOnWardViewModel(){
        chatRepository = ChatRepository.getInstance();
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

    public void setInfo(String chatroomName, ArrayList<User> members, ArrayList<Boolean> isParticipates){
        this.chatroomName = chatroomName;
        this.members = members;
        this.isParticipates = isParticipates;
    }

    public void loadMembersOfGroup(Integer groupId){
        teamRepository.loadMembersOfGroup(groupId, this::setIsloadMembersOfGroupSuccess);
    }

    public void createChatroom(Context context){

        /*null check*/
        if(members == null || isParticipates == null) {
            setIsCreateChatroomSuccess(Validation.NOT_VALID_ANYWAY);
        }
        else if(chatroomName == null || chatroomName.isEmpty()) {
            setIsCreateChatroomSuccess(Validation.CHATROOM_NAME_EMPTY);
        }

        /*processing*/
        ArrayList<User> participants = new ArrayList<>();
        File chatroomProfileFile;
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
        }

        /*goto repository*/
        chatRepository.createChatroom(chatroomName, chatroomProfileFile, participants, result -> {
            //TODO
        });
    }

}
