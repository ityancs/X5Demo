package com.gekson.x5demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gekson.x5demo.utils.FirstLoadingX5Service;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.QbSdk.PreInitCallback;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    public static boolean firstOpening = true;
    private boolean isQbSdkLoaded;

    // TODO: See init() for initialization!
    // TODO: Title -- Add other items here in strings.xml
    private static String[] titles = null;

    /**
     * {@value TIPS_SHOW the handler msg to show tips}
     */
    private static final int TIPS_SHOW = 0;
    private static final int POP_WINDOW_SHOW = 1;
    public static final int MSG_WEBVIEW_CONSTRUCTOR = 1;
    public static final int MSG_WEBVIEW_POLLING = 2;

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    // add constant here
    private static final int FILE_CHOOSER = 0;
    private static final int FULL_SCREEN_VIDEO = 1;
    // private static final int TBS_COOKIE=2;
    private static final int JAVA_TO_JS = 2;
    private static final int TBS_WEB = 3;
    private static final int TBS_VIDEO = 4;
    private static final int TBS_IMAGE = 5;
    // private static final int TBS_DB=7;
    private static final int TBS_FIRST_X5 = 6;
    private static final int TBS_NEW_WINDOW = 7;
    private static final int SYS_WEB = 8;
    private static final int TBS_FLASH = 9;
    // private static final int TBS_WEB_NOTICE=12;
    // private static final int TBS_BUILDING=13;
    private static final int TBS_LONG_PRESS = 10;
    private static final int TBS_OVER_SCROLL = 11;
    private static final int TBSS_SMALL_QB = 12;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // for view init
    private Context mContext = null;
    private SimpleAdapter gridAdapter;
    private GridView gridView;
    private ArrayList<HashMap<String, Object>> items;

    private static boolean main_initialized = false;

    private volatile boolean isX5WebViewEnabled = false;

    Handler handler;

    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Activity OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_advanced);
        mContext = this;
        if (!main_initialized) {
            this.new_init();
        }
        preinitX5WebCore();
        preinitX5WithService();// 此方法必须在非主进程执行才会有效果
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Activity OnResume
    @Override
    protected void onResume() {
        this.new_init();

        // this.gridView.setAdapter(gridAdapter);
        super.onResume();
    }

    //////////////////////////////////////////////////////////////////////////////////
    // initiate new UI content
    private void new_init() {
        items = new ArrayList<HashMap<String, Object>>();
        this.gridView = (GridView) this.findViewById(R.id.item_grid);

        if (gridView == null)
            throw new IllegalArgumentException("the gridView is null");

        titles = getResources().getStringArray(R.array.index_titles);
        int[] iconResourse = {R.drawable.filechooser, R.drawable.fullscreen, R.drawable.jsjava, R.drawable.tbsweb,
                R.drawable.tbsvideo, R.drawable.imageselect, R.drawable.firstx5, R.drawable.webviewtransport,
                R.drawable.sysweb, R.drawable.flash, R.drawable.longclick, R.drawable.refresh, R.drawable.refresh,};

        HashMap<String, Object> item = null;
        // HashMap<String, ImageView> block = null;
        for (int i = 0; i < titles.length; i++) {
            item = new HashMap<String, Object>();

            item.put("title", titles[i]);
            item.put("icon", iconResourse[i]);

            items.add(item);
        }
        this.gridAdapter = new SimpleAdapter(this, items, R.layout.function_block, new String[]{"title", "icon"},
                new int[]{R.id.Item_text, R.id.Item_bt});
        if (null != this.gridView) {
            this.gridView.setAdapter(gridAdapter);
            this.gridAdapter.notifyDataSetChanged();
            this.gridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> gridView, View view, int position, long id) {
                    HashMap<String, String> item = (HashMap<String, String>) gridView.getItemAtPosition(position);

                    String current_title = item.get("title");

                    Intent intent = null;
                    switch (position) {
                        case FILE_CHOOSER: {
                            intent = new Intent(MainActivity.this, FilechooserActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;
                        case FULL_SCREEN_VIDEO: {
                            intent = new Intent(MainActivity.this, FullScreenActivity.class);
                            MainActivity.this.startActivity(intent);
                        }
                        break;
                        case JAVA_TO_JS: {
                            intent = new Intent(MainActivity.this, JavaToJsActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;
                    /*
					 * case TBS_COOKIE: { // intent=new
					 * Intent(MainActivity.this,CookieTestActivity.class); //
					 * MainActivity.this.startActivity(intent);
					 * Toast.makeText(mContext, "未开放功能",
					 * Toast.LENGTH_LONG).show(); } break;
					 */
                        case TBS_VIDEO: {

                            MainActivity.this.invokeTbsVideoPlayer(
                                    "http://125.64.133.74/data9/userfiles/video02/2014/12/11/2796948-280-068-1452.mp4");

                        }
                        break;
                        case TBS_WEB: {
                            intent = new Intent(MainActivity.this, BrowserActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;
                        case TBS_IMAGE: {
                            intent = new Intent(MainActivity.this, ImageResultActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;
                        case TBS_FIRST_X5: {
                            Builder builder = new Builder(mContext);
                            builder.setTitle("首次加载X5内核");
                            builder.setPositiveButton("轮询检查法", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(MainActivity.this, X5FirstTimeActivity.class);
                                    MainActivity.this.startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("延时构造法", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(MainActivity.this, X5FirstTimeActivityForDelay.class);
                                    MainActivity.this.startActivity(intent);
                                }
                            });
                            builder.show();
                            //
                            // Toast.makeText(mContext, "未开放功能",
                            // Toast.LENGTH_LONG).show();

                        }
                        break;
                        case SYS_WEB: {
                            intent = new Intent(MainActivity.this, SystemWebActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;
                        case TBS_FLASH: {
                            intent = new Intent(MainActivity.this, FlashPlayerActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;

                        case TBS_NEW_WINDOW: {
                            intent = new Intent(MainActivity.this, WebViewTransportActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;

                        case TBS_LONG_PRESS: {
                            intent = new Intent(MainActivity.this, MyLongPressActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;
                        case TBS_OVER_SCROLL: {
                            intent = new Intent(MainActivity.this, RefreshActivity.class);
                            MainActivity.this.startActivity(intent);

                        }
                        break;
                        case TBSS_SMALL_QB:
                            // 加载简版QB必须预先使用Qbsdk.preInit()方法进行初始化，可以放到OnCreate方法中
                        {
                            QbSdk.startMiniQBToLoadUrl(mContext, "http://www.baidu.com", null);
                        }
                        break;
                    }

                }
            });

        }
        main_initialized = true;

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Activity menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.tbsSuiteExit();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void tbsSuiteExit() {
        // exit TbsSuite?
        Builder dialog = new Builder(mContext);
        dialog.setTitle("X5功能演示");
        dialog.setPositiveButton("OK", new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Process.killProcess(Process.myPid());
            }
        });
        dialog.setMessage("quit now?");
        dialog.create().show();
    }

    /**
     * 用于TBS 视频裸播
     *
     * @param videoUrl 视频源 url
     */
    private void invokeTbsVideoPlayer(String videoUrl) {
        if (TbsVideo.canUseTbsPlayer(mContext)) {
            TbsVideo.openVideo(mContext, videoUrl);
        } else {
            Toast.makeText(this, "X5内核无法正常加载，无法启动视频裸播", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 开启额外进程 服务 预加载X5内核， 此操作必须在主进程调起X5内核前进行，否则将不会实现预加载
     */
    private void preinitX5WithService() {
        Intent intent = new Intent(mContext, FirstLoadingX5Service.class);
        startService(intent);
    }

    /**
     * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
     * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
     * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
     */
    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            QbSdk.preInit(MainActivity.this, myCallback);// 设置X5初始化完成的回调接口
            // 第三个参数为true：如果首次加载失败则继续尝试加载；
        }
    }

    private PreInitCallback myCallback = new PreInitCallback() {

        @Override
        public void onViewInitFinished() {// 当X5webview 初始化结束后的回调
            new WebView(mContext);
            MainActivity.this.isX5WebViewEnabled = true;
        }

        @Override
        public void onCoreInitFinished() {
        }
    };
}
