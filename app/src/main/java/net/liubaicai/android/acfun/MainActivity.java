package net.liubaicai.android.acfun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
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

import net.liubaicai.android.acfun.adapters.ChannelListAdapter;
import net.liubaicai.android.acfun.models.ChannelResult;
import net.liubaicai.android.acfun.models.UserLoginResult;
import net.liubaicai.android.acfun.tools.DrawerHelper;
import net.liubaicai.android.acfun.tools.Settings;
import net.liubaicai.android.acfun.tools.UpdateManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    ProgressBar listProgressBar;
    PullToRefreshListView refreshListView;

    ChannelListAdapter adapter;
    List<ChannelResult.DataBean.ListBean> channelListItems = new ArrayList<>();
    private int pageIndex = 1;

    private boolean isLoading=false;

    DrawerHelper drawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerHelper = new DrawerHelper(this, toolbar);
        drawerHelper.setOnClick(new DrawerHelper.ICallBack() {
            @Override
            public void onItemClick(int i) {
                selectChannel(i);
            }
        });
        drawerHelper.build();

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
                    ChannelResult.DataBean.ListBean item = channelListItems.get(position-1);
                    Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                    intent.putExtra("contentId", Integer.parseInt(item.getContentId()));
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

    private void selectChannel(int id) {
        if (id == R.id.action_110) {
            Settings.setChannel(110);
            reloadChannelListData();
        }
        if (id == R.id.action_73) {
            Settings.setChannel(73);
            reloadChannelListData();
        }
        if (id == R.id.action_74) {
            Settings.setChannel(74);
            reloadChannelListData();
        }
        if (id == R.id.action_75) {
            Settings.setChannel(75);
            reloadChannelListData();
        }
        if (id == R.id.action_164) {
            Settings.setChannel(164);
            reloadChannelListData();
        }
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
        String url = String.format(Settings.getChannelUrl(),
                Settings.getChannel(), pageNum, System.currentTimeMillis());
        Log.d("baicaidebug",url);
        client.addHeader("deviceType","1");
        client.get(url,
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
                                && response.getData().getList() != null) {
                            if (pageNum == 1) {
                                channelListItems.clear();
                            }
                            for (ChannelResult.DataBean.ListBean item : response.getData().getList()) {
                                channelListItems.add(item);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else if (response!=null && !response.getMessage().isEmpty()){
                            Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ChannelResult errorResponse) {
                        Toast.makeText(getApplicationContext(), "获取数据失败，请稍后重试", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected ChannelResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return JSON.parseObject(rawJsonData, ChannelResult.class);
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
            String username = Settings.getUsername();
            String password = Settings.getPassword();
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
                            Settings.userLoginResult = response;
                            Settings.Cookies.clear();
                            for (Header header : headers) {
                                if (header.getName().equals("Set-Cookie")) {
                                    Settings.Cookies.add(header.getValue());
                                }
                            }
                            drawerHelper.setLogin();
                        } else {
                            Settings.Logout();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, UserLoginResult errorResponse) {
                        Settings.Logout();
                    }

                    @Override
                    protected UserLoginResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return JSON.parseObject(rawJsonData, UserLoginResult.class);
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
