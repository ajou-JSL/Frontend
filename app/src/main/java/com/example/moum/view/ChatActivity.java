package com.example.moum.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Chat;
import com.example.moum.databinding.ActivityChatBinding;
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
         * TO-DO
         */

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

        /*채팅 보내기 버튼 이벤트*/
        binding.buttonChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.edittextChatSend.getText().toString();
                chatViewModel.send(message);
            }
        });

    }
}
