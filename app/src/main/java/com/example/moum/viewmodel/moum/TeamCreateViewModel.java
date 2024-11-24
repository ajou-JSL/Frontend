package com.example.moum.viewmodel.moum;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.YoutubeManager;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeamCreateViewModel extends AndroidViewModel {
    private final TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Team> team = new MutableLiveData<>(new Team());
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();
    private String address;
    private Genre genre;
    private ArrayList<Record> records = new ArrayList<>();
    private final  MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Team>> isCreateTeamSuccess = new MutableLiveData<>();

    public MutableLiveData<Team> getTeam() {
        return team;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }

    public String getAddress() {
        return address;
    }

    public MutableLiveData<Result<Team>> getIsCreateTeamSuccess() {
        return isCreateTeamSuccess;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public void setTeam(Team team) { this.team.setValue(team);}

    public void setProfileImage(Uri uri) { this.profileImage.setValue(uri);}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGenre(String genreStr) {
        this.genre = Genre.fromString(genreStr);
    }

    public void setIsCreateTeamSuccess(Result<Team> isCreateTeamSuccess){
        this.isCreateTeamSuccess.setValue(isCreateTeamSuccess);
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess){
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public TeamCreateViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public void addRecord(String name, LocalDate startDate, LocalDate endDate){
        Record newRecord = new Record(name, startDate.toString().concat("T00:00:00"), endDate.toString().concat("T00:00:00"));
        records.add(newRecord);
    }

    public void clearRecords(){
        records.clear();
    }

    public void validCheck(){
        if(team.getValue() == null){
            setIsValidCheckSuccess(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(team.getValue().getTeamName() == null || team.getValue().getTeamName().isEmpty()) {
            setIsValidCheckSuccess(Validation.TEAM_NAME_NOT_WRITTEN);
            return;
        }
        else if(genre == null) {
            setIsValidCheckSuccess(Validation.TEAM_GENRE_NOT_WRITTEN);
            return;
        }
        else if(team.getValue().getVideoUrl() != null && !YoutubeManager.isUrlValid(team.getValue().getVideoUrl())){
            setIsValidCheckSuccess(Validation.VIDEO_URL_NOT_FORMAL);
            return;
        }
        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void createTeam(Integer leaderId, Context context){
        /*valid check*/
        if(isValidCheckSuccess.getValue() == null || isValidCheckSuccess.getValue() != Validation.VALID_ALL){
            Result<Team> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsCreateTeamSuccess(result);
            return;
        }

        /*processing for repository*/
        Team teamToCreate = team.getValue();
        teamToCreate.setLeaderId(leaderId);
        teamToCreate.setTeamName(team.getValue().getTeamName());
        teamToCreate.setDescription(team.getValue().getDescription());
        teamToCreate.setGenre(genre);
        File profileFile = null;
        if(profileImage.getValue() != null){
            Uri uri = profileImage.getValue();
            ImageManager imageManager = new ImageManager(context);
            profileFile = imageManager.convertUriToFile(uri);
        }
        if(!records.isEmpty())
            teamToCreate.setRecords(records);
        if(address != null)
            teamToCreate.setLocation(address);

        /*goto repository*/
        teamRepository.createTeam(teamToCreate, profileFile, this::setIsCreateTeamSuccess);
    }
}
