package harmony.app.Activity;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.looselycoupled.subscriptionintegration.OnSubscriptionListener;
import com.looselycoupled.subscriptionintegration.SubscribeUsingPaymentGateway;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import harmony.app.Helper.AppLogger;
import harmony.app.Helper.InsertPayment;
import harmony.app.Helper.SubscriptionService;
import harmony.app.ModelClass.Subscription;
import harmony.app.ModelClass.SubscriptionConfig;
import harmony.app.R;

public class SubscriptionActivity extends AppCompatActivity {

    private SubscriptionConfig subscriptionConfig;
    private TextView weeklyTV;
    private TextView monthlyTV;
    private TextView yearlyTV;
    private Button weeklyBuy;
    private Button monthlyBuy;
    private Button yearlyBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_layout);

        weeklyTV = findViewById(R.id.weeklyText);
        monthlyTV = findViewById(R.id.monthlyText);
        yearlyTV = findViewById(R.id.yearlyText);
        weeklyBuy = findViewById(R.id.weeklySubscription);
        monthlyBuy = findViewById(R.id.monthlySubscription);
        yearlyBuy = findViewById(R.id.yearlySubscription);

        String itemType = getIntent().getStringExtra("itemType");
        String itemSubCat = getIntent().getStringExtra("itemSubCat");
        subscriptionConfig = (SubscriptionConfig) getIntent().getSerializableExtra("config");

        Log.d("SubscriptionActivity","itemType: "+itemType+" itemSubCat: "+itemSubCat);
        applySubscriptionConfig(subscriptionConfig);
    }

    private void applySubscriptionConfig(final SubscriptionConfig subscriptionConfig) {
        weeklyTV.setText("সাপ্তাহিক সাবস্ক্রিপশন কিনুন মাত্র " +subscriptionConfig.getWeeklyPrice()+ " টাকার বিনিময়ে");
        monthlyTV.setText("মাসিক সাবস্ক্রিপশন কিনুন মাত্র " +subscriptionConfig.getMonthlyPrice()+ " টাকার বিনিময়ে");
        yearlyTV.setText("বাৎসরিক সাবস্ক্রিপশন কিনুন মাত্র " +subscriptionConfig.getYearlyPrice()+ " টাকার বিনিময়ে");

        weeklyBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSubscription(Integer.parseInt(subscriptionConfig.getWeeklyPrice()), "weekly", subscriptionConfig);
            }
        });

        monthlyBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSubscription(Integer.parseInt(subscriptionConfig.getMonthlyPrice()), "monthly", subscriptionConfig);
            }
        });

        yearlyBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSubscription(Integer.parseInt(subscriptionConfig.getYearlyPrice()), "yearly", subscriptionConfig);
            }
        });
    }

    private void initSubscription(final int price, final String type, final SubscriptionConfig subscriptionConfig) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String deviceId = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                    SubscribeUsingPaymentGateway obj = new SubscribeUsingPaymentGateway();
                    obj.setData("test", "test123", "1234", (float) price, deviceId, SubscriptionActivity.this.subscriptionConfig.getItemCategory(), SubscriptionActivity.this, new OnSubscriptionListener() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String transactionStatus = result.getString("transactionStatus");
                                String paymentID = result.getString("paymentID");
                                String paymentMethod = result.getString("paymentMethod");
                                String referenceCode = result.getString("referenceCode");
                                Long amount = result.getLong("amount");
                                if (transactionStatus.equals("Completed")) {

                                    String deviceId = Settings.Secure.getString(getContentResolver(),
                                            Settings.Secure.ANDROID_ID);
                                    InsertPayment.insertPayment(SubscriptionActivity.this, 0, amount, paymentID, paymentMethod, referenceCode, deviceId, SubscriptionActivity.this.subscriptionConfig.getItemSubcategory());

                                    Subscription subscription = new Subscription(
                                            subscriptionConfig.getSubscriptionFor(),
                                            subscriptionConfig.getItemType(),
                                            subscriptionConfig.getItemName(),
                                            subscriptionConfig.getItemCategory(),
                                            subscriptionConfig.getItemSubcategory(),
                                            amount.toString(), type, paymentID, paymentMethod
                                    );

                                    SubscriptionService service = new SubscriptionService();
                                    service.insertSubscription(SubscriptionActivity.this, subscription);

                                    Date currenTime;
                                    DateFormat dateFormat;
                                    dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                    currenTime = new Date();
                                    AppLogger.insertLogs(SubscriptionActivity.this, dateFormat.format(currenTime), "N", SubscriptionActivity.this.subscriptionConfig.getItemSubcategory() + "",
                                            "PAYMENT_DONE", result.getString("paymentMethod"), "subscription");
                                    Toast.makeText(SubscriptionActivity.this, "আপনার পেমেন্ট সফল হয়েছে", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("tranResult", "transactionResult: " + result);
                        }

                        @Override
                        public void onError(JSONObject result) {


                            Log.e("tranError", "transactionResult: " + result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
