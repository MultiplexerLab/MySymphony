package lct.mysymphony.helper;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import lct.mysymphony.R;

public class ProgressDialog {

    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    Context context;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void showProgressDialog() {
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void hideProgressDialog(){

        alertDialog.dismiss();
    }
}
