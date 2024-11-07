package com.example.moum.view.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.databinding.ActivityChatBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.adapter.ChatAdapter;
import com.example.moum.viewmodel.chat.ChatViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatViewModel chatViewModel;
    private Context context;
    private ArrayList<Chat> chats = new ArrayList<>();
    private InputMethodManager inputMethodManager;
    private final String TAG = getClass().toString();
    SharedPreferenceManager sharedPreferenceManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static int SIZE_OF_STREAM = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*채팅방 정보 불러오기*/
        Intent prevIntent = getIntent();
        int groupId = prevIntent.getIntExtra("groupId", 0);
        String receiverId = prevIntent.getStringExtra("receiverId");
        int chatroomId = prevIntent.getIntExtra("chatroomId", 0);
        String chatroomName = prevIntent.getStringExtra("chatroomName");
        String chatroomLastTimestamp = prevIntent.getStringExtra("chatroomLastTime");
        String chatroomProfileStr = prevIntent.getStringExtra("chatroomProfile");
        int chatroomTypeInt = prevIntent.getIntExtra("chatroomType", 0);
        String chatroomLeader = prevIntent.getStringExtra("chatroomLeader");

        Chatroom.ChatroomType chatroomType = Chatroom.ChatroomType.values()[chatroomTypeInt];
        binding.textviewChatUserName.setText(chatroomName);
        binding.textviewChatMessageTime.setText(chatroomLastTimestamp);
        chatViewModel.setChatroomInfo(username, groupId, receiverId, chatroomId, chatroomName, chatroomType, chatroomLeader);
        Uri chatroomProfile;
        if(chatroomProfileStr != null) {
            chatroomProfile = Uri.parse(chatroomProfileStr);
            Glide.with(this).load(chatroomProfile).into(binding.imageChatProfile);
        }
        if(chatroomLeader != null && chatroomLeader.equals(username) && chatroomType == Chatroom.ChatroomType.MULTI_CHAT)
            binding.buttonChatInvite.setVisibility(View.VISIBLE);
        else
            binding.buttonChatInvite.setVisibility(View.INVISIBLE);

        /*채팅 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerChat;
        ChatAdapter chatAdapter = new ChatAdapter();
        chatAdapter.setChats(chats, chatroomType);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        /*초대하기 버튼 이벤트*/
        binding.buttonChatInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*다음 Acitivity로 이동*/
                Intent nextIntent = new Intent(ChatActivity.this, ChatInviteActivity.class);
                nextIntent.putExtra("receiverId", receiverId);
                startActivity(nextIntent);
            }
        });

        /*설정 스피너 Adapter 연결*/
        Spinner etcSpinner = binding.spinnerChatEtc;
        String[] etcList = getResources().getStringArray(R.array.chat_etc_list);
        if(chatroomType != Chatroom.ChatroomType.MULTI_CHAT || chatroomLeader == null || !chatroomLeader.equals(username))
            etcList = Arrays.copyOfRange(etcList, 0, 1);
        ArrayAdapter<String> etcAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, etcList);
        etcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etcSpinner.setAdapter(etcAdapter);

        etcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    /*모음톡 나가기*/
                    //TODO - 다이얼로그 띄울 것

                }
                else if(position == 1){
                    /*수정하기*/
                    Intent nextIntent = new Intent(ChatActivity.this, ChatUpdateChatroomActivity.class);
                    //TODO - 수정을 위해 필요한 정보 putExtra할 것
                    startActivity(nextIntent);

                }
                else if(position == 2){
                    /*정산 요청하기*/
                    //TODO - 정산 요청 기능 추가해야함
                }
                else{
                    Log.e(TAG, "알 수 없는 아이템 선택");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*입력창 클릭 시 키보드 show 이벤트*/
        binding.edittextChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = binding.edittextChatSend;
                inputMethodManager.showSoftInput(editText, 0);
            }
        });

        /*여백 클릭 시 키보드 hide 이벤트*/
        binding.constraintChatTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout topView = binding.constraintChatTop;
                inputMethodManager.hideSoftInputFromWindow(topView.getWindowToken(), 0);
                binding.edittextChatSend.clearFocus();
            }
        });

        /*최신 채팅 불러오기*/
        chatViewModel.receiveRecentChat();

        /*최신 채팅 도착 감시*/
        Disposable recentChatDisposable = chatViewModel.getIsReceiveRecentChatSuccess().subscribe(isReceiveRecentChatSuccess -> {
            Validation validation = isReceiveRecentChatSuccess.getValidation();
            Chat receivedChat = isReceiveRecentChatSuccess.getData();
            if(validation == Validation.CHAT_RECEIVE_SUCCESS){
                chats.add(receivedChat);
                chatAdapter.notifyItemInserted(chats.size()-1);
                recyclerView.scrollToPosition(chats.size()-1);
            }
            else if(validation == Validation.CHAT_RECEIVE_FAIL){
                Toast.makeText(context, "채팅 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });
        compositeDisposable.add(recentChatDisposable);

        /*채팅 보내기 버튼 이벤트*/
        binding.buttonChatSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String message = binding.edittextChatSend.getText().toString();
                chatViewModel.chatSend(message);
                binding.edittextChatSend.setText("");
            }
        });

        /*채팅 보내기 결과 감시*/
        chatViewModel.getIsChatSendSuccess().observe(this, isChatSendSuccess -> {
            Validation validation = isChatSendSuccess.getValidation();
            Chat sentChat = isChatSendSuccess.getData();
            if(validation == Validation.CHAT_POST_SUCCESS){
                chats.add(sentChat);
                chatAdapter.notifyItemInserted(chats.size()-1);
                recyclerView.scrollToPosition(chats.size()-1);
            }
            else if(validation == Validation.CHAT_POST_FAIL){
                Toast.makeText(context, "채팅 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 전송 결과를 알 수 없습니다.");
            }
        });

        /*최상단에서 스크롤 시 이전 채팅 불러오기*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE && !chats.isEmpty()){
                    chatViewModel.receiveOldChat(chats.get(0).getTimestamp());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        /*이전 채팅 결과 감시*/
        Disposable oldChatDisposable = chatViewModel.getIsReceiveOldChatSuccess().subscribe(isReceiveOldChatSuccess -> {
            Validation validation = isReceiveOldChatSuccess.getValidation();
            Chat receivedChat = isReceiveOldChatSuccess.getData();
            if(validation == Validation.CHAT_RECEIVE_SUCCESS){
                chats.add(0, receivedChat); // 맨 앞에 add
                chatAdapter.notifyItemInserted(chats.size()-1);
                recyclerView.scrollToPosition(SIZE_OF_STREAM - 1);
            }
            else if(validation == Validation.CHAT_RECEIVE_FAIL){
                Toast.makeText(context, "채팅 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });
        compositeDisposable.add(oldChatDisposable);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
