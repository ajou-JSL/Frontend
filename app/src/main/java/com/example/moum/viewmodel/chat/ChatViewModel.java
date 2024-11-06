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
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatRepository;
import com.example.moum.utils.Validation;

import java.time.LocalDateTime;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ChatViewModel extends AndroidViewModel {
    private final ChatRepository chatRepository;
    private MutableLiveData<Chat> receivedChat = new MutableLiveData<>();
    private MutableLiveData<Result<Chat>> isChatSendSuccess = new MutableLiveData<>();
    private PublishSubject<Result<Chat>> isReceiveRecentChatSuccess = PublishSubject.create();
    private PublishSubject<Result<Chat>> isReceiveOldChatSuccess = PublishSubject.create();
    private String sender;
    private Chatroom chatroom;
    private String TAG = getClass().toString();

    public ChatViewModel(Application application){

        super(application);
        chatRepository = ChatRepository.getInstance(application);
    }

//    public ChatViewModel(ChatRepository chatRepository){
//        this.chatRepository = chatRepository;
//    }

    public void setIsChatSendSuccess(Result<Chat> isChatSendSuccess) {
        this.isChatSendSuccess.setValue(isChatSendSuccess);
    }

    public void setIsReceiveRecentChatSuccess(Result<Chat> isReceiveRecentChatSuccess) {
        this.isReceiveRecentChatSuccess.onNext(isReceiveRecentChatSuccess);
    }

    public void setIsReceiveOldChatSuccess(Result<Chat> isReceiveOldChatSuccess) {
        this.isReceiveOldChatSuccess.onNext(isReceiveOldChatSuccess);
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

    public void setChatroomInfo(String sender, Integer groupId, String receiverId, Integer chatroomId, String chatroomName, Chatroom.ChatroomType chatroomType, String chatroomLeader) {
        this.sender = sender;
        this.chatroom = new Chatroom(groupId, receiverId, chatroomId, chatroomName, chatroomType, chatroomLeader);
    }

    public MutableLiveData<Chat> getReceivedChat() {
        return receivedChat;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void chatSend(String message){
        Chat chat = new Chat(sender, null, message, chatroom.getChatroomId(), LocalDateTime.now());
        chatRepository.chatSend(chat, result -> {
            if(result.getData() != null){
                result.getData().setSentByMe(true);
            }
            setIsChatSendSuccess(result);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void chatSendTest(String message){
        Chat chat = new Chat(sender, null, message, chatroom.getChatroomId(), LocalDateTime.now());
        chat.setSentByMe(true);
        Result<Chat> result = new Result<>(Validation.CHAT_POST_SUCCESS, chat);
        setIsChatSendSuccess(result);
    }

    public void receiveRecentChat(){
        chatRepository.receiveRecentChat(chatroom.getChatroomId(), result -> {
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
        chatRepository.receiveOldChat(chatroom.getChatroomId(), beforeTimestamp, result -> {
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
