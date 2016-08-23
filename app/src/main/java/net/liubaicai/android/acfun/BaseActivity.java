package net.liubaicai.android.acfun;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by mac on 16/8/23.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
