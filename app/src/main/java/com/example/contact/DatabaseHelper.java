package com.example.contact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.contact.ContactSchema.ContactTable.Fields.COL2;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL3;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL4;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL5;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL6;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL7;
import static com.example.contact.ContactSchema.ContactTable.TABLE_NAME;



//Database helper, setups the database essentially
public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;

    private static final String alterDatabase1 = "ALTER TABLE " + TABLE_NAME + " ADD " + COL3 + " TEXT";
    private static final String alterDatabase2 = "ALTER TABLE " + TABLE_NAME + " ADD " + COL4 + " TEXT";
    private static final String alterDatabase3 = "ALTER TABLE " + TABLE_NAME + " ADD " + COL5 + " TEXT";
    private static final String alterDatabase4 = "ALTER TABLE " + TABLE_NAME + " ADD " + COL6 + " TEXT";
    private static final String alterDatabase5 = "ALTER TABLE " + TABLE_NAME + " ADD " + COL7 + " TEXT";

    public DatabaseHelper(@Nullable Context mContext) {
        super(mContext, TABLE_NAME , null, 12);
        context = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableCreation = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT , "
                + COL3 + " TEXT , " + COL4 + " TEXT , " + COL5 + " TEXT , " + COL6 + " TEXT , " + COL7 + " TEXT );";
        db.execSQL(tableCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       if(oldVersion < 12)
       {
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
           onCreate(db);
       }

    }
}
