package com.example.moum.view.community;

import static com.example.moum.utils.TimeAgo.getTimeAgo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.databinding.ActivityBoardRecruitDetailBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardFreeDetailAdapter;
import com.example.moum.view.report.ReportMemberFragment;
import com.example.moum.viewmodel.community.BoardRecruitDetailViewModel;

import java.util.ArrayList;

public class BoardRecruitDetailActivity extends AppCompatActivity {
    private ActivityBoardRecruitDetailBinding binding;
    private BoardRecruitDetailViewModel boardRecruitDetailViewModel;
    private SharedPreferenceManager sharedPreferenceManager;
    private BoardFreeDetailAdapter adapter;
    private Integer memberId;
    private int targetBoardId;
    private ToggleButton wishlistButton;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        boardRecruitDetailViewModel = new ViewModelProvider(this).get(BoardRecruitDetailViewModel.class);
        super.onCreate(savedInstanceState);
        binding = ActivityBoardRecruitDetailBinding.inflate(getLayoutInflater());
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

        /* 게시글 감지 설정*/
        boardRecruitDetailViewModel.getIsLoadArticeSuccess().observe(this, articleData -> {
            if (articleData != null) {
                binding.boardRecruitDetailWriter.setText(articleData.getAuthor());
                binding.boardRecruitDetailTime.setText(getTimeAgo(articleData.getCreateAt()));
                binding.boardRecruitDetailTitle.setText(articleData.getTitle());
                binding.boardRecruitDetailContent.setText(articleData.getContent());
                binding.boardRecruitDetailLikeCount.setText(String.valueOf(articleData.getLikeCounts()));
            } else {
                Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /* 게시글 로드 */
        boardRecruitDetailViewModel.loadArticlesDetail(targetBoardId);


        /* UI 동작 추가 */
        initLeftArrow();
        initMenu();
        initImageRecyclerview();
        initRecyclerviewContent();
        initInputbutton();

    }

    public void initLeftArrow(){
        binding.leftarrow.setOnClickListener(v -> {
            finish();
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

    private void initImageRecyclerview() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.boardRecruitDetailRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //TODO 이미지 어댑터 구성 + viewmodel 이미지 파일 적용
    }


    private void initRecyclerviewContent() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.boardRecruitDetailRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // RecyclerView 어댑터 설정 (처음에 빈 데이터로 어댑터 설정)
        ArrayList<Comment> comments = new ArrayList<>();
        adapter = new BoardFreeDetailAdapter( comments, context); // 초기 null 값 설정
        recyclerView.setAdapter(adapter);

        // 댓글 데이터 관찰
        boardRecruitDetailViewModel.getCurrentComments().observe(this, commentList -> {
            if (commentList != null) {
                adapter.updateComment(commentList);
            }
        });

        // Validation 상태 관찰
        boardRecruitDetailViewModel.getValidationStatus().observe(this, validation -> {
            if (validation == Validation.ARTICLE_GET_FAILED) {
                Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        boardRecruitDetailViewModel.loadArticlesDetail(targetBoardId);
    }

    public void initInputbutton(){
        binding.boardRecruitDetailInputButton.setOnClickListener(v -> {
            String content = binding.boardRecruitDetailInputBox.getText().toString();
            boardRecruitDetailViewModel.postComment(targetBoardId, content);
            binding.boardRecruitDetailInputBox.setText("");
        });
    }

    public void commentPopupMenu(View view, int position) {
        // PopupMenu 생성
        PopupMenu popupMenu = new PopupMenu(this, view);

        // 메뉴 항목 추가
        popupMenu.getMenu().add("삭제하기");
        popupMenu.getMenu().add("신고하기");

        // 메뉴 항목 클릭 이벤트 처리
        popupMenu.setOnMenuItemClickListener(item -> {
            Comment comment = boardRecruitDetailViewModel.getCurrentComments().getValue().get(position);
            switch (item.getTitle().toString()) {
                case "삭제하기":
                    Toast.makeText(this, "삭제하기가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                    boardRecruitDetailViewModel.deleteComment(comment.getCommentId());
                    adapter.notifyItemRemoved(position);
                    break;

                case "신고하기":
//                    Toast.makeText(this, "신고하기가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Position: " + position + ", Author: " + comment.getAuthor(), Toast.LENGTH_SHORT).show();


                    ReportMemberFragment reportMemberFragment = new ReportMemberFragment(context);

                    // 신고 대상 멤버 ID 전달
                    Bundle args = new Bundle();
                    args.putInt("targetMemberId", comment.getAuthorId());
                    reportMemberFragment.setArguments(args);

                    // BottomSheetDialogFragment 표시
                    AppCompatActivity activity = (AppCompatActivity) context;
                    reportMemberFragment.show(activity.getSupportFragmentManager(), "ReportMemberFragment");
                    break;

                default:
                    break;
            }
            return true;
        });

        // 메뉴 표시
        popupMenu.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
