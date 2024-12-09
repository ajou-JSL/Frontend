package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.PerformRepository;
import com.example.moum.repository.TeamRepository;
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
public class TeamRepositoryTest {
    private MockWebServer mockWebServer;
    private TeamRepository teamRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        teamRepository = new TeamRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testCreateTeam_ShouldReturnSuccessResponse_WhenLCreateTeam_Success() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Team team = new Team();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-T001\",\n"
                + "    \"message\": \"팀 생성 성공.\",\n"
                + "    \"data\": {\n"
                + "        \"teamId\": 7,\n"
                + "        \"leaderId\": 2,\n"
                + "        \"teamName\": \"Test Team\",\n"
                + "        \"description\": \"This is a test team.\",\n"
                + "        \"genre\": \"Rock\",\n"
                + "        \"location\": \"Seoul\",\n"
                + "        \"createdAt\": \"2024-11-10T17:32:28.757673\",\n"
                + "        \"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/teams/Test Team/통ㅇ장사본.jpeg\",\n"
                + "        \"members\": [],\n"
                + "        \"records\": [\n"
                + "            {\n"
                + "                \"recordId\": 18,\n"
                + "                \"recordName\": \"First Record\",\n"
                + "                \"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "                \"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"recordId\": 19,\n"
                + "                \"recordName\": \"Second Record\",\n"
                + "                \"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "                \"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "            }\n"
                + "        ]\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        teamRepository.createTeam(team, file, result -> {
            try {
                assertEquals(Validation.CREATE_TEAM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/teams", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateTeam_ShouldReturnFailureResponse_WhenCreateTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Team team = new Team();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "    \"status\": 403,\n"
                + "    \"code\": \"F-C001\",\n"
                + "    \"message\": \"INTERNAL_SERVER_ERROR\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setBody(mockResponse));

        // When
        teamRepository.createTeam(team, file, result -> {
            try {
                assertEquals(Validation.INTERNAL_SERVER_ERROR, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/teams", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testCreateTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Team team = new Team();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.createTeam(team, file, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/teams", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testUpdateTeam_ShouldReturnSuccessResponse_WhenUpdateTeamSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
        Team team = new Team();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-T002\",\n"
                + "    \"message\": \"팀 정보 수정 성공.\",\n"
                + "    \"data\": {\n"
                + "        \"teamId\": 7,\n"
                + "        \"leaderId\": 2,\n"
                + "        \"teamName\": \"Test Team Updated\",\n"
                + "        \"description\": \"This is an updated test team.\",\n"
                + "        \"genre\": \"Rock\",\n"
                + "        \"location\": \"Seoul\",\n"
                + "        \"createdAt\": \"2024-11-10T17:32:28.757673\",\n"
                + "        \"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/teams/Test Team/통ㅇ장사본.jpeg\",\n"
                + "        \"records\": [\n"
                + "            {\n"
                + "                \"recordId\": 0,\n"
                + "                \"recordName\": \"Updated First Record\",\n"
                + "                \"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "                \"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "            }\n"
                + "        ]\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        teamRepository.updateTeam(teamId, team, file, result -> {
            try {
                assertEquals(Validation.UPDATE_TEAM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdateTeam_ShouldReturnFailureResponse_WhenUpdateTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
        Team team = new Team();
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
        teamRepository.updateTeam(teamId, team, file, result -> {
            try {
                assertEquals(Validation.TEAM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testUpdateTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
        Team team = new Team();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.updateTeam(teamId, team, file, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testDeleteTeam_ShouldReturnSuccessResponse_WhenDeleteTeamSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-T003\",\n"
                + "\t\"message\": \"팀 삭제 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"teamId\": 7,\n"
                + "\t\t\"leaderId\": 2,\n"
                + "\t\t\"teamName\": \"Test Team Updated\",\n"
                + "\t\t\"description\": \"This is an updated test team.\",\n"
                + "\t\t\"genre\": \"Rock\",\n"
                + "\t\t\"location\": \"Seoul\",\n"
                + "\t\t\"createdAt\": \"2024-11-10T17:32:28.757673\",\n"
                + "\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/teams/Test Team/통ㅇ장사본.jpeg\",\n"
                + "\t\t\"members\": [],\n"
                + "\t\t\"records\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 20,\n"
                + "\t\t\t\t\"recordName\": \"Updated First Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        teamRepository.deleteTeam(teamId, result -> {
            try {
                assertEquals(Validation.DELETE_TEAM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteTeam_ShouldReturnFailureResponse_WhenDeleteTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
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
        teamRepository.deleteTeam(teamId, result -> {
            try {
                assertEquals(Validation.TEAM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testDeleteTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.deleteTeam(teamId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testLoadTeam_ShouldReturnSuccessResponse_WhenLoadTeamSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-T005\",\n"
                + "\t\"message\": \"팀 단건 조회 성공.\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"teamId\": 7,\n"
                + "\t\t\"leaderId\": 2,\n"
                + "\t\t\"teamName\": \"Test Team Updated\",\n"
                + "\t\t\"description\": \"This is an updated test team.\",\n"
                + "\t\t\"genre\": \"Rock\",\n"
                + "\t\t\"location\": \"Seoul\",\n"
                + "\t\t\"createdAt\": \"2024-11-10T17:32:28.757673\",\n"
                + "\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/teams/Test Team/통ㅇ장사본.jpeg\",\n"
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
                + "\t\t\"records\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 20,\n"
                + "\t\t\t\t\"recordName\": \"Updated First Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        teamRepository.loadTeam(teamId, result -> {
            try {
                assertEquals(Validation.GET_TEAM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadTeam_ShouldReturnFailureResponse_WhenLoadTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
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
        teamRepository.loadTeam(teamId, result -> {
            try {
                assertEquals(Validation.TEAM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 7;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.loadTeam(teamId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d", teamId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadTeamsAsMember_ShouldReturnSuccessResponse_WhenLoadTeamsAsMemberSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-T006\",\n"
                + "\t\"message\": \"전체 팀 리스트 조회 성공\",\n"
                + "\t\"data\": [\n"
                + "\t\t{\n"
                + "\t\t\t\"teamId\": 7,\n"
                + "\t\t\t\"leaderId\": 2,\n"
                + "\t\t\t\"teamName\": \"Test Team Updated\",\n"
                + "\t\t\t\"description\": \"This is an updated test team.\",\n"
                + "\t\t\t\"genre\": \"Rock\",\n"
                + "\t\t\t\"location\": \"Seoul\",\n"
                + "\t\t\t\"createdAt\": \"2024-11-10T17:32:28.757673\",\n"
                + "\t\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/teams/Test Team/통ㅇ장사본.jpeg\",\n"
                + "\t\t\t\"members\": [\n"
                + "\t\t\t\t{\n"
                + "\t\t\t\t\t\"id\": 2,\n"
                + "\t\t\t\t\t\"name\": \"tester\",\n"
                + "\t\t\t\t\t\"username\": \"tester\",\n"
                + "\t\t\t\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "\t\t\t\t\t\"memberRecords\": [\n"
                + "\t\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\t\"recordId\": 12,\n"
                + "\t\t\t\t\t\t\t\"recordName\": \"Second Record\",\n"
                + "\t\t\t\t\t\t\t\"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\t\"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t\t},\n"
                + "\t\t\t\t\t\t{\n"
                + "\t\t\t\t\t\t\t\"recordId\": 13,\n"
                + "\t\t\t\t\t\t\t\"recordName\": \"First Record\",\n"
                + "\t\t\t\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t]\n"
                + "\t\t\t\t}\n"
                + "\t\t\t],\n"
                + "\t\t\t\"memberRecords\": [\n"
                + "\t\t\t\t{\n"
                + "\t\t\t\t\t\"recordId\": 20,\n"
                + "\t\t\t\t\t\"recordName\": \"Updated First Record\",\n"
                + "\t\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t\t}\n"
                + "\t\t\t]\n"
                + "\t\t}\n"
                + "\t]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        teamRepository.loadTeamsAsMember(memberId, result -> {
            try {
                assertEquals(Validation.GET_TEAM_LIST_SUCCESS, result.getValidation());
                assertEquals(1, result.getData().size());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams-all/%d", memberId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadTeamsAsMember_ShouldReturnFailureResponse_WhenLoadTeamsAsMemberFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
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
        teamRepository.loadTeamsAsMember(memberId, result -> {
            try {
                assertEquals(Validation.TEAM_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams-all/%d", memberId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadTeamsAsMember_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.loadTeamsAsMember(memberId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams-all/%d", memberId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testKickMemberFromTeam_ShouldReturnSuccessResponse_WhenKickMemberFromTeamSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        Integer memberId = 2;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-T009\",\n"
                + "\t\"message\": \"멤버를 팀에서 강퇴하였습니다.\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"teamId\": 6,\n"
                + "\t\t\"leaderId\": 1,\n"
                + "\t\t\"teamName\": \"테스트팀\",\n"
                + "\t\t\"description\": \"test description\",\n"
                + "\t\t\"genre\":\"ROCK\",\n"
                + "\t\t\"location\":\"활동지역\",\n"
                + "\t\t\"createdAt\": \"2024-11-08T20:47:03.585441\",\n"
                + "\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/teams/테스트팀/통ㅇ장사본.jpeg\",\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 1,\n"
                + "\t\t\t\t\"name\": \"tester\",\n"
                + "\t\t\t\t\"username\": \"tester\",\n"
                + "\t\t\t\t\"profileDescription\": \"description\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\"\n"
                + "\t\t\t}\n"
                + "\t\t],\n"
                + "\t\t\"records\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 20,\n"
                + "\t\t\t\t\"recordName\": \"Updated First Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        teamRepository.kickMemberFromTeam(teamId, memberId, result -> {
            try {
                assertEquals(Validation.KICK_MEMBER_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d/kick/%d", teamId, memberId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testKickMemberFromTeam_ShouldReturnFailureResponse_WhenKickMemberFromTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        Integer memberId = 2;
        String mockResponse = "{\n"
                + "\t\"status\": 404,\n"
                + "\t\"code\": \"F-M001\",\n"
                + "\t\"message\": \"존재하지 않은 회원입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        teamRepository.kickMemberFromTeam(teamId, memberId, result -> {
            try {
                assertEquals(Validation.MEMBER_NOT_EXIST, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d/kick/%d", teamId, memberId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testKickMemberFromTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        Integer memberId = 2;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.kickMemberFromTeam(teamId, memberId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d/kick/%d", teamId, memberId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testLeaveTeam_ShouldReturnSuccessResponse_WhenLeaveTeamSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-T010\",\n"
                + "\t\"message\": \"팀에서 탈퇴했습니다.\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"teamId\": 6,\n"
                + "\t\t\"leaderId\": 1,\n"
                + "\t\t\"teamName\": \"테스트팀\",\n"
                + "\t\t\"description\": \"test description\",\n"
                + "\t\t\"genre\":\"POP\",\n"
                + "\t\t\"location\":\"활동지역\",\n"
                + "\t\t\"createdAt\": \"2024-11-08T20:47:03.585441\",\n"
                + "\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/teams/테스트팀/통ㅇ장사본.jpeg\",\n"
                + "\t\t\"members\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"id\": 1,\n"
                + "\t\t\t\t\"name\": \"tester\",\n"
                + "\t\t\t\t\"username\": \"tester\",\n"
                + "\t\t\t\t\"profileDescription\": \"description\",\n"
                + "\t\t\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\"\n"
                + "\t\t\t}\n"
                + "\t\t],\n"
                + "\t\t\"records\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 20,\n"
                + "\t\t\t\t\"recordName\": \"Updated First Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t}\n"
                + "\t\t]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        teamRepository.leaveTeam(teamId, result -> {
            try {
                assertEquals(Validation.LEAVE_TEAM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/leave/%d", teamId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testLeaveTeam_ShouldReturnFailureResponse_WhenLeaveTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        String mockResponse = "{\n"
                + "\t\"status\": 404,\n"
                + "\t\"code\": \"F-T003\",\n"
                + "\t\"message\": \"팀에 속한 멤버가 아닙니다.\",\n"
                + "\t\"errors\": []\n"
                + "}\n";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        teamRepository.leaveTeam(teamId, result -> {
            try {
                assertEquals(Validation.NOT_TEAM_MEMBER, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/leave/%d", teamId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testLeaveTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.leaveTeam(teamId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/leave/%d", teamId), request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testInviteMemberToTeam_ShouldReturnSuccessResponse_WhenInviteMemberToTeamSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        Integer memberId = 2;
        String mockResponse = "{\n"
                + "\t\"status\": 201,\n"
                + "\t\"code\": \"S-T008\",\n"
                + "\t\"message\": \"멤버를 팀에 초대했습니다.\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"id\": 2,\n"
                + "\t\t\"name\": \"tester2\",\n"
                + "\t\t\"username\": \"tester2\",\n"
                + "\t\t\"profileDescription\": \"description\",\n"
                + "\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester2/\"\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        teamRepository.inviteMemberToTeam(teamId, memberId, result -> {
            try {
                assertEquals(Validation.INVITE_MEMBER_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d/invite/%d", teamId, memberId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testInviteMemberToTeam_ShouldReturnFailureResponse_WhenInviteMemberToTeamFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        Integer memberId = 2;
        String mockResponse = "{\n"
                + "\t\"status\": 404,\n"
                + "\t\"code\": \"F-M001\",\n"
                + "\t\"message\": \"존재하지 않은 회원입니다.\",\n"
                + "\t\"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        teamRepository.inviteMemberToTeam(teamId, memberId, result -> {
            try {
                assertEquals(Validation.MEMBER_NOT_EXIST, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d/invite/%d", teamId, memberId), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testInviteMemberToTeam_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer teamId = 6;
        Integer memberId = 2;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        teamRepository.inviteMemberToTeam(teamId, memberId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/teams/%d/invite/%d", teamId, memberId), request.getPath());
        assertEquals("POST", request.getMethod());
    }
}
