package com.example.moum.view.community;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;


import com.example.moum.R;
import com.example.moum.databinding.ActivityCommunitySearchBinding;
import com.example.moum.viewmodel.community.CommunitySearchViewModel;

public class CommunitySearchActivity extends AppCompatActivity {
    private ActivityCommunitySearchBinding binding;
    private CommunitySearchViewModel communitySearchViewModel;
    private Context context;

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
        initRecyclerview();
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
                String keyword = searchView.getQuery().toString();
                communitySearchViewModel.loadSearchArticles(keyword);
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
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner2() {
        // 스피너 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.community_board_spinner2_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySearchSpinner2.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySearchSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner3() {
        // TODO 장르 필터링
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.board_free_spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySearchSpinner3.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySearchSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerview(){

    }


}