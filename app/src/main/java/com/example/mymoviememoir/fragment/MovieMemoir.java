package com.example.mymoviememoir.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.MovieAPI;
import com.example.mymoviememoir.MovieViewForMemoir;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.Memoir;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MovieMemoir extends Fragment {

    ImageView ivPoster;
    TextView tvMovieName, tvReleaseYear, tvReleaseDate, tvDateTimeWatched, tvCinemaSuburb, tvComment, tvRating;
    ListView memoirListView;

    String moviename = "";
    String releaseyear = "";
    String releasedate = "";
    String datetimewatched = "";
    String cinemasuburb = "";
    String comment = "";
    String rating = "";
    String imageURL = "";
    String personid = "";
    String genre = "";
    String publicRating = "";


    String movieDetails = "";
    NetworkConnection networkConnection = null;

    MovieAPI movieAPI = null;

    List<HashMap<String, String>> memoirList;
    HashMap<String,String> map = new HashMap<String,String>();

    ArrayList<String> movieNameList;
    ArrayList<Double> ratingList;
    ArrayList<String> postcodeList;

    ArrayList<HashMap<String, String>> comedyList;
    ArrayList<HashMap<String, String>> familyList;
    ArrayList<HashMap<String, String>> thrillerList;
    Spinner sortSpinner, filterSpinner;

    Memoir memoir = null;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.movie_memoir_layout, container, false);
        tvMovieName = view.findViewById(R.id.tv_memoir_moviename);
        tvReleaseDate = view.findViewById(R.id.tv_memoir_releasedate);
        tvDateTimeWatched = view.findViewById(R.id.tv_memoir_datetimewatched);
        tvCinemaSuburb = view.findViewById(R.id.tv_memoir_cinemasuburb);
        tvComment = view.findViewById(R.id.tv_memoir_comment);
        tvReleaseYear = view.findViewById(R.id.tv_memoir_releaseyear);

        tvRating = view.findViewById(R.id.tv_memoir_rating);

        ivPoster = view.findViewById(R.id.iv_memoir_poster);
        memoirListView = view.findViewById(R.id.listView);
        memoirList = new ArrayList<>();

        networkConnection = new NetworkConnection();
        movieAPI = new MovieAPI();

        movieNameList = new ArrayList<>();
        postcodeList = new ArrayList<>();
        ratingList = new ArrayList<>();

        comedyList = new ArrayList<HashMap<String, String>>();
        familyList = new ArrayList<HashMap<String, String>>();
        thrillerList = new ArrayList<HashMap<String, String>>();

        // Receive personid
        SharedPreferences sharedPref = getActivity().
                getSharedPreferences("personid", Context.MODE_PRIVATE);
        personid = sharedPref.getString("personid", null);

        GetAllMovies getAllMovies = new GetAllMovies();
        getAllMovies.execute(personid);


        sortSpinner = view.findViewById(R.id.sp_sorting);
        List<String> sortList = new ArrayList<String>();
        sortList.add("Sort by Movie Release Date");
        sortList.add("Sort by User Rating");
        sortList.add("Sort by Public Rating");
        final ArrayAdapter<String> spinnerAdapter = new
                ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortList);
        sortSpinner.setAdapter(spinnerAdapter);

        filterSpinner = view.findViewById(R.id.sp_filtering);
        List<String> filterList = new ArrayList<String>();
        filterList.add("Comedy");
        filterList.add("Family");
        filterList.add("Thriller");
        final ArrayAdapter<String> spinnerAdapter1 = new
                ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, filterList);
        filterSpinner.setAdapter(spinnerAdapter1);

        return view;
    }


    private class GetAllMovies extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            personid = strings[0];
            String result = "";
            try {
                result = networkConnection.FindAllMoviesbyPersonID(Integer.valueOf(personid));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            try {
                //JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    moviename = jsonArray.getJSONObject(i).getString("MovieName");
                    releasedate = jsonArray.getJSONObject(i).getString("ReleaseDate");
                    releaseyear = jsonArray.getJSONObject(i).getString("ReleaseYear");
                    datetimewatched = jsonArray.getJSONObject(i).getString("WatchedDateTime");
                    cinemasuburb = jsonArray.getJSONObject(i).getString("CinemaPostcode");
                    comment = jsonArray.getJSONObject(i).getString("Comment");
                    rating = jsonArray.getJSONObject(i).getString("RatingScore");

                    //String result1 = MovieAPI.searchByMovieNameAndYear(moviename, releaseyear, new String[]{"num"},
                            //new String[]{"3"});

                    GetMovieDetails getMovieDetails = new GetMovieDetails();
                    try {
                        getMovieDetails.execute(moviename,releaseyear).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject jsonObject1 = new JSONObject(movieDetails);
                        genre = jsonObject1.getString("Genre");
                        publicRating = jsonObject1.getString("imdbRating");
                        imageURL = jsonObject1.getString("Poster");
                        Log.e ( "response", "" + imageURL );
                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }

                    float oldRating = 0;

                    if (publicRating.equalsIgnoreCase("n/a")) {
                        oldRating = 0;
                    } else {
                        oldRating = Float.parseFloat(publicRating);
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


                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("MovieName", moviename);
                    map.put("ReleaseDate", releasedate);
                    map.put("ReleaseYear", "(" + releaseyear + ")");
                    map.put("WatchedDateTime", "Date time watched: " + datetimewatched);
                    map.put("CinemaPostcode", "Cinema poscode: " + cinemasuburb);
                    map.put("Comment", "Comment: " + comment);
                    map.put("Rating", "User rating score: " + rating);
                    map.put("Poster", imageURL);
                    map.put("Genre", genre);
                    map.put("imdbRating", "Public rating score: " + newRating);

                    movieNameList.add(moviename);
                    postcodeList.add(cinemasuburb);
                    ratingList.add(Double.valueOf(rating));

                    memoirList.add(map);
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }

            final ListAdapter adapter = new SimpleAdapter(getActivity(), memoirList, R.layout.list_view_for_movie_memoir,
                    new String[]{"MovieName", "ReleaseDate", "ReleaseYear", "WatchedDateTime", "CinemaPostcode", "Comment", "Rating", "imdbRating", "Poster"},
                    new int[]{R.id.tv_memoir_moviename, R.id.tv_memoir_releasedate, R.id.tv_memoir_releaseyear, R.id.tv_memoir_datetimewatched,
                    R.id.tv_memoir_cinemasuburb, R.id.tv_memoir_comment, R.id.tv_memoir_rating, R.id.tv_memoir_publicRating, R.id.iv_memoir_poster});
            memoirListView.setAdapter(adapter);


            // Sorting options - Spinner
            // sortSpinner.setSelection(1);
            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long
                        id) {
                    String selectedSort = parent.getItemAtPosition(position).toString();
                    if(selectedSort.equalsIgnoreCase("Sort by Movie Release Date")){
                        Collections.sort(memoirList, new Comparator<HashMap<String, String>>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                                /*int result = 0;
                                DateTimeFormatter dtf = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    //dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
                                    dtf = new DateTimeFormatterBuilder().parseCaseInsensitive()
                                            .append(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")).toFormatter();
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    String o1date = Objects.toString(o1.get("ReleaseDate"));
                                    String o2date = Objects.toString(o2.get("ReleaseDate"));
                                    ZonedDateTime zdt1 = OffsetDateTime.parse(o1date, dtf)
                                            .atZoneSameInstant(ZoneId.of("Australia/Sydney"));
                                    ZonedDateTime zdt2 = OffsetDateTime.parse(o2date, dtf)
                                            .atZoneSameInstant(ZoneId.of("Australia/Sydney"));
                                    String dateInTimeZone1 = zdt1.format(dtf);
                                    String dateInTimeZone2 = zdt2.format(dtf);

                                    result = dateInTimeZone1.compareTo(dateInTimeZone2);

                                    //result = Objects.requireNonNull(OffsetDateTime.parse(o2.get("ReleaseDate"),dtf).atZoneSameInstant(ZoneId.of("Australia/Sydney"))).
                                             //compareTo(OffsetDateTime.parse(Objects.requireNonNull(o1.get("ReleaseDate")),dtf).atZoneSameInstant(ZoneId.of("Australia/Sydney")));
                                }*/
                                DateTimeFormatter f = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    f = new DateTimeFormatterBuilder().appendPattern("MMM dd HH:mm:ss yyyy")
                                            .toFormatter();
                                }
                                String o1date = Objects.toString(o1.get("ReleaseDate")).substring(4).replace("AEDT",  "").replace("AEST", "").replaceAll("\\s{2,}", " ");
                                String o2date = Objects.toString(o2.get("ReleaseDate")).substring(4).replace("AEDT",  "").replace("AEST", "").replaceAll("\\s{2,}", " ");
                                LocalDate parsedDate1 = LocalDate.parse(o1date, f);
                                LocalDate parsedDate2 = LocalDate.parse(o2date, f);
                                DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                o1date = parsedDate1.format(f2);
                                o2date = parsedDate2.format(f2);

                                return o2date.compareTo(o1date);
                            }
                        });
                        memoirListView.setAdapter(adapter);
                    } else if (selectedSort.equalsIgnoreCase("Sort by User Rating")) {
                        Collections.sort(memoirList, new Comparator<HashMap<String, String>>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                                return Objects.requireNonNull(Double.valueOf(o2.get("Rating").toString().replaceAll("[^\\.0123456789]",""))).
                                        compareTo(Objects.requireNonNull(Double.valueOf(o1.get("Rating").toString().replaceAll("[^\\.0123456789]",""))));
                            }
                        });
                        memoirListView.setAdapter(adapter);
                    } else if (selectedSort.equalsIgnoreCase("Sort by Public Rating")) {
                        Collections.sort(memoirList, new Comparator<HashMap<String, String>>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                                return Objects.requireNonNull(Double.valueOf(o2.get("imdbRating").toString().replaceAll("[^\\.0123456789]",""))).
                                        compareTo(Objects.requireNonNull(Double.valueOf(o1.get("imdbRating").toString().replaceAll("[^\\.0123456789]",""))));
                            }
                        });
                        memoirListView.setAdapter(adapter);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long
                        id) {
                    String filter = parent.getItemAtPosition(position).toString();
                    if (filter.equalsIgnoreCase("Comedy")) {
                        List<HashMap<String, String>> filteredList = new ArrayList<>();
                        for(HashMap<String, String> memoir : memoirList) {
                            if((memoir.get("Genre").contains("Comedy"))){
                                filteredList.add(memoir);
                            }
                        }
                        memoirList.clear();
                        memoirList.addAll(filteredList);
                        memoirListView.setAdapter(adapter);
                    } else if (filter.equalsIgnoreCase("Family")) {
                        List<HashMap<String, String>> filteredList = new ArrayList<>();
                        for(HashMap<String, String> memoir : memoirList) {
                            if((memoir.get("Genre").contains("Family"))){
                                filteredList.add(memoir);
                            }
                        }
                        memoirList.clear();
                        memoirList.addAll(filteredList);
                        memoirListView.setAdapter(adapter);
                    } else if (filter.equalsIgnoreCase("Thriller")) {
                        List<HashMap<String, String>> filteredList = new ArrayList<>();
                        for(HashMap<String, String> memoir : memoirList) {
                            if((memoir.get("Genre").contains("Thriller"))){
                                filteredList.add(memoir);
                            }
                        }
                        memoirList.clear();
                        memoirList.addAll(filteredList);
                        memoirListView.setAdapter(adapter);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });



            memoirListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    String moviename = ((TextView) view.findViewById(R.id.tv_memoir_moviename)).getText().toString();
                    String releaseyear = ((TextView) view.findViewById(R.id.tv_memoir_releaseyear)).getText().toString();
                    String rating = ((TextView) view.findViewById(R.id.tv_memoir_rating)).getText().toString();
                    Intent i = new Intent(getActivity(), MovieViewForMemoir.class);
                    i.putExtra("moviename", moviename);
                    i.putExtra("releaseyear", releaseyear);
                    i.putExtra("rating", rating);
                    startActivity(i);
                }
            });



            // reference: https://stackoverflow.com/questions/30314314/image-from-url-in-listview-using-simpleadapter

            if (imageURL != null) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(i);
                    String imgUrl = (String) hm.get("Poster");

                    if (imgUrl != null) {

                        HashMap<String, Object> hmDownload = new HashMap<String, Object>();
                        hmDownload.put("Poster", imgUrl);
                        hmDownload.put("position", i);

                        ImageLoaderTask imageLoaderTask = new ImageLoaderTask();

                        // Starting ImageLoaderTask to download and populate image in the listview
                        imageLoaderTask.execute(hmDownload);
                    }
                }
            }

        }

        // reference: https://stackoverflow.com/questions/30314314/image-from-url-in-listview-using-simpleadapter
        private class ImageLoaderTask extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>>{

            @Override
            protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {


                InputStream iStream= null;
                String imgUrl = (String) hm[0].get("Poster");
                int position = (Integer) hm[0].get("position");

                URL url;
                try {
                    url = new URL(imgUrl);

                    // Creating an http connection to communicate with url
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    // Connecting to url
                    urlConnection.connect();

                    // Reading data from url
                    iStream = urlConnection.getInputStream();

                    // Getting Caching directory
                    File cacheDirectory = getActivity().getBaseContext().getCacheDir();

                    // Temporary file to store the downloaded image
                    File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"+position+".png");

                    // The FileOutputStream to the temporary file
                    FileOutputStream fOutStream = new FileOutputStream(tmpFile);

                    // Creating a bitmap from the downloaded inputstream
                    Bitmap b = BitmapFactory.decodeStream(iStream);

                    // Writing the bitmap to the temporary file as png or jpeg file
                    b.compress(Bitmap.CompressFormat.JPEG,10, fOutStream);

                    // Flush the FileOutputStream
                    fOutStream.flush();

                    //Close the FileOutputStream
                    fOutStream.close();

                    // Create a hashmap object to store image path and its position in the listview
                    HashMap<String, Object> hmBitmap = new HashMap<String, Object>();

                    // Storing the path to the temporary image file
                    hmBitmap.put("Poster", tmpFile.getPath());

                    // Storing the position of the image in the listview
                    hmBitmap.put("position", position);


                    // Returning the HashMap object containing the image path and position
                    return hmBitmap;



                }catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> result) {
                String path = "";
                // Getting the path to the downloaded image
                if(result != null) {
                    path = (String) result.get("Poster");
                }

                // Getting the position of the downloaded image
                int position = (Integer) result.get("position");

                // Getting adapter of the listview
                SimpleAdapter adapter = (SimpleAdapter ) memoirListView.getAdapter();

                // Getting the hashmap object at the specified position of the listview
                HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);

                // Overwriting the existing path in the adapter
                hm.put("Poster", path);

                // Noticing listview about the dataset changes
                adapter.notifyDataSetChanged();

            }
        }

    }

    private class GetMovieDetails extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            String moviename = strings[0];
            String year = strings[1];
            try {
                movieDetails = MovieAPI.searchByMovieNameAndYear(moviename, year, new String[]{"num"},
                        new String[]{"3"});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieDetails;
        }
    }
}

