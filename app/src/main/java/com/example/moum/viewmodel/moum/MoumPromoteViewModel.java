package com.example.moum.viewmodel.moum;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Promote;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PerformRepository;
import com.example.moum.repository.PromoteRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

public class MoumPromoteViewModel extends AndroidViewModel {
    private PerformRepository performRepository;
    private PromoteRepository promoteRepository;
    private final MutableLiveData<Result<String>> isLoadQrSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Performance>> isLoadPerformOfMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> isMakeQrSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> downloadSuccess = new MutableLiveData<>();

    public MoumPromoteViewModel(Application application) {
        super(application);
        performRepository = PerformRepository.getInstance(application);
        promoteRepository = PromoteRepository.getInstance(application);
    }

    public MutableLiveData<Result<String>> getIsLoadQrSuccess() {
        return isLoadQrSuccess;
    }

    public MutableLiveData<Result<Performance>> getIsLoadPerformOfMoumSuccess() {
        return isLoadPerformOfMoumSuccess;
    }

    public MutableLiveData<Result<String>> getIsMakeQrSuccess() {
        return isMakeQrSuccess;
    }

    public MutableLiveData<Validation> getDownloadSuccess() {
        return downloadSuccess;
    }

    public void setIsLoadQrSuccess(Result<String> isLoadQrSuccess) {
        this.isLoadQrSuccess.setValue(isLoadQrSuccess);
    }

    private void setIsLoadPerformOfMoumSuccess(Result<Performance> isLoadPerformOfMoumSuccess) {
        this.isLoadPerformOfMoumSuccess.setValue(isLoadPerformOfMoumSuccess);
    }

    private void setIsMakeQrSuccess(Result<String> isMakeQrSuccess) {
        this.isMakeQrSuccess.setValue(isMakeQrSuccess);
    }

    private void setDownloadSuccess(Validation downloadSuccess) {
        this.downloadSuccess.setValue(downloadSuccess);
    }

    public void loadPerformOfMoum(Integer moumId) {
        performRepository.loadPerformOfMoum(moumId, this::setIsLoadPerformOfMoumSuccess);
    }

    public void loadQr(Integer performId) {
        /*valid check*/
        if (isLoadPerformOfMoumSuccess.getValue() == null
                || isLoadPerformOfMoumSuccess.getValue().getValidation() != Validation.PERFORMANCE_GET_SUCCESS) {
            Result<String> result = new Result<>(Validation.PERFORMANCE_NOT_FOUNT);
            setIsLoadQrSuccess(result);
            return;
        }

        promoteRepository.loadQr(performId, this::setIsLoadQrSuccess);
    }

    public void makeQr(Integer performId) {
        /*valid check*/
        if (isLoadPerformOfMoumSuccess.getValue() == null
                || isLoadPerformOfMoumSuccess.getValue().getValidation() != Validation.PERFORMANCE_GET_SUCCESS) {
            Result<String> result = new Result<>(Validation.PERFORMANCE_NOT_FOUNT);
            setIsMakeQrSuccess(result);
            return;
        }

        /*goto repository*/
        promoteRepository.createQr(performId, this::setIsMakeQrSuccess);
    }

    public void downloadQr(Context context, String qrUrl) {
        /*valid check*/
        if (isLoadPerformOfMoumSuccess.getValue() == null
                || isLoadPerformOfMoumSuccess.getValue().getValidation() != Validation.PERFORMANCE_GET_SUCCESS) {
            setDownloadSuccess(Validation.PERFORMANCE_NOT_FOUNT);
            return;
        }
        if ((isMakeQrSuccess.getValue() != null && isMakeQrSuccess.getValue().getValidation() == Validation.QR_SUCCESS)
                || isLoadQrSuccess.getValue() != null && isLoadQrSuccess.getValue().getValidation() == Validation.QR_SUCCESS) {
            /*download qr image into member's gallery*/
            ImageManager.downloadImageToGallery(context, qrUrl, "moum_qr_code.png");
            return;
        }

        /*if other, fail*/
        setDownloadSuccess(Validation.QR_FAIL);

    }
}
