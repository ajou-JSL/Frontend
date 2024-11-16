package com.example.moum.viewmodel.bottomnavi;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.api.LoginApi;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.LoginRepository;

public class MyInformationModel extends AndroidViewModel {
    private LoginRepository loginRepository;
    private final MutableLiveData<Result<Member>> isLogoutSuccess = new MutableLiveData<>();

    public MyInformationModel(Application application) {
        super(application);
        loginRepository = new LoginRepository(application);
    }

    public MutableLiveData<Result<Member>> getIsLogoutSuccess() {
        return isLogoutSuccess;
    }

    public void setIsLogoutSuccess(Result<Member> isLogoutSuccess){
        this.isLogoutSuccess.setValue(isLogoutSuccess);
    }

    public void logout(){
        loginRepository.logout(this::setIsLogoutSuccess);
    }
}