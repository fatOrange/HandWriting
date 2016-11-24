package com.example.orange.handwriting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    static List<List<Integer>> datas = new ArrayList<List<Integer>>();
    private Bitmap mSignBitmap; //位图
    private String signPath;    //
    private ImageView ivSign;
    private TextView tvSign;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ivSign =(ImageView)findViewById(R.id.iv_sign);
        tvSign = (TextView)findViewById(R.id.tv_sign);

        ivSign.setOnClickListener(signListener); //设置监听器
        tvSign.setOnClickListener(signListener);
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
                    String text = ImageUtils.converNumMat(small_greybitmap);//将缩小的图片转mat字符
                    System.out.println(text);
                    List<List<Integer>> testDatas = new ArrayList<List<Integer>>();
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

    /**
     * 创建手写签名文件
     *
     * @return
     */
    private String createFile() {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory() + File.separator;
            _path = sign_dir + System.currentTimeMillis() + ".jpg";
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }
}