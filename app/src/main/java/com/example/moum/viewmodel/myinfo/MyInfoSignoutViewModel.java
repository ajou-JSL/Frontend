package com.example.moum.viewmodel.myinfo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.api.SignupApi;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Signout;
import com.example.moum.repository.SignupRepository;
import com.example.moum.utils.Validation;

public class MyInfoSignoutViewModel extends AndroidViewModel {
    private SignupRepository signupRepository;
    private final MutableLiveData<Signout> signout = new MutableLiveData<>(new Signout());
    private final MutableLiveData<Result<Member>> isSignoutSuccess = new MutableLiveData<>();

    public MyInfoSignoutViewModel(Application application){
        super(application);
        signupRepository = new SignupRepository(application);
    }

    public MutableLiveData<Signout> getSignout() {
        return signout;
    }

    public MutableLiveData<Result<Member>> getIsSignoutSuccess() {
        return isSignoutSuccess;
    }

    public void setIsSignoutSuccess(Result<Member> isSignoutSuccess){
        this.isSignoutSuccess.setValue(isSignoutSuccess);
    }

    public void signout(String username){
        /*null check*/
        if(signout.getValue() == null || signout.getValue().getUsername() == null || signout.getValue().getUsername().isEmpty()){
            Result<Member> result = new Result<>(Validation.ID_NOT_WRITTEN);
            setIsSignoutSuccess(result);
            return;
        }
        else if(!signout.getValue().getUsername().equals(username)){
            Result<Member> result = new Result<>(Validation.ID_NOT_EQUAL);
            setIsSignoutSuccess(result);
            return;
        }

        /*goto repository*/
        //TODO
    }
}
