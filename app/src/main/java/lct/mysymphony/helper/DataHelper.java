package lct.mysymphony.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

public class DataHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "contentDb";
    public static final String TABLE_NAME = "downloadeContent";
    public static final String COL_ID = "id";
    public static final String CONTENT_ID = "id";
    public static final String COL_CONTENT_CAT = "contentCat";
    public static final String COL_CONTENT_TYPE = "contentType";
    public static final String COL_CONTENT_TITLE = "contentTitle";
    public static final String COL_CONTENT_DESC = "contentDesc";
    public static final String COL_CONTENT_TEXT = "contentText";
    public static final String COL_CONTENT_DATA = "contentData";
    public static final String COL_DOWLOAD_TIMESTAMP = "downloadTimestamp";
    public static final String COL_EXPIRE_TIMESTAMP = "expireTimestamp";
    public static final String COL_CONTENT_STATUS = "contentStatus";

    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  , " + COL_CONTENT_CAT + " TEXT , " + COL_CONTENT_TYPE + " TEXT , " + COL_CONTENT_TITLE + " TEXT , " + COL_CONTENT_DESC + " TEXT ," + COL_CONTENT_TEXT + " TEXT , " + COL_CONTENT_DATA + "  BLOB , " + COL_DOWLOAD_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP , " + COL_EXPIRE_TIMESTAMP + "DATETIME DEFAULT CURRENT_TIMESTAMP , " + COL_CONTENT_STATUS + "STRING )";
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

    public void insertBitmap(Bitmap bm) {
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
            values.put("contentData", buffer);
            values.put("contentDesc", "Image description");
            // Insert Row
            long i = db.insert(TABLE_NAME, null, values);
            Log.i("Insert", i + "");
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }

    public Bitmap getBitmap(int id) {
        Bitmap bitmap = null;
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        Log.d("enter","enter");
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount()==0)
                Log.d("cursor","0");
            else if (cursor.getCount()>0)
                Log.d("cursor","found");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ///while (cursor.moveToNext()) {
                    // Convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("contentData"));
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                    Log.d("getBitmap", "blob lenght: " + blob.length);
                    // Convert the byte array to Bitmap
                    bitmap = BitmapFactory.decodeStream(inputStream);

                ///}

            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("excption",e.toString());

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return bitmap;
    }

    public long getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }
}