package com.example.moum.view.moum;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Moum;
import com.example.moum.databinding.ActivityMoumCreateBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.MoumCreateDialog;
import com.example.moum.view.moum.adapter.MoumCreateImageAdapter;
import com.example.moum.viewmodel.moum.MoumCreateViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;

public class MoumCreateActivity extends AppCompatActivity {
    private MoumCreateViewModel viewModel;
    private ActivityMoumCreateBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private Integer teamId;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    private ArrayList<Uri> uris = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumCreateBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumCreateViewModel.class);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        //TODO 리더만 생성 가능하게 로직 수정

        /*단체 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        teamId = prevIntent.getIntExtra("teamId", -1);

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*이미지 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerMoumImages;
        MoumCreateImageAdapter moumCreateImageAdapter = new MoumCreateImageAdapter();
        moumCreateImageAdapter.setUris(uris, context);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(moumCreateImageAdapter);
        uris.add(null); //for image selector
        moumCreateImageAdapter.notifyItemInserted(uris.size()-1);

        /*Photo picker 선택 후 콜백*/
        pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    Log.d(TAG,  "Number of items selected: " + uris.size());
                    viewModel.setProfileImages(uris);
                } else {
                    Log.d(TAG, "No media selected");
                }
        });

        /*사진 업로드 감시*/
        viewModel.getProfileImages().observe(this, addedUris -> {
            for(Uri uri : addedUris){
                Log.e(TAG, "Uri: " + uri.toString());
            }
            uris.clear();
            uris.addAll(addedUris);
            uris.add(null); //for image selector
            moumCreateImageAdapter.notifyItemInserted(uris.size()-1);
            recyclerView.scrollToPosition(uris.size()-1);
        });

        /*날짜 선택 이벤트*/
        final Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        binding.buttonMoumDateStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MoumCreateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            binding.buttonMoumDateStart.setText(selectedDate);
                        }, thisYear, thisMonth, thisDay);
                datePickerDialog.show();
            }
        });
        binding.buttonMoumDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MoumCreateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            binding.buttonMoumDateEnd.setText(selectedDate);
                        }, thisYear, thisMonth, thisDay);
                datePickerDialog.show();
            }
        });

        /*genre 스피너 Adapter 연결*/
        Spinner genreSpinner = binding.spinnerGenre;
        String[] genreList = Genre.toStringArray();
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreList);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);

        viewModel.setGenre(genreList[0]);
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setGenre(genreList[position]);
                binding.errorMoumGenre.setText("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*song 레이아웃 동적 생성*/
        LinearLayout songParent = binding.moumMusicParent;
        binding.buttonAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inflate
                View songChild = LayoutInflater.from(MoumCreateActivity.this).inflate(R.layout.item_moum_music_form, songParent, false);

                // child 세팅
                LinearLayout placeholderMusicName = songChild.findViewById(R.id.placeholder_music_name);
                EditText editTextMusicName = songChild.findViewById(R.id.edittext_music_name);
                TextView errorMusicName = songChild.findViewById(R.id.error_music_name);
                LinearLayout placeholderArtistName = songChild.findViewById(R.id.placeholder_artist_name);
                EditText editTextArtistName = songChild.findViewById(R.id.edittext_artist_name);
                TextView errorArtistName = songChild.findViewById(R.id.error_artist_name);


                editTextMusicName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if(hasFocus){
                            placeholderMusicName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                        }else{
                            placeholderMusicName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                        }
                    }
                });
                editTextArtistName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if(hasFocus){
                            placeholderArtistName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                        }else{
                            placeholderArtistName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                        }
                    }
                });

                // parent에 child view 추가
                songParent.addView(songChild);
            }
        });

        /*제출 버튼 클릭 시 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String startDateString = binding.buttonMoumDateStart.getText().toString();
                String endDateString = binding.buttonMoumDateEnd.getText().toString();
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendValue(ChronoField.YEAR, 4)
                        .appendLiteral('-')
                        .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
                        .appendLiteral('-')
                        .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                        .toFormatter();
                LocalDate startDate = null;
                LocalDate endDate = null;
                if(!startDateString.equals("시작 날짜")) startDate = LocalDate.parse(startDateString, formatter);
                if(!endDateString.equals("종료 날짜")) endDate = LocalDate.parse(endDateString, formatter);
                viewModel.setDates(startDate, endDate);

                for (int i = 0; i < songParent.getChildCount(); i++) {
                    View songChild = songParent.getChildAt(i);
                    EditText editTextMusicName = songChild.findViewById(R.id.edittext_music_name);
                    EditText editTextArtistName = songChild.findViewById(R.id.edittext_artist_name);
                    String musicName = editTextMusicName.getText().toString();
                    String artistName = editTextArtistName.getText().toString();
                    viewModel.addMusic(musicName, artistName);
                }
                viewModel.validCheck();
            }
        });

        /*제출 버튼 클릭 결과 감시*/
        viewModel.getIsValidCheckSuccess().observe(this, isValidCheckSuccess -> {
            if(isValidCheckSuccess == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isValidCheckSuccess == Validation.MOUM_NAME_NOT_WRITTEN){
                binding.errorMoumName.setText("모음 이름을 입력하세요.");
                binding.errorMoumName.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.GENRE_NOT_WRITTEN){
                binding.errorMoumGenre.setText("장르를 선택하세요.");
                binding.spinnerGenre.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.VALID_ALL){
                // valid check 유효하다면, 최종 다이얼로그 띄우기
                MoumCreateDialog moumCreateDialog = new MoumCreateDialog(this, binding.edittextMoumName.getText().toString());
                moumCreateDialog.show();
            }
            else{
                Toast.makeText(context, "모음 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*createMoum() 결과 감시*/
        viewModel.getIsCreateMoumSuccess().observe(this, isCreateTeamSuccess -> {
            Validation validation = isCreateTeamSuccess.getValidation();
            Moum createdMoum = isCreateTeamSuccess.getData();
            if(validation == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.MUST_JOIN_TEAM_FIRST){
                Toast.makeText(context, "팀에 먼저 가입해야 합니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.TEAM_NOT_FOUND){
                Toast.makeText(context, "팀을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.CREATE_MOUM_SUCCESS){
                Toast.makeText(context, "모음 생성에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(context, "모음 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        binding.edittextMoumName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumName.setText("");
                    binding.placeholderMoumName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextMoumDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumDescription.setText("");
                    binding.placeholderMoumDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextMoumPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumPlace.setText("");
                    binding.placeholderMoumPlace.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumPlace.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextMoumPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumPrice.setText("");
                    binding.placeholderMoumPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.spinnerGenre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumGenre.setText("");
                }else{
                }
            }
        });

    }

    public void onDialogYesClicked(){
        /*다이얼로그에서 Yes 버튼 클릭 시, createMoum() 호출*/
        viewModel.createMoum(id, teamId, context);
    }

    public void onImageSelectorClicked(){
        /*이미지 선택 버튼 클릭 시, Photo picker 열기*/
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}
