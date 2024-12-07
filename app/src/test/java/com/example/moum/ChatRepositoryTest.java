package com.example.moum;

import static org.junit.Assert.assertEquals;

import com.example.moum.data.entity.Chat;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import retrofit2.Retrofit;

@RunWith(JUnit4.class)
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
        String fakeAccessToken = "fake-access-token";
        chatRepository = new ChatRepository(retrofitClientManager, fakeAccessToken);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testChatSend_ShouldReturnSuccessResponse_WhenChatSendSuccess() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Chat chat = new Chat("sender", "message", 0, LocalDateTime.now());
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-CH001\",\n"
                + "    \"message\": \"채팅 메시지 전송 성공\",\n"
                + "    \"data\": {\n"
                + "        \"sender\": \"testuser\",\n"
                + "        \"message\": \"Message Contents\",\n"
                + "        \"chatroomId\": 0,\n"
                + "        \"timestamp\": \"2024-10-28T20:29:22.6351588\"\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        chatRepository.chatSend(chat, result -> {
            try {
                assertEquals(Validation.CHAT_POST_SUCCESS, result.getValidation());
                assertEquals("Message Contents", result.getData().getMessage());
            } finally {
                latch.countDown();
            }
        });
        latch.await(10, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        Assert.assertEquals("/api/chat/0", request.getPath());
        Assert.assertEquals("POST", request.getMethod());
    }

    @Test
    public void testChatSend_ShouldReturnFailureResponse_WhenChatSendFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Chat chat = new Chat("sender", "message", 0, LocalDateTime.now());
        String mockResponse =
                "{\n"
                        + "\t\"status\": 400,\n"
                        + "\t\"code\": \"F-CH001\",\n"
                        + "\t\"message\": \"채팅 메시지 전송 실패\"\n"
                        + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        chatRepository.chatSend(chat, result -> {
            try {
                assertEquals(Validation.CHAT_POST_FAIL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(10, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        Assert.assertEquals("/api/chat/0", request.getPath());
        Assert.assertEquals("POST", request.getMethod());
    }

    @Test
    public void testChatSend_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Chat chat = new Chat("sender", "message", 0, LocalDateTime.now());
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        chatRepository.chatSend(chat, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(10, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        Assert.assertEquals("/api/chat/0", request.getPath());
        Assert.assertEquals("POST", request.getMethod());
    }
}
