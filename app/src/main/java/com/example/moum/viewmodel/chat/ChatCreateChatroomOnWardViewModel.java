package com.example.moum.viewmodel.chat;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatCreateChatroomOnWardViewModel extends ViewModel {
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(Uri uri){
        profileImage.setValue(uri);
    }
}
