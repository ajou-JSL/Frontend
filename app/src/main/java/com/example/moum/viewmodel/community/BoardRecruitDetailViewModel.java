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
    private final MutableLiveData<Result<Like>> isLoadLikeSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Member>> isLoadMemberSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Member>> isLoadItemMemberSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Comment>> isChangeCommentSuccess = new MutableLiveData<>();
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

    private void setIsChangeCommentSuccess(Result<Comment> result) {
        this.isChangeCommentSuccess.setValue(result);
    }

    private void setIsLikeSuccess(Result<Like> result) {
        isLoadLikeSuccess.setValue(result);
    }

    private void setIsLoadMemberSuccess(Result<Member> result) {
        isLoadMemberSuccess.setValue(result);
    }

    private void setIsLoadItemMemberSuccess(Result<Member> result) {
        isLoadItemMemberSuccess.setValue(result);
    }

    private void setIsItemLoadMemberSuccess(Result<Member> result) {
        isLoadItemMemberSuccess.setValue(result);
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

    public MutableLiveData<Result<Member>> getIsLoadItemMemberSuccess(){
        return isLoadItemMemberSuccess;
    }

    public MutableLiveData<Result<Comment>> getIsChangeCommentSuccess(){
        return isChangeCommentSuccess;
    }

    public void loadArticlesDetail(Integer targetBoardId) {
        articleRepository.loadArticleDetail(targetBoardId, this::setIsLoadArticleSuccess);
    }

    public void loadComments(Integer targetBoardId) {
        articleRepository.loadArticleComments(targetBoardId, this::setIsLoadCommentSuccess);
    }

    public void postComment(Integer ArticleId, String content) {
        articleRepository.createComment(ArticleId, content, this::setIsChangeCommentSuccess);
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

    public void loadItemProfileImage(Integer authorId){
        profileRepository.loadMemberProfile(authorId, this::setIsLoadItemMemberSuccess);
    }

    public void deleteComment(Integer commentId){
        articleRepository.deleteComment(commentId, this::setIsChangeCommentSuccess);
    }

    public void deleteArticle(Integer articleId){
        articleRepository.deleteArticle(articleId, this::setIsdeleteArticeSuccess);
    }
}
