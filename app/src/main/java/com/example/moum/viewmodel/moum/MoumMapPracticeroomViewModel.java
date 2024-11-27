package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.MoumPracticeroom;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;
import com.example.moum.utils.Validation;

import java.util.List;

public class MoumMapPracticeroomViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Boolean> isNaverMapReady = new MutableLiveData<>();
    private final MutableLiveData<Result<Practiceroom>> isLoadPracticeroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<MoumPracticeroom>> isCreatePracticeroomSuccess = new MutableLiveData<>();
    private final MediatorLiveData<Boolean> isAllReady = new MediatorLiveData<>();

    public MoumMapPracticeroomViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
        setupCombinedObserver(isNaverMapReady, isLoadPracticeroomSuccess);
    }

    public void setupCombinedObserver(MutableLiveData<Boolean> data1, MutableLiveData<Result<Practiceroom>> data2) {
        isAllReady.addSource(data1, value -> combineValues(data1.getValue(), data2.getValue()));
        isAllReady.addSource(data2, value -> combineValues(data1.getValue(), data2.getValue()));
    }

    private void combineValues(Boolean value1, Result<Practiceroom> value2) {
        // 두 LiveData가 모두 true일 때만 MediatorLiveData를 true로 설정
        if (Boolean.TRUE.equals(value1) && value2 != null && value2.getValidation() == Validation.PRACTICE_ROOM_GET_SUCCESS) {
            isAllReady.setValue(true);
        } else {
            isAllReady.setValue(false);
        }
    }

    public MediatorLiveData<Boolean> getIsAllReady() {
        return isAllReady;
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
