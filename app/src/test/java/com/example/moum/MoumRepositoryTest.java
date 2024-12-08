package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

@RunWith(JUnit4.class)
public class MoumRepositoryTest {
    private MockWebServer mockWebServer;
    private MoumRepository moumRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        moumRepository = new MoumRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testCreateMoum_ShouldReturnSuccessResponse_WhenLCreateMoum_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Moum moum = new Moum();
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassLoader classLoader = getClass().getClassLoader();
            assertNotNull(classLoader.getResource("image_example.jpeg"));
            File file = new File(classLoader.getResource("image_example.jpeg").getFile());
            files.add(file);
        }
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-MM001\",\n"
                + "\t\"message\": \"모음 생성 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"moumId\": 20,\n"
                + "\t\t\"moumName\": \"Summer Concert\",\n"
                + "\t\t\"moumDescription\": \"A live music concert in the summer.\",\n"
                + "\t\t\"performLocation\": \"Seoul Stadium\",\n"
                + "\t\t\"startDate\": \"2024-06-15\",\n"
                + "\t\t\"endDate\": null,\n"
                + "\t\t\"price\": 50000,\n"
                + "\t\t\"imageUrls\": [\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/건강보험 자격득실확인서.jpeg\"\n"
                + "\t\t],\n"
                + "\t\t\"leaderId\": 2,\n"
                + "\t\t\"leaderName\": null,\n"
                + "\t\t\"teamId\": 9,\n"
                + "\t\t\"process\": {\n"
                + "\t\t\t\"recruitStatus\": false,\n"
                + "\t\t\t\"chatroomStatus\": false,\n"
                + "\t\t\t\"practiceroomStatus\": false,\n"
                + "\t\t\t\"performLocationStatus\": false,\n"
                + "\t\t\t\"promoteStatus\": false,\n"
                + "\t\t\t\"paymentStatus\": false,\n"
                + "\t\t\t\"finishStatus\": false,\n"
                + "\t\t\t\"processPercentage\": 0\n"
                + "\t\t},\n"
                + "    \"music\": [\n"
                + "            {\n"
                + "                \"musicName\": \"testmusic1\",\n"
                + "                \"artistName\": \"tester1\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"musicName\": \"testmusic2\",\n"
                + "                \"artistName\": \"tester2\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"musicName\": \"testmusic3\",\n"
                + "                \"artistName\": \"tester3\"\n"
                + "            }\n"
                + "    ],\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 2,\n"
                + "\t\t\t\t\"name\": \"tester\",\n"
                + "\t\t\t\t\"username\": \"tester\",\n"
                + "\t\t\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "\t\t\t\t\"memberRecords\": [\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 12,\n"
                + "\t\t\t\t\t\t\"recordName\": \"Second Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t},\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 13,\n"
                + "\t\t\t\t\t\t\"recordName\": \"First Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t}\n"
                + "\t\t\t\t]\n"
                + "\t\t\t}\n"
                + "\t\t],\n"
                + "\t\t\"genre\": \"CLASSICAL\"\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        moumRepository.createMoum(moum, files, result -> {
            try {
                assertEquals(Validation.CREATE_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/moum", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateMoum_ShouldReturnFailureResponse_WhenCreateMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Moum moum = new Moum();
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassLoader classLoader = getClass().getClassLoader();
            assertNotNull(classLoader.getResource("image_example.jpeg"));
            File file = new File(classLoader.getResource("image_example.jpeg").getFile());
            files.add(file);
        }
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-T002\",\n"
                + "    \"message\": \"팀을 찾을 수 없습니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        moumRepository.createMoum(moum, files, result -> {
            try {
                assertEquals(Validation.TEAM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/moum", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Moum moum = new Moum();
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassLoader classLoader = getClass().getClassLoader();
            assertNotNull(classLoader.getResource("image_example.jpeg"));
            File file = new File(classLoader.getResource("image_example.jpeg").getFile());
            files.add(file);
        }
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.createMoum(moum, files, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/moum", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testUpdateMoum_ShouldReturnSuccessResponse_WhenUpdateMoumSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 44;
        Moum moum = new Moum();
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassLoader classLoader = getClass().getClassLoader();
            assertNotNull(classLoader.getResource("image_example.jpeg"));
            File file = new File(classLoader.getResource("image_example.jpeg").getFile());
            files.add(file);
        }
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-MM003\",\n"
                + "    \"message\": \"모음 수정 성공\",\n"
                + "    \"data\": {\n"
                + "        \"moumId\": 44,\n"
                + "        \"moumName\": \"22업데이트 테스트 모음\",\n"
                + "        \"moumDescription\": \"update A live music concert in the summer.\",\n"
                + "        \"performLocation\": \"Seoul Stadium\",\n"
                + "        \"startDate\": \"2024-06-15\",\n"
                + "        \"endDate\": null,\n"
                + "        \"price\": 50000,\n"
                + "        \"imageUrls\": [\n"
                + "            \"https://kr.object.ncloudstorage.com/moumstorage/moums/22업데이트 테스트 모음/건강보험 자격득실확인서.jpeg\"\n"
                + "        ],\n"
                + "        \"leaderId\": 2,\n"
                + "        \"leaderName\": null,\n"
                + "        \"teamId\": 9,\n"
                + "        \"process\": {\n"
                + "            \"recruitStatus\": false,\n"
                + "            \"chatroomStatus\": false,\n"
                + "            \"practiceroomStatus\": false,\n"
                + "            \"performLocationStatus\": false,\n"
                + "            \"promoteStatus\": false,\n"
                + "            \"paymentStatus\": false,\n"
                + "            \"finishStatus\": false,\n"
                + "            \"processPercentage\": 0\n"
                + "        },\n"
                + "       \"music\": [{\n"
                + "\t         \"musicName\": \"testmusic\",\n"
                + "\t         \"artistName\": \"tester\"\n"
                + "\t\t\t  }],\n"
                + "        \"members\": [\n"
                + "            {\n"
                + "                \"id\": 2,\n"
                + "                \"name\": \"tester\",\n"
                + "                \"username\": \"tester\",\n"
                + "                \"profileDescription\": \"This is a test profile description.\",\n"
                + "                \"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "                \"memberRecords\": [\n"
                + "                    {\n"
                + "                        \"recordId\": 12,\n"
                + "                        \"recordName\": \"Second Record\",\n"
                + "                        \"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "                        \"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"recordId\": 13,\n"
                + "                        \"recordName\": \"First Record\",\n"
                + "                        \"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "                        \"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "                    }\n"
                + "                ]\n"
                + "            }\n"
                + "        ]\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        moumRepository.updateMoum(moumId, moum, files, result -> {
            try {
                assertEquals(Validation.UPDATE_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdateMoum_ShouldReturnFailureResponse_WhenUpdateMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 44;
        Moum moum = new Moum();
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassLoader classLoader = getClass().getClassLoader();
            assertNotNull(classLoader.getResource("image_example.jpeg"));
            File file = new File(classLoader.getResource("image_example.jpeg").getFile());
            files.add(file);
        }
        String mockResponse = "{\n"
                + "    \"status\": 401,\n"
                + "    \"code\": \"F-T005\",\n"
                + "    \"message\": \"팀에 먼저 가입해야 합니다.\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody(mockResponse));

        // When
        moumRepository.updateMoum(moumId, moum, files, result -> {
            try {
                assertEquals(Validation.MUST_JOIN_TEAM_FIRST, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdateMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 44;
        Moum moum = new Moum();
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassLoader classLoader = getClass().getClassLoader();
            assertNotNull(classLoader.getResource("image_example.jpeg"));
            File file = new File(classLoader.getResource("image_example.jpeg").getFile());
            files.add(file);
        }
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.updateMoum(moumId, moum, files, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testDeleteMoum_ShouldReturnSuccessResponse_WhenDeleteMoumSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 20;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-MM004\",\n"
                + "\t\"message\": \"모음 삭제 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"moumId\": 20,\n"
                + "\t\t\"moumName\": \"Summer Concert\",\n"
                + "\t\t\"moumDescription\": \"A live music concert in the summer.\",\n"
                + "\t\t\"performLocation\": \"Seoul Stadium\",\n"
                + "\t\t\"startDate\": \"2024-06-15\",\n"
                + "\t\t\"endDate\": null,\n"
                + "\t\t\"price\": 50000,\n"
                + "\t\t\"imageUrls\": [\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/건강보험 자격득실확인서.jpeg\"\n"
                + "\t\t],\n"
                + "\t\t\"leaderId\": 2,\n"
                + "\t\t\"leaderName\": null,\n"
                + "\t\t\"teamId\": 9,\n"
                + "\t\t\"process\": {\n"
                + "\t\t\t\"recruitStatus\": false,\n"
                + "\t\t\t\"chatroomStatus\": false,\n"
                + "\t\t\t\"practiceroomStatus\": false,\n"
                + "\t\t\t\"performLocationStatus\": false,\n"
                + "\t\t\t\"promoteStatus\": false,\n"
                + "\t\t\t\"paymentStatus\": false,\n"
                + "\t\t\t\"finishStatus\": false,\n"
                + "\t\t\t\"processPercentage\": 0\n"
                + "\t\t},\n"
                + "\t  \"music\": [{\n"
                + "         \"musicName\": \"testmusic\",\n"
                + "         \"artistName\": \"tester\"\n"
                + "\t  }],\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 2,\n"
                + "\t\t\t\t\"name\": \"tester\",\n"
                + "\t\t\t\t\"username\": \"tester\",\n"
                + "\t\t\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "\t\t\t\t\"memberRecords\": [\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 12,\n"
                + "\t\t\t\t\t\t\"recordName\": \"Second Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t},\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 13,\n"
                + "\t\t\t\t\t\t\"recordName\": \"First Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t}\n"
                + "\t\t\t\t]\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        moumRepository.deleteMoum(moumId, result -> {
            try {
                assertEquals(Validation.DELETE_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteMoum_ShouldReturnFailureResponse_WhenDeleteMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 20;
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
        moumRepository.deleteMoum(moumId, result -> {
            try {
                assertEquals(Validation.NO_AUTHORITY, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 20;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.deleteMoum(moumId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testLoadMoum_ShouldReturnSuccessResponse_WhenLoadMoumSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 20;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-MM002\",\n"
                + "\t\"message\": \"모음 조회 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"moumId\": 20,\n"
                + "\t\t\"moumName\": \"Summer Concert\",\n"
                + "\t\t\"moumDescription\": \"A live music concert in the summer.\",\n"
                + "\t\t\"performLocation\": \"Seoul Stadium\",\n"
                + "\t\t\"startDate\": \"2024-06-15\",\n"
                + "\t\t\"endDate\": null,\n"
                + "\t\t\"price\": 50000,\n"
                + "\t\t\"imageUrls\": [\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/건강보험 자격득실확인서.jpeg\"\n"
                + "\t\t],\n"
                + "\t\t\"leaderId\": 2,\n"
                + "\t\t\"leaderName\": null,\n"
                + "\t\t\"teamId\": 9,\n"
                + "\t\t\"process\": {\n"
                + "\t\t\t\"recruitStatus\": false,\n"
                + "\t\t\t\"chatroomStatus\": false,\n"
                + "\t\t\t\"practiceroomStatus\": false,\n"
                + "\t\t\t\"performLocationStatus\": false,\n"
                + "\t\t\t\"promoteStatus\": false,\n"
                + "\t\t\t\"paymentStatus\": false,\n"
                + "\t\t\t\"finishStatus\": false,\n"
                + "\t\t\t\"processPercentage\": 0\n"
                + "\t\t},\n"
                + "\t\t\"music\": [\n"
                + "            {\n"
                + "                \"musicName\": \"testmusic1\",\n"
                + "                \"artistName\": \"tester1\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"musicName\": \"testmusic2\",\n"
                + "                \"artistName\": \"tester2\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"musicName\": \"testmusic3\",\n"
                + "                \"artistName\": \"tester3\"\n"
                + "            }\n"
                + "    ],\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 2,\n"
                + "\t\t\t\t\"name\": \"tester\",\n"
                + "\t\t\t\t\"username\": \"tester\",\n"
                + "\t\t\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "\t\t\t\t\"memberRecords\": [\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 12,\n"
                + "\t\t\t\t\t\t\"recordName\": \"Second Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t},\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 13,\n"
                + "\t\t\t\t\t\t\"recordName\": \"First Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t}\n"
                + "\t\t\t\t]\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        moumRepository.loadMoum(moumId, result -> {
            try {
                assertEquals(Validation.GET_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoum_ShouldReturnFailureResponse_WhenLoadMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 20;
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
        moumRepository.loadMoum(moumId, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 20;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.loadMoum(moumId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/%d", moumId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoumOfTeam_ShouldReturnSuccessResponse_WhenLoadMoumOfTeamSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-MM002\",\n"
                + "    \"message\": \"모음 조회 성공\",\n"
                + "    \"data\": [\n"
                + "        {\n"
                + "            \"moumId\": 1,\n"
                + "            \"moumName\": \"아주대학교 대동제 가을 공연\",\n"
                + "            \"moumDescription\": \"가을 공연 열심히 해봅시다~\uD83D\uDD25\",\n"
                + "            \"performLocation\": \"아주대학교 노천극장\",\n"
                + "            \"startDate\": \"2024-11-27\",\n"
                + "            \"endDate\": \"2024-11-28\",\n"
                + "            \"price\": 5000,\n"
                + "            \"imageUrls\": [\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/아주대학교 대동제 가을 공연/temp_image.jpg\",\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/아주대학교 대동제 가을 공연/temp_image.jpg\",\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/아주대학교 대동제 가을 공연/temp_image.jpg\"\n"
                + "            ],\n"
                + "            \"leaderId\": 1,\n"
                + "            \"leaderName\": null,\n"
                + "            \"teamId\": 1,\n"
                + "            \"process\": {\n"
                + "                \"recruitStatus\": true,\n"
                + "                \"chatroomStatus\": true,\n"
                + "                \"practiceroomStatus\": false,\n"
                + "                \"performLocationStatus\": false,\n"
                + "                \"promoteStatus\": false,\n"
                + "                \"paymentStatus\": false,\n"
                + "                \"finishStatus\": false,\n"
                + "                \"processPercentage\": 28\n"
                + "            },\n"
                + "            \"music\": [\n"
                + "                {\n"
                + "                    \"musicName\": \"너의 의미\",\n"
                + "                    \"artistName\": \"아이유\"\n"
                + "                },\n"
                + "                {\n"
                + "                    \"musicName\": \"주저하는 연인들을 위해\",\n"
                + "                    \"artistName\": \"잔나비\"\n"
                + "                }\n"
                + "            ],\n"
                + "            \"members\": [\n"
                + "                {\n"
                + "                    \"id\": 1,\n"
                + "                    \"name\": \"소성하\",\n"
                + "                    \"username\": \"sosongha3\",\n"
                + "                    \"profileDescription\": \"이클립스 오케스트라 회장 소성하입니다:)\",\n"
                + "                    \"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/sosongha3/temp_image.jpg\",\n"
                + "                    \"exp\": 12,\n"
                + "                    \"genres\": [\n"
                + "                        \"CLASSICAL\"\n"
                + "                    ],\n"
                + "                    \"tier\": \"PLATINUM\",\n"
                + "                    \"videoUrl\": \"https://youtu.be/0M2WL1XF_E0?si=RQOWDm8IpGJkfJzG\",\n"
                + "                    \"memberRecords\": [\n"
                + "                        {\n"
                + "                            \"recordId\": 62,\n"
                + "                            \"recordName\": \"아주대학교 2024 대동제 공연\",\n"
                + "                            \"startDate\": \"2024-11-27\",\n"
                + "                            \"endDate\": \"2024-11-28\"\n"
                + "                        },\n"
                + "                        {\n"
                + "                            \"recordId\": 63,\n"
                + "                            \"recordName\": \"인하아주 연합 공연 'Freak'\",\n"
                + "                            \"startDate\": \"2024-12-10\",\n"
                + "                            \"endDate\": \"2024-12-12\"\n"
                + "                        }\n"
                + "                    ],\n"
                + "                    \"moumRecords\": []\n"
                + "                }\n"
                + "            ],\n"
                + "            \"genre\": \"JAZZ\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"moumId\": 2,\n"
                + "            \"moumName\": \"winter 대학가요제 오디션 1차\",\n"
                + "            \"moumDescription\": \"겨울방학 '대학가요제' 준비해봅시다!\",\n"
                + "            \"performLocation\": \"성남아트홀\",\n"
                + "            \"startDate\": \"2025-01-03\",\n"
                + "            \"endDate\": \"2025-01-04\",\n"
                + "            \"price\": 0,\n"
                + "            \"imageUrls\": [\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/winter 대학가요제 오디션 1차/temp_image4.jpg\",\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/winter 대학가요제 오디션 1차/temp_image5.jpg\"\n"
                + "            ],\n"
                + "            \"leaderId\": 1,\n"
                + "            \"leaderName\": null,\n"
                + "            \"teamId\": 1,\n"
                + "            \"process\": {\n"
                + "                \"recruitStatus\": false,\n"
                + "                \"chatroomStatus\": false,\n"
                + "                \"practiceroomStatus\": false,\n"
                + "                \"performLocationStatus\": false,\n"
                + "                \"promoteStatus\": false,\n"
                + "                \"paymentStatus\": false,\n"
                + "                \"finishStatus\": false,\n"
                + "                \"processPercentage\": 0\n"
                + "            },\n"
                + "            \"music\": [],\n"
                + "            \"members\": [\n"
                + "                {\n"
                + "                    \"id\": 1,\n"
                + "                    \"name\": \"소성하\",\n"
                + "                    \"username\": \"sosongha3\",\n"
                + "                    \"profileDescription\": \"이클립스 오케스트라 회장 소성하입니다:)\",\n"
                + "                    \"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/sosongha3/temp_image.jpg\",\n"
                + "                    \"exp\": 12,\n"
                + "                    \"genres\": [\n"
                + "                        \"CLASSICAL\"\n"
                + "                    ],\n"
                + "                    \"tier\": \"PLATINUM\",\n"
                + "                    \"videoUrl\": \"https://youtu.be/0M2WL1XF_E0?si=RQOWDm8IpGJkfJzG\",\n"
                + "                    \"memberRecords\": [\n"
                + "                        {\n"
                + "                            \"recordId\": 62,\n"
                + "                            \"recordName\": \"아주대학교 2024 대동제 공연\",\n"
                + "                            \"startDate\": \"2024-11-27\",\n"
                + "                            \"endDate\": \"2024-11-28\"\n"
                + "                        },\n"
                + "                        {\n"
                + "                            \"recordId\": 63,\n"
                + "                            \"recordName\": \"인하아주 연합 공연 'Freak'\",\n"
                + "                            \"startDate\": \"2024-12-10\",\n"
                + "                            \"endDate\": \"2024-12-12\"\n"
                + "                        }\n"
                + "                    ],\n"
                + "                    \"moumRecords\": []\n"
                + "                }\n"
                + "            ],\n"
                + "            \"genre\": \"POP\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        moumRepository.loadMoumsOfTeam(teamId, result -> {
            try {
                assertEquals(Validation.GET_MOUM_SUCCESS, result.getValidation());
                assertEquals(2, result.getData().size());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum-all/team/%d", teamId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoumOfTeam_ShouldReturnFailureResponse_WhenLoadMoumOfTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 1;
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
        moumRepository.loadMoumsOfTeam(teamId, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum-all/team/%d", teamId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoumOfTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.loadMoumsOfTeam(teamId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum-all/team/%d", teamId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoumsOfMe_ShouldReturnSuccessResponse_WhenLoadMoumsOfMeSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-MM002\",\n"
                + "    \"message\": \"모음 조회 성공\",\n"
                + "    \"data\": [\n"
                + "        {\n"
                + "            \"moumId\": 1,\n"
                + "            \"moumName\": \"아주대학교 대동제 가을 공연\",\n"
                + "            \"moumDescription\": \"가을 공연 열심히 해봅시다~\uD83D\uDD25\",\n"
                + "            \"performLocation\": \"아주대학교 노천극장\",\n"
                + "            \"startDate\": \"2024-11-27\",\n"
                + "            \"endDate\": \"2024-11-28\",\n"
                + "            \"price\": 5000,\n"
                + "            \"imageUrls\": [\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/아주대학교 대동제 가을 공연/temp_image.jpg\",\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/아주대학교 대동제 가을 공연/temp_image.jpg\",\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/아주대학교 대동제 가을 공연/temp_image.jpg\"\n"
                + "            ],\n"
                + "            \"leaderId\": 1,\n"
                + "            \"leaderName\": null,\n"
                + "            \"teamId\": 1,\n"
                + "            \"process\": {\n"
                + "                \"recruitStatus\": true,\n"
                + "                \"chatroomStatus\": true,\n"
                + "                \"practiceroomStatus\": false,\n"
                + "                \"performLocationStatus\": false,\n"
                + "                \"promoteStatus\": false,\n"
                + "                \"paymentStatus\": false,\n"
                + "                \"finishStatus\": false,\n"
                + "                \"processPercentage\": 28\n"
                + "            },\n"
                + "            \"music\": [\n"
                + "                {\n"
                + "                    \"musicName\": \"너의 의미\",\n"
                + "                    \"artistName\": \"아이유\"\n"
                + "                },\n"
                + "                {\n"
                + "                    \"musicName\": \"주저하는 연인들을 위해\",\n"
                + "                    \"artistName\": \"잔나비\"\n"
                + "                }\n"
                + "            ],\n"
                + "            \"members\": [\n"
                + "                {\n"
                + "                    \"id\": 1,\n"
                + "                    \"name\": \"소성하\",\n"
                + "                    \"username\": \"sosongha3\",\n"
                + "                    \"profileDescription\": \"이클립스 오케스트라 회장 소성하입니다:)\",\n"
                + "                    \"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/sosongha3/temp_image.jpg\",\n"
                + "                    \"exp\": 12,\n"
                + "                    \"genres\": [\n"
                + "                        \"CLASSICAL\"\n"
                + "                    ],\n"
                + "                    \"tier\": \"PLATINUM\",\n"
                + "                    \"videoUrl\": \"https://youtu.be/0M2WL1XF_E0?si=RQOWDm8IpGJkfJzG\",\n"
                + "                    \"memberRecords\": [\n"
                + "                        {\n"
                + "                            \"recordId\": 62,\n"
                + "                            \"recordName\": \"아주대학교 2024 대동제 공연\",\n"
                + "                            \"startDate\": \"2024-11-27\",\n"
                + "                            \"endDate\": \"2024-11-28\"\n"
                + "                        },\n"
                + "                        {\n"
                + "                            \"recordId\": 63,\n"
                + "                            \"recordName\": \"인하아주 연합 공연 'Freak'\",\n"
                + "                            \"startDate\": \"2024-12-10\",\n"
                + "                            \"endDate\": \"2024-12-12\"\n"
                + "                        }\n"
                + "                    ],\n"
                + "                    \"moumRecords\": []\n"
                + "                }\n"
                + "            ],\n"
                + "            \"genre\": \"JAZZ\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"moumId\": 2,\n"
                + "            \"moumName\": \"winter 대학가요제 오디션 1차\",\n"
                + "            \"moumDescription\": \"겨울방학 '대학가요제' 준비해봅시다!\",\n"
                + "            \"performLocation\": \"성남아트홀\",\n"
                + "            \"startDate\": \"2025-01-03\",\n"
                + "            \"endDate\": \"2025-01-04\",\n"
                + "            \"price\": 0,\n"
                + "            \"imageUrls\": [\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/winter 대학가요제 오디션 1차/temp_image4.jpg\",\n"
                + "                \"https://kr.object.ncloudstorage.com/moumstorage/moums/winter 대학가요제 오디션 1차/temp_image5.jpg\"\n"
                + "            ],\n"
                + "            \"leaderId\": 1,\n"
                + "            \"leaderName\": null,\n"
                + "            \"teamId\": 1,\n"
                + "            \"process\": {\n"
                + "                \"recruitStatus\": false,\n"
                + "                \"chatroomStatus\": false,\n"
                + "                \"practiceroomStatus\": false,\n"
                + "                \"performLocationStatus\": false,\n"
                + "                \"promoteStatus\": false,\n"
                + "                \"paymentStatus\": false,\n"
                + "                \"finishStatus\": false,\n"
                + "                \"processPercentage\": 0\n"
                + "            },\n"
                + "            \"music\": [],\n"
                + "            \"members\": [\n"
                + "                {\n"
                + "                    \"id\": 1,\n"
                + "                    \"name\": \"소성하\",\n"
                + "                    \"username\": \"sosongha3\",\n"
                + "                    \"profileDescription\": \"이클립스 오케스트라 회장 소성하입니다:)\",\n"
                + "                    \"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/sosongha3/temp_image.jpg\",\n"
                + "                    \"exp\": 12,\n"
                + "                    \"genres\": [\n"
                + "                        \"CLASSICAL\"\n"
                + "                    ],\n"
                + "                    \"tier\": \"PLATINUM\",\n"
                + "                    \"videoUrl\": \"https://youtu.be/0M2WL1XF_E0?si=RQOWDm8IpGJkfJzG\",\n"
                + "                    \"memberRecords\": [\n"
                + "                        {\n"
                + "                            \"recordId\": 62,\n"
                + "                            \"recordName\": \"아주대학교 2024 대동제 공연\",\n"
                + "                            \"startDate\": \"2024-11-27\",\n"
                + "                            \"endDate\": \"2024-11-28\"\n"
                + "                        },\n"
                + "                        {\n"
                + "                            \"recordId\": 63,\n"
                + "                            \"recordName\": \"인하아주 연합 공연 'Freak'\",\n"
                + "                            \"startDate\": \"2024-12-10\",\n"
                + "                            \"endDate\": \"2024-12-12\"\n"
                + "                        }\n"
                + "                    ],\n"
                + "                    \"moumRecords\": []\n"
                + "                }\n"
                + "            ],\n"
                + "            \"genre\": \"POP\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        moumRepository.loadMoumsAll(result -> {
            try {
                assertEquals(Validation.GET_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/moum-all/my", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoumsOfMe_ShouldReturnFailureResponse_WhenLoadMoumsOfMeFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 1;
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
        moumRepository.loadMoumsAll(result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/moum-all/my", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMoumsOfMe_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.loadMoumsAll(result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/moum-all/my", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testFinishMoum_ShouldReturnSuccessResponse_WhenFinishMoumSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 10;
        String mockResponse = "{\n"
                + "\t\"status\": 201,\n"
                + "\t\"code\": \"S-MM005\",\n"
                + "\t\"message\": \"모음 마감하기 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"moumId\": 10,\n"
                + "\t\t\"moumName\": \"테스트모음5\",\n"
                + "\t\t\"moumDescription\": \"A live music concert in the summer.\",\n"
                + "\t\t\"performLocation\": \"Seoul Stadium\",\n"
                + "\t\t\"startDate\": \"2024-06-15\",\n"
                + "\t\t\"endDate\": null,\n"
                + "\t\t\"price\": 50000,\n"
                + "\t\t\"imageUrls\": [\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/테스트모음5/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/테스트모음5/건강보험 자격득실확인서.jpeg\"\n"
                + "\t\t],\n"
                + "\t\t\"leaderId\": 1,\n"
                + "\t\t\"leaderName\": null,\n"
                + "\t\t\"teamId\": 1,\n"
                + "\t\t\"process\": {\n"
                + "\t\t\t\"recruitStatus\": false,\n"
                + "\t\t\t\"chatroomStatus\": false,\n"
                + "\t\t\t\"practiceroomStatus\": false,\n"
                + "\t\t\t\"performLocationStatus\": false,\n"
                + "\t\t\t\"promoteStatus\": false,\n"
                + "\t\t\t\"paymentStatus\": false,\n"
                + "\t\t\t\"finishStatus\": true,\n"
                + "\t\t\t\"processPercentage\": 14\n"
                + "\t\t},\n"
                + "\t\t\"music\": [{\n"
                + "         \"musicName\": \"testmusic\",\n"
                + "         \"artistName\": \"tester\"\n"
                + "    }],\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 1,\n"
                + "\t\t\t\t\"name\": \"tester1\",\n"
                + "\t\t\t\t\"username\": \"tester1\",\n"
                + "\t\t\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester1/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\t\"memberRecords\": [\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 1,\n"
                + "\t\t\t\t\t\t\"recordName\": \"테스트모음5\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2024-06-15\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2024-11-13\"\n"
                + "\t\t\t\t\t}\n"
                + "\t\t\t\t]\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        moumRepository.finishMoum(moumId, result -> {
            try {
                assertEquals(Validation.FINISH_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/finish/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testFinishMoum_ShouldReturnFailureResponse_WhenFinishMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 10;
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
        moumRepository.finishMoum(moumId, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/finish/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testFinishMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 10;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.finishMoum(moumId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/finish/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testReopenMoum_ShouldReturnSuccessResponse_WhenReopenMoumSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 10;
        String mockResponse = "{\n"
                + "\t\"status\": 201,\n"
                + "\t\"code\": \"S-MM006\",\n"
                + "\t\"message\": \"모음 되살리기 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"moumId\": 10,\n"
                + "\t\t\"moumName\": \"테스트모음5\",\n"
                + "\t\t\"moumDescription\": \"A live music concert in the summer.\",\n"
                + "\t\t\"performLocation\": \"Seoul Stadium\",\n"
                + "\t\t\"startDate\": \"2024-06-15\",\n"
                + "\t\t\"endDate\": null,\n"
                + "\t\t\"price\": 50000,\n"
                + "\t\t\"imageUrls\": [\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/테스트모음5/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/테스트모음5/건강보험 자격득실확인서.jpeg\"\n"
                + "\t\t],\n"
                + "\t\t\"leaderId\": 1,\n"
                + "\t\t\"leaderName\": null,\n"
                + "\t\t\"teamId\": 1,\n"
                + "\t\t\"process\": {\n"
                + "\t\t\t\"recruitStatus\": false,\n"
                + "\t\t\t\"chatroomStatus\": false,\n"
                + "\t\t\t\"practiceroomStatus\": false,\n"
                + "\t\t\t\"performLocationStatus\": false,\n"
                + "\t\t\t\"promoteStatus\": false,\n"
                + "\t\t\t\"paymentStatus\": false,\n"
                + "\t\t\t\"finishStatus\": false,\n"
                + "\t\t\t\"processPercentage\": 0\n"
                + "\t\t},\n"
                + "\t  \"music\": [{\n"
                + "         \"musicName\": \"testmusic\",\n"
                + "         \"artistName\": \"tester\"\n"
                + "\t  }],\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 1,\n"
                + "\t\t\t\t\"name\": \"tester1\",\n"
                + "\t\t\t\t\"username\": \"tester1\",\n"
                + "\t\t\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester1/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\t\"memberRecords\": []\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        moumRepository.reopenMoum(moumId, result -> {
            try {
                assertEquals(Validation.REOPEN_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/reopen/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testReopenMoum_ShouldReturnFailureResponse_WhenReopenMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 10;
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
        moumRepository.reopenMoum(moumId, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/reopen/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testReopenMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 10;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        moumRepository.reopenMoum(moumId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/reopen/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdateProcessMoum_ShouldReturnSuccessResponse_WhenUpdateProcessMoumSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 22;
        Moum.Process process = new Moum.Process();
        String mockResponse = "{\n"
                + "\t\"status\": 201,\n"
                + "\t\"code\": \"S-MM007\",\n"
                + "\t\"message\": \"모음 진행률 상태 수정 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"moumId\": 22,\n"
                + "\t\t\"moumName\": \"Summer Concert\",\n"
                + "\t\t\"moumDescription\": \"A live music concert in the summer.\",\n"
                + "\t\t\"performLocation\": \"Seoul Stadium\",\n"
                + "\t\t\"startDate\": \"2024-06-15\",\n"
                + "\t\t\"endDate\": null,\n"
                + "\t\t\"price\": 50000,\n"
                + "\t\t\"imageUrls\": [\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\"https://kr.object.ncloudstorage.com/moumstorage/moums/Summer Concert/건강보험 자격득실확인서.jpeg\"\n"
                + "\t\t],\n"
                + "\t\t\"leaderId\": 2,\n"
                + "\t\t\"leaderName\": null,\n"
                + "\t\t\"teamId\": 9,\n"
                + "\t\t\"process\": {\n"
                + "\t\t\t\"recruitStatus\": true,\n"
                + "\t\t\t\"chatroomStatus\": true,\n"
                + "\t\t\t\"practiceroomStatus\": false,\n"
                + "\t\t\t\"performLocationStatus\": false,\n"
                + "\t\t\t\"promoteStatus\": false,\n"
                + "\t\t\t\"paymentStatus\": false,\n"
                + "\t\t\t\"finishStatus\": false,\n"
                + "\t\t\t\"processPercentage\": 28\n"
                + "\t\t},\n"
                + "   \t\"music\": [{\n"
                + "         \"musicName\": \"testmusic\",\n"
                + "         \"artistName\": \"tester\"\n"
                + "\t  }],\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 2,\n"
                + "\t\t\t\t\"name\": \"tester\",\n"
                + "\t\t\t\t\"username\": \"tester\",\n"
                + "\t\t\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "\t\t\t\t\"memberRecords\": [\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 12,\n"
                + "\t\t\t\t\t\t\"recordName\": \"Second Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t},\n"
                + "\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"recordId\": 13,\n"
                + "\t\t\t\t\t\t\"recordName\": \"First Record\",\n"
                + "\t\t\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t}\n"
                + "\t\t\t\t]\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        moumRepository.updateProcessMoum(moumId, process, result -> {
            try {
                assertEquals(Validation.UPDATE_PROCESS_MOUM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/update-process/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdateProcessMoum_ShouldReturnFailureResponse_WhenUpdateProcessMoumFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 22;
        Moum.Process process = new Moum.Process();
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
        moumRepository.updateProcessMoum(moumId, process, result -> {
            try {
                assertEquals(Validation.ILLEGAL_ARGUMENT, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/update-process/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdateProcessMoum_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer moumId = 22;
        Moum.Process process = new Moum.Process();
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));
        // When
        moumRepository.updateProcessMoum(moumId, process, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/moum/update-process/%d", moumId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }
}
