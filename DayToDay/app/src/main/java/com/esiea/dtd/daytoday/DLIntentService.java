package com.esiea.dtd.daytoday;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Antoine PUISSANT on 30/12/2014.
 */
public class DLIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.esiea.dtd.daytoday.action.FOO";
    private static final String ACTION_BAZ = "com.esiea.dtd.daytoday.action.BAZ";

    private static final String DONE = "com.esiea.dtd.daytoday.DONE";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.esiea.dtd.daytoday.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.esiea.dtd.daytoday.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DLIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DLIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public DLIntentService() {
        super("DLIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
            String dataString = intent.getStringExtra("request");
            try{
                Log.d("URL", dataString);
                String result = downloadUrl(dataString);
                Log.d("JSON", result);
                Intent i = new Intent();
                i.setAction(DONE);
                i.putExtra("Json", result);
                sendBroadcast(i);
            }catch (IOException e){
                Log.d("IOExep", "erreur dans le DLintent service 1"+e.getMessage());
                Toast.makeText(getApplicationContext(), "Something  went wrong... " +
                                "Check your internet connection and repress the Get button",
                        Toast.LENGTH_SHORT).show();
                //e.printStackTrace();
            }
        }
    }

    private String downloadUrl(String myUrl) throws IOException{
        InputStream is = null;
        HttpURLConnection connec = null;
        String contentAsString = null;
        try{
            URL url = new URL(myUrl);
            connec = (HttpURLConnection) url.openConnection();
            connec.setRequestMethod("GET");
            connec.connect();
            //int reponse = connec.getResponseCode();
            is = connec.getInputStream();
            contentAsString = convertStreamToString(is);
            is.close();
        }finally {
            if(is != null){
                is.close();
            }
            connec.disconnect();
            Log.d("DISCONEC", "Déconnexion effectuée");
        }
        return(contentAsString);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}