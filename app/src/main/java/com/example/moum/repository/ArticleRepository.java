package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ArticleApi;
import com.example.moum.data.api.TeamApi;
import com.example.moum.data.dto.ArticleRequest;
import com.example.moum.data.dto.CommentRequest;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.dto.TeamRequest;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.data.entity.Member;
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

public class ArticleRepository {
    private static ArticleRepository instance;
    private final ArticleApi articleApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private ArticleRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        articleApi = retrofitClient.create(ArticleApi.class);
    }

    public ArticleRepository(RetrofitClientManager retrofitClientManager, ArticleApi articleApi){
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.articleApi = articleApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static ArticleRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ArticleRepository(application);
        }
        return instance;
    }

    public void loadArticlesHot(Integer page, Integer size, com.example.moum.utils.Callback<Result<List<Article>>> callback){
       Call<SuccessResponse<List<Article>>> result = articleApi.loadArticlesHot(page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Article>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Article>>> call, Response<SuccessResponse<List<Article>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Article>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Article> articles = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Article>> result = new Result<>(validation, articles);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Article>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Article>>> call, Throwable t) {
                Result<List<Article>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    // 게시글 카테고리 가져오기
    public void loadArticlesCategory(String keyword, String category, Integer page, Integer size, com.example.moum.utils.Callback<Result<List<Article>>> callback){
        Call<SuccessResponse<List<Article>>> result = articleApi.loadArticlesCategory(keyword, category, page, size);

        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Article>>>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Article>>> call, Response<SuccessResponse<List<Article>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Article>> responseBody = response.body();

                    List<Article> articles = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Article>> result = new Result<>(validation, articles);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/

                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);

                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Article>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Article>>> call, Throwable t) {
                Result<List<Article>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadArticleDetail(Integer articleId, com.example.moum.utils.Callback<Result<Article>> callback) {
        Call<SuccessResponse<Article>> result = articleApi.loadArticleDetail(articleId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Article>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Article>> call, Response<SuccessResponse<Article>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Article> responseBody = response.body();
                    Article article = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Article> result = new Result<>(validation, article);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);

                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Article> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Article>> call, Throwable t) {
                Result<Article> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void createArticle(Article article, List<File> files, com.example.moum.utils.Callback<Result<Article>> callback) {
        /* 파일 처리 */
        // 여러 파일을 처리할 MultipartBody.Part 리스트 준비
        List<MultipartBody.Part> fileParts = new ArrayList<>();

        // 파일이 null이 아니고 비어 있지 않다면 파일을 하나씩 MultipartBody.Part로 변환하여 리스트에 추가
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                fileParts.add(filePart);
            }
        }

        /*RequestDto 처리 */
        ArticleRequest articleRequest = new ArticleRequest(article.getTitle(), article.getCategory(), article.getContent(), article.getGenre());

        /* API 호출 */
        Call<SuccessResponse<Article>> result = articleApi.createArticle(fileParts, articleRequest);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Article>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Article>> call, Response<SuccessResponse<Article>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Article> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Article article = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Article> result = new Result<>(validation, article);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Article> result = new Result<>(validation);
                            callback.onResult(result);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Article>> call, Throwable t) {
                Result<Article> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void createComment(int articleId, String content, com.example.moum.utils.Callback<Result<Comment>> callback) {
        Call<SuccessResponse<Comment>> result = articleApi.createComment(articleId, new CommentRequest(content));
        result.enqueue(new retrofit2.Callback<SuccessResponse<Comment>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Comment>> call, Response<SuccessResponse<Comment>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Comment> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Comment comment = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Comment> result = new Result<>(validation, comment);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Comment> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Comment>> call, Throwable t) {
                Result<Comment> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }
}
