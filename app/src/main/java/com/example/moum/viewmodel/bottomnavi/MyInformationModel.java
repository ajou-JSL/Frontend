package com.example.moum.viewmodel.bottomnavi;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.api.LoginApi;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.LoginRepository;

public class MyInformationModel extends AndroidViewModel {
    private LoginRepository loginRepository;
    private final MutableLiveData<Result<Object>> isLogoutSuccess = new MutableLiveData<>();

    public MyInformationModel(Application application) {
        super(application);
        loginRepository = LoginRepository.getInstance(application);
    }

    public MutableLiveData<Result<Object>> getIsLogoutSuccess() {
        return isLogoutSuccess;
    }

    public void setIsLogoutSuccess(Result<Object> isLogoutSuccess){
        this.isLogoutSuccess.setValue(isLogoutSuccess);
    }

    public void logout(){
        loginRepository.logout(this::setIsLogoutSuccess);
    }
}