package com.example.moum.data.api;

import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.ChatSendResponse;
import com.example.moum.data.dto.SuccessResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApi {
    @POST("/api/chat/test/{id}")
    Call<ChatSendResponse> chatSend(
            @Path("id") int id,
            @Body ChatSendRequest chatSendRequest
    );
}
