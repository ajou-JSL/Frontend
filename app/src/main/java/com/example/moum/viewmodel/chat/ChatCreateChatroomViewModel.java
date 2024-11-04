package com.example.moum.viewmodel.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Group;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatCreateChatroomViewModel extends ViewModel {
    private ChatRepository chatRepository;
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

    public ChatCreateChatroomViewModel(){ this.chatRepository = ChatRepository.getInstance(); }

    public void loadGroups(String memberId){
        chatRepository.loadGroups(memberId, this::setIsLoadGroupSuccess);
    }

}
