package harmony.app.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import harmony.app.Activity.HomePage;
import harmony.app.Activity.ImageViewActivity;
import harmony.app.BuildConfig;
import harmony.app.ModelClass.DataBaseData;
import harmony.app.R;

public class DownloadApk {
    Context context;
    String apkUrl;
    String contentSdCardUrl;
    DataBaseData dataBaseData;
    DataHelper dbHelper;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public void downLoadAPK(final String apkUrl, final Context context, final DataBaseData dataBaseData) {
        final ProgressDialog progressDialog = new harmony.app.Helper.ProgressDialog(context);
        this.context = context;
        this.apkUrl = apkUrl;
        dbHelper = new DataHelper(context);
        this.dataBaseData = dataBaseData;

        if(checkPermissions()){
            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            final String fname = dataBaseData.getContentTitle()+ ".apk";
            destination += fname;
            contentSdCardUrl = fname;
            final Uri uri = Uri.parse("file://" + destination);

            File file = new File(destination);
            if (file.exists())
                file.delete();

            Log.i("apkUrlDownload", apkUrl);
            SharedPreferences preferences = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
            int flag = preferences.getInt("unknownSource", 0);
            if (flag == 0) {
                progressDialog.showProgressDialogAPK();
            } else {
                progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
            request.setDescription("Downloading "+dataBaseData.getContentTitle()+" please wait!");
            request.setTitle(dataBaseData.getContentTitle());
            request.setNotificationVisibility(View.VISIBLE);

            final Uri apkUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            request.setDestinationUri(uri);

            final Date startTime;
            final DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            startTime = new Date();
            AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", ""+dataBaseData.getContentId(),
                    "DOWNLOAD_START", "Download starts for " + dataBaseData.getContentTitle(), "app");

            final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            final long downloadId = manager.enqueue(request);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    try {
                        progressDialog.hideProgressDialog();
                        Toast.makeText(context, dataBaseData.getContentTitle()+" ডাউনলোড হয়েছে", Toast.LENGTH_SHORT).show();
                        Date startTime;
                        DateFormat dateFormat;
                        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        startTime = new Date();
                        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", ""+dataBaseData.getContentId(),
                                "DOWNLOADED", "Download completed for "+ dataBaseData.getContentTitle(), "app");
                        AsyncResponse asyncResponse = (AsyncResponse) context;
                        asyncResponse.processFinish("completeAPK");
                        Log.d("onComplete", "onComplete");
                        insertDataInDatabaseWithContentSdcardUl();
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.setDataAndType(apkUri,
                                manager.getMimeTypeForDownloadedFile(downloadId));
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(install);
                        Log.i("RunningDownloadApk", apkUri.toString());
                        context.unregisterReceiver(this);
                    } catch (Exception e) {
                        AsyncResponse asyncResponse = (AsyncResponse) context;
                        asyncResponse.processFinish("completeAPK");
                        Log.e("ErrrInDownloadApk", e.toString());
                        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", ""+dataBaseData.getContentId(),
                                "DOWNLOAD_FAILED", e.toString(), "app");
                    }
                }
            };
            context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }else{
            Toast.makeText(context, "ডাওনলোড এর পূর্বে ডিভাইস এক্সেস দিয়ে নিন", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            Activity activity = (Activity) context;
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
    /*public void downLoadAPK(final String appTitle, final String apkUrl, final String contentId, final Context context) {
        final ProgressDialog progressDialog = new sym.appstore.Helper.ProgressDialog(context);
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

        final Date startTime;
        final DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        startTime = new Date();
        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", contentId,
                "DOWNLOAD_START", "Download starts for " + appTitle, "app");
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
                    Toast.makeText(context, dataBaseData.getContentTitle()+" ডাউনলোড হয়েছে", Toast.LENGTH_SHORT).show();
                    Date startTime;
                    DateFormat dateFormat;
                    dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    startTime = new Date();
                    AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", contentId,
                            "DOWNLOADED", "Download completed for "+ appTitle, "app");

                    Log.d("onComplete", "onComplete");
                    progressDialog.hideProgressDialog();

                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setDataAndType(apkUri,
                            manager.getMimeTypeForDownloadedFile(downloadId));
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(install);

                    Log.i("RunningDownloadApk", apkUri.toString());
                    context.unregisterReceiver(this);

                    AsyncResponse asyncResponse = (AsyncResponse) context;
                    asyncResponse.processFinish("complete");

                } catch (Exception e) {
                    Log.e("ErrrInDownloadApk", e.toString());
                    AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", contentId,
                            "DOWNLOAD_FAILED", e.toString(), "app");
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }*/

    public void insertDataInDatabaseWithContentSdcardUl() {
        Log.d("enterInsertApkToDB", "enterInsertAudioToDB");
        dbHelper.insertContentDataWithSdCardUrl(contentSdCardUrl, dataBaseData);
    }

    public void downLoadAPK(String finalApkURL, final Context context) {
        final ProgressDialog progressDialog = new harmony.app.Helper.ProgressDialog(context);
        this.context = context;
        this.apkUrl = finalApkURL;

        if(checkPermissions()){
            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
            final String fname = "harmony"+".apk";
            destination += fname;
            contentSdCardUrl = fname;
            final Uri uri = Uri.parse("file://" + destination);

            File file = new File(destination);
            if (file.exists())
                file.delete();

            Log.i("apkUrlDownload", apkUrl);
            SharedPreferences preferences = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
            int flag = preferences.getInt("unknownSource", 0);
            if (flag == 0) {
                progressDialog.showProgressDialogAPK();
            } else {
                progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
            request.setDescription("Downloading Harmony please wait!");
            request.setTitle("Harmony App");
            request.setNotificationVisibility(View.VISIBLE);

            final Uri apkUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            request.setDestinationUri(uri);

            final Date startTime;
            final DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            startTime = new Date();
            AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "Harmony App",
                    "DOWNLOAD_START", "Download starts for Harmony App", "app");

            final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            final long downloadId = manager.enqueue(request);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    try {
                        progressDialog.hideProgressDialog();
                        Toast.makeText(context, "Harmony App ডাউনলোড হয়েছে", Toast.LENGTH_SHORT).show();
                        Date startTime;
                        DateFormat dateFormat;
                        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        startTime = new Date();
                        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "Harmony App",
                                "DOWNLOADED", "Download completed for Harmony App", "app");
                        AsyncResponse asyncResponse = (AsyncResponse) context;
                        asyncResponse.processFinish("completeAPK");
                        Log.d("onComplete", "onComplete");
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.setDataAndType(apkUri,
                                manager.getMimeTypeForDownloadedFile(downloadId));
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(install);
                        context.unregisterReceiver(this);
                    } catch (Exception e) {
                        AsyncResponse asyncResponse = (AsyncResponse) context;
                        asyncResponse.processFinish("completeAPK");
                        Log.e("ErrrInDownloadApk", e.toString());
                        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "Harmony App",
                                "DOWNLOAD_FAILED", e.toString(), "app");
                    }
                }
            };
            context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }else{
            Toast.makeText(context, "ডাওনলোড এর পূর্বে ডিভাইস এক্সেস দিয়ে নিন", Toast.LENGTH_SHORT).show();
        }
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }
}
