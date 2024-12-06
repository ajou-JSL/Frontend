package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Token;
import com.example.moum.repository.LoginRepository;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.auth.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(AndroidJUnit4.class)
public class LoginViewModelTest {

    @Mock
    LoginRepository loginRepository;

    private LoginViewModel loginViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        Application application = ApplicationProvider.getApplicationContext();
        loginViewModel = new LoginViewModel(application, loginRepository);
    }

    @DisplayName("로그인 정보가 비어있을 시에, 이에 맞는 isLoginSuccess 값을 리턴한다.")
    @Test
    public void testWhenNull_shouldReturnExpectedValue() {

        // Given
        String id = "sosongha3";
        String password = "asdfg12345!";
        loginViewModel.setId(id);
        loginViewModel.setPassword(password);

        // When
        loginViewModel.login();

        // Then
        assertEquals(Validation.EMAIL_NOT_WRITTEN, loginViewModel.getIsLoginSuccess().getValue());
    }

    @DisplayName("로그인 정보가 validation check에서 실패 시에, 이에 맞는 isLoginSuccess 값을 리턴한다.")
    @Test
    public void testWhenNotValid_shouldReturnExpectedValue() {

        // Given
        String id = "sosongha3";
        String password = "asdfg12345!";
        loginViewModel.setId(id);
        loginViewModel.setPassword(password);

        // When
        loginViewModel.login();

        // Then
        assertEquals(Validation.EMAIL_NOT_FORMAL, loginViewModel.getIsLoginSuccess().getValue());
    }

    @DisplayName("올바른 정보를 입력 시에, 토큰과 isLoginSuccess 값을 올바르게 리턴한다.")
    @Test
    public void testWhenCorrect_shouldReturnExpectedValue() {

        // Given
        String id = "sosongha3";
        String password = "asdfg12345!";
        loginViewModel.setId(id);
        loginViewModel.setPassword(password);
        Validation validation = Validation.VALID_ALL;
        Token token = new Token("abcde", "12345", 0, new Member());
        Result<Token> expectedResult = new Result<>(validation, token);

        doAnswer(invocation -> {
            Callback<Result<Token>> callback = invocation.getArgument(2);
            callback.onResult(expectedResult);
            return null;
        }).when(loginRepository).login(eq(id), eq(password), any(Callback.class));

        // When
        loginViewModel.login();

        // Then
        assertEquals(Validation.VALID_ALL, loginViewModel.getIsLoginSuccess().getValue());
        assertEquals(token, loginViewModel.getToken().getValue());
    }
}