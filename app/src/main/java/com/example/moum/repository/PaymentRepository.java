package com.example.moum.repository;

import android.app.Application;
import android.util.Log;

import com.example.moum.data.api.PaymentApi;
import com.example.moum.data.api.PromoteApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SettlementRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Settlement;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentRepository {
    private static PaymentRepository instance;
    private final PaymentApi paymentApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private PaymentRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        paymentApi = retrofitClient.create(PaymentApi.class);
    }

    public PaymentRepository(RetrofitClientManager retrofitClientManager, PaymentApi paymentApi) {
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.paymentApi = paymentApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static PaymentRepository getInstance(Application application) {
        if (instance == null) {
            instance = new PaymentRepository(application);
        }
        return instance;
    }

    public void createSettlement(Integer moumId, Settlement settlement, com.example.moum.utils.Callback<Result<Settlement>> callback) {
        SettlementRequest settlementRequest = new SettlementRequest(settlement.getSettlementName(), settlement.getFee(), moumId);
        Call<SuccessResponse<Settlement>> result = paymentApi.createSettlement(moumId, settlementRequest);
        result.enqueue(new Callback<SuccessResponse<Settlement>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Settlement>> call, Response<SuccessResponse<Settlement>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Settlement> responseBody = response.body();
                    assert responseBody != null;
                    Settlement loadedSettlement = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Settlement> result = new Result<>(validation, loadedSettlement);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Settlement> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Settlement>> call, Throwable t) {
                Result<Settlement> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadSettlements(Integer moumId, com.example.moum.utils.Callback<Result<List<Settlement>>> callback) {
        Call<SuccessResponse<List<Settlement>>> result = paymentApi.loadSettlements(moumId);
        result.enqueue(new Callback<SuccessResponse<List<Settlement>>>() {
            @Override
            public void onResponse(Call<SuccessResponse<List<Settlement>>> call, Response<SuccessResponse<List<Settlement>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Settlement>> responseBody = response.body();
                    assert responseBody != null;
                    List<Settlement> loadedSettlements = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Settlement>> result = new Result<>(validation, loadedSettlements);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Settlement>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<List<Settlement>>> call, Throwable t) {
                Result<List<Settlement>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void deleteSettlement(Integer moumId, Integer settlementId, com.example.moum.utils.Callback<Result<Settlement>> callback) {
        Call<SuccessResponse<Settlement>> result = paymentApi.deleteSettlement(moumId, settlementId);
        result.enqueue(new Callback<SuccessResponse<Settlement>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Settlement>> call, Response<SuccessResponse<Settlement>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Settlement> responseBody = response.body();
                    assert responseBody != null;
                    Settlement settlement = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Settlement> result = new Result<>(validation, settlement);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Settlement> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Settlement>> call, Throwable t) {
                Result<Settlement> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }
}
