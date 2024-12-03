package com.example.moum.viewmodel.moum;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moum.data.dto.SearchPerformHallArgs;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.PracticeNPerformRepository;
import com.naver.maps.geometry.LatLng;

import java.util.List;

public class MoumFindPerformanceHallViewModel extends AndroidViewModel {
    private PracticeNPerformRepository practiceNPerformRepository;
    private final MutableLiveData<Result<List<PerformanceHall>>> isQueryPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isQueryNextPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isLoadPerformanceHallSuccess = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PerformanceHall>>> isLoadNextPerformanceHallSuccess = new MutableLiveData<>();
    private final int LOAD_NUMBER = 10; //한번에 10개 조회
    private static Integer page = 1;
    private Integer recentPageNumber = 0;
    private boolean isQuerying = false;
    private String name;
    private LatLng latLng;
    private final SearchPerformHallArgs args = new SearchPerformHallArgs();

    public MoumFindPerformanceHallViewModel(Application application){
        super(application);
        practiceNPerformRepository = PracticeNPerformRepository.getInstance(application);
    }

    public MutableLiveData<Result<List<PerformanceHall>>> getIsLoadNextPerformanceHallSuccess() {
        return isLoadNextPerformanceHallSuccess;
    }

    public MutableLiveData<Result<List<PerformanceHall>>> getIsLoadPerformanceHallSuccess() {
        return isLoadPerformanceHallSuccess;
    }

    public MutableLiveData<Result<List<PerformanceHall>>> getIsQueryPerformanceHallSuccess() {
        return isQueryPerformanceHallSuccess;
    }

    public MutableLiveData<Result<List<PerformanceHall>>> getIsQueryNextPerformanceHallSuccess() {
        return isQueryNextPerformanceHallSuccess;
    }

    public SearchPerformHallArgs getArgs() {
        return args;
    }

    public boolean isQuerying() {
        return isQuerying;
    }

    public void setIsQueryPerformanceHallSuccess(Result<List<PerformanceHall>> isQueryPerformanceHallSuccess){
        this.isQueryPerformanceHallSuccess.setValue(isQueryPerformanceHallSuccess);
    }

    public void setIsLoadPerformanceHallSuccess(Result<List<PerformanceHall>> isLoadPerformanceHallSuccess){
        this.isLoadPerformanceHallSuccess.setValue(isLoadPerformanceHallSuccess);
    }

    public void setIsLoadNextPerformanceHallSuccess(Result<List<PerformanceHall>> isLoadNextPerformanceHallSuccess){
        this.isLoadNextPerformanceHallSuccess.setValue(isLoadNextPerformanceHallSuccess);
    }

    public void setIsQueryNextPerformanceHallSuccess(Result<List<PerformanceHall>> isQueryNextPerformanceHallSuccess){
        this.isQueryNextPerformanceHallSuccess.setValue(isQueryNextPerformanceHallSuccess);
    }

    public void clearArgs(){
        this.args.clear();
    }

    public void setName(String name) {
        this.args.setName(name);
    }

    public void setLatLng(LatLng latLng){
        this.args.setLatitude(latLng.latitude);
        this.args.setLongitude(latLng.longitude);
    }

    public void queryPerformanceHallWithArgs(SearchPerformHallArgs args){
        this.args.setSortBy(args.getSortBy());
        this.args.setOrderBy(args.getOrderBy());
        if(args.getMinPrice() != -1) this.args.setMinPrice(args.getMinPrice());
        if(args.getMaxPrice() != -1) this.args.setMaxPrice(args.getMaxPrice());
        if(args.getMinCapacity() != -1) this.args.setMinCapacity(args.getMinCapacity());
        if(args.getMaxCapacity() != -1) this.args.setMaxCapacity(args.getMaxCapacity());
        if(args.getMinHallSize() != -1) this.args.setMinHallSize(args.getMinHallSize());
        if(args.getMaxHallSize() != -1) this.args.setMaxHallSize(args.getMaxHallSize());
        if(args.getMinStand() != -1) this.args.setMinStand(args.getMinStand());
        if(args.getMaxStand() != -1) this.args.setMaxStand(args.getMaxStand());
        if(args.getHasPiano() != null) this.args.setHasPiano(args.getHasPiano());
        if(args.getHasAmp() != null) this.args.setHasAmp(args.getHasAmp());
        if(args.getHasSpeaker() != null) this.args.setHasSpeaker(args.getHasSpeaker());
        if(args.getHasMic() != null) this.args.setHasMic(args.getHasMic());
        if(args.getHasDrums() != null) this.args.setHasDrums(args.getHasDrums());
        queryPerformanceHall();
    }

    public void queryPerformanceHallWithName(String name){
        setName(name);
        queryPerformanceHall();
    }

    public void queryPerformanceHall(){
        clearPage();
        if(args.getLatitude() == null || args.getLongitude() == null) return;
        isQuerying = true;

        /*goto repository*/
        practiceNPerformRepository.searchPerformHalls(page, LOAD_NUMBER, args, this::setIsQueryPerformanceHallSuccess);
        page = page + 1;
    }

    public void queryNextPerformanceHall(){
        if(args.getLatitude() == null || args.getLongitude() == null) return;
        isQuerying = true;
        if(recentPageNumber < LOAD_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PRACTICE_ROOM_NUMBER 적다면, 그만 불러오기

        /*goto repository*/
        practiceNPerformRepository.searchPerformHalls(page, LOAD_NUMBER, args, this::setIsQueryNextPerformanceHallSuccess);
        page = page + 1;
    }

    public void setRecentPageNumber(Integer recentPageNumber) {
        this.recentPageNumber = recentPageNumber;
    }

    public void clearPage(){
        page = 1;
        recentPageNumber = 0;
        name = null;
        latLng = null;
    }

    public void loadPerformanceHall(){
        practiceNPerformRepository.getPerformHalls(page, LOAD_NUMBER, this::setIsLoadPerformanceHallSuccess);
        page = page + 1;
    }


    public void loadNextPerformanceHall(){
        if(recentPageNumber < LOAD_NUMBER) return; //이전에 불러온 이미지들이 LOAD_PRACTICE_ROOM_NUMBER 적다면, 그만 불러오기
        practiceNPerformRepository.getPerformHalls(page, LOAD_NUMBER, this::setIsLoadNextPerformanceHallSuccess);
        page = page + 1;
    }
}
