package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.moum.data.entity.Performance;
import com.example.moum.repository.PerformRepository;
import com.example.moum.repository.PromoteRepository;
import com.example.moum.repository.client.RetrofitClientManager;
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
public class PromoteRepositoryTest {
    private MockWebServer mockWebServer;
    private PromoteRepository promoteRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        promoteRepository = new PromoteRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testCreateQr_ShouldReturnSuccessResponse_WhenLCreateQr_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 28;
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-QR001\",\n"
                + "    \"message\": \"QR 코드 생성 성공\",\n"
                + "    \"data\": \"https://kr.object.ncloudstorage.com/moumstorage/qr/1.png\"\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        promoteRepository.createQr(performId, result -> {
            try {
                assertEquals(Validation.QR_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code?id=%d", performId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateQr_ShouldReturnFailureResponse_WhenCreateQrFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 28;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-QR001\",\n"
                + "    \"message\": \"QR 코드 생성 실패\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        promoteRepository.createQr(performId, result -> {
            try {
                assertEquals(Validation.QR_FAIL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code?id=%d", performId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateQr_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 28;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        promoteRepository.createQr(performId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code?id=%d", performId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLoadQr_ShouldReturnSuccessResponse_WhenLoadQrSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 28;
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-QR001\",\n"
                + "    \"message\": \"QR 코드 생성 성공\",\n"
                + "    \"data\": \"https://kr.object.ncloudstorage.com/moumstorage/qr/1.png\"\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        promoteRepository.loadQr(performId, result -> {
            try {
                assertEquals(Validation.QR_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code/%d", performId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadQr_ShouldReturnFailureResponse_WhenLoadQrFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 28;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-QR001\",\n"
                + "    \"message\": \"QR 코드 생성 실패\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        promoteRepository.loadQr(performId, result -> {
            try {
                assertEquals(Validation.QR_FAIL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code/%d", performId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testUpdatePerform_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 28;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        promoteRepository.loadQr(performId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code/%d", performId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testDeleteQr_ShouldReturnSuccessResponse_WhenDeleteQrSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 27;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-QR002\",\n"
                + "    \"message\": \"QR 코드 삭제 성공\",\n"
                + "    \"data\": null\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        promoteRepository.deleteQr(performId, result -> {
            try {
                assertEquals(Validation.QR_DELETE_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code?id=%d", performId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteQr_ShouldReturnFailureResponse_WhenDeleteQrFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 27;
        String mockResponse = "{\n"
                + "    \"status\": 403,\n"
                + "    \"code\": \"F-M003\",\n"
                + "    \"message\": \"권한이 없습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setBody(mockResponse));

        // When
        promoteRepository.deleteQr(performId, result -> {
            try {
                assertEquals(Validation.NO_AUTHORITY, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code?id=%d", performId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteQr_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 27;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        promoteRepository.deleteQr(performId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/pamphlet/qr-code?id=%d", performId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

}
