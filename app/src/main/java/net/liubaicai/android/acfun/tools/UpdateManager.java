package net.liubaicai.android.acfun.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.models.UpdateMessage;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Created by liush on 2016/3/27.
 */
public class UpdateManager {

    UpdateMessage message;
    Context context;

    /* 下载保存路径 */
    private String mSavePath;
    private String mFileName;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    public UpdateManager(Context context)
    {
        this.context = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://file.liubaicai.net/apk/acfun/update.json", new BaseJsonHttpResponseHandler<UpdateMessage>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, UpdateMessage response) {
                int ver = response.getVersioncode();
                if (getVersionCode() < ver) {
                    message = response;
                    showNoticeDialog(response.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, UpdateMessage errorResponse) {

            }

            @Override
            protected UpdateMessage parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), UpdateMessage.class).next();
            }
        });
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(String content)
    {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("软件更新");
        builder.setMessage(content);
        // 更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                startDownload();
            }
        });
        // 稍后更新
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 开始下载
     */
    private void startDownload()
    {
        try
        {
            Log.d("bcdebug","startDownload");
            // 创建连接
            final AsyncHttpClient client = new AsyncHttpClient();

            // 构造软件下载对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("正在更新");
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.softupdate_progress, null);
            mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
            builder.setView(v);
            mDownloadDialog = builder.create();
            mDownloadDialog.show();

            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                mSavePath = sdpath + "Download";
                try {
                    mFileName = message.getApkurl().split("/")[message.getApkurl().split("/").length-1];
                }
                catch (Exception e){
                    mFileName = "AcFun文章区_"+message.getVersioncode()+"_update.apk";
                    e.printStackTrace();
                }

                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists())
                {
                    file.mkdir();
                }
                final File apkFile = new File(mSavePath, mFileName);
                final File tmpFile = new File(mSavePath, mFileName+".tmp");
                if (!apkFile.exists()){
                    client.get(message.getApkurl(), new FileAsyncHttpResponseHandler(tmpFile) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            Log.d("bcdebug","Download onFailure");
                            if (tmpFile.exists())
                                tmpFile.delete();
                            if (mDownloadDialog!=null&&mDownloadDialog.isShowing())
                                mDownloadDialog.hide();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            Log.d("bcdebug",file.getAbsolutePath());
                            tmpFile.renameTo(apkFile);
                            if (mDownloadDialog!=null&&mDownloadDialog.isShowing())
                                mDownloadDialog.hide();
                            installApk();
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            if(mProgress!=null)
                                mProgress.setProgress((int) (((float) bytesWritten / totalSize) * 100));
                            super.onProgress(bytesWritten, totalSize);
                        }

                        @Override
                        public void onCancel() {
                            if (tmpFile.exists())
                                tmpFile.delete();
                            if (mDownloadDialog!=null&&mDownloadDialog.isShowing())
                                mDownloadDialog.hide();
                            super.onCancel();
                        }
                    });
                }
                else {
                    installApk();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取软件版本号
     *
     * @return
     */
    private int getVersionCode()
    {
        int versionCode = 0;
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = this.context.getPackageManager().getPackageInfo("net.liubaicai.android.acfun", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 安装APK文件
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath, mFileName);
        if (!apkfile.exists())
        {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }
}
