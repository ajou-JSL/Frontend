package com.example.moum.viewmodel.community;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.data.entity.Member;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.repository.ProfileRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoardFreeWriteViewModel extends AndroidViewModel {
    private final ArticleRepository articleRepository;
    private final TeamRepository teamRepository;
    private final ProfileRepository profileRepository;
    private final MutableLiveData<Article> article = new MutableLiveData<>(new Article());
    private final MutableLiveData<Result<List<Team>>> isLoadTeamsAsLeaderSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Article>> isArticleCreateSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Team> teamSelected = new MutableLiveData<>();
    private final MutableLiveData<Member> memberSelected = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Uri>> fileImageList = new MutableLiveData<>();


    public BoardFreeWriteViewModel(Application application) {
        super(application);
        articleRepository = ArticleRepository.getInstance(application);
        teamRepository = TeamRepository.getInstance(application);
        profileRepository = ProfileRepository.getInstance(application);
    }

    public Team getTeamSelectedValue(){
        return teamSelected.getValue();
    }

    public MutableLiveData<Result<List<Team>>> getIsLoadTeamsAsLeaderSuccess() {
        return isLoadTeamsAsLeaderSuccess;
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess){
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void validCheck(){
        /*null check*/
        if(article.getValue() == null){
            setIsValidCheckSuccess(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(article.getValue().getTitle() == null || article.getValue().getTitle().isEmpty()) {
            setIsValidCheckSuccess(Validation.MOUM_NAME_NOT_WRITTEN);
            return;
        }
    }

    public void setIsLoadTeamsAsLeaderSuccess(Result<List<Team>> isLoadTeamsAsLeaderSuccess){
        this.isLoadTeamsAsLeaderSuccess.setValue(isLoadTeamsAsLeaderSuccess);
    }

    public void setIsArticleCreateSuccess(Result<Article> isArticleCreateSuccess){
        this.isArticleCreateSuccess.setValue(isArticleCreateSuccess);
    }

    public void setMemberSelected(Result<Member> memberSelected) {
        this.memberSelected.setValue(memberSelected.getData());
    }

    public MutableLiveData<Result<Article>> getIsArticleCreateSuccess() {
        return isArticleCreateSuccess;
    }

    public MutableLiveData<Member> getMemberSelected(){
        return memberSelected;
    }

    public void setFileImage(Uri uri) {
        if (uri != null) {
            // 기존 리스트 가져오기
            ArrayList<Uri> currentList = fileImageList.getValue();
            if (currentList == null) {
                currentList = new ArrayList<>();
            }
            // 새로운 URI 추가
            currentList.add(uri);
            // 리스트 업데이트
            fileImageList.setValue(currentList);
        }
    }


    public MutableLiveData<ArrayList<Uri>> getFileImage() {
        return fileImageList;
    }


    public void loadTeamsAsLeader(Integer memberId){
        teamRepository.loadTeamsAsMember(memberId, result -> {
            Validation validation = result.getValidation();
            List<Team> loadedTeams = result.getData();
            List<Team> teamsAsLeader = new ArrayList<>();
            if(validation == Validation.GET_TEAM_LIST_SUCCESS && !loadedTeams.isEmpty()) {
                for (Team team : loadedTeams)
                    if (team.getLeaderId().equals(memberId))
                        teamsAsLeader.add(team);
                Result<List<Team>> newResult = new Result<>(validation, teamsAsLeader);
                setIsLoadTeamsAsLeaderSuccess(newResult);
            }
            else{
                setIsLoadTeamsAsLeaderSuccess(result);
            }
        });
    }

    public void createArticle(String title, String content, String category, Integer genre, List<Uri> imageList){
        // 제목 검증
        if (title == null || title.trim().isEmpty()) {
            setIsArticleCreateSuccess(new Result<>(Validation.ARTICLE_INVALID_TITLE));
            return;
        }

        // 내용 검증
        if (content == null || content.trim().isEmpty()) {
            setIsArticleCreateSuccess(new Result<>(Validation.ARTICLE_INVALID_CONTENT));
            return;
        }

        // 카테고리 검증
        if (category == null || category.trim().isEmpty()) {
            setIsArticleCreateSuccess(new Result<>(Validation.ARTICLE_INVALID_CATEGORY));
            return;
        }

        // 장르 검증 (필요에 따라 세부 조건 추가 가능)
        if (genre == null || genre < 0) {
            setIsArticleCreateSuccess(new Result<>(Validation.ARTICLE_INVALID_GENRE));
            return;
        }

        Article articleToCreate = article.getValue();
        articleToCreate.setTitle(title);
        articleToCreate.setContent(content);
        articleToCreate.setCategory(category);
        articleToCreate.setGenre(genre);

        List<File> imageURL = new ArrayList<>();
        if (imageList != null) {
            for (Uri uri : imageList) {
                File file = new File(getRealPathFromUri(uri));
                imageURL.add(file);
            }
        }

        articleRepository.createArticle(articleToCreate, imageURL, this::setIsArticleCreateSuccess);
    }

    public void loadMemberProfile(Integer memberId){
        profileRepository.loadMemberProfile(memberId, this::setMemberSelected);
    }

    private String getRealPathFromUri(Uri uri) {
        return uri.getPath();
    }
}
