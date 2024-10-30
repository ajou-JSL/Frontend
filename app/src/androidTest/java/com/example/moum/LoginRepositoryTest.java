package com.example.moum;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.moum.data.api.LoginApi;
import com.example.moum.repository.LoginRepository;
import com.example.moum.repository.RetrofitClientManager;
import com.example.moum.utils.Validation;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import retrofit2.Retrofit;

@RunWith(AndroidJUnit4.class)
public class LoginRepositoryTest {

    private MockWebServer mockWebServer;
    private Retrofit retrofitClient;
    private LoginRepository loginRepository;
    private LoginApi loginApi;

    @Before
    public void setUp() throws Exception {

        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        retrofitClient = new RetrofitClientManager().getClient();
        loginApi = retrofitClient.create(LoginApi.class);

        loginRepository = new LoginRepository(retrofitClient, loginApi);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @DisplayName("로그인 성공 시 토큰 반환")
    @Test
    public void testLoginSuccess() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String access = "eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoidGVzdHVzZXIzIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3Mjg5MDM3NDksImV4cCI6MTcyODkwNDM0OX0.ozImUNza_VC3ATdZXbst19DlUARvtEloGH0G-iShaDw";
        String refresh = "eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6InJlZnJlc2giLCJ1c2VybmFtZSI6InRlc3R1c2VyMyIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzI4OTAzNzQ5LCJleHAiOjE3Mjg5OTAxNDl9.KY5BNqBEe8lfaZZ76pTvgxK5wlt7BPVfpuuoE2We7pQ";
        String mockResponse = "{ \"status\": 200, \"code\": \"0000\", \"message\": \"로그인 되었습니다.\", \"data\": \"testuser\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("access", access)
                .addHeader("Set-Cookie", refresh)
                .setBody(mockResponse));

        // When
        loginRepository.login("test@example.com", "asdfg1345!", result -> {

            try {
                assertEquals(Validation.VALID_ALL, result.getValidation());
                assertEquals(access, result.getData().getAccess());
                assertEquals(refresh, result.getData().getRefresh());
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

    @DisplayName("로그인 실패 시 결과 반환")
    @Test
    public void testLoginFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String mockResponse = "{ \"status\": 400, \"code\": \"A001\", \"message\": \"아이디 또는 비밀번호가 유효하지 않습니다.\", \"errors\": []}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        loginRepository.login("test@example.com", "asdfg1345!", result -> {

            try{
                assertEquals(Validation.LOGIN_FAILED, result.getValidation());
            }finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/login", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @DisplayName("네트워크 연결 실패 시, 결과 반환")
    @Test
    public void testLoginNetworkFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        loginRepository.login("test@example.com", "asdfg1345!", result -> {

            try{
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            }finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/login", request.getPath());
        assertEquals("POST", request.getMethod());
    }
}
