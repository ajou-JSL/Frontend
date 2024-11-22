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
    private final MutableLiveData<Result<Performance>> isLoadPerformOfMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Promote>> isMakeQrSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> downloadSuccess = new MutableLiveData<>();

    public MoumPromoteViewModel(Application application){
        super(application);
        performRepository = PerformRepository.getInstance(application);
        promoteRepository = PromoteRepository.getInstance(application);
    }

    public MutableLiveData<Result<Performance>> getIsLoadPerformOfMoumSuccess() {
        return isLoadPerformOfMoumSuccess;
    }

    public MutableLiveData<Result<Promote>> getIsMakeQrSuccess() {
        return isMakeQrSuccess;
    }

    public MutableLiveData<Validation> getDownloadSuccess() {
        return downloadSuccess;
    }

    private void setIsLoadPerformOfMoumSuccess(Result<Performance> isLoadPerformOfMoumSuccess){
        this.isLoadPerformOfMoumSuccess.setValue(isLoadPerformOfMoumSuccess);
    }

    private void setIsMakeQrSuccess(Result<Promote> isMakeQrSuccess){
        this.isMakeQrSuccess.setValue(isMakeQrSuccess);
    }

    private void setDownloadSuccess(Validation downloadSuccess){
        this.downloadSuccess.setValue(downloadSuccess);
    }

    public void loadPerformOfMoum(Integer moumId){
        performRepository.loadPerformOfMoum(moumId, this::setIsLoadPerformOfMoumSuccess);
    }

    public void makeQr(Integer performId){
        /*valid check*/
        if(isLoadPerformOfMoumSuccess.getValue() == null || isLoadPerformOfMoumSuccess.getValue().getValidation() != Validation.PERFORMANCE_GET_SUCCESS){
            Result<Promote> result = new Result<Promote>(Validation.PERFORMANCE_NOT_FOUNT);
            setIsMakeQrSuccess(result);
            return;
        }

        /*goto repository*/
        //promoteRepository.makeQr(performId, this::setIsMakeQrSuccess);
    }

    public void downloadQr(Context context, Promote promote){
        /*valid check*/
//        if(isLoadPerformOfMoumSuccess.getValue() == null || isLoadPerformOfMoumSuccess.getValue().getValidation() != Validation.PERFORMANCE_GET_SUCCESS){
//            setDownloadSuccess(Validation.PERFORMANCE_NOT_FOUNT);
//            return;
//        }
//        if(isMakeQrSuccess.getValue() == null || isMakeQrSuccess.getValue().getValidation() != Validation.MAKE_QR_SUCCESS){
//            setDownloadSuccess(Validation.MAKE_QR_FALIL);
//            return;
//        }

        /*download qr image into member's gallery*/
        String imageUrl = "https://www.wikihow.com/images_en/thumb/d/db/Get-the-URL-for-Pictures-Step-2-Version-6.jpg/v4-460px-Get-the-URL-for-Pictures-Step-2-Version-6.jpg";
        ImageManager.downloadImageToGallery(context, imageUrl, "moum_qr_code");
    }
}
