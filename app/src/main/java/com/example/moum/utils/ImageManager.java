package com.example.moum.utils;

import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ImageManager {

    private Context context;
    private static int num = 0;
    public ImageManager(Context context){
        this.context = context;
    }

    public File convertUriToFile(Uri uri) {
        File file = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                // 임시 파일 생성
                file = new File(context.getCacheDir(), "temp_image" + num++ + ".jpg");
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Boolean isUrlValid(String url) {
        String urlFormat = "^https://kr\\.object\\.ncloudstorage\\.com/moumstorage/([\\w가-힣\\s-]+/)*[\\w가-힣\\s-]+\\.(jpg|png|jpeg|gif|bmp)$";
        Pattern urlPattern = Pattern.compile(urlFormat, Pattern.CASE_INSENSITIVE); // 대소문자 무시
        return urlPattern.matcher(url).matches();
    }

    public File downloadImageToFile(String imageUrl) {
        try{
            return Glide.with(context)
                    .downloadOnly()
                    .load(imageUrl)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
