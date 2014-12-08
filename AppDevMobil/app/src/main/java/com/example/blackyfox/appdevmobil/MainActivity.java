package com.example.blackyfox.appdevmobil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    ListView ll = null;
    ListView ll2 = null;
    public static String VALUE = "com.example.blackyfox.appdevmobil.value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll = (ListView) findViewById(R.id.lv_main);
        ll2 = (ListView) findViewById(R.id.lv_main2);

        final String[] lls = {"test", "coucou", "blabla", "Goudou", "coucou", "blabla", "Goudou",
                "coucou", "blabla", "Goudou", "coucou", "blabla", "Goudou", "coucou", "blabla",
                "Goudou"};
        final String[] ll2s = {"AHAHAHA", "BHBHBHBH", "CHCHCHCHCH", "BHBHBHBH", "CHCHCHCHCH",
                "BHBHBHBH", "CHCHCHCHCH", "BHBHBHBH", "CHCHCHCHCH", "BHBHBHBH", "CHCHCHCHCH",
                "BHBHBHBH", "CHCHCHCHCH", "BHBHBHBH", "CHCHCHCHCH", "BHBHBHBH", "CHCHCHCHCH",
                "BHBHBHBH", "CHCHCHCHCH", "BHBHBHBH", "CHCHCHCHCH"};

        ll.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return lls.length;
            }

            @Override
            public String getItem(int position) {
                return lls[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final String ms = getItem(position);
                if(convertView == null){
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.row, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.tv);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        i.putExtra(VALUE, ms);
                        startActivity(i);
                    }
                });
                tv.setText(ms);
                return convertView;
            }
        });

        ll2.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return ll2s.length;
            }

            @Override
            public Object getItem(int position) {
                return ll2s[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String ms = (String) getItem(position);
                if(convertView == null){
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.row, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.tv);
                tv.setText(ms);
                return convertView;
            }
        });
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

        if (id == R.id.m_help){
            MyDialog md = new MyDialog();
            md.show(getFragmentManager(), "tag_frag");
            /*new AlertDialog.Builder(this)
                    .setTitle("Help")
                    .setMessage("Do you really need some help?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_menu_help)
                    .show();*/
        }

        if (id == R.id.m_home){
            Context context = getApplicationContext();
            CharSequence text = "Already on the home page";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }
    public static class MyDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Test message");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                }
            });
            return builder.create();
        }

    }
}
