package com.example.moum.viewmodel.profile;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.repository.ProfileRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.util.ArrayList;

public class MemberProfileViewModel extends AndroidViewModel {
    private final ProfileRepository profileRepository;
    private final ChatroomRepository chatroomRepository;
    private final MutableLiveData<Result<Member>> isLoadMemberProfileSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Chatroom>> isCreateChatroomSuccess = new MutableLiveData<>();

    public MemberProfileViewModel(Application application) {
        super(application);
        profileRepository = ProfileRepository.getInstance(application);
        chatroomRepository = ChatroomRepository.getInstance(application);
    }

    public MutableLiveData<Result<Member>> getIsLoadMemberProfileSuccess() {
        return isLoadMemberProfileSuccess;
    }

    public MutableLiveData<Result<Chatroom>> getIsCreateChatroomSuccess() {
        return isCreateChatroomSuccess;
    }

    public void setIsLoadMemberProfileSuccess(Result<Member> isLoadMemberProfileSuccess) {
        this.isLoadMemberProfileSuccess.setValue(isLoadMemberProfileSuccess);
    }

    public void setIsCreateChatroomSuccess(Result<Chatroom> isCreateChatroomSuccess) {
        this.isCreateChatroomSuccess.setValue(isCreateChatroomSuccess);
    }

    public void loadMemberProfile(Integer targetMemberId) {
        // goto repository
        profileRepository.loadMemberProfile(targetMemberId, this::setIsLoadMemberProfileSuccess);
    }

    public void createChatroom(Member targetMember, Integer myId, String myName, Context context) {
        // valid check
        if (isLoadMemberProfileSuccess.getValue() == null
                || isLoadMemberProfileSuccess.getValue().getValidation() != Validation.GET_PROFILE_SUCCESS) {
            Result<Chatroom> result = new Result<>(Validation.PROFILE_NOT_LOADED);
            setIsCreateChatroomSuccess(result);
            return;
        } else if (targetMember.getId() == myId) {
            Result<Chatroom> result = new Result<>(Validation.CHATROOM_WITH_ME);
            setIsCreateChatroomSuccess(result);
            return;
        }

        // processing
        File chatroomProfileFile = null;
        if (targetMember.getProfileImageUrl() != null) {
            Uri uri = Uri.parse(targetMember.getProfileImageUrl());
            ImageManager imageManager = new ImageManager(context);
            chatroomProfileFile = imageManager.convertUriToFile(uri);
        }
        ArrayList<Integer> participants = new ArrayList<>();
        participants.add(targetMember.getId());
        participants.add(myId);
        String chatroomName = targetMember.getName() + ", " + myName;

        // goto repository
        Chatroom chatroom = new Chatroom(chatroomName, Chatroom.ChatroomType.PERSONAL_CHAT, null, null);
        chatroomRepository.createChatroom(chatroom, chatroomProfileFile, participants, this::setIsCreateChatroomSuccess);
    }
}
