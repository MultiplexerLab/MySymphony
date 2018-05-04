package lct.mysymphony.Activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import lct.mysymphony.R;
import lct.mysymphony.helper.DataHelper;

public class ShowOnlyImage extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    ImageView imageView;
    int id;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_only_image);

        toolbar = findViewById(R.id.toolbarlayoutinonlyimage);
        setSupportActionBar(toolbar);
        id = getIntent().getIntExtra("id",0);

        imageView=findViewById(R.id.imageInOnlyImage);
        new RetriveBitMapFromDatabase().execute();
        Log.d("idImage",Integer.toString(id));

    }


    private class RetriveBitMapFromDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            DataHelper dataHelper=new DataHelper(ShowOnlyImage.this);
            bitmap=dataHelper.getBitmap(id+1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setImage();

        }
    }

    private void setImage() {

        Glide.with(ShowOnlyImage.this)
                .load(bitmap)
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        myIntent.putExtra("cameFromWhichActivity","ShowOnlyImage");
        
        this.startActivity(myIntent);
        //overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    public void setAsAWallpaper(View view) {
        View drawingView = imageView;
        drawingView.buildDrawingCache(true);
        Bitmap bitmap = drawingView.getDrawingCache(true).copy(Bitmap.Config.RGB_565, false);
        drawingView.destroyDrawingCache();

        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("exceptionImage",e.toString());
        }
        Toast.makeText(this, "ওয়ালপেপার পরিবর্তন করা হয়েছে", Toast.LENGTH_SHORT).show();
    }

}
