package com.example.moum;

import static org.junit.Assert.assertEquals;

import com.example.moum.data.api.PracticeNPerformApi;
import com.example.moum.data.dto.SearchPerformHallArgs;
import com.example.moum.data.dto.SearchPracticeroomArgs;
import com.example.moum.data.entity.Settlement;
import com.example.moum.repository.PaymentRepository;
import com.example.moum.repository.PracticeNPerformRepository;
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
public class PracticeNPerformRepositoryTest {
    private MockWebServer mockWebServer;
    private PracticeNPerformRepository practiceNPerformRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        practiceNPerformRepository = new PracticeNPerformRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testGetPracticeroom_ShouldReturnSuccessResponse_WhenGetPracticeroom_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer practiceroomId = 11;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-B005\",\n"
                + "    \"message\": \"연습실 조회 성공\",\n"
                + "    \"data\": {\n"
                + "        \"id\": 11,\n"
                + "        \"name\": \"메종데쟈\",\n"
                + "        \"address\": \"서울특별시 서초구 방배로 62 성산빌딩 지하1\",\n"
                + "        \"owner\": \"김땡땡\",\n"
                + "        \"phone\": \"01012348765\",\n"
                + "        \"email\": \"kim@gmail.com\",\n"
                + "        \"mapUrl\": \"https://naver.me/5AfqSPxD\",\n"
                + "        \"imageUrls\": [\n"
                + "            \"https://kr.object.ncloudstorage.com/moumstorage/practiceRoom/11/Picsart_23-09-02_22-57-15-777.jpg\"\n"
                + "        ],\n"
                + "        \"price\": 50000,\n"
                + "        \"capacity\": 100,\n"
                + "        \"type\": 1,\n"
                + "        \"stand\": 60,\n"
                + "        \"hasPiano\": true,\n"
                + "        \"hasAmp\": true,\n"
                + "        \"hasSpeaker\": true,\n"
                + "        \"hasMic\": true,\n"
                + "        \"hasDrums\": true,\n"
                + "        \"details\": \"메종데쟈\",\n"
                + "        \"latitude\": 37.4803128,\n"
                + "        \"longitude\": 126.9988264\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPracticeroom(practiceroomId, result -> {
            try {
                assertEquals(Validation.PRACTICE_ROOM_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-room/view/%d", practiceroomId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPracticeroom_ShouldReturnFailureResponse_WhenGetPracticeroomFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer practiceroomId = 11;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-B004\",\n"
                + "    \"message\": \"해당 연습실을 찾을 수 없습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPracticeroom(practiceroomId, result -> {
            try {
                assertEquals(Validation.PRACTICE_ROOM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-room/view/%d", practiceroomId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPracticeroom_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer practiceroomId = 11;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        practiceNPerformRepository.getPracticeroom(practiceroomId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-room/view/%d", practiceroomId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPracticerooms_ShouldReturnSuccessResponse_WhenGetPracticerooms_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-B005\",\n"
                + "    \"message\": \"연습실 조회 성공\",\n"
                + "    \"data\": {\n"
                + "        \"content\": [\n"
                + "            {\n"
                + "                \"id\": 1,\n"
                + "                \"name\": \"아주대학교\",\n"
                + "                \"address\": \"경기도 수원시 영통구 월드컵로 206 아주대학교\",\n"
                + "                \"owner\": \"총장님\",\n"
                + "                \"phone\": \"01012345678\",\n"
                + "                \"email\": \"ajou@ajou.ac.kr\",\n"
                + "                \"mapUrl\": \"https://naver.me/5ncXnSHq\",\n"
                + "                \"imageUrls\": [],\n"
                + "                \"price\": 0,\n"
                + "                \"capacity\": 0,\n"
                + "                \"type\": 0,\n"
                + "                \"stand\": 0,\n"
                + "                \"hasPiano\": false,\n"
                + "                \"hasAmp\": false,\n"
                + "                \"hasSpeaker\": false,\n"
                + "                \"hasMic\": false,\n"
                + "                \"hasDrums\": false,\n"
                + "                \"details\": \"\",\n"
                + "                \"latitude\": 37.2833808,\n"
                + "                \"longitude\": 127.046072\n"
                + "            },\n"
                + "            {\n"
                + "                \"id\": 2,\n"
                + "                \"name\": \"아주대학교\",\n"
                + "                \"address\": \"경기도 수원시 영통구 월드컵로 206 아주대학교\",\n"
                + "                \"owner\": \"총장님\",\n"
                + "                \"phone\": \"01012345678\",\n"
                + "                \"email\": \"ajou@ajou.ac.kr\",\n"
                + "                \"mapUrl\": \"https://naver.me/5ncXnSHq\",\n"
                + "                \"imageUrls\": [],\n"
                + "                \"price\": 0,\n"
                + "                \"capacity\": 0,\n"
                + "                \"type\": 0,\n"
                + "                \"stand\": 0,\n"
                + "                \"hasPiano\": false,\n"
                + "                \"hasAmp\": false,\n"
                + "                \"hasSpeaker\": false,\n"
                + "                \"hasMic\": false,\n"
                + "                \"hasDrums\": false,\n"
                + "                \"details\": \"\",\n"
                + "                \"latitude\": 37.2833808,\n"
                + "                \"longitude\": 127.046072\n"
                + "            }\n"
                + "        ],\n"
                + "        \"pageable\": {\n"
                + "            \"pageNumber\": 0,\n"
                + "            \"pageSize\": 2,\n"
                + "            \"sort\": {\n"
                + "                \"empty\": true,\n"
                + "                \"sorted\": false,\n"
                + "                \"unsorted\": true\n"
                + "            },\n"
                + "            \"offset\": 0,\n"
                + "            \"paged\": true,\n"
                + "            \"unpaged\": false\n"
                + "        },\n"
                + "        \"last\": false,\n"
                + "        \"totalPages\": 6,\n"
                + "        \"totalElements\": 12,\n"
                + "        \"size\": 2,\n"
                + "        \"number\": 0,\n"
                + "        \"sort\": {\n"
                + "            \"empty\": true,\n"
                + "            \"sorted\": false,\n"
                + "            \"unsorted\": true\n"
                + "        },\n"
                + "        \"first\": true,\n"
                + "        \"numberOfElements\": 2,\n"
                + "        \"empty\": false\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPracticerooms(page, size, result -> {
            try {
                assertEquals(Validation.PRACTICE_ROOM_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-rooms?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPracticerooms_ShouldReturnFailureResponse_WhenGetPracticeroomsFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-B021\",\n"
                + "    \"message\": \"페이지 값이 유효하지 않습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPracticerooms(page, size, result -> {
            try {
                assertEquals(Validation.PARAMETER_NOT_VALID, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-rooms?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPracticerooms_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        practiceNPerformRepository.getPracticerooms(page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-rooms?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testSearchPracticerooms_ShouldReturnSuccessResponse_WhenSearchPracticeroomsSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        SearchPracticeroomArgs args = new SearchPracticeroomArgs();
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-B005\",\n"
                + "    \"message\": \"연습실 조회 성공\",\n"
                + "    \"data\": {\n"
                + "        \"content\": [\n"
                + "            {\n"
                + "                \"id\": 1,\n"
                + "                \"name\": \"아주대학교\",\n"
                + "                \"address\": \"경기도 수원시 영통구 월드컵로 206 아주대학교\",\n"
                + "                \"owner\": \"총장님\",\n"
                + "                \"phone\": \"01012345678\",\n"
                + "                \"email\": \"ajou@ajou.ac.kr\",\n"
                + "                \"mapUrl\": \"https://naver.me/5ncXnSHq\",\n"
                + "                \"imageUrls\": [],\n"
                + "                \"price\": 0,\n"
                + "                \"capacity\": 0,\n"
                + "                \"type\": 0,\n"
                + "                \"stand\": 0,\n"
                + "                \"hasPiano\": false,\n"
                + "                \"hasAmp\": false,\n"
                + "                \"hasSpeaker\": false,\n"
                + "                \"hasMic\": false,\n"
                + "                \"hasDrums\": false,\n"
                + "                \"details\": \"\",\n"
                + "                \"latitude\": 37.2833808,\n"
                + "                \"longitude\": 127.046072\n"
                + "            },\n"
                + "            {\n"
                + "                \"id\": 2,\n"
                + "                \"name\": \"아주대학교\",\n"
                + "                \"address\": \"경기도 수원시 영통구 월드컵로 206 아주대학교\",\n"
                + "                \"owner\": \"총장님\",\n"
                + "                \"phone\": \"01012345678\",\n"
                + "                \"email\": \"ajou@ajou.ac.kr\",\n"
                + "                \"mapUrl\": \"https://naver.me/5ncXnSHq\",\n"
                + "                \"imageUrls\": [],\n"
                + "                \"price\": 0,\n"
                + "                \"capacity\": 0,\n"
                + "                \"type\": 0,\n"
                + "                \"stand\": 0,\n"
                + "                \"hasPiano\": false,\n"
                + "                \"hasAmp\": false,\n"
                + "                \"hasSpeaker\": false,\n"
                + "                \"hasMic\": false,\n"
                + "                \"hasDrums\": false,\n"
                + "                \"details\": \"\",\n"
                + "                \"latitude\": 37.2833808,\n"
                + "                \"longitude\": 127.046072\n"
                + "            }\n"
                + "        ],\n"
                + "        \"pageable\": {\n"
                + "            \"pageNumber\": 0,\n"
                + "            \"pageSize\": 2,\n"
                + "            \"sort\": {\n"
                + "                \"empty\": true,\n"
                + "                \"sorted\": false,\n"
                + "                \"unsorted\": true\n"
                + "            },\n"
                + "            \"offset\": 0,\n"
                + "            \"paged\": true,\n"
                + "            \"unpaged\": false\n"
                + "        },\n"
                + "        \"last\": false,\n"
                + "        \"totalPages\": 6,\n"
                + "        \"totalElements\": 12,\n"
                + "        \"size\": 2,\n"
                + "        \"number\": 0,\n"
                + "        \"sort\": {\n"
                + "            \"empty\": true,\n"
                + "            \"sorted\": false,\n"
                + "            \"unsorted\": true\n"
                + "        },\n"
                + "        \"first\": true,\n"
                + "        \"numberOfElements\": 2,\n"
                + "        \"empty\": false\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.searchPracticerooms(page, size, args, result -> {
            try {
                assertEquals(Validation.PRACTICE_ROOM_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-rooms/search?page=%d&size=%d&sortBy=distance&orderBy=asc", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testSearchPracticerooms_ShouldReturnFailureResponse_WhenSearchPracticeroomsFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        SearchPracticeroomArgs args = new SearchPracticeroomArgs();
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-B021\",\n"
                + "    \"message\": \"페이지 값이 유효하지 않습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.searchPracticerooms(page, size, args, result -> {
            try {
                assertEquals(Validation.PARAMETER_NOT_VALID, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-rooms/search?page=%d&size=%d&sortBy=distance&orderBy=asc", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void tesSearchPracticerooms_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        SearchPracticeroomArgs args = new SearchPracticeroomArgs();
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        practiceNPerformRepository.searchPracticerooms(page, size, args, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/practice-rooms/search?page=%d&size=%d&sortBy=distance&orderBy=asc", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPerformHall_ShouldReturnSuccessResponse_WhenGetPerformHallSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performHallId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-B006\",\n"
                + "    \"message\": \"공연장 조회 성공\",\n"
                + "    \"data\": {\n"
                + "        \"id\": 1,\n"
                + "        \"name\": \"경기아트센터\",\n"
                + "        \"address\": \"경기도 수원시 팔달구 효원로307번길 20 경기아트센터\",\n"
                + "        \"owner\": \"테스트owner\",\n"
                + "        \"phone\": \"01055556666\",\n"
                + "        \"email\": \"gyeonggi@naver.com\",\n"
                + "        \"mapUrl\": \"https://naver.me/GJrE1DU6\",\n"
                + "        \"imageUrls\": [\n"
                + "            \"https://kr.object.ncloudstorage.com/moumstorage/practiceRoom/1/20220504_201939.jpg\"\n"
                + "        ],\n"
                + "        \"price\": 0,\n"
                + "        \"size\": 0,\n"
                + "        \"capacity\": 0,\n"
                + "        \"type\": 0,\n"
                + "        \"stand\": 0,\n"
                + "        \"hasPiano\": false,\n"
                + "        \"hasAmp\": false,\n"
                + "        \"hasSpeaker\": false,\n"
                + "        \"hasMic\": false,\n"
                + "        \"hasDrums\": false,\n"
                + "        \"details\": \"\",\n"
                + "        \"latitude\": 37.2618797,\n"
                + "        \"longitude\": 127.0372499\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPerformHall(performHallId, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_HALL_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-hall/view/%d", performHallId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPerformHall_ShouldReturnFailureResponse_WhenGetPerformHallFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performHallId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-B005\",\n"
                + "    \"message\": \"해당 공연장을 찾을 수 없습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPerformHall(performHallId, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_HALL_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-hall/view/%d", performHallId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPerformHall_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer performHallId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        practiceNPerformRepository.getPerformHall(performHallId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-hall/view/%d", performHallId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPerformHalls_ShouldReturnSuccessResponse_WhenGetPerformHalls_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-B006\",\n"
                + "    \"message\": \"공연장 조회 성공\",\n"
                + "    \"data\": {\n"
                + "        \"content\": [\n"
                + "            {\n"
                + "                \"id\": 17,\n"
                + "                \"name\": \"바른아트센터\",\n"
                + "                \"address\": \"경기도 성남시 분당구 야탑로75번길 9 금호프라자\",\n"
                + "                \"owner\": \"바른세상병원\",\n"
                + "                \"phone\": \"031-783-9691\",\n"
                + "                \"email\": \"barunartcenter@naver.com\",\n"
                + "                \"mapUrl\": \"https://naver.me/F3oOs9on\",\n"
                + "                \"imageUrls\": [\n"
                + "                    \"https://kr.object.ncloudstorage.com/moumstorage/practiceRoom/17/바른아트센터.jpg\",\n"
                + "                    \"https://kr.object.ncloudstorage.com/moumstorage/practiceRoom/17/바른아트센터2.jpg\"\n"
                + "                ],\n"
                + "                \"price\": 700000,\n"
                + "                \"size\": 70,\n"
                + "                \"capacity\": 180,\n"
                + "                \"type\": 0,\n"
                + "                \"stand\": 30,\n"
                + "                \"hasPiano\": true,\n"
                + "                \"hasAmp\": false,\n"
                + "                \"hasSpeaker\": true,\n"
                + "                \"hasMic\": true,\n"
                + "                \"hasDrums\": false,\n"
                + "                \"details\": \"지역 사회와 소통하고 대중성, 예술성을 겸비한 넓고 쾌적한 공간, 바른아트센터입니다.\",\n"
                + "                \"latitude\": 37.410849,\n"
                + "                \"longitude\": 127.1268202\n"
                + "            },\n"
                + "            {\n"
                + "                \"id\": 16,\n"
                + "                \"name\": \"오렌지폭스\",\n"
                + "                \"address\": \"경기도 안양시 동안구 시민대로98번길 12\",\n"
                + "                \"owner\": \"멋스크\",\n"
                + "                \"phone\": \"070-4667-7920\",\n"
                + "                \"email\": \"orangefoxco@naver.com\",\n"
                + "                \"mapUrl\": \"https://naver.me/5tjSrqkd\",\n"
                + "                \"imageUrls\": [\n"
                + "                    \"https://kr.object.ncloudstorage.com/moumstorage/practiceRoom/16/오렌지폭스1.jpg\",\n"
                + "                    \"https://kr.object.ncloudstorage.com/moumstorage/practiceRoom/16/오렌지폭스2.jpg\"\n"
                + "                ],\n"
                + "                \"price\": 120000,\n"
                + "                \"size\": 70,\n"
                + "                \"capacity\": 50,\n"
                + "                \"type\": 0,\n"
                + "                \"stand\": 10,\n"
                + "                \"hasPiano\": true,\n"
                + "                \"hasAmp\": true,\n"
                + "                \"hasSpeaker\": true,\n"
                + "                \"hasMic\": true,\n"
                + "                \"hasDrums\": true,\n"
                + "                \"details\": \"오렌지폭스는 신인 음악인 발굴, 공연 기획 및 음반 제작 및 연습실, 공연장 대관 서비스합니다.\",\n"
                + "                \"latitude\": 37.3854523,\n"
                + "                \"longitude\": 126.9447218\n"
                + "            }\n"
                + "        ],\n"
                + "        \"pageable\": {\n"
                + "            \"pageNumber\": 0,\n"
                + "            \"pageSize\": 2,\n"
                + "            \"sort\": {\n"
                + "                \"empty\": false,\n"
                + "                \"sorted\": true,\n"
                + "                \"unsorted\": false\n"
                + "            },\n"
                + "            \"offset\": 0,\n"
                + "            \"paged\": true,\n"
                + "            \"unpaged\": false\n"
                + "        },\n"
                + "        \"totalPages\": 3,\n"
                + "        \"totalElements\": 6,\n"
                + "        \"last\": false,\n"
                + "        \"size\": 2,\n"
                + "        \"number\": 0,\n"
                + "        \"sort\": {\n"
                + "            \"empty\": false,\n"
                + "            \"sorted\": true,\n"
                + "            \"unsorted\": false\n"
                + "        },\n"
                + "        \"numberOfElements\": 2,\n"
                + "        \"first\": true,\n"
                + "        \"empty\": false\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPerformHalls(page, size, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_HALL_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-halls?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPerformHalls_ShouldReturnFailureResponse_WhenGetPerformHallsFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-B021\",\n"
                + "    \"message\": \"페이지 값이 유효하지 않습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.getPerformHalls(page, size, result -> {
            try {
                assertEquals(Validation.PARAMETER_NOT_VALID, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-halls?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPerformHalls_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        practiceNPerformRepository.getPerformHalls(page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-halls?page=%d&size=%d", page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testSearchPerformHalls_ShouldReturnSuccessResponse_WhenSearchPerformHallsSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        SearchPerformHallArgs args = new SearchPerformHallArgs();
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-B006\",\n"
                + "    \"message\": \"공연장 조회 성공\",\n"
                + "    \"data\": {\n"
                + "        \"content\": [\n"
                + "            {\n"
                + "                \"id\": 1,\n"
                + "                \"name\": \"경기아트센터\",\n"
                + "                \"address\": \"경기도 수원시 팔달구 효원로307번길 20 경기아트센터\",\n"
                + "                \"owner\": \"테스트owner\",\n"
                + "                \"phone\": \"01055556666\",\n"
                + "                \"email\": \"gyeonggi@naver.com\",\n"
                + "                \"mapUrl\": \"https://naver.me/GJrE1DU6\",\n"
                + "                \"imageUrls\": [\n"
                + "                    \"https://kr.object.ncloudstorage.com/moumstorage/practiceRoom/1/20220504_201939.jpg\"\n"
                + "                ],\n"
                + "                \"price\": 0,\n"
                + "                \"size\": 0,\n"
                + "                \"capacity\": 0,\n"
                + "                \"type\": 0,\n"
                + "                \"stand\": 0,\n"
                + "                \"hasPiano\": false,\n"
                + "                \"hasAmp\": false,\n"
                + "                \"hasSpeaker\": false,\n"
                + "                \"hasMic\": false,\n"
                + "                \"hasDrums\": false,\n"
                + "                \"details\": \"\",\n"
                + "                \"latitude\": 37.2618797,\n"
                + "                \"longitude\": 127.0372499\n"
                + "            }\n"
                + "        ],\n"
                + "        \"pageable\": {\n"
                + "            \"pageNumber\": 0,\n"
                + "            \"pageSize\": 2,\n"
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
                + "        \"size\": 2,\n"
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
        practiceNPerformRepository.searchPerformHalls(page, size, args, result -> {
            try {
                assertEquals(Validation.PERFORMANCE_HALL_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-halls/search?page=%d&size=%d&sortBy=distance&orderBy=asc", page, size),
                request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testSearchPerformHalls_ShouldReturnFailureResponse_WhenSearchPerformHallsFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        SearchPerformHallArgs args = new SearchPerformHallArgs();
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-B021\",\n"
                + "    \"message\": \"페이지 값이 유효하지 않습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        practiceNPerformRepository.searchPerformHalls(page, size, args, result -> {
            try {
                assertEquals(Validation.PARAMETER_NOT_VALID, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-halls/search?page=%d&size=%d&sortBy=distance&orderBy=asc", page, size),
                request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void tesSearchPerformHalls_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 1;
        Integer size = 10;
        SearchPerformHallArgs args = new SearchPerformHallArgs();
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        practiceNPerformRepository.searchPerformHalls(page, size, args, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/business/performance-halls/search?page=%d&size=%d&sortBy=distance&orderBy=asc", page, size),
                request.getPath());
        assertEquals("GET", request.getMethod());
    }
}
