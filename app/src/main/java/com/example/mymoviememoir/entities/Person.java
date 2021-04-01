package com.example.mymoviememoir.entities;

import java.util.Date;

public class Person {
    private int personid;
    private String firstname;
    private String lastname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;

    public Person(int personid, String firstname, String lastname, String gender, String dob, String address, String state, String postcode) {
        this.personid = personid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
    }

    public Person(int personid) {
        this.personid = personid;
    }

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int id) {
        personid = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
