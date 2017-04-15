package lhq.app.com.applicationtime;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.OpenButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!isStatAccessPermissionSet(MainActivity.this)){
                        startActivity(new Intent((Settings.ACTION_USAGE_ACCESS_SETTINGS)));//查看是否为应用设置了权限
                        Toast.makeText(getApplicationContext(),"请开启应用统计的使用权限",Toast.LENGTH_SHORT).show(); // 显示toast信息
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if ((isStatAccessPermissionSet(this))){
                Intent intent3 = new Intent(MainActivity.this,SplashActivity.class);
                startActivity(intent3);
                finish();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isStatAccessPermissionSet(Context c) throws PackageManager.NameNotFoundException {
        //获得包管理器并且从中得到ApplicationInfo对象
        PackageManager pm = c.getPackageManager();
        ApplicationInfo info = pm.getApplicationInfo(c.getPackageName(), 0);
        AppOpsManager aom = (AppOpsManager) c.getSystemService(Context.APP_OPS_SERVICE);
        /**
         *
         * public int checkOpNoThrow (String op, int uid, String packageName):快速检查应用是否可以执行操作
         * param：op-检查操作，这里给出的OPSTR_GET_USAGE_STATS表示访问UsageStatsManager。
         * param：uid-理解为一种标识就可以了，应用的标识
         * param：packageName-应用包名
         * return：int(抛异常时会返回一个MODE_ERROR，相当于抛出一个SecurityException)
         */
        int i = aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
        return i == AppOpsManager.MODE_ALLOWED;
    }
}
