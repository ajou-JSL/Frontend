package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.MoumPerformHall;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;
import com.example.moum.utils.Validation;

import java.util.List;

public class MoumMapPerformanceHallViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Boolean> isNaverMapReady = new MutableLiveData<>();
    private final MutableLiveData<Result<PerformanceHall>> isLoadPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<MoumPerformHall>> isCreatePerformanceHallSuccess = new MutableLiveData<>();
    private final MediatorLiveData<Boolean> isAllReady = new MediatorLiveData<>();

    public MoumMapPerformanceHallViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
        setupCombinedObserver(isNaverMapReady, isLoadPerformanceHallSuccess);
    }

    public void setupCombinedObserver(MutableLiveData<Boolean> data1, MutableLiveData<Result<PerformanceHall>> data2) {
        isAllReady.addSource(data1, value -> combineValues(data1.getValue(), data2.getValue()));
        isAllReady.addSource(data2, value -> combineValues(data1.getValue(), data2.getValue()));
    }

    private void combineValues(Boolean value1, Result<PerformanceHall> value2) {
        // 두 LiveData가 모두 true일 때만 MediatorLiveData를 true로 설정
        if (Boolean.TRUE.equals(value1) && value2 != null && value2.getValidation() == Validation.PERFORMANCE_HALL_GET_SUCCESS) {
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

    public MutableLiveData<Result<PerformanceHall>> getIsLoadPerformanceHallSuccess() {
        return isLoadPerformanceHallSuccess;
    }

    public MutableLiveData<Result<MoumPerformHall>> getIsCreatePerformanceHallSuccess() {
        return isCreatePerformanceHallSuccess;
    }

    public void setIsNaverMapReady(Boolean isNaverMapReady){
        this.isNaverMapReady.setValue(isNaverMapReady);
    }

    public void setIsLoadPerformanceHallSuccess(Result<PerformanceHall> isLoadPerformanceHallSuccess){
        this.isLoadPerformanceHallSuccess.setValue(isLoadPerformanceHallSuccess);
    }

    public void setIsCreatePerformanceHallSuccess(Result<MoumPerformHall> isCreatePerformanceHallSuccess){
        this.isCreatePerformanceHallSuccess.setValue(isCreatePerformanceHallSuccess);
    }

    public void loadPerformanceHall(Integer performanceHallId){
        practiceNPerformRepository.getPerformHall(performanceHallId, this::setIsLoadPerformanceHallSuccess);
    }

    public void createPerformanceHall(Integer moumId, Integer hallId){
        practiceNPerformRepository.createPerformHallOfMoum(moumId, hallId, null, this::setIsCreatePerformanceHallSuccess);
    }
}
