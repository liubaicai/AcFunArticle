package net.liubaicai.android.acfun.tools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import net.liubaicai.android.acfun.models.UserLoginResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liush on 2016/3/25.
 */
public class Settings {

    public static List<String> Cookies = new ArrayList<String>();

    public static boolean IsLogin(){
        return Cookies.size()!=0;
    }

    public static UserLoginResult userLoginResult;

    public static void Logout() {
        Cookies.clear();
        setUsername("");
        setPassword("");
    }

    public static SharedPreferences sharedPreferences;

    public static boolean getIsNoPic() {
        return sharedPreferences.getBoolean("nopic", true);
    }

    public static void setIsNoPic(boolean isNoPic) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nopic", isNoPic);
        editor.commit();
    }

    public static int getChannel() {
        return sharedPreferences.getInt("channel", 110);
    }

    public static void setChannel(int channel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("channel", channel);
        editor.commit();
    }

    public static String getUsername() {
        return sharedPreferences.getString("username", "");
    }

    public static void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public static String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public static void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.commit();
    }


    public static String getChannelUrl() {
        return sharedPreferences.getString("channel_url", "http://api.aixifan.com/searches/channel?sort=4&pageSize=20&channelIds=%d&pageNo=%d&_=%d");
    }

    public static String getArticleUrl() {
        return sharedPreferences.getString("article_url", "http://api.aixifan.com/contents/");
    }

    public static String getCommentUrl() {
        return sharedPreferences.getString("comment_url", "http://www.acfun.cn/comment/content/web/list?pageSize=20&contentId=%d&pageNo=%d&_=%d");
    }

    public static String getSendCommentUrl() {
        return sharedPreferences.getString("send_comment_url", "http://m.acfun.cn/comment.aspx");
    }

    public static String getCheckInUrl() {
        return sharedPreferences.getString("check_in_url", "http://www.acfun.cn/webapi/record/actions/signin?channel=0&date=%d");
    }
}
