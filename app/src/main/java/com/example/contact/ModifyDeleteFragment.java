package com.example.contact;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


// Written by Cade Cottrell for CS4301.002, Contact Phase 1 Assignment
// netid: cac160030


//This is the Modify/Delete Fragment. It brings up the normal "Add contact fragment" but has two buttons
//And has the previous record's information in the edittexts

public class ModifyDeleteFragment extends Fragment {

    //Variables
    private EditText birthDate;
    private EditText dateMet;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;

    private EditText address1;
    private EditText address2;
    private EditText city;
    private EditText state;
    private EditText zipcode;

    private Button modifyButton;
    private Button deleteButton;

    private String originalRecord;



    //onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify_contact_fragment, container, false);

        //Find ids
        birthDate = view.findViewById(R.id.birthCalendarModify);
        dateMet = view.findViewById(R.id.firstMetModifyCalendar);
        firstName = view.findViewById(R.id.firstNameEditModify);
        lastName = view.findViewById(R.id.lastNameEditModify);
        phoneNumber = view.findViewById(R.id.phoneNumberEditModify);

        address1 = view.findViewById(R.id.Address1Modify);
        address2 = view.findViewById(R.id.Address2Modify);
        city = view.findViewById(R.id.CityModify);
        state = view.findViewById(R.id.StateModify);
        zipcode = view.findViewById(R.id.ZipcodeModify);

        modifyButton = view.findViewById(R.id.Modify);
        deleteButton = view.findViewById(R.id.Delete);


        //Display text on EditTexts
        setupEditTexts();

        ((Contact)getActivity()).setupAddress(getAddress());

        originalRecord = ((Contact)getActivity()).setupSaveData(lastName, firstName, phoneNumber, birthDate, dateMet);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Contact)getActivity()).setIsBirthDate(true);
                ((Contact)getActivity()).setViewPager(1);
            }
        });


        dateMet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Contact)getActivity()).setIsBirthDate(false);
                ((Contact)getActivity()).setViewPager(1);
            }
        });


        //Modify button to modify record. Made a new check to find Address.
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( ((Contact)getActivity()).addressLengthCheck(address1, address2, city, state, zipcode)) {

                    ArrayList<String> addressData = ((Contact) getActivity()).setupAddressData(address1, address2, city, state, zipcode);

                    if (((Contact) getActivity()).allFilled(lastName, firstName, phoneNumber, dateMet) && ((Contact) getActivity()).notOverSized(lastName, firstName, phoneNumber, birthDate, dateMet))
                    {
                        Toast.makeText(getActivity(), "Saving Modified Contact", Toast.LENGTH_LONG).show();

                        String record = ((Contact) getActivity()).setupSaveData(lastName, firstName, phoneNumber, birthDate, dateMet);

                        ((Contact) getActivity()).completeModifyDeleteActivity(record, "Modify", originalRecord, addressData);

                    }
                    else {
                        Toast.makeText(getActivity(), "Make sure everything is filled out.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Make sure the Address is filed out properly", Toast.LENGTH_LONG).show();
                }
            }
        });

        //delete button to delete record
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> addressData = ((Contact) getActivity()).setupAddressData(address1, address2, city, state, zipcode);
                Toast.makeText(getActivity(), "Deleting Contact", Toast.LENGTH_LONG).show();
                ((Contact)getActivity()).completeModifyDeleteActivity( "", "Delete", originalRecord, addressData);
            }
        });



        return view;
    }



    //Sets up editTexts, to display on the screen
    protected void setupEditTexts()
    {
        String record = ((Contact)getActivity()).getContactString();

        String[] recordList = record.split(":");

        lastName.setText(recordList[0]);
        firstName.setText(recordList[1]);
        phoneNumber.setText(recordList[2]);
        birthDate.setText(recordList[3]);
        dateMet.setText(recordList[4]);


        String newRecord = (((Contact) getActivity()).setupSaveData(lastName,firstName,phoneNumber,birthDate,dateMet));

        ArrayList<String> address = ((Contact) getActivity()).checkData(newRecord);



        address1.setText(address.get(0) + "");
        address2.setText(address.get(1) + "");
        city.setText(address.get(2) + "");
        state.setText(address.get(3) + "");
        zipcode.setText(address.get(4) + "");

    }

    protected ArrayList<String> getAddress()
    {
        String newRecord = (((Contact) getActivity()).setupSaveData(lastName,firstName,phoneNumber,birthDate,dateMet));

        ArrayList<String> address = ((Contact) getActivity()).checkData(newRecord);

        return address;
    }




    protected void addDate(String date)
    {
        if( ((Contact)getActivity()).getIsBirthDate() )
        {
            birthDate.setText(date);
        }
        else
        {
            dateMet.setText(date);
        }
    }

}
