package com.example.moum.view.community;

import static com.example.moum.utils.TimeAgo.getTimeAgo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.BoardFreeItem;
import com.example.moum.data.entity.Genre;
import com.example.moum.databinding.ActivityCommunitySearchBinding;
import com.example.moum.utils.Validation;
import com.example.moum.view.community.adapter.BoardFreeItemAdapter;
import com.example.moum.viewmodel.community.CommunitySearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommunitySearchActivity extends AppCompatActivity {
    private ActivityCommunitySearchBinding binding;
    private CommunitySearchViewModel communitySearchViewModel;
    private BoardFreeItemAdapter adapter;
    private Context context;
    private String keyword;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        communitySearchViewModel = new ViewModelProvider(this).get(CommunitySearchViewModel.class);
        super.onCreate(savedInstanceState);

        binding = ActivityCommunitySearchBinding.inflate(getLayoutInflater());
        context = this;
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarSearch);

        initBackButton();
        initSearchButton();
        initSpinner1();
        initSpinner2();
        initSpinner3();
        initRecyclerView();
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
        SearchView searchView = findViewById(R.id.search_view);
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
                communitySearchViewModel.loadSearchArticles(keyword, category,false);
            }
        });
    }

    private void initSpinner1() {
        // 조회순 최신순 댓글순 좋아요순
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.community_board_spinner1_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySearchSpinner1.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySearchSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void initSpinner2() {
        List<String> spinnerItems = new ArrayList<>();

        spinnerItems.add("장르 선택");

        for (Genre genre : Genre.values()) {
            spinnerItems.add(genre.name());
        }


        for (Genre genre : Genre.values()) {
            spinnerItems.add(genre.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItems){
            @Override
            public boolean isEnabled(int position) {

                return position != 0;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 어댑터 연결
        binding.communitySearchSpinner2.setAdapter(adapter);

        // 기본 텍스트 색상 설정
        binding.communitySearchSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        binding.communitySearchSpinner2.setSelection(0);
    }

    private void initSpinner3() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.board_search_spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySearchSpinner3.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySearchSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    switch(position){
                        case 0:
                            category = "FREE_TALKING_BOARD";
                            break;
                        case 1:
                            category = "RECRUIT_BOARD";
                            break;
                        default:
                            break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        ArrayList<BoardFreeItem> initialItemList = new ArrayList<>();
        adapter = new BoardFreeItemAdapter(initialItemList);
        recyclerView.setAdapter(adapter);


        // 스크롤 리스너 추가
        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper()); // 여러번 호출되는 것을 막기 위한 디바운싱
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && adapter.getItemCount() > 0 && !communitySearchViewModel.isLoading()){
                    communitySearchViewModel.loadSearchArticles(keyword, category,true);
                    handler.postDelayed(() -> communitySearchViewModel.setIsloading(false), DEBOUNCE_DELAY);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // LiveData 관찰 및 데이터 로딩
        communitySearchViewModel.resetPagination();
        communitySearchViewModel.getIsLoadSearchArticlesSuccess().observe( this, result -> {
            if (result != null) {
                Validation validation = result.getValidation();
                List<Article> loadedArticles = result.getData();

                if (validation == Validation.ARTICLE_LIST_GET_SUCCESS && loadedArticles != null) {
                    // 데이터 업데이트
                    ArrayList<BoardFreeItem> updatedItemList = new ArrayList<>();
                    for (Article article : loadedArticles) {
                        BoardFreeItem item = new BoardFreeItem();
                        item.setBoardFreeItem(
                                article.getId(),
                                article.getTitle(),
                                article.getAuthor(),
                                getTimeAgo(article.getCreateAt()),
                                article.getCommentsCounts(),
                                article.getViewCounts()
                        );
                        if (article.getFileURL() != null) {
                            item.setImage(article.getFileURL().get(0));
                        }
                        updatedItemList.add(item);
                    }
                    adapter.updateItemList(updatedItemList);
                    communitySearchViewModel.setRecentSize(updatedItemList.size());
                } else {
                    // 에러 처리
                    Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}