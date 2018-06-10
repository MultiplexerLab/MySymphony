package sym.appstore.helper;

/**
 * Created by ismail on 7/12/17.
 */

import android.content.*;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.widget.Toast;

import sym.appstore.Activity.PinActivity;

// Todo : SMS Reciever Class

public class SimpleSmsReciever extends BroadcastReceiver {

    private static final String TAG = "Message recieved";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
        Intent smsIntent = new Intent(context, PinActivity.class);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.putExtra("MessageNumber", messages.getOriginatingAddress());
        smsIntent.putExtra("Message", messages.getMessageBody());
        if (messages.getOriginatingAddress().contains("01552146213"))
            context.startActivity(smsIntent);
        Log.d(TAG, messages.getOriginatingAddress());
    }

}
