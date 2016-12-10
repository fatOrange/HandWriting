package com.example.orange.handwriting.Photo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by orange on 16/12/10.
 */
public class PhotoUtils {
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Context context;
    public PhotoUtils(Context context){
        this.context = context;
    }

    /** Create a file Uri for saving an image or video */
    public Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(context.getExternalCacheDir().getPath());
        //getExternalStoragePublicDirectory(
        //Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
//    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG.jpg");
            System.out.println(mediaStorageDir.getPath() + File.separator +
                    "IMG.jpg");
        }

        return mediaFile;
    }

}
