package com.example.moum.view.community;

import static com.example.moum.utils.TimeAgo.getTimeAgo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.databinding.ActivityBoardFreeDetailBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardFreeDetailAdapter;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.view.profile.TeamProfileFragment;
import com.example.moum.view.report.ReportMemberFragment;
import com.example.moum.viewmodel.community.BoardFreeDetailViewModel;

import java.util.ArrayList;

public class BoardFreeDetailActivity extends AppCompatActivity {
    private ActivityBoardFreeDetailBinding binding;
    private BoardFreeDetailViewModel boardFreeDetailViewModel;
    private SharedPreferenceManager sharedPreferenceManager;
    private BoardFreeDetailAdapter adapter;
    private Integer memberId;
    private int targetBoardId;
    private ToggleButton wishlistButton;
    private Context context;
    private String profileURL;

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

        /* 게시글 감지 설정*/
        boardFreeDetailViewModel.getIsLoadArticeSuccess().observe(this, articleData -> {
            if (articleData != null) {
                binding.boardFreeDetailWriter.setText(articleData.getAuthor());
                binding.boardFreeDetailTime.setText(getTimeAgo(articleData.getCreateAt()));
                binding.boardFreeDetailTitle.setText(articleData.getTitle());
                binding.boardFreeDetailContent.setText(articleData.getContent());
                binding.boardFreeDetailLikeCount.setText(String.valueOf(articleData.getLikeCounts()));
            } else {
                Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /* 게시글 로드 */
        boardFreeDetailViewModel.loadArticlesDetail(targetBoardId);


        /* UI 동작 추가 */
        initLeftArrow();
        initWishlistButton();
        initLikeButton();
        initMenu();
        initRecyclerviewContent();
        initInputbutton();
     //   initprofileImage();

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

            // 작성자가 게시글 보는 본인 일 때
            popupMenu.getMenu().add("수정하기");
            popupMenu.getMenu().add("삭제하기");
            //메뉴 기본 항목
            popupMenu.getMenu().add("URL 복사하기");
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
        ArrayList<Comment> comments = new ArrayList<>();
        adapter = new BoardFreeDetailAdapter(comments, context); // 초기 null 값 설정
        recyclerView.setAdapter(adapter);

        // 댓글 데이터 관찰
        boardFreeDetailViewModel.getCurrentComments().observe(this, commentList -> {
            if (commentList != null) {
                adapter.updateComment(commentList);
            }
        });

        // Validation 상태 관찰
        boardFreeDetailViewModel.getValidationStatus().observe(this, validation -> {
            if (validation == Validation.ARTICLE_GET_FAILED) {
                Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initInputbutton(){
        binding.boardFreeDetailInputButton.setOnClickListener(v -> {
            String content = binding.boardFreeDetailInputBox.getText().toString();
            boardFreeDetailViewModel.postComment(targetBoardId, content);
            binding.boardFreeDetailInputBox.setText("");
        });
    }

    public void initLikeButton(){
        binding.buttonLike.setOnClickListener(v -> {
            boardFreeDetailViewModel.postLike(targetBoardId);
        });
        boardFreeDetailViewModel.loadArticlesDetail(targetBoardId);
    }

    public void initprofileImage() {

        Article article = boardFreeDetailViewModel.getIsLoadArticeSuccess().getValue();
        profileURL = boardFreeDetailViewModel.loadProfileImage(article.getAuthorId(), article.getAuthor());

        if (profileURL != null) {
            Glide.with(binding.boardFreeDetailImage.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_circle_darkgray)
                            .error(R.drawable.background_circle_darkgray)
                            .circleCrop())
                    .load(profileURL)
                    .into(binding.boardFreeDetailImage);

            // 이미지뷰의 외곽을 둥글게 설정 (Outline 적용)
            binding.boardFreeDetailImage.setClipToOutline(true);
        } else {
            // 프로필 URL이 없을 경우 기본 이미지 설정
            binding.boardFreeDetailImage.setImageResource(R.drawable.background_circle_darkgray);
            binding.boardFreeDetailImage.setClipToOutline(true);
        }

        binding.boardFreeDetailImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if(boardFreeDetailViewModel.isTeamOrMember()) {
                bundle.putInt("targetTeamId", boardFreeDetailViewModel.getIsLoadTeamSuccess().getValue().getTeamId());
                // TeamProfile 열기
                TeamProfileFragment fragment = new TeamProfileFragment(v.getContext());
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), fragment.getTag());
            } else {
                bundle.putInt("targetMemberId", boardFreeDetailViewModel.getIsLoadMemberSuccess().getValue().getId());
                // MemberProfile 열기
                MemberProfileFragment fragment = new MemberProfileFragment(v.getContext());
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), fragment.getTag());
            }
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
            Comment comment = boardFreeDetailViewModel.getCurrentComments().getValue().get(position);
            switch (item.getTitle().toString()) {
                case "삭제하기":
                    Toast.makeText(this, "삭제하기가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                    boardFreeDetailViewModel.deleteComment(comment.getCommentId());
                    adapter.notifyItemRemoved(position);
                    break;

                case "신고하기":
                    Toast.makeText(this, "신고하기가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("Comment Info", "Position: " + position + ", Author: " + comment.getAuthor() + ", AuthorId: " + comment.getAuthorId());
                    ReportMemberFragment reportMemberFragment = new ReportMemberFragment(context);

                    // 신고 대상 멤버 ID 전달
                    Bundle args = new Bundle();
                    args.putInt("targetMemberId", comment.getAuthorId()); // 신고 대상 멤버 ID 전달
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
