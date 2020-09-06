package com.example.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;


// Written by Cade Cottrell for CS4301.002, Contact Phase 1 Assignment
// netid: cac160030

public class ContactMainMenu extends Fragment {

    EditText birthDate;
    EditText dateMet;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;

    private EditText address1;
    private EditText address2;
    private EditText city;
    private EditText state;
    private EditText zipcode;

    Button saveButton;



    //OnCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_contact_fragment, container, false);

        //Find ids
        birthDate = view.findViewById(R.id.birthCalendar);
        dateMet = view.findViewById(R.id.firstMetCalendar);
        firstName = view.findViewById(R.id.firstNameEdit);
        lastName = view.findViewById(R.id.lastNameEdit);
        phoneNumber = view.findViewById(R.id.phoneNumberEdit);

        address1 = view.findViewById(R.id.Address1);
        address2 = view.findViewById(R.id.Address2);
        city = view.findViewById(R.id.City);
        state = view.findViewById(R.id.State);
        zipcode = view.findViewById(R.id.Zipcode);


        saveButton = view.findViewById(R.id.Save);



        //Fragment onClick listeners were giving me AddFrag hard time, this
        //anonymous class fixed the problem. (I avoided this where I could)
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


        // Save button, writes new Record, setsup the address data and the record data
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checks to see if any input was put into address
                if( ((Contact)getActivity()).addressLengthCheck(address1, address2, city, state, zipcode)) {

                    ArrayList<String> addressData = ((Contact) getActivity()).setupAddressData(address1, address2, city, state, zipcode);

                    if (((Contact) getActivity()).allFilled(lastName, firstName, phoneNumber, dateMet) && ((Contact) getActivity()).notOverSized(lastName, firstName, phoneNumber, birthDate, dateMet)) {

                        Toast.makeText(getActivity(), "Saving Contact", Toast.LENGTH_LONG).show();

                        String record = ((Contact) getActivity()).setupSaveData(lastName, firstName, phoneNumber, birthDate, dateMet);

                        ((Contact) getActivity()).completeAddActivity(record, addressData);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Make sure everything is filled out.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Make sure the Address is filed out properly", Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;
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
