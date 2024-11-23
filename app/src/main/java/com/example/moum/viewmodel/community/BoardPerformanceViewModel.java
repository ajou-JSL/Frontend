package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PerformRepository;

import java.util.List;

public class BoardPerformanceViewModel extends AndroidViewModel {
    private PerformRepository performRepository;
    private final MutableLiveData<Result<List<Performance>>> isLoadPerformancesSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Performance>>> isLoadNextPerformancesSuccess = new MutableLiveData<>();
    private final int LOAD_PERFORM_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 0;
    private Integer recentPageNumber = 0;

    public BoardPerformanceViewModel(Application application){
        super(application);
        performRepository = PerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Performance>>> getIsLoadPerformancesSuccess() {
        return isLoadPerformancesSuccess;
    }

    public MutableLiveData<Result<List<Performance>>> getIsLoadNextPerformancesSuccess() {
        return isLoadNextPerformancesSuccess;
    }

    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void setIsLoadPerformancesSuccess(Result<List<Performance>> isLoadPerformancesSuccess){
        this.isLoadPerformancesSuccess.setValue(isLoadPerformancesSuccess);
    }

    public void setIsLoadNextPerformancesSuccess(Result<List<Performance>> isLoadNextPerformancesSuccess){
        this.isLoadNextPerformancesSuccess.setValue(isLoadNextPerformancesSuccess);
    }

    public void clearPage(){
        page = 0;
    }

    public void loadPerformances(){
        performRepository.loadPerforms(page, LOAD_PERFORM_NUMBER, this::setIsLoadPerformancesSuccess);
        page = page + 1;
    }

    public void loadNextPerformances(){
        if(recentPageNumber < LOAD_PERFORM_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PERFORM_NUMBER보다 적다면, 그만 불러오기
        performRepository.loadPerforms(page, LOAD_PERFORM_NUMBER, this::setIsLoadNextPerformancesSuccess);
        page = page + 1;
    }
}