package com.example.mymoviememoir.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.MovieAPI;
import com.example.mymoviememoir.MovieView;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.SearchGoogleAPI;
import com.example.mymoviememoir.adapter.CustomAdapterForMovieSearch;
import com.example.mymoviememoir.entities.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieSearch  extends Fragment  {
    private TextView tv, moviename, year, click;
    private EditText etMovieName, etYear;
    private ImageView ivposter;
    ListView movieList;

    String movie = "";
    String releaseyear = "";
    String image = "";
    String movieID = "";
    List<HashMap<String, String>> movieListArray;
    HashMap<String,String> map = new HashMap<String,String>();

    public MovieSearch() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.search_layout, container, false);
        tv = view.findViewById(R.id.tv_Result);
        etMovieName = view.findViewById(R.id.et_keyword);
        etYear = view.findViewById(R.id.et_year);

        moviename = view.findViewById(R.id.tv_result_moviename);
        year = view.findViewById(R.id.tv_result_year);
        ivposter = view.findViewById(R.id.iv_poster);
        click = view.findViewById(R.id.tv_click);
        movieList = view.findViewById(R.id.listView);
        movieListArray = new ArrayList<>();

        // Receive personid
        SharedPreferences sharedPref= getActivity().
                getSharedPreferences("personid", Context.MODE_PRIVATE);
        final String personid = sharedPref.getString("personid",null);


        Button btnSearch = view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(final View view) {

                final String keyword = etMovieName.getText().toString();
                final String inputYear = etYear.getText().toString();
                /* create an anonymous AsyncTask */
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        return MovieAPI.search(keyword, inputYear, new String[]{"num"},
                                new String[]{"3"});
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("Search");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                movieID = jsonArray.getJSONObject(i).getString("imdbID") + "\n";
                                String title = jsonArray.getJSONObject(i).getString("Title") + "\n";
                                String releaseYear = jsonArray.getJSONObject(i).getString("Year") + "\n";
                                String poster = jsonArray.getJSONObject(i).getString("Poster") + "\n\n";

                                HashMap<String,String> map = new HashMap<String,String>();
                                map.put("imdbID", movieID);
                                map.put("Title", title);
                                map.put("Year", releaseYear);
                                map.put("Poster", poster);

                                movieListArray.add(map);


                                //String[] parts = strings.split("\n");
                                /*movie = parts[0];
                                releaseyear = parts[1];
                                image = parts[2];*/
                                /*moviename.setText(title);
                                year.setText(releaseYear);
                                Picasso.get()
                                        .load(poster)
                                        .resize(400, 300)
                                        .centerInside()
                                        .into(ivposter);
                                click.setText("Click here for more details");*/
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        };

                        /*click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                Intent i = new Intent(getActivity(), MovieView.class);
                                i.putExtra("movieID", movieID);
                                i.putExtra("movie", movie);
                                i.putExtra("year", releaseyear);
                                startActivity(i);

                            }
                        });*/
                        ListAdapter adapter = new SimpleAdapter(getActivity(), movieListArray, R.layout.list_view_for_movie_search,
                                new String[]{"imdbID","Title","Year","Poster"},
                                new int[]{R.id.tv_result_movieid,R.id.tv_result_moviename,R.id.tv_result_year,R.id.iv_poster});
                        movieList.setAdapter(adapter);

                        // reference: https://stackoverflow.com/questions/30314314/image-from-url-in-listview-using-simpleadapter
                        for(int i=0;i<adapter.getCount();i++) {
                            HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(i);
                            String imgUrl = "";
                            imgUrl = (String) hm.get("Poster");

                            HashMap<String, Object> hmDownload = new HashMap<String, Object>();
                            hmDownload.put("Poster", imgUrl);
                            hmDownload.put("position", i);

                            ImageLoaderTask imageLoaderTask = new ImageLoaderTask();

                            // Starting ImageLoaderTask to download and populate image in the listview
                            imageLoaderTask.execute(hmDownload);
                        }

                        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                movieID = ((TextView) view.findViewById(R.id.tv_result_movieid)).getText().toString();
                                Intent i = new Intent(getActivity(), MovieView.class);
                                i.putExtra("movieID", movieID);
                                i.putExtra("personid", personid);
                                startActivity(i);
                            }
                        });

/*                        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                Intent i = new Intent(getActivity(), MovieView.class);
                                i.putExtra("movieID", movieID);
//                                i.putExtra("movie", movie);
//                                i.putExtra("year", releaseyear);
                                startActivity(i);
                            }
                        });*/

                        /*String strings = MovieAPI.getDetails(result);
                        String[] parts = strings.split("\n");
                        movie = parts[0];
                        releaseyear = parts[1];
                        image = parts[2];
                        moviename.setText(movie);
                        year.setText(releaseyear);
                        Picasso.get()
                                .load(image)
                                .resize(300, 300)
                                .centerInside()
                                .into(poster);
                        click.setText("Click here for more details");*/

                        /*ListAdapter adapter = new SimpleAdapter(getActivity(), movieListArray, R.layout.list_view_for_movie_search,
                                new String[]{"Movie Name","Release Year","Image"},
                                new int[]{R.id.tv_result_moviename,R.id.tv_result_year,R.id.iv_poster});
                        listView.setAdapter(adapter);*/
                        /*HashMap<String,String> map = new HashMap<String,String>();
                        // adding each child node to HashMap key => value
                        map.put("Movie Name", part1);
                        map.put("Year", part2);
                        map.put("Image", part3);
                        List<HashMap<String, String>> listArray = null;

                        listArray.add(map);*/

                        // codes below worked for list
/*                        String[] values = new String[] { part1, part2, part3 };
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.search_list_view, R.id.tv_test, values);
                        listView.setAdapter(adapter);*/




//                        ListAdapter adapter = new SimpleAdapter(getActivity(), listArray, R.layout.search_list_view,
//                                new String[]{"Movie Name","Year","Image"},
//                                new int[]{R.id.tv_result_moviename,R.id.tv_result_year,R.id.iv_poster});
//                        listView.setAdapter(adapter);

                    }
                }.execute();
            }
        });




        return view;
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
            // Problem here!
            int position = (Integer) result.get("position");

            // Getting adapter of the listview
            SimpleAdapter adapter = (SimpleAdapter ) movieList.getAdapter();

            // Getting the hashmap object at the specified position of the listview
            HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);

            // Overwriting the existing path in the adapter
            hm.put("Poster", path);

            // Noticing listview about the dataset changes
            adapter.notifyDataSetChanged();

        }
    }

}