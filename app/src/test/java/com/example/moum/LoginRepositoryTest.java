package com.example.moum;

import static org.junit.Assert.assertEquals;

import com.example.moum.repository.LoginRepository;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

@RunWith(JUnit4.class)
public class LoginRepositoryTest {

    private MockWebServer mockWebServer;
    private LoginRepository loginRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        loginRepository = new LoginRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testLogin_ShouldReturnSuccessResponse_WhenLoginSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String access =
                "eyJhbGciOiJIUzI1NiJ9"
                        +
                        ".eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoidGVzdHVzZXIzIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3Mjg5MDM3NDksImV4cCI6MTcyODkwNDM0OX0.ozImUNza_VC3ATdZXbst19DlUARvtEloGH0G-iShaDw";
        String setCookie =
                "refresh=eyJhbGciOiJIUzI1NiJ9"
                        +
                        ".eyJjYXRlZ29yeSI6InJlZnJlc2giLCJ1c2VybmFtZSI6InNvc29uZ2hhMyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MzM1NzE4NjYsImV4cCI6MTczMzU3MjcwOH0.GLIcnHzzkrWFeMJXvGbIktZ5Bq1VcYJKuoK4zSOY5q4; Max-Age=86400; Expires=Sun, 08 Dec 2024 11:44:26 GMT; HttpOnly";
        String refresh =
                "eyJhbGciOiJIUzI1NiJ9"
                        +
                        ".eyJjYXRlZ29yeSI6InJlZnJlc2giLCJ1c2VybmFtZSI6InNvc29uZ2hhMyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MzM1NzE4NjYsImV4cCI6MTczMzU3MjcwOH0.GLIcnHzzkrWFeMJXvGbIktZ5Bq1VcYJKuoK4zSOY5q4";
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-M002\",\n"
                + "    \"message\": \"로그인 되었습니다.\",\n"
                + "    \"data\": {\n"
                + "        \"name\": \"소성하\",\n"
                + "        \"id\": \"1\"\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("access", access)
                .addHeader("Set-Cookie", setCookie)
                .setBody(mockResponse));

        // When
        loginRepository.login("testuser", "asdfg1345!", result -> {
            try {
                assertEquals(Validation.LOGIN_SUCCESS, result.getValidation());
                assertEquals(access, result.getData().getAccess());
                assertEquals(refresh, result.getData().getRefresh());
                assertEquals((Integer) 1, result.getData().getId());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/login", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLogin_ShouldReturnFailureResponse_WhenLoginFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-A001\",\n"
                + "    \"message\": \"아이디 또는 비밀번호가 유효하지 않습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        loginRepository.login("test@example.com", "asdfg1345!", result -> {
            try {
                assertEquals(Validation.LOGIN_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/login", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLogin_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        loginRepository.login("test@example.com", "asdfg1345!", result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/login", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLogout_ShouldReturnSuccessResponse_WhenLogoutSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-M004\",\n"
                + "    \"message\": \"로그아웃 되었습니다.\",\n"
                + "    \"data\": {\n"
                + "        \"username\": \"sosongha3\"\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        loginRepository.logout(result -> {
            try {
                assertEquals(Validation.LOGOUT_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/logout", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLogout_ShouldReturnFailureResponse_WhenLogoutFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        // Given
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-M006\",\n"
                + "    \"message\": \"이미 로그아웃 하였습니다. 먼저 로그인을 해주세요.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setHeader("Content-Type", "charset=UTF-8")
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        loginRepository.logout(result -> {
            try {
                assertEquals(Validation.LOGOUT_ALREADY, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/logout", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLogout_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        loginRepository.logout(result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/logout", request.getPath());
        assertEquals("POST", request.getMethod());
    }
}
