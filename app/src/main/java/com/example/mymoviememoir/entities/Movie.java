package com.example.mymoviememoir.entities;

public class Movie {
    public static String movieName;
    public static String releaseYear;
    public static  String imageLink;

    public Movie(String movieName, String releaseYear, String imageLink) {
        this.movieName = movieName;
        this.releaseYear = releaseYear;
        this.imageLink = imageLink;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
