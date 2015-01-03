package com.esiea.dtd.daytoday;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Antoine on 02/01/2015.
 */
public class MeteoActivity extends ActionBarActivity {
    private static final String DONE = "com.esiea.dtd.daytoday.DONE";
    private MyReceiver receiver;
    private IntentFilter filter;

    private static final int notificationID = 1234;

    private String urlStart = "http://api.openweathermap.org/data/2.5/weather?q=";
    private Button bget, braz;
    private TextView location_ville, location_pays,country,temperature,humidity,pressure, titre,
            wdesc, Wdate;
    private ImageView img;
    private JSONHandler obj;

    ProgressDialog pDialog;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo);

        setTitle("MétéoCity");
        //getActionBar().setIcon(R.drawable.ic_weather_logo);

        filter = new IntentFilter(DONE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyReceiver();

        bget = (Button) findViewById(R.id.bget);
        bget.setTextAppearance(this, R.style.TextShadow_disable);
        braz = (Button) findViewById(R.id.bclear);
        braz.setTextAppearance(this, R.style.TextShadow_disable);
        location_ville = (EditText)findViewById(R.id.location_ville);
        location_pays = (EditText)findViewById(R.id.location_pays);
        country = (TextView)findViewById(R.id.pays);
        temperature = (TextView)findViewById(R.id.temperature);
        humidity = (TextView)findViewById(R.id.humidite);
        pressure = (TextView)findViewById(R.id.pression);
        titre = (TextView)findViewById(R.id.titre);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/cfont.ttf");
        titre.setTypeface(customFont);
        img = (ImageView)findViewById(R.id.condition);
        wdesc = (TextView)findViewById(R.id.wdesc);
        Wdate = (TextView)findViewById(R.id.date);

        bget.setOnClickListener(bget_click);
        braz.setOnClickListener(raz_click);
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
    }

    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent){
            String MyJson = intent.getStringExtra("Json");
            obj = new JSONHandler(MyJson);
            obj.fetchJSON();

            while(obj.parsingComplete);

            if(obj.getEtat() == "good") {
                long unixSeconds = new Long(obj.getTime());
                Date date = new Date(unixSeconds*1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
                String formattedDate = sdf.format(date);
                Wdate.setText("Le "+formattedDate);

                country.setText(obj.getCity()+", ("+obj.getCountry()+ ")");

                String temp = obj.getTemperature();
                float f = Float.parseFloat(obj.getTemperature());
                f -= 273.15;
                temperature.setText(String.format("%.2f", f) + "°C");

                humidity.setText(obj.getHumidity()+"%");

                pressure.setText(obj.getPressure()+" hPa");

                new LoadImage().execute("http://openweathermap.org/img/w/"+obj.getIcon()+".png");

                wdesc.setText("("+obj.getDesc()+")");
                showNotification(country.getText().toString(), temperature.getText().toString());
            }
            if(obj.getEtat() == "do not exist"){
                Toast.makeText(getApplicationContext(), "Spelling is wrong, please check and " +
                        "submit again", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void hideSoftKeyboard(){
        if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(location_ville.getWindowToken(), 0);
        }
    }

    private void showSoftKeyboard(){
        if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            imm.showSoftInput(location_ville, InputMethodManager.SHOW_IMPLICIT);
            imm.showSoftInput(location_pays, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private View.OnClickListener raz_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            country.setText("");
            Wdate.setText("");
            temperature.setText("");
            humidity.setText("");
            pressure.setText("");
            wdesc.setText("");
            img.setImageBitmap(null);
            location_pays.setText("");
            location_ville.setText("");
            showSoftKeyboard();
        }
    };
    private View.OnClickListener bget_click = new View.OnClickListener(){
        public void onClick(View v){
            if(location_ville.getText().toString().matches("") && location_pays.getText()
                    .toString().matches("")) {
                Toast.makeText(getApplicationContext(), "Entrez une ville", Toast.LENGTH_LONG)
                        .show();
            }else {
                hideSoftKeyboard();
                country.setText("");
                Wdate.setText("");
                temperature.setText("");
                humidity.setText("");
                pressure.setText("");
                wdesc.setText("");
                img.setImageBitmap(null);

                //Toast.makeText(getApplicationContext(), "URL OK", Toast.LENGTH_LONG).show();
                if(location_pays.getText().toString().matches("")){
                    String url = location_ville.getText().toString();
                    String url_totale = urlStart + url + "&lang=fr";
                    url_totale = url_totale.replaceAll(" ", "%20");
                    url_totale = removeAccent(url_totale);

                    Intent i = new Intent(MeteoActivity.this, DLIntentService.class);
                    i.putExtra("request", url_totale);
                    startService(i);
                }else {
                    String url = location_ville.getText().toString() + ","
                            + location_pays.getText().toString();
                    String url_totale = urlStart + url + "&lang=fr";
                    url_totale = removeAccent(url_totale);

                    Intent i = new Intent(MeteoActivity.this, DLIntentService.class);
                    i.putExtra("request", url_totale);
                    startService(i);
                }
            }
        }
    };


    private void showNotification(String ville, String temperature){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setContentTitle("Météo de "+ ville)
                        .setContentText("Il fait "+temperature);
        Intent resultIntent = new Intent(this, MeteoActivity.class);
        resultIntent.putExtra("notificationID", notificationID);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MeteoActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());

    }
    public static String removeAccent(String source) {
        return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
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
        switch (id){
            case R.id.action_settings: {
                return true;
            }
            case R.id.m_home: {
                if(this.getClass().getSimpleName().matches("MainActivity")) {
                    CharSequence text = "Already on the home page";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }else{
                    Intent i = new Intent(this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                break;
            }
            case R.id.m_help: {
                MyDialog md = new MyDialog();
                md.show(getFragmentManager(), "tag_frag");
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.realise);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                }
            });
            return builder.create();
        }

    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MeteoActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            InputStream is = null;
            URL myUrl = null;
            try {
                myUrl = new URL(args[0]);
                is = (InputStream) myUrl.getContent();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(is != null) {
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                img.setImageBitmap(image);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(MeteoActivity.this, "Image Does Not exist or Network Error",
                        Toast.LENGTH_SHORT).show();
            }
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