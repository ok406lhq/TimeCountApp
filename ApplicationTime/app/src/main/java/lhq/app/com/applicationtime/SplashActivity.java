package lhq.app.com.applicationtime;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import lhq.app.com.applicationtime.Utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    long startTime = System.currentTimeMillis();
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_IO_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;
    private static final int CODE_ENTER_HOME = 4;

    private String mversionName;
    private int mversionCode;
    private String mdesrcption;
    private String mdownloadUrl;
    private HttpURLConnection conn;

    private TextView tv_progress;
    private TextView tv_versionName;
    private RelativeLayout rlRoot;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdatDialog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_IO_ERROR:
                    Toast.makeText(SplashActivity.this, "您未连接到服务器，无法更新版本", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "JSON解析错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };


    private void enterHome() {
        Intent intent = new Intent(SplashActivity.this, AppStatisticsList.class);
        startActivity(intent);
        finish();
    }

    private void showUpdatDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:" + mversionName);
        builder.setMessage(mdesrcption);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
                download();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    private void download() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            tv_progress.setVisibility(View.VISIBLE);
            String target = Environment.getExternalStorageDirectory() + "/update.apk";
            HttpUtils utils = new HttpUtils();
            utils.download(mdownloadUrl, target, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    System.out.println("下载成功");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);//如果用户取消安装的话，会返回结果，回调方法onActivityResult
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度：" + current + "/" + total);
                    tv_progress.setText("下载进度：" + current * 100 / total + "%");
                }
            });
        } else {
            Toast.makeText(SplashActivity.this, "没有找到sd卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_versionName = (TextView) findViewById(R.id.tv_versionName);
        String versionName = getVersionName();
        tv_versionName.setText("当前版本号：" + versionName);
        tv_progress = (TextView) findViewById(R.id.tv_progress);

        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        SharedPreferences mPref = getSharedPreferences("config",MODE_PRIVATE);
        // 判断是否需要自动更新
        boolean autoUpdate = mPref.getBoolean("auto_update",true);
        if (autoUpdate){
            checkVersioh();
        }else {
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME,2000);
        }

        // 渐变的动画效果
        AlphaAnimation anim = new AlphaAnimation(0.3f,1f);
        anim.setDuration(2000);
        rlRoot.startAnimation(anim);
    }

    private void checkVersioh() {
        final Message msg = Message.obtain();
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.0.103:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamUtils.ReadFromStream(is);

                        JSONObject jo = new JSONObject(result);
                        mversionName = jo.getString("versionName");
                        mversionCode = jo.getInt("versionCode");
                        mdesrcption = jo.getString("description");
                        mdownloadUrl = jo.getString("downloadUrl");
                        if (mversionCode > getVersionCode()) {
                            msg.what = CODE_UPDATE_DIALOG;
                        } else {
                            msg.what = CODE_ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = CODE_IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;
                    if (timeUsed < 5000) {
                        try {
                            Thread.sleep(5000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }

    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
