package com.example.mymoviememoir.entities;

public class Cinema {
    private int cinemaid;
    private String cinemaname;
    private String postcode;

    public Cinema(int cinemaid, String cinemaname, String postcode) {
        this.cinemaid = cinemaid;
        this.cinemaname = cinemaname;
        this.postcode = postcode;
    }

    public Cinema(int cinemaid) {
        this.cinemaid = cinemaid;
    }

    public int getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(int cinemaid) {
        this.cinemaid = cinemaid;
    }

    public String getCinemaName() {
        return cinemaname;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaname = cinemaName;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
