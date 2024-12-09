package com.example.moum;

import static org.junit.Assert.assertEquals;

import com.example.moum.data.entity.Settlement;
import com.example.moum.repository.PaymentRepository;
import com.example.moum.repository.PromoteRepository;
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
public class PaymentRepositoryTest {
    private MockWebServer mockWebServer;
    private PaymentRepository paymentRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        paymentRepository = new PaymentRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testCreateSettlement_ShouldReturnSuccessResponse_WhenLCreateSettlement_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Settlement settlement = new Settlement();
        Integer moumId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 201,\n"
                + "\t\"code\": \"S-S001\",\n"
                + "\t\"message\": \"정산 등록 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\t\"settlementId\":1,\n"
                + "\t\t\t\"settlementName\":\"name\",\n"
                + "\t\t\t\"fee\":1111,\n"
                + "\t\t\t\"moumId\":1\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        paymentRepository.createSettlement(moumId, settlement, result -> {
            try {
                assertEquals(Validation.SETTLEMENT_CREATE_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements", moumId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateSettlement_ShouldReturnFailureResponse_WhenCreateSettlementFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Settlement settlement = new Settlement();
        Integer moumId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 403,\n"
                + "\t\"code\": \"F-M003\",\n"
                + "\t\"message\": \"권한이 없습니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setBody(mockResponse));

        // When
        paymentRepository.createSettlement(moumId, settlement, result -> {
            try {
                assertEquals(Validation.NO_AUTHORITY, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements", moumId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateSettlement_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Settlement settlement = new Settlement();
        Integer moumId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        paymentRepository.createSettlement(moumId, settlement, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements", moumId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void tesLoadSettlements_ShouldReturnSuccessResponse_WhenLoadSettlementsSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-S003\",\n"
                + "\t\"message\": \"정산 목록 조회 성공\",\n"
                + "\t\"data\": [\n"
                + "\t\t{\n"
                + "\t\t\t\"settlementId\":1,\n"
                + "\t\t\t\"settlementName\": \"name1\",\n"
                + "\t\t\t\"fee\": 1111,\n"
                + "\t\t\t\"moumId\": 1\n"
                + "\t\t},\n"
                + "\t\t{\n"
                + "\t\t\t\"settlementId\":2,\n"
                + "\t\t\t\"settlementName\": \"name2\",\n"
                + "\t\t\t\"fee\": 2222,\n"
                + "\t\t\t\"moumId\": 2\n"
                + "\t\t},\n"
                + "\t\t{\n"
                + "\t\t  \"settlementId\":3,\n"
                + "\t\t\t\"settlementName\": \"name3\",\n"
                + "\t\t\t\"fee\": 3333,\n"
                + "\t\t\t\"moumId\": 3\n"
                + "\t\t}\n"
                + "\t]\n"
                + "}\n";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        paymentRepository.loadSettlements(moumId, result -> {
            try {
                assertEquals(Validation.SETTLEMENT_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements-all", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadSettlements_ShouldReturnFailureResponse_WhenLoadSettlementsFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 404,\n"
                + "\t\"code\": \"F-MM003\",\n"
                + "\t\"message\": \"존재하지 않는 모음입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        paymentRepository.loadSettlements(moumId, result -> {
            try {
                assertEquals(Validation.MOUM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements-all", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadSettlements_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        paymentRepository.loadSettlements(moumId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements-all", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testDeleteSettlement_ShouldReturnSuccessResponse_WhenDeleteSettlementSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        Integer settlementId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-S002\",\n"
                + "\t\"message\": \"정산 삭제 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\t\"settlementId\":1,\n"
                + "\t\t\t\"settlementName\":\"name\",\n"
                + "\t\t\t\"fee\":1111,\n"
                + "\t\t\t\"moumId\":1\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        paymentRepository.deleteSettlement(moumId, settlementId, result -> {
            try {
                assertEquals(Validation.SETTLEMENT_DELETE_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements/%d", moumId, settlementId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteSettlement_ShouldReturnFailureResponse_WhenDeleteSettlementFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        Integer settlementId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C006\",\n"
                + "\t\"message\": \"유효하지 않은 데이터입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}\n";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        paymentRepository.deleteSettlement(moumId, settlementId, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements/%d", moumId, settlementId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteSettlement_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        Integer settlementId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        paymentRepository.deleteSettlement(moumId, settlementId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moums/%d/settlements/%d", moumId, settlementId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

}
