package com.example.moum.viewmodel.report;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ReportRepository;

public class ReportTeamReplyViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private final MutableLiveData<Result<ReportTeam>> isLoadReportTeamSuccess = new MutableLiveData<>();

    public ReportTeamReplyViewModel(Application application){
        super(application);
        reportRepository = ReportRepository.getInstance(application);
    }

    public MutableLiveData<Result<ReportTeam>> getIsLoadReportTeamSuccess() {
        return isLoadReportTeamSuccess;
    }

    public void setIsLoadReportTeamSuccess(Result<ReportTeam> isLoadReportTeamSuccess){
        this.isLoadReportTeamSuccess.setValue(isLoadReportTeamSuccess);
    }

    public void loadReportTeam(Integer reportTeamId){
        /*goto repository*/
        reportRepository.loadReportTeam(reportTeamId, this::setIsLoadReportTeamSuccess);
    }
}
