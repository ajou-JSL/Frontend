package com.example.moum.viewmodel.report;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ReportRepository;
import com.example.moum.utils.Validation;

public class ReportTeamViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private final MutableLiveData<Result<ReportTeam>> isReportTeamSuccess = new MutableLiveData<>();
    private String type;

    public ReportTeamViewModel(Application application){
        super(application);
        reportRepository = ReportRepository.getInstance(application);
    }

    public MutableLiveData<Result<ReportTeam>> getIsReportTeamSuccess() {
        return isReportTeamSuccess;
    }

    public void setIsReportTeamSuccess(Result<ReportTeam> isReportTeamSuccess){
        this.isReportTeamSuccess.setValue(isReportTeamSuccess);
    }

    public void setType(String type){
        this.type = type;
    }

    public void reportTeam(Integer targetTeamId, Integer myId, String details){
        /*null check*/
        if(type == null){
            Result<ReportTeam> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsReportTeamSuccess(result);
            return;
        }

        /*goto repository*/
        reportRepository.reportTeam(targetTeamId, myId, type, details, this::setIsReportTeamSuccess);
    }
}
