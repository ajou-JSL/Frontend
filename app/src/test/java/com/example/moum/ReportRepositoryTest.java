package com.example.moum;

import static org.junit.Assert.assertEquals;

import com.example.moum.data.dto.SearchPerformHallArgs;
import com.example.moum.data.dto.SearchPracticeroomArgs;
import com.example.moum.repository.PracticeNPerformRepository;
import com.example.moum.repository.ReportRepository;
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
public class ReportRepositoryTest {
    private MockWebServer mockWebServer;
    private ReportRepository reportRepository;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);

        RetrofitClientManager retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(mockWebServer.url("/").toString());
        reportRepository = new ReportRepository(retrofitClientManager);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testReportMember_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-RP002\",\n"
                + "    \"message\": \"사용자 신고 성공\",\n"
                + "    \"data\": {\n"
                + "    \t\t\"id\": 1,\n"
                + "        \"memberId\": 7,\n"
                + "        \"memberUsername\": \"tester5\",\n"
                + "        \"reporterId\": 2,\n"
                + "        \"reporterUsername\": \"kimajou\",\n"
                + "        \"type\": \"선정성 또는 폭력성\",\n"
                + "        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "        \"reply\": null,\n"
                + "        \"resolved\" : false\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        reportRepository.reportMember(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.REPORT_MEMBER_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testReportMember_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-RP002\",\n"
                + "    \"message\": \"사용자 신고 오류\",\n"
                + "    \"errors\": []\n"
                + "}\n";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        reportRepository.reportMember(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.REPORT_MEMBER_FAIL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testReportMember_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));
        // When
        reportRepository.reportMember(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLoadReportMembers_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-RP022\",\n"
                + "    \"message\": \"사용자 신고 조회 성공\",\n"
                + "    \"data\": [\n"
                + "        {\n"
                + "            \"id\": 1,\n"
                + "            \"memberId\": 21,\n"
                + "            \"memberUsername\": \"tester21\",\n"
                + "            \"reporterId\": 2,\n"
                + "            \"reporterUsername\": \"kimajou\",\n"
                + "            \"type\": \"선정성 또는 폭력성\",\n"
                + "            \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "            \"reply\": \"해당 사용자에 대한 제제를 완료했습니다.\",\n"
                + "            \"resolved\": true\n"
                + "        },\n"
                + "        {\n"
                + "            \"id\": 2,\n"
                + "            \"memberId\": 7,\n"
                + "            \"memberUsername\": \"tester5\",\n"
                + "            \"reporterId\": 2,\n"
                + "            \"reporterUsername\": \"kimajou\",\n"
                + "            \"type\": \"선정성 또는 폭력성\",\n"
                + "            \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "            \"reply\": null,\n"
                + "            \"resolved\": false\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportMembers(reporter, page, size, result -> {
            try {
                assertEquals(Validation.REPORT_MEMBER_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-member?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportMembers_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-RP001\",\n"
                + "    \"message\": \"신고 내역을 찾을 수 없습니다\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportMembers(reporter, page, size, result -> {
            try {
                assertEquals(Validation.REPORT_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-member?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportMembers_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.loadReportMembers(reporter, page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-member?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportMember_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-RP022\",\n"
                + "    \"message\": \"사용자 신고 조회 성공\",\n"
                + "    \"data\": {\n"
                + "\t\t    \"id\": 1,\n"
                + "        \"memberId\": 7,\n"
                + "        \"memberUsername\": \"tester5\",\n"
                + "        \"reporterId\": 2,\n"
                + "        \"reporterUsername\": \"kimajou\",\n"
                + "        \"type\": \"선정성 또는 폭력성\",\n"
                + "        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "        \"reply\": null,\n"
                + "        \"resolved\" : false\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportMember(reportId, result -> {
            try {
                assertEquals(Validation.REPORT_MEMBER_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportMember_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-RP001\",\n"
                + "    \"message\": \"신고 내역을 찾을 수 없습니다\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportMember(reportId, result -> {
            try {
                assertEquals(Validation.REPORT_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportMember_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.loadReportMember(reportId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testReportTeam_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-RP003\",\n"
                + "    \"message\": \"음악단체 신고 성공\",\n"
                + "    \"data\": {\n"
                + "\t\t    \"id\": 1,\n"
                + "        \"teamId\": 7,\n"
                + "        \"teamName\": \"team name\",\n"
                + "        \"reporterId\": 2,\n"
                + "        \"reporterUsername\": \"kimajou\",\n"
                + "        \"type\": \"선정성 또는 폭력성\",\n"
                + "        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "        \"reply\": \"관리자 답변\",\n"
                + "        \"resolved\" : false \n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        reportRepository.reportTeam(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.REPORT_TEAM_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/team/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testReportTeam_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-RP003\",\n"
                + "    \"message\": \"음악단체 신고 오류\",\n"
                + "    \"errors\": []\n"
                + "}\n";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        reportRepository.reportTeam(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.REPORT_TEAM_FAIL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/team/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testReportTeam_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.reportTeam(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/team/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLoadReportTeams_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-RP023\",\n"
                + "    \"message\": \"음악단체 신고 조회 성공\",\n"
                + "    \"data\": [\n"
                + "\t\t\t\t{\n"
                + "\t\t\t\t    \"id\": 1,\n"
                + "\t\t        \"teamId\": 7,\n"
                + "\t\t        \"teamName\": \"team name\",\n"
                + "\t\t        \"reporterId\": 2,\n"
                + "\t\t        \"reporterUsername\": \"kimajou\",\n"
                + "\t\t        \"type\": \"선정성 또는 폭력성\",\n"
                + "\t\t        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "\t\t        \"reply\": \"관리자 답변\",\n"
                + "\t\t        \"resolved\" : true\n"
                + "\t\t    },\n"
                + "\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"id\": 2,\n"
                + "\t\t        \"teamId\": 3,\n"
                + "\t\t        \"teamName\": \"other team name\",\n"
                + "\t\t        \"reporterId\": 2,\n"
                + "\t\t        \"reporterUsername\": \"kimajou\",\n"
                + "\t\t        \"type\": \"선정성 또는 폭력성\",\n"
                + "\t\t        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "\t\t        \"reply\": \"관리자 답변\",\n"
                + "\t\t        \"resolved\" : true\n"
                + "\t\t    }\n"
                + "    ]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportTeams(reporter, page, size, result -> {
            try {
                assertEquals(Validation.REPORT_TEAM_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-team?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportTeams_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-RP001\",\n"
                + "    \"message\": \"신고 내역을 찾을 수 없습니다\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportTeams(reporter, page, size, result -> {
            try {
                assertEquals(Validation.REPORT_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-team?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportTeams_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.loadReportTeams(reporter, page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-team?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportTeam_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-RP023\",\n"
                + "    \"message\": \"음악단체 신고 조회 성공\",\n"
                + "    \"data\": {\n"
                + "    \t\t\"id\": 1,\n"
                + "        \"teamId\": 7,\n"
                + "        \"teamName\": \"team name\",\n"
                + "        \"reporterId\": 2,\n"
                + "        \"reporterUsername\": \"kimajou\",\n"
                + "        \"type\": \"선정성 또는 폭력성\",\n"
                + "        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "        \"reply\": \"관리자 답변\",\n"
                + "        \"resolved\" : true\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportTeam(reportId, result -> {
            try {
                assertEquals(Validation.REPORT_TEAM_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/team/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportTeam_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-RP001\",\n"
                + "    \"message\": \"신고 내역을 찾을 수 없습니다\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportTeam(reportId, result -> {
            try {
                assertEquals(Validation.REPORT_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/team/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportTeam_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.loadReportTeam(reportId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/team/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testReportArticle_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        String mockResponse = "{\n"
                + "    \"status\": 201,\n"
                + "    \"code\": \"S-RP004\",\n"
                + "    \"message\": \"게시글 신고 성공\",\n"
                + "    \"data\": {\n"
                + "\t\t    \"id\": 1,\n"
                + "        \"articleId\": 7,\n"
                + "        \"articleTitle\": \"Article 제목\",\n"
                + "        \"reporterId\": 2,\n"
                + "        \"reporterUsername\": \"kimajou\",\n"
                + "        \"type\": \"선정성 또는 폭력성\",\n"
                + "        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "        \"reply\": \"관리자 답변\",\n"
                + "        \"resolved\" : false\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(mockResponse));

        // When
        reportRepository.reportArticle(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.REPORT_ARTICLE_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/article/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testReportArticle_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        String mockResponse = "{\n"
                + "    \"status\": 400,\n"
                + "    \"code\": \"F-RP004\",\n"
                + "    \"message\": \"게시글 신고 오류\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(mockResponse));

        // When
        reportRepository.reportArticle(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.REPORT_ARTICLE_FAIL, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/article/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testReportArticle_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reported = 1;
        Integer reporter = 2;
        String type = "기타";
        String details = "";
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.reportArticle(reported, reporter, type, details, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/article/%d", reported), request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testLoadReportArticles_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-RP024\",\n"
                + "    \"message\": \"게시글 신고 조회 성공\",\n"
                + "    \"data\": [\n"
                + "\t\t\t\t{\n"
                + "\t\t\t\t\t\t\"id\": 1,\n"
                + "\t\t        \"articleId\": 7,\n"
                + "\t\t        \"articleTitle\": \"Article 제목\",\n"
                + "\t\t        \"reporterId\": 2,\n"
                + "\t\t        \"reporterUsername\": \"kimajou\",\n"
                + "\t\t        \"type\": \"선정성 또는 폭력성\",\n"
                + "\t\t        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "\t\t        \"reply\": \"관리자 답변\",\n"
                + "\t\t        \"resolved\" : true\n"
                + "\t\t    },\n"
                + "\t\t\t\t{\n"
                + "\t\t\t\t    \"id\": 2,\n"
                + "\t\t        \"articleId\": 2,\n"
                + "\t\t        \"articleTitle\": \"other Article 제목\",\n"
                + "\t\t        \"reporterId\": 2,\n"
                + "\t\t        \"reporterUsername\": \"kimajou\",\n"
                + "\t\t        \"type\": \"선정성 또는 폭력성\",\n"
                + "\t\t        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "\t\t        \"reply\": \"관리자 답변\",\n"
                + "\t\t        \"resolved\" : true \n"
                + "\t\t    }\n"
                + "    ]\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportArticles(reporter, page, size, result -> {
            try {
                assertEquals(Validation.REPORT_ARTICLE_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-article?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportArticles_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-RP001\",\n"
                + "    \"message\": \"신고 내역을 찾을 수 없습니다\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportArticles(reporter, page, size, result -> {
            try {
                assertEquals(Validation.REPORT_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-article?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportArticles_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer page = 0;
        Integer size = 10;
        Integer reporter = 2;
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.loadReportArticles(reporter, page, size, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/member/%d/sent-article?page=%d&size=%d", reporter, page, size), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportArticle_ShouldReturnSuccessResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 200,\n"
                + "    \"code\": \"S-RP024\",\n"
                + "    \"message\": \"게시글 신고 조회 성공\",\n"
                + "    \"data\": {\n"
                + "    \t\t\"id\": 1,\n"
                + "        \"articleId\": 7,\n"
                + "        \"articleTitle\": \"Article 제목\",\n"
                + "        \"reporterId\": 2,\n"
                + "        \"reporterUsername\": \"kimajou\",\n"
                + "        \"type\": \"선정성 또는 폭력성\",\n"
                + "        \"details\": \"이력에 선정적인 내용 작성\",\n"
                + "        \"reply\": \"관리자 답변\",\n"
                + "        \"resolved\" : true\n"
                + "    }\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportArticle(reportId, result -> {
            try {
                assertEquals(Validation.REPORT_ARTICLE_GET_SUCCESS, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/article/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportArticle_ShouldReturnFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-RP001\",\n"
                + "    \"message\": \"신고 내역을 찾을 수 없습니다\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(mockResponse));

        // When
        reportRepository.loadReportArticle(reportId, result -> {
            try {
                assertEquals(Validation.REPORT_NOT_FOUND, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/article/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testLoadReportArticle_ShouldReturnNetworkFailureResponse() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Given
        Integer reportId = 1;
        String mockResponse = "{\n"
                + "    \"status\": 404,\n"
                + "    \"code\": \"F-RP001\",\n"
                + "    \"message\": \"신고 내역을 찾을 수 없습니다\",\n"
                + "    \"errors\": []\n"
                + "}";
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // When
        reportRepository.loadReportArticle(reportId, result -> {
            try {
                assertEquals(Validation.NETWORK_FAILED, result.getValidation());
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Then
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(String.format("/api/report/article/view/%d", reportId), request.getPath());
        assertEquals("GET", request.getMethod());
    }

}
