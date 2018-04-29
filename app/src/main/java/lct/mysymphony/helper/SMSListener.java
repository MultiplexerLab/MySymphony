package lct.mysymphony.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Lenovo on 4/29/2018.
 */

public class SMSListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    msgs[pdus.length - 1] = SmsMessage.createFromPdu((byte[]) pdus[pdus.length - 1]);
                    msg_from = msgs[pdus.length - 1].getOriginatingAddress();
                    if(msg_from.contains("01552146213")){
                        String msgBody = msgs[pdus.length - 1].getMessageBody();
                        String numbers = msgBody.replaceAll("\\D+","");
                        SharedPreferences.Editor editor = context.getSharedPreferences("pincode", context.MODE_PRIVATE).edit();
                        editor.putString("verificationCode", numbers);
                        editor.apply();
                        Log.d("verificationCode", numbers);
                    }
                } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}