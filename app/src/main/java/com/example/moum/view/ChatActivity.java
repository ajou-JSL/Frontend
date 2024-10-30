package com.example.moum.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Chat;
import com.example.moum.databinding.ActivityChatBinding;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.ChatViewModel;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatViewModel chatViewModel;
    private Context context;
    private ArrayList<Chat> chats = new ArrayList<>();
    private InputMethodManager inputMethodManager;
    private final String TAG = getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        /*채팅방 정보 불러오기*/
        /**
         * TO-DO: Intent 써서 채팅방 액티비티로부터 불러오는 로직으로 변경할 것
         */
        String sender = "testuser1";
        String receiver = "testuser2";
        Integer chatroomId = 1;
        chatViewModel.loadChatroomInfo(sender, receiver, chatroomId);

        /*채팅 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerChat;
        ChatAdapter chatAdapter = new ChatAdapter();
        chatAdapter.setChats(chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        /*메세지 새로 도착 시, 리사이클러뷰 업데이트*/
        chatViewModel.getReceivedChat().observe(this, receivedChat -> {
            chats.add(receivedChat);
            chatAdapter.notifyItemInserted(chats.size()-1);
            recyclerView.scrollToPosition(chats.size()-1);
        });

        /*초대하기 버튼 이벤트*/
        /**
         * To-DO: 우선순위 낮으므로 이후에 구현 예정
         */

        /*설정 스피너 Adapter 연결*/
        Spinner etcSpinner = binding.spinnerChatEtc;
        String[] etcList = getResources().getStringArray(R.array.chat_etc_list);
        ArrayAdapter<String> etcAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, etcList);
        etcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etcSpinner.setAdapter(etcAdapter);

        etcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    /*채팅방 나가기*/
                    /**
                     * TO-DO
                     */

                }else{
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

        /*기존 채팅 불러오기*/
        chatViewModel.receiveRecentChat();

        /*기존 채팅 도착 감시*/
        chatViewModel.getIsReceiveRecentChatSuccess().observe(this, isReceiveRecentChatSuccess -> {
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

        /*채팅 보내기 버튼 이벤트*/
        binding.buttonChatSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String message = binding.edittextChatSend.getText().toString();
                /**
                 * TO-DO: 테스트용 chatSendTest 대신 chatSend()로 변경 요망
                 */
                chatViewModel.chatSendTest(message);
                binding.edittextChatSend.setText("");
            }
        });

        /*채팅 보내기 결과 감시*/
        chatViewModel.getIsChatSendSuccess().observe(this, isChatSendSuccess -> {
            Validation validation = isChatSendSuccess.getValidation();
            Chat sentChat = isChatSendSuccess.getData();
            if(validation == Validation.CHAT_SEND_SUCCESS){
                chats.add(sentChat);
                chatAdapter.notifyItemInserted(chats.size()-1);
                recyclerView.scrollToPosition(chats.size()-1);
            }
            else if(validation == Validation.CHAT_SEND_FAIL){
                Toast.makeText(context, "채팅 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 전송 결과를 알 수 없습니다.");
            }
        });

        /*위로 스크롤 시 이전 채팅 불러오기*/
        /**
         * TO-DO
         */
        //chatViewModel.receiveOldChat(chats.get(0).getTimestamp());

        /*이전 채팅 결과 감시*/
        chatViewModel.getIsReceiveOldChatSuccess().observe(this, isReceiveOldChatSuccess -> {
            Validation validation = isReceiveOldChatSuccess.getValidation();
            Chat receivedChat = isReceiveOldChatSuccess.getData();
            if(validation == Validation.CHAT_RECEIVE_SUCCESS){
                chats.add(0, receivedChat); // 맨 앞에 add
                chatAdapter.notifyItemInserted(chats.size()-1);
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
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}
