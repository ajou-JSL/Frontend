package com.example.moum.viewmodel;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.SignupRequest;
import com.example.moum.repository.SignupRepository;
import com.example.moum.utils.Validation;

import java.util.regex.Pattern;

public class SignupViewModel extends ViewModel {

    private SignupRepository signupRepository;
    private final MutableLiveData<SignupRequest> user = new MutableLiveData<>(new SignupRequest());
    private final MutableLiveData<Validation> isEmailAuthSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isEmailCodeSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isBasicValid = new MutableLiveData<>();
    private final MutableLiveData<Validation> isProfileValid = new MutableLiveData<>();

    public SignupViewModel() {
        signupRepository = SignupRepository.getInstance();
    }

    /*getter, setter*/
    public LiveData<SignupRequest> getUser() {
        return user;
    }

    private void setIsEmailAuthSuccess(Validation validation){
        isEmailAuthSuccess.setValue(validation);
    }

    private void setIsEmailCodeSuccess(Validation validation) {
        isEmailCodeSuccess.setValue(validation);
    }

    public void setIsBasicValid(Validation validation) {
        isBasicValid.setValue(validation);
    }

    public void setIsProfileValid(Validation validation) {
        isBasicValid.setValue(validation);
    }

    public MutableLiveData<Validation> getIsEmailAuthSuccess() {
        return isEmailAuthSuccess;
    }

    public MutableLiveData<Validation> getIsBasicValid() {
        return isBasicValid;
    }

    public MutableLiveData<Validation> getIsProfileValid() {
        return isProfileValid;
    }

    public MutableLiveData<Validation> getIsEmailCodeSuccess() {
        return isEmailCodeSuccess;
    }

    public void emailAuth(){

        /*null check*/
        if (user.getValue() == null || user.getValue().getEmail() == null || user.getValue().getEmail().isEmpty()) {
            setIsEmailAuthSuccess(Validation.EMAIL_NOT_WRITTEN);
            return;
        }

        /*validation check*/
        String email = user.getValue().getEmail();
        String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern emailPattern = Pattern.compile(emailFormat);
        if(!emailPattern.matcher(email).matches()){
            setIsEmailAuthSuccess(Validation.EMAIL_NOT_FORMAL);
            return;
        }

        /*goto repository*/
        signupRepository.emailAuth(email, this::setIsEmailAuthSuccess);
    }

    public void validCheckBasic(){

        /*null or empty check*/
        if(user.getValue() == null){
            setIsBasicValid(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(user.getValue().getName() == null || user.getValue().getName().isEmpty()) {
            setIsBasicValid(Validation.NAME_NOT_WRITTEN);
            return;
        }
        else if(user.getValue().getPassword() == null || user.getValue().getPassword().isEmpty()) {
            setIsBasicValid(Validation.PASSWORD_NOT_WRITTEN);
            return;
        }
        else if(user.getValue().getPasswordCheck() == null || user.getValue().getPasswordCheck().isEmpty()) {
            setIsBasicValid(Validation.PASSWORD_CHECK_NOT_WRITTEN);
            return;
        }
        else if(user.getValue().getEmail() == null || user.getValue().getEmail().isEmpty()) {
            setIsBasicValid(Validation.EMAIL_NOT_WRITTEN);
            return;
        }
        else if(user.getValue().getEmailCode() == null || user.getValue().getEmailCode().isEmpty()) {
            setIsBasicValid(Validation.EMAIL_CODE_NOT_WRITTEN);
            return;
        }

        /*formal check*/
        String nameFormat = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{2,10}$";
        String passwordFormat = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$";
        String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String emailCodeFormat = "^[0-9]{6}$";
        Pattern namePattern = Pattern.compile(nameFormat);
        Pattern passwordPattern = Pattern.compile(passwordFormat);
        Pattern emailPattern = Pattern.compile(emailFormat);
        Pattern emailCodePattern = Pattern.compile(emailCodeFormat);
        if(!namePattern.matcher(user.getValue().getName()).matches()){
            setIsBasicValid(Validation.NAME_NOT_FORMAL);
            return;
        }
        else if(!passwordPattern.matcher(user.getValue().getPassword()).matches()){
            setIsBasicValid(Validation.PASSWORD_NOT_FORMAL);
            return;
        }
        else if(!emailPattern.matcher(user.getValue().getEmail()).matches()){
            setIsBasicValid(Validation.EMAIL_NOT_FORMAL);
            return;
        }
        else if(!emailCodePattern.matcher(user.getValue().getEmailCode()).matches()){
            setIsBasicValid(Validation.EMAIL_CODE_NOT_FORMAL);
            return;
        }

        /*isEmailAuthSuccess check*/
        if(isEmailAuthSuccess.getValue() != Validation.VALID_ALL){
            setIsBasicValid(Validation.EMAIL_AUTH_NOT_TRIED);
            return;
        }

        /*other check*/
        if(!user.getValue().getPassword().equals(user.getValue().getPasswordCheck())){
            setIsBasicValid(Validation.PASSWORD_NOT_EQUAL);
            return;
        }

        /*if above all pass, valid all!*/
        setIsBasicValid(Validation.VALID_ALL);

    }

    public void validCheckProfile(){
        //TO-DO
    }

    public void validCheckEmailCode(){

        /*null check*/
        if (user.getValue() == null || user.getValue().getEmailCode() == null || user.getValue().getEmailCode().isEmpty()) {
            setIsEmailAuthSuccess(Validation.EMAIL_CODE_NOT_WRITTEN);
            return;
        }

        /*validation check*/
        String emailCode = user.getValue().getEmailCode();
        String emailCodeFormat = "^[0-9]{6}$";
        Pattern emailCodePattern = Pattern.compile(emailCodeFormat);
        if(!emailCodePattern.matcher(emailCode).matches()){
            setIsEmailAuthSuccess(Validation.EMAIL_CODE_NOT_FORMAL);
            return;
        }

        /*goto repository*/
        signupRepository.checkEmailCode(emailCode, this::setIsEmailCodeSuccess);
    }


    public void signup(){
        //TO-DO
    }
}
