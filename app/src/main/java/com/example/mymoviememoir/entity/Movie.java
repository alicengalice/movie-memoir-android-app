package com.example.mymoviememoir.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {
    @PrimaryKey(autoGenerate = true)
    public int movieID;
    @ColumnInfo(name = "movie_name")
    public String movieName;
    @ColumnInfo(name = "release_date")
    public String releaseDate;
    @ColumnInfo(name = "date_time_added")
    public String dateTimeAdded;

    public Movie(int movieID, String movieName, String releaseDate, String dateTimeAdded) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.dateTimeAdded = dateTimeAdded;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }
}