package lhq.app.com.applicationtime;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import lhq.app.com.applicationtime.Chart.DemoBase;
import lhq.app.com.applicationtime.Chart.XYMarkerView;
import lhq.app.com.applicationtime.Statistic.AppInformation;
import lhq.app.com.applicationtime.Statistic.StatisticsInfo;

public class BarChartActivity extends DemoBase implements SeekBar.OnSeekBarChangeListener, OnChartValueSelectedListener {
    protected BarChart mChart;
    private int style = StatisticsInfo.DAY;
    ArrayList<AppInformation> ShowList;
    boolean usehour = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bar_chart);

        Button buttonday = (Button) findViewById(R.id.daybuttonchart2);
        buttonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.DAY) {
                    style = StatisticsInfo.DAY;
                    onResume();
                }
            }
        });
        Button buttonweek = (Button) findViewById(R.id.weekbuttonchart2);
        buttonweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.WEEK) {
                    style = StatisticsInfo.WEEK;
                    onResume();
                }
            }
        });
        Button buttonmonth = (Button) findViewById(R.id.monthbuttonchart2);
        buttonmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.MONTH) {
                    style = StatisticsInfo.MONTH;
                    onResume();
                }
            }
        });
        Button buttonyear = (Button) findViewById(R.id.yearbuttonchart2);
        buttonyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.YEAR) {
                    style = StatisticsInfo.YEAR;
                    onResume();
                }
            }
        });

        Button buttonpie = (Button) findViewById(R.id.PieButton2);
        buttonpie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartActivity.this, PiePolylineChartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button buttonlist = (Button) findViewById(R.id.ListButton2);
        buttonlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartActivity.this, AppStatisticsList.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        System.out.println("Nothing");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar,menu);
        return true;

    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();

        setButtonColor();

        StatisticsInfo statisticsInfo = new StatisticsInfo(this, style);
        ShowList = statisticsInfo.getShowList();
        usehour = getLargerestTime() > 300;

        mChart = (BarChart) findViewById(R.id.barchart);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(60);

        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                int i = (int) v;
                if (ShowList.size() > i) {
                    if (i >= 6) {
                        return "其他应用";
                    } else {
                        String str = ShowList.get(i).getLabel();
                        if (str.length() <= 4) {
                            return str;
                        } else {
                            return (str.substring(0, 3) + "..");
                        }
                    }
                } else return "";
            }
        };
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (usehour) {
                    return (int) v + "hour";
                } else {
                    return (int) v + "min";
                }
            }
        };
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        //需要api24，版本兼容问题
        //要在方法前面加上：@TargetApi(Build.VERSION_CODES.N)
        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        setDataBarChart();

    }


    private void setDataBarChart() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        if (ShowList.size() < 6) {
            for (int i = 0; i < ShowList.size(); i++) {
                float value;
                if (usehour) {
                    value = (float) (1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60 / 60);
                } else {
                    value = (float) (1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60);
                }
                yVals1.add(new BarEntry(i, value));
            }
        } else {
            for (int i = 0; i < 6; i++) {
                float value;
                if (usehour) {
                    value = (float) (1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60 / 60);
                } else {
                    value = (float) (1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60);
                }
                yVals1.add(new BarEntry(i, value));
            }
            long otherTime = 0;
            for (int i = 6; i < ShowList.size(); i++) {
                otherTime += ShowList.get(i).getUsedTimebyDay();
            }
            if (usehour) {
                yVals1.add(new BarEntry(6, (float) (1.0 * otherTime / 1000 / 60 / 60)));
            } else {
                yVals1.add(new BarEntry(6, (float) (1.0 * otherTime / 1000 / 60)));
            }
        }
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Different APPs");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
        mChart.invalidate();
    }


    private double getLargerestTime() {
        double time = 0;
        for (AppInformation appinformation : ShowList) {
            if (1.0 * appinformation.getUsedTimebyDay() / 1000 / 60 > time) {
                time = 1.0 * appinformation.getUsedTimebyDay() / 1000 / 60;
            }
        }
        return time;
    }

    private void setButtonColor() {
        Button buttonday = (Button) findViewById(R.id.daybuttonchart2);
        Button buttonweek = (Button) findViewById(R.id.weekbuttonchart2);
        Button buttonmonth = (Button) findViewById(R.id.monthbuttonchart2);
        Button buttonyear = (Button) findViewById(R.id.yearbuttonchart2);
        Button buttonpie = (Button) findViewById(R.id.PieButton2);
        Button buttonbar = (Button) findViewById(R.id.BarButton2);
        Button buttonlist = (Button) findViewById(R.id.ListButton2);

        buttonday.setTextColor(Color.WHITE);
        buttonweek.setTextColor(Color.WHITE);
        buttonmonth.setTextColor(Color.WHITE);
        buttonyear.setTextColor(Color.WHITE);
        buttonpie.setTextColor(Color.WHITE);
        buttonbar.setTextColor(Color.WHITE);
        buttonlist.setTextColor(Color.WHITE);

        switch (style) {
            case StatisticsInfo.DAY:
                buttonday.setTextColor(Color.GREEN);
                break;

            case StatisticsInfo.WEEK:
                buttonweek.setTextColor(Color.GREEN);
                break;

            case StatisticsInfo.MONTH:
                buttonmonth.setTextColor(Color.GREEN);
                break;

            case StatisticsInfo.YEAR:
                buttonyear.setTextColor(Color.GREEN);
                break;
        }

        String classname = this.getClass().getName();
        if (classname.contains("BarChartActivity")) {
            buttonbar.setTextColor(Color.YELLOW);
        } else if (classname.contains("AppStatisticsList")) {
            buttonlist.setTextColor(Color.YELLOW);
        } else if (classname.contains("PiePolylineChartActivity")) {
            buttonpie.setTextColor(Color.YELLOW);
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.actionToggleValues:{
                for (IDataSet set:mChart.getData().getDataSets()) {
                    set.setDrawValues(!set.isDrawValuesEnabled());

                    mChart.invalidate();
                    break;
                }
            }
            case R.id.actionTogglePinch:{
                if (mChart.isPinchZoomEnabled()){
                    mChart.setPinchZoom(false);
                }else {
                    mChart.setPinchZoom(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax:{
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleBarBorders:{
                for (IBarDataSet set:mChart.getData().getDataSets()) {
                    ((BarDataSet)set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                    mChart.invalidate();
                    break;
                }
            }
            case R.id.animateX:{
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY:{
                mChart.animateY(3000);
                break;
            }
            case R.id.animateXY:{
                mChart.animateXY(3000,3000);
                break;
            }
            case R.id.actionSave:{
                if (mChart.saveToGallery("title"+System.currentTimeMillis(),50)){
                    Toast.makeText(getApplicationContext(),"Saving SUCCESSFUL!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Saving FAILED!",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return true;
    }

    protected RectF mOnValueSelectedRectF = new RectF();
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null) {
            return;
        }

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index", "low:" + mChart.getLowestVisibleX() + ",high:" + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);


    }

    @Override
    public void onNothingSelected() {

    }

}