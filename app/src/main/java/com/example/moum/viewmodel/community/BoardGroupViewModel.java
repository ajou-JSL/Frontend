package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class BoardGroupViewModel extends AndroidViewModel {
    private MutableLiveData<List<Team>> TeamList = new MutableLiveData<>();
    private MutableLiveData<Validation> validationStatus = new MutableLiveData<>();
    private TeamRepository TeamRepository;

    public BoardGroupViewModel(Application application) {
        super(application);
        TeamRepository = TeamRepository.getInstance(application);
    }

    // LiveData를 외부에서 관찰할 수 있도록 제공
    public MutableLiveData<List<Team>> getBoardGroupList() {
        return TeamList;
    }

    public MutableLiveData<Validation> getValidationStatus() {
        return validationStatus;
    }

    public void setBoardGroupList(Result<List<Team>> result) {
        if (result != null && result.getData() != null) {
            this.TeamList.setValue(result.getData());
        } else {
            this.TeamList.setValue(new ArrayList<>());
        }
    }


    public void LoadBoardTeamList() {
        TeamRepository.loadTeams(new Callback<Result<List<Team>>>() {
            @Override
            public void onResult(Result<List<Team>> result) {
                if (result.getValidation() == Validation.GET_TEAM_LIST_SUCCESS) {
                    TeamList.setValue(result.getData());
                } else {
                    // 실패 시 상태 업데이트
                    validationStatus.setValue(result.getValidation());
                }
            }
        });
    }

}
