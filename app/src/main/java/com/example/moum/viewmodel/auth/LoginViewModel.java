package com.example.moum.viewmodel.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Token;
import com.example.moum.repository.LoginRepository;
import com.example.moum.utils.Validation;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private final LoginRepository loginRepository;
    private final MutableLiveData<String> id = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<Validation> isLoginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Token> token = new MutableLiveData<>();

    public LoginViewModel(){
        loginRepository = LoginRepository.getInstance();
    }

    public LoginViewModel(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }

    public MutableLiveData<String> getId() {
        return id;
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

    public void setId(String id) { this.id.setValue(id); }

    public void setPassword(String password) { this.password.setValue(password); }

    public void setIsLoginSuccess(Validation validation){
        isLoginSuccess.setValue(validation);
    }

    public void login(){

        /*null check*/
        if(id.getValue() == null || id.getValue().isEmpty()){
            setIsLoginSuccess(Validation.ID_NOT_WRITTEN);
            return;
        }
        else if(password.getValue() == null || password.getValue().isEmpty()){
            setIsLoginSuccess(Validation.PASSWORD_NOT_WRITTEN);
            return;
        }

        /*valid check*/
        //String idFormant = "^[a-z0-9]{4,20}$";
        String passwordFormat = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$";
        //Pattern idPattern = Pattern.compile(idFormant);
        Pattern passwordPattern = Pattern.compile(passwordFormat);
//        if(!idPattern.matcher(id.getValue()).matches()){
//            setIsLoginSuccess(Validation.ID_NOT_FORMAL);
//            return;
//        }
        if(!passwordPattern.matcher(password.getValue()).matches()){
            setIsLoginSuccess(Validation.PASSWORD_NOT_FORMAL);
            return;
        }

        /*goto repository*/
        loginRepository.login(id.getValue(), password.getValue(), result -> {

            if(result.getValidation() != null && result.getValidation() == Validation.VALID_ALL && result.getData() != null){
                Token token = result.getData();
                token.setMemberId(id.getValue());
            }
            setIsLoginSuccess(result.getValidation());

        });

    }
}
