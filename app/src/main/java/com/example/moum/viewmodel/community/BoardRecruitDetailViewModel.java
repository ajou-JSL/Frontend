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

public class BoardRecruitDetailViewModel extends AndroidViewModel {
    private final MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private final MutableLiveData<Article> isLoadArticeSuccess = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Comment>> currentComments = new MutableLiveData<>();
    private final MutableLiveData<Like> isSetLikeSuccess = new MutableLiveData<>();
    private final MutableLiveData<Member> isLoadMemberSuccess = new MutableLiveData<>();
    private final MutableLiveData<Team>  isLoadTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Comment> isDeleteCommentSuccess = new MutableLiveData<>();
    private String userName = new String();
    private Comment isLoadCommentSuccess = new Comment();
    private ArticleRepository articleRepository;
    private TeamRepository teamRepository;
    private ProfileRepository profileRepository;
    private boolean isTeam = false;


    public BoardRecruitDetailViewModel(Application application) {
        super(application);
        articleRepository = ArticleRepository.getInstance(application);
        teamRepository = TeamRepository.getInstance(application);
        profileRepository = ProfileRepository.getInstance(application);
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
            isLoadCommentSuccess = result.getData();
        } else {
            // 실패 시 처리
            Log.e( "abc" , "댓글 추가 실패");
        }
    }

    private void setIsLikeSuccess(Result<Like> result) {
        if (result != null && result.getData() != null) {
            Like like = result.getData();
            isSetLikeSuccess.setValue(like);
        }
    }

    private void setIsLoadTeamSuccess(Result<Team> result){
        if(result != null && result.getData() != null) {
            Team team = result.getData();
            isLoadTeamSuccess.setValue(team);
        }
        else isLoadTeamSuccess.setValue(null);
    }

    private void setIsLoadMemberSuccess(Result<Member> result) {
        if(result != null && result.getData() != null) {
            Member member = result.getData();
            isLoadMemberSuccess.setValue(member);
        }
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

    public MutableLiveData<ArrayList<Comment>> getCurrentComments() {
        return currentComments;
    }

    public MutableLiveData<Team> getIsLoadTeamSuccess() {
        return isLoadTeamSuccess;
    }

    public MutableLiveData<Member> getIsLoadMemberSuccess(){
        return isLoadMemberSuccess;
    }

    public boolean isTeamOrMember(){
        return isTeam;
    }

    // 게시글과 댓글 데이터를 로드하는 메서드
    public void loadArticlesDetail(Integer positionId) {
        articleRepository.loadArticleDetail(positionId, this::setIsLoadArticleSuccess);
    }

    public void postComment(Integer ArticleId, String content) {
        articleRepository.createComment(ArticleId, content, this::setIsCommentSuccess);
        // 만약 isCommentSuccess에 값이 들어있다면
        if (isLoadCommentSuccess != null) {
            // 기존 댓글 리스트를 가져오고, 새로운 댓글을 추가한 뒤, currentComments에 설정
            ArrayList<Comment> currentList = currentComments.getValue();
            if (currentList == null) {
                currentList = new ArrayList<>(); // 리스트가 비어있다면 새로 초기화
            }
            currentList.add(isLoadCommentSuccess); // 새로 받은 댓글 추가

            currentComments.setValue(new ArrayList<>(currentList));
        }
    }

    public void postLike(Integer ArticleId) {
        articleRepository.postLike(ArticleId, this::setIsLikeSuccess);
        isLoadArticeSuccess.setValue(isLoadArticeSuccess.getValue());
    }

    public String loadProfileImage(Integer authorId, String authorName){
        teamRepository.loadTeamToTeamName(authorName, this::setIsLoadTeamSuccess);
        profileRepository.loadMemberProfile(authorId, this::setIsLoadMemberSuccess);
        if(isLoadTeamSuccess == null){
            isTeam = true;
            return isLoadTeamSuccess.getValue().getFileUrl();
        }
        return isLoadMemberSuccess.getValue().getProfileImageUrl();
    }

    public void deleteComment(Integer commentId){
        articleRepository.deleteComment(commentId, this::setIsDeleteCommentSuccess);
        isLoadArticeSuccess.setValue(isLoadArticeSuccess.getValue());
    }
}
