package net.liubaicai.android.acfun.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
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

/**
 * Created by Baicai on 2016/4/7.
 */
public class DrawerHelper {

    Drawer drawer;

    LayoutInflater inflater;
    View header;
    SimpleDraweeView slide_menu_avatar;
    TextView slide_menu_nickname;

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
                                        new Settings(activity).Logout();
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
