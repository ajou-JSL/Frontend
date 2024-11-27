package com.example.moum.view.moum;

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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivitySignupProfileBinding;
import com.example.moum.databinding.ActivityTeamCreateBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.auth.SignupProfileActivity;
import com.example.moum.view.dialog.TeamCreateDialog;
import com.example.moum.viewmodel.auth.SignupViewModel;
import com.example.moum.viewmodel.moum.TeamCreateViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class TeamCreateActivity extends AppCompatActivity {
    private TeamCreateViewModel viewModel;
    private ActivityTeamCreateBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamCreateBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(TeamCreateViewModel.class);
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
                viewModel.setProfileImage(uri);
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
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                    .error(R.drawable.background_more_rounded_gray_size_fit))
                    .load(uri).into(binding.imageviewTeamProfile);
            binding.imageviewTeamProfile.setClipToOutline(true);
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
                binding.teamErrorAddress.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
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
                binding.errorTeamGenre.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*이력 레이아웃 동적 생성*/
        final Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        LinearLayout recordParent = binding.teamRecordParent;
        binding.buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // inflate
                View recordChild = LayoutInflater.from(TeamCreateActivity.this).inflate(R.layout.item_signup_record_form, recordParent, false);

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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TeamCreateActivity.this,
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TeamCreateActivity.this,
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
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                viewModel.clearRecords();
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
                    if(!startDateString.isEmpty() && !startDateString.equals("시작 날짜"))
                        startDate = LocalDate.parse(startDateString, formatter);
                    if(!endDateString.isEmpty() && !startDateString.equals("종료 날짜"))
                        endDate = LocalDate.parse(endDateString, formatter);

                    viewModel.addRecord(recordName, startDate, endDate);
                }
                viewModel.validCheck();
            }
        });

        /*제출 버튼 클릭 결과 감시*/
        viewModel.getIsValidCheckSuccess().observe(this, isValidCheckSuccess -> {
            if(isValidCheckSuccess == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isValidCheckSuccess == Validation.TEAM_NAME_NOT_WRITTEN){
                binding.errorTeamName.setText("단체 이름을 입력하세요.");
                binding.edittextTeamName.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.TEAM_GENRE_NOT_WRITTEN){
                binding.errorTeamGenre.setText("단체의 분야를 선택하세요.");
                binding.spinnerGenre.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.VIDEO_URL_NOT_FORMAL){
                binding.errorVideo.setText("형식이 올바르지 않습니다.");
                binding.errorVideo.setTextColor(getColor(R.color.red));
                binding.edittextVideo.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.VALID_ALL){
                // valid check 유효하다면, 최종 다이얼로그 띄우기
                TeamCreateDialog teamCreateDialog = new TeamCreateDialog(this, binding.edittextTeamName.getText().toString());
                teamCreateDialog.show();
            }
            else{
                Toast.makeText(context, "단체 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*createTeam() 결과 감시*/
        viewModel.getIsCreateTeamSuccess().observe(this, isCreateTeamSuccess -> {
            Validation validation = isCreateTeamSuccess.getValidation();
            Team createdTeam = isCreateTeamSuccess.getData();
            if(validation == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.CREATE_TEAM_SUCCESS){
                Toast.makeText(context, "단체 생성에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(context, "단체 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        binding.edittextTeamName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorTeamName.setText("");
                    binding.placeholderTeamName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderTeamName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextTeamDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorTeamDescription.setText("");
                    binding.placeholderTeamDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderTeamDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.spinnerAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.teamErrorAddress.setText("");
                }else{
                }
            }
        });
        binding.spinnerGenre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorTeamGenre.setText("");
                }else{
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

    public void onDialogYesClicked(){
        /*다이얼로그에서 Yes 버튼 클릭 시, createTeam() 호출*/
        viewModel.createTeam(id, context);
    }
}
