package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.repository.ProfileRepository;
import com.example.moum.repository.SignupRepository;
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
public class ProfileRepositoryTest {
    private MockWebServer mockWebServer;
    private ProfileRepository profileRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        profileRepository = new ProfileRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testLoadMemberProfile_ShouldReturnSuccessResponse_WhenLoadMemberProfileSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-P001\",\n"
                + "\t\"message\": \"프로필 조회 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"id\": 2,\n"
                + "\t\t\"name\": \"tester\",\n"
                + "\t\t\"username\": \"tester\",\n"
                + "\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\"email\": \"test@gmail.com\",\n"
                + "\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "\t\t\"proficiency\": \"10\",\n"
                + "\t\t\"videoUrl\": \"https://www.youtube.com/watch?v=Q7R8P_HuQ-M\",\n"
                + "\t\t\"instrument\": \"piano\",\n"
                + "\t\t\"address\": \"123 Test Street\",\n"
                + "\t\t\"teams\": [],\n"
                + "\t\t\"memberRecords\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 12,\n"
                + "\t\t\t\t\"recordName\": \"Second Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "\t\t\t},\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 13,\n"
                + "\t\t\t\t\"recordName\": \"First Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t}\n"
                + "\t\t],\n"
                + "\t\t\"moumRecords\": [],\n"
                + "\t\t\"genres\" : [\"POP\",\"ROCK\"]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        profileRepository.loadMemberProfile(memberId, result -> {
            try {
                assertEquals(Validation.GET_PROFILE_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles/2", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMemberProfile_ShouldReturnFailureResponse_WhenLoadMemberProfileFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
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
        profileRepository.loadMemberProfile(memberId, result -> {
            try {
                assertEquals(Validation.MEMBER_NOT_EXIST, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles/2", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadMemberProfile_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        profileRepository.loadMemberProfile(memberId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles/2", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testUpdateMemberProfile_ShouldReturnSuccessResponse_WhenUpdateMemberProfileSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
        Member member = new Member();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-P002\",\n"
                + "\t\"message\": \"프로필 수정 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"id\": 2,\n"
                + "\t\t\"name\": \"tester\",\n"
                + "\t\t\"username\": \"tester\",\n"
                + "\t\t\"profileDescription\": \"This is a test profile description.\",\n"
                + "\t\t\"email\": \"test@gmail.com\",\n"
                + "\t\t\"profileImageUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/tester/\",\n"
                + "\t\t\"proficiency\": \"10\",\n"
                + "\t\t\"videoUrl\": \"https://www.youtube.com/watch?v=Q7R8P_HuQ-M\",\n"
                + "\t\t\"instrument\": \"piano\",\n"
                + "\t\t\"address\": \"123 Test Street\",\n"
                + "\t\t\"teams\": [],\n"
                + "\t\t\"memberRecords\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 12,\n"
                + "\t\t\t\t\"recordName\": \"Second Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-21T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-12-01T00:00:00.000+00:00\"\n"
                + "\t\t\t},\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"recordId\": 13,\n"
                + "\t\t\t\t\"recordName\": \"First Record\",\n"
                + "\t\t\t\t\"startDate\": \"2023-11-10T00:00:00.000+00:00\",\n"
                + "\t\t\t\t\"endDate\": \"2023-11-20T00:00:00.000+00:00\"\n"
                + "\t\t\t}\n"
                + "\t\t],\n"
                + "\t\t\"moumRecords\": [],\n"
                + "\t\t\"genres\" : [\"POP\",\"ROCK\"]\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        profileRepository.updateMemberProfile(memberId, file, member, result -> {
            try {
                assertEquals(Validation.UPDATE_PROFILE_SUCCESS, result.getValidation());
                assertEquals(2, result.getData().getMemberRecords().size());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles/2", request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testtUpdateMemberProfile_ShouldReturnFailureResponse_WhentUpdateMemberProfileFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
        Member member = new Member();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
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
        profileRepository.updateMemberProfile(memberId, file, member, result -> {
            try {
                assertEquals(Validation.NO_AUTHORITY, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles/2", request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testtUpdateMemberProfile_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer memberId = 2;
        Member member = new Member();
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader.getResource("image_example.jpeg"));
        File file = new File(classLoader.getResource("image_example.jpeg").getFile());
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        profileRepository.updateMemberProfile(memberId, file, member, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles/2", request.getPath());
        assertEquals("PATCH", request.getMethod());
    }

    @Test
    public void testLoadMembersByRank_ShouldReturnSuccessResponse_WhenLoadMembersByRankSuccess() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        String mockResponse = "{\n"
                + "\t\"status\": 200,\n"
                + "\t\"code\": \"S-P001\",\n"
                + "\t\"message\": \"프로필 조회 성공\",\n"
                + "\t\"data\": {\n"
                + "\t\t\"content\": [\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"memberId\": 8,\n"
                + "\t\t\t\t\"memberName\": \"피리부는사나이\",\n"
                + "\t\t\t\t\"memberUsername\": \"qwer12345\",\n"
                + "\t\t\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/qwer12345/temp_image4.jpg\",\n"
                + "\t\t\t\t\"exp\": 15,\n"
                + "\t\t\t\t\"tier\": \"PLATINUM\"\n"
                + "\t\t\t},\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"memberId\": 5,\n"
                + "\t\t\t\t\"memberName\": \"김재훈\",\n"
                + "\t\t\t\t\"memberUsername\": \"kjaehoon777\",\n"
                + "\t\t\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/kjaehoon777/temp_image1.jpg\",\n"
                + "\t\t\t\t\"exp\": 14,\n"
                + "\t\t\t\t\"tier\": \"PLATINUM\"\n"
                + "\t\t\t},\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"memberId\": 2,\n"
                + "\t\t\t\t\"memberName\": \"장재혁\",\n"
                + "\t\t\t\t\"memberUsername\": \"cjaehyuk4zed\",\n"
                + "\t\t\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/cjaehyuk4zed/temp_image1.jpg\",\n"
                + "\t\t\t\t\"exp\": 6,\n"
                + "\t\t\t\t\"tier\": \"SILVER\"\n"
                + "\t\t\t},\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"memberId\": 1,\n"
                + "\t\t\t\t\"memberName\": \"소성하\",\n"
                + "\t\t\t\t\"memberUsername\": \"sosongha3\",\n"
                + "\t\t\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/sosongha3/temp_image.jpg\",\n"
                + "\t\t\t\t\"exp\": 4,\n"
                + "\t\t\t\t\"tier\": \"SILVER\"\n"
                + "\t\t\t},\n"
                + "\t\t\t{\n"
                + "\t\t\t\t\"memberId\": 4,\n"
                + "\t\t\t\t\"memberName\": \"Jason Eich\",\n"
                + "\t\t\t\t\"memberUsername\": \"jeieichi777\",\n"
                + "\t\t\t\t\"fileUrl\": \"https://kr.object.ncloudstorage.com/moumstorage/profiles/jeieichi777/temp_image0.jpg\",\n"
                + "\t\t\t\t\"exp\": 3,\n"
                + "\t\t\t\t\"tier\": \"SILVER\"\n"
                + "\t\t\t}\n"
                + "\t\t],\n"
                + "\t\t\"pageable\": {\n"
                + "\t\t\t\"pageNumber\": 0,\n"
                + "\t\t\t\"pageSize\": 5,\n"
                + "\t\t\t\"sort\": {\n"
                + "\t\t\t\t\"sorted\": false,\n"
                + "\t\t\t\t\"empty\": true,\n"
                + "\t\t\t\t\"unsorted\": true\n"
                + "\t\t\t},\n"
                + "\t\t\t\"offset\": 0,\n"
                + "\t\t\t\"paged\": true,\n"
                + "\t\t\t\"unpaged\": false\n"
                + "\t\t},\n"
                + "\t\t\"last\": false,\n"
                + "\t\t\"totalPages\": 3,\n"
                + "\t\t\"totalElements\": 11,\n"
                + "\t\t\"size\": 5,\n"
                + "\t\t\"number\": 0,\n"
                + "\t\t\"sort\": {\n"
                + "\t\t\t\"sorted\": false,\n"
                + "\t\t\t\"empty\": true,\n"
                + "\t\t\t\"unsorted\": true\n"
                + "\t\t},\n"
                + "\t\t\"first\": true,\n"
                + "\t\t\"numberOfElements\": 5,\n"
                + "\t\t\"empty\": false\n"
                + "\t}\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        profileRepository.loadMembersByRank(page, size, result -> {
            try {
                assertEquals(Validation.GET_PROFILE_SUCCESS, result.getValidation());
                assertEquals(5, result.getData().size());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles-all/rank?page=0&size=10", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testULoadMembersByRank_ShouldReturnFailureResponse_WhentLoadMembersByRankFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
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
        profileRepository.loadMembersByRank(page, size, result -> {
            try {
                assertEquals(Validation.NO_AUTHORITY, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles-all/rank?page=0&size=10", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testULoadMembersByRank_ShouldReturnNetworkFailureResponse_WhenNetworkFailure() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        profileRepository.loadMembersByRank(page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/profiles-all/rank?page=0&size=10", request.getPath());
        assertEquals("GET", request.getMethod());
    }
}
