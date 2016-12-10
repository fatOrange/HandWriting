package com.example.orange.handwriting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orange.handwriting.Knn.KNN;
import com.example.orange.handwriting.Knn.KNNreader;
import com.example.orange.handwriting.Photo.PhotoUtils;
import com.example.orange.handwriting.Socket.MySocket;
import com.example.orange.handwriting.Write.DBAdapter;
import com.example.orange.handwriting.Write.DialogListener;
import com.example.orange.handwriting.Write.WritePadDialog;
import com.example.orange.handwriting.image.ImageFile;
import com.example.orange.handwriting.image.ImageUtils;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    static List<List<Integer>> datas = new ArrayList<List<Integer>>();
    private Bitmap mSignBitmap; //位图
    private String signPath;    //
    private ImageView ivSign;
    private TextView tvSign;
    static String text;
    private DBAdapter dba ;


    //拍照的基本设置
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private ImageView photoImg;
    ImageFile imageFile;
    PhotoUtils photoUtils;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dba = new DBAdapter(getApplicationContext());

        ivSign =(ImageView)findViewById(R.id.iv_sign);
        tvSign = (TextView)findViewById(R.id.tv_sign);

        ivSign.setOnClickListener(signListener); //设置监听器
        tvSign.setOnClickListener(signListener);

        photoImg = (ImageView) findViewById(R.id.photoImg);
        imageFile = new ImageFile(getApplicationContext());
        photoUtils=new PhotoUtils(getApplicationContext());


    }


    private OnClickListener signListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            WritePadDialog writeTabletDialog = new WritePadDialog(
                    MainActivity.this, new DialogListener() {
                @Override
                public void refreshActivity(Object object) {

                    mSignBitmap = (Bitmap) object;
//                    signPath = createFile();
                    //业务逻辑

                    Bitmap greybitmap = ImageUtils.convertGreyImg(mSignBitmap); //将原图转灰色图片
                    Bitmap small_greybitmap = ImageUtils.zoomBitmap(greybitmap,32,32);//将灰色图片缩小
                    text = ImageUtils.converNumMat(small_greybitmap);//将缩小的图片转mat字符
                    Log.d("msg", "refreshActivity: "+text);
                    List<List<Integer>> testDatas = new ArrayList<List<Integer>>();

                    //对数据库进行查询 并且 格式化训练集合
                    dba.open();
                    //String[] a = dba.getAllData(); //使用数据库
                    if (datas.size()==0)
                        KNNreader.read(datas,getApplicationContext());
                    KNNreader.read2(testDatas,text);
                    KNN knn = new KNN();
                    String result = knn.knn(datas, testDatas.get(0), 3);
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();


                    //
                            /*BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 15;
                            options.inTempStorage = new byte[5 * 1024];
                            Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/
                    ivSign.setImageBitmap(greybitmap);
                    tvSign.setVisibility(View.GONE);
                }
            });
            writeTabletDialog.show();
        }
    };

//    public void onsubmit(View view){
//        EditText ed =(EditText)findViewById(R.id.editText);
//        String a = ed.getText().toString();
//        if (a.length()>1) Toast.makeText(getApplicationContext(),"请输入单个数字",Toast.LENGTH_LONG).show();
//        else {
//            text+=a;
//            dba.open();
//            dba.insert(text);
//            System.out.println(text);
//        }
//    }

    public void makephoto(View view){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = photoUtils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap img = null;
                try {
                    img = imageFile.readFilefromsrc("IMG.jpg");

                    Bitmap newimg=MySocket.connecttoserver(img);
                    System.out.println("1");
                    ivSign.setImageBitmap(newimg);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

}