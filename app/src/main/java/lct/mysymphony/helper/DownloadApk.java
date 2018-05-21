package lct.mysymphony.helper;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.Random;

import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.BuildConfig;

public class DownloadApk {
    Context context;
    String apkUrl;

    public void downLoadAPK(final String apkUrl, final Context context) {
        this.context = context;
        this.apkUrl = apkUrl;

        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "apk"+ n +".apk";
        destination += fname;
        final Uri uri = Uri.parse("file://" + destination);

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
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(apkUri,
                            manager.getMimeTypeForDownloadedFile(downloadId));
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(install);
                    Log.i("Running", apkUri.toString());
                    context.unregisterReceiver(this);
                }catch (Exception e){
                    Log.e("Errr", e.toString());
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
