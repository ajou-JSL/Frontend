package com.example.moum.viewmodel.myinfo;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.repository.ProfileRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.TimeManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyInformationUpdateViewModel extends AndroidViewModel {
    private ProfileRepository profileRepository;
    private MutableLiveData<Member> member = new MutableLiveData<>(new Member());
    private final MutableLiveData<Result<Member>> isLoadMemberProfileSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isProfileValid = new MutableLiveData<>();
    private final MutableLiveData<Result<Member>> isUpdateProfileSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> proficiency = new MutableLiveData<>("하");
    private final MutableLiveData<String> address = new MutableLiveData<>("");
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();
    private ArrayList<Record> records = new ArrayList<>();
    private Boolean fromExisting = false;
    private String email;

    public MyInformationUpdateViewModel(Application application) {
        super(application);
        profileRepository = ProfileRepository.getInstance(application);
    }

    public MutableLiveData<Member> getMember() {
        return member;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }

    public MutableLiveData<Result<Member>> getIsLoadMemberProfileSuccess() {
        return isLoadMemberProfileSuccess;
    }

    public MutableLiveData<Validation> getIsProfileValid() {
        return isProfileValid;
    }

    public MutableLiveData<Result<Member>> getIsUpdateProfileSuccess() {
        return isUpdateProfileSuccess;
    }

    public void setProfileImage(Uri profileImage, Boolean fromExisting){
        this.profileImage.setValue(profileImage);
        this.fromExisting = fromExisting;
    }

    public void setProficiency(String string) {proficiency.setValue(string);}

    public void setAddress(String string) {address.setValue(string);}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsLoadMemberProfileSuccess(Result<Member> isLoadMemberProfileSuccess){
        this.isLoadMemberProfileSuccess.setValue(isLoadMemberProfileSuccess);
    }

    public void setIsProfileValid(Validation isProfileValid){
        this.isProfileValid.setValue(isProfileValid);
    }

    public void setIsUpdateProfileSuccess(Result<Member> isUpdateProfileSuccess){
        this.isUpdateProfileSuccess.setValue(isUpdateProfileSuccess);
    }

    public void loadMemberProfile(Integer memberId){
        profileRepository.loadMemberProfile(memberId, this::setIsLoadMemberProfileSuccess);
    }

    public void addRecord(String name, LocalDate startDate, LocalDate endDate){
        String startDateStr = null;
        String endDateStr = null;
        if(startDate != null)
            startDateStr = startDate.toString().concat("T00:00:00");
        if(endDate != null)
            endDateStr = endDate.toString().concat("T00:00:00");
        Record newRecord = new Record(name, startDateStr,endDateStr);
        records.add(newRecord);
    }

    public void clearRecord(){
        records.clear();
    }

    public void validCheckProfile(){
        /*null check*/
        if(member.getValue() == null){
            setIsProfileValid(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(member.getValue().getName() == null || member.getValue().getName().isEmpty()) {
            setIsProfileValid(Validation.NICKNAME_NOT_WRITTEN);
            return;
        }
        else if(member.getValue().getInstrument() == null || member.getValue().getInstrument().isEmpty()) {
            setIsProfileValid(Validation.INSTRUMENT_NOT_WRITTEN);
            return;
        }
        else if(proficiency.getValue() == null || proficiency.getValue().isEmpty()) {
            setIsProfileValid(Validation.PROFICIENCY_NOT_WRITTEN);
            return;
        }

        /*valid check*/
        if(isLoadMemberProfileSuccess.getValue() == null || isLoadMemberProfileSuccess.getValue().getValidation() != Validation.GET_PROFILE_SUCCESS){
            setIsProfileValid(Validation.MEMBER_NOT_EXIST);
            return;
        }

        /*if above all pass, valid all!*/
        setIsProfileValid(Validation.VALID_ALL);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateProfile(Context context, Integer memberId, String username){
        /*processing*/
        Member memberToUpdate = member.getValue();

        /*isProfileValid check*/
        if(isProfileValid.getValue() != Validation.VALID_ALL){
            Result<Member> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsUpdateProfileSuccess(result);
            return;
        }

        /*processing for repository*/
        memberToUpdate.setUsername(username);
        if(email != null) memberToUpdate.setEmail(email);
        File profileFile = null;
//        if (profileImage.getValue() != null && fromExisting) {
//            Callable<File> callable = () -> {
//                ImageManager imageManager = new ImageManager(context);
//                return imageManager.downloadImageToFile(profileImage.getValue().toString());
//            };
//            FutureTask<File> futureTask = new FutureTask<>(callable);
//            Thread thread = new Thread(futureTask);
//            thread.start();
//            try {
//                profileFile = futureTask.get();
//                if (profileFile != null) {
//                    Log.d("ProfileFile", "File downloaded: " + profileFile.getAbsolutePath());
//                } else {
//                    Log.e("ProfileFile", "Failed to download profile image");
//                }
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        else if(profileImage.getValue() != null && !fromExisting)
//            profileFile = new ImageManager(context).convertUriToFile(profileImage.getValue());
        if(!records.isEmpty()){
            for(Record record : records)
                if(TimeManager.strToDate(record.getEndDate()).isEmpty() && !TimeManager.strToDate(record.getStartDate()).isEmpty()){
                    Result<Member> result = new Result<>(Validation.RECORD_NOT_VALID);
                    setIsUpdateProfileSuccess(result);
                    return;
                }
            memberToUpdate.setMemberRecords(records);
        }
        else
            memberToUpdate.setMemberRecords(new ArrayList<Record>());
        if(proficiency.getValue() != null)
            memberToUpdate.setProficiency(proficiency.getValue());
        if(address.getValue() != null)
            memberToUpdate.setAddress(address.getValue());
        ArrayList<Genre> genres = new ArrayList<>();
        memberToUpdate.setGenres(genres);

        /*goto repository*/
        profileRepository.updateMemberProfile(memberId, profileFile, memberToUpdate, this::setIsUpdateProfileSuccess);
    }
}