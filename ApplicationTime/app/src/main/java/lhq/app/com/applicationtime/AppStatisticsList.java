package lhq.app.com.applicationtime;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lhq.app.com.applicationtime.Statistic.AppInformation;
import lhq.app.com.applicationtime.Statistic.StatisticsInfo;

public class AppStatisticsList extends AppCompatActivity {
    private int style;
    private long totalTime;
    private int totalTimes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_statistics_list);
        this.style = StatisticsInfo.DAY;

        Button buttonday = (Button) findViewById(R.id.daybuttonlist3);
        buttonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.DAY) {
                    style = StatisticsInfo.DAY;
                    onResume();
                }
            }
        });
        Button buttonweek = (Button) findViewById(R.id.weekbuttonlist3);
        buttonweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.WEEK) {
                    style = StatisticsInfo.WEEK;
                    onResume();
                }
            }
        });
        Button buttonmonth = (Button) findViewById(R.id.monthbuttonlist3);
        buttonmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.MONTH) {
                    style = StatisticsInfo.MONTH;
                    onResume();
                }
            }
        });
        Button buttonyear = (Button) findViewById(R.id.yearbuttonlist3);
        buttonyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style != StatisticsInfo.YEAR) {
                    style = StatisticsInfo.YEAR;
                    onResume();
                }
            }
        });

        Button buttonbar = (Button) findViewById(R.id.BarButton3);
        buttonbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppStatisticsList.this, BarChartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button buttonpie = (Button) findViewById(R.id.PieButton3);
        buttonpie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppStatisticsList.this,PiePolylineChartActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setButtonColor() {
        Button buttonday = (Button) findViewById(R.id.daybuttonlist3);
        Button buttonmonth = (Button) findViewById(R.id.monthbuttonlist3);
        Button buttonyear = (Button) findViewById(R.id.yearbuttonlist3);
        Button buttonweek = (Button) findViewById(R.id.weekbuttonlist3);
        Button buttonpie = (Button) findViewById(R.id.PieButton3);
        Button buttonbar = (Button) findViewById(R.id.BarButton3);
        Button buttonlist = (Button) findViewById(R.id.ListButton3);

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
        } else if (classname.contains("AppStatisticsList")) {
            buttonlist.setTextColor(Color.YELLOW);
        } else if (classname.contains("PiePolylineChartActivity")) {
            buttonpie.setTextColor(Color.YELLOW);
        }
    }

    //每次重新进入界面的时候加载listView
    @Override
    protected void onResume() {
        super.onResume();

        setButtonColor();

        List<Map<String, Object>> datalist = null;

        StatisticsInfo statisticsInfo = new StatisticsInfo(this, this.style);
        totalTime = statisticsInfo.getTotalTime();
        totalTimes = statisticsInfo.getTotalTimes();
        datalist = getDataList(statisticsInfo.getShowList());

        ListView listView = (ListView) findViewById(R.id.AppStatisticsList);
        SimpleAdapter adapter = new SimpleAdapter(this, datalist, R.layout.inner_list,
                new String[]{"label", "info", "times", "icon"},
                new int[]{R.id.label, R.id.info, R.id.times, R.id.icon});
        listView.setAdapter(adapter);

        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else return false;
            }
        });

//      TextView textView = (TextView)findViewById(R.id.text1);
//      textView.setText("运行总时间 ：" + DateUtils.formatElapsedTime(titalTime / 1000));

    }

    private List<Map<String, Object>> getDataList(ArrayList<AppInformation> showList) {
        List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("label", "全部应用");
        map.put("info", "运行时间" + DateUtils.formatElapsedTime(totalTime / 1000));
        map.put("times", "本次开机操作次数:" + totalTimes);
        map.put("icon", R.drawable.use);
        datalist.add(map);

        for (AppInformation appInformation : showList
                ) {
            map = new HashMap<String, Object>();
            map.put("label", appInformation.getLabel());
            map.put("info", "运行时间" + DateUtils.formatElapsedTime(appInformation.getUsedTimebyDay() / 1000));
            map.put("times", "本次开机操作次数:" + appInformation.getTimes());
            map.put("icon", appInformation.getIcon());
            datalist.add(map);
        }

        return datalist;
    }
}
