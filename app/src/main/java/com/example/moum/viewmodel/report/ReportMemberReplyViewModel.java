package com.example.moum.viewmodel.report;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ReportRepository;
import com.example.moum.utils.Validation;

public class ReportMemberReplyViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private final MutableLiveData<Result<ReportMember>> isLoadReportMemberSuccess = new MutableLiveData<>();

    public ReportMemberReplyViewModel(Application application){
        super(application);
        reportRepository = ReportRepository.getInstance(application);
    }

    public MutableLiveData<Result<ReportMember>> getIsLoadReportMemberSuccess() {
        return isLoadReportMemberSuccess;
    }

    public void setIsLoadReportMemberSuccess(Result<ReportMember> isLoadReportMemberSuccess){
        this.isLoadReportMemberSuccess.setValue(isLoadReportMemberSuccess);
    }

    public void loadReportMember(Integer reportMemberId){
        /*goto repository*/
        reportRepository.loadReportMember(reportMemberId,this::setIsLoadReportMemberSuccess);
    }
}
