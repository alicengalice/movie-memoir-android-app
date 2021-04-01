package com.example.mymoviememoir.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.DashboardActivity;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView textView, dateTextView;
    NetworkConnection networkConnection = null;

    List<HashMap<String, String>> movieListArray;
    SimpleAdapter myListAdapter;
    ListView movieList;
    HashMap<String,String> map = new HashMap<String,String>();
    String[] colHEAD = new String[] {"Movie Name","Release Date","Rating Score"};
    int[] dataCell = new int[] {R.id.tv_moviename,R.id.tv_releasedate,R.id.tv_ratingscore};

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        textView=view.findViewById(R.id.tv);
        textView.setText("Good Day!");
        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        Date date = new Date();
        dateTextView = view.findViewById(R.id.tv_date);
        dateTextView.setText(dateFormat.format(date));

        // calling GetFirstName method to display list of movies in dashboard
        networkConnection = new NetworkConnection();

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        String email = bundle.getString("email");
        GetFirstName getFirstName = new GetFirstName();
        getFirstName.execute(email);

        movieListArray = new ArrayList<>();

        /*LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.home_fragment, null);*/

        movieList = view.findViewById(R.id.listView);

        new GetMovieList().execute(email);

        return view;
    }



    private class GetFirstName extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            String email = strings[0];
            String result = "";
            String firstname = "";
            int personid = 0;
            try {
                personid = networkConnection.findPersonIDByEmail(email);
                SharedPreferences sharedPref= getActivity().
                        getSharedPreferences("personid", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                spEditor.putString("personid", String.valueOf(personid));
                spEditor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                firstname = networkConnection.findFirstNameByPersonID(personid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return firstname;
        }

        @Override
        protected void onPostExecute(String firstname) {
            TextView resultTextView = getView().findViewById(R.id.tv_fname);
            resultTextView.setText("How are you, " + firstname + "?");

        }
    }

    private class GetMovieList extends AsyncTask<String, Void, List<HashMap<String, String>>> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            String email = strings[0];
            String result = "";
            String result1 = "";
            int personid = 0;
            try {
                personid = networkConnection.findPersonIDByEmail(email);
                SharedPreferences sharedPref= getActivity().
                        getSharedPreferences("personid", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                spEditor.putString("personid", String.valueOf(personid));
                spEditor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                result = networkConnection.FindMaxRatingScorebyPersonID(personid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // reference: https://www.tutorialspoint.com/android/android_json_parser.htm
            if (result != null) {
                try {
                    JSONArray jsonarray = new JSONArray(result);

                    // Getting JSON Array node
                    // JSONArray movies = jsonarray.getJSONArray("movies");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject movies = jsonarray.getJSONObject(i);
                        String movieName = movies.getString("MovieName");
                        String releaseDate = movies.getString("ReleaseDate");
                        String ratingScore = movies.getString("RatingScore");

                        // Remove 00:00:00 AEST or 00:00:00 AEDT from release date
                        String aest = "00:00:00 AEST";
                        String aedt = "00:00:00 AEDT";
                        if (releaseDate.contains(aest)) {
                            String tempAest = aest + " ";
                            releaseDate = releaseDate.replaceAll(tempAest, "");
                            tempAest = " " + aest;
                            releaseDate = releaseDate.replaceAll(tempAest, "");
                        } else if (releaseDate.contains(aedt)){
                            String tempAedt = aedt + " ";
                            releaseDate = releaseDate.replaceAll(tempAedt, "");
                            tempAedt = " " + aedt;
                            releaseDate = releaseDate.replaceAll(tempAedt, "");
                        }

                        HashMap<String,String> map = new HashMap<String,String>();
                        // adding each child node to HashMap key => value
                        map.put("Movie Name", movieName);
                        map.put("Release Date", releaseDate.substring(3));
                        map.put("Rating Score", ratingScore);

                        // adding movie to movie list

                        movieListArray.add(map);
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                };

            }

            return movieListArray;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> movieListArray) {

            /*String[] colHEAD = new String[] {"Movie Name","Release Date","Rating Score"};
            int[] dataCell = new int[] {R.id.tv_moviename,R.id.tv_releasedate,R.id.tv_ratingscore};*/


            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_view, movieListArray);
            //movieList.setAdapter(arrayAdapter);

            ListAdapter adapter = new SimpleAdapter(getActivity(), movieListArray, R.layout.list_view,
                    new String[]{"Movie Name","Release Date","Rating Score"},
                    new int[]{R.id.tv_moviename,R.id.tv_releasedate,R.id.tv_ratingscore});
            movieList.setAdapter(adapter);
            View header = getLayoutInflater().inflate(R.layout.list_header,null);
            movieList.addHeaderView(header);

        }
    }


}