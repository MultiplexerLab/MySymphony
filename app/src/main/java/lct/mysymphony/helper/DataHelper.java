package lct.mysymphony.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import lct.mysymphony.ModelClass.DataBaseData;

public class DataHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "contentDb";
    public static final String TABLE_NAME = "downloadeContent";
    public static final String COL_ID = "id";
    public static final String COL_CONTENT_ID = "contentId";
    public static final String COL_CONTENT_CAT = "contentCat";
    public static final String COL_CONTENT_TYPE = "contentType";
    public static final String COL_CONTENT_TITLE = "contentTitle";
    public static final String COL_CONTENT_DESC = "contentDesc";
    public static final String COL_CONTENT_THUMBNAILIMG = "contentThumbnailImg";
    public static final String COL_CONTENT_TEXT = "contentText";
    public static final String COL_CONTENT_DATA = "contentData";
    public static final String COL_DOWLOAD_TIMESTAMP = "downloadTimestamp";
    public static final String COL_EXPIRE_TIMESTAMP = "expireTimestamp";
    public static final String COL_CONTENT_STATUS = "contentStatus";

    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  , " + COL_CONTENT_ID + " INTEGER , " + COL_CONTENT_CAT + " TEXT , " + COL_CONTENT_TYPE + " TEXT , " + COL_CONTENT_TITLE + " TEXT , " + COL_CONTENT_DESC + " TEXT ," + COL_CONTENT_TEXT + " TEXT , " + COL_CONTENT_THUMBNAILIMG + " TEXT, " + COL_CONTENT_DATA + "  BLOB , " + COL_DOWLOAD_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP , " + COL_EXPIRE_TIMESTAMP + "DATETIME DEFAULT CURRENT_TIMESTAMP , " + COL_CONTENT_STATUS + " TEXT )";
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL(DELETE_TABLE);
        //Create tables again
        onCreate(db);
    }

    public void insertBitmap(Bitmap bm, DataBaseData dataBaseData) {
        // Convert the image into byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] buffer = out.toByteArray();
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(COL_CONTENT_CAT, dataBaseData.getContentCat());
            values.put(COL_CONTENT_TYPE, dataBaseData.getContentType());
            values.put(COL_CONTENT_TITLE, dataBaseData.getContentTitle());
            values.put(COL_CONTENT_DESC, dataBaseData.getContentDesc());
            values.put(COL_CONTENT_TEXT, "ContentText");
            values.put(COL_CONTENT_DATA, buffer);
            values.put(COL_DOWLOAD_TIMESTAMP, "11/12/13");
            values.put(COL_CONTENT_STATUS, dataBaseData.getContentStatus());
            values.put(COL_CONTENT_ID, dataBaseData.getContentId());

            Log.d("contentcat", dataBaseData.getContentCat());
            Log.d("contentid", Integer.toString(dataBaseData.getContentId()));


            // Insert Row
            long i = db.insert(TABLE_NAME, null, values);
            Log.i("Insert", i + "");
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("problem", e.toString());

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }

    public Bitmap getBitmap(int id) {
        Bitmap bitmap = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("enter", "enter");
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() == 0)
                Log.d("cursor", "0");
            else if (cursor.getCount() > 0)
                Log.d("cursor", "found");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("contentData"));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                Log.d("getBitmap", "blob lenght: " + blob.length);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("excption", e.toString());

        } finally {
            db.endTransaction();
            db.close();
        }
        return bitmap;
    }

    public long getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }


    public String getContentType(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String contentType = null;
        Log.d("enter", "enterGet");
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() == 0)
                Log.d("cursorType", "0");
            else if (cursor.getCount() > 0)
                Log.d("cursorType", "found");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ///while (cursor.moveToNext()) {
                // Convert blob data to byte array
                contentType = cursor.getString(cursor.getColumnIndex(COL_CONTENT_TYPE));
                Log.d("contentType", contentType);

            }


        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("excption", e.toString());

        }
        db.close();
        return contentType;
    }

    public String getContentTitle(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String contentType = null;
        Log.d("enter", "enterTitle");
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() == 0)
                Log.d("cursorTitle", "0");
            else if (cursor.getCount() > 0)
                Log.d("cursorTitle", "found");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ///while (cursor.moveToNext()) {
                // Convert blob data to byte array
                contentType = cursor.getString(cursor.getColumnIndex(COL_CONTENT_TITLE));
                Log.d("contentTitle", contentType);

            }


        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("excptionTitle", e.toString());

        }
        db.close();
        return contentType;
    }

    public DataBaseData getAllData(int id) {
        DataBaseData dataBaseData = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("enter", "enterAll");
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() == 0)
                Log.d("cursorAll", "0");
            else if (cursor.getCount() > 0)
                Log.d("cursorAll", "found");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                dataBaseData = new DataBaseData(cursor.getString(cursor.getColumnIndex(COL_CONTENT_TITLE)), cursor.getString(cursor.getColumnIndex(COL_CONTENT_CAT)), cursor.getString(cursor.getColumnIndex(COL_CONTENT_TYPE)), cursor.getString(cursor.getColumnIndex(COL_CONTENT_DESC)), cursor.getString(cursor.getColumnIndex(COL_CONTENT_THUMBNAILIMG)), cursor.getString(cursor.getColumnIndex(COL_CONTENT_STATUS)), cursor.getInt(cursor.getColumnIndex(COL_CONTENT_ID)));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("excptionAll", e.toString());
        }
        db.close();
        return dataBaseData;
    }

    public boolean checkDownLoadedOrNot(String contentCat, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_CONTENT_ID + " = " + id + " AND " + COL_CONTENT_CAT + " = '" + contentCat + "'";

            Log.d("contentcat2", contentCat);
            Log.d("contentid2", Integer.toString(id));
            ///Cursor cursor = db.query(TABLE_NAME, new String[]{COL_CONTENT_ID + " =? and " + COL_CONTENT_CAT + " =? "}, new String[] { String.valueOf(id), contentCat }, null, null, null);
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                Log.d("cursorCheck", "found");
                return true;
            } else {
                Log.d("cursorCheck", "notfound");
                return false;
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("excptionCheck", e.toString());

        }
        db.close();

        return false;
    }

    public void insertVideoStr(String result, DataBaseData dataBaseData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(COL_CONTENT_CAT, dataBaseData.getContentCat());
            values.put(COL_CONTENT_TYPE, dataBaseData.getContentType());
            values.put(COL_CONTENT_TITLE, dataBaseData.getContentTitle());
            values.put(COL_CONTENT_DESC, dataBaseData.getContentDesc());
            values.put(COL_CONTENT_TEXT, "ContentText");
            values.put(COL_CONTENT_THUMBNAILIMG, dataBaseData.getThumbNailImgUrl());
            values.put(COL_CONTENT_DATA, result);
            values.put(COL_DOWLOAD_TIMESTAMP, "11/12/13");
            values.put(COL_CONTENT_STATUS, dataBaseData.getContentStatus());
            values.put(COL_CONTENT_ID, dataBaseData.getContentId());

            long i = db.insert(TABLE_NAME, null, values);
            Log.i("Insert", values.toString());
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("insertErr", e.toString());
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}