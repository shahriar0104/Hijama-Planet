package com.hijamaplanet.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hijamaplanet.R;
import com.hijamaplanet.service.parser.FetchLocationStatus;
import com.hijamaplanet.service.parser.TaskListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphShow extends AppCompatActivity {

    private ArrayList<String> district = new ArrayList<>();
    private ArrayList<Integer> userCount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_graph_show);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("User Statistics");

        Intent intent = getIntent();
        district  = intent.getStringArrayListExtra("district");
        userCount = intent.getIntegerArrayListExtra("userCount");
        graph();
    }

    private void graph() {
        int maxValue = Collections.max(userCount);
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= maxValue; i++) {
            numbers.add(i);
        }

        HorizontalBarChart mChart = findViewById(R.id.horizontalChart);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setLabelCount(district.size());
        xl.setDrawGridLines(false);
        xl.setGranularity(1.0f);
        xl.setValueFormatter((value, axis) -> district.get((int) value));

        YAxis yl = mChart.getAxisLeft();
        //yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);
        //yl.setInverted(true);
        yl.setGranularity(1.0f);
        yl.setAxisMaximum(maxValue);
        yl.setValueFormatter((value, axis) -> "" + numbers.get((int) value));

        YAxis yr = mChart.getAxisRight();
        //yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);
        //yr.setInverted(true);
        yr.setGranularity(1.0f);
        yr.setAxisMaximum(maxValue);
        yr.setValueFormatter((value, axis) -> "" + numbers.get((int) value));

        //setData(12, 50);
        mChart.setFitBars(true);
        mChart.animateY(1500);

        // setting data
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < userCount.size(); i++) {
            barEntries.add(new BarEntry(i, userCount.get(i)));
        }
        BarDataSet dataSet = new BarDataSet(barEntries, "Number of Users");

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new IntegerValueFormatter());
        data.setValueTextSize(10f);
        data.setBarWidth(0.6f);
        mChart.setData(data);
        mChart.invalidate();
    }

    class IntegerValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public IntegerValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}
