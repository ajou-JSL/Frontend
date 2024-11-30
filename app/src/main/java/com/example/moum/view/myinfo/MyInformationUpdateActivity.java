package com.example.moum.view.myinfo;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Record;
import com.example.moum.databinding.ActivityMyinfoUpdateBinding;
import com.example.moum.databinding.ActivityPerformanceUpdateBinding;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.TimeManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.auth.SignupProfileActivity;
import com.example.moum.view.auth.adapter.GenreAdapter;
import com.example.moum.view.community.PerformanceUpdateActivity;
import com.example.moum.view.community.adapter.ParticipantAdapter;
import com.example.moum.view.dialog.ProfileUpdateDialog;
import com.example.moum.viewmodel.community.PerformanceUpdateViewModel;
import com.example.moum.viewmodel.myinfo.MyInformationUpdateViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;

public class MyInformationUpdateActivity extends AppCompatActivity {
    private MyInformationUpdateViewModel viewModel;
    private ActivityMyinfoUpdateBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    private ParticipantAdapter performanceParticipantAdapter;
    private LinearLayout recordParent;
    private Member member;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyinfoUpdateBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MyInformationUpdateViewModel.class);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

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
            Glide.with(this).load(uri).into(binding.imageviewProfile);
        });

        /*proficiency 스피너 Adapter 연결*/
        Spinner proficiencySpinner = binding.spinnerProficiency;
        String[] proficiencyList = getResources().getStringArray(R.array.proficiency_list);
        ArrayAdapter<String> proficiencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, proficiencyList);
        proficiencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proficiencySpinner.setAdapter(proficiencyAdapter);
        viewModel.setProficiency(proficiencyList[0]);
        proficiencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setProficiency(proficiencyList[position]);
                binding.errorProficiency.setText("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*address 스피너 Adapter 연결*/
        Spinner addressSpinner = binding.spinnerAddress;
        String[] addressList = getResources().getStringArray(R.array.address_list);
        ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addressList);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressSpinner.setAdapter(addressAdapter);
        viewModel.setAddress(addressList[0]);
        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setAddress(addressList[position]);
                binding.errorAddress.setText("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*이력 레이아웃 동적 생성*/
        recordParent = binding.recordParent;
        binding.buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // parent에 child view 추가
                View recordChild = getRecordChild();
                recordParent.addView(recordChild);
            }
        });

        /*장르 선택 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerGenres;
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this); //자연스러운 줄넘김을 위한 Flexbox 사용
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        GenreAdapter genreAdapter = new GenreAdapter();
        genreAdapter.setGenres(Genre.values(), context);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(genreAdapter);

        /*내 정보 불러오기*/
        viewModel.loadMemberProfile(id);

        /*내 정보 불러오기 결과 감시*/
        viewModel.getIsLoadMemberProfileSuccess().observe(this, isLoadMemberProfileSuccess -> {
            Validation validation = isLoadMemberProfileSuccess.getValidation();
            Member tMember = isLoadMemberProfileSuccess.getData();
            if(validation == Validation.GET_PROFILE_SUCCESS){
                member = tMember;
                if(tMember.getName() != null) binding.edittextNickname.setText(tMember.getName());
                if(tMember.getProfileDescription() != null) binding.edittextProfileDescription.setText(tMember.getProfileDescription());
                if(tMember.getInstrument() != null) binding.edittextInstrument.setText(tMember.getInstrument());
                if(tMember.getVideoUrl() != null) binding.edittextVideo.setText(tMember.getVideoUrl());
                if(ImageManager.isUrlValid(tMember.getProfileImageUrl()))
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray_size_fit)
                            .error(R.drawable.background_circle_gray_size_fit))
                            .load(tMember.getProfileImageUrl())
                            .into(binding.imageviewProfile);
                viewModel.setProfileImage(Uri.parse(tMember.getProfileImageUrl()), true);
                if(tMember.getMemberRecords() != null && !tMember.getMemberRecords().isEmpty()){
                    for(Record record : tMember.getMemberRecords()){
                        View recordChild = getRecordChild();
                        recordParent.addView(recordChild);
                        EditText edittextRecordName = recordChild.findViewById(R.id.signup_edittext_record_name);
                        AppCompatButton buttonRecordStart = recordChild.findViewById(R.id.button_record_date_start);
                        AppCompatButton buttonRecordEnd = recordChild.findViewById(R.id.button_record_date_end);
                        edittextRecordName.setText(record.getRecordName());
                        if(record.getStartDate() != null && !record.getStartDate().isEmpty()) buttonRecordStart.setText(record.getStartDate());
                        else buttonRecordStart.setText("시작 날짜");
                        if(record.getEndDate() != null && !record.getEndDate().isEmpty()) buttonRecordEnd.setText(record.getEndDate());
                        else buttonRecordEnd.setText("종료 날짜");
                    }
                }
                if(tMember.getGenres() != null && !tMember.getGenres().isEmpty()){
                    for(Genre genre : tMember.getGenres()){
                        genreAdapter.setIsSelected(genre.getValue(), true);
                    }
                    genreAdapter.notifyDataSetChanged();
                }
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "프로필을 불러올 수 없습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*제출 버튼 클릭 시 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.validCheckProfile();
            }
        });

        /*제출 버튼 결과 감시*/
        viewModel.getIsProfileValid().observe(this, isProfileValid -> {
            if(isProfileValid == Validation.NOT_VALID_ANYWAY || isProfileValid == Validation.NICKNAME_NOT_WRITTEN) {
                binding.edittextNickname.requestFocus();
                binding.errorNickname.setText("닉네임을 입력하세요.");
            }
            else if(isProfileValid == Validation.INSTRUMENT_NOT_WRITTEN) {
                binding.edittextInstrument.requestFocus();
                binding.errorInstrument.setText("악기 및 분야를 입력하세요.");
            }
            else if(isProfileValid == Validation.PROFICIENCY_NOT_WRITTEN) {
                binding.errorProficiency.setText("숙련도를 선택하세요.");
            }
            else if(isProfileValid == Validation.MEMBER_NOT_EXIST) {
                Toast.makeText(context, "내 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isProfileValid == Validation.VIDEO_URL_NOT_FORMAL){
                binding.errorVideo.setTextColor(getColor(R.color.red));
                binding.errorVideo.setText("유효하지 않은 형식입니다.");
            }
            else if(isProfileValid == Validation.VALID_ALL) {
                viewModel.clearRecord();
                for (int i = 0; i < recordParent.getChildCount(); i++) {
                    View recordChild = recordParent.getChildAt(i);
                    EditText edittextRecordName = recordChild.findViewById(R.id.signup_edittext_record_name);
                    AppCompatButton buttonRecordStart = recordChild.findViewById(R.id.button_record_date_start);
                    AppCompatButton buttonRecordEnd = recordChild.findViewById(R.id.button_record_date_end);
                    String recordName = edittextRecordName.getText().toString();
                    String startDateString = buttonRecordStart.getText().toString();
                    String endDateString = buttonRecordEnd.getText().toString();
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            .appendValue(ChronoField.YEAR, 4)
                            .appendLiteral('-')
                            .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
                            .appendLiteral('-')
                            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                            .toFormatter();
                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    if(!TimeManager.strToDate(startDateString).isEmpty()) startDate = LocalDate.parse(startDateString, formatter);
                    if(!TimeManager.strToDate(endDateString).isEmpty()) endDate = LocalDate.parse(endDateString, formatter);
                    viewModel.addRecord(recordName, startDate, endDate);
                }
                viewModel.setEmail(member.getEmail());
                viewModel.setGenres(Genre.values(), genreAdapter.getIsSelecteds());

                /*최종 확인 다이얼로그 띄우기*/
                ProfileUpdateDialog profileUpdateDialog = new ProfileUpdateDialog(context, binding.edittextNickname.getText().toString());
                profileUpdateDialog.show();
            }
            else{
                Toast.makeText(context, "내 정보를 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "다음 버튼 감시 결과를 알 수 없습니다.");
            }
        });

        /*updateProfile() 결과 감시*/
        viewModel.getIsUpdateProfileSuccess().observe(this, isUpdateProfileSuccess -> {
            Validation validation = isUpdateProfileSuccess.getValidation();
            Member member = isUpdateProfileSuccess.getData();
            if(validation == Validation.NOT_VALID_ANYWAY) {
                Toast.makeText(context, "내 정보를 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.RECORD_NOT_VALID) {
                Toast.makeText(context, "이력 시작 날짜는 종료 날짜보다 이전이어야 합니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.RECORD_NAME_NOT_WRITTEN) {
                Toast.makeText(context, "이력의 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NO_AUTHORITY) {
                binding.errorProficiency.setText("권한이 없습니다.");
            }
            else if(validation == Validation.MEMBER_NOT_EXIST) {
                Toast.makeText(context, "내 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.UPDATE_PROFILE_SUCCESS) {
                Toast.makeText(context, "내 정보를 성공적으로 수정하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(context, "내 정보를 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "다음 버튼 감시 결과를 알 수 없습니다.");
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        binding.edittextNickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorNickname.setText("");
                    binding.placeholderNickname.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderNickname.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextProfileDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorProfileDescription.setText("");
                    binding.placeholderProfileDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderProfileDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextInstrument.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorInstrument.setText("");
                    binding.placeholderInstrument.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderInstrument.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextVideo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorVideo.setText("");
                    binding.placeholderVideo.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.errorVideo.setText("");
                    binding.placeholderVideo.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
    }

    private View getRecordChild(){
        // inflate
        final Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        View recordChild = LayoutInflater.from(MyInformationUpdateActivity.this).inflate(R.layout.item_signup_record_form, recordParent, false);

        // child 세팅
        LinearLayout placeholderRecordName = recordChild.findViewById(R.id.signup_placeholder_record_name);
        EditText edittextRecordName = recordChild.findViewById(R.id.signup_edittext_record_name);
        AppCompatButton buttonRecordStart = recordChild.findViewById(R.id.button_record_date_start);
        AppCompatButton buttonRecordEnd = recordChild.findViewById(R.id.button_record_date_end);
        edittextRecordName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    placeholderRecordName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    placeholderRecordName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        buttonRecordStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MyInformationUpdateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            buttonRecordStart.setText(selectedDate);
                        }, thisYear, thisMonth, thisDay);
                datePickerDialog.show();
            }
        });
        buttonRecordEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MyInformationUpdateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            buttonRecordEnd.setText(selectedDate);
                        }, thisYear, thisMonth, thisDay);
                datePickerDialog.show();
            }
        });

        return recordChild;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onDialogYesClicked(){
        viewModel.updateProfile(context, id, member.getUsername());
    }
}
