package com.example.orange.handwriting;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.orange.handwriting.R;


public class WritePadDialog extends Dialog {

    Context context;
    LayoutParams p ;
    DialogListener dialogListener;

    public WritePadDialog(Context context,DialogListener dialogListener) {
        super(context);
        this.context = context;
        this.dialogListener = dialogListener;
    }

    static final int BACKGROUND_COLOR = Color.WHITE;

    static final int BRUSH_COLOR = Color.BLACK;

    PaintView mView;

    /** The index of the current color to use. */
    int mColorIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗体大小
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.write_pad);

        p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = 960;//(int) (d.getHeight() * 0.4);   //高度设置为屏幕的0.4
        p.width = 960;//(int) (d.getWidth() * 0.6);    //宽度设置为屏幕的0.6
        getWindow().setAttributes(p);     //设置生效


        mView = new PaintView(context); //是个图
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
        frameLayout.addView(mView);
        //mView.requestFocus();
        Button btnClear = (Button) findViewById(R.id.tablet_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mView.clear();
            }
        });

        //点击确定的操作
        Button btnOk = (Button) findViewById(R.id.tablet_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {



                    //把对象传给监听器
                    dialogListener.refreshActivity(mView.getCachebBitmap());
                    //把图片去掉
                    WritePadDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnCancel = (Button)findViewById(R.id.tablet_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }


    /**
     * This view implements the drawing canvas.
     *
     * It handles all of the input events and drawing functions.
     */
    class PaintView extends View {
        private Paint paint;
        private Canvas cacheCanvas;
        private Bitmap cachebBitmap;
        private Path path;

        public Bitmap getCachebBitmap() {
            return cachebBitmap;
        }

        public PaintView(Context context) {
            super(context);
            init();
        }

        /**
         * 初始化画笔和画板
         * 画笔
         *  抗锯齿 笔宽100 颜色 红
         * 画布
         *  宽和长 width height 背景色 white
         */
        private void init(){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(100);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            path = new Path();
            cachebBitmap = Bitmap.createBitmap(p.width, p.height, Config.ARGB_4444);//创建位图
            cacheCanvas = new Canvas(cachebBitmap);
            cacheCanvas.drawColor(Color.WHITE);
        }

        /**
         * 清除按钮
         * 重新设置画笔和画板
         */
        public void clear() {
            if (cacheCanvas != null) {

                paint.setColor(Color.RED);
                cacheCanvas.drawPaint(paint);
                cacheCanvas.drawColor(Color.WHITE);
                invalidate();
            }
        }



        @Override
        protected void onDraw(Canvas canvas) {
            // canvas.drawColor(BRUSH_COLOR);
            canvas.drawBitmap(cachebBitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
            int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
            if (curW >= w && curH >= h) {
                return;
            }

            if (curW < w)
                curW = w;
            if (curH < h)
                curH = h;

            Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
            Canvas newCanvas = new Canvas();
            newCanvas.setBitmap(newBitmap);
            if (cachebBitmap != null) {
                newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
            }
            cachebBitmap = newBitmap;
            cacheCanvas = newCanvas;
        }

        private float cur_x, cur_y;

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    cur_x = x;
                    cur_y = y;
                    path.moveTo(cur_x, cur_y);

                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    path.quadTo(cur_x, cur_y, x, y);
                    cur_x = x;
                    cur_y = y;
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    cacheCanvas.drawPath(path, paint);
                    path.reset();
                    break;
                }
            }

            invalidate();

            return true;
        }
    }

}