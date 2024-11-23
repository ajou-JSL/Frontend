package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;

import java.util.List;

public class MoumMapPracticeroomViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> isNaverMapReady = new MutableLiveData<>();
    private final MutableLiveData<Result<Practiceroom>> isLoadPracticeroomSuccess = new MutableLiveData<>();

    public MoumMapPracticeroomViewModel(Application application){
        super(application);
    }

    public MutableLiveData<Boolean> getIsNaverMapReady() {
        return isNaverMapReady;
    }

    public MutableLiveData<Result<Practiceroom>> getIsLoadPracticeroomSuccess() {
        return isLoadPracticeroomSuccess;
    }

    public void setIsNaverMapReady(Boolean isNaverMapReady){
        this.isNaverMapReady.setValue(isNaverMapReady);
    }

    public void setIsLoadPracticeroomSuccess(Result<Practiceroom> isLoadPracticeroomSuccess){
        this.isLoadPracticeroomSuccess.setValue(isLoadPracticeroomSuccess);
    }

    public void loadPracticeroom(Integer practiceroomId){

    }
}
