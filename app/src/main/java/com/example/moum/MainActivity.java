package com.example.moum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // BottomNavigationView 클릭 시 중복 클릭 방지
            bottomNavigationView.setOnItemSelectedListener(item -> {
                item.setEnabled(false);  // 클릭 비활성화
                new Handler().postDelayed(() -> item.setEnabled(true), 500);  // 500ms 후 다시 클릭 가능

                return NavigationUI.onNavDestinationSelected(item, navController);
            });
        } else {
            // navHostFragment가 null인 경우에 대한 처리
            Log.e("MainActivity", "NavHostFragment not found");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*activity로 리턴 시, result와 함께 리턴된다면 fragment_index 값에 따라 해당 프래그먼트로 즉시 이동*/
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            int fragmentIndex = data.getIntExtra("fragment_index", -1);
            if (fragmentIndex != -1) {
                bottomNavigationView.setSelectedItemId(R.id.menu_moumtalk);
            }
        }
    }
}