package com.esiea.dtd.daytoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class MyCalendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types and number of parameters
    public static MyCalendarFragment newInstance() {
        MyCalendarFragment fragment = new MyCalendarFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public MyCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_calendar, container, false);
        ImageButton orga = (ImageButton)v.findViewById(R.id.organizer);
        orga.setOnClickListener(click);
        return v;
    }

    private View.OnClickListener click = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Calendrier", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), AgendaActivity.class);
            getActivity().startActivity(i);
        }
    };
}
