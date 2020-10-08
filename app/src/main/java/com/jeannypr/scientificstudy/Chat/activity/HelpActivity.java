package com.jeannypr.scientificstudy.Chat.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class HelpActivity extends AppCompatActivity {
    WebView webView;
    String url, title, subtitle = "";
    ProgressBar pb;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        context = this;

        url = getIntent().getStringExtra("webUrl");
        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");
        if (title == null || title.equals("")) title = getResources().getString(R.string.app_name);

        if (getIntent().hasExtra("subtitle"))
            subtitle = getIntent().getStringExtra("subtitle");

        setToolBar();
        pb = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webview);

        initializeWebView();
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, title, subtitle);
    }

    private void initializeWebView() {
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pb.setVisibility(View.GONE);
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
