package com.example.moum.viewmodel.community;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.PerformRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PerformanceUpdateViewModel extends AndroidViewModel {
    private final PerformRepository performRepository;
    private final TeamRepository teamRepository;
    private final MutableLiveData<Performance> perform = new MutableLiveData<>(new Performance());
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();
    private final MutableLiveData<Result<Team>> isLoadTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Performance>> isLoadPerformanceSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Performance>> isPerformanceUpdateSuccess = new MutableLiveData<>();
    private Boolean fromExisting = false;
    private ArrayList<Music> music = new ArrayList<>();
    private ArrayList<Member> members;
    private ArrayList<Boolean> isParticipates;
    private LocalDate startDate;
    private LocalDate endDate;
    private Genre genre;

    public PerformanceUpdateViewModel(Application application){
        super(application);
        performRepository = PerformRepository.getInstance(application);
        teamRepository = TeamRepository.getInstance(application);
    }

    public MutableLiveData<Performance> getPerform() {
        return perform;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }

    public MutableLiveData<Result<Team>> getIsLoadTeamSuccess() {
        return isLoadTeamSuccess;
    }

    public MutableLiveData<Result<Performance>> getIsLoadPerformanceSuccess() {
        return isLoadPerformanceSuccess;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Performance>> getIsPerformanceUpdateSuccess() {
        return isPerformanceUpdateSuccess;
    }

    public void setProfileImage(Uri profileImage, Boolean fromExisting){
        this.profileImage.setValue(profileImage);
        this.fromExisting = fromExisting;
    }

    public void setDates(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addMusic(String musicName, String artistName){
        this.music.add(new Music(musicName, artistName));
    }

    public void setMembers(ArrayList<Member> members, ArrayList<Boolean> isParticipates){
        this.members = members;
        this.isParticipates = isParticipates;
    }

    public void setGenre(String genreStr) {
        this.genre = Genre.fromString(genreStr);
    }

    public void setIsLoadTeamSuccess(Result<Team> isLoadTeamSuccess){
        this.isLoadTeamSuccess.setValue(isLoadTeamSuccess);
    }

    public void setIsLoadPerformanceSuccess(Result<Performance> isLoadPerformanceSuccess) {
        this.isLoadPerformanceSuccess.setValue(isLoadPerformanceSuccess);
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess){
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsPerformanceUpdateSuccess(Result<Performance> isPerformanceUpdateSuccess){
        this.isPerformanceUpdateSuccess.setValue(isPerformanceUpdateSuccess);
    }

    public void loadTeam(Integer teamId){
        teamRepository.loadTeam(teamId, this::setIsLoadTeamSuccess);
    }

    public void loadPerformance(Integer performId){
        performRepository.loadPerform(performId, this::setIsLoadPerformanceSuccess);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validCheck(){
        /*null check*/
        if(perform.getValue() == null){
            setIsValidCheckSuccess(Validation.NOT_VALID_ANYWAY);
            return;
        }
        else if(perform.getValue().getPerformanceName() == null || perform.getValue().getPerformanceName().isEmpty()) {
            setIsValidCheckSuccess(Validation.MOUM_NAME_NOT_WRITTEN);
            return;
        }
        else if(genre == null){
            setIsValidCheckSuccess(Validation.GENRE_NOT_WRITTEN);
            return;
        }
        else if(startDate != null && endDate != null && startDate.isAfter(endDate)){
            setIsValidCheckSuccess(Validation.DATE_NOT_VALID);
            return;
        }
        else if(music != null){
            for(Music one : music){
                if(one.getMusicName() == null || one.getMusicName().isEmpty()){
                    setIsValidCheckSuccess(Validation.MUSIC_NAME_NOT_WRITTEN);
                    return;
                }
                else if(one.getArtistName() == null || one.getArtistName().isEmpty()){
                    setIsValidCheckSuccess(Validation.ARTIST_NAME_NOT_WRITTEN);
                    return;
                }
            }
        }
        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void updatePerformance(Integer performId, Integer teamId, Context context){
        /*valid check*/
        if(isValidCheckSuccess.getValue() == null || isValidCheckSuccess.getValue() != Validation.VALID_ALL){
            Result<Performance> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsPerformanceUpdateSuccess(result);
            return;
        }

        /*processing for repository*/
        Performance performToCreate = perform.getValue();
        performToCreate.setTeamId(teamId);
        performToCreate.setGenre(genre);
        if(startDate != null) performToCreate.setPerformanceStartDate(startDate.toString().concat("T00:00:00"));
        if(endDate != null) performToCreate.setPerformanceEndDate(endDate.toString().concat("T00:00:00"));
        performToCreate.setMusics(music);
        ArrayList<Integer> participants = new ArrayList<>();
        if(members != null && isParticipates != null)
            for (int i = 0; i < members.size(); i++) {
                if (isParticipates.get(i))
                    participants.add(members.get(i).getId());
            }
        performToCreate.setMembersId(participants);
        File profileFile = null;

        /*이미지를 동기적으로 다운로드받기 위한 작업*/
        if (profileImage.getValue() != null && fromExisting) {
            Callable<File> callable = () -> {
                ImageManager imageManager = new ImageManager(context);
                return imageManager.downloadImageToFile(profileImage.getValue().toString());
            };
            FutureTask<File> futureTask = new FutureTask<>(callable);
            Thread thread = new Thread(futureTask);
            thread.start();
            try {
                profileFile = futureTask.get();
                if (profileFile != null) {
                    Log.d("ProfileFile", "File downloaded: " + profileFile.getAbsolutePath());
                } else {
                    Log.e("ProfileFile", "Failed to download profile image");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(profileImage.getValue() != null && !fromExisting)
            profileFile = new ImageManager(context).convertUriToFile(profileImage.getValue());

        //TODO 백에서 구현되면 삭제할 것
        performToCreate.setMusics(null);

        /*goto repository*/
        performRepository.updatePerform(performId, performToCreate, profileFile, this::setIsPerformanceUpdateSuccess);
    }
}
