package com.example.mymoviememoir;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class MovieViewForMemoir extends AppCompatActivity {

    String moviename = "";
    String releaseyear = "";
    String releasedate = "";
    String movieid = "";
    String imageURL = "";
    String personid = "";
    String rating = "";

    TextView tvMovieName, tvYear, tvReleaseDate, tvGenre, tvActors, tvDirector, tvCountry, tvPlot;
    ImageView ivPoster;
    RatingBar rbRatingScore;

    @SuppressLint("StaticFieldLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_view_memoir_layout);
        moviename = getIntent().getStringExtra("moviename");
        releaseyear = getIntent().getStringExtra("releaseyear").replaceAll("[^\\.0123456789]","");
        rating = getIntent().getStringExtra("rating").replaceAll("[^\\.0123456789]","");

        tvMovieName = findViewById(R.id.tv_view_moviename);
        tvYear = findViewById(R.id.tv_view_year);
        tvReleaseDate = findViewById(R.id.tv_view_releasedate);
        tvGenre = findViewById(R.id.tv_view_genre);
        tvActors = findViewById(R.id.tv_view_actors);
        tvDirector = findViewById(R.id.tv_view_director);
        tvCountry = findViewById(R.id.tv_view_country);
        tvPlot = findViewById(R.id.tv_view_plot);
        ivPoster = findViewById(R.id.iv_poster);
        rbRatingScore = findViewById(R.id.rb_rating);


        //create an anonymous AsyncTask
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return MovieAPI.searchByMovieNameAndYear(moviename, releaseyear, new String[]{"num"},
                        new String[]{"3"});
            }

            @Override
            protected void onPostExecute(String result) {

                // details = title + year + image + genre + actors + releasedate + country + directors + plot + rating;
                String strings = MovieAPI.getDetailsForMovieView(result);
                String[] parts = strings.split("\n");
                tvMovieName.setText(parts[0]);
                tvYear.setText("(" + parts[1] + ")");
                Picasso.get()
                        .load(parts[2])
                        .resize(700, 700)
                        .centerInside()
                        .into(ivPoster);
                tvGenre.setText("Genre: " + parts[3]);
                tvActors.setText("Actors: " + parts[4]);
                tvReleaseDate.setText("Date released: " + parts[5]);
                tvCountry.setText("Country: " + parts[6]);
                tvDirector.setText("Directors: " + parts[7]);
                tvPlot.setText("Summary: " + parts[8]);


                releasedate = parts[5];
                moviename = parts[0];
                releaseyear = parts[1];
                imageURL = parts[2];


                rbRatingScore.setRating(Float.valueOf(rating));

            }
        }.execute();
    }


}
