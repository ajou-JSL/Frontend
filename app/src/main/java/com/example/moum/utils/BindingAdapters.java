package com.example.moum.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class BindingAdapters {

    // EditText에 Integer 값을 설정하는 BindingAdapter
    @BindingAdapter("android:text")
    public static void setInteger(EditText view, Integer value) {
        if (value != null && !view.getText().toString().equals(String.valueOf(value))) {
            view.setText(String.valueOf(value));
        }
    }

    // EditText에서 Integer 값을 가져오는 InverseBindingAdapter
    @InverseBindingAdapter(attribute = "android:text")
    public static Integer getInteger(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0; // 변환에 실패하면 기본값 0 반환
        }
    }

    // 양방향 바인딩을 위한 Listener 설정
    @BindingAdapter("android:textAttrChanged")
    public static void setIntegerListener(EditText view, InverseBindingListener listener) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onChange();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
