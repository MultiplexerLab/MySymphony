package sym.appstore.helper;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import sym.appstore.R;


public class Audio_Controle_button_click_listener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //manager.cancel(intent.getExtras().getInt("id"));
       /// Toast.makeText(context, "Button clicked!", Toast.LENGTH_SHORT).show();
        RemoteViews remoteViews= (RemoteViews) intent.getSerializableExtra("notificationView");
        if (remoteViews==null)
            Log.d("remoteViews","null");
        //remoteViews.setTextViewText(R.id.notif_title,"test");
        Log.d("enterBroadcast","enterBroadcast");

        Intent audioIntent=new Intent(context, PlayAudioInBackgroundService.class);
        audioIntent.putExtra("message","stop");
        context.startService(audioIntent);
    }
}
