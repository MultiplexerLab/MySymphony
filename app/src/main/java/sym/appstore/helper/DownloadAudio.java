package sym.appstore.helper;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

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
import java.util.Date;
import java.util.Random;

import sym.appstore.ModelClass.DataBaseData;

public class DownloadAudio {

    Context context;
    String audioUrl;
    String audioTitle;
    private DataBaseData dataBaseData;
    String contentSdCardUrl;
    DataHelper dbHelper;

    public void downloadAudio(String audioUrl, String songTitle, Context context, DataBaseData dataBaseData) {
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
            /*DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioUrl));
            request.setDescription("Downloading the " + dataBaseData.getContentTitle() + " please wait!");
            request.setTitle(dataBaseData.getContentTitle());*/
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
            /* dbHelper.insertAudioStr(result, dataBaseData);*/
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
        /*if (audioTitle.length()>0)
            fname = audioTitle+"-"+n +".mp3";
        else*/
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
    }
}