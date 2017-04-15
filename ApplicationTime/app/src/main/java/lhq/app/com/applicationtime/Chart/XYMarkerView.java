package lhq.app.com.applicationtime.Chart;

import android.content.Context;
import android.os.Build;
import java.text.DecimalFormat;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import lhq.app.com.applicationtime.R;

/**
 * Created by lhq on 2017/4/7 0007.
 *
 * 该类的作用是显示图形的高亮特性
 *
 */

public class XYMarkerView extends MarkerView {
    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void refreshContent(Entry e, Highlight h){
        tvContent.setText("x:"+xAxisValueFormatter.getFormattedValue(e.getX(),null)+", y: "+format.format(e.getY()));

        super.refreshContent(e,h);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth()/2),-getHeight());
    }
}
