package com.example.moum.viewmodel.moum;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.MoumPerformHall;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MoumListPerformanceHallViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<MoumPerformHall>>> isLoadPerformanceHallsOfMoumSuccess = new MutableLiveData<>();
    private final PublishSubject<Result<PerformanceHall>> isLoadPerformanceHallSuccess = PublishSubject.create();

    public MoumListPerformanceHallViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<MoumPerformHall>>> getIsLoadPerformanceHallsOfMoumSuccess() {
        return isLoadPerformanceHallsOfMoumSuccess;
    }

    public Observable<Result<PerformanceHall>> getIsLoadPerformanceHallSuccess() {
        return isLoadPerformanceHallSuccess;
    }

    public void setIsLoadPerformanceHallsOfMoumSuccess(Result<List<MoumPerformHall>> isLoadPerformanceHallsOfMoumSuccess){
        this.isLoadPerformanceHallsOfMoumSuccess.setValue(isLoadPerformanceHallsOfMoumSuccess);
    }

    public void setIsLoadPerformanceHallSuccess(Result<PerformanceHall> isLoadPerformanceHallSuccess){
        Log.e("gg", isLoadPerformanceHallSuccess.getData().getName());
        this.isLoadPerformanceHallSuccess.onNext(isLoadPerformanceHallSuccess);
    }

    public void loadPerformanceHallsOfMoum(Integer moumId){
        practiceNPerformRepository.getPerformHallsOfMoum(moumId, this::setIsLoadPerformanceHallsOfMoumSuccess);
    }

    public void loadPerformanceHall(Integer hallId){
        practiceNPerformRepository.getPerformHall(hallId, this::setIsLoadPerformanceHallSuccess);
    }
}
