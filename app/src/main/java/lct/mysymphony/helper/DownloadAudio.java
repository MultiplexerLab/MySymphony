package lct.mysymphony.helper;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.Random;

import lct.mysymphony.ModelClass.DataBaseData;

public class DownloadAudio {

    Context context;
    String audioUrl;
    String audioTitle;
    private DataBaseData dataBaseData;
    String contentSdCardUrl;
    DataHelper dbHelper;

   /* public void downloadAudio(String audioUrl, Context context, String title) {
        this.context = context;
        this.audioUrl = audioUrl;
        dbHelper = new DataHelper(context);
        audioTitle=title;
        Log.i("DonwloadAudioEnter", "Downloading the Audio. Please wait...");
        DownloadAudio.RetrieveAudioTask bt = new DownloadAudio.RetrieveAudioTask();
        bt.execute(audioUrl);
    }*/

    public void downloadAudio(String audioUrl, Context context, DataBaseData dataBaseData) {
        this.context = context;
        this.audioUrl = audioUrl;
        this.dataBaseData = dataBaseData;
        dbHelper=new DataHelper(context);
        if (dataBaseData==null)
            Log.d("dataBaseDataNull","dataBaseDataNull");
        audioTitle=dataBaseData.getContentTitle();
        Log.i("DonwloadAudioEnter", "Downloading the Audio. Please wait...");
        DownloadAudio.RetrieveAudioTask bt = new DownloadAudio.RetrieveAudioTask();
        bt.execute(audioUrl);
    }
    class RetrieveAudioTask extends AsyncTask<String, Void, String> {

        private Exception exception;


        protected void onPreExecute() {
            Log.i("DonwloadAudio", "Downloading the Audio. Please wait...");
        }
        protected String doInBackground(String... urls) {
            String video = "";
            InputStream is = null;

            try {
                URL url = new URL(urls[0]);
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
                    //byte[] data = bao.toByteArray();
                    /*video = Base64.encodeToString(data, Base64.DEFAULT);*/
                    ///Log.i("audioStr", video);
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
            Log.d("postExecuteAudio","postExecuteAudio");
            DownloadAudio.AsyncResponse asyncResponse = (DownloadAudio.AsyncResponse) context;
            asyncResponse.processFinish("complete");
           /* dbHelper.insertAudioStr(result, dataBaseData);*/
        }
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private void saveAudio(ByteArrayOutputStream bao) {
        Log.d("entersaveAudio","entersaveAudio");
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
            fname = "Audio-"+ n +".mp3";
        fname.replaceAll(" ","_");
        contentSdCardUrl=fname;
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bao.toByteArray());
            out.flush();
            out.close();
            insertDataInDatabaseWithContentSdcardUl();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("saveAudioExcptn",e.toString());
        }
    }
    public void insertDataInDatabaseWithContentSdcardUl()
    {
        Log.d("enterInsertVideoToDB","enterInsertVideoToDB");
        if (dataBaseData==null)
            Log.d("dataBaseDataNull2","dataBaseDataNull");
        dbHelper.insertContentDataWithSdCardUrl(contentSdCardUrl,dataBaseData);
    }
}
