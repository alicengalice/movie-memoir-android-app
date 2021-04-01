package com.example.mymoviememoir;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mymoviememoir.fragment.MovieMemoir;
import com.example.mymoviememoir.fragment.Watchlist;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

public class MovieView extends AppCompatActivity {
    String moviename = "";
    String releaseyear = "";
    String releasedate = "";
    String movieid = "";
    String imageURL = "";
    String personid = "";

    TextView tvMovieName, tvYear, tvReleaseDate, tvGenre, tvActors, tvDirector, tvCountry, tvPlot;
    ImageView ivPoster;
    RatingBar rbRatingScore;
    Button btnWatchlist, btnMemoir;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_view_layout);
        movieid = getIntent().getStringExtra("movieID");
        personid = getIntent().getStringExtra("personid");
//        moviename = getIntent().getStringExtra("movie");
//        releaseyear = getIntent().getStringExtra("year");

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
                return MovieAPI.searchByMovieId(movieid, new String[]{"num"},
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

                float oldRating = 0;

                if (parts[9].equalsIgnoreCase("n/a")) {
                    oldRating = 0;
                } else {
                    oldRating = Float.parseFloat(parts[9]);
                }
                double newRating = 0;
                if (oldRating >= 0.0 && oldRating <= 0.9) {
                    newRating = 0;
                } else if (oldRating >= 1.0 && oldRating <= 1.8) {
                    newRating = 0.5;
                } else if (oldRating >= 1.9 && oldRating <= 2.7) {
                    newRating = 1;
                } else if (oldRating >= 2.8 && oldRating <= 3.6) {
                    newRating = 1.5;
                } else if (oldRating >= 3.7 && oldRating <= 4.5) {
                    newRating = 2;
                } else if (oldRating >= 4.6 && oldRating <= 5.4) {
                    newRating = 2.5;
                } else if (oldRating >= 5.5 && oldRating <= 6.3) {
                    newRating = 3;
                } else if (oldRating >= 6.4 && oldRating <= 7.2) {
                    newRating = 3.5;
                } else if (oldRating >= 7.3 && oldRating <= 8.1) {
                    newRating = 4;
                } else if (oldRating >= 8.2 && oldRating <= 9.0) {
                    newRating = 4.5;
                } else if (oldRating >= 9.1 && oldRating <= 9.9) {
                    newRating = 5;
                }

                rbRatingScore.setRating((float) newRating);

            }
        }.execute();



        btnWatchlist = findViewById(R.id.btn_addWatchlist);
        btnMemoir = findViewById(R.id.btn_addMemoir);
        btnWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Bundle bundle = new Bundle();
                bundle.putString("movie", moviename);
                bundle.putString("year", releaseyear);
                bundle.putString("releasedate", releasedate);

                // reference: https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
                // set Fragmentclass Arguments
                Fragment watchlist = new Fragment();
                watchlist.setArguments(bundle);

                // reference: https://stackoverflow.com/questions/43449904/how-to-move-from-activity-page-to-fragment-in-android
                /*FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, watchlist).addToBackStack(null);
                fragmentTransaction.commit();*/



            }
        });

        btnMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /*Bundle bundle = new Bundle();
                bundle.putString("movie", moviename);
                bundle.putString("year", releaseyear);
                bundle.putString("releasedate", releasedate);*/


                Intent i = new Intent(MovieView.this, AddToMemoir.class);
                i.putExtra("movie", moviename);
                i.putExtra("year", releaseyear);
                i.putExtra("releasedate", releasedate);
                i.putExtra("imageURL", imageURL);
                i.putExtra("personid", personid);
                startActivity(i);

                // reference: https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
                // set Fragmentclass Arguments
                /*Fragment moviememoir = new MovieMemoir();
                moviememoir.setArguments(bundle);*/

                // reference: https://stackoverflow.com/questions/43449904/how-to-move-from-activity-page-to-fragment-in-android
                /*FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, moviememoir).addToBackStack(null);
                fragmentTransaction.commit();*/




                /*Fragment mFragment = null;
                mFragment = new MovieMemoir();
                mFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();*/



            }
        });
    }

}
