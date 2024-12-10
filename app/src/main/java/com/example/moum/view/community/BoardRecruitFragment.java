package com.example.moum.view.community;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.databinding.FragmentBoardRecruitBinding;
import com.example.moum.utils.RefreshableFragment;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardRecruitItemAdapter;
import com.example.moum.viewmodel.community.BoardRecruitViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardRecruitFragment extends Fragment implements RefreshableFragment {
    private FragmentBoardRecruitBinding binding;
    private SharedPreferenceManager sharedPreferenceManager;
    private BoardRecruitViewModel boardRecruitViewModel;
    private BoardRecruitItemAdapter adapter;
    private final ArrayList<Article> articles = new ArrayList<>();
    private Context context;
    private Integer memberId;
    private boolean isLoading = false;
    private Integer spinner1Position = 0,spinner2Position = 0;
    private final String TAG = getClass().toString();
    private Boolean spinnerStatus[] = new Boolean[3];
    private NestedScrollView nestedScrollView = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardRecruitViewModel = new ViewModelProvider(this).get(BoardRecruitViewModel.class);
        binding = FragmentBoardRecruitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext();

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
        }

        /* 다른 화면 갔다올 때 스피너 UI 바인딩 */
        nestedScrollView = binding.boardRecruitScrollView;
        binding.boardRecruitSpinner1.setSelection(spinner1Position);
        binding.boardRecruitSpinner2.setSelection(spinner2Position);
        initSpinnerStatus();

        initSpinner1();
        initSpinner2();
        initRecyclerView();
        initFloatingActionButton();

        boardRecruitViewModel.loadArticleList(spinner1Position,spinner2Position);
        return root;
    }

    private void initSpinnerStatus(){
        Arrays.fill(spinnerStatus, false);
    }

    private void initSpinner1() {
        // 스피너 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.community_spinner1_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.boardRecruitSpinner1.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.boardRecruitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && spinnerStatus[1]) {
                    boardRecruitViewModel.resetPagination();
                    spinner1Position = position;
                    boardRecruitViewModel.loadArticleList(spinner1Position, spinner2Position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.boardRecruitSpinner1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerStatus[1] = true;
                }
                return false;
            }
        });
    }

    private void initSpinner2() {
        List<String> spinnerItems = new ArrayList<>();

        spinnerItems.add("모든 장르");
        spinnerItems.addAll(boardRecruitViewModel.getGenreName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 어댑터 연결
        binding.boardRecruitSpinner2.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.boardRecruitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
                if (parent != null && spinnerStatus[2]) {
                    boardRecruitViewModel.resetPagination();
                    spinner2Position = position;
                    boardRecruitViewModel.loadArticleList(spinner1Position, spinner2Position);
                }
            }
            @Override
            public void onNothingSelected (AdapterView < ? > parent){
                // 아무것도 선택되지 않았을 때
            }
        });

        binding.boardRecruitSpinner2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerStatus[2] = true;
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.boardRecruitRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // item 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new BoardRecruitItemAdapter(articles);
        recyclerView.setAdapter(adapter);


        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper()); // 여러번 호출되는 것을 막기 위한 디바운싱
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // NestedScrollView의 바닥에 도달했을 때
                if (scrollY + nestedScrollView.getHeight() >= v.getChildAt(0).getHeight()) {
                    // 스크롤이 바닥에 도달했을 때
                    if (adapter.getItemCount() > 0 && !isLoading) {
                        isLoading = true;
                        boardRecruitViewModel.loadNextArticleList(spinner1Position, spinner2Position);
                        handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
                    }
                }
            }
        });

        // LiveData 처음 페이지 관찰
        boardRecruitViewModel.getIsLoadArticlesCategorySuccess().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Validation validation = result.getValidation();
                List<Article> loadedArticles = result.getData();
                nestedScrollView.scrollTo(0, 0);
                if (loadedArticles != null && !loadedArticles.isEmpty()) {
                    // 데이터 업데이트
                    articles.clear();
                    articles.addAll(loadedArticles);
                    adapter.updateItemList(articles);
                } else {
                    articles.clear();
                    Article articleDummy = new Article();
                    articleDummy.setTitle("게시글이 없습니다.");
                    articleDummy.setCommentsCounts(0);
                    articleDummy.setViewCounts(0);
                    articles.add(articleDummy);
                    adapter.updateItemList(articles);
                }
            } else {
                // result가 null일 경우 에러 처리
                Toast.makeText(getContext(), "응답이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // LiveData 다음 페이지 불러오기 관찰
        boardRecruitViewModel.getIsLoadNextArticlesCategorySuccess().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Validation validation = result.getValidation();
                List<Article> loadedArticles = result.getData();

                if (loadedArticles != null && !loadedArticles.isEmpty()) {
                    // 데이터 업데이트
                    articles.addAll(loadedArticles);
                    adapter.updateItemList(articles);
                } else {
                    // 에러 처리
                    boardRecruitViewModel.setPageHolder();
                    Toast.makeText(getContext(), "마지막 게시글입니다", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "응답이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void initFloatingActionButton() {
        binding.communityFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardRecruitWriteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        boardRecruitViewModel.resetPagination();
        binding = null;
    }

    @Override
    public void refreshContent() {
        isLoading = true;
        boardRecruitViewModel.resetPagination();
        spinner1Position = 0;
        spinner2Position = 0;
        initSpinnerStatus();
        binding.boardRecruitSpinner1.setSelection(spinner1Position);
        binding.boardRecruitSpinner2.setSelection(spinner2Position);
        boardRecruitViewModel.loadArticleList(spinner1Position,spinner2Position);
        isLoading = false;
    }


}