package com.esiea.dtd.daytoday;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class AgendaActivity extends ActionBarActivity {

    private Button newTask2;
    private TextView v;
    String s1, s2;
    private ListView maListeView;

    Context context;
    String path;
    File file;

    private SimpleAdapter mSchedule;
    private HashMap<String, String> map;
    //Création de la ArrayList qui remplira la listView

    private ArrayList<HashMap<String, String>> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        context = getApplicationContext();
        listItem = new ArrayList<HashMap<String, String>>();

        path = context.getFilesDir().getAbsolutePath();
        file = new File(path + "/myAgenda.txt");

        //Initialisation des variables
        newTask2 = (Button) findViewById(R.id.newtask);
        v = (TextView)findViewById(R.id.recup);
        maListeView = (ListView)findViewById(R.id.listviewperso);



        //On créer notre propre adapter
        mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichageitem,
                new String[] {"img","titre", "type", "time", "date"},
                new int[] {R.id.img, R.id.titre, R.id.type, R.id.time, R.id.date});


        //On applique notre Adapter sur notre listView
        maListeView.setAdapter(mSchedule);
        registerForContextMenu(maListeView);

        newTask2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AgendaActivity.this, NewTaskActivity.class);
                startActivity(i);
            }
        });


        //On déclare la HashMap qui contiendra les informations pour un item
        map = new HashMap<String, String>();

        int length = (int) file.length();
        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        String contents = new String(bytes);
        StringTokenizer token = new StringTokenizer(contents, "¤¤");

        while(token.hasMoreTokens()) {
            String element = token.nextToken();
            StringTokenizer newtoken = new StringTokenizer(element, "**");

            s1 = newtoken.nextToken();
            s2 = newtoken.nextToken();
            String tempdate = new String(newtoken.nextToken());
            String temptime = new String(newtoken.nextToken());

            map.put("type", s2);
            map.put("titre", s1);
            map.put("time", temptime);
            map.put("date", tempdate);

            switch (s2) {
                case "Déjeuner":
                    map.put("img", String.valueOf(R.drawable.dejeuner_logo));
                    break;
                case "Diner":
                    map.put("img", String.valueOf(R.drawable.diner_logo));
                    break;
                case "Rendez-vous":
                    map.put("img", String.valueOf(R.drawable.rdv_logo));
                    break;
                case "Réunion":
                    map.put("img", String.valueOf(R.drawable.meeting_logo));
                    break;
                case "Soirée":
                    map.put("img", String.valueOf(R.drawable.soiree_logo));
                    break;
                case "Autre":
                    map.put("img", String.valueOf(R.drawable.autre_logo));
                    break;
            }

            listItem.add(map);

            mSchedule.notifyDataSetChanged();
            map = new HashMap<String, String>();
        }

        Log.d("Taille du bordel: ", new String(listItem.size() +"éléments"));
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listviewperso) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        int j = item.getItemId();
        switch(j) {
            case 0:
                Log.d("", "Supprimer " + index);
                listItem.remove(index);
                Log.d("Nouvelle taille du bordel: ", new String(listItem.size() +"éléments"));
                removeFromFile(listItem);
                return true;

            default:
                Log.d("", "Error");
                break;
        }

        return false;
    }

    private void removeFromFile(ArrayList<HashMap<String, String>> listItem) {

        File ff = getFileStreamPath("myAgenda.txt");
        boolean deleted = ff.delete();

        for(int a = 0; a < listItem.size(); a++){
            HashMap<String, String>tempMap = listItem.get(a);
            String tempNom = tempMap.get("titre");
            String tempType = tempMap.get("type");
            String tempDate = tempMap.get("date");
            String tempHeure = tempMap.get("time");


            if (ff.length() == 0) {
                //On écrit dedans
                try {
                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(tempNom.getBytes());
                    stream.write("**".getBytes());
                    stream.write(tempType.getBytes());
                    stream.write("**".getBytes());
                    stream.write(tempDate.getBytes());
                    stream.write("**".getBytes());
                    stream.write(tempHeure.getBytes());
                    stream.write("¤¤".getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //On lit le fichier, on stocke ce qu'on a lu, on ajoute à l'arraylist la tache actuelle et on réécrit tout

                int length = (int) file.length();

                byte[] bytes = new byte[length];


                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String contents = new String(bytes);

                try {
                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(contents.getBytes());

                    stream.write(tempNom.getBytes());
                    stream.write("**".getBytes());
                    stream.write(tempType.getBytes());
                    stream.write("**".getBytes());
                    stream.write(tempDate.getBytes());
                    stream.write("**".getBytes());
                    stream.write(tempHeure.getBytes());
                    stream.write("¤¤".getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
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
            case R.id.m_delete: {
                File ff = getFileStreamPath("myAgenda.txt");
                if (ff.length() > 0) {
                    ff.delete();
                    finish();
                    startActivity(getIntent());
                }
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
