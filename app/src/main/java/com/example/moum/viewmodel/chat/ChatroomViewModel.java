package com.example.moum.viewmodel.chat;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatRepository;

import java.util.List;

public class ChatroomViewModel extends AndroidViewModel {
    private final ChatRepository chatRepository;
    private MutableLiveData<Result<List<Chatroom>>> isLoadChatroomsSuccess = new MutableLiveData<>();
    private String memberId;

    public ChatroomViewModel(Application application){
        super(application);
        chatRepository = ChatRepository.getInstance(application);}

    public void setIsLoadChatroomsSuccess(Result<List<Chatroom>> isLoadChatroomsSuccess) {
        this.isLoadChatroomsSuccess.setValue(isLoadChatroomsSuccess);
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public MutableLiveData<Result<List<Chatroom>>> getIsLoadChatroomsSuccess() {
        return isLoadChatroomsSuccess;
    }

    public void loadChatrooms(){
        chatRepository.loadChatrooms(memberId, result -> {
            setIsLoadChatroomsSuccess(result);
        });
    }
}
