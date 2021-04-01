package com.example.mymoviememoir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddToMemoir extends AppCompatActivity {
    ImageView ivPoster;
    TextView tvMoviename, tvYear, tvRating;

    EditText etCinemaName, etPostcode, etComment;

    DatePicker picker;
    TimePicker timepicker;
    Button btnGet;
    TextView tvDateTime;
    String selectedDateTime;

    String movie = "";
    String year = "";
    String imgURL = "";
    String releasedate = "";
    String personid = "";
    String memid = "";
    String cinemaid = "";
    Spinner spinner;
    Spinner cinemaSpinner;
    Button btnCinema;
    String selectedCinema;

    NetworkConnection networkConnection = null;

    ArrayList<String> cinemaList = new ArrayList<>();

    RatingBar ratingBar;
    String selectedRating;

    List<Integer> cinemaIDlist = new ArrayList<Integer>();

    String comment;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_memoir_layout);

        selectedCinema = "";
        selectedDateTime = "";
        selectedRating = "";
        comment = "";

        networkConnection = new NetworkConnection();

        movie = getIntent().getStringExtra("movie");
        year = getIntent().getStringExtra("year");
        imgURL = getIntent().getStringExtra("imageURL");
        releasedate = getIntent().getStringExtra("releasedate");
        personid = getIntent().getStringExtra("personid");



        // Release date : 16 Nov 2001 -> 2001-11-16

        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("dd MMM yyyy")
                .toFormatter();
        LocalDate parsedDate = LocalDate.parse(releasedate, f);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        releasedate = parsedDate.format(f2) + "T00:00:00+10:00";

        /*String formatReleaseDate = "";
        // reference: https://stackoverflow.com/questions/16426703/how-to-convert-a-date-dd-mm-yyyy-to-yyyy-mm-dd-hhmmss-android
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd mmm yyyy");
        //SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-mm-dd" );
        String pattern = "yyyy-mm-dd";
        Date date;
        try {
            date = originalFormat.parse(releasedate);
            DateFormat df = new SimpleDateFormat(pattern);
            formatReleaseDate = df.format(date);
            releasedate = formatReleaseDate + "T00:00:00+10:00";

        } catch (ParseException e) {
            e.printStackTrace();
        }
*/

        tvMoviename = findViewById(R.id.tv_addmemoir_moviename);
        tvYear = findViewById(R.id.tv_addmemoir_year);
        ivPoster = findViewById(R.id.iv_addmemoir_poster);
        Picasso.get()
                .load(imgURL)
                .resize(700, 700)
                .centerInside()
                .into(ivPoster);
        tvMoviename.setText(movie);
        tvYear.setText("(" + year +")");


        // credit: https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
        tvDateTime = (TextView) findViewById(R.id.tv_addmemoir_datetime);
        picker = (DatePicker) findViewById(R.id.dp_datetime);
        btnGet = (Button) findViewById(R.id.btn_datetime);
        timepicker = (TimePicker) findViewById(R.id.time_picker);
        // credit: https://stackoverflow.com/questions/14194109/datepicker-in-android-get-month-and-day-as-mm-dd
        btnGet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                Calendar calendar = new GregorianCalendar(picker.getYear(),
                        picker.getMonth(),
                        picker.getDayOfMonth(),
                        timepicker.getCurrentHour(),
                        timepicker.getCurrentMinute());

                Integer month = picker.getMonth() + 1;
                Integer day = picker.getDayOfMonth();
                selectedDateTime = picker.getYear() + "-" + ((month.toString().length() == 1 ? "0" + month.toString() : month.toString()))
                        + "-" + ((day.toString().length() == 1 ? "0" + day.toString() : day.toString())) + "T" +
                        timepicker.getCurrentHour() + ":" + timepicker.getCurrentMinute() + ":00+10:00";
                tvDateTime.setText("Date and time watched: " + selectedDateTime);
            }
        });



        //cinemaSpinner = (Spinner) findViewById(R.id.cinemaSpinner);
        etCinemaName = (EditText) findViewById(R.id.et_addcinemaname);
        etPostcode = (EditText) findViewById(R.id.et_addpostcode);

        getCinemaAndPostcode getCinemaAndPostcode = new getCinemaAndPostcode();
        getCinemaAndPostcode.execute();

        spinner = findViewById(R.id.cinemaSpinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (AddToMemoir.this ,android.R.layout.simple_spinner_item, cinemaList);
//        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCinema = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Cinema selected is " + selectedCinema,
                        Toast.LENGTH_LONG).show();
                cinemaid = String.valueOf(selectedCinema.charAt(0));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button addButton = findViewById(R.id.btn_addcinema);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newCinemaName = etCinemaName.getText().toString();
                String newPostcode = etPostcode.getText().toString();
                String newCinemaId = String.valueOf(Collections.max(cinemaIDlist) + 1);

                if(newCinemaName.equalsIgnoreCase("") || newPostcode.equalsIgnoreCase("")) {
                    Toast.makeText(AddToMemoir.this, "Not enough details to add a new cinema.",
                            Toast.LENGTH_LONG).show();
                } else if (newCinemaName.equalsIgnoreCase("") || newPostcode.equalsIgnoreCase("")) {
                    Toast.makeText(AddToMemoir.this, "Not enough details to add a new cinema.",
                            Toast.LENGTH_LONG).show();
                } else {
                    AddCinema addCinema = new AddCinema();
                    addCinema.execute(newCinemaId, newCinemaName, newPostcode);
                    String newCinema = newCinemaId + newCinemaName + newPostcode;
                    selectedCinema = newCinema;
                    spinnerAdapter.add(newCinema);;
                    spinnerAdapter.notifyDataSetChanged();
                    spinner.setSelection(spinnerAdapter.getPosition(newCinema));
                }
            }});



        // reference: https://www.youtube.com/watch?v=LpNJhJF3gW8
        tvRating = findViewById(R.id.tv_addrating);
        ratingBar = findViewById(R.id.ratingbar);
        Button submitButton = findViewById(R.id.btn_submitrating);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRating = String.valueOf(ratingBar.getRating());
                tvRating.setText("Your rating is: " + selectedRating);
            }
        });

        // Comment
        etComment = findViewById(R.id.et_comment);


        // Click add memoir
        Button addMemoirButton = findViewById(R.id.btn_submitdetails);
        addMemoirButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                comment = etComment.getText().toString();
                FindMemId findMemId = new FindMemId();
                try {
                    findMemId.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String memoir = memid + "&" + movie + "&" + releasedate + "&"
                        + selectedDateTime + "&" + comment + "&" + selectedRating + "&" + personid + "&" + cinemaid;
                String[] details = memoir.split("&");
                if (details.length == 8) {
                    AddMemoir addMemoir = new AddMemoir();
                    addMemoir.execute(details);
                    Toast.makeText(AddToMemoir.this, "New memoir added!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddToMemoir.this, "Error. Cannot add memoir.",
                            Toast.LENGTH_LONG).show();
                }
            }

        });

    }


    private class getCinemaAndPostcode extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                result = networkConnection.FindCinemaAndPostcode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject cinemas = jsonarray.getJSONObject(i);
                        String cinemaId = cinemas.getString("cinemaid");
                        String cinemaName = cinemas.getString("cinemaname");
                        String postcode = cinemas.getString("postcode");

                        String details = cinemaId + cinemaName + postcode;
                        cinemaList.add(details);
                        cinemaIDlist.add(Integer.valueOf(cinemaId));
                    }
                    final Spinner spinner = findViewById(R.id.cinemaSpinner);
                    final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (AddToMemoir.this ,android.R.layout.simple_spinner_item, cinemaList);
                    spinner.setAdapter(spinnerAdapter);
                } catch (final JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private class AddCinema extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            //memid = String.valueOf(networkConnection.findMaxMemID() + 1);
            String message= "";
            if (networkConnection.addCinema(params).startsWith("<")) {
                message = "Not enough information to add a new cinema.";
            } else {
                message = "New cinema added!";
            }
            return message;
        }
        @Override
        protected void onPostExecute(String result) {
            TextView resultTextView = findViewById(R.id.tv_displaytextcinema);
            resultTextView.setText(result);
        }
    }

    private class AddMemoir extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            //memid = String.valueOf(networkConnection.findMaxMemID() + 1);
            String message= "New memoir added!";
            return networkConnection.addMemoir(params)+ message ;
        }
        @Override
        protected void onPostExecute(String result) {
            TextView resultTextView = findViewById(R.id.tv_displaytextmemoir);
            resultTextView.setText(result);
        }
    }

    private class FindMemId extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            memid = String.valueOf(networkConnection.findMaxMemID() + 1);
            return memid;
        }
    }

}
