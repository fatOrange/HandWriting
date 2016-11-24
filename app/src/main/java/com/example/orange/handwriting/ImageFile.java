package com.example.orange.handwriting;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
