package com.example.moum.viewmodel.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.repository.MoumRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MoumRepository moumRepository;
    //private final MoumRepository moumRepository;
    //private final MoumRepository moumRepository;
    private final MutableLiveData<Result<List<Moum>>> isLoadMoumsSuccess = new MutableLiveData<>();
    private Integer memberId;

    public HomeViewModel(Application application) {
        super(application);
        moumRepository = MoumRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Moum>>> getIsLoadMoumsSuccess() {
        return isLoadMoumsSuccess;
    }

    public void setIsLoadMoumsSuccess(Result<List<Moum>> isLoadMoumsSuccess){
        this.isLoadMoumsSuccess.setValue(isLoadMoumsSuccess);
    }

    public void loadPosts(Integer memberId){
        //TODO
    }

    public void loadMoums(Integer memberId){
        moumRepository.loadMoumsAll(this::setIsLoadMoumsSuccess);
    }

    public void loadPerformances(Integer memberId){
        //TODO
    }
}