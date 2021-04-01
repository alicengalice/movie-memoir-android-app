package com.example.mymoviememoir.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymoviememoir.entity.Movie;
import com.example.mymoviememoir.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MovieViewModel extends ViewModel {
    private MovieRepository cRepository;
    private MutableLiveData<List<Movie>> allMovies;
    public MovieViewModel () {
        allMovies=new MutableLiveData<>();
    }
    public void setMovies(List<Movie> movies) {
        allMovies.setValue(movies);
    }
    public LiveData<List<Movie>> getAllMovies() {
        return cRepository.getAllMovies();
    }
    public void initalizeVars(Application application){
        cRepository = new MovieRepository(application);
    }
    public void insert(Movie movie) {
        cRepository.insert(movie);
    }
    public void insertAll(Movie... movies) {
        cRepository.insertAll(movies);
    }
    public void deleteAll() {
        cRepository.deleteAll();
    }
    public void update(Movie... movies) {
        cRepository.updateMovies(movies);
    }
    public void updateByID(int movieID, String movieName, String releaseDate,
                           String dateTimeAdded) {
        cRepository.updateMovieByID(movieID,movieName, releaseDate, dateTimeAdded);
    }
    public Movie findByID(int movieID){
        return cRepository.findByID(movieID);
    }
/*    public Movie findByMovieNameAndReleaseDate(String movieName, String releaseDate) {
        return cRepository.findByMovieNameAndReleaseDate(movieName, releaseDate);
    }
    public Movie findMaxID(){
        return cRepository.findMaxID();
    }*/
    public long insertUploadStatus(Movie movie){
        return cRepository.insertUploadStatus(movie);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture findByIDFuture(final int movieID) {
        return cRepository.findByIDFuture(movieID);
    }
}
