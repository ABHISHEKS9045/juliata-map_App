package com.example.map_ui.screens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Random;


public class AppUtils {

    private static final boolean IS_DEBUG = false;
    //public static final String DOC_ALERT_MSG = "You have to allow permission to access this feature";
    public static final String START_POINT = "Source file path is \n ***/***/";
// function get random Images drivers.
    public static String getRandomImageFileName(Context context) {
        File mediaStorageDir = context.getFilesDir();
        int random = new Random().nextInt(8997);
        String mImageName = "Braver_Img".concat("_") + random + ".jpg";
        return new File(mediaStorageDir.getPath() + "/" + mImageName).getAbsolutePath();
    }

    /**
     * @param tag - Contains class name
     * @param msg - Log message as String
     *            Method used to print log in console for development
     */
    // print console logcate.
    public static void printLogConsole(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d("##@" + tag, msg);
        }
    }

    /**
     * This method is a string return method
     * Method used get file name from local file path
     */
    // file path function driver.
    public static String getFileNameFromPath(String filePath) {
        String fileName = "";
        try {
            fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        } catch (Exception e) {
            AppUtils.printLogConsole("getFileNameFromPath", "Exception-------->" + e.getMessage());
        }
        return fileName;
    }
// function called openDocument any files perform.
    public static void openDocument(Context context, String filePath) {
        try {
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(filePath));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (filePath.contains(".doc") || filePath.contains(".docx")) {
                intent.setDataAndType(uri, "application/msword");
            } else if (filePath.contains(".pdf")) {
                intent.setDataAndType(uri, "application/pdf");
            } else if (filePath.contains(".ppt") || filePath.contains(".pptx")) {
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (filePath.contains(".xls") || filePath.contains(".xlsx")) {
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (filePath.contains(".zip") || filePath.contains(".rar")) {
                intent.setDataAndType(uri, "application/x-wav");
            } else if (filePath.contains(".rtf")) {
                intent.setDataAndType(uri, "application/rtf");
            } else if (filePath.contains(".wav") || filePath.contains(".mp3")) {
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (filePath.contains(".gif")) {
                intent.setDataAndType(uri, "image/gif");
            } else if (filePath.contains(".jpg") || filePath.contains(".jpeg") || filePath.contains(".png")) {
                intent.setDataAndType(uri, "image/jpeg");
            } else if (filePath.contains(".txt")) {
                intent.setDataAndType(uri, "text/plain");
            } else if (filePath.contains(".3gp") || filePath.contains(".mpg") || filePath.contains(".mpeg") || filePath.contains(".mpe") || filePath.contains(".mp4") || filePath.contains(".avi")) {
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            AppUtils.printLogConsole("openDocument", "Exception-------->" + e.getMessage());
        }
    }
}