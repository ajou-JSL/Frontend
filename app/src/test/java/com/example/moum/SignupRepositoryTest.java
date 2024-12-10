package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.net.Uri;

import com.example.moum.data.entity.SignupUser;
import com.example.moum.repository.SignupRepository;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.Validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

@RunWith(JUnit4.class)
public class SignupRepositoryTest {
    private MockWebServer mockWebServer;
    private SignupRepository signupRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        signupRepository = new SignupRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testEmailAuth_ShouldReturnSuccessResponse_WhenEmailAuthSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String email = "testmail@gmail.com";
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-E001\",\n"
                + "\t\"message\": \"인증 이메일 발송 성공하였습니다.\",\n"
                + "\t\"data\": \"test@gmail.com\"\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.emailAuth(email, result -> {
            try {
                assertEquals(Validation.EMAIL_AUTH_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/send-mail", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testEmailAuth_ShouldReturnFailureResponse_WhenEmailAuthFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String email = "testmail@gmail.com";
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C002\",\n"
                + "\t\"message\": \"invalid input type\",\n"
                + "\t\"errors\": [\n"
                + "\t\t{\n"
                + "\t\t\t\"field\": \"email\",\n"
                + "\t\t\t\"value\": \"testmail@gmail.com\",\n"
                + "\t\t\t\"reason\": \"이메일 형식이 올바르지 않습니다.\"\n"
                + "\t\t}\n"
                + "\t]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        signupRepository.emailAuth(email, result -> {
            try {
                assertEquals(Validation.INVALID_INPUT_VALUE, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/send-mail", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testEmailAuth_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String email = "testmail@gmail.com";
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        signupRepository.emailAuth(email, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/send-mail", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testEmailCode_ShouldReturnSuccessResponse_WhenEmailAuthSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String email = "testmail@gmail.com";
        String verifyCode = "123456";
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-E001\",\n"
                + "\t\"message\": \"이메일 인증 성공하였습니다.\",\n"
                + "\t\"data\": null\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.checkEmailCode(email, verifyCode, result -> {
            try {
                assertEquals(Validation.EMAIL_AUTH_SUCCESS, result.getValidation());
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

    @Test
    public void testEmailCode_ShouldReturnFailureResponse_WhenEmailCodeFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String email = "testmail@gmail.com";
        String verifyCode = "123456";
        String mockResponse = "{\n"
                + "\t\"status\": 422,\n"
                + "\t\"code\": \"F-E001\",\n"
                + "\t\"message\": \"이메일 인증이 실패하였습니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(422)
                .setBody(mockResponse));

        // When
        signupRepository.checkEmailCode(email, verifyCode, result -> {
            try {
                assertEquals(Validation.EMAIL_AUTH_FAILED, result.getValidation());
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

    @Test
    public void testEmailCode_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String email = "testmail@gmail.com";
        String verifyCode = "123456";
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        signupRepository.checkEmailCode(email, verifyCode, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
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

    @Test
    public void testSignup_ShouldReturnSuccessResponse_WhenSignupSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        SignupUser signupUser = new SignupUser();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        signupUser.setProfileImage(file);
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-M001\",\n"
                + "\t\"message\": \"회원가입 되었습니다.\",\n"
                + "\t\"data\": \"testuser\"\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.signup(signupUser, result -> {
            try {
                assertEquals(Validation.SIGNUP_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/join", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testSignup_ShouldReturnFailureResponse_WhenSignupFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        SignupUser signupUser = new SignupUser();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        signupUser.setProfileImage(file);
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C001\",\n"
                + "\t\"message\": \"INTERNAL_SERVER_ERROR\",\n"
                + "\t\"data\": \"\"\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.signup(signupUser, result -> {
            try {
                assertEquals(Validation.INTERNAL_SERVER_ERROR, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/join", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testSignup_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        SignupUser signupUser = new SignupUser();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        signupUser.setProfileImage(file);
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        signupRepository.signup(signupUser, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/join", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testSignout_ShouldReturnSuccessResponse_WhenSignoutSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-M006\",\n"
                + "\t\"message\": \"회원 탈퇴 완료\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"id\": 3,\n"
                + "\t\t\"name\": \"이승우\",\n"
                + "\t\t\"username\": \"이승우\",\n"
                + "\t\t\"profileDescription\": \"어드민 관리자용 계정\",\n"
                + "\t\t\"profileImageUrl\": null,\n"
                + "\t\t\"exp\": 1,\n"
                + "\t\t\"genres\": [],\n"
                + "\t\t\"tier\": \"BRONZE\",\n"
                + "\t\t\"videoUrl\": null,\n"
                + "\t\t\"memberRecords\": [],\n"
                + "\t\t\"moumRecords\": []\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        signupRepository.signout(result -> {
            try {
                assertEquals(Validation.SIGNOUT_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/signout", request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testSignout_ShouldReturnFailureResponse_WhenSignoutFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C001\",\n"
                + "\t\"message\": \"INTERNAL_SERVER_ERROR\",\n"
                + "\t\"data\": \"\"\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        signupRepository.signout(result -> {
            try {
                assertEquals(Validation.INTERNAL_SERVER_ERROR, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/signout", request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testSignout_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        signupRepository.signout(result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/signout", request.getPath());
        assertEquals("PATCH", request.getMethod());
    }
}
