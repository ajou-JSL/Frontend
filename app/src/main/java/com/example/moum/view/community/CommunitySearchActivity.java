package com.example.moum.view.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.databinding.ActivityCommunitySearchBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.CommunitySearchAdapter;
import com.example.moum.viewmodel.community.CommunitySearchViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommunitySearchActivity extends AppCompatActivity{
    private ActivityCommunitySearchBinding binding;
    private CommunitySearchViewModel communitySearchViewModel;
    private SharedPreferenceManager sharedPreferenceManager;
    private CommunitySearchAdapter adapter;
    private Context context;
    private String keyword;
    private final ArrayList<Article> articles = new ArrayList<>();
    private Integer memberId;
    private boolean isLoading = false;
    private Integer spinner1Position = 0, spinner2Position = 0, spinner3Position = 0;
    private final String TAG = getClass().toString();
    private Boolean spinnerStatus[] = new Boolean[4];
    private NestedScrollView nestedScrollView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        communitySearchViewModel = new ViewModelProvider(this).get(CommunitySearchViewModel.class);
        super.onCreate(savedInstanceState);
        binding = ActivityCommunitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

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

        /* 새로고침 */
        SwipeRefreshLayout swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refresh();
            swipeRefreshLayout.setRefreshing(false);
        });

        /* UI 바인딩 */
        nestedScrollView = binding.communitySearchScrollView;;
        binding.communitySearchSpinner1.setSelection(spinner1Position);
        binding.communitySearchSpinner2.setSelection(spinner2Position);
        binding.communitySearchSpinner3.setSelection(spinner3Position);
        initSpinnerStatus();


        initBackButton();
        initSearchButton();
        initSpinner1();
        initSpinner2();
        initSpinner3();
        initRecyclerView();
    }

    private void initSpinnerStatus(){
        Arrays.fill(spinnerStatus, false);
    }

    private void initBackButton() {
        binding.leftarrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSearchButton(){
        SearchView searchView = binding.searchView;
        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchText != null) {
            searchText.setTextSize(16);
            searchText.setTypeface(ResourcesCompat.getFont(context, R.font.noto_sans_kr_regular));
            searchText.setIncludeFontPadding(false);
        }
        binding.searchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = searchView.getQuery().toString();
                communitySearchViewModel.loadArticleList( keyword, spinner1Position, spinner2Position, spinner3Position);
            }
        });
    }

    private void initSpinner1() {
        // 스피너 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.community_spinner1_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySearchSpinner1.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySearchSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "아이템 선택 spinner1");
                if (parent != null && spinnerStatus[1]) {
                    communitySearchViewModel.resetPagination();
                    spinner1Position = position;
                    communitySearchViewModel.loadArticleList( keyword, spinner1Position, spinner2Position, spinner3Position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.communitySearchSpinner1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.e(TAG, "onTouch spinner1");
                    spinnerStatus[1] = true;
                }
                return false;
            }
        });
    }

    private void initSpinner2() {
        List<String> spinnerItems = new ArrayList<>();

        spinnerItems.add("모든 장르");
        spinnerItems.addAll(communitySearchViewModel.getGenreName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 어댑터 연결
        binding.communitySearchSpinner2.setAdapter(adapter);

        // 스피너 아이템 클릭
        binding.communitySearchSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                if (parent != null && spinnerStatus[2]) {
                    communitySearchViewModel.resetPagination();
                    spinner2Position = position;
                    communitySearchViewModel.loadArticleList(keyword, spinner1Position, spinner2Position, spinner3Position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때
            }
        });

        binding.communitySearchSpinner2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerStatus[2] = true;
                }
                return false;
            }
        });
    }

    private void initSpinner3() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.board_search_spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySearchSpinner3.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySearchSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "아이템 선택 spinner3");
                if (parent != null && spinnerStatus[3]) {
                    communitySearchViewModel.resetPagination();
                    spinner3Position = position;
                    communitySearchViewModel.loadArticleList( keyword, spinner1Position, spinner2Position, spinner3Position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.communitySearchSpinner3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerStatus[3] = true;
                    Log.e(TAG, "onTouch spinner3");
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.communitySearchRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // item 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // RecyclerView 어댑터 설정
        adapter = new CommunitySearchAdapter(articles);
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
                        communitySearchViewModel.loadNextArticleList(keyword, spinner1Position, spinner2Position, spinner3Position);
                        handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
                    }
                }
            }
        });

        // LiveData 처음 호출 데이터 관찰 및 데이터 로딩
        communitySearchViewModel.getIsLoadArticlesCategorySuccess().observe(this, result -> {
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
                Toast.makeText(context, "응답이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // LiveData 다음 페이지 불러오기 관찰
        communitySearchViewModel.getIsLoadNextArticlesCategorySuccess().observe(this, result -> {
            if (result != null) {
                Validation validation = result.getValidation();
                List<Article> loadedArticles = result.getData();

                if (loadedArticles != null && !loadedArticles.isEmpty()) {
                    // 데이터 업데이트
                    articles.addAll(loadedArticles);
                    adapter.updateItemList(articles);
                } else {
                    // 에러 처리
                    communitySearchViewModel.setPageHolder();
                    Toast.makeText(context, "마지막 게시글입니다", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "응답이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        communitySearchViewModel.resetPagination();
        binding = null;
    }

    public void refresh() {
        isLoading = true;
        communitySearchViewModel.resetPagination();
        spinner1Position = 0;
        spinner2Position = 0;
        spinner3Position = 0;
        initSpinnerStatus();
        binding.communitySearchSpinner1.setSelection(spinner1Position);
        binding.communitySearchSpinner2.setSelection(spinner2Position);
        binding.communitySearchSpinner2.setSelection(spinner3Position);
        communitySearchViewModel.loadArticleList(keyword, spinner1Position, spinner2Position, spinner3Position);
        isLoading = false;
    }
}