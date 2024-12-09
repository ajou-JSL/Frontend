package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.R;
import com.example.moum.data.dto.SearchPerformHallArgs;
import com.example.moum.data.dto.SearchPracticeroomArgs;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;
import com.example.moum.view.moum.MoumFindPracticeroomActivity;
import com.naver.maps.geometry.LatLng;

import java.util.List;

public class MoumFindPracticeroomViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<Practiceroom>>> isQueryPracticeroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Practiceroom>>> isQueryNextPracticeroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Practiceroom>>> isLoadPracticeroomSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Practiceroom>>> isLoadNextPracticeroomSuccess = new MutableLiveData<>();
    private final int LOAD_PRACTICE_ROOM_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 1;
    private Integer recentPageNumber = 0;
    private boolean isQuerying = false;
    private final SearchPracticeroomArgs args = new SearchPracticeroomArgs();

    public MoumFindPracticeroomViewModel(Application application) {
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsQueryPracticeroomSuccess() {
        return isQueryPracticeroomSuccess;
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsQueryNextPracticeroomSuccess() {
        return isQueryNextPracticeroomSuccess;
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsLoadPracticeroomSuccess() {
        return isLoadPracticeroomSuccess;
    }

    public MutableLiveData<Result<List<Practiceroom>>> getIsLoadNextPracticeroomSuccess() {
        return isLoadNextPracticeroomSuccess;
    }

    public SearchPracticeroomArgs getArgs() {
        return args;
    }

    public boolean isQuerying() {
        return isQuerying;
    }

    public void setIsQueryPracticeroomSuccess(Result<List<Practiceroom>> isQueryPracticeroomSuccess) {
        this.isQueryPracticeroomSuccess.setValue(isQueryPracticeroomSuccess);
    }

    public void setIsQueryNextPracticeroomSuccess(Result<List<Practiceroom>> isQueryNextPracticeroomSuccess) {
        this.isQueryNextPracticeroomSuccess.setValue(isQueryNextPracticeroomSuccess);
    }

    public void setIsLoadPracticeroomSuccess(Result<List<Practiceroom>> isLoadPracticeroomSuccess) {
        this.isLoadPracticeroomSuccess.setValue(isLoadPracticeroomSuccess);
    }

    public void setIsLoadNextPracticeroomSuccess(Result<List<Practiceroom>> isLoadNextPracticeroomSuccess) {
        this.isLoadNextPracticeroomSuccess.setValue(isLoadNextPracticeroomSuccess);
    }

    public void setName(String name) {
        this.args.setName(name);
    }

    public void setLatLng(LatLng latLng) {
        this.args.setLatitude(latLng.latitude);
        this.args.setLongitude(latLng.longitude);
    }

    public void queryPracticeroomWithArgs(SearchPracticeroomArgs args) {
        this.args.setSortBy(args.getSortBy());
        this.args.setOrderBy(args.getOrderBy());
        if (args.getType() != -1) this.args.setType(args.getType());
        if (args.getMinPrice() != -1) this.args.setMinPrice(args.getMinPrice());
        if (args.getMaxPrice() != -1) this.args.setMaxPrice(args.getMaxPrice());
        if (args.getMinCapacity() != -1) this.args.setMinCapacity(args.getMinCapacity());
        if (args.getMaxCapacity() != -1) this.args.setMaxCapacity(args.getMaxCapacity());
        if (args.getMinStand() != -1) this.args.setMinStand(args.getMinStand());
        if (args.getMaxStand() != -1) this.args.setMaxStand(args.getMaxStand());
        if (args.getHasPiano() != null) this.args.setHasPiano(args.getHasPiano());
        if (args.getHasAmp() != null) this.args.setHasAmp(args.getHasAmp());
        if (args.getHasSpeaker() != null) this.args.setHasSpeaker(args.getHasSpeaker());
        if (args.getHasMic() != null) this.args.setHasMic(args.getHasMic());
        if (args.getHasDrums() != null) this.args.setHasDrums(args.getHasDrums());
        queryPracticeroom();
    }

    public void queryPracticeroomWithName(String name) {
        setName(name);
        queryPracticeroom();
    }

    public void queryPracticeroom() {
        clearPage();
        if (args.getLatitude() == null || args.getLongitude() == null) return;
        isQuerying = true;

        /*goto repository*/
        practiceNPerformRepository.searchPracticerooms(page, LOAD_PRACTICE_ROOM_NUMBER, args, this::setIsQueryPracticeroomSuccess);
        page = page + 1;
    }

    public void queryNextPracticeroom() {
        if (args.getLatitude() == null || args.getLongitude() == null) return;
        isQuerying = true;
        if (recentPageNumber < LOAD_PRACTICE_ROOM_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PRACTICE_ROOM_NUMBER 적다면, 그만 불러오기

        /*goto repository*/
        practiceNPerformRepository.searchPracticerooms(page, LOAD_PRACTICE_ROOM_NUMBER, args, this::setIsQueryNextPracticeroomSuccess);
        page = page + 1;
    }


    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void clearPage() {
        page = 1;
        recentPageNumber = 0;
    }

    public void loadPracticeroom() {
        practiceNPerformRepository.getPracticerooms(page, LOAD_PRACTICE_ROOM_NUMBER, this::setIsLoadPracticeroomSuccess);
        page = page + 1;
    }


    public void loadNextPracticeroom() {
        if (recentPageNumber < LOAD_PRACTICE_ROOM_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PRACTICE_ROOM_NUMBER 적다면, 그만 불러오기
        practiceNPerformRepository.getPracticerooms(page, LOAD_PRACTICE_ROOM_NUMBER, this::setIsLoadNextPracticeroomSuccess);
        page = page + 1;
    }
}
