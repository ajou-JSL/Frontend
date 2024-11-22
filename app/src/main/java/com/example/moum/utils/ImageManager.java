package com.example.moum.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

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

    public static Boolean isUrlValid(String url){
        String urlFormat = "^https://kr\\.object\\.ncloudstorage\\.com/moumstorage/([\\w가-힣\\s-]+/)*[\\w가-힣\\s-]+\\.jpg$";
        Pattern urlPattern = Pattern.compile(urlFormat);
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

    public static void downloadImageToGallery(Context context, String imageUrl, String fileName) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setTitle("Downloading Image");
            request.setDescription("Downloading " + fileName);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName);

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
                Toast.makeText(context, "다운로드 시작", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "다운로드 실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
