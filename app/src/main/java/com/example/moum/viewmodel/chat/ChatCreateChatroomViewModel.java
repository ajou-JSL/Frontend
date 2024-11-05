package com.example.moum.viewmodel.chat;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Group;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatCreateChatroomViewModel extends AndroidViewModel {
    private ChatRepository chatRepository;
    private TeamRepository teamRepository;
    private MutableLiveData<Result<List<Group>>> isLoadGroupSuccess = new MutableLiveData<>();
    private Group selectedGroup;

    public MutableLiveData<Result<List<Group>>> getIsLoadGroupSuccess() {
        return isLoadGroupSuccess;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setIsLoadGroupSuccess(Result<List<Group>> isLoadGroupSuccess) {
        this.isLoadGroupSuccess.setValue(isLoadGroupSuccess);
    }

    public void setSelectedGroup(Group group) {
        this.selectedGroup = group;
    }

    public ChatCreateChatroomViewModel(Application application){
        super(application);
        this.chatRepository = ChatRepository.getInstance(application);
        this.teamRepository = TeamRepository.getInstance();
    }

    public void loadGroups(String memberId){
        teamRepository.loadGroups(memberId, this::setIsLoadGroupSuccess);
    }

}
