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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class NewTaskActivity extends ActionBarActivity {

    private EditText nom;
    private Spinner type;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private Button add,submit, time_btn;
    private TimePicker timePicker;


    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    Context context;
    String path;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        context = getApplicationContext();

        path = context.getFilesDir().getAbsolutePath();
        file = new File(path + "/myAgenda.txt");

        nom = (EditText)findViewById(R.id.nom);
        type = (Spinner)findViewById(R.id.type);
        add = (Button) findViewById(R.id.button1);
        time_btn = (Button) findViewById(R.id.button2);
        submit = (Button) findViewById(R.id.submit);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        showDate(year, month, day);
        showTime(hour, minute);
        add.setOnClickListener(date);
        time_btn.setOnClickListener(time);
        submit.setOnClickListener(badd_click);
    }

    private View.OnClickListener date = new View.OnClickListener() {
        public void onClick(View v) {
            setDate(v);
        }
    };

    private View.OnClickListener time = new View.OnClickListener() {
        public void onClick(View v) {
            setTime(v);
        }
    };

    private View.OnClickListener badd_click = new View.OnClickListener() {
        public void onClick(View v) {

            //Etablissement d'une communicaton entre deux activity
            Intent i;
            i = new Intent(NewTaskActivity.this, AgendaActivity.class);
            String hr_s, min_s, day_s, month_s;

            File f = getFileStreamPath("myAgenda.txt");

            if(nom.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), "Nommez la tache", Toast.LENGTH_LONG).show();
            }
            else {
                if (f.length() == 0) {
                    //On écrit dedans
                    try {
                        FileOutputStream stream = new FileOutputStream(file);
                        stream.write(nom.getText().toString().getBytes());
                        stream.write("**".getBytes());
                        stream.write(type.getSelectedItem().toString().getBytes());
                        stream.write("**".getBytes());

                        month += 1;
                        if (day < 10)
                            day_s = new String("0" + day);
                        else
                            day_s = new String(Integer.toString(day));
                        if (month < 10) {
                            month_s = new String("0" + month);
                        } else {
                            month_s = new String(Integer.toString(month));
                        }
                        String date = new String(day_s + "/" + month_s + "/" + year);
                        stream.write(date.getBytes());
                        stream.write("**".getBytes());

                        if (hour < 10)
                            hr_s = new String("0" + hour);
                        else
                            hr_s = new String(Integer.toString(hour));
                        if (minute < 10)
                            min_s = new String("0" + minute);
                        else
                            min_s = new String(Integer.toString(minute));
                        String time = new String(hr_s + ":" + min_s);
                        stream.write(time.getBytes());
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

                        stream.write(nom.getText().toString().getBytes());
                        stream.write("**".getBytes());

                        stream.write(type.getSelectedItem().toString().getBytes());
                        stream.write("**".getBytes());

                        month += 1;
                        if (day < 10)
                            day_s = new String("0" + day);
                        else
                            day_s = new String(Integer.toString(day));
                        if (month < 10) {
                            month_s = new String("0" + month);
                        } else {
                            month_s = new String(Integer.toString(month));
                        }
                        String date = new String(day_s + "/" + month_s + "/" + year);
                        stream.write(date.getBytes());
                        stream.write("**".getBytes());

                        if (hour < 10)
                            hr_s = new String("0" + hour);
                        else
                            hr_s = new String(Integer.toString(hour));
                        if (minute < 10)
                            min_s = new String("0" + minute);
                        else
                            min_s = new String(Integer.toString(minute));
                        String time = new String(hr_s + ":" + min_s);
                        stream.write(time.getBytes());
                        stream.write("¤¤".getBytes());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                //Envoi des informations à travers l'intent grâce à putExtra
                i.putExtra("name", nom.getText().toString());
                i.putExtra("type", type.getSelectedItem().toString());
                i.putExtra("hour", hour);
                i.putExtra("min", minute);
                i.putExtra("day", day);
                i.putExtra("month", month);
                i.putExtra("year", year);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
    };
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(998);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if (id == 998) {
            return new TimePickerDialog(this, myTimeListener, hour, minute, true);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            showTime(arg1, arg2);

        }

    };

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2, arg3);
        }
    };

    private void showTime(int hour, int min) {
        this.hour = hour;
        this.minute = min;
        String hr_s, min_s;
        if(hour < 10)
            hr_s = new String("0"+hour);
        else
            hr_s = new String(Integer.toString(hour));
        if(min < 10)
            min_s = new String("0"+min);
        else
            min_s = new String(Integer.toString(min));

        time_btn.setText(new StringBuilder().append(hr_s).append(":").append(min_s));
    }

    private void showDate(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
        String day_s, month_s;
        if(day < 10)
            day_s = new String("0"+day);
        else
            day_s = new String(Integer.toString(day));
        if(month < 9) {
            month += 1;
            month_s = new String("0" + month);
        }
        else {
            month+=1;
            month_s = new String(Integer.toString(month));
        }
        add.setText(new StringBuilder().append(day_s).append("/").append(month_s).append("/").append(year));
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
        switch (id) {
            case R.id.action_settings: {
                return true;
            }
            case R.id.m_home: {
                if (this.getClass().getSimpleName().matches("MainActivity")) {
                    CharSequence text = "Already on the home page";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                } else {
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
        public Dialog onCreateDialog(Bundle savedInstanceState) {
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
