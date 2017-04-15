package lhq.app.com.applicationtime.Statistic;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Created by lhq on 2017/3/31 0031.
 *
 * 应用信息工具类，可以使用此类对应用信息进行统计
 *
 */

public class AppInformation {
    private UsageStats usageStats;  // UsageStats-包含特定时间范围内应用程式包的使用统计资料
    private String packageName; // 包名
    private String label;   // 标签
    private Drawable Icon;  // 应用图标
    private long UsedTimebyDay; //毫秒
    private Context context;
    private int times; // 启动次数

    public AppInformation(UsageStats usageStats, Context context) {
        this.usageStats = usageStats;
        this.context = context;

        try {
            // 生成应用信息
            GenerateInfo();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    // 生成应用信息方法GenerateInfo()
    private void GenerateInfo() throws PackageManager.NameNotFoundException, NoSuchFieldException, IllegalAccessException {
        //获得包管理器PackageManager对象
        PackageManager packageManager = context.getPackageManager();
        //通过usageStats获得包名
        this.packageName = usageStats.getPackageName();
        //若packName不为空,通过包管理器对象给label，UsedTimebyDay，times，Icon赋值
        if (this.packageName != null && !this.packageName.equals("")) {
            // 获得ApplicationInfo对象
            // getApplicationInfo(String packageName, int flags):检索关于特定包/应用程序的所有信息
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.packageName, PackageManager. SIGNATURE_MATCH );
            this.label = (String) packageManager.getApplicationLabel(applicationInfo);
            //getTotalTimeInForeground():获取此软件包在前台的总时间，以毫秒为单位
            this.UsedTimebyDay = usageStats.getTotalTimeInForeground();
            this.times = (int) usageStats.getClass().getDeclaredField("mLaunchCount").get(usageStats);

            if (this.UsedTimebyDay > 0) {
                this.Icon = applicationInfo.loadIcon(packageManager);
            }
        }
    }

    public UsageStats getUsageStats() {
        return usageStats;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setUsedTimebyDay(long usedTimebyDay) {
        this.UsedTimebyDay = usedTimebyDay;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public long getUsedTimebyDay() {
        return UsedTimebyDay;
    }

    public String getLabel() {
        return label;
    }

    public String getPackageName() {
        return packageName;
    }

}
