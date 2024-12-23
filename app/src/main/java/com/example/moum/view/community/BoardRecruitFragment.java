package com.example.moum.view.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private final String TAG = getClass().toString();


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
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
        }

        //initSpinner();
        initRecyclerView();
        initFloatingActionButton();

        boardRecruitViewModel.loadArticleCategoryList();

        return root;
    }

    private void initSpinner() {
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
                if (parent != null) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


        // 스크롤 리스너 추가
        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper()); // 여러번 호출되는 것을 막기 위한 디바운싱
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && adapter.getItemCount() > 0 && !isLoading) {
                    isLoading = true;
                    boardRecruitViewModel.loadNextArticleCategoryList();
                    handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // LiveData 처음 페이지 관찰
        boardRecruitViewModel.getIsLoadArticlesCategorySuccess().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Validation validation = result.getValidation();
                List<Article> loadedArticles = result.getData();

                if (validation == Validation.ARTICLE_LIST_GET_SUCCESS && loadedArticles != null) {
                    // 데이터 업데이트
                    articles.clear();
                    articles.addAll(loadedArticles);
                    adapter.updateItemList(articles);
                } else {
                    // 에러 처리
                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
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

                if (validation == Validation.ARTICLE_LIST_GET_SUCCESS && loadedArticles != null) {
                    // 데이터 업데이트
                    articles.addAll(loadedArticles);
                    adapter.updateItemList(articles);
                } else {
                    // 에러 처리
                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // result가 null일 경우 에러 처리
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
        boardRecruitViewModel.resetPagination();
        boardRecruitViewModel.loadArticleCategoryList();
    }


}