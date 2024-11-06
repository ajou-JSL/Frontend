package com.example.moum.data.api;

import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.ChatroomCreateRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Group;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ChatApi {
    @POST("/api/chat/{chatroomId}")
    Call<SuccessResponse<Chat>> chatSend(
            @Path("chatroomId") int chatroomId,
            @Body ChatSendRequest chatSendRequest
    );
}
