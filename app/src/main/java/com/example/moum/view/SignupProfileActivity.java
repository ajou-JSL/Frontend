package com.example.moum.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.databinding.ActivitySignupProfileBinding;
import com.example.moum.viewmodel.SignupViewModel;

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
        Spinner addressSpinner = binding.spinnerProficiency;
        /**
         * TO-DO: address list를 주소 API에서 끌어오기
         */
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


        /*각 placeholder 포커스 시 이벤트*/
        binding.signupEdittextNickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorNickname.setText("");
                    binding.placeholderNickname.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderNickname.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextProfileDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorProfileDescription.setText("");
                    binding.placeholderProfileDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderProfileDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextInstrument.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorInstrument.setText("");
                    binding.placeholderInstrument.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderInstrument.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
    }
}
