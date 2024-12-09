package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.MoumApi;
import com.example.moum.data.api.TeamApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.MoumProcessRequest;
import com.example.moum.data.dto.MoumRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.dto.TeamRequest;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MoumRepository {
    private static MoumRepository instance;
    private final MoumApi moumApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    @RequiresApi(api = Build.VERSION_CODES.O)
    private MoumRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        moumApi = retrofitClient.create(MoumApi.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MoumRepository(RetrofitClientManager retrofitClientManager) {
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getAuthClient(null);
        moumApi = retrofitClient.create(MoumApi.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static MoumRepository getInstance(Application application) {
        if (instance == null) {
            instance = new MoumRepository(application);
        }
        return instance;
    }

    public void createMoum(Moum moum, ArrayList<File> moumProfiles, com.example.moum.utils.Callback<Result<Moum>> callback) {
        /*processing into DTO*/
        List<MultipartBody.Part> profileImages = new ArrayList<>();
        if (moumProfiles != null && !moumProfiles.isEmpty()) {
            for (File moumProfile : moumProfiles) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), moumProfile);
                MultipartBody.Part profileImage = MultipartBody.Part.createFormData("file", moumProfile.getName(), requestFile);
                profileImages.add(profileImage);
            }
        }
        MoumRequest moumRequest = new MoumRequest(moum.getMoumName(), moum.getMoumDescription(), moum.getPerformLocation(), moum.getStartDate(),
                moum.getEndDate(), moum.getPrice(), moum.getLeaderId(), moum.getTeamId(), moum.getMembers(), moum.getRecords(), moum.getMusic(),
                moum.getGenre());
        Call<SuccessResponse<Moum>> result = moumApi.createMoum(profileImages, moumRequest);

        result.enqueue(new retrofit2.Callback<SuccessResponse<Moum>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Moum>> call, Response<SuccessResponse<Moum>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Moum> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Moum moum = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Moum> result = new Result<>(validation, moum);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Moum> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Moum>> call, Throwable t) {
                Result<Moum> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadMoum(Integer moumId, com.example.moum.utils.Callback<Result<Moum>> callback) {
        Call<SuccessResponse<Moum>> result = moumApi.loadMoum(moumId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Moum>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Moum>> call, Response<SuccessResponse<Moum>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Moum> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Moum moum = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Moum> result = new Result<>(validation, moum);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Moum> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Moum>> call, Throwable t) {
                Result<Moum> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadMoumsOfTeam(Integer teamId, com.example.moum.utils.Callback<Result<List<Moum>>> callback) {
        Call<SuccessResponse<List<Moum>>> result = moumApi.loadMoumsOfTeam(teamId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Moum>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Moum>>> call, Response<SuccessResponse<List<Moum>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Moum>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Moum> moums = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Moum>> result = new Result<>(validation, moums);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Moum>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<List<Moum>>> call, Throwable t) {
                Result<List<Moum>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadMoumsAll(com.example.moum.utils.Callback<Result<List<Moum>>> callback) {
        Call<SuccessResponse<List<Moum>>> result = moumApi.loadMoumsOfMe();
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Moum>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Moum>>> call, Response<SuccessResponse<List<Moum>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Moum>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Moum> moums = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Moum>> result = new Result<>(validation, moums);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Moum>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<List<Moum>>> call, Throwable t) {
                Result<List<Moum>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void updateMoum(Integer moumId, Moum moum, ArrayList<File> moumProfiles, com.example.moum.utils.Callback<Result<Moum>> callback) {
        /*processing into DTO*/
        Log.e(TAG, "moumProfiles: " + moumProfiles.toString() + " moumProfiles size: " + moumProfiles.size());
        List<MultipartBody.Part> profileImages = new ArrayList<>();
        ;
        if (moumProfiles != null && !moumProfiles.isEmpty()) {
            Log.e(TAG, "if문 들어옴");
            for (File moumProfile : moumProfiles) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), moumProfile);
                MultipartBody.Part profileImage = MultipartBody.Part.createFormData("file", "temp_image.jpg", requestFile);
                profileImages.add(profileImage);
            }
        }

        if (profileImages.isEmpty()) {
            MultipartBody.Part profileImage = MultipartBody.Part.createFormData("file", null, RequestBody.create(null, new byte[0]));
            profileImages.add(profileImage);
        }
        //Log.e(TAG, "profileImages: " +  profileImages.toString() + " profileImages size: " + profileImages.size());
        MoumRequest moumRequest = new MoumRequest(moum.getMoumName(), moum.getMoumDescription(), moum.getPerformLocation(), moum.getStartDate(),
                moum.getEndDate(), moum.getPrice(), moum.getLeaderId(), moum.getTeamId(), moum.getMembers(), moum.getRecords(), moum.getMusic(),
                moum.getGenre());
        Call<SuccessResponse<Moum>> result = moumApi.updateMoum(moumId, profileImages, moumRequest);

        result.enqueue(new retrofit2.Callback<SuccessResponse<Moum>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Moum>> call, Response<SuccessResponse<Moum>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Moum> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Moum moum = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Moum> result = new Result<>(validation, moum);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Moum> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Moum>> call, Throwable t) {
                Result<Moum> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void deleteMoum(Integer moumId, com.example.moum.utils.Callback<Result<Moum>> callback) {
        Call<SuccessResponse<Moum>> result = moumApi.deleteMoum(moumId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Moum>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Moum>> call, Response<SuccessResponse<Moum>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Moum> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Moum moum = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Moum> result = new Result<>(validation, moum);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Moum> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Moum>> call, Throwable t) {
                Result<Moum> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void finishMoum(Integer moumId, com.example.moum.utils.Callback<Result<Moum>> callback) {
        Call<SuccessResponse<Moum>> result = moumApi.finishMoum(moumId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Moum>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Moum>> call, Response<SuccessResponse<Moum>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Moum> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Moum moum = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Moum> result = new Result<>(validation, moum);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Moum> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Moum>> call, Throwable t) {
                Result<Moum> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void reopenMoum(Integer moumId, com.example.moum.utils.Callback<Result<Moum>> callback) {
        Call<SuccessResponse<Moum>> result = moumApi.reopenMoum(moumId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Moum>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Moum>> call, Response<SuccessResponse<Moum>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Moum> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Moum moum = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Moum> result = new Result<>(validation, moum);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Moum> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Moum>> call, Throwable t) {
                Result<Moum> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void updateProcessMoum(Integer moumId, Moum.Process process, com.example.moum.utils.Callback<Result<Moum>> callback) {
        MoumProcessRequest request = new MoumProcessRequest(process.getRecruitStatus(), process.getChatroomStatus(), process.getPracticeroomStatus(),
                process.getPerformLocationStatus(), process.getPromoteStatus(), process.getPaymentStatus());
        Call<SuccessResponse<Moum>> result = moumApi.updateProcessMoum(moumId, request);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Moum>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Moum>> call, Response<SuccessResponse<Moum>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Moum> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Moum moum = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Moum> result = new Result<>(validation, moum);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Moum> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Moum>> call, Throwable t) {
                Result<Moum> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}
