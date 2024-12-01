package com.example.moum.viewmodel.myinfo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ProfileRepository;

public class MyInformationViewModel extends AndroidViewModel {
    private ProfileRepository profileRepository;
    private final MutableLiveData<Result<Member>> isLoadMemberProfileSuccess = new MutableLiveData<>();

    public MyInformationViewModel(Application application) {
        super(application);
        profileRepository = ProfileRepository.getInstance(application);
    }

    public MutableLiveData<Result<Member>> getIsLoadMemberProfileSuccess() {
        return isLoadMemberProfileSuccess;
    }

    public void setIsLoadMemberProfileSuccess(Result<Member> isLoadMemberProfileSuccess){
        this.isLoadMemberProfileSuccess.setValue(isLoadMemberProfileSuccess);
    }

    public void loadMemberProfile(Integer memberId){
        profileRepository.loadMemberProfile(memberId, this::setIsLoadMemberProfileSuccess);
    }
}