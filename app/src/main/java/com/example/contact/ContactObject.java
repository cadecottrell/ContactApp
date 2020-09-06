package com.example.contact;


// Written by Cade Cottrell for CS4301.002, Contact Phase 1 Assignment
// netid: cac160030


// ContactObject holds all things related to what a Contact holds
public class ContactObject {

    private String lastName;
    private String firstName;
    private String phone;
    private String birthDate;
    private String dateMet;


    //Constructor
    public ContactObject(String lastName, String firstName, String phone, String birthDate, String dateMet) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.birthDate = birthDate;
        this.dateMet = dateMet;
    }

    //Getters and Setters

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDateMet() {
        return dateMet;
    }

    public void setDateMet(String dateMet) {
        this.dateMet = dateMet;
    }
}
