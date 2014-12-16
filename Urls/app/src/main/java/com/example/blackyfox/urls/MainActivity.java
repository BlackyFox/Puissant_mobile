package com.example.blackyfox.urls;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Object;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    Button jsonbut;
    TextView jsonText;
    EditText url;
    public static final String BROADCAST_ACTION = "com.example.blackyfox.urls.TRANSACTION_DONE";
    private MyReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter = new IntentFilter(BROADCAST_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyReceiver();
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        //registerReceiver(receiver, filter);

        jsonbut = (Button) findViewById(R.id.jbut);
        jsonText = (TextView) findViewById(R.id.jsonres);
        url = (EditText) findViewById(R.id.url_edit);

        jsonbut.setOnClickListener(jclick);
    }

    @Override
    public void onResume(){
        super.onResume();

        registerReceiver(receiver, filter);
    }

    @Override
    public void onStop(){
        super.onStop();
        unregisterReceiver(receiver);
        //tototototototo

    }
    private class MyReceiver extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent){
            String content = intent.getStringExtra("content");
            jsonText.setText(content);
        }
    }

    private View.OnClickListener jclick = new OnClickListener(){
        public void onClick(View v){
            if(url.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), "Entrez l\'URL !!", Toast.LENGTH_LONG).show();
            }else {
                Intent i = new Intent(MainActivity.this, DLIntentService.class);
                i.putExtra("request", url.getText().toString());
                startService(i);
            }
        }
    };

    /*private class DLTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls){
            try{
                //TODO
            }catch(IOException e){
                return"Unable to retrieve the Web Page.";
            }
        }
        @Override
        protected void onPostExecute(String result){
            jsonText.setText(result);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
