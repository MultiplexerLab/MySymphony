package tanvir.multiplexer.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import tanvir.multiplexer.R;

public class PaymentMethod extends AppCompatActivity {

    ImageButton payWithPin, payWithBikash, payWithRocket, payWithMaxis, payWithVisa, payWithMasterCard;


    boolean isAnyThingSelected = false;

    ImageButton selectedButton;
    String selectedMethod = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        payWithPin = findViewById(R.id.payWithPin);
        payWithBikash = findViewById(R.id.payWithBikash);
        payWithRocket = findViewById(R.id.payWithRocket);
        payWithMaxis = findViewById(R.id.payWithMaxis);
        payWithVisa = findViewById(R.id.payWithVisa);
        payWithMasterCard = findViewById(R.id.payWithMasterCard);

        payWithPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);

                selectedButton = findViewById(R.id.payWithPin);
                selectedMethod = "পিন";
                payWithPin.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));


            }
        });

        payWithBikash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "বিকাশ";

                selectedButton = findViewById(R.id.payWithBikash);
                payWithBikash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });

        payWithRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "রকেট";

                selectedButton = findViewById(R.id.payWithRocket);
                payWithRocket.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));

            }
        });

        payWithMaxis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "ম্যাক্সিস";
                selectedButton = findViewById(R.id.payWithMaxis);
                payWithMaxis.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));

            }
        });

        payWithVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "ভিসা কার্ড";

                selectedButton = findViewById(R.id.payWithVisa);
                payWithVisa.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });

        payWithMasterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedButton != null)
                    selectedButton.setBackgroundColor(Color.WHITE);
                selectedMethod = "মাস্টার কার্ড";

                selectedButton = findViewById(R.id.payWithMasterCard);

                payWithMasterCard.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_red_border, null));
            }
        });
    }

    public void payMent(View view) {

        if (selectedMethod.length() > 0) {
            //Toast.makeText(PaymentMethod.this, "ধন্যবাদ " + selectedMethod + " এর মাধ্যমে পেমেন্ট করার জন্যে", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PaymentMethod.this, PaymentConfirmation.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "যেকোনো একটি পেমেন্ট মেথড সিলেক্ট করুন", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }
}
