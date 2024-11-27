package com.example.moum.view.community;

import static com.example.moum.view.community.adapter.TimeAgo.getTimeAgo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.databinding.ActivityBoardFreeDetailBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardFreeDetailAdapter;
import com.example.moum.viewmodel.community.BoardFreeDetailViewModel;

import java.util.ArrayList;

public class BoardFreeDetailActivity extends AppCompatActivity {
    private ActivityBoardFreeDetailBinding binding;
    private BoardFreeDetailViewModel boardFreeDetailViewModel;
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer memberId;
    private int targetBoardId;
    private ToggleButton wishlistButton;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        boardFreeDetailViewModel = new ViewModelProvider(this).get(BoardFreeDetailViewModel.class);
        super.onCreate(savedInstanceState);
        binding = ActivityBoardFreeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        Intent intent = getIntent();
        targetBoardId = intent.getIntExtra("targetBoardId", -1);

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context, InitialActivity.class);
            startActivity(intent1);
        }


        if (targetBoardId < 0) {
            Toast.makeText(this, "잘못된 게시물입니다.", Toast.LENGTH_SHORT).show();
            finish();  // 잘못된 경우 Activity 종료
            return;
        }

        initLeftArrow();
        initWishlistButton();
        initMenu();
        initRecyclerviewContent();

    }

    public void initLeftArrow(){
        binding.leftarrow.setOnClickListener(v -> {
            finish();
        });
    }

    private void initWishlistButton() {
        wishlistButton = findViewById(R.id.wishlist);
        wishlistButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Wishlist", "On 상태");
                } else {
                    Log.d("Wishlist", "Off 상태");
                }
            }
        });
    }

    private void initMenu() {
        binding.menu.setOnClickListener(v -> {
            // PopupMenu 생성
            PopupMenu popupMenu = new PopupMenu(this, binding.menu);

            // 메뉴 항목 추가
            popupMenu.getMenu().add("수정하기");
            popupMenu.getMenu().add("신고하기");

            // 메뉴 항목 클릭 이벤트 처리
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "수정하기":
                        Intent editIntent = new Intent(this, BoardFreeWriteActivity.class);
                        startActivity(editIntent);
                        break;

                    case "신고하기":
                        Toast.makeText(this, "신고하기가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
                return true;
            });

            // 메뉴 표시
            popupMenu.show();
        });
    }


    private void initRecyclerviewContent() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.boardFreeDetailRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // RecyclerView 어댑터 설정 (처음에 빈 데이터로 어댑터 설정)
        Article article = new Article();
        ArrayList<Comment> comments = new ArrayList<>();
        BoardFreeDetailAdapter adapter = new BoardFreeDetailAdapter(article, comments); // 초기 null 값 설정
        recyclerView.setAdapter(adapter);

        // ViewModel 초기화
        BoardFreeDetailViewModel viewModel = new ViewModelProvider(this).get(BoardFreeDetailViewModel.class);

        // 게시글 데이터 관찰
        viewModel.getArticleLiveData().observe(this, articleData -> {
            if (articleData != null) {
                // 어댑터에 게시글 데이터를 전달하여 갱신
                adapter.updateArticleData(articleData);
            }
        });

        // 댓글 데이터 관찰
        viewModel.getCommentLiveData().observe(this, commentList -> {
            if (commentList != null) {
                // 어댑터에 댓글 데이터를 전달하여 갱신
                adapter.updateCommentData(commentList);
            }
        });

        // Validation 상태 관찰
        viewModel.getValidationStatus().observe(this, validation -> {
            if (validation == Validation.ARTICLE_GET_FAILED) {
                // 데이터 로딩 실패 처리
                Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.loadArticlesDetail(targetBoardId);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
