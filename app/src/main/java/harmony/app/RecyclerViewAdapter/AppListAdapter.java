package harmony.app.RecyclerViewAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import harmony.app.Activity.AppList;
import harmony.app.ModelClass.AppData;
import harmony.app.ModelClass.DataBaseData;
import harmony.app.R;
import harmony.app.helper.AppLogger;
import harmony.app.helper.DownloadApk;
import harmony.app.helper.ProgressDialog;

public class AppListAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<AppData> appData;

    public AppListAdapter(Context context, ArrayList<AppData> appData) {
        this.context = context;
        this.appData = appData;
        progressDialog = new harmony.app.helper.ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return appData.size();
    }

    @Override
    public Object getItem(int i) {
        return appData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.app_list_item, viewGroup, false);
        ImageView appThumbnail = customView.findViewById(R.id.appCoverImage);
        //Log.i("backgroundUrl", appData.get(position).getThumbNailImage());
        final TextView apptitle = customView.findViewById(R.id.appTitle);
        //TextView appDesc = customView.findViewById(R.id.appDescription);
        Button installButton = customView.findViewById(R.id.buttonInstall);
        Button payButton = customView.findViewById(R.id.buttonPay);
        String imageUrl = appData.get(position).getThumbNail_image();
        final int price = appData.get(position).getContentPrice();
        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.equals("null")) {

        } else {
            Glide.with(context).load(appData.get(position).getThumbNail_image()).into(appThumbnail);
        }

        if(price>0){
            payButton.setVisibility(View.VISIBLE);
            payButton.setText("BDT "+price);
        }

        apptitle.setText(appData.get(position).getContentTitle());

        String packageName = appData.get(position).getReference1();
        if (isPackageExisted(packageName)) {
            PackageInfo pinfo = null;
            try {
                pinfo = context.getPackageManager().getPackageInfo(packageName, 0);
                int versionNumber = pinfo.versionCode;
                Log.i("VersionCode", packageName+"  "+versionNumber);
                String versionCodeStr = appData.get(position).getReference3();
                if (versionCodeStr == null || versionCodeStr.equals("null")) {

                } else {
                    if (versionNumber == Integer.parseInt(appData.get(position).getReference3())) {
                        installButton.setEnabled(false);
                        installButton.setText("Installed");
                        installButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        installButton.setEnabled(true);
                        installButton.setText("UPDATE");
                        installButton.setBackgroundColor(context.getResources().getColor(R.color.dark_red));
                    }
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        ExpandableTextView expTv1 = (ExpandableTextView) customView.findViewById(R.id.cardViewAppList)
                .findViewById(R.id.expand_text_view);
        if (appData.get(position).getContentDescription().isEmpty()) {
            expTv1.setText("No Description");
        } else {
            expTv1.setText(appData.get(position).getContentDescription());
        }

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(price>0){
                    Toast.makeText(context, "ইন্সটল এর পূর্বে পেমেন্ট সম্পন্ন করুন", Toast.LENGTH_LONG).show();
                }else{
                    Date startTime;
                    DateFormat dateFormat;
                    dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    startTime = new Date();
                    AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "Start Downloading",
                            "INSTALL", "Install Button Clicked for " + apptitle, "app");

                    String apkUrl = appData.get(position).getContentUrl();
                    String appTitle = appData.get(position).getContentTitle();
                    Log.i("DownloadAPK", apkUrl);
                    DataBaseData dataBaseData = new DataBaseData(appTitle, "mobile_app","apk", appData.get(position).getContentDescription(),
                            appData.get(position).getThumbNail_image(), "free", Integer.parseInt(appData.get(position).getId()));
                    DownloadApk downloadApk = new DownloadApk();
                    downloadApk.downLoadAPK(apkUrl, context, dataBaseData);
                }
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Sure?").setMessage("Are you sure you want to pay "+price+ " BDT?").setCancelable(false);
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initSubscription(price);
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();

            }
        });
        return customView;
    }

    private void initSubscription(final int price) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SubscribeUsingPaymentGateway obj = new SubscribeUsingPaymentGateway();
                    obj.setData("test","test123","1234", (float) price, context, new OnSubscriptionListener() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String transactionStatus = result.getString("transactionStatus");
                                if(transactionStatus.equals("Completed")){
                                    Toast.makeText(context, "আপনার পেমেন্ট সফল হয়েছে", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("tranResult","transactionResult: "+result);
                        }

                        @Override
                        public void onError(JSONObject result) {
                            Log.e("tranError","transactionResult: "+result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public boolean isPackageExisted(String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            Log.i("PackageIstalled", packageInfo.packageName.toString());
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

}
