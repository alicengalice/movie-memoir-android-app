package com.example.mymoviememoir.entities;

public class Credentials {
    private int creid;
    private String email;
    private String passhash;
    private String signupdate;
    private Person personid;


    public Credentials(int creid, String email, String passhash, String signupdate) {
        this.creid = creid;
        this.email = email;
        this.passhash = passhash;
        this.signupdate = signupdate;
    }


    public int getCreid() {
        return creid;
    }

    public void setCreid(int creid) {
        this.creid = creid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasshash() {
        return passhash;
    }

    public void setPasshash(String passhash) {
        this.passhash = passhash;
    }

    public String getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(String signupdate) {
        this.signupdate = signupdate;
    }

    public int getPersonid() {
        return personid.getPersonid();
    }

    public void setPersonid(int id) {
        personid = new Person(id);
    }
}
