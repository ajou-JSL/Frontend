package com.example.moum.viewmodel.myinfo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ReportRepository;

import java.util.List;

public class MyInfoReportNQuestionListViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private final MutableLiveData<Result<List<ReportMember>>> isLoadReportMembersSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<ReportTeam>>> isLoadReportTeamsSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<ReportArticle>>> isLoadReportArticlesSuccess = new MutableLiveData<>();
    private final int LOAD_PAGE_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 0;
    private Integer recentPageNumber = 0;

    public MyInfoReportNQuestionListViewModel(Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<ReportArticle>>> getIsLoadReportArticlesSuccess() {
        return isLoadReportArticlesSuccess;
    }

    public MutableLiveData<Result<List<ReportMember>>> getIsLoadReportMembersSuccess() {
        return isLoadReportMembersSuccess;
    }

    public MutableLiveData<Result<List<ReportTeam>>> getIsLoadReportTeamsSuccess() {
        return isLoadReportTeamsSuccess;
    }

    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void setIsLoadReportMembersSuccess(Result<List<ReportMember>> isLoadReportMembersSuccess) {
        this.isLoadReportMembersSuccess.setValue(isLoadReportMembersSuccess);
    }

    public void setIsLoadReportTeamsSuccess(Result<List<ReportTeam>> isLoadReportTeamsSuccess) {
        this.isLoadReportTeamsSuccess.setValue(isLoadReportTeamsSuccess);
    }

    public void setIsLoadReportArticlesSuccess(Result<List<ReportArticle>> isLoadReportArticlesSuccess) {
        this.isLoadReportArticlesSuccess.setValue(isLoadReportArticlesSuccess);
    }

    public void clearPage() {
        page = 0;
    }

    public void loadReports(String reportType, Integer reporterId) {
        if (reportType.equals("member")) {
            reportRepository.loadReportMembers(reporterId, page, LOAD_PAGE_NUMBER, this::setIsLoadReportMembersSuccess);
        } else if (reportType.equals("team")) {
            reportRepository.loadReportTeams(reporterId, page, LOAD_PAGE_NUMBER, this::setIsLoadReportTeamsSuccess);
        } else {
            reportRepository.loadReportArticles(reporterId, page, LOAD_PAGE_NUMBER, this::setIsLoadReportArticlesSuccess);
        }
        page = page + 1;
    }

    public void loadNextReports(String reportType, Integer reporterId) {
        if (recentPageNumber < LOAD_PAGE_NUMBER) return; //이전에 불러온 신고들이 LOAD_PAGE_NUMBER 적다면, 그만 불러오기
        loadReports(reportType, reporterId);
    }
}
