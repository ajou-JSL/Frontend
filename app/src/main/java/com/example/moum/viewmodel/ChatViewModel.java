package com.example.moum.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chat;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<Chat> receivedChat = new MutableLiveData<>();

    public MutableLiveData<Chat> getReceivedChat() {
        return receivedChat;
    }

    public void send(String message){

    }
}
