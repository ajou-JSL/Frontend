package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.dto.ArticleFilterRequest;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class CommunitySearchViewModel extends AndroidViewModel {
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadArticlesCategorySuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Article>>> isLoadNextArticlesCategorySuccess = new MutableLiveData<>();
    private ArticleRepository articleRepository;
    private final MutableLiveData<Result<String>> profileURL = new MutableLiveData<>();
    private boolean isLoading = false;
    private Integer currentPage = 0;
    private boolean pageHolder = false;
    private final Integer currentSize = 10;
    private final List<String> genreName = new ArrayList<>();

    public CommunitySearchViewModel(Application application) {
        super(application);
        articleRepository = articleRepository.getInstance(application);
        for (Genre genre : Genre.values()) {
            genreName.add(genre.name());
        }
    }

    public List<String> getGenreName(){
        return genreName;
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
        pageHolder = false;
    }

    public void loadArticleList(String keyword, Integer spinner1, Integer spinner2, Integer spinner3) {
        if (!isLoading) {
            isLoading = true;
            boolean createdAt = false, ViewCount = false, CommentCount = false, likesCount = false;
            String genre, category = null;
            switch(spinner1){
                case 0: createdAt = true; break;
                case 1: ViewCount = true; break;
                case 2: CommentCount = true; break;
                case 3: likesCount = true; break;
                default: break;
            }
            if(spinner2 == 0){
                genre = null;
            } else {
                genre = genreName.get(spinner2-1);
            }

            /* 스피너 3 값 초기화 */
            switch (spinner3){
                case 1: category = "FREE_TALKING_BOARD"; break;
                case 2: category = "RECRUIT_BOARD"; break;
                default: break;
            }

            /* api 호출 */
            ArticleFilterRequest articleFilterRequest = new ArticleFilterRequest(
                    keyword, likesCount, ViewCount, CommentCount, createdAt, null, category, genre);
            articleRepository.loadArticlesByFilter(articleFilterRequest, currentPage, currentSize,this::setIsLoadArticlesCategorySuccess);
        }
    }

    public void loadNextArticleList(String keyword, Integer spinner1, Integer spinner2, Integer spinner3) {
        if(pageHolder) {
            return;
        }
        currentPage++;
        /* 스피너 1 값 초기화 */
        boolean createdAt = false, ViewCount = false, CommentCount = false, likesCount = false;
        String genre, category = null;
        switch(spinner1){
            case 0: createdAt = true; break;
            case 1: ViewCount = true; break;
            case 2: CommentCount = true; break;
            case 3: likesCount = true; break;
            default: break;
        }

        /* 스피너 2 값 초기화 */
        if(spinner2 == 0){
            genre = null;
        } else {
            genre = genreName.get(spinner2-1);
        }

        /* 스피너 3 값 초기화 */
        switch (spinner3){
            case 0: category = null; break;
            case 1: category = "FREE_TALKING_BOARD"; break;
            case 2: category = "RECRUIT_BOARD"; break;
            default: break;
        }

        /* api 호출 */
        ArticleFilterRequest articleFilterRequest = new ArticleFilterRequest(
                keyword, likesCount, ViewCount, CommentCount, createdAt, null, category, genre);
        articleRepository.loadArticlesByFilter(articleFilterRequest, currentPage, currentSize,this::setIsLoadNextArticlesCategorySuccess);
    }
    public void setPageHolder() {
        this.pageHolder = true;
    }
}