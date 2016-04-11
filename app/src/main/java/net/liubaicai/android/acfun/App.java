package net.liubaicai.android.acfun;

import android.app.Application;

import net.liubaicai.android.acfun.tools.Settings;

/**
 * Created by liush on 2016/4/12.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Settings.sharedPreferences = this.getSharedPreferences("settings", this.MODE_PRIVATE);
    }
}
