package com.example.moum.viewmodel.bottomnavi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MymoumViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MymoumViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Mymoum fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}