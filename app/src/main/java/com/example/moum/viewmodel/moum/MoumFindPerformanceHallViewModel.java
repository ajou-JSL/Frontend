package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;
import com.naver.maps.geometry.LatLng;

import java.util.List;

public class MoumFindPerformanceHallViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<PerformanceHall>>> isQueryPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isQueryNextPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isLoadPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isLoadNextPerformanceHallSuccess = new MutableLiveData<>();
    private final int LOAD_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 1;
    private Integer recentPageNumber = 0;
    private boolean isQuerying = false;
    private String query;
    private LatLng latLng;

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

    public MutableLiveData<Result<List<PerformanceHall>>> getIsQueryNextPerformanceHallSuccess() {
        return isQueryNextPerformanceHallSuccess;
    }

    public boolean isQuerying() {
        return isQuerying;
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

    public void setIsQueryNextPerformanceHallSuccess(Result<List<PerformanceHall>> isQueryNextPerformanceHallSuccess){
        this.isQueryNextPerformanceHallSuccess.setValue(isQueryNextPerformanceHallSuccess);
    }

    public void queryPerformanceHall(String query, LatLng latLng){
        clearPage();
        if(query == null || latLng == null) return;
        this.query = query;
        this.latLng = latLng;
        isQuerying = true;

        /*goto repository*/
        practiceNPerformRepository.searchPerformHalls(page, LOAD_NUMBER, query, latLng, this::setIsQueryPerformanceHallSuccess);
        page = page + 1;
    }

    public void queryNextPerformanceHall(){
        if(query == null || latLng == null) return;
        isQuerying = true;
        if(recentPageNumber < LOAD_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PRACTICE_ROOM_NUMBER 적다면, 그만 불러오기

        /*goto repository*/
        practiceNPerformRepository.searchPerformHalls(page, LOAD_NUMBER, query, latLng, this::setIsQueryNextPerformanceHallSuccess);
        page = page + 1;
    }

    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void clearPage(){
        page = 1;
        recentPageNumber = 0;
        query = null;
        latLng = null;
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
