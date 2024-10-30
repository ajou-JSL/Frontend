package com.example.moum.view;

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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.databinding.ActivitySignupProfileBinding;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.SignupViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        String name = prevIntent.getStringExtra("name");
        String password = prevIntent.getStringExtra("password");
        String email = prevIntent.getStringExtra("email");
        signupViewModel.loadBasic(name, password, email);

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
                View recordChild = LayoutInflater.from(SignupProfileActivity.this).inflate(R.layout.signup_record_form, recordParent, false);

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
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDate = LocalDate.parse(startDateString, formatter);
                    LocalDate endDate = LocalDate.parse(endDateString, formatter);

                    signupViewModel.addRecord(recordName, startDate, endDate);
                }
                signupViewModel.signup(context);
            }
            else{
                Log.e(TAG, "다음 버튼 감시 결과를 알 수 없습니다.");
            }
        });

        /*signup 결과 감시*/
        signupViewModel.getIsSignupSuccess().observe(this, isSignupSuccess -> {

            if(isSignupSuccess == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isSignupSuccess == Validation.VALID_ALL) {

                /*다음 Acitivity로 이동*/
                Intent nextIntent = new Intent(SignupProfileActivity.this, InitialActivity.class);
                startActivity(nextIntent);
            }
            else{
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
    }

    private File convertUriToFile(Uri uri){

        File file = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                // 임시 파일 생성
                file = new File(context.getCacheDir(), "temp_image.jpg");
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
