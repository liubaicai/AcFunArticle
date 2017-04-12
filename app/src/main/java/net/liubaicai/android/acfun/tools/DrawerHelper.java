package net.liubaicai.android.acfun.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.liubaicai.android.acfun.AboutActivity;
import net.liubaicai.android.acfun.Popups.LoginDialog;
import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.SettingsActivity;
import net.liubaicai.android.acfun.models.CheckInResult;
import net.liubaicai.android.acfun.models.CommentSubmitResult;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Baicai on 2016/4/7.
 */
public class DrawerHelper {

    Drawer drawer;

    LayoutInflater inflater;
    View header;
    SimpleDraweeView slide_menu_avatar;
    TextView slide_menu_nickname;
    Button checkInButton;

    PrimaryDrawerItem userItem;
    PrimaryDrawerItem settingItem;
    PrimaryDrawerItem aboutItem;

    SecondaryDrawerItem c110Item;
    SecondaryDrawerItem c73Item;
    SecondaryDrawerItem c74Item;
    SecondaryDrawerItem c75Item;
    SecondaryDrawerItem c164Item;

    public interface ICallBack {
        public void onItemClick(int i);
    }

    ICallBack icallBack = null;

    public void setOnClick(ICallBack iBack) {
        icallBack = iBack;
    }

    Activity activity;
    Toolbar toolbar;

    public DrawerHelper(Activity activity, Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;
    }

    public void build() {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        header = inflater.inflate(R.layout.slide_menu_header, null);
        slide_menu_avatar = (SimpleDraweeView) header.findViewById(R.id.slide_menu_avatar);
        slide_menu_nickname = (TextView) header.findViewById(R.id.slide_menu_nickname);
        checkInButton = (Button) header.findViewById(R.id.checkInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = String.format(Settings.getCheckInUrl(), System.currentTimeMillis());
                Log.d("baicaidebug", url);
                AsyncHttpClient client = new AsyncHttpClient();
                String cookies = "";
                for (String cookie : Settings.Cookies) {
                    cookies += cookie + ";";
                }
                client.addHeader("Cookie", cookies);
                client.post(url, new BaseJsonHttpResponseHandler<CheckInResult>() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, CheckInResult response) {
                        Log.d("baicaidebug", rawJsonResponse);
                        if (response.getCode() == 200) {
                            Toast.makeText(activity, response.getData().getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, response.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, CheckInResult errorResponse) {
                        Toast.makeText(activity, "提交数据失败，请稍后重试", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected CheckInResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return JSON.parseObject(rawJsonData, CheckInResult.class);
                    }
                });
            }
        });

        userItem = new PrimaryDrawerItem().withName(R.string.action_user).withSelectable(false);
        settingItem = new PrimaryDrawerItem().withName(R.string.action_settings).withSelectable(false);
        aboutItem = new PrimaryDrawerItem().withName(R.string.action_about).withSelectable(false);

        c110Item = new SecondaryDrawerItem().withName(R.string.action_110);
        c73Item = new SecondaryDrawerItem().withName(R.string.action_73);
        c74Item = new SecondaryDrawerItem().withName(R.string.action_74);
        c75Item = new SecondaryDrawerItem().withName(R.string.action_75);
        c164Item = new SecondaryDrawerItem().withName(R.string.action_164);

        drawer = new DrawerBuilder().withActivity(activity).withToolbar(toolbar).build();
        drawer.addItems(
                c110Item,
                c73Item,
                c74Item,
                c75Item,
                c164Item,
                new DividerDrawerItem(),
                userItem,
                settingItem,
                aboutItem);
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (drawerItem == userItem) {
                    if (Settings.IsLogin()) {
                        new AlertDialog.Builder(activity).setTitle("系统提示")//设置对话框标题
                                .setMessage("确定要登出嘛？！")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        Settings.Logout();
                                        setLogout();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "O(∩_∩)O~~", Toast.LENGTH_LONG);
                            }
                        }).show();//在按键响应事件中显示此对话框

                    } else {
                        LoginDialog loginDialog = new LoginDialog(activity);
                        loginDialog.setOnLogin(new LoginDialog.ICallBack() {
                            @Override
                            public void onLogin(boolean b) {
                                setLogin();
                            }
                        });
                        loginDialog.show();
                    }
                    return false;
                }
                if (drawerItem == settingItem) {
                    Intent intent = new Intent(activity, SettingsActivity.class);
                    activity.startActivity(intent);
                    return false;
                }
                if (drawerItem == aboutItem) {
                    Intent intent = new Intent(activity, AboutActivity.class);
                    activity.startActivity(intent);
                    return false;
                }
                if (drawerItem == c110Item) {
                    icallBack.onItemClick(R.id.action_110);
                    return false;
                }
                if (drawerItem == c73Item) {
                    icallBack.onItemClick(R.id.action_73);
                    return false;
                }
                if (drawerItem == c74Item) {
                    icallBack.onItemClick(R.id.action_74);
                    return false;
                }
                if (drawerItem == c75Item) {
                    icallBack.onItemClick(R.id.action_75);
                    return false;
                }
                if (drawerItem == c164Item) {
                    icallBack.onItemClick(R.id.action_164);
                    return false;
                }
                return false;
            }
        });
        setLogin();
    }

    public void setLogin() {
        if (Settings.IsLogin()) {
            userItem.withName(R.string.action_login_user);
            drawer.updateItem(userItem);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Settings.userLoginResult.getImg())
                    .setAutoPlayAnimations(true)
                    .build();
            slide_menu_avatar.setController(controller);
            slide_menu_nickname.setText(Settings.userLoginResult.getUsername());
            drawer.setHeader(header);
        }
    }

    public void setLogout() {
        userItem.withName(R.string.action_user);
        drawer.updateItem(userItem);
        drawer.removeHeader();
    }
}
