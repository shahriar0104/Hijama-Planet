package com.hijamaplanet.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hijamaplanet.R;

import java.util.List;

public class SpinnerUtil {

    public static void showSpinner(Context context, Spinner spinner, List<String> data) {
        ArrayAdapter<String> genAdapter =
                new ArrayAdapter<String>(context, R.layout.z_spinner_item, data) {
                    @Override
                    public boolean isEnabled(int position) {
                        return true;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
        genAdapter.setDropDownViewResource(R.layout.z_spinner_item);
        spinner.setAdapter(genAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //(String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
