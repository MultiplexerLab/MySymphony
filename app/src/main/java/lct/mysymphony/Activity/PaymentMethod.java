package lct.mysymphony.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;

public class PaymentMethod extends AppCompatActivity {

    ImageButton payWithPin, payWithBikash, payWithRocket, payWithMaxis, payWithVisa, payWithMasterCard;
    ImageButton selectedButton;
    String selectedMethod = "";
    String imageUrl;
    DataBaseData dataBaseData;
    ///Integer price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        ////price=getIntent().getIntExtra("price",0);
        payWithPin = findViewById(R.id.payWithPin);
        payWithBikash = findViewById(R.id.payWithBikash);
        payWithRocket = findViewById(R.id.payWithRocket);
        payWithMaxis = findViewById(R.id.payWithMaxis);
        payWithVisa = findViewById(R.id.payWithVisa);
        payWithMasterCard = findViewById(R.id.payWithMasterCard);
        imageUrl=getIntent().getStringExtra("imageUrl");
        dataBaseData= (DataBaseData) getIntent().getSerializableExtra("dataBaseData");
        if (dataBaseData==null)
            Log.d("dataBaseDataMethod","null");

        payWithPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedButton = findViewById(R.id.payWithPin);
                selectedMethod = "Pay with pin";
                payWithPin.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });

        payWithBikash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "Bikash";
                selectedButton = findViewById(R.id.payWithBikash);
                payWithBikash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });

        payWithRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "Rocket";
                selectedButton = findViewById(R.id.payWithRocket);
                payWithRocket.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });

        payWithMaxis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "Maxis";
                selectedButton = findViewById(R.id.payWithMaxis);
                payWithMaxis.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));

            }
        });

        payWithVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "Visa Card";
                selectedButton = findViewById(R.id.payWithVisa);
                payWithVisa.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });

        payWithMasterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "Master Card";
                selectedButton = findViewById(R.id.payWithMasterCard);
                payWithMasterCard.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });
    }

    public void payMent(View view) {

        if (selectedMethod.length() > 0) {
            //Toast.makeText(PaymentMethod.this, "ধন্যবাদ " + selectedMethod + " এর মাধ্যমে পেমেন্ট করার জন্যে", Toast.LENGTH_SHORT).show();
            if (selectedMethod.contains("Pay with pin"))
            {
                Intent intent = new Intent(PaymentMethod.this, PayWithPinActivity.class);
                intent.putExtra("paymentMethod","Pay with pin");
                intent.putExtra("dataBaseData",dataBaseData);
                intent.putExtra("imageUrl",imageUrl);
                ///intent.putExtra("price",price);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }else if(selectedMethod.contains("Rocket")){
                Intent intent = new Intent(PaymentMethod.this, PayWithRocketActivity.class);
                intent.putExtra("paymentMethod","Rocket");
                intent.putExtra("dataBaseData",dataBaseData);
                intent.putExtra("imageUrl",imageUrl);
                ///intent.putExtra("price",price);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }else
            {
                Intent intent = new Intent(PaymentMethod.this, PaymentConfirmation.class);
                intent.putExtra("imageUrl",imageUrl);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        } else {
            Toast.makeText(this, "যেকোনো একটি পেমেন্ট মেথড সিলেক্ট করুন", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}
