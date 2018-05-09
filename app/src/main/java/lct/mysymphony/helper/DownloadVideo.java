package lct.mysymphony.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import lct.mysymphony.ModelClass.DataBaseData;

public class DownloadVideo {

    Context context;
    String videoUrl;
    private DataBaseData dataBaseData;

    public void downloadVideo(String videoUrl, Context context, DataBaseData dataBaseData) {
        this.context = context;
        this.videoUrl = videoUrl;
        this.dataBaseData = dataBaseData;
        DownloadVideo.RetrieveVideoTask bt = new DownloadVideo.RetrieveVideoTask();
        bt.execute(videoUrl);
    }

    class RetrieveVideoTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        DataHelper dbHelper = new DataHelper(context);


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
                    byte[] data = bao.toByteArray();
                    video = Base64.encodeToString(data, Base64.DEFAULT);
                    Log.i("videoStr", video);
                }
            } catch (MalformedURLException mue) {
                Log.e("URLException", mue.toString());
            } catch (IOException ioe) {
                Log.e("IOException", ioe.toString());
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
            DownloadImage.AsyncResponse asyncResponse = (DownloadImage.AsyncResponse) context;
            asyncResponse.processFinish("complete");
            dbHelper.insertVideoStr(result, dataBaseData);
        }
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }
}
