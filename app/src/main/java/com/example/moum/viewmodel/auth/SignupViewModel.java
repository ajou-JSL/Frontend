package com.example.moum.viewmodel.auth;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.repository.SignupRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.YoutubeManager;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SignupViewModel extends AndroidViewModel {

    private SignupRepository signupRepository;
    public String TAG = getClass().toString();
    private final MutableLiveData<SignupUser> signupUser = new MutableLiveData<>(new SignupUser());
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
    private ArrayList<Genre> selectedGenres = new ArrayList<>();

    public SignupViewModel(Application application) {
        super(application);
        signupRepository = SignupRepository.getInstance(application);
    }

    public SignupViewModel(Application application, SignupRepository signupRepository) {
        super(application);
        this.signupRepository = signupRepository;
    }

    /*getter, setter*/
    public LiveData<SignupUser> getSignupUser() {
        return signupUser;
    }

    public void setSignupUser(SignupUser signupUser) {
        this.signupUser.setValue(signupUser);
    }

    public void setIsEmailAuthSuccess(Validation validation) {
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

    public void setIsPersonalAgree(Validation validation) {
        isPersonalAgree.setValue(validation);
    }

    public void setIsSignupSuccess(Validation validation) {
        isSignupSuccess.setValue(validation);
    }

    public void setProficiency(String string) {
        proficiency.setValue(string);
    }

    public void setAddress(String string) {
        address.setValue(string);
    }

    public void setProfileImage(Uri uri) {
        profileImage.setValue(uri);
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

    public MutableLiveData<Validation> getIsPersonalAgree() {
        return isPersonalAgree;
    }

    public MutableLiveData<Validation> getIsSignupSuccess() {
        return isSignupSuccess;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }

    public void emailAuth() {

        /*null check*/
        if (signupUser.getValue() == null || signupUser.getValue().getEmail() == null || signupUser.getValue().getEmail().isEmpty()) {
            setIsEmailAuthSuccess(Validation.EMAIL_NOT_WRITTEN);
            return;
        }

        /*validation check*/
        String email = signupUser.getValue().getEmail();
        String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern emailPattern = Pattern.compile(emailFormat);
        if (!emailPattern.matcher(email).matches()) {
            setIsEmailAuthSuccess(Validation.EMAIL_NOT_FORMAL);
            return;
        }

        /*goto repository*/
        signupRepository.emailAuth(email, result -> {
            setIsEmailAuthSuccess(result.getValidation());
        });
    }

    public void validCheckBasic() {

        /*null or empty check*/
        if (signupUser.getValue() == null) {
            setIsBasicValid(Validation.NOT_VALID_ANYWAY);
            return;
        } else if (signupUser.getValue().getUsername() == null || signupUser.getValue().getUsername().isEmpty()) {
            setIsBasicValid(Validation.ID_NOT_WRITTEN);
            return;
        } else if (signupUser.getValue().getPassword() == null || signupUser.getValue().getPassword().isEmpty()) {
            setIsBasicValid(Validation.PASSWORD_NOT_WRITTEN);
            return;
        } else if (signupUser.getValue().getPasswordCheck() == null || signupUser.getValue().getPasswordCheck().isEmpty()) {
            setIsBasicValid(Validation.PASSWORD_CHECK_NOT_WRITTEN);
            return;
        } else if (signupUser.getValue().getEmail() == null || signupUser.getValue().getEmail().isEmpty()) {
            setIsBasicValid(Validation.EMAIL_NOT_WRITTEN);
            return;
        } else if (signupUser.getValue().getEmailCode() == null || signupUser.getValue().getEmailCode().isEmpty()) {
            setIsBasicValid(Validation.EMAIL_CODE_NOT_WRITTEN);
            return;
        }

        /*formal check*/
        String idFormat = "^[a-z0-9]{3,20}$";
        String passwordFormat = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$";
        String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String emailCodeFormat = "^[0-9a-zA-Z]{6}$";
        Pattern idPattern = Pattern.compile(idFormat);
        Pattern passwordPattern = Pattern.compile(passwordFormat);
        Pattern emailPattern = Pattern.compile(emailFormat);
        Pattern emailCodePattern = Pattern.compile(emailCodeFormat);
        if (!idPattern.matcher(signupUser.getValue().getUsername()).matches()) {
            setIsBasicValid(Validation.ID_NOT_FORMAL);
            return;
        } else if (!passwordPattern.matcher(signupUser.getValue().getPassword()).matches()) {
            setIsBasicValid(Validation.PASSWORD_NOT_FORMAL);
            return;
        } else if (!emailPattern.matcher(signupUser.getValue().getEmail()).matches()) {
            setIsBasicValid(Validation.EMAIL_NOT_FORMAL);
            return;
        } else if (!emailCodePattern.matcher(signupUser.getValue().getEmailCode()).matches()) {
            setIsBasicValid(Validation.EMAIL_CODE_NOT_FORMAL);
            return;
        }

        /*isEmailCodeSuccess check*/
        if (isEmailCodeSuccess.getValue() == null || isEmailCodeSuccess.getValue() != Validation.EMAIL_AUTH_SUCCESS) {
            setIsBasicValid(Validation.EMAIL_AUTH_NOT_TRIED);
            return;
        }

        /*isPersonalAgreement check*/
        if (isPersonalAgree.getValue() == null || isPersonalAgree.getValue() != Validation.VALID_ALL) {
            setIsBasicValid(Validation.PERSONAL_NOT_AGREE);
            return;
        }

        /*other check*/
        if (!signupUser.getValue().getPassword().equals(signupUser.getValue().getPasswordCheck())) {
            setIsBasicValid(Validation.PASSWORD_NOT_EQUAL);
            return;
        }

        /*if above all pass, valid all!*/
        setIsBasicValid(Validation.VALID_ALL);

    }

    public void validCheckEmailCode() {

        /*null check*/
        if (signupUser.getValue() == null || signupUser.getValue().getEmailCode() == null || signupUser.getValue().getEmailCode().isEmpty()) {
            setIsEmailCodeSuccess(Validation.EMAIL_CODE_NOT_WRITTEN);
            return;
        }

        /*validation check*/
        String emailCode = signupUser.getValue().getEmailCode();
        String emailCodeFormat = "^[0-9a-zA-Z]{6}$";
        Pattern emailCodePattern = Pattern.compile(emailCodeFormat);
        if (!emailCodePattern.matcher(emailCode).matches()) {
            setIsEmailCodeSuccess(Validation.EMAIL_CODE_NOT_FORMAL);
            return;
        }

        /*emailAuthSuccess check*/
        if (getIsEmailAuthSuccess().getValue() == null || getIsEmailAuthSuccess().getValue() != Validation.EMAIL_AUTH_SUCCESS) {
            setIsEmailCodeSuccess(Validation.EMAIL_AUTH_NOT_TRIED);
        }
        String email = signupUser.getValue().getEmail();

        /*goto repository*/
        signupRepository.checkEmailCode(email, emailCode, result -> {
            setIsEmailCodeSuccess(result.getValidation());
        });
    }

    public void setBasic(String memberId, String password, String email, String emailCode) {

        if (signupUser.getValue() == null) {
            signupUser.setValue(new SignupUser());
        }
        SignupUser signupUserValue = signupUser.getValue();
        signupUserValue.setUsername(memberId);
        signupUserValue.setPassword(password);
        signupUserValue.setEmail(email);
        signupUserValue.setEmailCode(emailCode);
    }

    public void validCheckProfile() {

        /*null check*/
        if (signupUser.getValue() == null) {
            setIsProfileValid(Validation.NOT_VALID_ANYWAY);
            return;
        } else if (signupUser.getValue().getName() == null || signupUser.getValue().getName().isEmpty()) {
            setIsProfileValid(Validation.NICKNAME_NOT_WRITTEN);
            return;
        } else if (signupUser.getValue().getProfileDescription() == null || signupUser.getValue().getProfileDescription().isEmpty()) {
            setIsProfileValid(Validation.DESCRIPTION_NOT_WRITTEN);
            return;
        } else if (signupUser.getValue().getInstrument() == null || signupUser.getValue().getInstrument().isEmpty()) {
            setIsProfileValid(Validation.INSTRUMENT_NOT_WRITTEN);
            return;
        } else if (proficiency.getValue() == null || proficiency.getValue().isEmpty()) {
            setIsProfileValid(Validation.PROFICIENCY_NOT_WRITTEN);
            return;
        } else if (signupUser.getValue().getVideoUrl() != null && !YoutubeManager.isUrlValid(signupUser.getValue().getVideoUrl())) {
            setIsProfileValid(Validation.VIDEO_URL_NOT_FORMAL);
            return;
        }

        /*if above all pass, valid all!*/
        setIsProfileValid(Validation.VALID_ALL);
    }

    public void addRecord(String name, LocalDate startDate, LocalDate endDate) {
        String startDateStr = null;
        String endDateStr = null;
        if (startDate != null) {
            startDateStr = startDate.toString().concat("T00:00:00");
        }
        if (endDate != null) {
            endDateStr = endDate.toString().concat("T00:00:00");
        }
        Record newRecord = new Record(name, startDateStr, endDateStr);
        records.add(newRecord);
    }

    public void setGenres(Genre[] genres, ArrayList<Boolean> isSelecteds) {
        Log.e(TAG, "setGenres start");
        Log.e(TAG, "isSelecteds: " + isSelecteds.toString());
        selectedGenres.clear();
        ArrayList<Genre> genresList = new ArrayList<>(Arrays.asList(genres));
        if (isSelecteds != null) {
            for (int i = 0; i < genresList.size(); i++) {
                if (isSelecteds.get(i)) {
                    selectedGenres.add(genresList.get(i));
                }
            }
        }
    }

    public void signup(Context context) {
        /*isProfileValid check*/
        if (getIsProfileValid().getValue() == null || getIsProfileValid().getValue() != Validation.VALID_ALL) {
            setIsSignupSuccess(Validation.NOT_VALID_ANYWAY);
            return;
        }

        /*processing for repository*/
        SignupUser signupUserValue = signupUser.getValue();
        if (profileImage.getValue() != null) {
            Uri uri = profileImage.getValue();
            ImageManager imageManager = new ImageManager(context);
            File file = imageManager.convertUriToFile(uri);
            signupUserValue.setProfileImage(file);
        }
        if (records != null) {
            for (Record record : records) {
                if (record.getStartDate() != null && record.getEndDate() != null && record.getStartDate().compareTo(record.getEndDate()) > 0) {
                    setIsSignupSuccess(Validation.RECORD_NOT_VALID);
                    return;
                } else if (record.getRecordName() == null || record.getRecordName().isEmpty()) {
                    setIsSignupSuccess(Validation.RECORD_NAME_NOT_WRITTEN);
                    return;
                }
            }
            signupUserValue.setRecords(records);
        } else {
            signupUserValue.setRecords(new ArrayList<Record>());
        }
        if (proficiency.getValue() != null) {
            signupUserValue.setProficiency(proficiency.getValue());
        }
        if (address.getValue() != null) {
            signupUserValue.setAddress(address.getValue());
        }
        if (selectedGenres != null) {
            signupUserValue.setGenres(selectedGenres);
        }

        /*goto repository*/
        signupRepository.signup(signupUserValue, result -> {
            setIsSignupSuccess(result.getValidation());
        });
    }

}
