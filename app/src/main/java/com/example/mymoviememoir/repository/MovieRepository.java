package com.example.mymoviememoir.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.mymoviememoir.dao.MovieDAO;
import com.example.mymoviememoir.database.MovieDatabase;
import com.example.mymoviememoir.entity.Movie;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class MovieRepository {
    private MovieDAO dao;
    private LiveData<List<Movie>> allMovies;
    private Movie movie;
    private ExecutorService executorService;


    public MovieRepository(Application application){
        MovieDatabase db = MovieDatabase.getInstance(application);
        dao=db.movieDao();
    }
    public LiveData<List<Movie>> getAllMovies() {
        allMovies=dao.getAll();
        return allMovies;
    }
    public void insert(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(movie);
            }
        });
    }
    public void deleteAll(){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }
    public void delete(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(movie);
            }
        });
    }
    public void insertAll(final Movie... movies){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(movies);
            }
        });
    }
    public void updateMovies(final Movie... movies){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateMovies(movies);
            }
        });
    }

    public void updateMovieByID(final int movieID, final String movieName,
                                final String releaseDate, final String dateTimeAdded) {
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updatebyID(movieID, movieName, releaseDate, dateTimeAdded);
            }
        });
    }

    public Movie findByID(final int movieID) {
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Movie runMovie = dao.findByID(movieID);
                setMovie(runMovie);
            }
        });
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

/*    public Movie findByMovieNameAndReleaseDate(final String movieName, final String releaseDate) {

        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Movie runMovie = dao.findByMovieNameAndReleaseDate(movieName, releaseDate);
                setMovie(runMovie);
            }
        });
        return movie;
    }*/

/*    public Movie findMaxID() {

        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Movie runMovie = dao.findMaxID();
                setMovie(runMovie);
            }
        });
        return movie;
    }*/

    // reference: https://stackoverflow.com/questions/44364240/android-room-get-the-id-of-new-inserted-row-with-auto-generate
    public long insertUploadStatus(final Movie movie) {
        Callable<Long> insertCallable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return dao.insert(movie);
            }
        };
        long rowId = 0;

        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture findByIDFuture(final int movieID) {
        return CompletableFuture.supplyAsync(new Supplier() {
            @Override
            public Object get() {
                return dao.findByID(movieID);
            }
        }, MovieDatabase.databaseWriteExecutor);
    }

}
