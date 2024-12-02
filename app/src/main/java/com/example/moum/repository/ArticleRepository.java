package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ArticleApi;
import com.example.moum.data.dto.ArticleRequest;
import com.example.moum.data.dto.CommentRequest;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.data.entity.Like;
import com.example.moum.data.entity.Result;
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

    public void searchAllArticles(String keyword, Integer page, Integer size, com.example.moum.utils.Callback<Result<List<Article>>> callback) {
        Call<SuccessResponse<List<Article>>> result = articleApi.loadAllArticles(keyword, null, page, size);

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

    public void createArticle(Article article, ArrayList<File> files, com.example.moum.utils.Callback<Result<Article>> callback) {
        /*processing into DTO*/
        List<MultipartBody.Part> fileList = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
                MultipartBody.Part uploadfile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                fileList.add(uploadfile);
            }
            Log.e(TAG, "파일 있음");
        }
        Log.e(TAG, "파일 없음");
        /*RequestDto 처리 */
        ArticleRequest articleRequest = new ArticleRequest(article.getTitle(), article.getContent(), article.getCategory(), article.getGenre());
        Call<SuccessResponse<Article>> result = articleApi.createArticle(fileList, articleRequest);

        /* API 호출 */
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
                Log.e(TAG, "onFailure");
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

    public void postLike(int articleId, com.example.moum.utils.Callback<Result<Like>> callback) {
        Call<SuccessResponse<Like>> result = articleApi.postLike(articleId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Like>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Like>> call, Response<SuccessResponse<Like>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Like> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Like like = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Like> result = new Result<>(validation, like);
                    callback.onResult(result);
                } else {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Like> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<SuccessResponse<Like>> call, Throwable t) {
                Result<Like> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void deleteComment(int commentId, com.example.moum.utils.Callback<Result<Comment>> callback) {
        Call<SuccessResponse<Comment>> result = articleApi.deleteComment(commentId);

        result.enqueue(new retrofit2.Callback<SuccessResponse<Comment>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Comment>> call, Response<SuccessResponse<Comment>> response) {

                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Comment> responseBody = response.body();

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
