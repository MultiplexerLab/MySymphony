package lct.mysymphony.helper;

import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadVideo {

    public String downloadVideo(String url)
    {
        String video = "";
        URL u = null;
        InputStream is = null;

        Log.i("url", url);

        try {
            u = new URL(url);
            is = u.openStream();
            HttpURLConnection huc = (HttpURLConnection)u.openConnection();

            if(huc != null)
            {
                InputStream inputStream =  huc.getInputStream();
                byte[] buff = new byte[8000];
                int bytesRead = 0;

                ByteArrayOutputStream bao = new ByteArrayOutputStream();

                while((bytesRead = inputStream.read(buff)) != -1) {
                    bao.write(buff, 0, bytesRead);
                }
                byte[] data = bao.toByteArray();
                video = Base64.encodeToString(data, Base64.DEFAULT);
                Log.i("videoStr", video);
            }
        }catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(is != null){
                    is.close();
                }
            }catch (IOException ioe) {

            }
        }
        return video;
    }
}
