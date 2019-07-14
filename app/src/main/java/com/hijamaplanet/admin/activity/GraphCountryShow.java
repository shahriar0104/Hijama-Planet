package com.hijamaplanet.admin.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import com.hijamaplanet.R;
import com.hijamaplanet.admin.adapter.GraphCountryAdapter;
import com.hijamaplanet.service.network.model.LocationData;
import com.hijamaplanet.service.parser.TaskListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphCountryShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_graph_country_show);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Country List");

        List<LocationData> locationData = getIntent().getParcelableArrayListExtra("location");
        Collections.sort(locationData, (LocationData o1, LocationData o2) -> {
            try {
                return o1.getPlace().split(",")[1].compareToIgnoreCase(o2.getPlace().split(",")[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                return o1.getPlace().compareToIgnoreCase(o2.getPlace());
            }
        });

        ArrayList<String> CountryList = new ArrayList<>();
        for (int i = 0; i < locationData.size(); i++) {
            try {
                if (i==0) CountryList.add(locationData.get(i).getPlace().split(",")[1]);
                else {
                    if (!locationData.get(i).getPlace().split(",")[1].equalsIgnoreCase(locationData.get(i-1).getPlace().split(",")[1]))
                        CountryList.add(locationData.get(i).getPlace().split(",")[1]);
                    else continue;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                if (i==0) CountryList.add(locationData.get(i).getPlace());
                else {
                    if (!locationData.get(i).getPlace().equalsIgnoreCase(locationData.get(i-1).getPlace()))
                        CountryList.add(locationData.get(i).getPlace());
                    else continue;
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.countryList);
        GraphCountryAdapter adapter = new GraphCountryAdapter(this,CountryList,locationData);
        recyclerView.setAdapter(adapter);
    }
}
