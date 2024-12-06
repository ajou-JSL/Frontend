package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;

public class MoumManageViewModel extends AndroidViewModel {
    private final MoumRepository moumRepository;
    private final MutableLiveData<Result<Moum>> isLoadMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isFinishMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isReopenMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isUpdateMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isDeleteMoumSuccess = new MutableLiveData<>();

    public MoumManageViewModel(Application application) {
        super(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public MutableLiveData<Result<Moum>> getIsLoadMoumSuccess() {
        return isLoadMoumSuccess;
    }

    public MutableLiveData<Result<Moum>> getIsFinishMoumSuccess() {
        return isFinishMoumSuccess;
    }

    public MutableLiveData<Result<Moum>> getIsReopenMoumSuccess() {
        return isReopenMoumSuccess;
    }

    public MutableLiveData<Result<Moum>> getIsUpdateMoumSuccess() {
        return isUpdateMoumSuccess;
    }

    public MutableLiveData<Result<Moum>> getIsDeleteMoumSuccess() {
        return isDeleteMoumSuccess;
    }

    public void setIsLoadMoumSuccess(Result<Moum> isLoadMoumSuccess) {
        this.isLoadMoumSuccess.setValue(isLoadMoumSuccess);
    }

    public void setIsFinishMoumSuccess(Result<Moum> isFinishMoumSuccess) {
        this.isFinishMoumSuccess.setValue(isFinishMoumSuccess);
    }

    public void setIsReopenMoumSuccess(Result<Moum> isReopenMoumSuccess) {
        this.isReopenMoumSuccess.setValue(isReopenMoumSuccess);
    }

    public void setIsUpdateMoumSuccess(Result<Moum> isUpdateMoumSuccess) {
        this.isUpdateMoumSuccess.setValue(isUpdateMoumSuccess);
    }

    public void setIsDeleteMoumSuccess(Result<Moum> isDeleteMoumSuccess) {
        this.isDeleteMoumSuccess.setValue(isDeleteMoumSuccess);
    }

    public void loadMoum(Integer moumId) {
        moumRepository.loadMoum(moumId, this::setIsLoadMoumSuccess);
    }

    public void finishMoum(Integer moumId) {
        moumRepository.finishMoum(moumId, this::setIsFinishMoumSuccess);
    }

    public void reopenMoum(Integer moumId) {
        moumRepository.reopenMoum(moumId, this::setIsReopenMoumSuccess);
    }

    public void updateProcessMoum(Integer moumId, Moum.Process process) {
        moumRepository.updateProcessMoum(moumId, process, this::setIsUpdateMoumSuccess);
    }

    public void deleteMoum(Integer moumId) {
        moumRepository.deleteMoum(moumId, this::setIsDeleteMoumSuccess);
    }
}
