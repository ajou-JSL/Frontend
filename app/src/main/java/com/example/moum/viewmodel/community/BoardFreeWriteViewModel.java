package com.example.moum.viewmodel.community;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.repository.boardRepository;

import java.util.List;

public class BoardFreeWriteViewModel extends AndroidViewModel {

    private final boardRepository userboardRepository;
    private final MutableLiveData<String> writer = new MutableLiveData<>();
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> content = new MutableLiveData<>();
    private final MutableLiveData<List<String>> spinnerData = new MutableLiveData<>();


    public BoardFreeWriteViewModel(Application application) {
        super(application);
        userboardRepository = boardRepository.getInstance(application);
    }


    public MutableLiveData<String> getWriter() {
        return writer;
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getContent() {
        return content;
    }

    public MutableLiveData<List<String>> getSpinnerData() {
        return spinnerData;
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
