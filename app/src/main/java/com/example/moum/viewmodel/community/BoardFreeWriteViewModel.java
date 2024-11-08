package com.example.moum.viewmodel.community;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoardFreeWriteViewModel extends ViewModel{

    private final MutableLiveData<String> mText;

    private final MutableLiveData<String> writer = new MutableLiveData<>();
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> content = new MutableLiveData<>();



    public BoardFreeWriteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Boardfreewrite Activity");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getWriter() {
        return writer;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getContent() {
        return content;
    }

    public void setWriter(String writer) {
        this.writer.setValue(writer);
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setContent(String content) {
        this.content.setValue(content);
    }
}
