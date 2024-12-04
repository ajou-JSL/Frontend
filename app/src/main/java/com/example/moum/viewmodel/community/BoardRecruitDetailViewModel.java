package com.example.moum.viewmodel.community;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.data.entity.Like;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.repository.ProfileRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class BoardRecruitDetailViewModel extends AndroidViewModel {
    private final MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Article> isLoadArticeSuccess = new MutableLiveData<>();
    private final MutableLiveData<Article> isdeleteArticeSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Comment>>> isLoadCommentsSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Comment>> isPostCommentsSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Like>> isLoadLikeSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Member>> isLoadMemberSuccess = new MutableLiveData<>();
    private final MutableLiveData<Comment> isDeleteCommentSuccess = new MutableLiveData<>();
    private String userName = new String();
    private ArticleRepository articleRepository;
    private ProfileRepository profileRepository;


    public BoardRecruitDetailViewModel(Application application) {
        super(application);
        articleRepository = ArticleRepository.getInstance(application);
        profileRepository = ProfileRepository.getInstance(application);
    }
    private void setIsLoadArticleSuccess(Result<Article> result) {
        if (result != null && result.getData() != null) {
            this.isLoadArticeSuccess.setValue(result.getData());
        } else {
            validationStatus.setValue(Validation.ARTICLE_GET_FAILED); // 에러 상태 처리
        }
    }

    private void setIsdeleteArticeSuccess(Result<Article> result) {
        this.isdeleteArticeSuccess.setValue(result.getData());
    }

    private void setIsLoadCommentSuccess(Result<List<Comment>> result) {
        this.isLoadCommentsSuccess.setValue(result);
    }

    private void setIsPostCommentSuccess(Result<Comment> result) {
        this.isPostCommentsSuccess.setValue(result);
    }

    private void setIsLikeSuccess(Result<Like> result) {
        isLoadLikeSuccess.setValue(result);
    }

    private void setIsLoadMemberSuccess(Result<Member> result) {
        isLoadMemberSuccess.setValue(result);
    }

    private void setIsDeleteCommentSuccess(Result<Comment> result) {
        if(result != null && result.getData() != null) {
            Comment comment = result.getData();
            isDeleteCommentSuccess.setValue(comment);
        }
        else isDeleteCommentSuccess.setValue(null);
    }

    public MutableLiveData<Validation> getValidationStatus() {
        return validationStatus;
    }

    public MutableLiveData<Article> getIsLoadArticeSuccess() {
        return isLoadArticeSuccess;
    }

    public MutableLiveData<Result<Like>> getIsLikeSuccess() {
        return isLoadLikeSuccess;
    }

    public MutableLiveData<Result<List<Comment>>> getisLoadCommentsSuccess() {
        return isLoadCommentsSuccess;
    }

    public MutableLiveData<Result<Member>> getIsLoadMemberSuccess(){
        return isLoadMemberSuccess;
    }

    public void loadArticlesDetail(Integer targetBoardId) {
        articleRepository.loadArticleDetail(targetBoardId, this::setIsLoadArticleSuccess);
    }

    public void loadComments(Integer targetBoardId) {
        articleRepository.loadArticleComments(targetBoardId, this::setIsLoadCommentSuccess);
    }

    public void postComment(Integer ArticleId, String content) {
        articleRepository.createComment(ArticleId, content, this::setIsPostCommentSuccess);
    }

    public void loadLike(Integer memberId, Integer articleId) {
        articleRepository.loadLike(memberId, articleId, this::setIsLikeSuccess);
    }

    public void postLike(Integer memberId, Integer articleId) {
        articleRepository.postLike(memberId, articleId, this::setIsLikeSuccess);
    }

    public void loadProfileImage(Integer authorId){
        profileRepository.loadMemberProfile(authorId, this::setIsLoadMemberSuccess);
    }

    public void deleteComment(Integer commentId){
        articleRepository.deleteComment(commentId, this::setIsDeleteCommentSuccess);
    }

    public void deleteArticle(Integer articleId){
        articleRepository.deleteArticle(articleId, this::setIsdeleteArticeSuccess);
    }
}
