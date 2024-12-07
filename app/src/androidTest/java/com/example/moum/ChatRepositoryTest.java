package com.example.moum;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

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
        String fakeAccessToken = "fake-access-token";
        chatRepository = new ChatRepository(retrofitClientManager, fakeAccessToken);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    boolean onMessage = true;

    @Test
    public void testReceiveRecentChat_ShouldReturnSuccessResponse_WhenReceiveRecentChatSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        onMessage = true;

        // Given
        Integer chatroomId = 0;
        String mockResponse = ""
                + "event: message\n"
                + "data: {\"sender\": \"sender\", \"message\": \"Test message! Hello!\", \"chatroomId\": 0, \"timestamp\": "
                + "\"2024-10-28T00:20:12:126\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "text/event-stream")
                .setChunkedBody(
                        "event: message\n" +
                                "data: {\"sender\": \"sender\", \"message\": \"Test message! Hello!\", \"chatroomId\": 0, \"timestamp\": "
                                + "\"2024-10-28T00:20:12.126\"}\n\n",
                        64)
                .setSocketPolicy(SocketPolicy.KEEP_OPEN));

        // When
        chatRepository.receiveRecentChat(chatroomId, result -> {
            try {
                if (onMessage) { // 처음엔 메세지 수신
                    assertEquals(Validation.CHAT_RECEIVE_SUCCESS, result.getValidation());
                    assertEquals("Test message! Hello!", result.getData().getMessage());
                    onMessage = false;
                } else { // 다음엔 stream 닫히므로 fail 수신
                    assertEquals(Validation.CHAT_RECEIVE_FAIL, result.getValidation());
                }
            } finally {
                latch.countDown();
            }
        });
        latch.await();

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        Assert.assertEquals("/api/chat/0", request.getPath());
        Assert.assertEquals("text/event-stream", request.getHeader("Accept"));
        Assert.assertEquals("GET", request.getMethod());
    }

    @Test
    public void testReceiveRecentChat_ShouldReturnFailureResponse_WhenReceiveRecentChatFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer chatroomId = 0;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AT_END));

        // When
        chatRepository.receiveRecentChat(chatroomId, result -> {
            try {
                assertEquals(Validation.CHAT_RECEIVE_FAIL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await();

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        Assert.assertEquals("/api/chat/0", request.getPath());
        Assert.assertEquals("text/event-stream", request.getHeader("Accept"));
        Assert.assertEquals("GET", request.getMethod());
    }
}
