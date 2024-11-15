package com.example.moum.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeManager {

    public static String getVideoId(String url){
        String urlFormat = "^https://www\\.youtube\\.com/watch/?v=([a-zA-Z0-9_-]+)$";
        Pattern urlPattern = Pattern.compile(urlFormat);
        Matcher matcher = urlPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
