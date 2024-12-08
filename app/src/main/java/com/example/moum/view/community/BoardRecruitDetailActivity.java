package com.example.moum.view.community;

import static com.example.moum.utils.TimeAgo.getTimeAgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.databinding.ActivityBoardRecruitDetailBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardRecruitDetailAdapter;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.view.report.ReportArticleFragment;
import com.example.moum.view.report.ReportMemberFragment;
import com.example.moum.viewmodel.community.BoardRecruitDetailViewModel;

import java.util.ArrayList;

public class BoardRecruitDetailActivity extends AppCompatActivity {
    private ActivityBoardRecruitDetailBinding binding;
    private BoardRecruitDetailViewModel boardRecruitDetailViewModel;
    private SharedPreferenceManager sharedPreferenceManager;
    private BoardRecruitDetailAdapter adapter;
    private Integer memberId;
    private int targetBoardId;
    private Context context;
    private String profileURL;
    private ArrayList<Comment> comments = new ArrayList<>();

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

        /* 좋아요 감시 */
        boardRecruitDetailViewModel.getIsLikeSuccess().observe(this, like -> {
            if (like.getData() != null) {
                Drawable currentBackground = binding.buttonLikeImage.getBackground();
                Drawable heartClickDrawable = ContextCompat.getDrawable(context, R.drawable.icon_heart_click);

                if (currentBackground != null && currentBackground.getConstantState() != null &&
                        currentBackground.getConstantState().equals(heartClickDrawable.getConstantState())) {
                    binding.buttonLikeImage.setBackgroundResource(R.drawable.icon_heart_click_no);
                } else {
                    binding.buttonLikeImage.setBackgroundResource(R.drawable.icon_heart_click);
                }
                // 좋아요 카운트 업데이트
                binding.boardRecruitDetailLikeCount.setText(String.valueOf(like.getData().getLikesCount()));
            }
        });

        /* 좋아요 추가 삭제 */
        boardRecruitDetailViewModel.getIsPostLikeSuccess().observe(this, like -> {
            Validation validation = like.getValidation();
            if(validation != null){
                switch(validation){
                    case ARTICLE_NOT_FOUND:
                        Toast.makeText(context, "이미 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case CANNOT_CREATE_SELF_LIKES:
                        Toast.makeText(context, "본인은 좋아요를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
            boardRecruitDetailViewModel.loadLike(memberId, targetBoardId);
        });

        /* 댓글 감시 */
        boardRecruitDetailViewModel.getisLoadCommentsSuccess().observe(this, commentList -> {
            if (commentList != null) {
                Log.e("commentList", commentList.toString());
                adapter.updateComment(commentList.getData());
            }
        });

        /* 댓글 삭제 추가 감시 */
        boardRecruitDetailViewModel.getIsChangeCommentSuccess().observe(this, comment -> {
            Validation validataion = comment.getValidation();
            if(validataion == Validation.COMMENT_CREATE_SUCCESS || validataion == Validation.COMMENT_DELETE_SUCCESS){
                boardRecruitDetailViewModel.loadComments(targetBoardId);
                adapter.notifyDataSetChanged();
            }
        });

        /* 작성자 프로필 감시 */
        boardRecruitDetailViewModel.getIsLoadMemberSuccess().observe(this, member -> {
            if (member != null) {
                profileURL = member.getData().getProfileImageUrl();
                if (profileURL != null) {
                    Glide.with(binding.boardRecruitDetailImage.getContext())
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .placeholder(R.drawable.background_circle_darkgray)
                                    .error(R.drawable.background_circle_darkgray)
                                    .circleCrop())
                            .load(profileURL)
                            .into(binding.boardRecruitDetailImage);

                    // 이미지뷰의 외곽을 둥글게 설정 (Outline 적용)
                    binding.boardRecruitDetailImage.setClipToOutline(true);
                } else {
                    // 프로필 URL이 없을 경우 기본 이미지 설정
                    binding.boardRecruitDetailImage.setImageResource(R.drawable.background_circle_darkgray);
                    binding.boardRecruitDetailImage.setClipToOutline(true);
                }
            }
        });

        /* 게시글 로드 */
        boardRecruitDetailViewModel.loadArticlesDetail(targetBoardId);
        boardRecruitDetailViewModel.loadLike(memberId,targetBoardId);
        boardRecruitDetailViewModel.loadComments(targetBoardId);


        /* UI 동작 추가 */
        initLeftArrow();
        initLikeButton();
        initMenu();
        initRecyclerviewContent();
        initInputbutton();
        initprofileImage();

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
            Article article = boardRecruitDetailViewModel.getIsLoadArticeSuccess().getValue();
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
                        Intent editIntent = new Intent(this, BoardRecruitWriteActivity.class);
                        startActivity(editIntent);
                        break;
                    case "삭제하기":
                        boardRecruitDetailViewModel.deleteArticle(targetBoardId);
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

    public void initLikeButton(){
        binding.buttonLike.setOnClickListener(v -> {
            boardRecruitDetailViewModel.postLike(memberId ,targetBoardId);
            Validation validation = boardRecruitDetailViewModel.getIsLikeSuccess().getValue().getValidation();
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
            boardRecruitDetailViewModel.loadLike(memberId,targetBoardId);
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
        adapter = new BoardRecruitDetailAdapter(comments, context, boardRecruitDetailViewModel);

        // 프로필 클릭 이벤트
        adapter.setOnProfileClickListener(position -> {
            // 어댑터에서 클릭한 아이템 가져오기
            Comment clickedComment = adapter.getCommentAt(position);
            if (clickedComment != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("targetMemberId", boardRecruitDetailViewModel.getIsLoadMemberSuccess().getValue().getData().getId());

                // 프로필 프래그먼트 생성
                MemberProfileFragment fragment = new MemberProfileFragment(context);
                fragment.setArguments(bundle);

                fragment.show(((FragmentActivity) context).getSupportFragmentManager(), fragment.getTag());

            }
        });
        recyclerView.setAdapter(adapter);

        boardRecruitDetailViewModel.getValidationStatus().observe(this, validation -> {
            if (validation == Validation.ARTICLE_GET_FAILED) {
                Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initInputbutton(){
        binding.boardRecruitDetailInputButton.setOnClickListener(v -> {
            String content = binding.boardRecruitDetailInputBox.getText().toString();
            boardRecruitDetailViewModel.postComment(targetBoardId, content);
            binding.boardRecruitDetailInputBox.setText("");
        });
    }

    public void initprofileImage() {
        binding.boardRecruitDetailImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("targetMemberId", boardRecruitDetailViewModel.getIsLoadMemberSuccess().getValue().getData().getId());
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
        if(memberId.equals(comments.get(position).getAuthorId())) {
            popupMenu.getMenu().add("삭제하기");
        }
        popupMenu.getMenu().add("신고하기");

        // 메뉴 항목 클릭 이벤트 처리
        popupMenu.setOnMenuItemClickListener(item -> {
            Comment comment = boardRecruitDetailViewModel.getisLoadCommentsSuccess().getValue().getData().get(position);
            switch (item.getTitle().toString()) {
                case "삭제하기":
                    boardRecruitDetailViewModel.deleteComment(comment.getCommentId());
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
