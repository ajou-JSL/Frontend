package com.example.moum.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.dto.LoginRequest;
import com.example.moum.utils.Validation;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<Validation> isLoginSuccess = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<Validation> getIsLoginSuccess() {
        return isLoginSuccess;
    }

    public void setIsLoginSuccess(Validation validation){
        isLoginSuccess.setValue(validation);
    }

    public void login(){

    }
}
