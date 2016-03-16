package com.gekson.x5demo;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gekson.x5demo.utils.X5WebView;

public class FilechooserActivity extends Activity {


    /**
     * 用于展示在web端<input type=text>的标签被选择之后，文件选择器的制作和生成
     */

    private X5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filechooser_layout);


        webView = (X5WebView) findViewById(R.id.web_filechooser);
        webView.loadUrl("file:///android_asset/webpage/fileChooser.html");

        this.initBtn();

//		Log.i("midPageView", "miniQb ret is"+QbSdk.startMiniQBToLoadUrl(this, "http:\\www.baidu.com", null));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == X5WebView.FILE_CHOOSER) {//file chooser result from html <input> label

            if (data != null) {
                Log.i("fileChooser", "intent is" + data.toString());
                Log.i("fileChooser", "bundle is" + data.getDataString());
                String path = data.getDataString();
                Builder builder = new Builder(this);
                builder.setTitle("文件选择");
                builder.setMessage("当前选择的文件全路径为：" + path);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        }
    }


    private void initBtn() {
        Button btnFlush = (Button) findViewById(R.id.bt_filechooser_flush);
        btnFlush.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.reload();
                Log.i("yuanhaizhou", "webview use night mode!");
                webView.setDayOrNight(false);
            }
        });

        Button btnBackForward = (Button) findViewById(R.id.bt_filechooser_back);
        btnBackForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.goBack();
            }
        });

        Button btnHome = (Button) findViewById(R.id.bt_filechooser_home);
        btnHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.loadUrl("file:///android_asset/webpage/fileChooser.html");
            }
        });

    }


    //////////////////////////////////////////////////////////////

    /**
     * 确保注销配置能够被释放
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (this.webView != null) {
            webView.getSettings().setBuiltInZoomControls(true);
            webView.destroy();
        }
        super.onDestroy();
    }


}
