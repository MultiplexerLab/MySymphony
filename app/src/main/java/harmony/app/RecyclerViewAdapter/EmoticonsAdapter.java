package harmony.app.RecyclerViewAdapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.looselycoupled.subscriptionintegration.OnSubscriptionListener;
import com.looselycoupled.subscriptionintegration.SubscribeUsingPaymentGateway;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import harmony.app.Activity.Emoticons;
import harmony.app.Activity.PlayAudioActivity;
import harmony.app.ModelClass.DataBaseData;
import harmony.app.ModelClass.SliderImage;
import harmony.app.ModelClass.SubscriptionConfig;
import harmony.app.R;
import harmony.app.Helper.AppLogger;
import harmony.app.Helper.DataHelper;
import harmony.app.Helper.DownloadApk;
import harmony.app.Helper.DownloadImage;
import harmony.app.Helper.InsertPayment;
import harmony.app.Helper.ProgressDialog;

public class EmoticonsAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<SliderImage> emoticonList;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    SubscriptionConfig config;
    boolean isSubscribed;

    public EmoticonsAdapter(Context context, ArrayList<SliderImage> emoticonList, SubscriptionConfig config, boolean isSubscribed) {
        this.context = context;
        this.emoticonList = emoticonList;
        this.config = config;
        this.isSubscribed = isSubscribed;
        progressDialog = new harmony.app.Helper.ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return emoticonList.size();
    }

    @Override
    public Object getItem(int i) {
        return emoticonList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.emoticon_item, viewGroup, false);
        ImageView emoticon = customView.findViewById(R.id.imageEmoticon);
        final TextView emoticonTitle = customView.findViewById(R.id.textEmoticon);
        TextView priceTag = customView.findViewById(R.id.priceTag);
        ImageView downloadButton = customView.findViewById(R.id.downloadIcon);
        RelativeLayout paymentLock = customView.findViewById(R.id.paymentLock);
        RelativeLayout mainLayout = customView.findViewById(R.id.mainLayout);

        final String emoticonUrl = emoticonList.get(position).getContentUrl();
        final String title = emoticonList.get(position).getContentTitle();
        final int price = emoticonList.get(position).getContentPrice();
        Glide.with(context).load(emoticonUrl).into(emoticon);
        emoticonTitle.setText(emoticonList.get(position).getContentTitle());

        final SliderImage emoticonObj = emoticonList.get(position);

        if(price>0){
            DataHelper dataHelper = new DataHelper(context);
            Boolean check = dataHelper.checkDownLoadedOrNot(emoticonObj.getContentCat(), emoticonObj.getId());
            Log.d("CheckEmoticon", check.toString());
            if (dataHelper.checkDownLoadedOrNot(emoticonObj.getContentCat(), emoticonObj.getId()) || isSubscribed) {
                Log.d("enter", "emoticon");
                priceTag.setVisibility(View.INVISIBLE);
                paymentLock.setVisibility(View.INVISIBLE);
            }else{
                priceTag.setText("৳"+price);
                priceTag.setVisibility(View.VISIBLE);
                paymentLock.setVisibility(View.VISIBLE);
                mainLayout.setAlpha((float) 0.5);
            }

        }

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor mPrefs = context.getSharedPreferences("tempEmoticon", Context.MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String json = gson.toJson(emoticonList.get(position));
                mPrefs.putString("emoticonObj", json);
                mPrefs.putString("emoticonUrl", emoticonUrl);
                mPrefs.commit();

                final SliderImage emoticon = emoticonList.get(position);
                final DataBaseData dataBaseData = new DataBaseData(emoticon.getContentTitle(), emoticon.getContentCat(), emoticon.getContentType(),
                        emoticon.getContentDescription(), emoticon.getContentUrl(), "free", emoticon.getId());

                DataHelper dataHelper = new DataHelper(context);
                Boolean check = dataHelper.checkDownLoadedOrNot(emoticonObj.getContentCat(), emoticonObj.getId());
                Log.d("CheckEmoticon", check.toString());
                if (dataHelper.checkDownLoadedOrNot(emoticonObj.getContentCat(), emoticonObj.getId())) {
                    Toast.makeText(context, "আপনি এই কন্টেন্ট ডাউনলোড করেছেন ", Toast.LENGTH_SHORT).show();
                }else {
                    if (checkPermissions()) {
                        if (price > 0 && !isSubscribed) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        final Date currenTime;
                                        final DateFormat dateFormat;
                                        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                        currenTime = new Date();
                                        final String deviceId = Settings.Secure.getString(context.getContentResolver(),
                                                Settings.Secure.ANDROID_ID);
                                        AppLogger.insertLogs(context, dateFormat.format(currenTime), "N", emoticon.getId()+"",
                                                "PAYMENT_INITIATED", deviceId, "content");
                                        SubscribeUsingPaymentGateway obj = new SubscribeUsingPaymentGateway();
                                        obj.setData("test", "test123", "1234", (float) price, deviceId, emoticon.getContentTitle(), context, new OnSubscriptionListener() {
                                            @Override
                                            public void onSuccess(JSONObject result) {
                                                try {
                                                    final String deviceId = Settings.Secure.getString(context.getContentResolver(),
                                                            Settings.Secure.ANDROID_ID);
                                                    String transactionStatus = result.getString("transactionStatus");
                                                    String paymentID = result.getString("paymentID");
                                                    String paymentMethod = result.getString("paymentMethod");
                                                    String referenceCode = result.getString("referenceCode");
                                                    Long amount = result.getLong("amount");
                                                    if (transactionStatus.equals("Completed")) {
                                                        DownloadImage downloadImage = new DownloadImage();
                                                        downloadImage.downloadImage(emoticonUrl, context, dataBaseData);
                                                        InsertPayment.insertPayment(context, emoticon.getId(), amount, paymentID, paymentMethod, referenceCode, deviceId, emoticon.getContentTitle());
                                                        AppLogger.insertLogs(context, dateFormat.format(currenTime), "N", emoticon.getId() + "",
                                                                "PAYMENT_DONE", result.getString("paymentMethod"), "content");

                                                        Toast.makeText(context, "আপনার পেমেন্ট সফল হয়েছে, ডাউনলোড হচ্ছে", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Log.i("tranResult", "transactionResult: " + result);
                                            }

                                            @Override
                                            public void onError(JSONObject result) {
                                                AppLogger.insertLogs(context, dateFormat.format(currenTime), "N", emoticon.getId() + "",
                                                        "PAYMENT_FAILED", result.toString(), "content");

                                                Log.e("tranError", "transactionResult: " + result);

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                        } else {
                            DownloadImage downloadImage = new DownloadImage();
                            downloadImage.downloadImage(emoticonUrl, context, dataBaseData);
                        }
                    } else {
                        Toast.makeText(context, "ডাওনলোড এর পূর্বে ডিভাইস এক্সেস দিয়ে নিন", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("DownloadEmoticon", emoticonUrl);
                }

            }
        });
        return customView;
    }

    private void initSubscription(int price) {

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
