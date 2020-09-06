package com.example.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


// Written by Cade Cottrell for CS4301.002, Contact Phase 4 Assignment
// netid: cac160030


//Helps manage layouts and contact information
public class Contact extends AppCompatActivity {

    ViewPager viewPager;
    StatePagerAdapter statePagerAdapter;

    UpdateOrAddContact updateOrAddContact;
    FileIO fileIO;

    private ArrayList<String> address;
    Fragment AddFrag;
    Fragment CalendarFrag;
    Fragment ModifyFrag;
    private String info;
    private String contactString;
    private int lineNumber;
    private String date;
    private boolean isBirthDate;


    //onCreate, sets up the activity and determines which which fragment to show
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        statePagerAdapter = new StatePagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);

        fileIO = new FileIO(getApplicationContext());

        AddFrag = new ContactMainMenu();
        ModifyFrag = new ModifyDeleteFragment();
        CalendarFrag = new CalendarFragment();
        findInfo();
        setupPager(viewPager);

        isBirthDate = false;
    }


    //Now checks if we are in modify or add, if we are in modify we can show the Map Address
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        if(setupContact() == false)
        {
            getMenuInflater().inflate(R.menu.contact_menu, menu);
            return true;
        }
        else
        {
            return super.onCreateOptionsMenu(menu);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.MapAddress)
        {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putStringArrayListExtra("address", address);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //Determines is we are Adding a New Client or Modifying an Existing
    protected void setupPager(ViewPager pager)
    {
        if(setupContact())
        {
            statePagerAdapter = new StatePagerAdapter(getSupportFragmentManager());
            statePagerAdapter.addFragment(AddFrag, "ContactMainMenu");
            statePagerAdapter.addFragment(CalendarFrag, "CalendarFragment");
            pager.setAdapter(statePagerAdapter);
        }
        else
        {
            statePagerAdapter = new StatePagerAdapter(getSupportFragmentManager());
            statePagerAdapter.addFragment(ModifyFrag, "Modify/Delete Fragment");
            statePagerAdapter.addFragment(CalendarFrag, "CalendarFragment");
            pager.setAdapter(statePagerAdapter);
        }
    }

    //Writes new contact, and sends list to main activity
    protected void completeAddActivity(String record, ArrayList<String> address)
    {
        fileIO.writeFullRecord(record, address);
        ArrayList<String> contactObjects = fileIO.getAllContacts(getApplicationContext());
        Intent intent = new Intent();
        intent.putStringArrayListExtra("contactObjects", contactObjects);
        setResult(2, intent);
        finish();
    }

    //Writes/delete an existing contact, and sends list to main activity
    protected void completeModifyDeleteActivity(String newRecord, String action, String oldRecord, ArrayList<String> address)
    {
        if(action.equals("Delete"))
        {
            fileIO.deleteLineFromRecord(oldRecord);
        }
        else if(action.equals("Modify"))
        {
            fileIO.modifyLineFromRecord(oldRecord, newRecord, address);
        }

        ArrayList<String> contactObjects = fileIO.getAllContacts(getApplicationContext());
        Intent intent = new Intent();
        intent.putStringArrayListExtra("contactObjects", contactObjects);
        setResult(2, intent);
        finish();
    }


    //Checks to see if editTexts are filled
    protected boolean allFilled(EditText lastName, EditText firstName, EditText phoneNumber, EditText dateMet)
    {
        if(!isEmpty(dateMet) && !isEmpty(lastName) && !isEmpty(firstName) && !isEmpty(phoneNumber))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Makes sure editTexts are not over capacity
    protected boolean notOverSized(EditText lastName, EditText firstName, EditText phoneNumber, EditText birthDate, EditText dateMet)
    {
        String last = lastName.getText().toString();
        String first = firstName.getText().toString();
        String phone = phoneNumber.getText().toString();
        String birth = birthDate.getText().toString();
        String met =  dateMet.getText().toString();

        if(last.length() <= 25 && first.length() <= 25 && phone.length() == 10 && birth.length() <= 10 && met.length() == 10)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //Checks the length of the address items, if they are above the maximum length it is false.
    protected boolean addressLengthCheck(EditText address1, EditText address2, EditText city, EditText state, EditText zipcode)
    {
        String addressText1 = address1.getText().toString();
        String addressText2 = address2.getText().toString();
        String cityText = city.getText().toString();
        String stateText = state.getText().toString();
        String zipcodeText = zipcode.getText().toString();


        if(addressText1.length() == 0 && address2.length() == 0 && cityText.length() == 0 && stateText.length() == 0 && zipcodeText.length() == 0)
        {
            return true;
        }
        else if(addressText1.length() > 0 && (zipcodeText.length() < 5 || cityText.length() == 0 || stateText.length() < 2))
        {
            return false;
        }
        else if(addressText1.length() <= 25 && addressText2.length() <= 25 && cityText.length() <= 25 && stateText.length() == 2 && zipcodeText.length() == 5)
        {
            return true;
        }

        return false;
    }

    //Sets up address data, if Address items are empty they are set to null
    protected ArrayList<String> setupAddressData(EditText address1, EditText address2, EditText city, EditText state, EditText zipcode)
    {
        String addressText1 = address1.getText().toString();
        String addressText2 = address2.getText().toString();
        String cityText = city.getText().toString();
        String stateText = state.getText().toString();
        String zipcodeText = zipcode.getText().toString();

        ArrayList<String> addressItems = new ArrayList<>();

        if(addressText1.length() == 0)
        {
            addressText1 = null;
            cityText = null;
            stateText = null;
            zipcodeText = null;
            addressText2 = null;

            addressItems.add(addressText1);
            addressItems.add(addressText2);
            addressItems.add(cityText);
            addressItems.add(stateText);
            addressItems.add(zipcodeText);

        }
        else
        {

            addressItems.add(addressText1);

            if(addressText2.length() == 0)
            {
                addressText2 = null;
                addressItems.add(addressText2);
            }
            else
            {
                addressItems.add(addressText2);
            }

            addressItems.add(cityText);
            addressItems.add(stateText);
            addressItems.add(zipcodeText);
        }

        return addressItems;

    }

    protected ArrayList<String> checkData(String a)
    {
        return fileIO.getRow(a);
    }

    //Checks if editText is empty
    protected boolean isEmpty(EditText editText)
    {
        return editText.getText().toString().trim().length() == 0;
    }


    //Puts all texts into one string to be represented as a record
    protected String setupSaveData(EditText lastName, EditText firstName, EditText phoneNumber, EditText birthDate, EditText dateMet)
    {
        //This is an effort to make sure the both Last and First names are capitialized
        String rawLast = lastName.getText().toString();
        String last =  rawLast.substring(0, 1).toUpperCase() + rawLast.substring(1);

        String rawFirst = firstName.getText().toString();
        String first = rawFirst.substring(0, 1).toUpperCase() + rawFirst.substring(1);

        String phone = phoneNumber.getText().toString() + " ";
        String birth = birthDate.getText().toString();
        String met = " " + dateMet.getText().toString();


        last = setNameSpaceLength(last);
        first = setNameSpaceLength(first);
        birth = setBirthSpaceLength(birth);

        String record = (last + first + phone + birth + met);

        return record;

    }


    //Makes name texts the appropriate length
    protected String setNameSpaceLength(String line)
    {
        for(int i = line.length(); i < 26; i++)
        {
            line = line + " ";
        }

        return line;
    }

    //Makes birthDate appropriate length
    protected String setBirthSpaceLength(String line)
    {
        if(line.length() == 0)
        {
            line = "N/A";
            for(int i = 0; i < 8; i++)
            {
                line = line + " ";
            }
        }
        else if(line.length() == 3)
        {
            for(int i = 0; i < 8; i++)
            {
                line = line + " ";
            }
        }
        else
        {
            line = line + " ";
        }

        return line;
    }

    //Getters and Setters
    public String getContactString() {
        return contactString;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    protected void findInfo()
    {
        ArrayList<String> stringArrayList =  getIntent().getStringArrayListExtra("addOrModify");

        if(stringArrayList.size() > 1)
        {
            info = stringArrayList.get(0);
            contactString = stringArrayList.get(1);
            lineNumber = Integer.parseInt(stringArrayList.get(2));
        }
        else
        {
            info = stringArrayList.get(0);
        }
    }

    protected String sendInfo()
    {
        return info;
    }

    protected void setViewPager(int num)
    {
        viewPager.setCurrentItem(num);
    }

    protected void setIsBirthDate(boolean set)
    {
        isBirthDate = set;
    }

    protected boolean getIsBirthDate()
    {
        return isBirthDate;
    }

    protected void setDate(String date)
    {
        this.date = date;
    }

    //Updates the date on the specific fragment
    protected void updateDate()
    {
       Fragment frag = statePagerAdapter.getItem(0);

       if(frag instanceof ContactMainMenu)
       {
           ((ContactMainMenu)frag).addDate(date);
       }
       else if(frag instanceof ModifyDeleteFragment)
       {
           ((ModifyDeleteFragment)frag).addDate(date);
       }
    }

    protected boolean setupContact()
    {
        updateOrAddContact = new UpdateOrAddContact();
        return updateOrAddContact.detectAddOrModify(info);
    }

    protected void setupAddress(ArrayList<String> list)
    {
        address = list;
    }
}
