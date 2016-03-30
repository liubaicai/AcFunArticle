package net.liubaicai.android.acfun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;
import com.umeng.analytics.MobclickAgent;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailActivity extends AppCompatActivity {

    PullToRefreshWebView pullToRefreshWebView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();
        int contentId=intent.getIntExtra("contentId",0);

        pullToRefreshWebView = (PullToRefreshWebView)findViewById(R.id.webView);

        webView = pullToRefreshWebView.getRefreshableView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.loadUrl("http://m.acfun.tv/v/?ac=" + contentId);
        webView.setWebViewClient(new WebViewClientEx());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public class WebViewClientEx extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("bcdebug", url);
            CookieManager cookieManager = CookieManager.getInstance();
            if (Settings.Cookies.size()==0)
                cookieManager.removeAllCookie();
            for (String cookie:Settings.Cookies){
                cookieManager.setCookie(url, cookie);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.equals("about:blank")) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            String javascript = "javascript:document.getElementById('prompt-box').parentNode.removeChild(document.getElementById('prompt-box'));document.getElementById('btn-app').parentNode.removeChild(document.getElementById('btn-app'));document.getElementById('bottom-download').parentNode.removeChild(document.getElementById('bottom-download'));";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.evaluateJavascript(javascript,new ValueCallback<String>(){
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("bcdebug",value);
                    }
                });
            }
            else{
                view.loadUrl(javascript);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
