package net.liubaicai.android.acfun;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import net.liubaicai.android.acfun.Popups.LoginDialog;
import net.liubaicai.android.acfun.adapters.ChannelListAdapter;
import net.liubaicai.android.acfun.models.ChannelList;
import net.liubaicai.android.acfun.models.ChannelResult;
import net.liubaicai.android.acfun.models.UserLoginResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ProgressBar listProgressBar;
    PullToRefreshListView refreshListView;

    ChannelListAdapter adapter;
    List<ChannelList> channelListItems = new ArrayList<ChannelList>();
    private int pageIndex = 1;

    private boolean isLoading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                reloadChannelListData();
            }
        });
        // Add an end-of-list listener
        refreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (!isLoading) {
                    loadChannelListData();
                }
            }
        });
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    ChannelList item = channelListItems.get(position-1);
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("contentId", item.getContentId());
                    startActivity(intent);
                }
            }
        });
        adapter = new ChannelListAdapter(getApplicationContext(),
                channelListItems, R.layout.channel_list_item);
        refreshListView.setAdapter(adapter);

        if (channelListItems.size()<=0)
            reloadChannelListData();

        autoLogin();

        new UpdateManager(this).checkUpdate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setIconEnable(menu, true);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_user){
            if (Settings.IsLogin()){
                new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要登出嘛？！")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                new Settings(getApplicationContext()).Logout();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "O(∩_∩)O~~", Toast.LENGTH_LONG);
                    }
                }).show();//在按键响应事件中显示此对话框

            }
            else {
                LoginDialog loginDialog = new LoginDialog(this);
                loginDialog.show();
            }
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_110) {
            new Settings(getApplicationContext()).setChannel(110);
            reloadChannelListData();
            return true;
        }
        if (id == R.id.action_73) {
            new Settings(getApplicationContext()).setChannel(73);
            reloadChannelListData();
            return true;
        }
        if (id == R.id.action_74) {
            new Settings(getApplicationContext()).setChannel(74);
            reloadChannelListData();
            return true;
        }
        if (id == R.id.action_75) {
            new Settings(getApplicationContext()).setChannel(75);
            reloadChannelListData();
            return true;
        }
        if (id == R.id.action_164) {
            new Settings(getApplicationContext()).setChannel(164);
            reloadChannelListData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadChannelListData()
    {
        pageIndex=1;
        adapter.notifyDataSetChanged();
        getChannelListData(pageIndex);
    }

    private void loadChannelListData()
    {
        pageIndex++;
        getChannelListData(pageIndex);
    }

    private void getChannelListData(final int pageNum)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://api.acfun.tv/apiserver/content/channel?pageSize=20&channelId="
                        + new Settings(getApplicationContext()).getChannel()
                        + "&pageNo=" + pageNum + "&_=" + System.currentTimeMillis(),
                new BaseJsonHttpResponseHandler<ChannelResult>() {

                    @Override
                    public void onStart() {
                        isLoading = true;
                        if (channelListItems.size() == 0 || pageNum != 1) {
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

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ChannelResult response) {
                        if (response != null && response.getData() != null
                                && response.getData().getPage() != null
                                && response.getData().getPage().getList() != null) {
                            if (pageNum == 1) {
                                channelListItems.clear();
                            }
                            for (ChannelList item : response.getData().getPage().getList()) {
                                channelListItems.add(item);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ChannelResult errorResponse) {
                        Toast.makeText(getApplicationContext(), "获取数据失败，请稍后重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    protected ChannelResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), ChannelResult.class).next();
                    }
                });
    }


    //enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
    private void setIconEnable(Menu menu, boolean enable)
    {
        try
        {
            Class<?> clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);

            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void autoLogin() {
        if(!Settings.IsLogin()) {
            String username = new Settings(getApplicationContext()).getUsername();
            String password = new Settings(getApplicationContext()).getPassword();
            if(!username.isEmpty()&&!password.isEmpty()){
                String url = "http://m.acfun.tv/login.aspx";
                RequestParams params = new RequestParams();
                params.add("username",username);
                params.add("password",password);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(url, params, new BaseJsonHttpResponseHandler<UserLoginResult>() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, UserLoginResult response) {
                        if (response.isSuccess()) {
                            Settings.Cookies.clear();
                            for (Header header : headers) {
                                if (header.getName().equals("Set-Cookie")) {
                                    Settings.Cookies.add(header.getValue());
                                }
                            }
                        } else {
                            new Settings(getApplicationContext()).Logout();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, UserLoginResult errorResponse) {
                        new Settings(getApplicationContext()).Logout();
                    }

                    @Override
                    protected UserLoginResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), UserLoginResult.class).next();
                    }
                });
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
