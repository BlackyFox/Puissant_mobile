package com.example.blackyfox.urls;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DLIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.blackyfox.urls.action.FOO";
    private static final String ACTION_BAZ = "com.example.blackyfox.urls.action.BAZ";

    public static final String TRANSACTION_DONE = "com.example.blackyfox.urls.TRANSACTION_DONE";
    public static final String BROADCAST_ACTION = "com.example.blackyfox.urls.TRANSACTION_DONE";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.blackyfox.urls.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.blackyfox.urls.extra.PARAM2";

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
                String result = downloadUrl(dataString);
                Intent i = new Intent();
                i.setAction(BROADCAST_ACTION);
                i.putExtra("content", result);
                sendBroadcast(i);
            }catch (IOException e){
                Log.d("IOExep", "erreur dans le DLintent service 1");
            }
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
