package harmony.app.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import harmony.app.BuildConfig;
import harmony.app.ModelClass.DataBaseData;

public class DownloadAudio {

    Context context;
    String audioUrl;
    String audioTitle;
    private DataBaseData dataBaseData;
    String contentSdCardUrl;
    DataHelper dbHelper;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    /*public void downloadAudio(String audioUrl, String songTitle, Context context, DataBaseData dataBaseData) {
        this.context = context;
        this.audioUrl = audioUrl;
        this.dataBaseData = dataBaseData;

        Date startTime;
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        startTime = new Date();
        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "DownloadAudio",
                "Start", "Download starts for " + dataBaseData.getContentTitle());

        dbHelper = new DataHelper(context);
        if (dataBaseData == null) {
            Log.d("dataBaseDataNull", "dataBaseDataNull");
            audioTitle = songTitle;
        } else {
            audioTitle = dataBaseData.getContentTitle();
        }
        Log.i("DonwloadAudioEnter", "Downloading the Audio. Please wait...");
        DownloadAudio.RetrieveAudioTask bt = new DownloadAudio.RetrieveAudioTask();
        bt.execute(audioUrl);
    }

    class RetrieveAudioTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            Log.i("DonwloadAudio", "Downloading the Audio. Please wait...");
            *//*DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioUrl));
            request.setDescription("Downloading the " + dataBaseData.getContentTitle() + " please wait!");
            request.setTitle(dataBaseData.getContentTitle());*//*
        }

        protected String doInBackground(String... urls) {
            String video = "";
            InputStream is = null;

            try {
                URL url = new URL(urls[0]);
                Log.i("urlAudio", url.toString());
                is = url.openStream();
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();

                if (huc != null) {
                    InputStream inputStream = huc.getInputStream();
                    byte[] buff = new byte[8000];
                    int bytesRead = 0;
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    while ((bytesRead = inputStream.read(buff)) != -1) {
                        bao.write(buff, 0, bytesRead);
                    }
                    saveAudio(bao);
                }
            } catch (MalformedURLException mue) {
                Log.e("URLException", mue.toString());
            } catch (IOException ioe) {
                Log.e("IOExceptionAudio", ioe.toString());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                    Log.e("IOExceptionAudio2", ioe.toString());
                }
            }
            return video;
        }

        protected void onPostExecute(String result) {
            Log.d("postExecuteAudio", "postExecuteAudio");
            DownloadAudio.AsyncResponse asyncResponse = (DownloadAudio.AsyncResponse) context;
            asyncResponse.processFinish("complete");
            Date startTime;
            DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            startTime = new Date();
            AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "DownloadAudio",
                    "Finish", "Download completed for " + dataBaseData.getContentTitle());
            *//* dbHelper.insertAudioStr(result, dataBaseData);*//*
        }
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private void saveAudio(ByteArrayOutputStream bao) {
        Log.d("entersaveAudio", "entersaveAudio");
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/appstore");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname;
        *//*if (audioTitle.length()>0)
            fname = audioTitle+"-"+n +".mp3";
        else*//*
        fname = "Audio-" + n + ".mp3";
        fname.replaceAll(" ", "_");
        contentSdCardUrl = fname;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bao.toByteArray());
            out.flush();
            out.close();
            insertDataInDatabaseWithContentSdcardUl();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("saveAudioExcptn", e.toString());
        }
    }

    public void insertDataInDatabaseWithContentSdcardUl() {
        Log.d("enterInsertVideoToDB", "enterInsertVideoToDB");
        if (dataBaseData == null)
            Log.d("dataBaseDataNull2", "dataBaseDataNull");
        dbHelper.insertContentDataWithSdCardUrl(contentSdCardUrl, dataBaseData);
    }*/

    public void downloadAudio(String audioUrl, String songTitle, final Context context, final DataBaseData dataBaseData) {
        this.context = context;
        this.audioUrl = audioUrl;
        this.dataBaseData = dataBaseData;

        dbHelper = new DataHelper(context);

        if (checkPermissions()) {
            final Date startTime;
            final DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            startTime = new Date();
            AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "" + dataBaseData.getContentId(),
                    "DOWNLOAD_START", "Download starts for " + dataBaseData.getContentTitle(), "content");

            String destination = Environment.getExternalStorageDirectory().toString() + "/appstore";
            File myDir = new File(destination);
            myDir.mkdirs();

            final String fname = "/" + songTitle + ".mp3";
            destination += fname;

            contentSdCardUrl = fname;
            final Uri uri = Uri.parse("file://" + destination);

            File file = new File(destination);
            if (file.exists())
                file.delete();

            Log.i("audioToDownload", audioUrl);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioUrl));
            request.setDescription("Downloading the Song please wait!");
            request.setTitle(dataBaseData.getContentTitle());

            final Uri audioUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            request.setDestinationUri(uri);

            final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            final long downloadId = manager.enqueue(request);

            insertDataInDatabaseWithContentSdcardUl();
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    try {
                        Date startTime;
                        DateFormat dateFormat;
                        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        startTime = new Date();

                        Log.d("onComplete", "onComplete");
                        contentSdCardUrl = fname;
                        //insertDataInDatabaseWithContentSdcardUl();
                        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "" + dataBaseData.getContentId(),
                                "DOWNLOADED", "Download completed for " + dataBaseData.getContentTitle(), "content");

                        context.unregisterReceiver(this);
                    } catch (Exception e) {
                        Log.e("ErrrInDownloadApk", e.toString());
                        AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", dataBaseData.getContentId() + "",
                                "DOWNLOAD_FAILED", e.toString(), "content");
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

    public void insertDataInDatabaseWithContentSdcardUl() {
        Log.d("contentSdCardUrl", contentSdCardUrl);
        dbHelper.insertContentDataWithSdCardUrl(contentSdCardUrl, dataBaseData);
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

}
