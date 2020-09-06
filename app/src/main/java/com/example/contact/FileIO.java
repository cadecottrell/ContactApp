package com.example.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static com.example.contact.ContactSchema.ContactTable.Fields.COL1;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL2;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL3;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL4;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL5;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL6;
import static com.example.contact.ContactSchema.ContactTable.Fields.COL7;
import static com.example.contact.ContactSchema.ContactTable.TABLE_NAME;
import static java.sql.Types.NULL;


// Written by Cade Cottrell for CS4301.002, Contact Phase 1 Assignment
// netid: cac160030

public class FileIO{


    //Variables that are responsible for proper file identification

    //Old Existing file
    private String filePath = "cac160030.txt";
    Context mContext;
    File file;

    //New Database
    private SQLiteDatabase myDatabase;



    //Constructor, implements the database
    public FileIO(Context mContext) {
        this.mContext = mContext;
        file = new File(mContext.getFilesDir(), filePath);
        myDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }



    //Writes the record into the database
    public void writeFullRecord(String record, ArrayList<String> address)
    {
        try
        {
            ContentValues cv = new ContentValues();

            if(address != null)
            {
                cv.put(COL2, record);
                cv.put(COL3, address.get(0));
                cv.put(COL4, address.get(1));
                cv.put(COL5, address.get(2));
                cv.put(COL6, address.get(3));
                cv.put(COL7, address.get(4));
            }
            else
            {
                cv.put(COL2, record);
                cv.put(COL3, NULL);
                cv.put(COL4, NULL);
                cv.put(COL5, NULL);
                cv.put(COL6, NULL);
                cv.put(COL7, NULL);
            }


            long result = myDatabase.insert(TABLE_NAME, null, cv);

            //Displays a toast on success or on failure.
            if(result == -1)
            {
                Toast.makeText(mContext, "ERROR, CONTACT NOT ADDED.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(mContext, "Contact added!", Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception ex)
        {
            System.out.println("Something went wrong with the writing FULL record");
            ex.printStackTrace();
        }

    }


    protected ArrayList<String> getRow(String record)
    {
        String query = "SELECT  * " + " FROM " + TABLE_NAME + " WHERE " + COL2 + " ='" + record + "'";

        Cursor cursor = myDatabase.rawQuery(query, null);

        if(cursor != null)
        {
            System.out.println("CURSOR NOT NULL");
            System.out.println("CURSOR NOT NULL");System.out.println("CURSOR NOT NULL");
            System.out.println("CURSOR NOT NULL");
            System.out.println("CURSOR NOT NULL");

        }
        else
        {
            System.out.println("CURSOR  NULL");
            System.out.println("CURSOR  NULL");
            System.out.println("CURSOR  NULL");
            System.out.println("CURSOR  NULL");
        }

        cursor.moveToFirst();

        String addressText1 = cursor.getString(cursor.getColumnIndex(COL3)) + "";
        String addressText2 = cursor.getString(cursor.getColumnIndex(COL4))+ "";
        String cityText = cursor.getString(cursor.getColumnIndex(COL5))+ "";
        String stateText = cursor.getString(cursor.getColumnIndex(COL6))+ "";
        String zipcodeText = cursor.getString(cursor.getColumnIndex(COL7))+ "";

        ArrayList<String> address = new ArrayList<>();

        address.add(addressText1);
        address.add(addressText2);
        address.add(cityText);
        address.add(stateText);
        address.add(zipcodeText);


        for(int i = 0; i < address.size(); i++)
        {
            if(address.get(i).equals("null"))
            {
                address.set(i, "");
            }
        }


        return address;
    }


    //Grabs all records in the database and returns an ArrayList of Strings
    public ArrayList<String> getAllContacts(Context context)
    {
        ArrayList<String> contactObjects = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        try
        {
            Cursor cursor = myDatabase.rawQuery(query, null);

            if(cursor != null)
            {
                while(cursor.moveToNext())
                {
                    contactObjects.add(cursor.getString(1));
                }
                Collections.sort(contactObjects);
                return contactObjects;
            }
            else
            {
                return contactObjects;
            }

        }
        catch(Exception ex)
        {
            System.out.println("SOMETHING WENT WRONG IN READING DATA");
        }

        return contactObjects;
    }


    //Modifies a given record, by overwritting its location
    protected void modifyLineFromRecord(String oldRecord, String newRecord, ArrayList<String> address)
    {
        try
        {
            deleteLineFromRecord(oldRecord);
            writeFullRecord(newRecord, address);
        }
        catch(Exception ex)
        {
            System.out.println("SOMETHING WENT WRONG IN MODIFY LINE");
            ex.printStackTrace();
        }

    }


    //Finds the record in the database and removes it
    protected void deleteLineFromRecord(String record)
    {
        //String query = "DELETE FROM " + TABLE_NAME;
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + record + "'";
        try
        {
            myDatabase.execSQL(query);
            Toast.makeText(mContext, "Deleting...", Toast.LENGTH_LONG).show();
        }
        catch(Exception ex)
        {
            System.out.println("DELETION WENT WRONG");
            Toast.makeText(mContext, "Could not delete contact!", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    //Tries to open old file (if it exists) and puts contents into database
    protected void importOldContacts()
    {
        try
        {
            Scanner scan = new Scanner(file);
            String currentLine = "";
            ArrayList<String> address = new ArrayList<>();
            while(scan.hasNextLine())
            {
                currentLine = scan.nextLine();
                writeFullRecord(currentLine, address);
            }
            scan.close();
        }
        catch(FileNotFoundException ex)
        {
            Toast.makeText(mContext, "Cannot Find Old Contacts", Toast.LENGTH_LONG).show();
        }
    }

    //resets the database
    protected void resetDatabase()
    {
        String query = "DELETE FROM " + TABLE_NAME;
        myDatabase.execSQL(query);
    }

}
