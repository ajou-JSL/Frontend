package com.example.moum.viewmodel.community;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.utils.Validation;

import java.util.ArrayList;

public class BoardRecruitDetailViewModel extends AndroidViewModel {
    private final MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Article> isLoadArticeSuccess = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Comment>> currentComments = new MutableLiveData<>();
    private Comment isCommentSuccess = new Comment();
    private ArticleRepository articleRepository;


    public BoardRecruitDetailViewModel(Application application) {
        super(application);
        articleRepository = articleRepository.getInstance(application);

    }

    private void setIsLoadArticleSuccess(Result<Article> result) {
        if (result != null && result.getData() != null) {
            Article article = result.getData();

            isLoadArticeSuccess.setValue(article);
            if (article.getComments() != null) {
                // 댓글 리스트
                currentComments.setValue(article.getComments());
            } else {
                //빈 댓글
                currentComments.setValue(new ArrayList<>());
            }

            // Validation 상태 업데이트
            validationStatus.setValue(result.getValidation());
        } else {
            validationStatus.setValue(Validation.ARTICLE_GET_FAILED); // 에러 상태 처리
        }
    }

    private void setIsCommentSuccess(Result<Comment> result) {
        if (result != null && result.getData() != null) {
            // 댓글 생성 성공 시, isCommentSuccess에 댓글 객체를 저장
            isCommentSuccess = result.getData();
        } else {
            // 실패 시 처리
            Log.e( "abc" , "댓글 추가 실패");
        }
    }

    public MutableLiveData<Validation> getValidationStatus() {
        return validationStatus;
    }

    public MutableLiveData<Article> getIsLoadArticeSuccess() {
        return isLoadArticeSuccess;
    }

    public MutableLiveData<ArrayList<Comment>> getCommentLiveData() {
        return currentComments;
    }

    // 게시글과 댓글 데이터를 로드하는 메서드
    public void loadArticlesDetail(Integer positionId) {
        articleRepository.loadArticleDetail(positionId, this::setIsLoadArticleSuccess);
    }

    public void postComment(Integer ArticleId, String content) {
        articleRepository.createComment(ArticleId, content, this::setIsCommentSuccess);
        // 만약 isCommentSuccess에 값이 들어있다면
        if (isCommentSuccess != null) {
            // 기존 댓글 리스트를 가져오고, 새로운 댓글을 추가한 뒤, currentComments에 설정
            ArrayList<Comment> currentList = currentComments.getValue();
            if (currentList == null) {
                currentList = new ArrayList<>(); // 리스트가 비어있다면 새로 초기화
            }
            currentList.add(isCommentSuccess); // 새로 받은 댓글 추가

            currentComments.setValue(new ArrayList<>(currentList));
        }
    }
}
