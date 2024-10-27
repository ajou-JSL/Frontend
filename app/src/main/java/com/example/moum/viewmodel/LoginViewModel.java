package com.example.moum.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.R;
import com.example.moum.data.dto.LoginRequest;
import com.example.moum.data.entity.Token;
import com.example.moum.repository.LoginRepository;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private final LoginRepository loginRepository;
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<Validation> isLoginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Token> token = new MutableLiveData<>();

    public LoginViewModel(){
        loginRepository = LoginRepository.getInstance();
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<Validation> getIsLoginSuccess() {
        return isLoginSuccess;
    }

    public MutableLiveData<Token> getToken() {
        return token;
    }

    public void setIsLoginSuccess(Validation validation){
        isLoginSuccess.setValue(validation);
    }

    public void login(){

        /*null check*/
        if(email.getValue() == null || email.getValue().isEmpty()){
            setIsLoginSuccess(Validation.EMAIL_NOT_WRITTEN);
            return;
        }
        else if(password.getValue() == null || password.getValue().isEmpty()){
            setIsLoginSuccess(Validation.PASSWORD_NOT_WRITTEN);
            return;
        }

        /*valid check*/
        String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String passwordFormat = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$";
        Pattern emailPattern = Pattern.compile(emailFormat);
        Pattern passwordPattern = Pattern.compile(passwordFormat);
        if(!emailPattern.matcher(email.getValue()).matches()){
            setIsLoginSuccess(Validation.EMAIL_NOT_FORMAL);
            return;
        }
        else if(!passwordPattern.matcher(password.getValue()).matches()){
            setIsLoginSuccess(Validation.PASSWORD_NOT_FORMAL);
            return;
        }

        /*goto repository*/
        loginRepository.login(email.getValue(), password.getValue(), result -> {

            if(result.getValidation() != null && result.getValidation() == Validation.VALID_ALL && result.getData() != null){
                this.token.setValue(result.getData());
            }
            setIsLoginSuccess(result.getValidation());

        });

    }
}
