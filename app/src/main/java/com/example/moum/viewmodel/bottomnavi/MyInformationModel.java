package com.example.moum.viewmodel.bottomnavi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyInformationModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyInformationModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my information fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}