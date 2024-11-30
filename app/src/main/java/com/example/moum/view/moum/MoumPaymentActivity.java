package com.example.moum.view.moum;

import static android.util.Log.e;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Settlement;
import com.example.moum.databinding.ActivityMoumManageBinding;
import com.example.moum.databinding.ActivityMoumPaymentBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.SettlementDeleteDialog;
import com.example.moum.view.moum.adapter.MoumPracticeroomAdapter;
import com.example.moum.view.moum.adapter.MoumSettlementAdapter;
import com.example.moum.viewmodel.moum.MoumManageViewModel;
import com.example.moum.viewmodel.moum.MoumPaymentViewModel;

import java.util.ArrayList;
import java.util.List;

public class MoumPaymentActivity extends AppCompatActivity {
    private MoumPaymentViewModel viewModel;
    private ActivityMoumPaymentBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private Integer moumId;
    private Integer teamId;
    private final ArrayList<Settlement> settlements = new ArrayList<>();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumPaymentBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumPaymentViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        moumId = prevIntent.getIntExtra("moumId", -1);
        teamId = prevIntent.getIntExtra("teamId", -1);
        if (teamId == -1 || moumId == -1) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
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

        /*정산 내역 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerPayment;
        MoumSettlementAdapter moumSettlementAdapter = new MoumSettlementAdapter();
        moumSettlementAdapter.setSettlements(settlements, context);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        recyclerView.setAdapter(moumSettlementAdapter);


        /*정산 모음톡 생성하기 버튼 클릭 이벤트*/
        binding.buttonMakeMoumtalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoumPaymentChatroomActivity.class);
                intent.putExtra("teamId", teamId);
                intent.putExtra("moumId", moumId);
                startActivity(intent);
            }
        });

        /*정산 추가하기 버튼 이벤트*/
        binding.buttonAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoumPaymentAddActivity.class);
                intent.putExtra("teamId", teamId);
                intent.putExtra("moumId", moumId);
                startActivity(intent);
            }
        });

        /*정산 내역 불러오기*/
        viewModel.loadSettlement(moumId);

        /*정산 내역 불러오기 결과 감시*/
        viewModel.getIsLoadSettlementsSuccess().observe(this, isLoadSettlementsSuccess -> {
            Validation validation = isLoadSettlementsSuccess.getValidation();
            List<Settlement> loadedSettlements = isLoadSettlementsSuccess.getData();
            if(validation == Validation.SETTLEMENT_GET_SUCCESS && !loadedSettlements.isEmpty()){
                settlements.clear();
                settlements.addAll(loadedSettlements);
                moumSettlementAdapter.notifyItemInserted(settlements.size()-1);
                binding.textviewEmpty.setVisibility(View.GONE);
                int sum = 0;
                for(Settlement settlement : loadedSettlements){
                    if(settlement.getFee() != null)
                        sum += settlement.getFee();
                }
                binding.textviewSum.setText(String.format("%d", sum));
            }
            else if(validation == Validation.SETTLEMENT_GET_SUCCESS){
                settlements.clear();
                moumSettlementAdapter.notifyDataSetChanged();
                binding.textviewEmpty.setVisibility(View.VISIBLE);
                binding.textviewSum.setText("정산할 내역이 없습니다.");
            }
            else if(validation == Validation.MOUM_NOT_FOUND) {
                Toast.makeText(context, "존재하지 않는 모음입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "정산 내역을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*정산 내역 삭제 결과 감시*/
        viewModel.getIsDeleteSettlementSuccess().observe(this, isDeleteSettlementSuccess -> {
            Validation validation = isDeleteSettlementSuccess.getValidation();
            Settlement settlement = isDeleteSettlementSuccess.getData();
            if(validation == Validation.SETTLEMENT_DELETE_SUCCESS){
                Toast.makeText(context, "정산 내역을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                refreshView();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT) {
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NO_AUTHORITY) {
                Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.MOUM_NOT_FOUND) {
                Toast.makeText(context, "존재하지 않는 모음입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "정산 내역을 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        // swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadSettlement(moumId);
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void onSettlementDeleteClicked(Settlement settlement){
        SettlementDeleteDialog settlementDeleteDialog = new SettlementDeleteDialog(context, settlement);
        settlementDeleteDialog.show();
    }

    public void onSettlementDeleteDialogYesClicked(Settlement settlement){
        viewModel.deleteSettlement(moumId, settlement.getSettlementId());
    }

    public void refreshView(){
        viewModel.loadSettlement(moumId);
    }
}
