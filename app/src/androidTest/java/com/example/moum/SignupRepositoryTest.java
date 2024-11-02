package com.example.moum;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.moum.data.api.SignupApi;
import com.example.moum.data.entity.User;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.repository.SignupRepository;
import com.example.moum.utils.Validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import retrofit2.Retrofit;

@RunWith(AndroidJUnit4.class)
public class SignupRepositoryTest {

    private MockWebServer mockWebServer;
    private Retrofit retrofitClient;
    private SignupRepository signupRepository;
    private SignupApi signupApi;
    private Context context;

    @Before
    public void setUp() throws Exception {

        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        retrofitClient = new RetrofitClientManager().getClient();
        signupApi = retrofitClient.create(SignupApi.class);
        signupRepository = new SignupRepository(retrofitClient, signupApi);

        context = ApplicationProvider.getApplicationContext();
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @DisplayName("emailAuth 성공 시 결과 반환")
    @Test
    public void testEmailAuthSuccess() throws Exception {


        // Given
        CountDownLatch latch = new CountDownLatch(1);
        String mockResponse = "{ \"status\": 200, \"code\": \"E001\", \"message\": \"인증 이메일 발송 성공하였습니다.\", \"data\": \"test@gmail.com\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.emailAuth("test@gmail.com", result -> {

            try {
                assertEquals(Validation.VALID_ALL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/send-email", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @DisplayName("emailAuth 실패 시 결과 반환")
    @Test
    public void testEmailAuthFailure() throws Exception {

        // Given
        CountDownLatch latch = new CountDownLatch(1);
        String mockResponse = "{ \"status\": 400, \"code\": \"C002\", \"message\": \"invalid input type.\", \"errors\": [{\"field\": \"email\", \"value\": \"swo9sadff.ac.kr\",\"reason\": \"이메일 형식이 올바르지 않습니다.\"}]}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.emailAuth("test@gmail.com", result -> {

            try {
                assertEquals(Validation.EMAIL_NOT_FORMAL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/send-email", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @DisplayName("네트워크 연결 실패 시, 결과 반환")
    @Test
    public void testNetworkFailure() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        // Given
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        signupRepository.emailAuth("test@gmail.com", result -> {

            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/send-email", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @DisplayName("checkEmailCode 성공 시 결과 반환")
    @Test
    public void testCheckEmailCodeSuccess() throws Exception {

        // Given
        CountDownLatch latch = new CountDownLatch(1);
        String mockResponse = "{ \"status\": 200, \"code\": \"E001\", \"message\": \"이메일 인증 성공하였습니다.\", \"data\": null}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.checkEmailCode("test@gmail.com", "123456", result -> {

            try {
                assertEquals(Validation.VALID_ALL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/verify-code", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @DisplayName("checkEmailCode 실패 시 결과 반환")
    @Test
    public void testCheckEmailCodeFailure() throws Exception {

        // Given
        CountDownLatch latch = new CountDownLatch(1);
        String mockResponse = "{ \"status\": 400, \"code\": \"C006\", \"message\": \"유효하지 않은 데이터입니다.\", \"errors\": []}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.checkEmailCode("test@gmail.com", "123456", result -> {

            try {
                assertEquals(Validation.EMAIL_CODE_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/verify-code", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @DisplayName("User 정보만 입력 후, signup 성공 시 결과 반환")
    @Test
    public void testSignupInsertLittleSuccess() throws Exception {

        // Given
        CountDownLatch latch = new CountDownLatch(1);
        String mockResponse = "{ \"status\": 200, \"code\": \"M001\", \"message\": \"회원가입 되었습니다.\", \"data\": \"test@gmail.com\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));
        User user = new User();

        // When
        signupRepository.signup(user, result -> {

            try {
                assertEquals(Validation.VALID_ALL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/signup", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @DisplayName("프로필 이미지 업로드까지 한 후, signup 성공 시 결과 반환")
    @Test
    public void testSignupInsertAllSuccess() throws Exception {

        // Given
        CountDownLatch latch = new CountDownLatch(1);
        String mockResponse = "{ \"status\": 200, \"code\": \"M001\", \"message\": \"회원가입 되었습니다.\", \"data\": \"test@gmail.com\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));
        User user = new User();
        File mockFile = new File(context.getCacheDir(), "mock_image.jpg");
        try (FileOutputStream fos = new FileOutputStream(mockFile)) {
            fos.write("Test content".getBytes());
        }
        user.setProfileImage(mockFile);

        // When
        signupRepository.signup(user, result -> {

            try {
                assertEquals(Validation.VALID_ALL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/signup", request.getPath());
        assertEquals("POST", request.getMethod());
    }
}
