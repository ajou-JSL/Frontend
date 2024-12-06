package com.example.moum.viewmodel.chat;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ChatUpdateChatroomViewModel extends AndroidViewModel {
    private ChatroomRepository chatroomRepository;
    private final MutableLiveData<Result<Chatroom>> isLoadChatroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Chatroom>> isUpdateChatroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();
    private Boolean fromExisting = false;

    public ChatUpdateChatroomViewModel(Application application) {
        super(application);
        chatroomRepository = ChatroomRepository.getInstance(application);
    }

    public MutableLiveData<Result<Chatroom>> getIsLoadChatroomSuccess() {
        return isLoadChatroomSuccess;
    }

    public MutableLiveData<Result<Chatroom>> getIsUpdateChatroomSuccess() {
        return isUpdateChatroomSuccess;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }

    public void setIsLoadChatroomSuccess(Result<Chatroom> isLoadChatroomSuccess) {
        this.isLoadChatroomSuccess.setValue(isLoadChatroomSuccess);
    }

    public void setIsUpdateChatroomSuccess(Result<Chatroom> isUpdateChatroomSuccess) {
        this.isUpdateChatroomSuccess.setValue(isUpdateChatroomSuccess);
    }

    public void setProfileImage(Uri profileImage, Boolean fromExisting) {
        this.profileImage.setValue(profileImage);
        this.fromExisting = fromExisting;
    }

    public void loadChatroom(Integer chatroomId) {
        chatroomRepository.loadChatroom(chatroomId, this::setIsLoadChatroomSuccess);
    }

    public void updateChatroom(Integer chatroomId, String chatroomName, Context context) {
        /*valid check*/
        Chatroom chatroom = new Chatroom();
        chatroom.setName(chatroomName);
        if (isLoadChatroomSuccess.getValue() == null || isLoadChatroomSuccess.getValue().getValidation() != Validation.CHATROOM_GET_SUCCESS) {
            Result<Chatroom> result = new Result<>(Validation.CHATROOM_NOT_LOADED);
            setIsUpdateChatroomSuccess(result);
            return;
        }

        /*processing*/
        File profileFile = null;
        if (profileImage.getValue() != null && fromExisting) {
            Callable<File> callable = () -> {
                ImageManager imageManager = new ImageManager(context);
                return imageManager.downloadImageToFile(profileImage.getValue().toString());
            };
            FutureTask<File> futureTask = new FutureTask<>(callable);
            Thread thread = new Thread(futureTask);
            thread.start();
            try {
                profileFile = futureTask.get();
                if (profileFile != null) {
                    Log.d("ProfileFile", "File downloaded: " + profileFile.getAbsolutePath());
                } else {
                    Log.e("ProfileFile", "Failed to download profile image");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (profileImage.getValue() != null && !fromExisting) {
            profileFile = new ImageManager(context).convertUriToFile(profileImage.getValue());
        }

        /*goto repository*/
        chatroomRepository.updateChatroom(chatroomId, chatroom, profileFile, this::setIsUpdateChatroomSuccess);
    }
}
