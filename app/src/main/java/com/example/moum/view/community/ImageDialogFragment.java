package com.example.moum.view.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;

public class ImageDialogFragment extends DialogFragment {
    private static final String ARG_IMAGE_URL = "image_url";

    public static ImageDialogFragment newInstance(String imageUrl) {
        ImageDialogFragment fragment = new ImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_fullscreen, container, false);

        ImageView imageView = view.findViewById(R.id.fullscreen_image);
        String imageUrl = getArguments() != null ? getArguments().getString(ARG_IMAGE_URL) : null;

        if (imageUrl != null) {
            Glide.with(requireContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_gray)
                            .error(R.drawable.background_gray))
                    .load(imageUrl)
                    .into(imageView);
        }

        view.findViewById(R.id.close_button).setOnClickListener(v -> dismiss());
        return view;
    }

}
