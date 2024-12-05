package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.dto.ArticleFilterRequest;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.utils.Validation;

import java.util.List;

public class BoardRecruitViewModel extends AndroidViewModel {
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadArticlesCategorySuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadNextArticlesCategorySuccess = new MutableLiveData<>();
    private ArticleRepository articleRepository;
    private boolean isLoading = false;
    private Integer currentPage = 0;
    private Integer recentSize = 0;
    private final Integer currentSize = 10;

    public BoardRecruitViewModel(Application application) {
        super(application);
        articleRepository = articleRepository.getInstance(application);
    }

    private void setIsLoadArticlesCategorySuccess(Result<List<Article>> isLoadArticlesCategorySuccess) {
        isLoading = false;
        this.isLoadArticlesCategorySuccess.setValue(isLoadArticlesCategorySuccess);
    }

    private void setIsLoadNextArticlesCategorySuccess(Result<List<Article>> isLoadNextArticlesCategorySuccess){
        this.isLoadNextArticlesCategorySuccess.setValue(isLoadNextArticlesCategorySuccess);
    }

    public MutableLiveData<Validation> getValidationStatus(){
        return validationStatus;
    }

    public MutableLiveData<Result<List<Article>>> getIsLoadArticlesCategorySuccess() { return isLoadArticlesCategorySuccess; }


    public MutableLiveData<Result<List<Article>>> getIsLoadNextArticlesCategorySuccess() {
        return isLoadNextArticlesCategorySuccess;
    }

    public boolean isLoading() {return isLoading; }

    public void resetPagination() {
        currentPage = 0;
        isLoading = false;
    }

    public void loadArticleCategoryList() {
        if (!isLoading) {
            isLoading = true;
            articleRepository.loadArticlesCategory(null, "RECRUIT_BOARD", currentPage, currentSize, this::setIsLoadArticlesCategorySuccess);
            currentPage++;
        }
    }

    public void loadArticlesByFilter(boolean likesCount, boolean ViewCount, boolean CommentCount, boolean createdAt, Integer genre) {
        ArticleFilterRequest articleFilterRequest = new ArticleFilterRequest(
                null, likesCount, ViewCount, CommentCount, createdAt, null, 0, genre);
        articleRepository.loadArticlesByFilter(articleFilterRequest, currentPage, currentSize,this::setIsLoadArticlesCategorySuccess);
    }

    public void loadNextArticleCategoryList() {
        //TODO 페이지 조절 필요 데이터가 더이상 없을 때
        articleRepository.loadArticlesCategory(null, "RECRUIT_BOARD", currentPage, currentSize, this::setIsLoadNextArticlesCategorySuccess);
        currentPage++;
    }
    public void setRecentSize(Integer recentSize) {
        this.recentSize = recentSize;
    }
}