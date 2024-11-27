package com.example.moum.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeManager {

    public static String getVideoId(String url){
        Log.e("youtubeManager", url);

        String regex = "(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }

    public static Boolean isUrlValid(String url) {
        String urlFormat = "(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)[a-zA-Z0-9_-]+";
        Pattern urlPattern = Pattern.compile(urlFormat);
        return urlPattern.matcher(url).matches();
    }

}
