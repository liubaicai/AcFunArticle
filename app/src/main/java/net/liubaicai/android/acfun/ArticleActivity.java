package net.liubaicai.android.acfun;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import net.liubaicai.android.acfun.models.ArticleResult;
import net.liubaicai.android.acfun.tools.HtmlTool;
import net.liubaicai.android.acfun.tools.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ArticleActivity extends BaseActivity {

    WebView webView;
    ProgressBar progressBar;
    TextView article_detail_head_view_title;
    TextView article_detail_head_view_name;
    TextView article_detail_head_view_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        final int contentId=intent.getIntExtra("contentId",0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                    intent.putExtra("contentId", contentId);
                    startActivity(intent);
                }
            });
        }

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        article_detail_head_view_title = (TextView)findViewById(R.id.article_detail_head_view_title);
        article_detail_head_view_name = (TextView)findViewById(R.id.article_detail_head_view_name);
        article_detail_head_view_time = (TextView)findViewById(R.id.article_detail_head_view_time);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.getSettings().setDefaultTextEncodingName("UTF -8");
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= 16){
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Settings.getArticleUrl()+contentId;
        Log.d("baicaidebug",url);
        client.addHeader("deviceType","1");
        client.get(url,
                new BaseJsonHttpResponseHandler<ArticleResult>(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ArticleResult response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.getCode()==200){
                            ArticleResult.DataBean articleDetail = response.getData();
                            article_detail_head_view_title.setText(Html.fromHtml(articleDetail.getTitle()));
                            article_detail_head_view_name.setText(articleDetail.getOwner().getName());
                            article_detail_head_view_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(articleDetail.getReleaseDate())));
                            webView.loadDataWithBaseURL("file:///android_asset/", HtmlTool.Covert2Html(response.getData().getArticle().getContent()), "text/html", "utf-8", null);
                            if(Build.VERSION.SDK_INT >= 11)
                                webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
                            //webView.setBackgroundColor(0x00000000);
                        }
                        else if (!response.getMessage().isEmpty()){
                            Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ArticleResult errorResponse) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("baicaidebug",rawJsonData);
                        Toast.makeText(getApplicationContext(), "获取数据失败，请稍后重试", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected ArticleResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), ArticleResult.class).next();
                    }

                    @Override
                    public void onStart() {
                        progressBar.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
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
