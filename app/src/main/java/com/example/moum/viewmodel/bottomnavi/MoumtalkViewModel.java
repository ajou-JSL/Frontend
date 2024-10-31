package com.example.moum.viewmodel.bottomnavi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MoumtalkViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MoumtalkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is moumtalk fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}