package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.R;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;
import com.example.moum.view.moum.MoumFindPracticeroomActivity;

import java.util.List;

public class MoumFindPracticeroomViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<Practiceroom>>> isQueryPracticeroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Practiceroom>>> isLoadPracticeroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Practiceroom>>> isLoadNextPracticeroomSuccess = new MutableLiveData<>();
    private final int LOAD_PRACTICE_ROOM_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 0;
    private Integer recentPageNumber = 0;

    public MoumFindPracticeroomViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsQueryPracticeroomSuccess() {
        return isQueryPracticeroomSuccess;
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsLoadPracticeroomSuccess() {
        return isLoadPracticeroomSuccess;
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsLoadNextPracticeroomSuccess() {
        return isLoadNextPracticeroomSuccess;
    }

    public void setIsQueryPracticeroomSuccess(Result<List<Practiceroom>> isQueryPracticeroomSuccess){
        this.isQueryPracticeroomSuccess.setValue(isQueryPracticeroomSuccess);
    }

    public void setIsLoadPracticeroomSuccess(Result<List<Practiceroom>> isLoadPracticeroomSuccess){
        this.isLoadPracticeroomSuccess.setValue(isLoadPracticeroomSuccess);
    }

    public void setIsLoadNextPracticeroomSuccess(Result<List<Practiceroom>> isLoadNextPracticeroomSuccess){
        this.isLoadNextPracticeroomSuccess.setValue(isLoadNextPracticeroomSuccess);
    }

    public void queryPracticeroom(String query){

    }

    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void clearPage(){
        page = 0;
    }

    public void loadPracticeroom(){
        practiceNPerformRepository.getPracticerooms(page, LOAD_PRACTICE_ROOM_NUMBER, this::setIsLoadPracticeroomSuccess);
        page = page + 1;
    }


    public void loadNextPracticeroom(){
        if(recentPageNumber < LOAD_PRACTICE_ROOM_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PRACTICE_ROOM_NUMBER 적다면, 그만 불러오기
        practiceNPerformRepository.getPracticerooms(page, LOAD_PRACTICE_ROOM_NUMBER, this::setIsLoadNextPracticeroomSuccess);
        page = page + 1;
    }
}
