package com.example.moum;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.moum.data.entity.Chat;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;

@RunWith(AndroidJUnit4.class)
public class ChatRepositoryTest {
    private MockWebServer mockWebServer;
    private RetrofitClientManager retrofitClientManager;
    private Retrofit retrofitClient;
    private ChatRepository chatRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        chatRepository = new ChatRepository(ApplicationProvider.getApplicationContext(), retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @DisplayName("chatSend 성공 시 응답 반환")
    @Test
    public void testChatSendSuccess() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Chat chat = new Chat("sender", "message", 0, LocalDateTime.now());
        String mockResponse =
                "{ \"status\": 200, \"code\": \"S-CH001\", \"message\": \"채팅 메세지 전송 성공\", \"data\": { \"sender\": \"testuser\", \"message\": "
                        + "\"Message Contents\", \"chatroomId\": 0, \"timestamp\": \"2024-10-28T20:29:22.6351588\"} }";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .setBodyDelay(0, TimeUnit.MILLISECONDS));

        // When
        chatRepository.chatSend(chat, result -> {
            try {
                assertEquals(Validation.CHAT_POST_SUCCESS, result.getValidation());
                assertEquals("message", result.getData().getMessage());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/chatSend", request.getPath());
        assertEquals("POST", request.getMethod());
    }
}
