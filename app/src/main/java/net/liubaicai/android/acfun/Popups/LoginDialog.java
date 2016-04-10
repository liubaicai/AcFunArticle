package net.liubaicai.android.acfun.Popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.tools.Settings;
import net.liubaicai.android.acfun.models.UserLoginResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by liush on 2016/3/26.
 */
public class LoginDialog extends Dialog {

    public interface ICallBack {
        public void onLogin(boolean b);
    }

    ICallBack icallBack = null;

    public void setOnLogin(ICallBack iBack) {
        icallBack = iBack;
    }

    private boolean isNeedCaptcha = false;

    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("登录");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_login_view, null);
        setContentView(view);

        final EditText username = (EditText)view.findViewById(R.id.login_view_username_edit);
        final EditText password = (EditText)view.findViewById(R.id.login_view_password_edit);
        final EditText captcha = (EditText)view.findViewById(R.id.login_view_validation_edit);
        final RelativeLayout captchaLayout = (RelativeLayout)view.findViewById(R.id.login_view_validation_layout);
        final ImageView captchaImage = (ImageView)view.findViewById(R.id.login_view_validation_img);


        Button loginButton = (Button)view.findViewById(R.id.login_view_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = username.getText().toString();
                final String pwd = password.getText().toString();
                String cap = captcha.getText().toString();
                String url = "http://m.acfun.tv/login.aspx";
//                if (isNeedCaptcha)
//                    url = String.format("http://m.acfun.tv/login.aspx?username=%s&password=%s&captcha=%s",name,pwd,cap);
//                else
//                    url = String.format("http://m.acfun.tv/login.aspx?username=%s&password=%s",name,pwd);
                RequestParams params = new RequestParams();
                params.add("username",name);
                params.add("password",pwd);
                if (isNeedCaptcha)
                    params.add("captcha",cap);
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
                            Settings settings = new Settings(getContext());
                            settings.setUsername(name);
                            settings.setPassword(pwd);
                            hide();
                            icallBack.onLogin(true);
                        } else if (response.getResult().contains("captcha")) {
                            Toast.makeText(getContext(), response.getResult(), Toast.LENGTH_SHORT).show();
                            captchaLayout.setVisibility(View.VISIBLE);
                            captchaImage.setImageBitmap(returnBitMap("http://www.acfun.tv/captcha.svl?" + new Date().getTime()));
                        } else {
                            Toast.makeText(getContext(), response.getResult(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, UserLoginResult errorResponse) {
                        Toast.makeText(getContext(), "登录失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected UserLoginResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), UserLoginResult.class).next();
                    }
                });
            }
        });
    }

    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
