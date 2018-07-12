package harmony.app.Helper;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import harmony.app.ModelClass.DataBaseData;

public class DownloadImage {

    Context context;
    DataBaseData dataBaseData;
    String imageUrl;
    String contentSdCardUrl;
    DataHelper dbHelper;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public void downloadImage(String imgURL, Context context, DataBaseData dataBaseData) {
        this.context = context;
        this.imageUrl = imgURL;
        dbHelper = new DataHelper(context);
        Log.i("downloadImage", context.toString());
        this.dataBaseData = dataBaseData;
        if (checkPermissions()) {
            Date currenTime;
            DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            currenTime = new Date();
            AppLogger.insertLogs(context, dateFormat.format(currenTime), "Y", "" + dataBaseData.getContentId(),
                    "DOWNLOAD_START", "Image download starts: " + dataBaseData.getContentTitle(), "content");

            DownloadImage.BackTask bt = new DownloadImage.BackTask();
            if (!imgURL.trim().equals("")) {
                bt.execute(imgURL);
            } else {
                Log.d("noContain", "noContain");
            }
        }else{
            Toast.makeText(context, "ডাওনলোড এর পূর্বে ডিভাইস এক্সেস দিয়ে নিন", Toast.LENGTH_SHORT).show();
        }
    }

    /*public void downloadImage(String imgURL, Context context, SliderImage sliderImage) {
        this.context = context;
        this.imageUrl=imgURL;
        dbHelper = new DataHelper(context);
        Log.i("downloadImage", context.toString());
        this.sliderImage = sliderImage;
        Date currenTime;
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(context, dateFormat.format(currenTime), "Y", "ImageDownload",
                "DownloadStart", "Image download from "+context.toString());

        DownloadImage.BackTask bt = new DownloadImage.BackTask();
        if (!imgURL.trim().equals("")) {
            bt.execute(imgURL);
        }
        else
            Log.d("noContain","noContain");
    }*/

    private class BackTask extends AsyncTask<String, Void, Bitmap> {
        TextView tv;

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
            Date currenTime;
            DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            currenTime = new Date();
            AppLogger.insertLogs(context, dateFormat.format(currenTime), "Y", "" + dataBaseData.getContentId(),
                    "DOWNLOADED", "Image Downloaded: " + dataBaseData.getContentTitle(), "content");
            /*dbHelper.insertBitmap(result, dataBaseData);*/
        }
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/appstore");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        fname.replaceAll(" ", "_");
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            contentSdCardUrl = fname;
            setImageUrlAsThumbnailImageUrl();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageUrlAsThumbnailImageUrl() {
        DataBaseData dataBaseData2 = new DataBaseData(dataBaseData.getContentTitle(), dataBaseData.getContentCat(), dataBaseData.getContentType(), dataBaseData.getContentDesc(), imageUrl, dataBaseData.getContentStatus(), dataBaseData.getContentId());
        this.dataBaseData = dataBaseData2;
        insertDataInDatabaseWithContentSdcardUl();
    }

    public void insertDataInDatabaseWithContentSdcardUl() {
        Log.d("enterInsertImageToDB", "enterInsertImageToDB");
        dbHelper.insertContentDataWithSdCardUrl(contentSdCardUrl, dataBaseData);
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


}
