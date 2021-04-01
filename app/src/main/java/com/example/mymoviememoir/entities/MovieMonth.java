package com.example.mymoviememoir.entities;

public class MovieMonth {
    private String month;
    private Integer numberofmovie;

    public MovieMonth(String month, Integer numberofmovie) {
        this.month = month;
        this.numberofmovie = numberofmovie;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getNumberofmovie() {
        return numberofmovie;
    }

    public void setNumberofmovie(Integer numberofmovie) {
        this.numberofmovie = numberofmovie;
    }
}
