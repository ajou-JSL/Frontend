package com.example.moum.viewmodel.report;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ReportRepository;
import com.example.moum.utils.Validation;

public class ReportArticleViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private final MutableLiveData<Result<ReportArticle>> isReportArticleSuccess = new MutableLiveData<>();
    private String type;

    public ReportArticleViewModel(Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application);
    }

    public MutableLiveData<Result<ReportArticle>> getIsReportArticleSuccess() {
        return isReportArticleSuccess;
    }

    public void setIsReportArticleSuccess(Result<ReportArticle> isReportArticleSuccess) {
        this.isReportArticleSuccess.setValue(isReportArticleSuccess);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void reportArticle(Integer targetArticleId, Integer myId, String details) {
        /*null check*/
        if (type == null) {
            Result<ReportArticle> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsReportArticleSuccess(result);
            return;
        }

        /*goto repository*/
        reportRepository.reportArticle(targetArticleId, myId, type, details, this::setIsReportArticleSuccess);
    }
}
