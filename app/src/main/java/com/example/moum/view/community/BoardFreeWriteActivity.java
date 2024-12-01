package com.example.moum.view.community;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivityBoardFreeWriteBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardFreeWriteAdapter;
import com.example.moum.viewmodel.community.BoardFreeWriteViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardFreeWriteActivity extends AppCompatActivity {
    private ActivityBoardFreeWriteBinding binding;
    private BoardFreeWriteViewModel boardFreeWriteViewModel;
    private SharedPreferenceManager sharedPreferenceManager;
    private BoardFreeWriteAdapter adapter;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Context context;
    private Integer memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boardFreeWriteViewModel = new ViewModelProvider(this).get(BoardFreeWriteViewModel.class);
        super.onCreate(savedInstanceState);

        binding = ActivityBoardFreeWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String userName = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        initBackButton();
        initWriteButton();
        initSpinnerWriter();
        initSpinnerGenre();
        initializePickMedia();
        initImageRecyclerView();

    }

    private void initializePickMedia() {
        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        // 선택된 이미지 URI 처리
                        Log.d("PickMedia", "선택된 이미지 URI: " + uri.toString());
                        // ViewModel 또는 RecyclerView에 이미지 추가 로직 작성
                        boardFreeWriteViewModel.setFileImage(uri);
                    } else {
                        Log.d("PickMedia", "선택된 이미지 없음");
                    }
                }
        );
    }

    private void initBackButton() {
        binding.leftarrow.setOnClickListener(v -> finish());
    }

    private void initWriteButton() {
        // 작성 버튼 클릭 이벤트
        binding.buttonWrite.setOnClickListener(v -> {
            //TODO author 바꿀 수 있게 api 추가
            String author = binding.boardFreeWriteSpinner.getSelectedItem().toString();
            String title = binding.boardFreeWriteTitle.getText().toString();
            String content = binding.boardFreeWriteContent.getText().toString();
            Integer genre = binding.boardFreeWriteGenreSpinner.getSelectedItemPosition() - 1;
            String category = "FREE_TALKING_BOARD";
            // 리사이클러뷰에 표시된 이미지 URI 리스트 가져오기
            List<Uri> imageUris = boardFreeWriteViewModel.getFileImage().getValue();
            // 장르 목록 (빈 리스트로 초기화)

            // 제목과 내용이 비어있지 않은지 확인
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(context, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 게시글 생성 함수 호출
            boardFreeWriteViewModel.createArticle(title, content, category, genre, imageUris);

            // 성공 시 처리
            boardFreeWriteViewModel.getIsArticleCreateSuccess().observe(this, result -> {
                Validation validation = result.getValidation();
                switch (validation) {
                    case VALID_ALL:
                        Toast.makeText(context, "게시글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        break;

                    case ARTICLE_INVALID_TITLE:
                        Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        break;

                    case ARTICLE_INVALID_CONTENT:
                        Toast.makeText(context, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        break;

                    case ARTICLE_INVALID_CATEGORY:
                        Toast.makeText(context, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        break;

                    case ARTICLE_INVALID_GENRE:
                        Toast.makeText(context, "장르를 하나 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(context, "게시글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            });
        });
    }

    private void initSpinnerWriter() {
        boardFreeWriteViewModel.loadTeamsAsLeader(memberId);

        binding.boardFreeWriteSpinner.setEnabled(false);
        boardFreeWriteViewModel.getIsLoadTeamsAsLeaderSuccess().observe(this, result -> {
            Validation validation = result.getValidation();
            List<Team> loadedTeams = result.getData();

            // memberSelected의 값을 비동기적으로 처리
            boardFreeWriteViewModel.loadMemberProfile(memberId);
            boardFreeWriteViewModel.getMemberSelected().observe(this, member -> {
                if (member != null) {
                    // 스피너 항목 리스트 생성
                    List<String> spinnerItems = new ArrayList<>();
                    spinnerItems.add(member.getName()); // 유저 이름 추가

                    if (validation == Validation.GET_TEAM_LIST_SUCCESS) {
                        // 데이터가 없는 경우 처리
                        if (loadedTeams.isEmpty()) {
                            Toast.makeText(context, "리더로 속한 단체가 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 팀 이름 추가
                            for (Team team : loadedTeams) {
                                spinnerItems.add(team.getTeamName());
                            }
                        }
                    } else {
                        // 실패 시 에러 메시지 처리
                        Toast.makeText(context, "팀 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }

                    // 어댑터 연결
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerItems);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // 스피너에 어댑터 연결
                    binding.boardFreeWriteSpinner.setAdapter(adapter);
                    binding.boardFreeWriteSpinner.setEnabled(true); // 스피너 활성화
                } else {
                    // 멤버 데이터를 로드하지 못한 경우 처리
                    Toast.makeText(context, "사용자 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



    public void initSpinnerGenre() {
        List<String> spinnerItems = new ArrayList<>();

        spinnerItems.add("장르를 선택하세요");

        for (Genre genre : Genre.values()) {
            spinnerItems.add(genre.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 어댑터 연결
        binding.boardFreeWriteGenreSpinner.setAdapter(adapter);

        // 기본 텍스트 색상 설정
        binding.boardFreeWriteGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView selectedTextView = (TextView) selectedItemView;
                if (position == 0) {
                    // "장르를 선택하세요"는 기본 텍스트로만 존재
                    selectedTextView.setTextColor(ContextCompat.getColor(context, R.color.gray3));
                } else {
                    // 장르 항목이 선택된 경우 색을 기본 색상으로 설정
                    selectedTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때
            }
        });

        // 스피너 선택 시 기본 텍스트가 선택되지 않도록 하기
        binding.boardFreeWriteGenreSpinner.setSelection(0);
    }

    public void initImageRecyclerView() {
        RecyclerView recyclerView = binding.boardFreeWriteImageRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        // 초기 이미지 리스트
        ArrayList<String> imageUris = new ArrayList<>();

        // Adapter 설정
        adapter = new BoardFreeWriteAdapter(context, imageUris, new BoardFreeWriteAdapter.OnImageClickListener() {
            @Override
            public void onAddImageClick() {
                // 사진 추가 버튼 클릭 시 동작
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }

            @Override
            public void onImageClick(int position) {
                // 이미지 클릭 시 동작 (예: 확대)

            }

            @Override
            public void onImageLongClick(int position) {
                // 이미지 삭제 처리
                imageUris.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, imageUris.size());

            }
        });

        recyclerView.setAdapter(adapter);

        // ViewModel 관찰로 이미지 추가
        boardFreeWriteViewModel.getFileImage().observe(this, uri -> {
            if (uri != null && !imageUris.contains(uri.toString())) {
                imageUris.add(uri.toString());
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }


}
