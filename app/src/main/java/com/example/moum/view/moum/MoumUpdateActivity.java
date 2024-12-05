package com.example.moum.view.moum;

import static android.util.Log.e;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Music;
import com.example.moum.databinding.ActivityMoumCreateBinding;
import com.example.moum.databinding.ActivityMoumUpdateBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.LoadingDialog;
import com.example.moum.view.dialog.MoumCreateDialog;
import com.example.moum.view.dialog.MoumUpdateDialog;
import com.example.moum.view.moum.adapter.MoumCreateImageAdapter;
import com.example.moum.view.moum.adapter.MoumUpdateImageAdapter;
import com.example.moum.viewmodel.moum.MoumCreateViewModel;
import com.example.moum.viewmodel.moum.MoumUpdateViewModel;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;

public class MoumUpdateActivity extends AppCompatActivity {
    private MoumUpdateViewModel viewModel;
    private ActivityMoumUpdateBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private Integer moumId;
    private Integer teamId;
    private Integer leaderId;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    private ArrayList<Uri> uris = new ArrayList<>();
    private LoadingDialog loadingDialog;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumUpdateBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumUpdateViewModel.class);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        loadingDialog = new LoadingDialog(context);

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        moumId = prevIntent.getIntExtra("moumId", -1);
        teamId = prevIntent.getIntExtra("teamId", -1);
        leaderId = prevIntent.getIntExtra("leaderId", -1);
        if(moumId == -1 || teamId == -1 || leaderId == -1){
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

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

        /*이미지 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerMoumImages;
        MoumUpdateImageAdapter moumUpdateImageAdapter = new MoumUpdateImageAdapter();
        moumUpdateImageAdapter.setUris(uris, context);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(moumUpdateImageAdapter);
        uris.add(null); //for image selector
        moumUpdateImageAdapter.notifyItemInserted(uris.size()-1);

        /*Photo picker 선택 후 콜백*/
        pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    Log.d(TAG,  "Number of items selected: " + uris.size());
                    viewModel.setProfileImages(uris);
                } else {
                    Log.d(TAG, "No media selected");
                }
        });

        /*사진 업로드 감시*/
        viewModel.getProfileImages().observe(this, addedUris -> {
            for(Uri uri : addedUris){
                Log.e(TAG, "Uri: " + uri.toString());
            }
            uris.clear();
            uris.addAll(addedUris);
            uris.add(null); //for image selector
            moumUpdateImageAdapter.notifyItemInserted(uris.size()-1);
            recyclerView.scrollToPosition(uris.size()-1);
            viewModel.setIsProfileUpdated(true);
        });

        /*날짜 선택 이벤트*/
        final Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        binding.buttonMoumDateStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MoumUpdateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            binding.buttonMoumDateStart.setText(selectedDate);
                        }, thisYear, thisMonth, thisDay);
                datePickerDialog.show();
            }
        });
        binding.buttonMoumDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MoumUpdateActivity.this,
                        (mView, mYear, mMonth, mDay) -> {
                            String selectedDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
                            binding.buttonMoumDateEnd.setText(selectedDate);
                        }, thisYear, thisMonth, thisDay);
                datePickerDialog.show();
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
                binding.errorMoumGenre.setText("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });


        /*song 레이아웃 동적 생성*/
        LinearLayout songParent = binding.moumMusicParent;
        binding.buttonAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSongLayout(songParent);
            }
        });

        /*모음 정보 불러오기*/
        viewModel.loadMoum(moumId);

        /*모음 정보 불러오기 결과 감시 후 폼 채우기*/
        viewModel.getIsLoadMoumSuccess().observe(this, isLoadMoumSuccess -> {
            Validation validation = isLoadMoumSuccess.getValidation();
            Moum loadedMoum = isLoadMoumSuccess.getData();
            if(validation == Validation.GET_MOUM_SUCCESS){
                if(loadedMoum.getMoumName() != null) binding.edittextMoumName.setText(loadedMoum.getMoumName());
                if(loadedMoum.getMoumDescription() != null) binding.edittextMoumDescription.setText(loadedMoum.getMoumDescription());
                if(loadedMoum.getPerformLocation() != null) binding.edittextMoumPlace.setText(loadedMoum.getPerformLocation());
                if(loadedMoum.getStartDate() != null && !loadedMoum.getStartDate().isEmpty()) binding.buttonMoumDateStart.setText(loadedMoum.getStartDate());
                else binding.buttonMoumDateStart.setText("시작 날짜");
                if(loadedMoum.getEndDate() != null && !loadedMoum.getEndDate().isEmpty()) binding.buttonMoumDateEnd.setText(loadedMoum.getEndDate());
                else binding.buttonMoumDateEnd.setText("종료 날짜");
                if(loadedMoum.getPrice() != null) binding.edittextMoumPrice.setText(String.format("%d", loadedMoum.getPrice()));
                if(loadedMoum.getGenre() != null) genreSpinner.setSelection(loadedMoum.getGenre().getValue());
                if(loadedMoum.getImageUrls() != null && !loadedMoum.getImageUrls().isEmpty()){
                    ArrayList<Uri> uriArrayList = new ArrayList<>();
                    for(String url : loadedMoum.getImageUrls())
                        uriArrayList.add(Uri.parse(url));
                    uris.clear();
                    uris.addAll(uriArrayList);
                    uris.add(null); //for image selector
                    moumUpdateImageAdapter.notifyItemInserted(uris.size()-1);
                    recyclerView.scrollToPosition(uris.size()-1);
                    viewModel.setProfileImages(loadedMoum.getImageUrls());
                }
                if(loadedMoum.getMusic() != null &&!loadedMoum.getMusic().isEmpty()){
                    for(Music music : loadedMoum.getMusic()){
                        View songChild = addSongLayout(songParent);
                        EditText edittextSingName = songChild.findViewById(R.id.edittext_music_name);
                        EditText edittextArtistName = songChild.findViewById(R.id.edittext_artist_name);
                        edittextSingName.setText(music.getMusicName());
                        edittextArtistName.setText(music.getArtistName());
                    }
                }
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT){
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "모음 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*제출 버튼 클릭 시 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String startDateString = binding.buttonMoumDateStart.getText().toString();
                String endDateString = binding.buttonMoumDateEnd.getText().toString();
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendValue(ChronoField.YEAR, 4)
                        .appendLiteral('-')
                        .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
                        .appendLiteral('-')
                        .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                        .toFormatter();
                LocalDate startDate = null;
                LocalDate endDate = null;
                if(!startDateString.equals("시작 날짜")) startDate = LocalDate.parse(startDateString, formatter);
                if(!endDateString.equals("종료 날짜")) endDate = LocalDate.parse(endDateString, formatter);
                viewModel.setDates(startDate, endDate);

                for (int i = 0; i < songParent.getChildCount(); i++) {
                    View songChild = songParent.getChildAt(i);
                    EditText editTextMusicName = songChild.findViewById(R.id.edittext_music_name);
                    EditText editTextArtistName = songChild.findViewById(R.id.edittext_artist_name);
                    String musicName = editTextMusicName.getText().toString();
                    String artistName = editTextArtistName.getText().toString();
                    viewModel.addMusic(musicName, artistName);
                }
                viewModel.validCheck();
            }
        });

        /*제출 버튼 클릭 결과 감시*/
        viewModel.getIsValidCheckSuccess().observe(this, isValidCheckSuccess -> {
            if(isValidCheckSuccess == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isValidCheckSuccess == Validation.MOUM_NAME_NOT_WRITTEN){
                binding.errorMoumName.setText("모음 이름을 입력하세요.");
                binding.errorMoumName.requestFocus();
            }
            else if(isValidCheckSuccess == Validation.DATE_NOT_VALID){
                binding.errorMoumDate.setText("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
            }
            else if(isValidCheckSuccess == Validation.MUSIC_NAME_NOT_WRITTEN){
                Toast.makeText(context, "곡 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isValidCheckSuccess == Validation.ARTIST_NAME_NOT_WRITTEN){
                Toast.makeText(context, "아티스트 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isValidCheckSuccess == Validation.VALID_ALL){
                // valid check 유효하다면, 최종 다이얼로그 띄우기
                MoumUpdateDialog moumUpdateDialog = new MoumUpdateDialog(this, binding.edittextMoumName.getText().toString());
                moumUpdateDialog.show();
            }
            else{
                Toast.makeText(context, "모음 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*createUpdate() 결과 감시*/
        viewModel.getIsUpdateMoumSuccess().observe(this, isUpdateMoumSuccess -> {
            loadingDialog.dismiss();
            Validation validation = isUpdateMoumSuccess.getValidation();
            Moum updateMoum = isUpdateMoumSuccess.getData();
            if(validation == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "잘못 입력된 값이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.MUST_JOIN_TEAM_FIRST){
                Toast.makeText(context, "팀에 먼저 가입해야 합니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.TEAM_NOT_FOUND){
                Toast.makeText(context, "팀을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.UPDATE_MOUM_SUCCESS){
                Toast.makeText(context, "모음 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(context, "모음 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        binding.edittextMoumName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumName.setText("");
                    binding.placeholderMoumName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextMoumDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumDescription.setText("");
                    binding.placeholderMoumDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextMoumPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumPlace.setText("");
                    binding.placeholderMoumPlace.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumPlace.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextMoumPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumPrice.setText("");
                    binding.placeholderMoumPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderMoumPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.buttonMoumDateStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumDate.setText("");
                }else{
                }
            }
        });
        binding.buttonMoumDateEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumDate.setText("");
                }else{
                }
            }
        });
        binding.spinnerGenre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.errorMoumGenre.setText("");
                }else{
                }
            }
        });

    }

    public void onDialogYesClicked(){
        /*다이얼로그에서 Yes 버튼 클릭 시, updateMoum() 호출*/
        loadingDialog.show();
        viewModel.updateMoum(moumId, teamId, leaderId, context);
    }

    public void onImageSelectorClicked(){
        /*이미지 선택 버튼 클릭 시, Photo picker 열기*/
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    public View addSongLayout(LinearLayout songParent){
        // inflate
        View songChild = LayoutInflater.from(MoumUpdateActivity.this).inflate(R.layout.item_moum_music_form, songParent, false);

        // child 세팅
        LinearLayout placeholderMusicName = songChild.findViewById(R.id.placeholder_music_name);
        EditText editTextMusicName = songChild.findViewById(R.id.edittext_music_name);
        TextView errorMusicName = songChild.findViewById(R.id.error_music_name);
        LinearLayout placeholderArtistName = songChild.findViewById(R.id.placeholder_artist_name);
        EditText editTextArtistName = songChild.findViewById(R.id.edittext_artist_name);
        TextView errorArtistName = songChild.findViewById(R.id.error_artist_name);
        ImageView buttonDelete = songChild.findViewById(R.id.button_delete);

        editTextMusicName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    placeholderMusicName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    placeholderMusicName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        editTextArtistName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    placeholderArtistName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    placeholderArtistName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songParent.removeView(songChild);
            }
        });

        // parent에 child view 추가
        songParent.addView(songChild);

        return songChild;
    }
}
