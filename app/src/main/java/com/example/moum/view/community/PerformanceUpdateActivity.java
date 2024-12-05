package com.example.moum.view.community;

import static android.util.Log.e;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivityPerformanceCreateBinding;
import com.example.moum.databinding.ActivityPerformanceCreateOnwardBinding;
import com.example.moum.databinding.ActivityPerformanceUpdateBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.TimeManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.ParticipantAdapter;
import com.example.moum.view.dialog.PerformCreateDialog;
import com.example.moum.view.dialog.PerformUpdateDialog;
import com.example.moum.viewmodel.community.PerformanceCreateOnwardViewModel;
import com.example.moum.viewmodel.community.PerformanceCreateViewModel;
import com.example.moum.viewmodel.community.PerformanceUpdateViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PerformanceUpdateActivity extends AppCompatActivity {
    private PerformanceUpdateViewModel viewModel;
    private ActivityPerformanceUpdateBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private Integer performId;
    private Integer teamId;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    private ArrayList<Uri> uris = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();
    private ParticipantAdapter performanceParticipantAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerformanceUpdateBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(PerformanceUpdateViewModel.class);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*정보 불러오기*/
        Intent prevIntent = getIntent();
        performId = prevIntent.getIntExtra("performId", -1);
        teamId = prevIntent.getIntExtra("teamId", -1);
        if(performId == -1 || teamId == -1){
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

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

        /*Photo picker 선택 후 콜백*/
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

            if (uri != null) {
                viewModel.setProfileImage(uri, false);
                Log.d(TAG, "Selected URI: " + uri);
            } else {
                Log.d(TAG, "No media selected");
            }
        });

        /*사진 업로드하기 버튼 이벤트*/
        binding.buttonImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //photo picker 띄우기
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        /*사진 업로드 감시*/
        viewModel.getProfileImage().observe(this, uri -> {
            Log.e(TAG, "Uri: " + uri.toString());
            Glide.with(this).load(uri).into(binding.imageviewPerformProfile);
        });

        /*날짜 선택 이벤트*/
        final Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        binding.buttonDateStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PerformanceUpdateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            binding.buttonDateStart.setText(selectedDate);
                        }, thisYear, thisMonth, thisDay);
                datePickerDialog.show();
            }
        });
        binding.buttonDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PerformanceUpdateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            binding.buttonDateEnd.setText(selectedDate);
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
                binding.errorGenre.setText("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*참여 멤버 리사이클러뷰 표시*/
        RecyclerView recyclerView = binding.recyclerPerformParticipants;
        performanceParticipantAdapter = new ParticipantAdapter();
        performanceParticipantAdapter.setParticipants(members, id, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(performanceParticipantAdapter);

        /*song 레이아웃 동적 생성*/
        LinearLayout songParent = binding.performMusicParent;
        binding.buttonAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSongLayout(songParent);
            }
        });

        /*단체 정보 불러오기*/
        viewModel.loadTeam(teamId);

        /*단체 정보 불러오기 감시 결과*/
        viewModel.getIsLoadTeamSuccess().observe(this, isLoadTeamSuccess -> {
            Validation validation = isLoadTeamSuccess.getValidation();
            Team loadedTeam = isLoadTeamSuccess.getData();
            if(validation == Validation.GET_TEAM_SUCCESS){
                members.addAll(loadedTeam.getMembers());
                performanceParticipantAdapter.notifyItemInserted(members.size()-1);
                recyclerView.scrollToPosition(0);
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.TEAM_NOT_FOUND){
                Toast.makeText(context, "팀을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "단체 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*공연 정보 불러오기*/
        viewModel.loadPerformance(performId);

        /*공연 정보 불러오기 결과 감시 후 폼 채우기*/
        viewModel.getIsLoadPerformanceSuccess().observe(this, isLoadPerformanceSuccess -> {
            Validation validation = isLoadPerformanceSuccess.getValidation();
            Performance loadedPerform = isLoadPerformanceSuccess.getData();
            if(validation == Validation.PERFORMANCE_GET_SUCCESS){
                if(loadedPerform.getPerformanceName() != null) binding.edittextPerformName.setText(loadedPerform.getPerformanceName());
                if(loadedPerform.getPerformanceDescription() != null) binding.edittextPerformDescription.setText(loadedPerform.getPerformanceDescription());
                if(loadedPerform.getPerformanceLocation() != null) binding.edittextPerformPlace.setText(loadedPerform.getPerformanceLocation());
                if(loadedPerform.getPerformanceStartDate() != null) binding.buttonDateStart.setText(TimeManager.strToDate(loadedPerform.getPerformanceStartDate()));
                if(loadedPerform.getPerformanceEndDate() != null) binding.buttonDateEnd.setText(TimeManager.strToDate(loadedPerform.getPerformanceEndDate()));
                if(loadedPerform.getPerformancePrice() != null) binding.edittextPerformPrice.setText(String.format("%d", loadedPerform.getPerformancePrice()));
                if(loadedPerform.getGenre() != null) {
                    binding.spinnerGenre.setSelection(loadedPerform.getGenre().getValue());
                    viewModel.setGenre(genreList[loadedPerform.getGenre().getValue()]);
                }
                if(loadedPerform.getPerformanceImageUrl() != null && !loadedPerform.getPerformanceImageUrl().isEmpty()){
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                                    .error(R.drawable.background_more_rounded_gray_size_fit))
                            .load(loadedPerform.getPerformanceImageUrl()).into(binding.imageviewPerformProfile);
                    binding.imageviewPerformProfile.setClipToOutline(true);
                    viewModel.setProfileImage(Uri.parse(loadedPerform.getPerformanceImageUrl()), true);
                }
                if(loadedPerform.getMusics() != null &&!loadedPerform.getMusics().isEmpty()){
                    for(Music music : loadedPerform.getMusics()){
                        View songChild = addSongLayout(songParent);
                        EditText edittextSingName = songChild.findViewById(R.id.edittext_music_name);
                        EditText edittextArtistName = songChild.findViewById(R.id.edittext_artist_name);
                        edittextSingName.setText(music.getMusicName());
                        edittextArtistName.setText(music.getArtistName());
                    }
                }
                if(loadedPerform.getMembersId() != null && !loadedPerform.getMembersId().isEmpty()){
                    for(Member member : members){
                        for(Integer participateId : loadedPerform.getMembersId())
                            if(member.getId().equals(participateId))
                                performanceParticipantAdapter.setIsParticipate(members.indexOf(member), true);
                    }
                }
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT){
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "공연 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*제출 버튼 클릭 시 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String startDateString = binding.buttonDateStart.getText().toString();
                String endDateString = binding.buttonDateEnd.getText().toString();
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
                binding.errorPerformName.setText("모음 이름을 입력하세요.");
                binding.errorPerformName.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.GENRE_NOT_WRITTEN){
                binding.errorGenre.setText("장르를 선택하세요.");
                binding.spinnerGenre.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.DATE_NOT_VALID){
                binding.errorPerformDate.setText("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
            }
            else if(isValidCheckSuccess == Validation.MUSIC_NAME_NOT_WRITTEN){
                Toast.makeText(context, "곡 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isValidCheckSuccess == Validation.ARTIST_NAME_NOT_WRITTEN){
                Toast.makeText(context, "아티스트 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isValidCheckSuccess == Validation.VALID_ALL){
                // valid check 유효하다면, 최종 다이얼로그 띄우기
                PerformUpdateDialog performUpdateDialog = new PerformUpdateDialog(this, binding.edittextPerformName.getText().toString());
                performUpdateDialog.show();
            }
            else{
                Toast.makeText(context, "공연게시글 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*updatePerformance 결과 감시*/
        viewModel.getIsPerformanceUpdateSuccess().observe(this, isPerformanceUpdateSuccess -> {
            Validation validation = isPerformanceUpdateSuccess.getValidation();
            Performance performance = isPerformanceUpdateSuccess.getData();
            if(validation == Validation.PERFORMANCE_UPDATE_SUCCESS){
                Toast.makeText(context, "공연게시글 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(validation == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.TEAM_NOT_FOUND) {
                Toast.makeText(context, "팀을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NO_AUTHORITY){
                Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "공연게시글 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        binding.edittextPerformName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorPerformName.setText("");
                    binding.placeholderMoumName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextPerformDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorPerformDescription.setText("");
                    binding.placeholderPerformDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderPerformDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextPerformPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorPerformPlace.setText("");
                    binding.placeholderPerformPlace.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderPerformPlace.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextPerformPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorPerformPrice.setText("");
                    binding.placeholderPerformPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderPerformPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.spinnerGenre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorGenre.setText("");
                }
            }
        });
    }

    public void onDialogYesClicked(){
        /*다이얼로그에서 Yes 버튼 클릭 시, updatePerformance() 호출*/
        viewModel.setMembers(members, performanceParticipantAdapter.getIsParticipates());
        viewModel.updatePerformance(performId, teamId, context);
    }

    public View addSongLayout(LinearLayout songParent){
        // inflate
        View songChild = LayoutInflater.from(PerformanceUpdateActivity.this).inflate(R.layout.item_moum_music_form, songParent, false);

        // child 세팅
        LinearLayout placeholderMusicName = songChild.findViewById(R.id.placeholder_music_name);
        EditText editTextMusicName = songChild.findViewById(R.id.edittext_music_name);
        TextView errorMusicName = songChild.findViewById(R.id.error_music_name);
        LinearLayout placeholderArtistName = songChild.findViewById(R.id.placeholder_artist_name);
        EditText editTextArtistName = songChild.findViewById(R.id.edittext_artist_name);
        TextView errorArtistName = songChild.findViewById(R.id.error_artist_name);
        ImageView buttonDelete = songChild.findViewById(R.id.button_delete);

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
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songParent.removeView(songChild);
            }
        });

        // parent에 child view 추가
        songParent.addView(songChild);

        return songChild;
    }
}
