package com.example.moum.viewmodel;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.User;
import com.example.moum.repository.SignupRepository;

import java.util.regex.Pattern;

public class SignupViewModel extends ViewModel {

    private SignupRepository signupRepository;
    private final MutableLiveData<User> user = new MutableLiveData<>(new User());
    private final MutableLiveData<Pair<Boolean, String>> isEmailAuthSuccess = new MutableLiveData<>();

    public SignupViewModel() {
        signupRepository = SignupRepository.getInstance();
    }

    /*getter, setter*/
    public LiveData<User> getUser() {
        return user;
    }

    private void setIsEmailAuthSuccess(Boolean isSuccess, String description){
        Pair<Boolean, String> pair = new Pair<>(isSuccess, description);
        isEmailAuthSuccess.setValue(pair);
    }

    public void emailAuth(){

        /*null check*/
        if (user.getValue() == null || user.getValue().getEmail() == null) {

            setIsEmailAuthSuccess(false, "이메일을 입력하세요.");
        }

        /*validation check*/
        String email = user.getValue().getEmail();
        String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern emailPattern = Pattern.compile(emailFormat);
        if(!emailPattern.matcher(email).matches()){
            setIsEmailAuthSuccess(false, "이메일이 유효하지 않습니다.");
        }

        /*goto repository*/
        signupRepository.emailAuth(email, result -> {
            setIsEmailAuthSuccess(result.first, result.second);
        });
    }

    public void storeBasic(){

    }

    public void storeProfile(){

    }

    public void signup(){

    }
}
