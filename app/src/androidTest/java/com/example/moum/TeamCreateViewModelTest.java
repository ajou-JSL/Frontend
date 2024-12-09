package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.data.entity.Token;
import com.example.moum.repository.LoginRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.auth.LoginViewModel;
import com.example.moum.viewmodel.moum.TeamCreateViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

@RunWith(AndroidJUnit4.class)
public class TeamCreateViewModelTest {

    @Mock
    TeamRepository teamRepository;

    private TeamCreateViewModel teamCreateViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Application application;

    @Before
    public void setUp() {
        application = ApplicationProvider.getApplicationContext();
        teamCreateViewModel = new TeamCreateViewModel(application, teamRepository);
    }

    @Test
    public void testValidaCheckWhenNull_shouldReturnExpectedValue() {
        // Given
        Team team = new Team();
        team.setTeamName("");
        teamCreateViewModel.setTeam(team);

        // When
        teamCreateViewModel.validCheck();

        // Then
        assertEquals(Validation.TEAM_NAME_NOT_WRITTEN, teamCreateViewModel.getIsValidCheckSuccess().getValue());
    }

    @Test
    public void testValidaCheckWhenCorrect_shouldReturnExpectedValue() {
        // Given
        Team team = new Team();
        team.setTeamName("팀이름");
        teamCreateViewModel.setGenre("CLASSICAL");
        teamCreateViewModel.setTeam(team);

        // When
        teamCreateViewModel.validCheck();

        // Then
        assertEquals(Validation.VALID_ALL, teamCreateViewModel.getIsValidCheckSuccess().getValue());
    }

    @Test
    public void testCreateTeamWhenNotValid_shouldReturnExpectedValue() {
        // Given
        Team team = new Team();
        team.setTeamName("팀이름");
        Integer leaderId = 1;
        teamCreateViewModel.setTeam(team);
        teamCreateViewModel.setGenre("CLASSICAL");
        teamCreateViewModel.setIsValidCheckSuccess(Validation.VIDEO_URL_NOT_FORMAL);

        // When
        teamCreateViewModel.createTeam(leaderId, application);

        // Then
        assertNotEquals(null, teamCreateViewModel.getIsCreateTeamSuccess().getValue());
        assertEquals(Validation.NOT_VALID_ANYWAY, teamCreateViewModel.getIsCreateTeamSuccess().getValue().getValidation());
    }

    @Test
    public void testCreateTeamWhenProcessingFail_shouldReturnExpectedValue() {
        // Given
        Team team = new Team();
        team.setTeamName("팀이름");
        Integer leaderId = 1;
        teamCreateViewModel.setTeam(team);
        teamCreateViewModel.setGenre("CLASSICAL");
        teamCreateViewModel.setIsValidCheckSuccess(Validation.VALID_ALL);
        teamCreateViewModel.addRecord("이력", LocalDate.now().plusDays(2), LocalDate.now());

        // When
        teamCreateViewModel.createTeam(leaderId, application);

        // Then
        assertNotEquals(null, teamCreateViewModel.getIsCreateTeamSuccess().getValue());
        assertEquals(Validation.RECORD_NOT_VALID, teamCreateViewModel.getIsCreateTeamSuccess().getValue().getValidation());
    }

    @Test
    public void testCreateTeamWhenCorrect_shouldReturnExpectedValue() {
        // Given
        Team team = new Team();
        team.setTeamName("팀이름");
        Integer leaderId = 1;
        team.setLeaderId(leaderId);
        File profileFile = null;
        teamCreateViewModel.setTeam(team);
        teamCreateViewModel.setGenre("CLASSICAL");
        teamCreateViewModel.setIsValidCheckSuccess(Validation.VALID_ALL);
        teamCreateViewModel.addRecord("이력", LocalDate.now(), LocalDate.now().plusDays(2));
        Validation validation = Validation.CREATE_TEAM_SUCCESS;
        Result<Team> expectedResult = new Result<>(validation, team);

        doAnswer(invocation -> {
            Callback<Result<Team>> callback = invocation.getArgument(2);
            callback.onResult(expectedResult);
            return null;
        }).when(teamRepository).createTeam(eq(team), eq(profileFile), any(Callback.class));

        // When
        teamCreateViewModel.createTeam(leaderId, application);

        // Then
        assertNotEquals(null, teamCreateViewModel.getIsCreateTeamSuccess().getValue());
        assertEquals(Validation.CREATE_TEAM_SUCCESS, teamCreateViewModel.getIsCreateTeamSuccess().getValue().getValidation());
        assertEquals(1, teamCreateViewModel.getIsCreateTeamSuccess().getValue().getData().getLeaderId().intValue());
    }
}