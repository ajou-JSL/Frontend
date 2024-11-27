package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Result;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;
import com.example.moum.repository.ArticleRepository;

import java.util.List;

public class BoardFreeViewModel extends AndroidViewModel {
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadArticlesCategorySuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Article>> isLoadArticleSuccess = new MutableLiveData<>();
    private ArticleRepository articleRepository;
 //   private final MutableLiveData<Result<String>> profileURL;
    private boolean isLoading = false;
    private Integer currentPage = 0;
    private final Integer currentSize = 10;

    public BoardFreeViewModel(Application application) {
        super(application);
        articleRepository = articleRepository.getInstance(application);
    }

    private void setIsLoadArticlesCategorySuccess(Result<List<Article>> isLoadArticlesCategorySuccess) {
        isLoading = false;
        this.isLoadArticlesCategorySuccess.setValue(isLoadArticlesCategorySuccess);
    }

    private void setIsLoadArticleSuccess(Result<Article> isLoadArticleSuccess){
        this.isLoadArticleSuccess.setValue(isLoadArticleSuccess);
    }

    public MutableLiveData<Validation> getValidationStatus(){
        return validationStatus;
    }

    public MutableLiveData<Result<List<Article>>> getIsLoadArticlesCategorySuccess() { return isLoadArticlesCategorySuccess; }

    public boolean isLoading() {return isLoading; }

    public void resetPagination() {
        currentPage = 0;
        isLoading = false;
    }

    public void loadArticleCategoryList() {
        if (!isLoading) {
            isLoading = true;
            articleRepository.loadArticlesCategory(null, "FREE_TALKING_BOARD", currentPage, currentSize, this::setIsLoadArticlesCategorySuccess);
            currentPage++;
        }
    }
}