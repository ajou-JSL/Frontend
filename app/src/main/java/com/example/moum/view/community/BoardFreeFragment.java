package com.example.moum.view.community;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.moum.R;
import com.example.moum.databinding.FragmentBoardFreeBinding;
import com.example.moum.viewmodel.community.BoardfreeViewModel;


public class BoardFreeFragment extends Fragment {

    private FragmentBoardFreeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BoardfreeViewModel BoardfreeViewModel = new ViewModelProvider(this).get(BoardfreeViewModel.class);

        binding = FragmentBoardFreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initSpinner();

        return root;
    }

    private void initSpinner() {
        // 스피너 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.community_spinner1_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySpinner1.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    Toast.makeText(requireContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}