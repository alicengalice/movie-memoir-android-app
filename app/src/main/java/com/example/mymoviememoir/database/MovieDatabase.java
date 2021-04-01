package com.example.mymoviememoir.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mymoviememoir.dao.MovieDAO;
import com.example.mymoviememoir.entity.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDAO movieDao();
    private static MovieDatabase INSTANCE;
    //we create an ExecutorService with a fixed thread pool so we can later
    //run database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static synchronized MovieDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, "MovieDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
