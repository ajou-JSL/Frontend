package com.example.moum.view.moum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.dto.SearchPracticeroomArgs;
import com.example.moum.databinding.FragmentPracticeroomFilterBinding;
import com.example.moum.view.moum.adapter.FilterExistOrNotAdapter;
import com.example.moum.view.moum.adapter.FilterOrderByAdapter;
import com.example.moum.view.moum.adapter.FilterSortByAdapter;
import com.example.moum.view.moum.adapter.FilterTypeAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class PracticeroomFilterFragment extends BottomSheetDialogFragment {
    private FragmentPracticeroomFilterBinding binding;
    private Context context;
    private final String TAG = getClass().toString();
    private ArrayList<String> sortBys = new ArrayList<>();
    private ArrayList<String> orderBys = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    private ArrayList<String> pianos = new ArrayList<>();
    private ArrayList<String> amps = new ArrayList<>();
    private ArrayList<String> speakers = new ArrayList<>();
    private ArrayList<String> mics = new ArrayList<>();
    private ArrayList<String> drums = new ArrayList<>();
    private SearchPracticeroomArgs args = new SearchPracticeroomArgs();

    public PracticeroomFilterFragment(Context context){
        this.context = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPracticeroomFilterBinding.inflate(inflater,container, false);
        context = requireContext();
        View view = binding.getRoot();
        view.setBackground(context.getDrawable(R.drawable.background_top_rounded_white));

        /*이전에 저장된 필터링 값 가져오기*/
        Bundle bundle = getArguments();
        if(bundle == null) dismiss();
//        args.setSortBy(bundle.getString("sortBy", "distance"));
//        args.setOrderBy(bundle.getString("orderBy", "asc"));
//        args.setType(bundle.getInt("type"));
//        args.setMinPrice(bundle.getInt("minPrice"));
//        args.setMaxPrice(bundle.getInt("maxPrice"));
//        args.setMinCapacity(bundle.getInt("minCapacity"));
//        args.setMaxCapacity(bundle.getInt("maxCapacity"));
//        args.setMinStand(bundle.getInt("minStand"));
//        args.setMaxStand(bundle.getInt("maxStand"));
//        args.setHasPiano(bundle.getBoolean("hasPiano"));
//        args.setHasAmp(bundle.getBoolean("hasAmp"));
//        args.setHasSpeaker(bundle.getBoolean("hasSpeaker"));
//        args.setHasMic(bundle.getBoolean("hasMic"));
//        args.setHasDrums(bundle.getBoolean("hasDrums"));

        /*x 버튼 클릭 이벤트*/
        binding.buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        /*sortBy 리사이클러뷰 설정*/
        RecyclerView recyclerSortBy = binding.recyclerSortBy;
        FlexboxLayoutManager sortFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        sortFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        sortFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        sortFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterSortByAdapter sortByAdapter = new FilterSortByAdapter();
        sortByAdapter.setSortBys(sortBys, context);
        recyclerSortBy.setLayoutManager(sortFlexboxLayoutManager);
        recyclerSortBy.setAdapter(sortByAdapter);
        List<String> sortByList = List.of("distance", "price", "capacity", "stand");
        sortBys.addAll(sortByList);
        sortByAdapter.notifyItemInserted(sortByList.size()-1);
        new Handler().postDelayed(() -> {
            sortByAdapter.setIsSelected(0, true);
            sortByAdapter.notifyItemChanged(0);
        }, 100);

        /*orderBy 리사이클러뷰 설정*/
        RecyclerView recyclerOrderBy = binding.recyclerOrderBy;
        FlexboxLayoutManager orderFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        orderFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        orderFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        orderFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterOrderByAdapter orderByAdapter = new FilterOrderByAdapter();
        orderByAdapter.setOrderBys(orderBys, context);
        recyclerOrderBy.setLayoutManager(orderFlexboxLayoutManager);
        recyclerOrderBy.setAdapter(orderByAdapter);
        List<String> orderByList = List.of("asc", "desc");
        orderBys.addAll(orderByList);
        orderByAdapter.notifyItemInserted(orderBys.size()-1);
        new Handler().postDelayed(() -> {
            orderByAdapter.setIsSelected(0, true);
            orderByAdapter.notifyItemChanged(0);
        }, 100);

        /*type 리사이클러뷰 설정*/
        RecyclerView recyclerType = binding.recyclerType;
        FlexboxLayoutManager typeFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        typeFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        typeFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        typeFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterTypeAdapter filterTypeAdapter = new FilterTypeAdapter();
        filterTypeAdapter.setTypes(types, context);
        recyclerType.setLayoutManager(typeFlexboxLayoutManager);
        recyclerType.setAdapter(filterTypeAdapter);
        List<String> typeList = List.of("상관없음", "클래식", "밴드");
        types.addAll(typeList);
        filterTypeAdapter.notifyItemInserted(types.size()-1);
        new Handler().postDelayed(() -> {
            filterTypeAdapter.setIsSelected(0, true);
            filterTypeAdapter.notifyItemChanged(0);
        }, 100);


        /*피아노 ~ 드럼 유무 리사이클러뷰 설정*/
        RecyclerView recyclerPiano = binding.recyclerPiano;
        FlexboxLayoutManager piaonFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        piaonFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        piaonFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        piaonFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterExistOrNotAdapter pianoAdapter = new FilterExistOrNotAdapter();
        pianoAdapter.setItems(pianos, context);
        recyclerPiano.setLayoutManager(piaonFlexboxLayoutManager);
        recyclerPiano.setAdapter(pianoAdapter);
        List<String> pianoList = List.of("don't-care", "exist", "not-exist");
        pianos.addAll(pianoList);
        pianoAdapter.notifyItemInserted(pianos.size()-1);
        new Handler().postDelayed(() -> {
            pianoAdapter.setIsSelected(0, true);
            pianoAdapter.notifyItemChanged(0);
        }, 100);

        RecyclerView recyclerAmp = binding.recyclerAmp;
        FlexboxLayoutManager ampFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        ampFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        ampFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        ampFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterExistOrNotAdapter ampAdapter = new FilterExistOrNotAdapter();
        ampAdapter.setItems(amps, context);
        recyclerAmp.setLayoutManager(ampFlexboxLayoutManager);
        recyclerAmp.setAdapter(ampAdapter);
        List<String> ampList = List.of("don't-care", "exist", "not-exist");
        amps.addAll(ampList);
        ampAdapter.notifyItemInserted(amps.size()-1);
        new Handler().postDelayed(() -> {
            ampAdapter.setIsSelected(0, true);
            ampAdapter.notifyItemChanged(0);
        }, 100);

        RecyclerView recyclerSpeaker = binding.recyclerSpeaker;
        FlexboxLayoutManager speakerFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        speakerFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        speakerFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        speakerFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterExistOrNotAdapter speakerAdapter = new FilterExistOrNotAdapter();
        speakerAdapter.setItems(speakers, context);
        recyclerSpeaker.setLayoutManager(speakerFlexboxLayoutManager);
        recyclerSpeaker.setAdapter(speakerAdapter);
        List<String> speakerList = List.of("don't-care", "exist", "not-exist");
        speakers.addAll(speakerList);
        speakerAdapter.notifyItemInserted(speakers.size()-1);
        new Handler().postDelayed(() -> {
            speakerAdapter.setIsSelected(0, true);
            speakerAdapter.notifyItemChanged(0);
        }, 100);

        RecyclerView recyclerMic = binding.recyclerMic;
        FlexboxLayoutManager micFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        micFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        micFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        micFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterExistOrNotAdapter micAdapter = new FilterExistOrNotAdapter();
        micAdapter.setItems(mics, context);
        recyclerMic.setLayoutManager(micFlexboxLayoutManager);
        recyclerMic.setAdapter(micAdapter);
        List<String> micList = List.of("don't-care", "exist", "not-exist");
        mics.addAll(micList);
        micAdapter.notifyItemInserted(mics.size()-1);
        new Handler().postDelayed(() -> {
            micAdapter.setIsSelected(0, true);
            micAdapter.notifyItemChanged(0);
        }, 100);

        RecyclerView recyclerDrums = binding.recyclerDrums;
        FlexboxLayoutManager drumsFlexboxLayoutManager = new FlexboxLayoutManager(context); //자연스러운 줄넘김을 위한 Flexbox 사용
        drumsFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        drumsFlexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        drumsFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        FilterExistOrNotAdapter drumAdapter = new FilterExistOrNotAdapter();
        drumAdapter.setItems(drums, context);
        recyclerDrums.setLayoutManager(drumsFlexboxLayoutManager);
        recyclerDrums.setAdapter(drumAdapter);
        List<String> drumList = List.of("don't-care", "exist", "not-exist");
        drums.addAll(drumList);
        drumAdapter.notifyItemInserted(drums.size()-1);
        new Handler().postDelayed(() -> {
            drumAdapter.setIsSelected(0, true);
            drumAdapter.notifyItemChanged(0);
        }, 100);

        /*가격, 수용인원, 스탠드 슬라이더 설정*/
        args.setMinPrice(Math.round(binding.sliderPrice.getValueFrom()));
        args.setMaxPrice(Math.round(binding.sliderPrice.getValueTo()));
        args.setMinCapacity(Math.round(binding.sliderCapacity.getValueFrom()));
        args.setMaxCapacity(Math.round(binding.sliderCapacity.getValueTo()));
        args.setMinStand(Math.round(binding.sliderStand.getValueFrom()));
        args.setMaxStand(Math.round(binding.sliderStand.getValueTo()));
        binding.sliderPrice.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                return;
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                args.setMinPrice(Math.round(slider.getValues().get(0)));
                args.setMaxPrice(Math.round(slider.getValues().get(1)));
                Log.e(TAG, "args.minPrice: " + args.getMinPrice() + " args.maxPrice: " + args.getMaxPrice());
            }
        });
        binding.sliderCapacity.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                return;
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                args.setMinCapacity(Math.round(slider.getValues().get(0)));
                args.setMaxCapacity(Math.round(slider.getValues().get(1)));
            }
        });
        binding.sliderStand.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                return;
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                args.setMinStand(Math.round(slider.getValues().get(0)));
                args.setMaxStand(Math.round(slider.getValues().get(1)));
            }
        });

        /*초기화 버튼 클릭 시 이벤트*/
        binding.buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                args.clear();
                for(int i = 0; i < sortBys.size(); i++) {
                    if (i == 0)
                        sortByAdapter.setIsSelected(i, true);
                    else
                        sortByAdapter.setIsSelected(i, false);
                }
                for(int i = 0; i < orderBys.size(); i++) {
                    if (i == 0)
                        orderByAdapter.setIsSelected(i, true);
                    else
                        orderByAdapter.setIsSelected(i, false);
                }
                for(int i = 0; i < types.size(); i++) {
                    if (i == 0)
                        filterTypeAdapter.setIsSelected(i, true);
                    else
                        filterTypeAdapter.setIsSelected(i, false);
                }
                for(int i = 0; i < pianos.size(); i++) {
                    if (i == 0)
                        pianoAdapter.setIsSelected(i, true);
                    else
                        pianoAdapter.setIsSelected(i, false);
                }
                for(int i = 0; i < amps.size(); i++) {
                    if (i == 0)
                        ampAdapter.setIsSelected(i, true);
                    else
                        ampAdapter.setIsSelected(i, false);
                }
                for(int i = 0; i < speakers.size(); i++) {
                    if (i == 0)
                        speakerAdapter.setIsSelected(i, true);
                    else
                        speakerAdapter.setIsSelected(i, false);
                }
                for(int i = 0; i < mics.size(); i++) {
                    if (i == 0)
                        micAdapter.setIsSelected(i, true);
                    else
                        micAdapter.setIsSelected(i, false);
                }
                for(int i = 0; i < drums.size(); i++) {
                    if (i == 0)
                        drumAdapter.setIsSelected(i, true);
                    else
                        drumAdapter.setIsSelected(i, false);
                }
                sortByAdapter.notifyDataSetChanged();
                orderByAdapter.notifyDataSetChanged();
                pianoAdapter.setExistOrNot(null);
                ampAdapter.setExistOrNot(null);
                speakerAdapter.setExistOrNot(null);
                micAdapter.setExistOrNot(null);
                drumAdapter.setExistOrNot(null);
                pianoAdapter.notifyDataSetChanged();
                ampAdapter.notifyDataSetChanged();
                speakerAdapter.notifyDataSetChanged();
                micAdapter.notifyDataSetChanged();
                drumAdapter.notifyDataSetChanged();
                binding.sliderPrice.setValues(0f, 1000000f);
                binding.sliderCapacity.setValues(0f, 200f);
                binding.sliderStand.setValues(0f, 50f);
            }
        });

        /*적용 버튼 클릭 시 이벤트*/
        binding.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                args.setSortBy(sortByAdapter.getSelectedItem());
                args.setOrderBy(orderByAdapter.getSelectedItem());
                args.setType(filterTypeAdapter.getSelectedItem());
                args.setHasPiano(pianoAdapter.getExistOrNot());
                args.setHasAmp(ampAdapter.getExistOrNot());
                args.setHasSpeaker(speakerAdapter.getExistOrNot());
                args.setHasMic(micAdapter.getExistOrNot());
                args.setHasDrums(drumAdapter.getExistOrNot());
                if(args.getSortBy() != null) bundle.putString("sortBy", args.getSortBy());
                if(args.getOrderBy() != null) bundle.putString("orderBy", args.getOrderBy());
                if(args.getType() != null) bundle.putInt("type", args.getType());
                if(args.getMinPrice() != null) bundle.putInt("minPrice", args.getMinPrice());
                if(args.getMaxPrice() != null) bundle.putInt("maxPrice", args.getMaxPrice());
                if(args.getMinCapacity() != null) bundle.putInt("minCapacity", args.getMinCapacity());
                if(args.getMaxCapacity() != null) bundle.putInt("maxCapacity", args.getMaxCapacity());
                if(args.getMinStand() != null) bundle.putInt("minStand", args.getMinStand());
                if(args.getMaxStand() != null) bundle.putInt("maxStand", args.getMaxStand());
                if(args.getHasPiano() != null) bundle.putBoolean("hasPiano", args.getHasPiano());
                if(args.getHasAmp() != null) bundle.putBoolean("hasAmp", args.getHasAmp());
                if(args.getHasSpeaker() != null) bundle.putBoolean("hasSpeaker", args.getHasSpeaker());
                if(args.getHasMic() != null) bundle.putBoolean("hasMic", args.getHasMic());
                if(args.getHasDrums() != null) bundle.putBoolean("hasDrums", args.getHasDrums());

                getParentFragmentManager().setFragmentResult("filter_args", bundle);
                dismiss();
            }
        });

        /*위가 둥근 형태로 만들기*/
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);

        return view;
    }

}

