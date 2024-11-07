package com.example.moum.data.api;

import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Chat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApi {
    @POST("/api/chat/{chatroomId}")
    Call<SuccessResponse<Chat>> chatSend(
            @Path("chatroomId") int chatroomId,
            @Body ChatSendRequest chatSendRequest
    );
}
