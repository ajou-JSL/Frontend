package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.utils.Validation;

import java.util.List;

public class CommunitySearchViewModel extends AndroidViewModel {
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadSearchArticlesSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadNextArticlesCategorySuccess = new MutableLiveData<>();

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

    private void setIsLoadNextArticlesCategorySuccess(Result<List<Article>> isLoadNextArticlesCategorySuccess){
        this.isLoadNextArticlesCategorySuccess.setValue(isLoadNextArticlesCategorySuccess);
    }

    public MutableLiveData<Validation> getValidationStatus(){
        return validationStatus;
    }

    public MutableLiveData<Result<List<Article>>> getIsLoadSearchArticlesSuccess() { return isLoadSearchArticlesSuccess; }


    public MutableLiveData<Result<List<Article>>> getIsLoadNextArticlesCategorySuccess() {
        return isLoadNextArticlesCategorySuccess;
    }

    public boolean isLoading() {return isLoading; }

    public void resetPagination() {
        currentPage = 0;
        isLoading = false;
    }

    public void loadSearchArticles(String keyword) {
        if (!isLoading) {
            isLoading = true;
            articleRepository.searchAllArticles(keyword, currentPage, currentSize, this::setIsLoadSearchArticlesSuccess);
            currentPage++;
        }
    }

    public void setRecentSize(Integer recentSize) {
        this.recentSize = recentSize;
    }
}
