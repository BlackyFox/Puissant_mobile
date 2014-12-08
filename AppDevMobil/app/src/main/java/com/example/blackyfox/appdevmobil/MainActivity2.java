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

import java.io.OutputStream;


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
        new MyAsyncTsk().execute(ms);
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
