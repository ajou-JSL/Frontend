package com.example.moum.viewmodel.auth;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.User;
import com.example.moum.repository.SignupRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignupViewModel extends ViewModel {

    private SignupRepository signupRepository;
    public String TAG = getClass().toString();
    private final MutableLiveData<User> user = new MutableLiveData<>(new User());
    private final MutableLiveData<Validation> isEmailAuthSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isEmailCodeSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isBasicValid = new MutableLiveData<>();
    private final MutableLiveData<Validation> isProfileValid = new MutableLiveData<>();
    private final MutableLiveData<Validation> isPersonalAgree = new MutableLiveData<>();
    private final MutableLiveData<Validation> isSignupSuccess = new MutableLiveData<>();

    private final MutableLiveData<String> proficiency = new MutableLiveData<>("하");
    private final MutableLiveData<String> address = new MutableLiveData<>("");
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();
    private ArrayList<Record> records = new ArrayList<>();

    public SignupViewModel() {
        signupRepository = SignupRepository.getInstance();
    }

    public SignupViewModel(SignupRepository signupRepository){
        this.signupRepository = signupRepository;
    }

    /*getter, setter*/
    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) { this.user.setValue(user);}

    public void setIsEmailAuthSuccess(Validation validation){
        isEmailAuthSuccess.setValue(validation);
    }

    public void setIsEmailCodeSuccess(Validation validation) {
        isEmailCodeSuccess.setValue(validation);
    }

    public void setIsBasicValid(Validation validation) {
        isBasicValid.setValue(validation);
    }

    public void setIsProfileValid(Validation validation) {
        isProfileValid.setValue(validation);
    }

    public void setIsPersonalAgree(Validation validation) {isPersonalAgree.setValue(validation);}

    public void setIsSignupSuccess(Validation validation) {isSignupSuccess.setValue(validation);}

    public void setProficiency(String string) {proficiency.setValue(string);}

    public void setAddress(String string) {address.setValue(string);}

    public void setProfileImage(Uri uri) {profileImage.setValue(uri);}

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

    public MutableLiveData<Validation> getIsPersonalAgree() {
        return isPersonalAgree;
    }

    public MutableLiveData<Validation> getIsSignupSuccess() {
        return isSignupSuccess;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
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
        signupRepository.emailAuth(email, result -> {
            setIsEmailAuthSuccess(result.getValidation());
        });
    }

    public void validCheckBasic(){

        /*null or empty check*/
        if(user.getValue() == null){
            setIsBasicValid(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(user.getValue().getMemberId() == null || user.getValue().getMemberId().isEmpty()) {
            setIsBasicValid(Validation.ID_NOT_WRITTEN);
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
        String idFormat = "^[a-z0-9]{4,20}$";
        String passwordFormat = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$";
        String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String emailCodeFormat = "^[0-9a-zA-Z]{6}$";
        Pattern idPattern = Pattern.compile(idFormat);
        Pattern passwordPattern = Pattern.compile(passwordFormat);
        Pattern emailPattern = Pattern.compile(emailFormat);
        Pattern emailCodePattern = Pattern.compile(emailCodeFormat);
        if(!idPattern.matcher(user.getValue().getMemberId()).matches()){
            setIsBasicValid(Validation.ID_NOT_FORMAL);
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

        /*isEmailCodeSuccess check*/
        if(isEmailCodeSuccess.getValue() == null || isEmailCodeSuccess.getValue() != Validation.EMAIL_AUTH_SUCCESS){
            setIsBasicValid(Validation.EMAIL_AUTH_NOT_TRIED);
            return;
        }

        /*isPersonalAgreement check*/
        if(isPersonalAgree.getValue() == null || isPersonalAgree.getValue() != Validation.VALID_ALL){
            setIsBasicValid(Validation.PERSONAL_NOT_AGREE);
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

    public void validCheckEmailCode(){

        /*null check*/
        if (user.getValue() == null || user.getValue().getEmailCode() == null || user.getValue().getEmailCode().isEmpty()) {
            setIsEmailCodeSuccess(Validation.EMAIL_CODE_NOT_WRITTEN);
            return;
        }

        /*validation check*/
        String emailCode = user.getValue().getEmailCode();
        String emailCodeFormat = "^[0-9a-zA-Z]{6}$";
        Pattern emailCodePattern = Pattern.compile(emailCodeFormat);
        if(!emailCodePattern.matcher(emailCode).matches()){
            setIsEmailCodeSuccess(Validation.EMAIL_CODE_NOT_FORMAL);
            return;
        }

        /*emailAuthSuccess check*/
        if(getIsEmailAuthSuccess().getValue() == null || getIsEmailAuthSuccess().getValue() != Validation.EMAIL_AUTH_SUCCESS){
            setIsEmailCodeSuccess(Validation.EMAIL_AUTH_NOT_TRIED);
        }
        String email = user.getValue().getEmail();

        /*goto repository*/
        signupRepository.checkEmailCode(email, emailCode, result -> {
            setIsEmailCodeSuccess(result.getValidation());
        });
    }

    public void setBasic(String memberId, String password, String email){

        if(user.getValue() == null){
            user.setValue(new User());
        }
        User userValue = user.getValue();
        userValue.setMemberId(memberId);
        userValue.setPassword(password);
        userValue.setEmail(email);
    }

    public void validCheckProfile(){

        /*null check*/
        if(user.getValue() == null){
            setIsProfileValid(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(user.getValue().getNickname() == null || user.getValue().getNickname().isEmpty()) {
            setIsProfileValid(Validation.NICKNAME_NOT_WRITTEN);
            return;
        }
        else if(user.getValue().getInstrument() == null || user.getValue().getInstrument().isEmpty()) {
            setIsProfileValid(Validation.INSTRUMENT_NOT_WRITTEN);
            return;
        }
        else if(proficiency.getValue() == null || proficiency.getValue().isEmpty()) {
            setIsProfileValid(Validation.PROFICIENCY_NOT_WRITTEN);
            return;
        }

        /*if above all pass, valid all!*/
        setIsProfileValid(Validation.VALID_ALL);
    }

    public void addRecord(String name, LocalDate startDate, LocalDate endDate){

        Record newRecord = new Record(name, startDate, endDate);
        records.add(newRecord);

    }

    public void signup(Context context){


        /*isProfileValid check*/
        if(getIsProfileValid().getValue() == null || getIsProfileValid().getValue() != Validation.VALID_ALL){
            setIsSignupSuccess(Validation.NOT_VALID_ANYWAY);
        }

        /*processing for repository*/
        User userValue = user.getValue();
        if(profileImage.getValue() != null){

            Uri uri = profileImage.getValue();
            ImageManager imageManager = new ImageManager(context);
            File file = imageManager.convertUriToFile(uri);
            userValue.setProfileImage(file);
        }
        if(!records.isEmpty())
            userValue.setRecords(records);
        if(proficiency.getValue() != null)
            userValue.setProficiency(proficiency.getValue());
        if(address.getValue() != null)
            userValue.setAddress(address.getValue());

        /*goto repository*/
        signupRepository.signup(userValue, result -> {
            setIsSignupSuccess(result.getValidation());
        });
    }

}
