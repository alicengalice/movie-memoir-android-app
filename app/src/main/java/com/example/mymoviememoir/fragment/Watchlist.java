package com.example.mymoviememoir.fragment;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.database.MovieDatabase;
import com.example.mymoviememoir.entity.Movie;
import com.example.mymoviememoir.viewmodel.MovieViewModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Watchlist extends Fragment {

    MovieDatabase db = null;
    TextView tvMoviedetails = null;
    TextView textView_insert = null;
    TextView textView_read = null;
    TextView textView_delete = null;
    TextView textView_update = null;
    MovieViewModel movieViewModel;
    Movie movie;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        String year = this. getArguments().getString("year");
        final String releasedate = this. getArguments().getString("releasedate");
        final String[] dateTimeAdded = {""};
        View view = inflater.inflate(R.layout.watchlist_layout, container, false);
        final String moviename = this.getArguments().getString("movie");

        final Movie movie = new Movie(0, "","","");

        Button addButton = view.findViewById(R.id.addButton);
        tvMoviedetails = view.findViewById(R.id.tv_moviedetails);

        textView_insert = view.findViewById(R.id.textView_add);
        textView_read = view.findViewById(R.id.textView_read);
        Button deleteButton = view.findViewById(R.id.deleteButton);
        textView_delete = view.findViewById(R.id.textView_delete);
        Button updateButton = view.findViewById(R.id.updateButton);
        textView_update = view.findViewById(R.id.textView_update);
        movieViewModel = new
                ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.initalizeVars(getActivity().getApplication());
        movieViewModel.getAllMovies().observe(this, new
                Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable final List<Movie> movies) {
                        String allMovies = "";
                        for (Movie temp : movies) {
                            String moviestr = (temp.getMovieID() + " " +
                                    temp.getMovieName() + " " + temp.getReleaseDate() + " " + temp.getDateTimeAdded());
                            allMovies = allMovies +
                                    System.getProperty("line.separator") + moviestr;
                        }
                        textView_read.setText("All movies: " + allMovies);
                    }
                });
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!(tvMoviedetails.getText().toString().isEmpty())) {
                    dateTimeAdded[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    long movieID = movieViewModel.insertUploadStatus(movie);
                    tvMoviedetails.setText(movieID + "&" + moviename + "&" + releasedate + "&" + dateTimeAdded[0]);
                    String[] details = tvMoviedetails.getText().toString().split("-");
                    if (details.length == 3) {
                        Movie movie = new Movie(Integer.parseInt(details[0]),
                                details[1], details[2], details[3]);
                        movieViewModel.insert(movie);
                        textView_insert.setText("Added Record: " + Arrays.toString(details));
                    }
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                movieViewModel.deleteAll();
                textView_delete.setText("All data was deleted");
            }
        });
/*        updateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String[] details = tvMoviedetails.getText().toString().split(" ");
                if (details.length == 4) {
                    movieViewModel.updateByID(new Integer(details[0]).intValue(),
                            details[1], details[2], new Double(details[3]).doubleValue());
                }
            }
        });*/

        return view;
    }


}

