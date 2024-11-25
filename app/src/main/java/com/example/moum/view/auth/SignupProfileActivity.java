package com.example.moum.view.auth;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.databinding.ActivitySignupProfileBinding;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.adapter.GenreAdapter;
import com.example.moum.view.dialog.SignupLoadingDialog;
import com.example.moum.view.moum.adapter.MoumCreateImageAdapter;
import com.example.moum.viewmodel.auth.SignupViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class SignupProfileActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    ActivitySignupProfileBinding binding;
    private Context context;
    public String TAG = getClass().toString();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignupProfileBinding.inflate(getLayoutInflater());
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        binding.setViewModel(signupViewModel);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*이전 액티비티에서 전달된 데이터 받기*/
        Intent prevIntent = getIntent();
        String memberId = prevIntent.getStringExtra("memberId");
        String password = prevIntent.getStringExtra("password");
        String email = prevIntent.getStringExtra("email");
        String emailCode = prevIntent.getStringExtra("emailCode");
        signupViewModel.setBasic(memberId, password, email, emailCode);

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
                signupViewModel.setProfileImage(uri);
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
        signupViewModel.getProfileImage().observe(this, uri -> {
            Log.e(TAG, "Uri: " + uri.toString());
            Glide.with(this).load(uri).into(binding.imageviewSignupProfile);
        });

        /*proficiency 스피너 Adapter 연결*/
        Spinner proficiencySpinner = binding.spinnerProficiency;
        String[] proficiencyList = getResources().getStringArray(R.array.proficiency_list);
        ArrayAdapter<String> proficiencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, proficiencyList);
        proficiencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proficiencySpinner.setAdapter(proficiencyAdapter);

        signupViewModel.setProficiency(proficiencyList[0]);
        proficiencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signupViewModel.setProficiency(proficiencyList[position]);
                binding.signupErrorProficiency.setText("");
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

        signupViewModel.setAddress(addressList[0]);
        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signupViewModel.setAddress(addressList[position]);
                binding.signupErrorAddress.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
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

        /*이력 레이아웃 동적 생성*/
        final Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        LinearLayout recordParent = binding.signupRecordParent;
        binding.buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // inflate
                View recordChild = LayoutInflater.from(SignupProfileActivity.this).inflate(R.layout.item_signup_record_form, recordParent, false);

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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(SignupProfileActivity.this,
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(SignupProfileActivity.this,
                                (mView, mYear, mMonth, mDay) -> {
                                    String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                                    buttonRecordEnd.setText(selectedDate);
                                }, thisYear, thisMonth, thisDay);
                        datePickerDialog.show();
                    }
                });

                // parent에 child view 추가
                recordParent.addView(recordChild);
            }
        });

        /*제출 버튼 클릭 시 이벤트*/
        binding.buttonSignupSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.validCheckProfile();
            }
        });

        /*제출 버튼 결과 감시*/
        SignupLoadingDialog signupLoadingDialog = new SignupLoadingDialog(context);
        signupViewModel.getIsProfileValid().observe(this, isProfileValid -> {
            if(isProfileValid == Validation.NOT_VALID_ANYWAY || isProfileValid == Validation.NICKNAME_NOT_WRITTEN) {
                binding.signupEdittextNickname.requestFocus();
                binding.signupErrorNickname.setText("닉네임을 입력하세요.");
            }
            else if(isProfileValid == Validation.INSTRUMENT_NOT_WRITTEN) {
                binding.signupEdittextInstrument.requestFocus();
                binding.signupErrorInstrument.setText("악기 및 분야를 입력하세요.");
            }
            else if(isProfileValid == Validation.PROFICIENCY_NOT_WRITTEN) {
                binding.signupErrorProficiency.setText("숙련도를 선택하세요.");
            }
            else if(isProfileValid == Validation.VIDEO_URL_NOT_FORMAL){
                binding.errorVideo.setTextColor(getColor(R.color.red));
                binding.errorVideo.setText("유효하지 않은 형식입니다.");
            }
            else if(isProfileValid == Validation.BASIC_SIGNUP_NOT_TRIED) {
                Toast.makeText(context, "이전 단계가 정상적으로 완료되지 않았습니다. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isProfileValid == Validation.VALID_ALL) {
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
                    if(!startDateString.isEmpty() && !startDateString.equals("시작 날짜")) startDate = LocalDate.parse(startDateString, formatter);
                    if(!endDateString.isEmpty() && !startDateString.equals("종료 날짜")) endDate = LocalDate.parse(endDateString, formatter);

                    signupViewModel.addRecord(recordName, startDate, endDate);
                    signupViewModel.setGenres(Genre.values(), genreAdapter.getIsSelecteds());
                }

                signupLoadingDialog.show();

                signupViewModel.signup(context);
            }
            else{
                Toast.makeText(context, "알수 없는 감시 결과(Code: " + isProfileValid.toString() + ")", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "다음 버튼 감시 결과를 알 수 없습니다.");
            }
        });

        /*signup 결과 감시*/
        signupViewModel.getIsSignupSuccess().observe(this, isSignupSuccess -> {
            signupLoadingDialog.dismiss();
            if(isSignupSuccess == Validation.INVALID_TYPE_VALUE){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isSignupSuccess == Validation.EMAIL_AUTH_ALREADY){
                Toast.makeText(context, "이미 가입 완료된 이메일입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isSignupSuccess == Validation.EMAIL_AUTH_FAILED){
                Toast.makeText(context, "이메일 인증이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isSignupSuccess == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isSignupSuccess == Validation.SIGNUP_SUCCESS) {
                /*다음 Acitivity로 이동*/
                Toast.makeText(context, "회원가입이 완료되었습니다. 환영합니다:)", Toast.LENGTH_SHORT).show();
                Intent nextIntent = new Intent(SignupProfileActivity.this, InitialActivity.class);
                startActivity(nextIntent);
            }
            else{
                Toast.makeText(context, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "signup 감시 결과를 알 수 없습니다.");
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        binding.signupEdittextNickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorNickname.setText("");
                    binding.signupPlaceholderNickname.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.signupPlaceholderNickname.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextProfileDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorProfileDescription.setText("");
                    binding.signupPlaceholderProfileDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.signupPlaceholderProfileDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextInstrument.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorInstrument.setText("");
                    binding.signupPlaceholderInstrument.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.signupPlaceholderInstrument.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
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

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.enter_left, R.anim.none);
    }
}
