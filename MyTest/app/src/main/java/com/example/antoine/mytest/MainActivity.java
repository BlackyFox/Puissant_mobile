package com.example.antoine.mytest;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private String urlStart = "http://api.openweathermap.org/data/2.5/weather?q=";
    private Button bget;
    private TextView location,country,temperature,humidity,pressure, titre;
    private HandleJSON obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bget = (Button) findViewById(R.id.bget);
        location = (EditText)findViewById(R.id.location);
        country = (TextView)findViewById(R.id.pays);
        temperature = (TextView)findViewById(R.id.temperature);
        humidity = (TextView)findViewById(R.id.humidite);
        pressure = (TextView)findViewById(R.id.pression);
        titre = (TextView)findViewById(R.id.titre);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/cfont.ttf");
        titre.setTypeface(customFont);

        bget.setOnClickListener(bget_click);
    }

    private View.OnClickListener bget_click = new View.OnClickListener(){
        public void onClick(View v){
            if(location.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), "Entrez l\'URL !!", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "URL OK", Toast.LENGTH_LONG).show();
                String url = location.getText().toString();
                String url_totale = urlStart+url;
                obj = new HandleJSON(url_totale);
                obj.fetchJSON();
                while(obj.parsingComplete);
                country.setText(obj.getCountry());
                temperature.setText(obj.getTemperature());
                humidity.setText(obj.getHumidity());
                pressure.setText(obj.getPressure());
    }
}
};

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
