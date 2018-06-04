package lct.mysymphony.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.BuildConfig;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;

public class DownloadApk {
    Context context;
    String apkUrl;
    String contentSdCardUrl;
    DataBaseData dataBaseData;
    DataHelper dbHelper;

    public void downLoadAPK(final String apkUrl, final Context context, DataBaseData dataBaseData) {
        this.context = context;
        this.apkUrl = apkUrl;
        dbHelper = new DataHelper(context);
        this.dataBaseData = dataBaseData;
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        final String fname = "apk" + n + ".apk";
        destination += fname;
        contentSdCardUrl = fname;
        final Uri uri = Uri.parse("file://" + destination);
        /* final Uri uri = Uri.parse("file://" + myDir);*/

        File file = new File(destination);
        if (file.exists())
            file.delete();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setDescription("Downloading the APK please wait!");
        request.setTitle("APK!");

        final Uri apkUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        request.setDestinationUri(uri);

        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                try {
                    Log.d("onComplete", "onComplete");
                    insertDataInDatabaseWithContentSdcardUl();
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(apkUri,
                            manager.getMimeTypeForDownloadedFile(downloadId));
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(install);
                    Log.i("RunningDownloadApk", apkUri.toString());
                    context.unregisterReceiver(this);
                    /* contentSdCardUrl=fname;*/

                } catch (Exception e) {
                    Log.e("ErrrInDownloadApk", e.toString());
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void downLoadAPK(final String appTitle, final String apkUrl, final Context context) {
        final ProgressDialog progressDialog = new lct.mysymphony.helper.ProgressDialog(context);
        SharedPreferences preferences = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
        int flag = preferences.getInt("unknownSource", 0);
        if (flag == 0) {
            progressDialog.showProgressDialogAPK();
        } else {
            progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
        }
        Log.i("apkUrlDownload", apkUrl);
        this.context = context;
        this.apkUrl = apkUrl;
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        final String fname = appTitle + ".apk";
        contentSdCardUrl = fname;
        destination += fname;
        final Uri uri = Uri.parse("file://" + destination);

        File file = new File(destination);
        if (file.exists())
            file.delete();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setDescription("Downloading the " + appTitle + " please wait!");
        request.setTitle(appTitle);

        final Uri apkUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        request.setDestinationUri(uri);

        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                try {
                    Log.d("onComplete", "onComplete");
                    progressDialog.hideProgressDialog();
                    AsyncResponse asyncResponse = (AsyncResponse) context;
                    asyncResponse.processFinish("complete");

                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(apkUri,
                            manager.getMimeTypeForDownloadedFile(downloadId));
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(install);

                    Log.i("RunningDownloadApk", apkUri.toString());
                    context.unregisterReceiver(this);

                } catch (Exception e) {
                    Log.e("ErrrInDownloadApk", e.toString());
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void insertDataInDatabaseWithContentSdcardUl() {
        Log.d("enterInsertApkToDB", "enterInsertAudioToDB");
        dbHelper.insertContentDataWithSdCardUrl(contentSdCardUrl, dataBaseData);
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }
}
