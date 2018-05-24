package lct.mysymphony.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import lct.mysymphony.Activity.ShowOnlyImage;
import lct.mysymphony.BuildConfig;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForMyItemFragment extends RecyclerView.Adapter<RecyclerAdapterForMyItemFragment.RecyclerViewHolder> {

    Activity activity;
    /*ArrayList<Bitmap> bitmapArrayList;*/
    ArrayList<String> contentSdCardUrl;
    ArrayList<DataBaseData> dataHelperArrayList;
   ArrayList<String> contentDownloadTimestampArrayList;
    public RecyclerAdapterForMyItemFragment(Activity activity, ArrayList<DataBaseData> dataHelperArrayList,ArrayList<String> contentSdCardUrl,ArrayList<String> contentDownloadTimestampArrayLis) {
        this.activity = activity;
        /*this.bitmapArrayList = bitmapArrayList;*/
        this.dataHelperArrayList = dataHelperArrayList;
        this.contentSdCardUrl=contentSdCardUrl;
        this.contentDownloadTimestampArrayList=contentDownloadTimestampArrayLis;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_my_item_in_user_profile, parent, false);
        /*RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, dataHelperArrayList, bitmapArrayList);*/
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, dataHelperArrayList,contentSdCardUrl,contentDownloadTimestampArrayList);
        return recyclerViewHolder;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        String contentType = dataHelperArrayList.get(position).getContentType();
        holder.contentDownloadTimestampTV.setText(contentDownloadTimestampArrayList.get(position));
      /*  if (dataHelperArrayList.get(position).getContentStatus().contains("free"))
            holder.downloadIcon.setVisibility(View.GONE);*/
        String text;
        if (contentType.contains("image"))
            text = "ছবি";
        else if (contentType.contains("apk")) {
            text = "গেম";
        } else if (contentType.contains("video")) {
            text = "ভিডিও";
        } else if (contentType.contains("audio")) {
            text = "গান";
        } else {
            text = "ছবি";
        }
        holder.textView.setText(text);

        /*if (contentType.contains("audio")||contentType.contains("video"))
        {*/
            Glide.with(activity)
                    .load(dataHelperArrayList.get(position).getThumbNailImgUrl())
                    .into(holder.imageView);
       /* }
        else
        {
            Glide.with(activity)
                    .load(bitmapArrayList.get(position))
                    .into(holder.imageView);
        }*/

    }

    @Override
    public int getItemCount() {
        /*return bitmapArrayList.size();*/
        return dataHelperArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;
        Activity activity;
        TextView textView,contentDownloadTimestampTV;
        ArrayList<DataBaseData> dataHelperArrayList;
        private ArrayList<String> contentDownloadTimestampArrayList;
       /* ArrayList<Bitmap> bitmapArrayList;*/
       ArrayList<String> contentSdCardUrl;
        /*ImageView downloadIcon;*/
       ImageView shareIcon;
        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<DataBaseData> dataHelperArrayList,final ArrayList<String> contentSdCardUrl,final ArrayList<String> contentDownloadTimestampArrayList) {
            super(view);
            this.activity = activity;
            this.dataHelperArrayList = dataHelperArrayList;
            this.contentDownloadTimestampArrayList=contentDownloadTimestampArrayList;
       /*     this.bitmapArrayList = bitmapArrayList;*/
            imageView = view.findViewById(R.id.imgMyitemFragment);
            textView = view.findViewById(R.id.titleMyItemFragment);
            cardView = view.findViewById(R.id.myItemFragmentCardView);
            shareIcon = view.findViewById(R.id.shareIcon);
            contentDownloadTimestampTV=view.findViewById(R.id.contentDownloadTimestampTV);
            view.setDrawingCacheEnabled(false);
            this.contentSdCardUrl=contentSdCardUrl;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String contentType=dataHelperArrayList.get(getAdapterPosition()).getContentType();
                    String root = Environment.getExternalStorageDirectory().toString();
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File file = null;
                    if (contentType.contains("apk"))
                    {
                        Log.d("contentTypeApk","contentTypeApk");
                        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+contentSdCardUrl.get(getAdapterPosition());
                        try {
                            file = new File(destination);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("fileProblemInApk","fileProblemInApk");
                        }
                    }
                    else {
                        file = new File(root + "/mySymphony/" + contentSdCardUrl.get(getAdapterPosition()));
                    }
                    if (file.exists())
                    {
                        if (contentType.contains("image"))
                        {
                            if (dataHelperArrayList.get(getAdapterPosition()).getContentCat().contains("moving_image"))
                                intentShareFile.setType("image/gif");
                            else
                            {
                                intentShareFile.setType("image/jpg");
                                Log.d("contentype","image/jpg");
                            }
                        }
                        else if (contentType.contains("apk")) {
                            intentShareFile.setType("application/apk");
                        } else if (contentType.contains("video")) {
                            Log.d("videoEnter","videoEnter");
                            /*Uri uri = Uri.fromFile(file);
                            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intentShareFile.putExtra(Intent.EXTRA_STREAM,uri);*/
                            intentShareFile.setType("video/mp4");

                        } else if (contentType.contains("audio")) {
                            intentShareFile.setType("audio/mp3");
                        } else {
                            intentShareFile.setType("audio/mp3");
                        }

                        /*intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));*/
                        final Uri fileUri = FileProvider.getUriForFile(activity,
                                BuildConfig.APPLICATION_ID + ".provider",
                                file);
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        activity.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                    else
                    {
                        Log.d("fileNotExist","fileNotExist");
                        Toast.makeText(activity, "দুঃখিত কন্টেন্টটি খুজে পাওয়া যাচ্ছেনা", Toast.LENGTH_SHORT).show();
                    }
                    /*view.setDrawingCacheEnabled(false);
                    if (dataHelperArrayList.get(getAdapterPosition()).getContentStatus().contains("free")) {
                        Intent myIntent = new Intent(activity, ShowOnlyImage.class);
                        myIntent.putExtra("id", getAdapterPosition());
                        activity.startActivity(myIntent);
                    } else {
                        Toast.makeText(activity, "এখন ডাউনলোড করার সুযোগটি নেই\n" + "পেমেন্ট যাচাই করা হলে ডাউললোড করার সুযোগ পাবেন", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });
        }
    }
}
