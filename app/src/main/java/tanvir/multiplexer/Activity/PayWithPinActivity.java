package tanvir.multiplexer.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tanvir.multiplexer.R;

public class PayWithPinActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_pin);

        toolbar = findViewById(R.id.toolbarlayoutinpaywithpin);
        setSupportActionBar(toolbar);
    }
}
