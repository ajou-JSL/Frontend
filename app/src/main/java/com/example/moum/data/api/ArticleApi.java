package com.example.moum.data.api;

import com.example.moum.data.dto.ArticleRequest;
import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.CommentRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Comment;
import com.example.moum.data.entity.Like;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApi {
    @GET("/api/articles/hot")
    Call<SuccessResponse<List<Article>>> loadArticlesHot(
        @Query("page") Integer page,
        @Query("size") Integer size
    );

    @GET("/api/articles-all/category")
    Call<SuccessResponse<List<Article>>> loadArticlesCategory(
        @Query("keyword") String keyword,
        @Query("category") String category,
        @Query("page") Integer page,
        @Query("size") Integer size
    );

    @GET("/api/articles/{articleId}")
    Call<SuccessResponse<Article>> loadArticleDetail(
        @Path("articleId") Integer articleId
    );

    @Multipart
    @POST("/api/articles")
    Call<SuccessResponse<Article>> createArticle(
            @Part List<MultipartBody.Part> file,
            @Part("articleRequestDto") ArticleRequest articleRequest
    );

    @POST("/api/comments/{articleId}")
    Call<SuccessResponse<Comment>> createComment(
            @Path("articleId") int articleId,
            @Body CommentRequest commentRequest
    );

    @POST("api/articles/likes/{articleId}")
    Call<SuccessResponse<Like>> postLike(
            @Path("articleId") int articleId
    );
}
