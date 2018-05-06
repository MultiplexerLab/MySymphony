package lct.mysymphony.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import lct.mysymphony.ModelClass.DataBaseData;

public class PushDataToSharedPref {
    public void pushDatabaseDataToSharedPref(DataBaseData dataBaseData, String imageUrl, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataBaseData);
        prefsEditor.putString("databaseData", json);
        prefsEditor.putString("imageUrl", imageUrl);
        prefsEditor.commit();
    }
}
