package com.example.moum.viewmodel.moum;

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
import com.example.moum.data.entity.Result;
import com.example.moum.repository.MoumRepository;
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

public class MoumUpdateViewModel extends AndroidViewModel {
    private final TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Moum> moum = new MutableLiveData<>(new Moum());
    private final MutableLiveData<ArrayList<Uri>> profileImages = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isLoadMoumSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Moum>> isUpdateMoumSuccess = new MutableLiveData<>();
    private ArrayList<Music> music = new ArrayList<>();
    private Boolean isProfileUpdated = false;
    private LocalDate startDate;
    private LocalDate endDate;
    private Genre genre;

    public MoumUpdateViewModel(Application application) {
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

    public MutableLiveData<Result<Moum>> getIsLoadMoumSuccess() {
        return isLoadMoumSuccess;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Moum>> getIsUpdateMoumSuccess() {
        return isUpdateMoumSuccess;
    }

    public void setMoum(Moum moum) {
        this.moum.setValue(moum);
    }

    public void setGenre(String genreStr) {
        this.genre = Genre.fromString(genreStr);
    }

    public void setProfileImages(List<Uri> uris) {
        ArrayList<Uri> uriArrayList = new ArrayList<>(uris);
        this.profileImages.setValue(uriArrayList);
        isProfileUpdated = true;
    }

    public void setProfileImages(ArrayList<String> urls) {
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        for (String url : urls) {
            uriArrayList.add(Uri.parse(url));
        }
        this.profileImages.setValue(uriArrayList);
        isProfileUpdated = false;
    }

    public void setDates(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setIsLoadMoumSuccess(Result<Moum> isLoadMoumSuccess) {
        this.isLoadMoumSuccess.setValue(isLoadMoumSuccess);
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess) {
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsUpdateMoumSuccess(Result<Moum> isUpdateMoumSuccess) {
        this.isUpdateMoumSuccess.setValue(isUpdateMoumSuccess);
    }

    public void setIsProfileUpdated(Boolean isProfileUpdated) {
        this.isProfileUpdated = isProfileUpdated;
    }

    public void loadMoum(Integer moumId) {
        moumRepository.loadMoum(moumId, this::setIsLoadMoumSuccess);
    }

    public void addMusic(String musicName, String artistName) {
        this.music.add(new Music(musicName, artistName));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validCheck() {
        /*null check*/
        if (moum.getValue() == null) {
            setIsValidCheckSuccess(Validation.NOT_VALID_ANYWAY);
            return;
        } else if (moum.getValue().getMoumName() == null || moum.getValue().getMoumName().isEmpty()) {
            setIsValidCheckSuccess(Validation.MOUM_NAME_NOT_WRITTEN);
            return;
        } else if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            setIsValidCheckSuccess(Validation.DATE_NOT_VALID);
            return;
        } else if (music != null) {
            for (Music one : music) {
                if (one.getMusicName() == null || one.getMusicName().isEmpty()) {
                    setIsValidCheckSuccess(Validation.MUSIC_NAME_NOT_WRITTEN);
                    return;
                } else if (one.getArtistName() == null || one.getArtistName().isEmpty()) {
                    setIsValidCheckSuccess(Validation.ARTIST_NAME_NOT_WRITTEN);
                    return;
                }
            }
        }
        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void updateMoum(Integer moumId, Integer teamId, Integer leaderId, Context context) {
        /*valid check*/
        if (isValidCheckSuccess.getValue() == null || isValidCheckSuccess.getValue() != Validation.VALID_ALL) {
            Result<Moum> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsUpdateMoumSuccess(result);
            return;
        }

        /*processing for repository*/
        Moum moumToUpdate = moum.getValue();
        moumToUpdate.setGenre(genre);
        moumToUpdate.setMoumId(moumId);
        moumToUpdate.setTeamId(teamId);
        moumToUpdate.setLeaderId(leaderId);
        if (startDate != null) moumToUpdate.setStartDate(startDate.toString());
        if (endDate != null) moumToUpdate.setEndDate(endDate.toString());
        ArrayList<File> profileFiles = new ArrayList<>();
        if (profileImages.getValue() != null && !profileImages.getValue().isEmpty() && isProfileUpdated) {
            for (Uri uri : profileImages.getValue()) {
                ImageManager imageManager = new ImageManager(context);
                File profileFile = imageManager.convertUriToFile(uri);
                profileFiles.add(profileFile);
            }
        } else if (profileImages.getValue() != null && !profileImages.getValue().isEmpty() && !isProfileUpdated) {
            for (Uri profileImage : profileImages.getValue()) {
                File profileFile = null;
                Callable<File> callable = () -> {
                    ImageManager imageManager = new ImageManager(context);
                    return imageManager.downloadImageToFile(profileImage.toString());
                };
                FutureTask<File> futureTask = new FutureTask<>(callable);
                Thread thread = new Thread(futureTask);
                thread.start();
                try {
                    profileFile = futureTask.get();
                    if (profileFile != null) {
                        Log.e("ProfileFile", "File downloaded: " + profileFile.getAbsolutePath());
                        profileFiles.add(profileFile);
                    } else {
                        Log.e("ProfileFile", "Failed to download profile image");
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        moumToUpdate.setMembers(new ArrayList<>());
        moumToUpdate.setRecords(new ArrayList<>());
        moumToUpdate.setMusic(music);

        /*goto repository*/
        moumRepository.updateMoum(moumId, moumToUpdate, profileFiles, this::setIsUpdateMoumSuccess);
    }
}
