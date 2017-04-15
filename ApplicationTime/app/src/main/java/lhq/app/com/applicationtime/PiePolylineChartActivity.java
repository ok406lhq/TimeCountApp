package lhq.app.com.applicationtime;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import lhq.app.com.applicationtime.Chart.DemoBase;
import lhq.app.com.applicationtime.Statistic.AppInformation;
import lhq.app.com.applicationtime.Statistic.StatisticsInfo;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class PiePolylineChartActivity extends DemoBase implements OnChartValueSelectedListener {

    private PieChart mChart;
    private int style = StatisticsInfo.DAY;
    private long totaltime;

    private Typeface tf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pie_polyline_chart);

        Button buttonday = (Button) findViewById(R.id.daybuttonchart1);
        buttonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.DAY) {
                    style = StatisticsInfo.DAY;
                    onResume();
                }
            }
        });
        Button buttonweek = (Button) findViewById(R.id.weekbuttonchart1);
        buttonweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.WEEK) {
                    style = StatisticsInfo.WEEK;
                    onResume();
                }
            }
        });
        Button buttonmonth = (Button) findViewById(R.id.monthbuttonchart1);
        buttonmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.MONTH) {
                    style = StatisticsInfo.MONTH;
                    onResume();
                }
            }
        });
        Button buttonyear = (Button) findViewById(R.id.yearbuttonchart1);
        buttonyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.YEAR) {
                    style = StatisticsInfo.YEAR;
                    onResume();
                }
            }
        });

        Button buttonbar = (Button) findViewById(R.id.BarButton1);
        buttonbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PiePolylineChartActivity.this, BarChartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button buttonlist = (Button) findViewById(R.id.ListButton1);
        buttonlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PiePolylineChartActivity.this, AppStatisticsList.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setButtonColor() {
        Button buttonday = (Button) findViewById(R.id.daybuttonchart1);
        Button buttonmonth = (Button) findViewById(R.id.monthbuttonchart1);
        Button buttonyear = (Button) findViewById(R.id.yearbuttonchart1);
        Button buttonweek = (Button) findViewById(R.id.weekbuttonchart1);
        Button buttonpie = (Button) findViewById(R.id.PieButton1);
        Button buttonbar = (Button) findViewById(R.id.BarButton1);
        Button buttonlist = (Button) findViewById(R.id.ListButton1);

        buttonday.setTextColor(Color.WHITE);
        buttonmonth.setTextColor(Color.WHITE);
        buttonweek.setTextColor(Color.WHITE);
        buttonyear.setTextColor(Color.WHITE);
        buttonbar.setTextColor(Color.WHITE);
        buttonpie.setTextColor(Color.WHITE);
        buttonlist.setTextColor(Color.WHITE);

        switch (style) {
            case StatisticsInfo.DAY:
                buttonday.setTextColor(Color.GREEN);
                break;
            case StatisticsInfo.MONTH:
                buttonmonth.setTextColor(Color.GREEN);
                break;
            case StatisticsInfo.WEEK:
                buttonweek.setTextColor(Color.GREEN);
                break;
            case StatisticsInfo.YEAR:
                buttonyear.setTextColor(Color.GREEN);
                break;
        }

        String classname = this.getClass().getName();
        if (classname.contains("BarChartActivity")) {
            buttonbar.setTextColor(Color.YELLOW);
        } else if (classname.contains("AppStatistcList")) {
            buttonlist.setTextColor(Color.YELLOW);
        } else if (classname.contains("PiePolyChartActivity")) {
            buttonpie.setTextColor(Color.YELLOW);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setButtonColor();

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf"));
        mChart.setCenterText(generateCenterSpannableText(style));

        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.YELLOW);

        mChart.setTransparentCircleColor(Color.YELLOW);
        mChart.setTransparentCircleAlpha(110);

        mChart.setEntryLabelColor(R.color.dimgrey);

        // 设置内圈半径的角度
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setOnChartValueSelectedListener(this);

        setDate(style);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet<?> set : mChart.getData().getDataSets()) {
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }
                mChart.invalidate();
                break;
            }

            case R.id.actionToggleHole: {
                if (mChart.isDrawHoleEnabled()) {
                    mChart.setDrawHoleEnabled(false);
                } else {
                    mChart.setDrawHoleEnabled(true);
                }
                mChart.invalidate();
                break;
            }

            case R.id.actionDrawCenter: {
                if (mChart.isDrawCenterTextEnabled()) {
                    mChart.setDrawCenterText(false);
                } else {
                    mChart.setDrawCenterText(true);
                }
                mChart.invalidate();
                break;
            }

            case R.id.actionToggleXVals: {
                mChart.setDrawEntryLabels(!mChart.isDrawCenterTextEnabled());
                mChart.invalidate();
                break;
            }

            case R.id.actionSave: {
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }

            case R.id.actionTogglePercent: {
                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
                mChart.invalidate();
                break;
            }

            case R.id.animateX: {
                mChart.animateX(1400);
                break;
            }

            case R.id.animateY: {
                mChart.animateY(1400);
                break;
            }

            case R.id.animateXY: {
                mChart.animateXY(140, 1400);
                break;
            }
        }
        return true;
    }

    private void setDate(int style) {
        StatisticsInfo statisticsInfo = new StatisticsInfo(this, style);
        ArrayList<AppInformation> showList = statisticsInfo.getShowList();

        totaltime = statisticsInfo.getTotalTime();
        TextView textView = (TextView) findViewById(R.id.textViewchart);

        SpannableString sp = new SpannableString("已使用总时间: " + DateUtils.formatElapsedTime(totaltime / 1000));
        sp.setSpan(new RelativeSizeSpan(1.35f), 0, sp.length(), 0);
        sp.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, sp.length(), 0);
        textView.setText(sp);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        if (showList.size() < 6) {
            for (int i = 0; i < showList.size(); i++) {
                float apptimes = (float) showList.get(i).getUsedTimebyDay() / 1000;
                if (apptimes / totaltime * 1000 >= 0.001) {
                    entries.add(new PieEntry(apptimes, showList.get(i).getLabel()));
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                float apptime = (float) showList.get(i).getUsedTimebyDay() / 1000;
                if (apptime / totaltime * 1000 >= 0.001) {
                    entries.add(new PieEntry(apptime, showList.get(i).getLabel()));
                }
            }
            long othertime = 0;
            for (int i = 6; i < showList.size(); i++) {
                othertime += showList.get(i).getUsedTimebyDay() / 1000;
            }
            if (1.0*othertime / totaltime * 1000 >= 0.001){
                entries.add(new PieEntry((float) othertime,"其他应用"));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries,"Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c: ColorTemplate.VORDIPLOM_COLORS)
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

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();

    }

    private SpannableString generateCenterSpannableText(int style) {

        String s1 = "应用数据统计";
        String s2;
        if (style == StatisticsInfo.WEEK) {
            s2 = "一周内应用使用情况";
        } else if (style == StatisticsInfo.MONTH)
            s2 = "30天应用使用情况";
        else if (style == StatisticsInfo.YEAR)
            s2 = "一年应用使用情况";
        else s2 = "当天应用使用情况";

        SpannableString s = new SpannableString(s1 + "\n" + s2);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, s1.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - s2.length(), s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        if (entry == null)
            return;
        Log.i("VAL SELECTED", "Value: " + entry.getY() + ",xIndex:" + entry.getX() + ",DataSet index:" + highlight.getDataSetIndex());
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected: ");
    }
}
