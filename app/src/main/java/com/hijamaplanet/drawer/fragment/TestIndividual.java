package com.hijamaplanet.drawer.fragment;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hijamaplanet.FontClass;
import com.hijamaplanet.MainActivity;
import com.hijamaplanet.R;
import com.hijamaplanet.admin.activity.AdminView;
import com.hijamaplanet.admin.activity.MasterAdminView;
import com.hijamaplanet.drawer.DuaActivity;
import com.hijamaplanet.drawer.QuestionLibrary;
import com.hijamaplanet.drawer.StepProgressBar;
import com.hijamaplanet.user.UserView;

import static com.hijamaplanet.BaseActivity.drawer;

public class TestIndividual extends Fragment {

    private Typeface custom_font2;
    private QuestionLibrary mQuestionLibrary;
    private TextView nResultView;
    private TextView mQuestionView;
    private Button buttonYes;
    private Button buttonNo;
    private Button buttonSometimes;
    private Button buttonDontKnow;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private TextView score,result,testAgain;

    private Double currentResult = 0.0;
    private int mQuestionNumber = 0;
    StepProgressBar mStepProgressBar;
    /*private LineChart chart;
    private PieChart pieChart;
    List<Entry> entries;
    ArrayList<Integer> stepCounter;
    private float[] yData;
    private String[] xData;
    int yesInt=0,noInt=0,someInt=0,DnKnow=0;*/

    String mQuestions[];
    int choiceValue[][];

    int pStatus = 0;
    private Handler handler = new Handler();
    private ProgressBar mProgress;
    private int resScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.nav_test_indivudual,container,false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("সমস্যা যাচাই");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mQuestions = bundle.getStringArray("question");
            choiceValue = (int[][]) bundle.getSerializable("value");
        }

        FontClass fontClass = new FontClass(getActivity());
        custom_font2 = fontClass.getBoldFont();

        mQuestionView =view.findViewById(R.id.question);
        buttonYes = view.findViewById(R.id.buttonYes);
        buttonNo =view.findViewById(R.id.buttonNo);
        buttonSometimes =view.findViewById(R.id.buttonSometimes);
        buttonDontKnow = view.findViewById(R.id.buttonDontKnow);
        frameLayout=view.findViewById(R.id.frameLayout);
        linearLayout=view.findViewById(R.id.linLayout);
        score=view.findViewById(R.id.score);
        result=view.findViewById(R.id.result);
        mStepProgressBar = view.findViewById(R.id.stepProgressBar);
        mStepProgressBar.setNumDots(mQuestions.length);
        testAgain=view.findViewById(R.id.testAgain);
        /*chart = view.findViewById(R.id.chart);
        pieChart = view.findViewById(R.id.pieChart);*/

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        mProgress = view.findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        /*entries = new ArrayList<Entry>();
        stepCounter=new ArrayList<>();
        yData = new float[4];*/

        updateQuestion();

        //Button Yes
        buttonYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentResult = currentResult +  choiceValue[mQuestionNumber-1][0];
                mStepProgressBar.next();
                /*yesInt++;
                yData[0]=(float) yesInt;
                stepCounter.add(0);*/
                updateQuestion();

                //entries.add(new Entry(mQuestionNumber-1,1));
                /*buttonYes.setTextColor(getResources().getColor(R.color.white));
                buttonYes.setBackground(getResources().getDrawable(R.drawable.background_shape_preset_button__pressed));
                buttonNo.setTextColor(getResources().getColor(R.color.black));
                buttonNo.setBackground(getResources().getDrawable(R.drawable.background_shape_preset_time_button));
                buttonSometimes.setTextColor(getResources().getColor(R.color.black));
                buttonSometimes.setBackground(getResources().getDrawable(R.drawable.background_shape_preset_time_button));
                buttonDontKnow.setTextColor(getResources().getColor(R.color.black));
                buttonDontKnow.setBackground(getResources().getDrawable(R.drawable.background_shape_preset_time_button));*/
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentResult = currentResult + choiceValue[mQuestionNumber-1][1];
                mStepProgressBar.next();
                /*stepCounter.add(1);
                noInt++;
                yData[1]=(float) noInt;*/
                updateQuestion();
                //entries.add(new Entry(mQuestionNumber-1,2));
            }
        });

        buttonSometimes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentResult = currentResult + choiceValue[mQuestionNumber-1][2];
                mStepProgressBar.next();
                /*stepCounter.add(2);
                someInt++;
                yData[2]=(float) someInt;*/
                updateQuestion();
                //entries.add(new Entry(mQuestionNumber-1,3));
            }
        });

        buttonDontKnow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentResult = currentResult + choiceValue[mQuestionNumber-1][3];
                mStepProgressBar.next();
                /*stepCounter.add(3);
                DnKnow++;
                yData[3]=(float) DnKnow;*/
                updateQuestion();
                //entries.add(new Entry(mQuestionNumber-1,4));
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            drawer.openDrawer(Gravity.START);
            return true;
        }
        return false;
    }

    private void updateQuestion(){

        if (mQuestionNumber == mQuestions.length){

            linearLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            result.setText("আপনার আক্রান্ত হওয়ার সম্ভাবনাঃ");
            result.setTypeface(custom_font2);
            Double finalScore = (currentResult/(mQuestionNumber*10))*100;
            resScore = (int) Math.round(finalScore);
            score.setTypeface(custom_font2);
            testAgain.setTypeface(custom_font2);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (pStatus < resScore) {
                        pStatus += 1;

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                mProgress.setProgress(pStatus);
                                score.setText(pStatus + "%");

                            }
                        });
                        try {
                            // Sleep for 200 milliseconds.
                            // Just to display the progress slowly
                            Thread.sleep(24); //thread will take approx 3 seconds to finish
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            testAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   getFragmentManager().popBackStackImmediate();
                }
            });

            /*final ArrayList<String> answers = new ArrayList();
            answers.add("Yes");
            answers.add("No");
            answers.add("Sometimes");
            answers.add("Don't Know");

            final ArrayList<Integer> numbers =new ArrayList<>();
            for (int i = 0; i <= mQuestionNumber; i++)
                numbers.add(i+1);

            XAxis xl = chart.getXAxis();
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setDrawAxisLine(true);
            xl.setLabelCount(mQuestionNumber);
            xl.setDrawGridLines(false);
            if (mQuestionNumber>20)
                xl.setGranularity(2.0f);
            else
                xl.setGranularity(1.0f);
            xl.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return ""+numbers.get((int)value);
                }
            });

            YAxis yl = chart.getAxisLeft();
            //yl.setTypeface(mTfLight);
            yl.setDrawAxisLine(true);
            yl.setDrawGridLines(true);
            yl.setAxisMinimum(0f);// this replaces setStartAtZero(true)
//        yl.setInverted(true);
            yl.setGranularity(1.0f);
            yl.setAxisMaximum(3);
            yl.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return answers.get((int)value);
                }
            });

            YAxis yr = chart.getAxisRight();
            yr.setDrawLabels(false);

            for (int i = 0 ; i < mQuestionNumber ; i++){
                entries.add(new Entry(i,stepCounter.get(i)));
            }

            chart.animateY(1000);
            Description description = new Description();
            description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[1]);
            description.setText("Questions Number → ");
            chart.setDescription(description);

            LineDataSet dataSet = new LineDataSet(entries, "Question & Answer Interaction → ");

            dataSet.setDrawCircleHole(false);
            dataSet.setCircleRadius(2f);

            dataSet.setDrawValues(false);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(dataSet);

            LineData lineData = new LineData(dataSets);
            lineData.setValueFormatter(new IntegerValueFormatter());
            chart.setData(lineData);
            chart.invalidate();



            xData = new String[]{"Yes", "No", "SomeTimes", "Don't Know"};

            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5, 10, 5, 5);

            pieChart.setDragDecelerationFrictionCoef(0.95f);

            pieChart.setCenterTextTypeface(adventSemi);
            pieChart.setCenterText(resScore+"%");

            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);

            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);

            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);

            pieChart.setDrawCenterText(true);

            pieChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);*/

            // mChart.setUnit(" €");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener
            //pieChart.setOnChartValueSelectedListener(getActivity());

            /*setData(4, 100);

            pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            // mChart.spin(2000, 0, 360);
            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            pieChart.setEntryLabelColor(Color.WHITE);
            pieChart.setEntryLabelTypeface(adventSemi);
            pieChart.setEntryLabelTextSize(12f);*/


            /*pieChart.setRotationEnabled(true);
            pieChart.setHoleRadius(40f);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.setCenterText("Answers Chart");
            pieChart.setCenterTextSize(7);
            addDataSet();
            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    int pos1 = e.toString().indexOf("(sum): ");
                    String sales = e.toString().substring(pos1 + 7);
                    for(int i = 0; i < yData.length; i++){
                        if(yData[i] == Float.parseFloat(sales)){
                            pos1 = i;
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            }); */

        }else {
            mQuestionView.setText(mQuestions[mQuestionNumber]);
            mQuestionNumber++;
        }
    }

    /*class IntegerValueFormatter implements IValueFormatter
    {
        private DecimalFormat mFormat;

        public IntegerValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry(yData[i],
                    xData[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Answers Selection");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(adventSemi);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private void addDataSet() {
        //Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Answers");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }*/
}
