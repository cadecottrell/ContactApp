package com.example.contact;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


// Written by Cade Cottrell for CS4301.002, Contact Phase 1 Assignment
// netid: cac160030

public class CalendarFragment extends Fragment {

    Button saveButton;
    Button backButton;

    TextView dateText;

    CalendarView calendarView;
    String date;


    //OnCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_fragment_layout, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        dateText = view.findViewById(R.id.date);
        saveButton = view.findViewById(R.id.saveButton);
        backButton = view.findViewById(R.id.backButton);


        //If user hits back button reset the Calendar and dont change anything
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setDate(System.currentTimeMillis(),false, true);
                backToInvisible();
                ((Contact)getActivity()).setViewPager(0);
            }
        });

        //Save what the user clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Contact)getActivity()).setDate(date);
                ((Contact)getActivity()).updateDate();
                calendarView.setDate(System.currentTimeMillis(),false, true);
                backToInvisible();
                ((Contact)getActivity()).setViewPager(0);
            }
        });


        //detects date change.
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month++;

                if(month < 10 && dayOfMonth < 10)
                {
                    date = "0" + month + "/0" + dayOfMonth + "/" + year;
                }
                else if(month < 10)
                {
                    date = "0" + month + "/" + dayOfMonth + "/" + year;
                }
                else
                {
                    date = month + "/0" + dayOfMonth + "/" + year;
                }

                if(dateText.getVisibility() == View.INVISIBLE)
                {
                    dateText.setVisibility(View.VISIBLE);
                }
                dateText.setText(date);
            }
        });


        return view;
    }

    protected void backToInvisible()
    {
        if(dateText.getVisibility() == View.VISIBLE)
        {
            dateText.setVisibility(View.INVISIBLE);
        }
    }

}
