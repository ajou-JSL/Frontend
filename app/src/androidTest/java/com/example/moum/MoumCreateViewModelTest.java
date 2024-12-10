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

import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.MoumRepository;
import com.example.moum.repository.TeamRepository;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.moum.MoumCreateViewModel;
import com.example.moum.viewmodel.moum.TeamCreateViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class MoumCreateViewModelTest {

    @Mock
    MoumRepository moumRepository;

    private MoumCreateViewModel moumCreateViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Application application;

    @Before
    public void setUp() {
        application = ApplicationProvider.getApplicationContext();
        moumCreateViewModel = new MoumCreateViewModel(application, moumRepository);
    }

    @Test
    public void testValidaCheckWhenNull_shouldReturnExpectedValue() {
        // Given
        Moum moum = new Moum();
        moum.setMoumName("");
        moumCreateViewModel.setMoum(moum);

        // When
        moumCreateViewModel.validCheck();

        // Then
        assertEquals(Validation.MOUM_NAME_NOT_WRITTEN, moumCreateViewModel.getIsValidCheckSuccess().getValue());
    }

    @Test
    public void testValidaCheckWhenCorrect_shouldReturnExpectedValue() {
        // Given
        Moum moum = new Moum();
        moum.setMoumName("모음 이름");
        moumCreateViewModel.setMoum(moum);
        moumCreateViewModel.setGenre("POP");

        // When
        moumCreateViewModel.validCheck();

        // Then
        assertEquals(Validation.VALID_ALL, moumCreateViewModel.getIsValidCheckSuccess().getValue());
    }

    @Test
    public void testCreateTeamWhenNotValid_shouldReturnExpectedValue() {
        // Given
        Moum moum = new Moum();
        moum.setMoumName("");
        Integer leaderId = 1;
        Integer teamId = 2;
        moumCreateViewModel.setMoum(moum);
        moumCreateViewModel.setGenre("POP");
        moumCreateViewModel.setIsValidCheckSuccess(Validation.ARTIST_NAME_NOT_WRITTEN);

        // When
        moumCreateViewModel.createMoum(leaderId, teamId, application);

        // Then
        assertEquals(Validation.NOT_VALID_ANYWAY, moumCreateViewModel.getIsCreateMoumSuccess().getValue().getValidation());
    }

    @Test
    public void testCreateTeamWhenCorrect_shouldReturnExpectedValue() {
        // Given
        Moum moum = new Moum();
        moum.setMoumName("");
        Integer leaderId = 1;
        Integer teamId = 2;
        ArrayList<File> profileFiles = null;
        moumCreateViewModel.setMoum(moum);
        moumCreateViewModel.setGenre("POP");
        moumCreateViewModel.setIsValidCheckSuccess(Validation.VALID_ALL);
        Validation validation = Validation.CREATE_MOUM_SUCCESS;
        Result<Moum> expectedResult = new Result<>(validation, moum);

        doAnswer(invocation -> {
            Callback<Result<Moum>> callback = invocation.getArgument(2);
            callback.onResult(expectedResult);
            return null;
        }).when(moumRepository).createMoum(eq(moum), eq(profileFiles), any(Callback.class));

        // When
        moumCreateViewModel.createMoum(leaderId, teamId, application);

        // Then
        assertNotEquals(null, moumCreateViewModel.getIsCreateMoumSuccess().getValue());
        assertEquals(Validation.CREATE_MOUM_SUCCESS, moumCreateViewModel.getIsCreateMoumSuccess().getValue().getValidation());
        assertEquals(2, moumCreateViewModel.getIsCreateMoumSuccess().getValue().getData().getTeamId().intValue());
    }
}