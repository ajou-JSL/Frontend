package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.MoumPracticeroom;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;
import com.example.moum.utils.Validation;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MoumListPracticeroomViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<MoumPracticeroom>>> isLoadPracticeroomsOfMoumSuccess = new MutableLiveData<>();
    private final PublishSubject<Result<Practiceroom>> isLoadPracticeroomSuccess = PublishSubject.create();

    public MoumListPracticeroomViewModel(Application application) {
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<MoumPracticeroom>>> getIsLoadPracticeroomsOfMoumSuccess() {
        return isLoadPracticeroomsOfMoumSuccess;
    }

    public Observable<Result<Practiceroom>> getIsLoadPracticeroomSuccess() {
        return isLoadPracticeroomSuccess;
    }

    public void setIsLoadPracticeroomsOfMoumSuccess(Result<List<MoumPracticeroom>> isLoadPracticeroomsOfMoumSuccess) {
        this.isLoadPracticeroomsOfMoumSuccess.setValue(isLoadPracticeroomsOfMoumSuccess);
    }

    public void setIsLoadPracticeroomSuccess(Result<Practiceroom> isLoadPracticeroomSuccess) {
        this.isLoadPracticeroomSuccess.onNext(isLoadPracticeroomSuccess);
    }

    public void loadPracticeroomsOfMoum(Integer moumId) {
        practiceNPerformRepository.getPracticeroomsOfMoum(moumId, this::setIsLoadPracticeroomsOfMoumSuccess);
    }

    public void loadPracticeroom(Integer roomId) {
        /*valid check*/
        if (isLoadPracticeroomsOfMoumSuccess.getValue() == null
                || isLoadPracticeroomsOfMoumSuccess.getValue().getValidation() != Validation.MOUM_PRACTICE_ROOM_GET_SUCCESS) {
            Result<Practiceroom> result = new Result<>(Validation.MOUM_PRACTICE_ROOM_NOT_FOUND);
            setIsLoadPracticeroomSuccess(result);
            return;
        }

        /*goto repository*/
        practiceNPerformRepository.getPracticeroom(roomId, this::setIsLoadPracticeroomSuccess);
    }
}
