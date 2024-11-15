package com.example.moum.viewmodel.community;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoardFreeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BoardFreeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Boardfree fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}