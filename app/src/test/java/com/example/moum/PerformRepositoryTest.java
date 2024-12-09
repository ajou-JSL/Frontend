package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Performance;
import com.example.moum.repository.PerformRepository;
import com.example.moum.repository.ProfileRepository;
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
public class PerformRepositoryTest {
    private MockWebServer mockWebServer;
    private PerformRepository performRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        performRepository = new PerformRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testCreatePerform_ShouldReturnSuccessResponse_WhenLCreatePerform_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Performance performance = new Performance();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-PF001\",\n"
                + "    \"message\": \"공연게시글 생성 성공\",\n"
                + "    \"data\": {\n"
                + "        \"id\": 28,\n"
                + "        \"teamName\": \"백엔드 초고수 테스터500의 클래식 팀\",\n"
                + "        \"teamId\":1,\n"
                + "        \"moumName\": \"테스터's 공연\",\n"
                + "        \"moumId\":1,\n"
                + "        \"performanceName\": \"22테스트Rock\",\n"
                + "        \"performanceDescription\": \"최강민호와 함께 불태워보아요\",\n"
                + "        \"performanceLocation\": \"강원도 태백산\",\n"
                + "        \"performanceStartDate\": \"2024-11-15T19:00:00.000+00:00\",\n"
                + "        \"performanceEndDate\": \"2024-12-01T21:00:00.000+00:00\",\n"
                + "        \"performancePrice\": 50000,\n"
                + "        \"performanceImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/performs/22테스트Rock/images.png\",\n"
                + "        \"membersId\": [\n"
                + "            9\n"
                + "        ],\n"
                + "        \"genre\": \"POP\"\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        performRepository.createPerform(performance, file, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_CREATE_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/performs", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreatePerform_ShouldReturnFailureResponse_WhenCreatePerformFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Performance performance = new Performance();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "    \"status\": 403,\n"
                + "    \"code\": \"F-PAT003\",\n"
                + "    \"message\": \"이미 게시된 공연 게시글이 존재합니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setBody(mockResponse));

        // When
        performRepository.createPerform(performance, file, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_ALREADY_MADE, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/performs", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreatePerformProfile_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Performance performance = new Performance();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        performRepository.createPerform(performance, file, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/performs", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testUpdatePerform_ShouldReturnSuccessResponse_WhenUpdatePerformSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 20;
        Performance perform = new Performance();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-PF002\",\n"
                + "    \"message\": \"공연게시글 수정 성공\",\n"
                + "    \"data\": {\n"
                + "        \"id\": 20,\n"
                + "        \"teamName\": \"백엔드 초고수 테스터500의 클래식 팀\",\n"
                + "        \"teamId\":1,\n"
                + "        \"moumName\": \"테스터's 공연\",\n"
                + "        \"moumId\":1,\n"
                + "        \"performanceName\": \"Amazing Concert\",\n"
                + "        \"performanceDescription\": \"An incredible night of live music and performances.\",\n"
                + "        \"performanceLocation\": \"Seoul Arts Center\",\n"
                + "        \"performanceStartDate\": \"2024-12-01T19:00:00.000+00:00\",\n"
                + "        \"performanceEndDate\": \"2024-12-01T22:00:00.000+00:00\",\n"
                + "        \"performancePrice\": 50000,\n"
                + "        \"performanceImageUrl\": \"https://example.com/images/concert.jpg\",\n"
                + "        \"membersId\": [],\n"
                + "        \"genre\": \"POP\"\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        performRepository.updatePerform(performId, perform, file, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_UPDATE_SUCCESS, result.getValidation());
                assertEquals(20, result.getData().getId().intValue());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdatePerform_ShouldReturnFailureResponse_WhenUpdatePerformFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 20;
        Performance perform = new Performance();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-T002\",\n"
                + "    \"message\": \"팀을 찾을 수 없습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        performRepository.updatePerform(performId, perform, file, result -> {
            try {
                assertEquals(Validation.TEAM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdatePerform_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 20;
        Performance perform = new Performance();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        performRepository.updatePerform(performId, perform, file, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testDeletePerform_ShouldReturnSuccessResponse_WhenDeletePerformSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 27;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-PF003\",\n"
                + "\t\"message\": \"공연게시글 삭제 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"id\": 27,\n"
                + "\t\t\"teamName\": \"백엔드 초고수 테스터500의 클래식 팀\",\n"
                + "\t\t\"teamId\":1,\n"
                + "\t\t\"moumName\": \"테스터's 공연\",\n"
                + "\t\t\"moumId\":1,\n"
                + "\t\t\"performanceName\": \"22테스트Rock\",\n"
                + "\t\t\"performanceDescription\": \"최강민호와 함께 불태워보아요\",\n"
                + "\t\t\"performanceLocation\": \"강원도 태백산\",\n"
                + "\t\t\"performanceStartDate\": \"2024-11-15T19:00:00.000+00:00\",\n"
                + "\t\t\"performanceEndDate\": \"2024-12-01T21:00:00.000+00:00\",\n"
                + "\t\t\"performancePrice\": 50000,\n"
                + "\t\t\"performanceImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/performs/22테스트Rock/images.png\",\n"
                + "\t\t\"membersId\": [\n"
                + "\t\t\t9\n"
                + "\t\t],\n"
                + "\t\t\"genres\": \"ROCK\"\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        performRepository.deletePerform(performId, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_DELETE_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeletePerform_ShouldReturnFailureResponse_WhenDeletePerformFailure() throws Exception {
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
        performRepository.deletePerform(performId, result -> {
            try {
                assertEquals(Validation.NO_AUTHORITY, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeletePerform_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 27;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        performRepository.deletePerform(performId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testLoadPerform_ShouldReturnSuccessResponse_WhenLoadPerformSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-PF004\",\n"
                + "\t\"message\": \"공연게시글 단건 조회 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"id\": 1,\n"
                + "    \"teamName\":\"테스트팀\",\n"
                + "    \"teamId\":1,\n"
                + "    \"moumName\":\"테스트모음\",\n"
                + "    \"moumId\":1,\n"
                + "    \"performanceName\": \"Amazing Concert\",\n"
                + "\t\t\"performanceDescription\": \"An incredible night of live music and performances.\",\n"
                + "\t\t\"performanceLocation\": \"Seoul Arts Center\",\n"
                + "\t\t\"performanceStartDate\": \"2024-12-01T19:00:00.000+00:00\",\n"
                + "\t\t\"performanceEndDate\": \"2024-12-01T22:00:00.000+00:00\",\n"
                + "\t\t\"performancePrice\": 50000,\n"
                + "\t\t\"performanceImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/performs/Amazing Concert/images.png\",\n"
                + "\t\t\"membersId\": [\n"
                + "\t\t\t1,\n"
                + "\t\t\t2,\n"
                + "\t\t\t3\n"
                + "\t\t],\n"
                + "    \"genre\":\"POP\"\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        performRepository.loadPerform(performId, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerform_ShouldReturnFailureResponse_WhenLoadPerformFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C006\",\n"
                + "\t\"message\": \"유효하지 않은 데이터입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        performRepository.loadPerform(performId, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerform_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        performRepository.loadPerform(performId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs/%d", performId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerforms_ShouldReturnSuccessResponse_WhenLoadPerformsSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-PF005\",\n"
                + "\t\"message\": \"공연게시글 리스트 조회 성공\",\n"
                + "\t\"data\": [\n"
                + "\t\t{\n"
                + "\t\t\t\"id\": 1,\n"
                + "      \"teamName\":\"테스트팀\",\n"
                + "      \"teamId\":1,\n"
                + "      \"moumName\":\"테스트모음\",\n"
                + "      \"moumId\":1,\n"
                + "\t\t\t\"performanceName\": \"Amazing Concert\",\n"
                + "\t\t\t\"performanceDescription\": \"An incredible night of live music and performances.\",\n"
                + "\t\t\t\"performanceLocation\": \"Seoul Arts Center\",\n"
                + "\t\t\t\"performanceStartDate\": \"2024-12-01T19:00:00.000+00:00\",\n"
                + "\t\t\t\"performanceEndDate\": \"2024-12-01T22:00:00.000+00:00\",\n"
                + "\t\t\t\"performancePrice\": 50000,\n"
                + "\t\t\t\"performanceImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/performs/Amazing Concert/images.png\",\n"
                + "\t\t\t\"membersId\": [\n"
                + "\t\t\t\t1,\n"
                + "\t\t\t\t2,\n"
                + "\t\t\t\t3\n"
                + "\t\t\t],\n"
                + "\t\t\t\"genre\":\"POP\"\n"
                + "\t\t},\n"
                + "\t\t{\n"
                + "\t\t\t\"id\": 2,\n"
                + "\t    \"teamName\":\"테스트팀\",\n"
                + "\t    \"teamId\":1,\n"
                + "      \"moumName\":\"테스트모음\",\n"
                + "      \"moumId\":1,\n"
                + "\t\t\t\"performanceName\": \"Amazing Concert\",\n"
                + "\t\t\t\"performanceDescription\": \"An incredible night of live music and performances.\",\n"
                + "\t\t\t\"performanceLocation\": \"Seoul Arts Center\",\n"
                + "\t\t\t\"performanceStartDate\": \"2024-12-01T19:00:00.000+00:00\",\n"
                + "\t\t\t\"performanceEndDate\": \"2024-12-01T22:00:00.000+00:00\",\n"
                + "\t\t\t\"performancePrice\": 50000,\n"
                + "\t\t\t\"performanceImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/performs/Amazing Concert/images.png\",\n"
                + "\t\t\t\"membersId\": [\n"
                + "\t\t\t\t1,\n"
                + "\t\t\t\t2,\n"
                + "\t\t\t\t3\n"
                + "\t\t\t],\n"
                + "\t\t  \"genre\": \"ROCK\"\n"
                + "\t\t}\n"
                + "\t]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        performRepository.loadPerforms(page, size, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_LIST_GET_SUCCESS, result.getValidation());
                assertEquals(2, result.getData().size());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs-all?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerforms_ShouldReturnFailureResponse_WhenLoadPerformsFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C006\",\n"
                + "\t\"message\": \"유효하지 않은 데이터입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        performRepository.loadPerforms(page, size, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs-all?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerforms_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        performRepository.loadPerforms(page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
                assertEquals(2, result.getData().size());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs-all?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerformsHot_ShouldReturnSuccessResponse_WhenLoadPerformsHotSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-PF006\",\n"
                + "    \"message\": \"이달의 공연게시글 리스트 조회 성공\",\n"
                + "    \"data\": {\n"
                + "        \"content\": [\n"
                + "            {\n"
                + "                \"id\": 3,\n"
                + "                \"teamName\": \"노마드 콰르텟\",\n"
                + "                \"teamId\": 2,\n"
                + "                \"moumName\": \"제3회 체임버 뮤직 콘서트\",\n"
                + "                \"moumId\": 5,\n"
                + "                \"performanceName\": \"제3회 체임버 뮤직 콘서트\",\n"
                + "                \"performanceDescription\": \"노마드 콰르텟 제3회 체임버 공연에 환영합니다. \",\n"
                + "                \"performanceLocation\": \"경기아트센터\",\n"
                + "                \"performanceStartDate\": \"2024-12-20T00:00:00.000+00:00\",\n"
                + "                \"performanceEndDate\": null,\n"
                + "                \"performancePrice\": 20000,\n"
                + "                \"performanceImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/performs/제3회 체임버 뮤직 "
                + "콘서트/7dae82a21a503cf4fb4c9250b4c7272cdb68b153355500c4c3a3a72b9748b740.0\",\n"
                + "                \"membersId\": [\n"
                + "                    2\n"
                + "                ],\n"
                + "                \"genre\": \"CLASSICAL\",\n"
                + "                \"likesCount\": 0,\n"
                + "                \"viewCount\": 27\n"
                + "            }\n"
                + "        ],\n"
                + "        \"pageable\": {\n"
                + "            \"pageNumber\": 0,\n"
                + "            \"pageSize\": 5,\n"
                + "            \"sort\": {\n"
                + "                \"empty\": true,\n"
                + "                \"sorted\": false,\n"
                + "                \"unsorted\": true\n"
                + "            },\n"
                + "            \"offset\": 0,\n"
                + "            \"paged\": true,\n"
                + "            \"unpaged\": false\n"
                + "        },\n"
                + "        \"last\": true,\n"
                + "        \"totalPages\": 1,\n"
                + "        \"totalElements\": 1,\n"
                + "        \"size\": 5,\n"
                + "        \"number\": 0,\n"
                + "        \"sort\": {\n"
                + "            \"empty\": true,\n"
                + "            \"sorted\": false,\n"
                + "            \"unsorted\": true\n"
                + "        },\n"
                + "        \"first\": true,\n"
                + "        \"numberOfElements\": 1,\n"
                + "        \"empty\": false\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        performRepository.loadPerformsHot(page, size, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_HOT_LIST_GET_SUCCESS, result.getValidation());
                assertEquals(1, result.getData().size());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs-all/this-month?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerformsHot_ShouldReturnFailureResponse_WhenLoadPerformsHotFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C006\",\n"
                + "\t\"message\": \"유효하지 않은 데이터입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        performRepository.loadPerformsHot(page, size, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs-all/this-month?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerformsHot_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        performRepository.loadPerformsHot(page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs-all/this-month?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerformsOfMoum_ShouldReturnSuccessResponse_WhenLoadPerformsOfMoumSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-PF004\",\n"
                + "\t\"message\": \"공연게시글 단건 조회 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"id\": 1,\n"
                + "    \"teamName\":\"테스트팀\",\n"
                + "    \"teamId\":1,\n"
                + "    \"moumName\":\"테스트모음\",\n"
                + "    \"moumId\":1,\n"
                + "    \"performanceName\": \"Amazing Concert\",\n"
                + "\t\t\"performanceDescription\": \"An incredible night of live music and performances.\",\n"
                + "\t\t\"performanceLocation\": \"Seoul Arts Center\",\n"
                + "\t\t\"performanceStartDate\": \"2024-12-01T19:00:00.000+00:00\",\n"
                + "\t\t\"performanceEndDate\": \"2024-12-01T22:00:00.000+00:00\",\n"
                + "\t\t\"performancePrice\": 50000,\n"
                + "\t\t\"performanceImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/performs/Amazing Concert/images.png\",\n"
                + "\t\t\"membersId\": [\n"
                + "\t\t\t1,\n"
                + "\t\t\t2,\n"
                + "\t\t\t3\n"
                + "\t\t],\n"
                + "    \"genres\":\"POP\"\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        performRepository.loadPerformOfMoum(moumId, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_GET_SUCCESS, result.getValidation());
                assertEquals(moumId, result.getData().getId());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs?moumId=%d", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerformsOfMoum_ShouldReturnFailureResponse_WhenLoadPerformsOfMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        String mockResponse = "{\n"
                + "\t\"status\": 400,\n"
                + "\t\"code\": \"F-C006\",\n"
                + "\t\"message\": \"유효하지 않은 데이터입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        performRepository.loadPerformOfMoum(moumId, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs?moumId=%d", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadPerformsOfMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        performRepository.loadPerformOfMoum(moumId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/performs?moumId=%d", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }
}
