package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;

import java.util.List;

public class MoumMapPerformanceHallViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Boolean> isNaverMapReady = new MutableLiveData<>();
    private final MutableLiveData<Result<PerformanceHall>> isLoadPerformanceHallSuccess = new MutableLiveData<>();

    public MoumMapPerformanceHallViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Boolean> getIsNaverMapReady() {
        return isNaverMapReady;
    }

    public MutableLiveData<Result<PerformanceHall>> getIsLoadPerformanceHallSuccess() {
        return isLoadPerformanceHallSuccess;
    }

    public void setIsNaverMapReady(Boolean isNaverMapReady){
        this.isNaverMapReady.setValue(isNaverMapReady);
    }

    public void setIsLoadPerformanceHallSuccess(Result<PerformanceHall> isLoadPerformanceHallSuccess){
        this.isLoadPerformanceHallSuccess.setValue(isLoadPerformanceHallSuccess);
    }

    public void loadPerformanceHall(Integer performanceHallId){
        practiceNPerformRepository.getPerformHall(performanceHallId, this::setIsLoadPerformanceHallSuccess);
    }
}
