package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.R;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Settlement;
import com.example.moum.repository.PaymentRepository;

import java.util.List;

public class MoumPaymentViewModel extends AndroidViewModel {
    private PaymentRepository paymentRepository;
    private final MutableLiveData<Result<List<Settlement>>> isLoadSettlementsSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Settlement>> isDeleteSettlementSuccess = new MutableLiveData<>();

    public MoumPaymentViewModel(Application application) {
        super(application);
        paymentRepository = PaymentRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Settlement>>> getIsLoadSettlementsSuccess() {
        return isLoadSettlementsSuccess;
    }

    public MutableLiveData<Result<Settlement>> getIsDeleteSettlementSuccess() {
        return isDeleteSettlementSuccess;
    }

    public void setIsLoadSettlementsSuccess(Result<List<Settlement>> isLoadSettlementsSuccess) {
        this.isLoadSettlementsSuccess.setValue(isLoadSettlementsSuccess);
    }

    public void setIsDeleteSettlementSuccess(Result<Settlement> isDeleteSettlementSuccess) {
        this.isDeleteSettlementSuccess.setValue(isDeleteSettlementSuccess);
    }

    public void loadSettlement(Integer moumId) {
        paymentRepository.loadSettlements(moumId, this::setIsLoadSettlementsSuccess);
    }

    public void deleteSettlement(Integer moumId, Integer settlementId) {
        paymentRepository.deleteSettlement(moumId, settlementId, this::setIsDeleteSettlementSuccess);
    }

}
