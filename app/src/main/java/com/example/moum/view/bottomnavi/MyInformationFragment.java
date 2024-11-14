package com.example.moum.view.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.databinding.FragmentHomeBinding;
import com.example.moum.databinding.FragmentMyInformationBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.bottomnavi.MyInformationModel;
import com.example.moum.viewmodel.home.HomeViewModel;

public class MyInformationFragment extends Fragment {
    private FragmentMyInformationBinding binding;
    private SharedPreferenceManager sharedPreferenceManager;
    private MyInformationModel viewModel;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MyInformationModel.class);
        binding = FragmentMyInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext();

        /*SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));

        /*로그아웃 버튼*/
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logout();
            }
        });

        /*로그아웃 결과 감시*/
        viewModel.getIsLogoutSuccess().observe(getViewLifecycleOwner(), isLoginSuccess -> {
            Validation validation = isLoginSuccess.getValidation();
            if(validation == Validation.LOGOUT_SUCCESS){
                Toast.makeText(context, "성공적으로 로그아웃하였습니다.", Toast.LENGTH_SHORT).show();

                sharedPreferenceManager.removeCache(getString(R.string.user_id_key));
                sharedPreferenceManager.removeCache(getString(R.string.user_username_key));
                sharedPreferenceManager.removeCache(getString(R.string.user_access_token_key));
                sharedPreferenceManager.removeCache(getString(R.string.user_refresh_token_key));

                Intent intent = new Intent(requireActivity(), InitialActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
            else if(validation == Validation.LOGOUT_ALREADY){
                Toast.makeText(context, "이미 로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "로그아웃할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}