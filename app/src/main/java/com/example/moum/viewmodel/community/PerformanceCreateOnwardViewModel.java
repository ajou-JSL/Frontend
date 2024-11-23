package com.example.moum.viewmodel.community;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PerformanceCreateOnwardViewModel extends AndroidViewModel {
    private final PerformRepository performRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Performance> perform = new MutableLiveData<>(new Performance());
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isLoadMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Performance>> isPerformanceCreateSuccess = new MutableLiveData<>();
    private Boolean fromMoum = false;
    private ArrayList<Music> music = new ArrayList<>();
    private ArrayList<Member> members;
    private ArrayList<Boolean> isParticipates;
    private LocalDate startDate;
    private LocalDate endDate;

    public PerformanceCreateOnwardViewModel(Application application){
        super(application);
        performRepository = PerformRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public MutableLiveData<Performance> getPerform() {
        return perform;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }

    public MutableLiveData<Result<Moum>> getIsLoadMoumSuccess() {
        return isLoadMoumSuccess;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Performance>> getIsPerformanceCreateSuccess() {
        return isPerformanceCreateSuccess;
    }

    public void setProfileImage(Uri profileImage, Boolean fromMoum){
        this.profileImage.setValue(profileImage);
        this.fromMoum = fromMoum;
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

    public void setIsLoadMoumSuccess(Result<Moum> isLoadMoumSuccess){
        this.isLoadMoumSuccess.setValue(isLoadMoumSuccess);
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess){
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsPerformanceCreateSuccess(Result<Performance> isPerformanceCreateSuccess){
        this.isPerformanceCreateSuccess.setValue(isPerformanceCreateSuccess);
    }

    public void loadMoum(Integer moumId){
        moumRepository.loadMoum(moumId, this::setIsLoadMoumSuccess);
    }

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
        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void createPerformance(Integer moumId, Integer teamId, Context context){
        /*valid check*/
        if(isValidCheckSuccess.getValue() == null || isValidCheckSuccess.getValue() != Validation.VALID_ALL){
            Result<Performance> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsPerformanceCreateSuccess(result);
            return;
        }

        /*processing for repository*/
        Performance performToCreate = perform.getValue();
        performToCreate.setMoumId(moumId);
        performToCreate.setTeamId(teamId);
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
        if (profileImage.getValue() != null && fromMoum) {
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
        else if(profileImage.getValue() != null && !fromMoum)
            profileFile = new ImageManager(context).convertUriToFile(profileImage.getValue());

        //TODO 백에서 구현되면 삭제할 것
        performToCreate.setMusics(null);

        /*goto repository*/
        performRepository.createPerform(performToCreate, profileFile, this::setIsPerformanceCreateSuccess);
    }
}
