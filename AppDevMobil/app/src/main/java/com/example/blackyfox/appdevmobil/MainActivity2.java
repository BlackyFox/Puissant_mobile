package com.example.blackyfox.appdevmobil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity2 extends Activity {
    private ProgressBar MyPB;
    private TextView tpro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        String ms = getIntent().getStringExtra(MainActivity.VALUE);

        TextView tv1 = (TextView) findViewById(R.id.g_value);
        MyPB = (ProgressBar)findViewById(R.id.pBAsync);
        tpro = (TextView)findViewById(R.id.tPro);
        tv1.setText(ms);
        new DLTask().execute("http://fabrigli.fr/cours/example.json");
        //new MyAsyncTsk().execute(ms);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DLTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            try{
                return downloadUrl(urls[0]);
            }catch(IOException e){
                return"Unable to retrieve the Web Page.";
            }
        }
        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    private String downloadUrl(String myUrl) throws IOException{
        InputStream is = null;
        try{
            URL url = new URL(myUrl);
            HttpURLConnection connec = (HttpURLConnection) url.openConnection();
            connec.setRequestMethod("GET");
            connec.connect();
            int reponse = connec.getResponseCode();
            is = connec.getInputStream();

            String contentAsString = readIt(is, 500);
            return(contentAsString);
        }finally {
            if(is != null){
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException{
        Reader r = null;
        r = new InputStreamReader(stream, "UTF-8");
        char[] buf = new char[len];
        r.read(buf);
        return new String(buf);
    }

    private class MyAsyncTsk extends AsyncTask<String, Integer, Void>{
        String s = null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            MyPB.setProgress(values[0]);
            tpro.setText(values[0].toString()+"%");

        }
        @Override
        protected Void doInBackground(String... strings){
            Looper.prepare();
            s = strings[0];
            int progress;
            for (progress=0;progress<=100;progress++)
            {
                for (int i=0; i<10000000; i++){}
                publishProgress(progress);
                progress++;
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            Log.d("STRING", "s = "+s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            LinearLayout ll = (LinearLayout) findViewById(R.id.root);
            ll.setBackgroundColor(Color.argb(255, 0, 255, 0));
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //setContentView(R.layout.activity_main_activity2);

        } else {
            //setContentView(R.layout.activity_main_activity2);
        }
    }
}
