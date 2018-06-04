package lct.mysymphony.Notification;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences.Editor editor;
        editor = getSharedPreferences("login", MODE_PRIVATE).edit();
        editor.putString("firebaseToken", refreshedToken);
        editor.apply();
        Log.i(TAG, "onTokenRefresh: " + refreshedToken);
    }
}