package net.liubaicai.android.acfun;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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

    public void Logout(){
        Cookies.clear();
        setUsername("");
        setPassword("");
    }

    SharedPreferences sharedPreferences;

    public Settings(Context context){
        sharedPreferences = context.getSharedPreferences("settings", context.MODE_PRIVATE);
    }

    public int getChannel() {
        return sharedPreferences.getInt("channel", 110);
    }

    public void setChannel(int channel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("channel", channel);
        editor.commit();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.commit();
    }
}
