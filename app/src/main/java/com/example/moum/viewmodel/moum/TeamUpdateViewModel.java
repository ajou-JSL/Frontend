package com.example.moum.viewmodel.moum;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.YoutubeManager;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TeamUpdateViewModel extends AndroidViewModel {
    private final TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Team> team = new MutableLiveData<>(new Team());
    private final MutableLiveData<Uri> profileImage = new MutableLiveData<>();
    private String address;
    private Genre genre;
    private ArrayList<Record> records = new ArrayList<>();
    private Boolean fromExisting = false;
    private final MutableLiveData<Result<Team>> isLoadTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Validation> isValidCheckSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Team>> isUpdateTeamSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<Team>> isDeleteTeamSuccess = new MutableLiveData<>();

    public MutableLiveData<Team> getTeam() {
        return team;
    }

    public MutableLiveData<Uri> getProfileImage() {
        return profileImage;
    }

    public String getAddress() {
        return address;
    }

    public MutableLiveData<Result<Team>> getIsLoadTeamSuccess() {
        return isLoadTeamSuccess;
    }

    public MutableLiveData<Result<Team>> getIsUpdateTeamSuccess() {
        return isUpdateTeamSuccess;
    }

    public MutableLiveData<Validation> getIsValidCheckSuccess() {
        return isValidCheckSuccess;
    }

    public MutableLiveData<Result<Team>> getIsDeleteTeamSuccess() {
        return isDeleteTeamSuccess;
    }

    public void setTeam(Team team) {
        this.team.setValue(team);
    }

    public void setProfileImage(Uri uri, Boolean fromExisting) {
        this.profileImage.setValue(uri);
        this.fromExisting = fromExisting;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGenre(String genreStr) {
        this.genre = Genre.fromString(genreStr);
    }

    public void setIsLoadTeamSuccess(Result<Team> isLoadTeamSuccess) {
        this.isLoadTeamSuccess.setValue(isLoadTeamSuccess);
    }

    public void setIsUpdateTeamSuccess(Result<Team> isUpdateTeamSuccess) {
        this.isUpdateTeamSuccess.setValue(isUpdateTeamSuccess);
    }

    public void setIsValidCheckSuccess(Validation isValidCheckSuccess) {
        this.isValidCheckSuccess.setValue(isValidCheckSuccess);
    }

    public void setIsDeleteTeamSuccess(Result<Team> isDeleteTeamSuccess) {
        this.isDeleteTeamSuccess.setValue(isDeleteTeamSuccess);
    }

    public TeamUpdateViewModel(Application application) {
        super(application);
        teamRepository = TeamRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public void addRecord(String name, LocalDate startDate, LocalDate endDate) {
        String startDateStr = null;
        String endDateStr = null;
        if (startDate != null) {
            startDateStr = startDate.toString().concat("T00:00:00");
        }
        if (endDate != null) {
            endDateStr = endDate.toString().concat("T00:00:00");
        }
        Record newRecord = new Record(name, startDateStr, endDateStr);
        records.add(newRecord);
    }

    public void clearRecords() {
        records.clear();
    }

    public void loadTeam(Integer teamId) {
        teamRepository.loadTeam(teamId, this::setIsLoadTeamSuccess);
    }

    public void validCheck() {
        if (team.getValue() == null) {
            setIsValidCheckSuccess(Validation.NOT_VALID_ANYWAY);
            return;
        } else if (team.getValue().getTeamName() == null || team.getValue().getTeamName().isEmpty()) {
            setIsValidCheckSuccess(Validation.TEAM_NAME_NOT_WRITTEN);
            return;
        } else if (genre == null) {
            setIsValidCheckSuccess(Validation.TEAM_GENRE_NOT_WRITTEN);
            return;
        } else if (team.getValue().getVideoUrl() != null && !YoutubeManager.isUrlValid(team.getValue().getVideoUrl())) {
            setIsValidCheckSuccess(Validation.VIDEO_URL_NOT_FORMAL);
            return;
        }
        setIsValidCheckSuccess(Validation.VALID_ALL);
    }

    public void updateTeam(Integer teamId, Integer leaderId, Context context) {
        /*valid check*/
        if (isValidCheckSuccess.getValue() == null || isValidCheckSuccess.getValue() != Validation.VALID_ALL) {
            Result<Team> result = new Result<>(Validation.NOT_VALID_ANYWAY);
            setIsUpdateTeamSuccess(result);
            return;
        }

        /*processing for repository*/
        Team teamToCreate = team.getValue();
        teamToCreate.setLeaderId(leaderId);
        teamToCreate.setTeamName(team.getValue().getTeamName());
        teamToCreate.setDescription(team.getValue().getDescription());
        teamToCreate.setGenre(genre);
        File profileFile = null;
        if (profileImage.getValue() != null && !fromExisting) {
            Uri uri = profileImage.getValue();
            ImageManager imageManager = new ImageManager(context);
            profileFile = imageManager.convertUriToFile(uri);
        } else if (profileImage.getValue() != null && fromExisting) {
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
        if (records != null) {
            for (Record record : records) {
                if (record.getStartDate() != null && record.getEndDate() != null && record.getStartDate().compareTo(record.getEndDate()) > 0) {
                    Result<Team> result = new Result<>(Validation.RECORD_NOT_VALID);
                    setIsUpdateTeamSuccess(result);
                    return;
                } else if (record.getRecordName() == null || record.getRecordName().isEmpty()) {
                    Result<Team> result = new Result<>(Validation.RECORD_NAME_NOT_WRITTEN);
                    setIsUpdateTeamSuccess(result);
                    return;
                }
            }
            teamToCreate.setRecords(records);
        }
        if (address != null) {
            teamToCreate.setLocation(address);
        }

        /*goto repository*/
        teamRepository.updateTeam(teamId, teamToCreate, profileFile, this::setIsUpdateTeamSuccess);
    }

    public void deleteTeam(Integer teamId) {
        teamRepository.deleteTeam(teamId, this::setIsDeleteTeamSuccess);
    }
}
