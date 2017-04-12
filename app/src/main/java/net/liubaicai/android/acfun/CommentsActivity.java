package net.liubaicai.android.acfun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import net.liubaicai.android.acfun.Popups.CommentsDialog;
import net.liubaicai.android.acfun.adapters.CommentsListAdapter;
import net.liubaicai.android.acfun.models.CommentContent;
import net.liubaicai.android.acfun.models.CommentResult;
import net.liubaicai.android.acfun.models.CommentSubmitResult;
import net.liubaicai.android.acfun.tools.Settings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends BaseActivity {

    Context context;

    ProgressBar listProgressBar;
    PullToRefreshListView refreshListView;

    private int contentId;
    private int quoteId = 0;

    private int pageIndex = 1;
    private boolean isBottom = false;

    final private SparseArray<CommentContent> commentContentArr = new SparseArray<>();
    final private List<Integer> commentList = new ArrayList<>();
    private CommentsListAdapter adapter;

    private boolean isLoading = false;

    EditText comments_edit;
    ImageButton comments_send_btn;
    ImageButton comments_emotion_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        Intent intent = this.getIntent();
        contentId = intent.getIntExtra("contentId", 0);

        comments_edit = (EditText) findViewById(R.id.comments_edit);
        comments_send_btn = (ImageButton) findViewById(R.id.comments_send_btn);
        comments_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment(comments_edit.getText().toString(), contentId, quoteId);
            }
        });
        comments_emotion_btn = (ImageButton) findViewById(R.id.comments_emotion_btn);
        comments_emotion_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        listProgressBar = (ProgressBar)findViewById(R.id.listProgressBar);
        refreshListView = (PullToRefreshListView) this.findViewById(R.id.refreshListView);
        // Set a listener to be invoked when the list should be refreshed.
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                reloadCommentsData();
            }
        });
        // Add an end-of-list listener
        refreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (!isLoading) {
                    loadCommentsData();
                }
            }
        });
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0
                        && commentContentArr.indexOfKey(commentList.get(position - 1)) > 0
                        && commentContentArr.get(commentList.get(position - 1)).getQuoteId() > 0) {
                    CommentsDialog commentsDialog = new CommentsDialog(context, commentList.get(position - 1), commentContentArr);
                    commentsDialog.show();
                }
            }
        });
        ListView listView = refreshListView.getRefreshableView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (quoteId == commentList.get(position - 1)) {
                        quoteId = 0;
                        comments_edit.setHint(R.string.post_comment);
                    } else {
                        quoteId = commentList.get(position - 1);
                        comments_edit.setHint("引用#" + commentContentArr.get(quoteId).getCount());
                    }
                }
                return true;
            }
        });
        adapter = new CommentsListAdapter(context,
                commentContentArr, commentList, R.layout.comments_list_item);
        refreshListView.setAdapter(adapter);

        reloadCommentsData();
    }

    private void reloadCommentsData() {
        pageIndex = 1;
        isBottom = false;
        getCommentsData(pageIndex);
    }

    private void loadCommentsData() {
        pageIndex++;
        getCommentsData(pageIndex);
    }

    private void getCommentsData(final int pageNum) {
        if (isLoading || isBottom)
            return;
        AsyncHttpClient client = new AsyncHttpClient();
        String url = String.format(Settings.getCommentUrl(),
                contentId, pageNum, System.currentTimeMillis());
        Log.d("baicaidebug", url);
        client.setTimeout(30000);
        client.get(url, new BaseJsonHttpResponseHandler<CommentResult>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, CommentResult response) {
                Log.d("baicaidebug", response.getMsg());
                if (response.isSuccess()) {
                    if (pageNum == 1) {
                        commentList.clear();
                    }
                    for (Map.Entry<String, CommentContent> entry : response.getData().getCommentContentArr().entrySet()) {
                        commentContentArr.put(entry.getValue().getCid(), entry.getValue());
                    }
                    for (int i : response.getData().getCommentList()) {
                        if (!commentList.contains(i))
                            commentList.add(i);
                    }
                    if (response.getData().getPage() >= response.getData().getTotalPage())
                        isBottom = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, CommentResult errorResponse) {
                Toast.makeText(getApplicationContext(), "获取数据失败，请稍后重试", Toast.LENGTH_LONG).show();
            }

            @Override
            protected CommentResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return JSON.parseObject(rawJsonData, CommentResult.class);
            }

            @Override
            public void onStart() {
                isLoading = true;
                if (commentList.size() == 0 || pageNum != 1) {
                    listProgressBar.setVisibility(View.VISIBLE);
                }
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                listProgressBar.setVisibility(View.INVISIBLE);
                isLoading = false;
                refreshListView.onRefreshComplete();
            }
        });
    }


    private void sendComment(String text, int contentId) {
        sendComment(text, contentId, 0);
    }

    private void sendComment(String text, int contentId, int quoteId) {
        if (!Settings.IsLogin()) {
            Toast.makeText(getApplicationContext(), "没登录o(╯□╰)o", Toast.LENGTH_LONG).show();
            return;
        }

        if (text.length() <= 5) {
            Toast.makeText(getApplicationContext(), "太短啦o(╯□╰)o", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = Settings.getSendCommentUrl() + "?" + "text=" + text + "&contentId=" + contentId + "&quoteId=" + quoteId;
        Log.d("baicaidebug", url);
        RequestParams params = new RequestParams();
        params.add("text", text);
        params.add("contentId", String.valueOf(contentId));
        params.add("quoteId", String.valueOf(quoteId));
        params.setContentEncoding("UTF-8");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        client.addHeader("Accept", "*/*");
        client.addHeader("Accept-Encoding", "gzip, deflate");
        client.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        client.addHeader("X-Requested-With", "XMLHttpRequest");
        client.addHeader("Content-Type", "multipart/form-data");
        String cookies = "";
        for (String cookie : Settings.Cookies) {
            cookies += cookie + ";";
        }
        client.addHeader("Cookie", cookies);
        client.post(url, params, new BaseJsonHttpResponseHandler<CommentSubmitResult>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, CommentSubmitResult response) {
                comments_edit.setText("");
                comments_edit.setHint(R.string.post_comment);
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comments_edit.getWindowToken(), 0);
//                refreshListView.scrollTo(0, 0-refreshListView.getVerticalScrollbarPosition());
//                reloadCommentsData();
                Toast.makeText(getApplicationContext(), "提交数据成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, CommentSubmitResult errorResponse) {
                Toast.makeText(getApplicationContext(), "提交数据失败，请稍后重试", Toast.LENGTH_LONG).show();
            }

            @Override
            protected CommentSubmitResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return JSON.parseObject(rawJsonData, CommentSubmitResult.class);
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
