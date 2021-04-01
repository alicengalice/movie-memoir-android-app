package com.example.mymoviememoir.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymoviememoir.entity.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDAO {
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAll();
    @Query("SELECT * FROM movie WHERE movieID = :movieID LIMIT 1")
    Movie findByID(int movieID);
/*    @Query("SELECT movie_name, release_date FROM movie WHERE movie_name = :movieName AND release_date=:releaseDate AND movie)
    Movie findByMovieNameAndReleaseDate(int movieID, String movieName, String releaseDate);
    @Query("SELECT movieID FROM movie WHERE movieID  =( SELECT max(movieID) FROM movie )")
    Movie findMaxID();*/
    @Insert
    void insertAll(Movie... movies);
    @Insert
    long insert(Movie movies);
    @Delete
    void delete(Movie movie);
    @Update(onConflict = REPLACE)
    void updateMovies(Movie... movies);
    @Query("DELETE FROM movie")
    void deleteAll();
    @Query("UPDATE movie SET movie_name = :movieName, release_date=:releaseDate, date_time_added=:dateTimeAdded WHERE movieID = :id")
    void updatebyID(int id, String movieName, String releaseDate, String dateTimeAdded);



}

