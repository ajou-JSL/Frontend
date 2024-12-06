package com.example.moum.viewmodel.chat;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.R;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ChatInviteMemberViewModel extends AndroidViewModel {
    private final ChatroomRepository chatroomRepository;
    private final TeamRepository teamRepository;
    private final MutableLiveData<Result<Team>> isLoadTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Member>>> isLoadMembersOfChatroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Chatroom>> isInviteMemberSuccess = new MutableLiveData<>();
    private final ArrayList<Member> loadedMembers = new ArrayList<>();
    private String TAG = getClass().toString();

    public ChatInviteMemberViewModel(Application application) {
        super(application);
        chatroomRepository = ChatroomRepository.getInstance(application);
        teamRepository = TeamRepository.getInstance(application);
    }

    public void setIsLoadTeamSuccess(Result<Team> isLoadTeamSuccess) {
        this.isLoadTeamSuccess.setValue(isLoadTeamSuccess);
    }

    public void setIsLoadMembersOfChatroomSuccess(Result<List<Member>> isLoadMembersOfChatroomSuccess) {
        this.isLoadMembersOfChatroomSuccess.setValue(isLoadMembersOfChatroomSuccess);
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess) {
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsInviteMemberSuccess(Result<Chatroom> isInviteMemberSuccess) {
        this.isInviteMemberSuccess.setValue(isInviteMemberSuccess);
    }

    public MutableLiveData<Result<Team>> getIsLoadTeamSuccess() {
        return isLoadTeamSuccess;
    }

    public MutableLiveData<Result<List<Member>>> getIsLoadMembersOfChatroomSuccess() {
        return isLoadMembersOfChatroomSuccess;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Chatroom>> getIsInviteMemberSuccess() {
        return isInviteMemberSuccess;
    }

    public void loadTeam(Integer teamId) {
        teamRepository.loadTeam(teamId, this::setIsLoadTeamSuccess);
    }

    public void loadMembersOfChatroom(Integer chatroomId) {
        chatroomRepository.loadMembersOfChatroom(chatroomId, result -> {
            if (result.getValidation() == Validation.CHATROOM_MEMBER_LOAD_SUCCESS) {
                loadedMembers.clear();
                loadedMembers.addAll(result.getData());
            }
            setIsLoadMembersOfChatroomSuccess(result);
        });
    }

    public void validCheck(ArrayList<Member> membersToInvite) {
        /*valid check*/
        if (membersToInvite == null || membersToInvite.isEmpty()) {
            setIsValidCheckSuccess(Validation.MEMBER_NOT_EXIST);
            return;
        } else if (isLoadTeamSuccess.getValue() == null || isLoadTeamSuccess.getValue().getValidation() != Validation.GET_TEAM_SUCCESS ||
                isLoadMembersOfChatroomSuccess.getValue() == null
                || isLoadMembersOfChatroomSuccess.getValue().getValidation() != Validation.CHATROOM_MEMBER_LOAD_SUCCESS) {
            setIsValidCheckSuccess(Validation.MEMBER_NOT_EXIST);
            return;
        }
        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void inviteMemberIntoChatroom(Integer chatroomId, Chatroom.ChatroomType chatroomType, ArrayList<Member> membersToInvite, Context context) {
        /*valid check*/
        if (isValidCheckSuccess.getValue() != Validation.VALID_ALL) {
            Result<Chatroom> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsInviteMemberSuccess(result);
            return;
        }

        /*goto repository*/
        Chatroom chatroom = new Chatroom();
        chatroom.setType(chatroomType);
        ArrayList<Integer> membersToInviteInt = new ArrayList<>();
        for (Member member : membersToInvite) {
            membersToInviteInt.add(member.getId());
        }
        chatroomRepository.inviteMembers(chatroomId, chatroom, membersToInviteInt, this::setIsInviteMemberSuccess);
    }

}
