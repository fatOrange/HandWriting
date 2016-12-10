package com.example.orange.handwriting.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by orange on 16/11/23.
 */
public class ImageFile {

    private Context context;

    private String Filename = null;

    private String Filetext = null;

    public void saveFile(String Filename,String Filetext) throws IOException {

        FileOutputStream fos = context.openFileOutput(Filename,Context.MODE_PRIVATE);
        String text = Filetext;
        fos.write(text.getBytes());
        fos.flush();
        fos.close();
    }

    public String readFile(String Filename) throws IOException{
        FileInputStream fis = context.openFileInput(Filename);
        byte[] readBytes = new byte[fis.available()];
        while(fis.read(readBytes)!=-1){}
        String text = new String(readBytes);
        return text;
    }

    public Bitmap readFilefromsrc(String Filename) throws IOException{

        String rootpath = context.getExternalCacheDir().getPath(); //图片存放的路径
        String path = rootpath+ File.separator+Filename;
        InputStream is = context.getClassLoader().getResourceAsStream(path); //得到图片流
        System.out.println(path);
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inMutable = true;
        Bitmap bitmap= BitmapFactory.decodeFile(path,op);
        System.out.println(bitmap);
        return bitmap;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }
    public ImageFile(Context context){
        this.context = context;
    }
    public String getFiletext() {
        return Filetext;
    }

    public void setFiletext(String filetext) {
        Filetext = filetext;
    }
}
