package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;

import java.util.List;

public class MoumFindPerformanceHallViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<PerformanceHall>>> isQueryPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isLoadPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isLoadNextPerformanceHallSuccess = new MutableLiveData<>();
    private final int LOAD_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 0;
    private Integer recentPageNumber = 0;

    public MoumFindPerformanceHallViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<PerformanceHall>>> getIsLoadNextPerformanceHallSuccess() {
        return isLoadNextPerformanceHallSuccess;
    }

    public MutableLiveData<Result<List<PerformanceHall>>> getIsLoadPerformanceHallSuccess() {
        return isLoadPerformanceHallSuccess;
    }

    public MutableLiveData<Result<List<PerformanceHall>>> getIsQueryPerformanceHallSuccess() {
        return isQueryPerformanceHallSuccess;
    }

    public void setIsQueryPerformanceHallSuccess(Result<List<PerformanceHall>> isQueryPerformanceHallSuccess){
        this.isQueryPerformanceHallSuccess.setValue(isQueryPerformanceHallSuccess);
    }

    public void setIsLoadPerformanceHallSuccess(Result<List<PerformanceHall>> isLoadPerformanceHallSuccess){
        this.isLoadPerformanceHallSuccess.setValue(isLoadPerformanceHallSuccess);
    }

    public void setIsLoadNextPerformanceHallSuccess(Result<List<PerformanceHall>> isLoadNextPerformanceHallSuccess){
        this.isLoadNextPerformanceHallSuccess.setValue(isLoadNextPerformanceHallSuccess);
    }

    public void queryPerformanceHall(String query){

    }

    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void clearPage(){
        page = 0;
    }

    public void loadPerformanceHall(){
        practiceNPerformRepository.getPerformHalls(page, LOAD_NUMBER, this::setIsLoadPerformanceHallSuccess);
        page = page + 1;
    }


    public void loadNextPerformanceHall(){
        if(recentPageNumber < LOAD_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PRACTICE_ROOM_NUMBER 적다면, 그만 불러오기
        practiceNPerformRepository.getPerformHalls(page, LOAD_NUMBER, this::setIsLoadNextPerformanceHallSuccess);
        page = page + 1;
    }
}
