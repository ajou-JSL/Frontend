package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.MoumPracticeroom;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;

import java.util.List;

public class MoumMapPracticeroomViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Boolean> isNaverMapReady = new MutableLiveData<>();
    private final MutableLiveData<Result<Practiceroom>> isLoadPracticeroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<MoumPracticeroom>> isCreatePracticeroomSuccess = new MutableLiveData<>();

    public MoumMapPracticeroomViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Boolean> getIsNaverMapReady() {
        return isNaverMapReady;
    }

    public MutableLiveData<Result<Practiceroom>> getIsLoadPracticeroomSuccess() {
        return isLoadPracticeroomSuccess;
    }

    public MutableLiveData<Result<MoumPracticeroom>> getIsCreatePracticeroomSuccess() {
        return isCreatePracticeroomSuccess;
    }

    public void setIsNaverMapReady(Boolean isNaverMapReady){
        this.isNaverMapReady.setValue(isNaverMapReady);
    }

    public void setIsLoadPracticeroomSuccess(Result<Practiceroom> isLoadPracticeroomSuccess){
        this.isLoadPracticeroomSuccess.setValue(isLoadPracticeroomSuccess);
    }

    public void setIsCreatePracticeroomSuccess(Result<MoumPracticeroom> isCreatePracticeroomSuccess){
        this.isCreatePracticeroomSuccess.setValue(isCreatePracticeroomSuccess);
    }

    public void loadPracticeroom(Integer practiceroomId){
        practiceNPerformRepository.getPracticeroom(practiceroomId, this::setIsLoadPracticeroomSuccess);
    }

    public void createPracticeroom(Integer moumId, Integer roomId){
        practiceNPerformRepository.createPracticeroomOfMoum(moumId, roomId, null, this::setIsCreatePracticeroomSuccess);
    }
}
