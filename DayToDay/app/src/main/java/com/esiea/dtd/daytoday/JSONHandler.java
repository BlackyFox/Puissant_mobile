package com.esiea.dtd.daytoday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Antoine PUISSANT on 30/12/2014.
 */
public class JSONHandler {

    private String country = "county";
    private String city = "city";
    private String temperature = "temperature";
    private String humidity = "humidity";
    private String pressure = "pressure";
    private String MyJsonString = null;
    private String etat = "etat";
    private String Wicon = null;
    private String Wdescription = null;
    private String time = null;

    public volatile boolean parsingComplete = true;
    public JSONHandler(String MyJson){
        this.MyJsonString = MyJson;
    }
    public String getCountry(){
        return country;
    }
    public String getCity(){
        return city;
    }
    public String getTemperature(){
        return temperature;
    }
    public String getHumidity(){
        return humidity;
    }
    public String getPressure(){
        return pressure;
    }
    public String getEtat() {
        return etat;
    }
    public String getIcon(){
        return Wicon;
    }
    public String getDesc(){
        return Wdescription;
    }
    public String getTime() {
        return time;
    }

    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            if(reader.getInt("cod") == 200) {
                city = reader.getString("name");
                JSONObject sys = reader.getJSONObject("sys");
                country = sys.getString("country");
                Log.d("PAYS", country);

                JSONObject main = reader.getJSONObject("main");
                temperature = main.getString("temp");

                pressure = main.getString("pressure");
                humidity = main.getString("humidity");

                JSONArray Warray = reader.getJSONArray("weather");
                JSONObject Jicon = Warray.getJSONObject(0);
                Wicon = Jicon.getString("icon");
                Wdescription = Jicon.getString("description");

                time = reader.getString("dt");

                etat = "good";

                parsingComplete = false;
            }
            if(reader.getInt("cod") == 404){
                etat = "do not exist";
                parsingComplete = false;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void fetchJSON(){
        Log.d("JSON2", MyJsonString);
        readAndParseJSON(MyJsonString);
    }

}
