package com.example.blackyfox.urls;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    Button jsonbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsonbut = (Button) findViewById(R.id.jbut);

        jsonbut.setOnClickListener(jclick);
    }

    private View.OnClickListener jclick = new OnClickListener(){
        public void onClick(View v){
            new DLTask().execute("http://fabrigli.fr/cours/example.json");
        }
    };

    private class DLTask extends AsyncTask<String, Void, String> {
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

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader r = null;
        r = new InputStreamReader(stream, "UTF-8");
        char[] buf = new char[len];
        r.read(buf);
        return new String(buf);
    }

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
