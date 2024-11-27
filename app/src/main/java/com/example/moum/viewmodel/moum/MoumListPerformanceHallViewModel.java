package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.MoumPerformHall;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;

import java.util.List;

public class MoumListPerformanceHallViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<MoumPerformHall>>> isLoadPerformanceHallsOfMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<PerformanceHall>> isLoadPerformanceHallSuccess = new MutableLiveData<>();

    public MoumListPerformanceHallViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<MoumPerformHall>>> getIsLoadPerformanceHallsOfMoumSuccess() {
        return isLoadPerformanceHallsOfMoumSuccess;
    }

    public MutableLiveData<Result<PerformanceHall>> getIsLoadPerformanceHallSuccess() {
        return isLoadPerformanceHallSuccess;
    }

    public void setIsLoadPerformanceHallsOfMoumSuccess(Result<List<MoumPerformHall>> isLoadPerformanceHallsOfMoumSuccess){
        this.isLoadPerformanceHallsOfMoumSuccess.setValue(isLoadPerformanceHallsOfMoumSuccess);
    }

    public void setIsLoadPerformanceHallSuccess(Result<PerformanceHall> isLoadPerformanceHallSuccess){
        this.isLoadPerformanceHallSuccess.setValue(isLoadPerformanceHallSuccess);
    }

    public void loadPerformanceHallsOfMoum(Integer moumId){
        practiceNPerformRepository.getPerformHallsOfMoum(moumId, this::setIsLoadPerformanceHallsOfMoumSuccess);
    }

    public void loadPerformanceHall(Integer hallId){
        practiceNPerformRepository.getPerformHall(hallId, this::setIsLoadPerformanceHallSuccess);
    }
}
