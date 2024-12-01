package com.example.moum.data.api;

import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.ChatroomCreateRequest;
import com.example.moum.data.dto.ChatroomDeleteRequest;
import com.example.moum.data.dto.ChatroomUpdateRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ChatroomApi {
    @GET("/api/chatroom/{chatroomId}")
    Call<SuccessResponse<Chatroom>> loadChatroom(
            @Path("chatroomId") Integer chatroomId
    );

    @GET("/api/chatroom/member/{memberId}")
    Call<SuccessResponse<List<Chatroom>>> loadChatrooms(
            @Path("memberId") Integer memberId
    );

    @GET("/api/chatroom/{chatroomId}/member")
    Call<SuccessResponse<List<Member>>> loadMembersOfChatroom(
            @Path("chatroomId") Integer chatroomId
    );

    @Multipart
    @POST("/api/chatroom")
    Call<SuccessResponse<Chatroom>> createChatroom(
            @Part MultipartBody.Part chatroomProfile,
            @Part("chatroomInfo") ChatroomCreateRequest chatroomCreateRequest
    );

    @Multipart
    @PATCH("/api/chatroom/{chatroomId}")
    Call<SuccessResponse<Chatroom>> updateChatroom(
            @Path("chatroomId") Integer chatroomId,
            @Part MultipartBody.Part chatroomProfile,
            @Part("chatroomInfo") ChatroomUpdateRequest chatroomUpdateRequest
    );

    @DELETE("/api/chatroom/{chatroomId}/member")
    Call<SuccessResponse<Chatroom>> deleteMembers(
            @Path("chatroomId") Integer chatroomId,
            @Body ChatroomDeleteRequest chatroomDeleteRequest
    );

}
