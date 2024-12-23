package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.repository.SignupRepository;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.auth.SignupViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class SignupViewModelTest {

    @Mock
    SignupRepository signupRepository;

    private SignupViewModel signupViewModel;
    private Context context;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        Application application = ApplicationProvider.getApplicationContext();
        signupViewModel = new SignupViewModel(application, signupRepository);
        context = ApplicationProvider.getApplicationContext();
    }

    @DisplayName("이메일 인증 시 정보 비어있다면, 이에 맞는 isEmailAuthSuccess 값을 리턴한다.")
    @Test
    public void testEmailAuth_WhenNull_shouldReturnExpectedValue() throws NoSuchFieldException {

        // Given
        String expectedEmail = "";
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupUser.setEmail(expectedEmail);

        // When
        signupViewModel.emailAuth();

        // Then
        assertEquals(Validation.EMAIL_NOT_WRITTEN, signupViewModel.getIsEmailAuthSuccess().getValue());
    }

    @DisplayName("이메일 인증 시 valid하지 않다면, 이에 맞는 isEmailAuthSuccess 값을 리턴한다.")
    @Test
    public void testEmailAuth_WhenNotValid_shouldReturnExpectedValue() {

        // Given
        String expectedEmail = "strange-email";
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupUser.setEmail(expectedEmail);

        // When
        signupViewModel.emailAuth();

        // Then
        assertEquals(Validation.EMAIL_NOT_FORMAL, signupViewModel.getIsEmailAuthSuccess().getValue());
    }

    @DisplayName("이메일 인증이 성공하여, 정상적인 isEmailAuthSuccess 값을 리턴한다.")
    @Test
    public void testEmailAuth_WhenSuccess_shouldReturnExpectedValue() {

        // Given
        String expectedEmail = "sosongha3@ajou.ac.kr";
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupUser.setEmail(expectedEmail);

        Validation validation = Validation.VALID_ALL;
        Result<Object> expectedResult = new Result<>(validation);

        doAnswer(invocation -> {
            Callback<Result<Object>> callback = invocation.getArgument(1);
            callback.onResult(expectedResult);
            return null;
        }).when(signupRepository).emailAuth(eq(expectedEmail), any(Callback.class));

        // When
        signupViewModel.emailAuth();

        // Then
        assertEquals(Validation.VALID_ALL, signupViewModel.getIsEmailAuthSuccess().getValue());
    }

    @DisplayName("Basic 정보에서 패스워드와 확인이 일치하지 않을 때, 이에 맞는 isBasicValid 값을 리턴한다.")
    @Test
    public void testValidCheckBasic_WhenPasswordNotEqual_shouldReturnExpectedValue() {

        // Given
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupUser.setUsername("sosongha3");
        signupUser.setEmail("sosongha3@ajou.ac.kr");
        signupUser.setName("소성하");
        signupUser.setPassword("asdf1234!!");
        signupUser.setPasswordCheck("zxcv1234!!");
        signupUser.setEmailCode("123456");
        signupViewModel.setIsEmailCodeSuccess(Validation.EMAIL_AUTH_SUCCESS);
        signupViewModel.setIsPersonalAgree(Validation.VALID_ALL);

        // When
        signupViewModel.validCheckBasic();

        // Then
        assertEquals(Validation.PASSWORD_NOT_EQUAL, signupViewModel.getIsBasicValid().getValue());
    }

    @DisplayName("Basic 정보에서 개인정보 동의를 하지 않았을 때, 이에 맞는 isBasicValid 값을 리턴한다.")
    @Test
    public void testValidCheckBasic_WhenPersonalNotAgree_shouldReturnExpectedValue() {

        // Given
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupUser.setUsername("sosongha3");
        signupUser.setEmail("sosongha3@ajou.ac.kr");
        signupUser.setName("소성하");
        signupUser.setPassword("asdf1234!!");
        signupUser.setPasswordCheck("asdf1234!!");
        signupUser.setEmailCode("123456");
        signupViewModel.setIsEmailCodeSuccess(Validation.EMAIL_AUTH_SUCCESS);
        signupViewModel.setIsPersonalAgree(Validation.PERSONAL_NOT_AGREE);

        // When
        signupViewModel.validCheckBasic();

        // Then
        assertEquals(Validation.PERSONAL_NOT_AGREE, signupViewModel.getIsBasicValid().getValue());
    }

    @DisplayName("Basic 정보를 모두 올바르게 기입 시, 이에 맞는 isBasicValid 값을 리턴한다.")
    @Test
    public void testValidCheckBasic_WhenSuccess_shouldReturnExpectedValue() {

        // Given
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupUser.setUsername("sosongha3");
        signupUser.setEmail("sosongha3@ajou.ac.kr");
        signupUser.setName("소성하");
        signupUser.setPassword("asdf1234!!");
        signupUser.setPasswordCheck("asdf1234!!");
        signupUser.setEmailCode("123456");
        signupViewModel.setIsEmailCodeSuccess(Validation.EMAIL_AUTH_SUCCESS);
        signupViewModel.setIsPersonalAgree(Validation.VALID_ALL);

        // When
        signupViewModel.validCheckBasic();

        // Then
        assertEquals(Validation.VALID_ALL, signupViewModel.getIsBasicValid().getValue());
    }

    @DisplayName("이메일 인증코드 확인이 성공일 시, 이에 맞는 isEmailAuthSuccess 값을 리턴한다.")
    @Test
    public void testValidCheckEmailCode_WhenSuccess_shouldReturnExpectedValue() {

        // Given
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        String email = "sosongha3@ajou.ac.kr";
        String emailCode = "123456";
        signupUser.setEmail(email);
        signupUser.setEmailCode(emailCode);
        signupViewModel.setIsEmailAuthSuccess(Validation.VALID_ALL);

        Validation validation = Validation.VALID_ALL;
        Result<Object> expectedResult = new Result<>(validation);

        doAnswer(invocation -> {
            Callback<Result<Object>> callback = invocation.getArgument(2);
            callback.onResult(expectedResult);
            return null;
        }).when(signupRepository).checkEmailCode(eq(email), eq(emailCode), any(Callback.class));

        // When
        signupViewModel.validCheckEmailCode();

        // Then
        assertEquals(Validation.VALID_ALL, signupViewModel.getIsEmailCodeSuccess().getValue());
    }

    @DisplayName("Profile 정보를 모두 올바르게 기입 시, 이에 맞는 isProfileValid 값을 리턴한다.")
    @Test
    public void testvalidCheckProfile_WhenSuccess_shouldReturnExpectedValue() {

        // Given
        SignupUser expectedSignupUser = new SignupUser();
        expectedSignupUser.setName("소지섭사촌");
        expectedSignupUser.setInstrument("트럼펫");
        expectedSignupUser.setProfileDescription("ㅎㅇ");
        signupViewModel.setSignupUser(expectedSignupUser);
        signupViewModel.setProficiency("하");

        // When
        signupViewModel.validCheckProfile();

        // Then
        assertEquals(Validation.VALID_ALL, signupViewModel.getIsProfileValid().getValue());
    }

    @DisplayName("회원가입 시도 시 추가정보가 모두 비었을 때, 이에 맞는 isSignupSuccess 값을 리턴한다.")
    @Test
    public void testSignup_WhenAllEmpty_shouldReturnExpectedValue() {

        // Given
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupViewModel.setIsProfileValid(Validation.VALID_ALL);
        Result<Object> expectedResult = new Result<>(Validation.VALID_ALL);
        doAnswer(invocation -> {
            Callback<Result<Object>> callback = invocation.getArgument(1);
            callback.onResult(expectedResult);
            return null;
        }).when(signupRepository).signup(eq(signupUser), any(Callback.class));

        // When
        signupViewModel.signup(context);

        // Then
        assertEquals(Validation.VALID_ALL, signupViewModel.getIsSignupSuccess().getValue());
    }

    @DisplayName("회원가입 시도 시 추가정보가 모두 기입되었을 때, 이에 맞는 isSignupSuccess 값을 리턴한다.")
    @Test
    public void testSignup_WhenAllInserted_shouldReturnExpectedValue() {

        // Given
        SignupUser signupUser = signupViewModel.getSignupUser().getValue();
        signupViewModel.setIsProfileValid(Validation.VALID_ALL);
        signupViewModel.addRecord("1회 연주회", LocalDate.now(), LocalDate.now());
        signupViewModel.addRecord("2회 연주회", LocalDate.now(), LocalDate.now());
        signupViewModel.setAddress("수원시 권선구 25-125");
        Uri uri = Uri.parse("content://com.example.app.provider/item/12345");
        signupViewModel.setProfileImage(uri);
        signupViewModel.setProficiency("중");

        Result<Object> expectedResult = new Result<>(Validation.VALID_ALL);
        doAnswer(invocation -> {
            Callback<Result<Object>> callback = invocation.getArgument(1);
            callback.onResult(expectedResult);
            return null;
        }).when(signupRepository).signup(eq(signupUser), any(Callback.class));

        // When
        signupViewModel.signup(context);

        // Then
        assertEquals(Validation.VALID_ALL, signupViewModel.getIsSignupSuccess().getValue());
    }


}
