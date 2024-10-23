package com.example.moum.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.databinding.ActivitySignupProfileBinding;
import com.example.moum.viewmodel.SignupViewModel;

import java.util.Calendar;

public class SignupProfileActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    ActivitySignupProfileBinding binding;
    private Context context;
    public String TAG = getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignupProfileBinding.inflate(getLayoutInflater());
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

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
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(SignupProfileActivity.this,
                                (mView, mYear, mMonth, mDay) -> {
                                    String selectedDate = mYear + "년 " + (mMonth + 1) + "월 " + mDay + "일";
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
                                    String selectedDate = mYear + "년 " + (mMonth + 1) + "월 " + mDay + "일";
                                    buttonRecordEnd.setText(selectedDate);
                                }, thisYear, thisMonth, thisDay);
                        datePickerDialog.show();
                    }
                });

                // parent에 child view 추가
                recordParent.addView(recordChild);
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
}
