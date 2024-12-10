package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Settlement;
import com.example.moum.repository.PaymentRepository;
import com.example.moum.utils.Validation;

import java.util.List;

public class MoumPaymentAddViewModel extends AndroidViewModel {
    private PaymentRepository paymentRepository;
    private final MutableLiveData<Settlement> settlement = new MutableLiveData<>(new Settlement());
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Settlement>> isCreateSettlementsSuccess = new MutableLiveData<>();

    public MoumPaymentAddViewModel(Application application) {
        super(application);
        paymentRepository = PaymentRepository.getInstance(application);
    }

    public MutableLiveData<Settlement> getSettlement() {
        return settlement;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Settlement>> getIsCreateSettlementsSuccess() {
        return isCreateSettlementsSuccess;
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess) {
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsCreateSettlementsSuccess(Result<Settlement> isCreateSettlementsSuccess) {
        this.isCreateSettlementsSuccess.setValue(isCreateSettlementsSuccess);
    }

    public void validCheck() {
        /*null check*/
        if (settlement.getValue() == null || settlement.getValue().getSettlementName() == null
                || settlement.getValue().getSettlementName().isEmpty()) {
            isValidCheckSuccess.setValue(Validation.SETTLEMENT_NAME_NOT_WRITTEN);
            return;
        } else if (settlement.getValue().getFee() == null || settlement.getValue().getFee() <= 0) {
            isValidCheckSuccess.setValue(Validation.SETTLEMENT_FEE_NOT_WRITTEN);
            return;
        }
        isValidCheckSuccess.setValue(Validation.VALID_ALL);
    }

    public void createSettlement(Integer moumId) {
        /*valid check*/
        if (isValidCheckSuccess.getValue() == null || isValidCheckSuccess.getValue() != Validation.VALID_ALL || settlement.getValue() == null) {
            Result<Settlement> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsCreateSettlementsSuccess(result);
            return;
        }

        /*goto repository*/
        paymentRepository.createSettlement(moumId, settlement.getValue(), this::setIsCreateSettlementsSuccess);
    }
}
