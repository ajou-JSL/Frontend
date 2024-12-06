package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ProfileApi;
import com.example.moum.data.api.ReportApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.MemberProfileUpdateRequest;
import com.example.moum.data.dto.ReportRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportRepository {
    private static ReportRepository instance;
    private final ReportApi reportApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private ReportRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        reportApi = retrofitClient.create(ReportApi.class);
    }

    public ReportRepository(RetrofitClientManager retrofitClientManager, ReportApi reportApi) {
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.reportApi = reportApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static ReportRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ReportRepository(application);
        }
        return instance;
    }

    public void reportMember(Integer reported, Integer reporter, String type, String details,
            com.example.moum.utils.Callback<Result<ReportMember>> callback) {
        ReportRequest reportRequest = new ReportRequest(reporter, type, details);
        Call<SuccessResponse<ReportMember>> result = reportApi.reportMember(reported, reportRequest);
        result.enqueue(new retrofit2.Callback<SuccessResponse<ReportMember>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<ReportMember>> call, Response<SuccessResponse<ReportMember>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<ReportMember> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    ReportMember reportMember = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<ReportMember> result = new Result<>(validation, reportMember);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<ReportMember> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<ReportMember>> call, Throwable t) {
                Result<ReportMember> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadReportMember(Integer reportId, com.example.moum.utils.Callback<Result<ReportMember>> callback) {
        Call<SuccessResponse<ReportMember>> result = reportApi.loadReportMember(reportId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<ReportMember>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<ReportMember>> call, Response<SuccessResponse<ReportMember>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<ReportMember> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    ReportMember reportMember = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<ReportMember> result = new Result<>(validation, reportMember);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<ReportMember> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<ReportMember>> call, Throwable t) {
                Result<ReportMember> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadReportMembers(Integer reporterId, Integer page, Integer size,
            com.example.moum.utils.Callback<Result<List<ReportMember>>> callback) {
        Call<SuccessResponse<List<ReportMember>>> result = reportApi.loadReportMembers(reporterId, page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<ReportMember>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<ReportMember>>> call, Response<SuccessResponse<List<ReportMember>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<ReportMember>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<ReportMember> reportMembers = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<ReportMember>> result = new Result<>(validation, reportMembers);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<ReportMember>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<List<ReportMember>>> call, Throwable t) {
                Result<List<ReportMember>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void reportTeam(Integer reported, Integer reporter, String type, String details,
            com.example.moum.utils.Callback<Result<ReportTeam>> callback) {
        ReportRequest reportRequest = new ReportRequest(reporter, type, details);
        Call<SuccessResponse<ReportTeam>> result = reportApi.reportTeam(reported, reportRequest);
        result.enqueue(new retrofit2.Callback<SuccessResponse<ReportTeam>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<ReportTeam>> call, Response<SuccessResponse<ReportTeam>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<ReportTeam> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    ReportTeam reportTeam = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<ReportTeam> result = new Result<>(validation, reportTeam);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<ReportTeam> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<ReportTeam>> call, Throwable t) {
                Result<ReportTeam> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadReportTeam(Integer reportId, com.example.moum.utils.Callback<Result<ReportTeam>> callback) {
        Call<SuccessResponse<ReportTeam>> result = reportApi.loadReportTeam(reportId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<ReportTeam>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<ReportTeam>> call, Response<SuccessResponse<ReportTeam>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<ReportTeam> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    ReportTeam reportTeam = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<ReportTeam> result = new Result<>(validation, reportTeam);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<ReportTeam> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<ReportTeam>> call, Throwable t) {
                Result<ReportTeam> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadReportTeams(Integer reporterId, Integer page, Integer size, com.example.moum.utils.Callback<Result<List<ReportTeam>>> callback) {
        Call<SuccessResponse<List<ReportTeam>>> result = reportApi.loadReportTeams(reporterId, page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<ReportTeam>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<ReportTeam>>> call, Response<SuccessResponse<List<ReportTeam>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<ReportTeam>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<ReportTeam> reportTeams = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<ReportTeam>> result = new Result<>(validation, reportTeams);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<ReportTeam>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<List<ReportTeam>>> call, Throwable t) {
                Result<List<ReportTeam>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void reportArticle(Integer reported, Integer reporter, String type, String details,
            com.example.moum.utils.Callback<Result<ReportArticle>> callback) {
        ReportRequest reportRequest = new ReportRequest(reporter, type, details);
        Call<SuccessResponse<ReportArticle>> result = reportApi.reportArticle(reported, reportRequest);
        result.enqueue(new retrofit2.Callback<SuccessResponse<ReportArticle>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<ReportArticle>> call, Response<SuccessResponse<ReportArticle>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<ReportArticle> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    ReportArticle reportArticle = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<ReportArticle> result = new Result<>(validation, reportArticle);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<ReportArticle> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<ReportArticle>> call, Throwable t) {
                Result<ReportArticle> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadReportArticle(Integer reportId, com.example.moum.utils.Callback<Result<ReportArticle>> callback) {
        Call<SuccessResponse<ReportArticle>> result = reportApi.loadReportArticle(reportId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<ReportArticle>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<ReportArticle>> call, Response<SuccessResponse<ReportArticle>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<ReportArticle> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    ReportArticle reportArticle = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<ReportArticle> result = new Result<>(validation, reportArticle);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<ReportArticle> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<ReportArticle>> call, Throwable t) {
                Result<ReportArticle> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadReportArticles(Integer reporterId, Integer page, Integer size,
            com.example.moum.utils.Callback<Result<List<ReportArticle>>> callback) {
        Call<SuccessResponse<List<ReportArticle>>> result = reportApi.loadReportArticles(reporterId, page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<ReportArticle>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<ReportArticle>>> call, Response<SuccessResponse<List<ReportArticle>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<ReportArticle>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<ReportArticle> reportArticles = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<ReportArticle>> result = new Result<>(validation, reportArticles);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<ReportArticle>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<List<ReportArticle>>> call, Throwable t) {
                Result<List<ReportArticle>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }
}
