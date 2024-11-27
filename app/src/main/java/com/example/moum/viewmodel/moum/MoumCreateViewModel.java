package com.example.moum.viewmodel.moum;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoumCreateViewModel extends AndroidViewModel {
    private final TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Moum> moum = new MutableLiveData<>(new Moum());
    private final MutableLiveData<ArrayList<Uri>> profileImages = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isCreateMoumSuccess = new MutableLiveData<>();
    private ArrayList<Music> music = new ArrayList<>();
    private Genre genre;
    private LocalDate startDate;
    private LocalDate endDate;

    public MoumCreateViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public MutableLiveData<Moum> getMoum() {
        return moum;
    }

    public MutableLiveData<ArrayList<Uri>> getProfileImages() {
        return profileImages;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Moum>> getIsCreateMoumSuccess() {
        return isCreateMoumSuccess;
    }

    public void setMoum(Moum moum) { this.moum.setValue(moum);}

    public void setGenre(String genreStr) {
        this.genre = Genre.fromString(genreStr);
    }

    public void setProfileImages(List<Uri> uris) {
        ArrayList<Uri> uriArrayList = new ArrayList<>(uris);
        this.profileImages.setValue(uriArrayList);
    }

    public void setDates(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess){
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsCreateMoumSuccess(Result<Moum> isCreateMoumSuccess){
        this.isCreateMoumSuccess.setValue(isCreateMoumSuccess);
    }

    public void addMusic(String musicName, String artistName){
        this.music.add(new Music(musicName, artistName));
    }

    public void validCheck(){
        /*null check*/
        if(moum.getValue() == null){
            setIsValidCheckSuccess(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(moum.getValue().getMoumName() == null || moum.getValue().getMoumName().isEmpty()) {
            setIsValidCheckSuccess(Validation.MOUM_NAME_NOT_WRITTEN);
            return;
        }
        else if(genre == null){
            setIsValidCheckSuccess(Validation.GENRE_NOT_WRITTEN);
            return;
        }
        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void createMoum(Integer leaderId, Integer teamId, Context context){
        /*valid check*/
        if(isValidCheckSuccess.getValue() == null || isValidCheckSuccess.getValue() != Validation.VALID_ALL){
            Result<Moum> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsCreateMoumSuccess(result);
            return;
        }

        /*processing for repository*/
        Moum moumToCreate = moum.getValue();
        moumToCreate.setLeaderId(leaderId);
        moumToCreate.setTeamId(teamId);
        moumToCreate.setGenre(genre);
        if(startDate != null) moumToCreate.setStartDate(startDate.toString());
        if(endDate != null) moumToCreate.setEndDate(endDate.toString());
        ArrayList<File> profileFiles = new ArrayList<>();
        if(profileImages.getValue() != null && !profileImages.getValue().isEmpty()){
            for(Uri uri : profileImages.getValue()){
                ImageManager imageManager = new ImageManager(context);
                File profileFile = imageManager.convertUriToFile(uri);
                profileFiles.add(profileFile);
            }
        }
        if(profileFiles.isEmpty()) profileFiles = null;
        ArrayList<Member> members = new ArrayList<>();
        moumToCreate.setMembers(members);
        moumToCreate.setRecords(new ArrayList<>());
        moumToCreate.setMusic(music);

        /*goto repository*/
        moumRepository.createMoum(moumToCreate, profileFiles, this::setIsCreateMoumSuccess);
    }
}
