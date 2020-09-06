package com.example.contact;


// Written by Cade Cottrell for CS4301.002, Contact Phase 1 Assignment
// netid: cac160030

//Determines whether we need to open Add fragment or Modify/Delete Fragment
public class UpdateOrAddContact {


    protected boolean detectAddOrModify(String detect)
    {
        if(detect.equals("Add"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
