package com.example.moum.viewmodel.chat;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatroomRepository;

import java.util.List;

public class ChatMemberListViewModel extends AndroidViewModel {
    private final ChatroomRepository chatroomRepository;
    private final MutableLiveData<Result<List<Member>>> isLoadMembersOfChatroomSuccess = new MutableLiveData<>();

    public ChatMemberListViewModel(Application application){
        super(application);
        chatroomRepository = ChatroomRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Member>>> getIsLoadMembersOfChatroomSuccess() {
        return isLoadMembersOfChatroomSuccess;
    }

    public void setIsLoadMembersOfChatroomSuccess(Result<List<Member>> isLoadMembersOfChatroomSuccess){
        this.isLoadMembersOfChatroomSuccess.setValue(isLoadMembersOfChatroomSuccess);
    }

    public void loadMembersOfChatroom(Integer chatroomId){
        chatroomRepository.loadMembersOfChatroom(chatroomId, this::setIsLoadMembersOfChatroomSuccess);
    }

}
