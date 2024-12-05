package com.example.moum.view.community;

import static com.example.moum.utils.TimeAgo.getTimeAgo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.moum.data.entity.Like;
import com.example.moum.databinding.ActivityBoardFreeDetailBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardFreeDetailAdapter;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.view.profile.TeamProfileFragment;
import com.example.moum.view.report.ReportArticleFragment;
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
    private Context context;
    private String profileURL;
    private ArrayList<Comment> comments = new ArrayList<>();

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
                boardFreeDetailViewModel.loadProfileImage(articleData.getAuthorId());

            } else {
                Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /* 좋아요 수 감시 */
        boardFreeDetailViewModel.getIsLikeSuccess().observe(this, like -> {
                binding.boardFreeDetailLikeCount.setText(String.valueOf(like.getData().getLikesCount()));
            binding.buttonLikeImage.setChecked(like.getData().getLiked());
        });

        /* 댓글 감시 */
        boardFreeDetailViewModel.getisLoadCommentsSuccess().observe(this, commentList -> {
            if (commentList != null) {
                for (Comment comment : commentList.getData()) {
                    Log.e("cccc", "Comment: " + comment.toString());
                }
                adapter.updateComment(commentList.getData());
            } else {
                Log.d("Comments", "No data or null");
            }
        });

        /* 댓글 삭제 추가 감시 */
        boardFreeDetailViewModel.getIsChangeCommentSuccess().observe(this, comment -> {
            Validation validataion = comment.getValidation();
            if(validataion == Validation.COMMENT_CREATE_SUCCESS || validataion == Validation.COMMENT_DELETE_SUCCESS){
                boardFreeDetailViewModel.loadComments(targetBoardId);
                adapter.notifyDataSetChanged();
            }
        });

        /* 작성자 프로필 감시 */
        boardFreeDetailViewModel.getIsLoadMemberSuccess().observe(this, member -> {
            if (member != null) {
                profileURL = member.getData().getProfileImageUrl();
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
            }
        });

        /* UI 동작 추가 */
        initLeftArrow();
        initLikeButton();
        initMenu();
        initRecyclerviewContent();
        initInputbutton();
        initprofileImage();

        /* 게시글 로드, 좋아요 로드 */
        boardFreeDetailViewModel.loadArticlesDetail(targetBoardId);
        boardFreeDetailViewModel.loadLike(memberId,targetBoardId);
        boardFreeDetailViewModel.loadComments(targetBoardId);

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

            // 작성자가 게시글 보는 본인 일 때
            Article article = boardFreeDetailViewModel.getIsLoadArticeSuccess().getValue();
            if(memberId.equals(article.getAuthorId())){
                //popupMenu.getMenu().add("수정하기");
                popupMenu.getMenu().add("삭제하기");
            }
            //메뉴 기본 항목
            popupMenu.getMenu().add("신고하기");

            // 메뉴 항목 클릭 이벤트 처리
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "수정하기":
                        Intent editIntent = new Intent(this, BoardFreeWriteActivity.class);
                        startActivity(editIntent);
                        break;
                    case "삭제하기":
                        boardFreeDetailViewModel.deleteArticle(targetBoardId);
                        finish();
                        break;
                    case "신고하기":
                        ReportArticleFragment reportArticleFragment = new ReportArticleFragment(context);

                        Bundle args = new Bundle();
                        args.putInt("targetArticleId", targetBoardId);
                        reportArticleFragment.setArguments(args);

                        AppCompatActivity activity = (AppCompatActivity) context;
                        reportArticleFragment.show(activity.getSupportFragmentManager(), "ReportArticleFragment");
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

        adapter = new BoardFreeDetailAdapter(comments, context, boardFreeDetailViewModel);
        recyclerView.setAdapter(adapter);

        // 프로필 클릭 이벤트
        adapter.setOnProfileClickListener(position -> {
            // 어댑터에서 클릭한 아이템 가져오기
            Comment clickedComment = adapter.getCommentAt(position);
            if (clickedComment != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("targetMemberId", boardFreeDetailViewModel.getIsLoadMemberSuccess().getValue().getData().getId());

                // 프로필 프래그먼트 생성
                MemberProfileFragment fragment = new MemberProfileFragment(context);
                fragment.setArguments(bundle);

                fragment.show(((FragmentActivity) context).getSupportFragmentManager(), fragment.getTag());

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
            boardFreeDetailViewModel.postLike(memberId ,targetBoardId);
            Validation validation = boardFreeDetailViewModel.getIsLikeSuccess().getValue().getValidation();
            if(validation != null){
                switch(validation){
                    case DUPLICATE_LIKES:
                        Toast.makeText(context, "이미 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case CANNOT_CREATE_SELF_LIKES:
                        Toast.makeText(context, "본인은 좋아요를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
            boardFreeDetailViewModel.loadLike(memberId,targetBoardId);
        });
    }

    public void initprofileImage() {
        binding.boardFreeDetailImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("targetMemberId", boardFreeDetailViewModel.getIsLoadMemberSuccess().getValue().getData().getId());
            // MemberProfile 열기
            MemberProfileFragment fragment = new MemberProfileFragment(v.getContext());
            fragment.setArguments(bundle);
            fragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), fragment.getTag());

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
            Comment comment = boardFreeDetailViewModel.getisLoadCommentsSuccess().getValue().getData().get(position);
            switch (item.getTitle().toString()) {
                case "삭제하기":
                    boardFreeDetailViewModel.deleteComment(comment.getCommentId());
                    break;

                case "신고하기":
                    Log.e("Comment Info", "Position: " + position + ", Author: " + comment.getAuthor() + ", AuthorId: " + comment.getAuthorId());
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
