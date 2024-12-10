package com.example.moum.viewmodel.myinfo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.LoginRepository;

public class MyInfoLogoutNSignoutViewModel extends AndroidViewModel {
    private LoginRepository loginRepository;
    private final MutableLiveData<Result<Member>> isLoginSuccess = new MutableLiveData<>();
    private String TAG = getClass().toString();

    public MyInfoLogoutNSignoutViewModel(Application application) {
        super(application);
        loginRepository = new LoginRepository(application);
    }

    public MutableLiveData<Result<Member>> getIsLoginSuccess() {
        return isLoginSuccess;
    }

    public void setIsLoginSuccess(Result<Member> isLoginSuccess) {
        this.isLoginSuccess.setValue(isLoginSuccess);
    }

    public void logout() {
        loginRepository.logout(this::setIsLoginSuccess);
    }
}
