package com.programmingmobile.pageviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Antoine on 03/01/2015.
 */
public class MeteoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myfragment_meteo_layout, container, false);

        ImageButton bv = (ImageButton) v.findViewById(R.id.btest2);
        bv.setOnClickListener(meteo_click);

        return v;
    }

    public static MeteoFragment newInstance(String text) {

        MeteoFragment f = new MeteoFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


    private View.OnClickListener meteo_click = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MeteoActivity.class);
            getActivity().startActivity(i);
        }
    };
}