package lct.mysymphony.helper;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import lct.mysymphony.Activity.MainActivity;
import lct.mysymphony.ModelClass.DataBaseData;

public class DownloadImage {

    Context context;
    DataBaseData dataBaseData;

    public void downloadImage(String imgURL, Context context, DataBaseData dataBaseData) {
        this.context = context;
        Log.i("downloadImage", "Inside download image");
        this.dataBaseData = dataBaseData;

        DownloadImage.BackTask bt = new DownloadImage.BackTask();
        if (!imgURL.trim().equals("")) {
            bt.execute(imgURL);
        }
        else
            Log.d("noContain","noContain");
    }

    private class BackTask extends AsyncTask<String, Void, Bitmap> {
        TextView tv;
        DataHelper dbHelper = new DataHelper(context);

        protected void onPreExecute() {
            Log.i("Donwload", "Downloading the image. Please wait...");
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeStream(is, null, options);
                saveImage(bitmap);
                is.close();
            } catch (IOException e) {
                return null;
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            AsyncResponse asyncResponse = (AsyncResponse) context;
            asyncResponse.processFinish("complete");
            dbHelper.insertBitmap(result, dataBaseData);
        }
    }
    public interface AsyncResponse {
        void processFinish(String output);
    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/mySymphony/");
        try{
            dir.mkdir();
            Log.i("Folder", "Directory Created");
        }catch(Exception e){
           Log.e("FolderErr", e.toString());
        }
        /*if (!myDir.exists()) {
            boolean flag = myDir.mkdirs();
            Log.i("Folder", String.valueOf(flag));
        }else{
            Log.i("Folder", "Created");
        }*/
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "img"+ n +".jpg";
        File file = new File (dir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            Log.e("ErrorImage", e.toString());
        }
    }
}
