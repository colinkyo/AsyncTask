package com.example.a7yan.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
    }
    public void downloadimg(View view){
        //MyAsyncTask myAsyncTask = new MyAsyncTask("http://d.hiphotos.baidu.com/image/pic/item/5882b2b7d0a20cf482c772bf73094b36acaf997f.jpg");
        new MyAsyncTask().execute("http://d.hiphotos.baidu.com/image/pic/item/5882b2b7d0a20cf482c772bf73094b36acaf997f.jpg");
    }

    /**
     * Params 表示当前的AsyncTask操作时需要的参数类型
     * Progress 表示当前的AsyncTask耗时操作时需要的参数类型
     * Result 表示当前的AsyncTask耗时操作时结果的参数类型
     */
    class MyAsyncTask extends AsyncTask<String,Void,byte[]>{

        /**
         * 表示AsynTask执行之前运行在UI线程的初始方法
         */
        @Override
        protected void onPreExecute() {
            Log.i("7Yan",Thread.currentThread().getName()+"-------------onPreExecute-----------");
            super.onPreExecute();
        }

        /**
         * onPreExecute()结束后，开始的后台工作
         * 该方法的可变参数与类中泛型参数第一个参数类型一致
         * @param params
         * @return
         */
        @Override
        protected byte[] doInBackground(String... params) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] images=null;
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode==200){
                    InputStream input = conn.getInputStream();
                    byte[] data=new byte[1024];
                    int temp=0;
                    while((temp=input.read(data))!=-1){
                        outputStream.write(data,0,temp);
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            images = outputStream.toByteArray();
            return images;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            Log.i("7Yan",Thread.currentThread().getName()+"-------------onPostExecute-----------");
            if(bytes!=null && bytes.length!=0){
                Bitmap bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                iv.setImageBitmap(bm);
            }else{
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(bytes);
        }
    }
}
