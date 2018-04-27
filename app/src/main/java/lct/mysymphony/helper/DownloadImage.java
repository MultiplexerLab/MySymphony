package lct.mysymphony.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lct.mysymphony.Activity.MainActivity;

public class DownloadImage {

    Context context;

    public void downloadImage(String imgURL, Context context){
        this.context = context;
        Log.i("downloadImage","Inside download image");
        DownloadImage.BackTask bt= new DownloadImage.BackTask();
        if(!imgURL.trim().equals("")){
            bt.execute(imgURL);
        }
    }

    private class BackTask extends AsyncTask<String,Void,Bitmap> {
        TextView tv;
        DataHelper dbHelper = new DataHelper(context);
        protected void onPreExecute(){
            Log.i("Donwload","Downloading the image. Please wait...");
        }
        protected Bitmap doInBackground(String...params){
            Bitmap bitmap=null;
            try {
                // Download the image
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                // Decode image to get smaller image to save memory
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize=4;
                bitmap = BitmapFactory.decodeStream(is,null, options);
                is.close();
            }
            catch(IOException e){
                return null;
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result){
            //tv.setVisibility(TextView.GONE);
            // Insert bitmap to the database
            dbHelper.insertBitmap(result);
            //ImageView imgView=(ImageView)findViewById(R.id.imgview);
            // Read the first image from database and show it in ImageView
            //imgView.setImageBitmap(dbHelper.getBitmap(1));
            ///Log.i("downlaoded image", dbHelper.getBitmap(1).toString());
        }
    }
}