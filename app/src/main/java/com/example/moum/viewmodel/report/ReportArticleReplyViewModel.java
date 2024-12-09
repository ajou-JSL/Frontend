package com.example.moum.viewmodel.report;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ReportRepository;

public class ReportArticleReplyViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private final MutableLiveData<Result<ReportArticle>> isLoadReportArticleSuccess = new MutableLiveData<>();

    public ReportArticleReplyViewModel(Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application);
    }

    public MutableLiveData<Result<ReportArticle>> getIsLoadReportArticleSuccess() {
        return isLoadReportArticleSuccess;
    }

    public void setIsLoadReportArticleSuccess(Result<ReportArticle> isLoadReportArticleSuccess) {
        this.isLoadReportArticleSuccess.setValue(isLoadReportArticleSuccess);
    }

    public void loadReportArticle(Integer reportArticleId) {
        /*goto repository*/
        reportRepository.loadReportArticle(reportArticleId, this::setIsLoadReportArticleSuccess);
    }
}
