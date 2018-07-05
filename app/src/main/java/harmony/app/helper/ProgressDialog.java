package harmony.app.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;

import harmony.app.R;

public class ProgressDialog {

    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    Context context;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void showProgressDialog() {
        Log.i("DialogProgress", "ProgressDialog");
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void showProgressDialog(String message) {
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        final TextView loading_msg = dialogView.findViewById(R.id.loading_msg);
        loading_msg.setText(message);
        YoYo.with(Techniques.FadeIn)
                .duration(3000)
                .repeat(5)
                .playOn(loading_msg);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                alertDialog.dismiss();
            }
        }, 10000);*/
    }

    public void showProgressDialogAPK() {
        SharedPreferences preferences = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putInt("unknownSource", 1);
        prefsEditor.commit();
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        TextView loading_msg = dialogView.findViewById(R.id.loading_msg);
        TextView animatedText = dialogView.findViewById(R.id.animatedText);
        loading_msg.setText("App is downloading!");
        animatedText.setText("You need to enable Unknown Source for once when prompted the screen! After enabling for this AppStore you don't need to do it again, so Click on the Settings and Enable it when it requires");
        YoYo.with(Techniques.Pulse)
                .duration(3000)
                .repeat(5)
                .playOn(animatedText);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                alertDialog.dismiss();
            }
        }, 10000);*/
    }

    public void hideProgressDialog() {
        alertDialog.dismiss();
    }

    public void setAlertdialogNull() {
        alertDialog = null;
    }
}
