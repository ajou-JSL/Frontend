package com.example.moum.viewmodel.bottomnavi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunityViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedTabIndex = new MutableLiveData<>(2);

    public LiveData<Integer> getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public void setSelectedTabIndex(int index) {
        selectedTabIndex.setValue(index);
    }
}
