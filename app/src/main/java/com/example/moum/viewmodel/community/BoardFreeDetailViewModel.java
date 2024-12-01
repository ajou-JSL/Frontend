package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.dto.ErrorDetail;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class BoardFreeDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Article> articleitem = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Comment>> comments = new MutableLiveData<>();
    private ArticleRepository articleRepository;


    public BoardFreeDetailViewModel(Application application) {
        super(application);
        articleRepository = articleRepository.getInstance(application);

    }

    private void setIsLoadArticleSuccess(Result<Article> result) {
        if (result != null && result.getData() != null) {
            Article article = result.getData();

            articleitem.setValue(article);
            if (article.getComments() != null) {
                // 댓글 리스트
                comments.setValue(article.getComments());
            } else {
                //빈 댓글
                comments.setValue(new ArrayList<>());
            }

            // Validation 상태 업데이트
            validationStatus.setValue(result.getValidation());
        } else {
            validationStatus.setValue(Validation.ARTICLE_GET_FAILED); // 에러 상태 처리
        }
    }

    public MutableLiveData<Validation> getValidationStatus() {
        return validationStatus;
    }

    public MutableLiveData<Article> getArticleLiveData() {
        return articleitem;
    }

    public MutableLiveData<ArrayList<Comment>> getCommentLiveData() {
        return comments;
    }

    // 게시글과 댓글 데이터를 로드하는 메서드
    public void loadArticlesDetail(Integer positionId) {
        articleRepository.loadArticleDetail(positionId, this::setIsLoadArticleSuccess);
    }
}
