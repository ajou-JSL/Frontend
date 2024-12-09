package com.example.moum.viewmodel.report;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ReportRepository;
import com.example.moum.utils.Validation;

public class ReportMemberViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private final MutableLiveData<Result<ReportMember>> isReportMemberSuccess = new MutableLiveData<>();
    private String type;

    public ReportMemberViewModel(Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application);
    }

    public MutableLiveData<Result<ReportMember>> getIsReportMemberSuccess() {
        return isReportMemberSuccess;
    }

    public void setIsReportMemberSuccess(Result<ReportMember> isReportMemberSuccess) {
        this.isReportMemberSuccess.setValue(isReportMemberSuccess);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void reportMember(Integer targetMemberId, Integer myId, String details) {
        /*null check*/
        if (type == null) {
            Result<ReportMember> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsReportMemberSuccess(result);
            return;
        }

        /*goto repository*/
        reportRepository.reportMember(targetMemberId, myId, type, details, this::setIsReportMemberSuccess);
    }
}
