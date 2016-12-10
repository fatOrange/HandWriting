package com.example.orange.handwriting.Knn;

import android.content.Context;

import com.example.orange.handwriting.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by orange on 16/11/24.
 */
public class KNNreader {
    public static void read(List<List<Integer>> datas,String [] a, Context context){
        try {
//            InputStream in = context.getResources().openRawResource(R.raw.datafile);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            String data = reader.readLine();

            List<Integer> l = null;
            int x =0;
            String data = a[x];
            while (data != null) {
                String t[] = data.split("");
                l = new ArrayList<Integer>();
                for (int i = 1; i < t.length; i++) {

                    l.add(Integer.parseInt(t[i]));
                }
                datas.add(l);
//                data = reader.readLine();
                if (x>=a.length-2) break;
                data=a[++x];

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read(List<List<Integer>> datas, Context context){
        try {
            InputStream in = context.getResources().openRawResource(R.raw.datafile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String data = reader.readLine();

            List<Integer> l = null;
            int x =0;
//            String data = a[x];
            while (data != null) {
                String t[] = data.split("");
                l = new ArrayList<Integer>();
                for (int i = 1; i < t.length; i++) {

                    l.add(Integer.parseInt(t[i]));
                }
                datas.add(l);
                data = reader.readLine();
//                if (x>=a.length) break;
//                data=a[++x];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read2(List<List<Integer>> datas, String data){
        try {
            List<Integer> l = null;
            String t[] = data.split("");
            l = new ArrayList<Integer>();
            for (int i = 1; i < t.length; i++) {
                l.add(Integer.parseInt(t[i]));
            }
            datas.add(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
