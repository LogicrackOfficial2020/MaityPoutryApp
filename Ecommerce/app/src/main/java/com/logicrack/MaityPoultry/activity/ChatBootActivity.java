package com.logicrack.MaityPoultry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.logicrack.MaityPoultry.R;

import java.io.File;

public class ChatBootActivity extends AppCompatActivity {
    public WebView webview;
    private static final int MODE_LIGHT = 0;
    public  String dataBasePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat_boot);
        changeStatusBar(MODE_LIGHT,getWindow());

        webview=findViewById(R.id.webview);
        WebSettings websettings =webview.getSettings();
        websettings.setDomStorageEnabled(true);
        websettings.setDatabaseEnabled(true);


        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            dataBasePath="/data/data/"+ webview.getContext().getPackageName()+"/databases/";
            websettings.setDatabasePath(dataBasePath);
        }
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://shopping.maitypoultries.org/URL/chatbot.html");
    }

    public void changeStatusBar(int mode, Window window){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.contentStatus));
            //Light mode
            if(mode==MODE_LIGHT){
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
}
