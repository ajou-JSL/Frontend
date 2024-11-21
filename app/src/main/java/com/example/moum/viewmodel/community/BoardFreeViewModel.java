package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Result;
import com.example.moum.utils.Validation;
import com.example.moum.repository.ArticleRepository;

import java.util.List;

public class BoardFreeViewModel extends AndroidViewModel {
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadArticlesCategorySuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Article>> isLoadArticleSuccess = new MutableLiveData<>();
    private ArticleRepository articleRepository;

    public BoardFreeViewModel(Application application) {
        super(application);
        articleRepository = articleRepository.getInstance(application);
    }

    private void setIsLoadArticlesCategorySuccess(Result<List<Article>> isLoadArticlesCategorySuccess) {
        this.isLoadArticlesCategorySuccess.setValue(isLoadArticlesCategorySuccess);
    }

    private void setIsLoadArticleSuccess(Result<Article> isLoadArticleSuccess){
        this.isLoadArticleSuccess.setValue(isLoadArticleSuccess);
    }

    public MutableLiveData<Validation> getValidationStatus(){
        return validationStatus;
    }

    public MutableLiveData<Result<List<Article>>> getIsLoadArticlesCategorySuccess() { return isLoadArticlesCategorySuccess; }

    public void loadArticleCategoryList() {
        articleRepository.loadArticlesCategory(null, "FREE_TALKING_BOARD", 0, 10,this::setIsLoadArticlesCategorySuccess);
    }

    public void loadArticlesDetail(Integer positionId) {
        Result<List<Article>> previousResult = isLoadArticlesCategorySuccess.getValue();
        if (previousResult != null && previousResult.getData() != null && !previousResult.getData().isEmpty()) {
            // 특정 ID 데이터 호출
            Integer articleId = previousResult.getData().get(positionId).getId();
            articleRepository.loadArticleDetail(articleId, this::setIsLoadArticleSuccess);
        } else {
            setIsLoadArticleSuccess(new Result<>(Validation.ARTICLE_NOT_FOUND));
        }
    }
}