package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


// Written by Cade Cottrell for CS4301.002, Contact Phase 4 Assignment
// netid: cac160030

public class MainMenu extends AppCompatActivity {

    //Recyclerview Variables and the FileIO class to help
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    FileIO fileIO;
    ArrayList<String> contactStrings;
    SensorManager sensorManager;
    float shake;
    float shakeCur;
    float shakeLast;

    //Standard onCreate function, sets up recyclerview, fileIO and sensor. Also asks for permission to have Location
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        fileIO = new FileIO(this);
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        shake = (float) 0;
        shakeCur = SensorManager.GRAVITY_EARTH;
        shakeLast = SensorManager.GRAVITY_EARTH;

        setupRecycle();


        //Asks for permissions for Location
        if (ContextCompat.checkSelfPermission( this,android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    1
            );
        }

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    1
            );
        }



    }

    //Custom menu on the top
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //inflate mainmenu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //Clicking the top right puts our activity 2 (Contact) into Adding Contact mode among other functions.
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        int id = menuItem.getItemId();

        //Add Contact brings up the menu to add a contact to the database
        //Import contacts brings the old text file contacts into the database (there is not limit on the button)
        //Clear contacts clears reinitializes the database and starts anew.
        if(id == R.id.addContact)
        {
            Intent intent = new Intent(this, Contact.class);
            ArrayList<String> extra = new ArrayList<>();
            extra.add("Add");
            intent.putStringArrayListExtra("addOrModify", extra);
            startActivityForResult(intent, 2);
        }
        else if(id == R.id.importContacts)
        {
            fileIO.importOldContacts();
            setupRecycle();
        }
        else if(id == R.id.clearContacts)
        {
            fileIO.resetDatabase();
            setupRecycle();
        }
        else
        {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(menuItem);
    }

    //Register Sensor when app opened
    @Override
    protected void onResume() {
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    //Unregister Sensor when app paused
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    //Callback when we add/modify/delete. Updates the Recyclerview
    protected void updateRecycleView(ArrayList<ContactObject> contactObjects)
    {
        adapter = new RecyclerViewAdapter(contactObjects, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    //Determines whether we call updateRecycleView, after being in the add Contact menu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2)
        {
            contactStrings = data.getStringArrayListExtra("contactObjects");
            ArrayList<ContactObject> contactObjects = new ArrayList<>();

            for(int i = 0; i < contactStrings.size(); i++)
            {
                String line1 = contactStrings.get(i).replaceAll("\\s+", ":");

                String[] line = line1.split(":");

                ContactObject a = new ContactObject(line[0], line[1], line[2], line[3], line[4]);

                contactObjects.add(a);

            }

            updateRecycleView(contactObjects);
        }
    }

    //Setup on app launch, and helps update recyclerview
    protected void setupRecycle()
    {
        contactStrings = fileIO.getAllContacts(this);

        ArrayList<ContactObject> contactObjects = createContactObjectList(contactStrings);

        updateRecycleView(contactObjects);
    }

    //Gets the String Arraylist of Contacts and convert to ContactObjectList
    protected ArrayList<ContactObject> createContactObjectList(ArrayList<String> stringContacts)
    {
        ArrayList<ContactObject> contactObjects = new ArrayList<>();

        for(int i = 0; i < stringContacts.size(); i++)
        {
            String line1 = stringContacts.get(i).replaceAll("\\s+", ":");

            String[] line = line1.split(":");

            ContactObject a = new ContactObject(line[0], line[1], line[2], line[3], line[4]);

            contactObjects.add(a);
        }

        return contactObjects;
    }

    //Reverses the contact list
    protected void reverseContacts()
    {
        Collections.sort(contactStrings, Collections.<String>reverseOrder());
        ArrayList<ContactObject> contactObjects = createContactObjectList(contactStrings);
        updateRecycleView(contactObjects);
    }

    // Sensor listeners
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            shakeLast = shakeCur;

            shakeCur = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float change = shakeCur - shakeLast;

            shake = shake * (float) 0.75 + change; //low pass

            if(shake > 13) //If it is sufficiently shaken reverse the list
            {
                reverseContacts();
                Toast.makeText(getApplicationContext(), "The Device has been shook", Toast.LENGTH_LONG).show();
            }

        }

        //Ignore this required function
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };





}
