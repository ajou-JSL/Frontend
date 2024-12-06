package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.dto.MemberProfileRankResponse;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PerformRepository;
import com.example.moum.repository.ProfileRepository;

import java.util.List;

public class BoardMemberViewModel extends AndroidViewModel {
    private ProfileRepository profileRepository;
    private final MutableLiveData<Result<List<MemberProfileRankResponse>>> isLoadMembersSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<MemberProfileRankResponse>>> isLoadNextMembersSuccess = new MutableLiveData<>();
    private final int LOAD_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 0;
    private Integer recentPageNumber = 0;

    public BoardMemberViewModel(Application application) {
        super(application);
        profileRepository = ProfileRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<MemberProfileRankResponse>>> getIsLoadMembersSuccess() {
        return isLoadMembersSuccess;
    }

    public MutableLiveData<Result<List<MemberProfileRankResponse>>> getIsLoadNextMembersSuccess() {
        return isLoadNextMembersSuccess;
    }

    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void setIsLoadMembersSuccess(Result<List<MemberProfileRankResponse>> isLoadMembersSuccess) {
        this.isLoadMembersSuccess.setValue(isLoadMembersSuccess);
    }

    public void setIsLoadNextMembersSuccess(Result<List<MemberProfileRankResponse>> isLoadNextMembersSuccess) {
        this.isLoadNextMembersSuccess.setValue(isLoadNextMembersSuccess);
    }

    public void clearPage() {
        page = 0;
    }

    public void loadMembers() {
        profileRepository.loadMembersByRank(page, LOAD_NUMBER, this::setIsLoadMembersSuccess);
        page = page + 1;
    }

    public void loadNextMembers() {
        if (recentPageNumber < LOAD_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PERFORM_NUMBER보다 적다면, 그만 불러오기
        profileRepository.loadMembersByRank(page, LOAD_NUMBER, this::setIsLoadNextMembersSuccess);
        page = page + 1;
    }
}