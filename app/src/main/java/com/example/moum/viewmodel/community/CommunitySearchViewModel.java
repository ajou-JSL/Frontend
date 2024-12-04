package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class CommunitySearchViewModel extends AndroidViewModel {
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadSearchArticlesSuccess = new MutableLiveData<>();
    private final List<Article> searchArticles = new ArrayList<>();

    private ArticleRepository articleRepository;
    private boolean isLoading = false;
    private Integer currentPage = 0;
    private Integer recentSize;
    private final Integer currentSize = 10;

    public CommunitySearchViewModel(Application application) {
        super(application);
        articleRepository = articleRepository.getInstance(application);
    }

    private void setIsLoadSearchArticlesSuccess(Result<List<Article>> isLoadSearchArticlesSuccess) {
        isLoading = false;
        this.isLoadSearchArticlesSuccess.setValue(isLoadSearchArticlesSuccess);
    }


    public MutableLiveData<Validation> getValidationStatus(){
        return validationStatus;
    }

    public List<Article> getSearchArticles() { return searchArticles; }

    public MutableLiveData<Result<List<Article>>> getIsLoadSearchArticlesSuccess() { return isLoadSearchArticlesSuccess; }


    public void setIsloading(boolean isLoading){
        this.isLoading = isLoading;
    }

    public boolean isLoading() {return isLoading; }


    public void resetPagination() {
        currentPage = 0;
        isLoading = false;
    }

    public void loadSearchArticles(String keyword, String category,boolean isNext) {
        if (!isNext) {
            currentPage = 0;
            searchArticles.clear();
        } else {
            //TODO 화면 스크롤 페이지 이동
        }
        if (!isLoading) {
            isLoading = true;
            articleRepository.searchArticles(keyword, category, currentPage, currentSize, this::setIsLoadSearchArticlesSuccess);
            if (isLoadSearchArticlesSuccess.getValue() != null) {
                searchArticles.addAll(isLoadSearchArticlesSuccess.getValue().getData());
            }
        }
    }


    public void setRecentSize(Integer recentSize) {
        this.recentSize = recentSize;
    }
}
