package com.example.moum.viewmodel.chat;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.utils.Validation;

import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ChatViewModel extends AndroidViewModel {
    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final MutableLiveData<Result<Chat>> isChatSendSuccess = new MutableLiveData<>();
    private final PublishSubject<Result<Chat>> isReceiveRecentChatSuccess = PublishSubject.create();
    private final PublishSubject<Result<Chat>> isReceiveOldChatSuccess = PublishSubject.create();
    private final MutableLiveData<Result<List<Member>>> isLoadMembersOfChatroomSuccess = new MutableLiveData<>();
    private String senderUsername;
    private Integer senderId;
    private Chatroom chatroom;
    private String TAG = getClass().toString();

    public ChatViewModel(Application application){
        super(application);
        chatRepository = ChatRepository.getInstance(application);
        chatroomRepository = ChatroomRepository.getInstance(application);
    }

    public ChatViewModel(Application application, ChatRepository chatRepository, ChatroomRepository chatroomRepository){
        super(application);
        this.chatRepository = chatRepository;
        this.chatroomRepository = chatroomRepository;
    }

    public void setIsChatSendSuccess(Result<Chat> isChatSendSuccess) {
        this.isChatSendSuccess.setValue(isChatSendSuccess);
    }

    public void setIsReceiveRecentChatSuccess(Result<Chat> isReceiveRecentChatSuccess) {
        this.isReceiveRecentChatSuccess.onNext(isReceiveRecentChatSuccess);
    }

    public void setIsReceiveOldChatSuccess(Result<Chat> isReceiveOldChatSuccess) {
        this.isReceiveOldChatSuccess.onNext(isReceiveOldChatSuccess);
    }

    public void setIsLoadMembersOfChatroomSuccess(Result<List<Member>> isLoadMembersOfChatroomSuccess){
        this.isLoadMembersOfChatroomSuccess.setValue(isLoadMembersOfChatroomSuccess);
    }
    public MutableLiveData<Result<Chat>> getIsChatSendSuccess() {
        return isChatSendSuccess;
    }

    public Observable<Result<Chat>> getIsReceiveRecentChatSuccess() {
        return isReceiveRecentChatSuccess;
    }

    public Observable<Result<Chat>> getIsReceiveOldChatSuccess() {
        return isReceiveOldChatSuccess;
    }

    public MutableLiveData<Result<List<Member>>> getIsLoadMembersOfChatroomSuccess() {
        return isLoadMembersOfChatroomSuccess;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroomInfo(String senderUsername, Integer senderId, Chatroom chatroom) {
        this.senderUsername = senderUsername;
        this.senderId = senderId;
        this.chatroom = chatroom;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void chatSend(String message){
        Chat chat = new Chat(senderUsername, message, chatroom.getId(), LocalDateTime.now());
        chatRepository.chatSend(chat, result -> {
            Log.e(TAG, "chatSend return");
            if(result.getData() != null){
                result.getData().setSentByMe(true);
            }
            setIsChatSendSuccess(result);
        });
    }

    public void receiveRecentChat(){
        chatRepository.receiveRecentChat(chatroom.getId(), result -> {
            if(result.getData() != null){
                Chat chat = result.getData();
                if(chat.getSender().equals(senderUsername))
                    chat.setSentByMe(true);
                else
                    chat.setSentByMe(false);
            }
            setIsReceiveRecentChatSuccess(result);
        });
    }

    public void receiveOldChat(LocalDateTime beforeTimestamp){
        chatRepository.receiveOldChat(chatroom.getId(), beforeTimestamp, result -> {
            if(result.getData() != null){
                Chat chat = result.getData();
                if(chat.getSender().equals(senderUsername))
                    chat.setSentByMe(true);
                else
                    chat.setSentByMe(false);
            }
            setIsReceiveOldChatSuccess(result);
        });
    }

    public void loadMembersOfChatroom(){
        chatroomRepository.loadMembersOfChatroom(chatroom.getId(), this::setIsLoadMembersOfChatroomSuccess);
    }
}