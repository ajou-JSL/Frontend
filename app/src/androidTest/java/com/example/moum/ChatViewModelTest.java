package com.example.moum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Token;
import com.example.moum.repository.ChatRepository;
import com.example.moum.repository.LoginRepository;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.auth.LoginViewModel;
import com.example.moum.viewmodel.chat.ChatViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.observers.TestObserver;

@RunWith(AndroidJUnit4.class)
public class ChatViewModelTest {
    @Mock
    ChatRepository chatRepository;

    private ChatViewModel chatViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        Application application = ApplicationProvider.getApplicationContext();
        chatViewModel = new ChatViewModel(application, chatRepository);
    }

    @DisplayName("채팅을 보내는 것이 성공하면, 이에 맞는 isChatSendSuccess 값을 리턴한다.")
    @Test
    public void testChatSendWhenSuccess_shouldReturnExpectedValue() {
        // Given
        String memberId = "testuser";
        Integer groupId = 1;
        String receiverId = "testuser2";
        Integer chatroomId = 1;
        String chatroomName = "testuser2";
        Chatroom.ChatroomType chatroomType = Chatroom.ChatroomType.PERSONAL_CHAT;
        chatViewModel.setChatroomInfo(memberId, groupId, receiverId, chatroomId, chatroomName, chatroomType, memberId);
        String message = "안녕하세요.";

        Validation validation = Validation.CHAT_POST_SUCCESS;
        Chat chat = new Chat(memberId, receiverId, message, chatroomId, LocalDateTime.now());
        Result<Chat> expectedResult = new Result<>(validation, chat);

        doAnswer(invocation -> {
            Callback<Result<Chat>> callback = invocation.getArgument(1);
            callback.onResult(expectedResult);
            return null;
        }).when(chatRepository).chatSend(any(Chat.class), any(Callback.class));

        // When
        chatViewModel.chatSend(message);

        // Then
        assertEquals(memberId, chatViewModel.getSender());
        assertEquals(chatroomId, chatViewModel.getChatroom().getChatroomId());
        assertNotEquals(null, chatViewModel.getIsChatSendSuccess().getValue());
        assertEquals(Validation.CHAT_POST_SUCCESS, chatViewModel.getIsChatSendSuccess().getValue().getValidation());
    }

    @DisplayName("최신 채팅을 받는 것이 성공하면, 이에 맞는 isReceiveRecentChatSuccess 값을 리턴한다.")
    @Test
    public void testReceiveRecentChatWhenSuccess_shouldReturnExpectedValue() {
        // Given
        String memberId = "testuser";
        Integer groupId = 1;
        String receiverId = "testuser2";
        Integer chatroomId = 1;
        String chatroomName = "testuser2";
        Chatroom.ChatroomType chatroomType = Chatroom.ChatroomType.PERSONAL_CHAT;
        chatViewModel.setChatroomInfo(memberId, groupId, receiverId, chatroomId, chatroomName, chatroomType, memberId);

        String message = "안녕하세요.";
        Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
        Chat chat = new Chat(memberId, receiverId, message, chatroomId, LocalDateTime.now());
        Result<Chat> expectedResult = new Result<>(validation, chat);

        doAnswer(invocation -> {
            Callback<Result<Chat>> callback = invocation.getArgument(1);
            callback.onResult(expectedResult);
            return null;
        }).when(chatRepository).receiveRecentChat(any(Integer.class), any(Callback.class));

        // When
        TestObserver<Result<Chat>> testObserver = new TestObserver<>();
        chatViewModel.getIsReceiveRecentChatSuccess().subscribe(testObserver);
        chatViewModel.receiveRecentChat();
        testObserver.awaitCount(1);

        // Then
        assertEquals(memberId, chatViewModel.getSender());
        assertEquals(chatroomId, chatViewModel.getChatroom().getChatroomId());
        testObserver.assertValue(Objects::nonNull);
        testObserver.assertValue(value -> value.getValidation() == Validation.CHAT_RECEIVE_SUCCESS);
        testObserver.dispose();
    }

    @DisplayName("이전 채팅을 받는 것이 성공하면, 이에 맞는 isReceiveRecentChatSuccess 값을 리턴한다.")
    @Test
    public void testReceiveOldChatWhenSuccess_shouldReturnExpectedValue() {
        // Given
        String memberId = "testuser";
        Integer groupId = 1;
        String receiverId = "testuser2";
        Integer chatroomId = 1;
        String chatroomName = "testuser2";
        Chatroom.ChatroomType chatroomType = Chatroom.ChatroomType.PERSONAL_CHAT;
        LocalDateTime beforeTimestamp = LocalDateTime.now();
        chatViewModel.setChatroomInfo(memberId, groupId, receiverId, chatroomId, chatroomName, chatroomType, memberId);

        String message = "안녕하세요.";
        Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
        Chat chat = new Chat(memberId, receiverId, message, chatroomId, LocalDateTime.now());
        Result<Chat> expectedResult = new Result<>(validation, chat);

        doAnswer(invocation -> {
            Callback<Result<Chat>> callback = invocation.getArgument(2);
            callback.onResult(expectedResult);
            return null;
        }).when(chatRepository).receiveOldChat(any(Integer.class), any(LocalDateTime.class), any(Callback.class));

        // When
        TestObserver<Result<Chat>> testObserver = new TestObserver<>();
        chatViewModel.getIsReceiveOldChatSuccess().subscribe(testObserver);
        chatViewModel.receiveOldChat(beforeTimestamp);
        testObserver.awaitCount(1);

        // Then
        assertEquals(memberId, chatViewModel.getSender());
        assertEquals(chatroomId, chatViewModel.getChatroom().getChatroomId());
        testObserver.assertValue(value -> value != null);
        testObserver.assertValue(value -> value.getValidation() == Validation.CHAT_RECEIVE_SUCCESS);
        testObserver.dispose();
    }
}
