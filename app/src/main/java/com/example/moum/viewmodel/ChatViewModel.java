package com.example.moum.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatRepository;
import com.example.moum.utils.Validation;

import java.time.LocalDateTime;

public class ChatViewModel extends ViewModel {
    private final ChatRepository chatRepository;
    private MutableLiveData<Chat> receivedChat = new MutableLiveData<>();
    private MutableLiveData<Result<Chat>> isChatSendSuccess = new MutableLiveData<>();
    private MutableLiveData<Result<Chat>> isReceiveRecentChatSuccess = new MutableLiveData<>();
    private MutableLiveData<Result<Chat>> isReceiveOldChatSuccess = new MutableLiveData<>();
    private String sender;
    private String receiver;
    private Integer chatroomId;

    public ChatViewModel(){
        chatRepository = ChatRepository.getInstance();
    }

    public ChatViewModel(ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }

    public void setIsChatSendSuccess(Result<Chat> isChatSendSuccess) {
        this.isChatSendSuccess.setValue(isChatSendSuccess);
    }

    public void setIsReceiveRecentChatSuccess(Result<Chat> isReceiveRecentChatSuccess) {
        this.isChatSendSuccess.postValue(isReceiveRecentChatSuccess);
    }

    public void setIsReceiveOldChatSuccess(Result<Chat> isReceiveOldChatSuccess) {
        this.isReceiveOldChatSuccess.postValue(isReceiveOldChatSuccess);
    }

    public MutableLiveData<Result<Chat>> getIsChatSendSuccess() {
        return isChatSendSuccess;
    }

    public MutableLiveData<Result<Chat>> getIsReceiveRecentChatSuccess() {
        return isReceiveRecentChatSuccess;
    }

    public MutableLiveData<Result<Chat>> getIsReceiveOldChatSuccess() {
        return isReceiveOldChatSuccess;
    }

    public void setChatroomInfo(String sender, String receiver, Integer chatroomId){
        this.sender = sender;
        this.receiver = receiver;
        this.chatroomId = chatroomId;
    }

    public MutableLiveData<Chat> getReceivedChat() {
        return receivedChat;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void chatSend(String message){
        Chat chat = new Chat(sender, receiver, message, chatroomId, LocalDateTime.now());
        chatRepository.chatSend(chat, result -> {
            if(result.getData() != null){
                result.getData().setSentByMe(true);
            }
            setIsChatSendSuccess(result);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void chatSendTest(String message){
        Chat chat = new Chat(sender, receiver, message, chatroomId, LocalDateTime.now());
        chat.setSentByMe(true);
        Result<Chat> result = new Result<>(Validation.CHAT_POST_SUCCESS, chat);
        setIsChatSendSuccess(result);
    }

    public void receiveRecentChat(){
        chatRepository.receiveRecentChat(chatroomId, result -> {
            if(result.getData() != null){
                Chat chat = result.getData();
                if(chat.getSender().equals(sender))
                    chat.setSentByMe(true);
                else
                    chat.setSentByMe(false);
            }
            setIsReceiveRecentChatSuccess(result);
        });
    }

    public void receiveOldChat(LocalDateTime beforeTimestamp){
        chatRepository.receiveOldChat(chatroomId, beforeTimestamp, result -> {
            if(result.getData() != null){
                Chat chat = result.getData();
                if(chat.getSender().equals(sender))
                    chat.setSentByMe(true);
                else
                    chat.setSentByMe(false);
            }
            setIsReceiveOldChatSuccess(result);
        });
    }
}
