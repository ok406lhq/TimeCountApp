package lhq.app.com.applicationtime.Chart;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import lhq.app.com.applicationtime.R;

/**
 * Created by lhq on 2017/4/6 0006.
 *
 * 演示应用的所有活动的基础类
 *
 */

public abstract class DemoBase extends FragmentActivity {
    private String[] mMouths = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    protected String[] mParties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"};

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Typeface类指定字体的字体和固有样式
        //public static Typeface createFromAsset (AssetManager mgr, String path)该方法用于将字体数据加载到typeface对象中返回
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

//    protected float getRandom(float range, float startfrom) {
//        return (float) (Math.random() * range) + startfrom;
//    }

    //设置滑动切换页面
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity,R.anim.move_left_out_activity);
    }

    public abstract void onValueSelected(Entry entry, int i, Highlight highlight);
}
