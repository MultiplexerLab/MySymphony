package harmony.app.helper;

import android.content.Context;
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

import harmony.app.ModelClass.DataBaseData;

public class DownloadVideo {

    Context context;
    String videoUrl;
    String videoTitle;
    private DataBaseData dataBaseData;
    String contentSdCardUrl;
    DataHelper dbHelper;

    public void downloadVideo(String videoUrl, Context context, DataBaseData dataBaseData) {
        this.context = context;
        this.videoUrl = videoUrl;
        dbHelper = new DataHelper(context);
        this.dataBaseData = dataBaseData;
        videoTitle=dataBaseData.getContentTitle();
        Log.i("DonwloadVideoEnter", "Downloading the video. Please wait...");
        DownloadVideo.RetrieveVideoTask bt = new DownloadVideo.RetrieveVideoTask();
        bt.execute(videoUrl);
    }
    class RetrieveVideoTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        protected void onPreExecute() {
            Log.i("DonwloadVideo", "Downloading the video. Please wait...");
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
                    saveVideo(bao);
                    /*byte[] data = bao.toByteArray();
                    video = Base64.encodeToString(data, Base64.DEFAULT);
                    Log.i("videoStr", video);*/
                }
            } catch (MalformedURLException mue) {
                Log.e("URLException", mue.toString());
            } catch (IOException ioe) {
                Log.e("IOExceptionVideo", ioe.toString());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {

                }
            }
            return video;
        }

        protected void onPostExecute(String result) {
            Log.d("postExecuteVideo","postExecuteVideo");
            DownloadImage.AsyncResponse asyncResponse = (DownloadImage.AsyncResponse) context;
            asyncResponse.processFinish("complete");
            ///dbHelper.insertVideoStr(result, dataBaseData);
        }
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private void saveVideo(ByteArrayOutputStream bao) {
        Log.d("entersaveVideo","entersaveVideo");
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/appstore");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname;
        /*if (videoTitle.length()>0)
            fname = videoTitle +"-"+n+".mp4";
        else*/
            fname = "Video-"+ n +".mp4";
        /*fname.replaceAll(" ","_");*/
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bao.toByteArray());
            out.flush();
            out.close();
            contentSdCardUrl=fname;
            insertDataInDatabaseWithContentSdcardUl();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("saveVideoExcptn",e.toString());
        }
    }
    public void insertDataInDatabaseWithContentSdcardUl()
    {
        Log.d("enterInsertVideoToDB","enterInsertVideoToDB");
        dbHelper.insertContentDataWithSdCardUrl(contentSdCardUrl,dataBaseData);
    }
}
