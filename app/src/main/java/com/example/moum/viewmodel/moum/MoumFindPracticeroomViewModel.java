package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.view.moum.MoumFindPracticeroomActivity;

import java.util.List;

public class MoumFindPracticeroomViewModel extends AndroidViewModel {
    private final MutableLiveData<Result<List<Practiceroom>>> isQueryPracticeroomSuccess = new MutableLiveData<>();

    public MoumFindPracticeroomViewModel(Application application){
        super(application);
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsQueryPracticeroomSuccess() {
        return isQueryPracticeroomSuccess;
    }

    public void setIsQueryPracticeroomSuccess(Result<List<Practiceroom>> isQueryPracticeroomSuccess){
        this.isQueryPracticeroomSuccess.setValue(isQueryPracticeroomSuccess);
    }

    public void queryPracticeroom(String query){

    }
}
