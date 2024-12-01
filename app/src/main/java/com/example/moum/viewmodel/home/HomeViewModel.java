package com.example.moum.viewmodel.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.ArticleRepository;
import com.example.moum.repository.ChatroomRepository;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.PerformRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MoumRepository moumRepository;
    private final ArticleRepository articleRepository;
    private final PerformRepository performRepository;
    private final MutableLiveData<Result<List<Article>>> isLoadArticlesHotSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Moum>>> isLoadMoumsSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Performance>>> isLoadPerformsHotSuccess = new MutableLiveData<>();
    private Integer memberId;

    public HomeViewModel(Application application) {
        super(application);
        moumRepository = MoumRepository.getInstance(application);
        articleRepository = ArticleRepository.getInstance(application);
        performRepository = PerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Article>>> getIsLoadArticlesHotSuccess() {
        return isLoadArticlesHotSuccess;
    }

    public MutableLiveData<Result<List<Moum>>> getIsLoadMoumsSuccess() {
        return isLoadMoumsSuccess;
    }

    public MutableLiveData<Result<List<Performance>>> getIsLoadPerformsHotSuccess() {
        return isLoadPerformsHotSuccess;
    }

    public void setIsLoadArticlesHotSuccess(Result<List<Article>> isLoadArticlesHotSuccess){
        this.isLoadArticlesHotSuccess.setValue(isLoadArticlesHotSuccess);
    }

    public void setIsLoadMoumsSuccess(Result<List<Moum>> isLoadMoumsSuccess){
        this.isLoadMoumsSuccess.setValue(isLoadMoumsSuccess);
    }

    public void setIsLoadPerformsHotSuccess(Result<List<Performance>> isLoadPerformsHotSuccess){
        this.isLoadPerformsHotSuccess.setValue(isLoadPerformsHotSuccess);
    }

    public void loadArticlesHot(){
        articleRepository.loadArticlesHot(0, 5, this::setIsLoadArticlesHotSuccess);
    }

    public void loadMoums(Integer memberId){
        moumRepository.loadMoumsAll(this::setIsLoadMoumsSuccess);
    }

    public void loadPerformsHot(){
        performRepository.loadPerformsHot(0, 5, this::setIsLoadPerformsHotSuccess);
    }
}