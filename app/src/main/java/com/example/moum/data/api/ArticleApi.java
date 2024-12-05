package com.example.moum.data.api;

import com.example.moum.data.dto.ArticleFilterRequest;
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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApi {
    @GET("/api/articles-all/search")
    Call<SuccessResponse<List<Article>>> searchArticles(
            @Query("keyword") String keyword,
            @Query("category") String category,
            @Query("page") Integer page,
            @Query("size") Integer size
    );

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

    @GET("api/articles/search")
    Call<SuccessResponse<List<Article>>> searchArticlesFilters(
            @Query("page") Integer page,
            @Query("size") Integer size,
            @Query("searchDto") ArticleFilterRequest articleFilterRequest
    );

    @Multipart
    @POST("/api/articles")
    Call<SuccessResponse<Article>> createArticle(
            @Part List<MultipartBody.Part> file,
            @Part("articleRequestDto") ArticleRequest articleRequest
    );

    @GET("/api/articles/member/{memberId}/likes/{articleId}")
    Call<SuccessResponse<Like>> loadLike(
            @Path("memberId") int memberId,
            @Path("articleId") int articleId
    );

    @PUT("api/articles/member/{memberId}/likes/{articleId}")
    Call<SuccessResponse<Like>> postLike(
            @Path("memberId") int memberId,
            @Path("articleId") int articleId
    );

    @POST("/api/comments/{articleId}")
    Call<SuccessResponse<Comment>> createComment(
            @Path("articleId") int articleId,
            @Body CommentRequest commentRequest
    );

    @DELETE("/api/comments/{commentId}")
    Call<SuccessResponse<Comment>> deleteComment(
            @Path("commentId") int commentId
    );

    @GET("/api/comments/{articleId}")
    Call<SuccessResponse<List<Comment>>> getArticleComments(
            @Path("articleId") int articleId
    );

    @DELETE("/api/articles/{articleId}")
    Call<SuccessResponse<Article>> deleteArticle(
            @Path("articleId") int articleId
    );

}
