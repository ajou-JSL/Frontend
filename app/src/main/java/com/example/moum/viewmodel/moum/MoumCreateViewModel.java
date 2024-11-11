package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;

import java.util.List;

public class MoumCreateViewModel extends AndroidViewModel {
    private final TeamRepository teamRepository;
    private final MoumRepository moumRepository;
    private final MutableLiveData<Moum> moum = new MutableLiveData<>(new Moum());

    public MutableLiveData<Moum> getMoum() {
        return moum;
    }

    public void setMoum(Moum moum) { this.moum.setValue(moum);}

    public MoumCreateViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
        moumRepository = MoumRepository.getInstance(application);
    }
}
