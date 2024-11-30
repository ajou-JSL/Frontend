package com.example.moum.view.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.databinding.ActivityChatBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.TimeManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.adapter.ChatAdapter;
import com.example.moum.view.dialog.ChatroomLeaveDialog;
import com.example.moum.view.moum.MoumManageActivity;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.viewmodel.chat.ChatViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ChatViewModel chatViewModel;
    private Context context;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();
    private InputMethodManager inputMethodManager;
    private final String TAG = getClass().toString();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static int SIZE_OF_STREAM = 5;
    private Boolean isLoading = false;
    private final int REQUEST_CODE = 100;
    private Integer chatroomId;
    private Integer leaderId;
    private Integer id;
    Chatroom.ChatroomType chatroomType;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*채팅방 정보 불러오기*/
        Intent prevIntent = getIntent();
        chatroomId = prevIntent.getIntExtra("chatroomId", -1);
        String chatroomName = prevIntent.getStringExtra("chatroomName");
        int chatroomTypeInt = prevIntent.getIntExtra("chatroomType", -1);
        int teamId = prevIntent.getIntExtra("teamId", -1);
        leaderId = prevIntent.getIntExtra("leaderId", -1);
        String lastChat = prevIntent.getStringExtra("lastChat");
        String lastTimestamp = prevIntent.getStringExtra("lastTimestamp");
        String fileUrl = prevIntent.getStringExtra("fileUrl");
        chatroomType = Chatroom.ChatroomType.values()[chatroomTypeInt];
        Chatroom chatroom = new Chatroom(chatroomId, chatroomName, chatroomType, teamId, leaderId, lastChat, lastTimestamp, fileUrl);

        binding.textviewChatUserName.setText(chatroomName);
        if(lastTimestamp != null && !lastTimestamp.isEmpty()){
            String prettyTime = TimeManager.strToPrettyTime(lastTimestamp);
            binding.textviewChatMessageTime.setText(prettyTime);
        }
        chatViewModel.setChatroomInfo(username, id, chatroom);
        Uri chatroomProfile;
        if(fileUrl != null) {
            chatroomProfile = Uri.parse(fileUrl);
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.background_circle_gray_size_fit)
                    .error(R.drawable.background_circle_gray_size_fit))
                    .load(chatroomProfile).into(binding.imageChatProfile);
        }
        if(chatroomType == Chatroom.ChatroomType.PERSONAL_CHAT)
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
                if(members == null || members.size() != 2) {
                    Toast.makeText(ChatActivity.this, "초대할 멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Member targetMember = null;
                for(Member member : members){
                    if(!member.getId().equals(id))
                        targetMember = member;
                }
                if(targetMember == null){
                    Toast.makeText(ChatActivity.this, "초대할 멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent nextIntent = new Intent(ChatActivity.this, InviteActivity.class);
                nextIntent.putExtra("targetMemberId", targetMember.getId());
                nextIntent.putExtra("targetMemberName", targetMember.getName());
                startActivity(nextIntent);
            }
        });

        /*설정 드롭다운 연결*/
        String[] etcList = getResources().getStringArray(R.array.chat_etc_list);
        binding.dropdownChatEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ChatActivity.this, binding.dropdownChatEtc);
                if(chatroomType == Chatroom.ChatroomType.PERSONAL_CHAT)
                    for (int i = 0; i < etcList.length-1; i++) {
                        popupMenu.getMenu().add(etcList[i]);
                    }
                else if(chatroomType == Chatroom.ChatroomType.MULTI_CHAT && Objects.equals(leaderId, id))
                    for (int i = 0; i < etcList.length; i++) {
                        popupMenu.getMenu().add(etcList[i]);
                    }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("모음톡 나가기")) {
                            ChatroomLeaveDialog chatroomLeaveDialog = new ChatroomLeaveDialog(context, chatroomName);
                            chatroomLeaveDialog.show();
                        }
                        else if (selectedItem.equals("수정하기")) {
                            Intent intent = new Intent(context, ChatUpdateChatroomActivity.class);
                            intent.putExtra("chatroomId", chatroomId);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        return true;
                    }
                });
                popupMenu.show();
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
                String prettyTime = TimeManager.strToPrettyTime(receivedChat.getTimestamp().toString());
                binding.textviewChatMessageTime.setText(prettyTime);

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
                // 이미 스트리밍 채팅을 받아오므로, 재표시할 필요 없음, 따라서 주석 처리
//                chats.add(sentChat);
//                chatAdapter.notifyItemInserted(chats.size()-1);
//                recyclerView.scrollToPosition(chats.size()-1);
            }
            else if(validation == Validation.CHAT_POST_FAIL){
                Toast.makeText(context, "채팅 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                //TODO 현재 잘 보내졌는데도 불구하고, 이미 연결된 스트리밍 연결 때문에 chatSend()가 네트워크 에러 처리되므로 주석 처리함
                //Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 전송 결과를 알 수 없습니다.");
            }
        });

        /*최상단에서 스크롤 시 이전 채팅 불러오기*/
        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper()); // 여러번 호출되는 것을 막기 위한 디바운싱
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE && !chats.isEmpty() &&  !isLoading){
                    isLoading = true;
                    Log.e(TAG, chats.get(0).getTimestamp() + chats.get(0).getMessage());
                    chatViewModel.receiveOldChat(chats.get(0).getTimestamp());
                    handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
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
                chatAdapter.notifyItemInserted(0);
            }
            else if(validation == Validation.CHAT_RECEIVE_FAIL){
                //TODO 채팅을 다 불러올 때 불리는 상태이므로 주석처리
                //Toast.makeText(context, "채팅 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });
        compositeDisposable.add(oldChatDisposable);

        /*채팅방의 멤버 리스트 가져오기*/
        chatViewModel.loadMembersOfChatroom();

        /*채팅방의 멤버 리스트 가져오기 결과 감시*/
        chatViewModel.getIsLoadMembersOfChatroomSuccess().observe(this, isLoadMembersOfChatroomSuccess -> {
            Validation validation = isLoadMembersOfChatroomSuccess.getValidation();
            List<Member> loadedMembers = isLoadMembersOfChatroomSuccess.getData();
            if(validation == Validation.CHATROOM_MEMBER_LOAD_SUCCESS){
                members.clear();
                members.addAll(loadedMembers);

                if(chatroomType == Chatroom.ChatroomType.PERSONAL_CHAT) {
                    Member targetMember = null;
                    for(Member member : members){
                        if(!member.getId().equals(id))
                            targetMember = member;
                    }
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray_size_fit)
                            .error(R.drawable.background_circle_gray_size_fit))
                            .load(targetMember.getProfileImageUrl()).into(binding.imageChatProfile);
                }
                else if(chatroomType == Chatroom.ChatroomType.MULTI_CHAT && chatroom.getFileUrl() != null && !chatroom.getFileUrl().isEmpty()){
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray_size_fit)
                            .error(R.drawable.background_circle_gray_size_fit))
                            .load(chatroom.getFileUrl()).into(binding.imageChatProfile);
                }
            }
            else if(validation == Validation.CHAT_RECEIVE_FAIL){
                Toast.makeText(context, "채팅방의 멤버 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.MEMBER_NOT_EXIST){
                Toast.makeText(context, "채팅방 내에 멤버가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });

        /*멀티채팅이라면 프로필 클릭 시 멤버 드롭다운 띄움*/
        final MemberProfileFragment memberProfileFragment = new MemberProfileFragment(context);
        if(chatroomType == Chatroom.ChatroomType.MULTI_CHAT) {
           binding.imageChatProfile.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   openLeftMenuWithMemberList();
               }
           });
        }

        /*개인채팅이라면 프로필 클릭 시 프로필 fragment 띄움*/
        else if(chatroomType == Chatroom.ChatroomType.PERSONAL_CHAT){
            binding.imageChatProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Member targetMember = null;
                    for(Member member : members){
                        if(!member.getId().equals(id))
                            targetMember = member;
                    }
                    if(targetMember == null){
                        Toast.makeText(ChatActivity.this, "멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("targetMemberId", targetMember.getId());
                    memberProfileFragment.setArguments(bundle);
                    memberProfileFragment.show(getSupportFragmentManager(), memberProfileFragment.getTag());
                }
            });

        }

        /*채팅방 나가기 결과 감시*/
        chatViewModel.getIsDeleteMembersSuccess().observe(this, isDeleteMembersSuccess -> {
            Validation validation = isDeleteMembersSuccess.getValidation();
            Chatroom loadedChatroom = isDeleteMembersSuccess.getData();
            if(validation == Validation.CHATROOM_DELETE_SUCCESS){
                Toast.makeText(context, "채팅방 나가기에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.CHATROOM_DELETE_FAIL){
                Toast.makeText(context, "채팅방 나가기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "채팅방 나가기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                int modified = data.getIntExtra("modified", -1);
                if(modified == 1){
                    //TODO refresh
                }
            }
        }
    }

    public void onChatroomLeaveDialogYesClicked(){
        chatViewModel.deleteMembers(chatroomId, id, chatroomType);
    }

    private void openLeftMenuWithMemberList(){
        binding.drawerLayout.openDrawer(GravityCompat.START);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChatMemberListFragment fragment = new ChatMemberListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("chatroomId", chatroomId);
        bundle.putInt("leaderId", leaderId);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.left_menu, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
