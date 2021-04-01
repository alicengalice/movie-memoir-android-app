package com.example.mymoviememoir;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieAPI {
    private static final String API_KEY = "de5c8f8c";
    private static ArrayList<HashMap<String, String>> movieListArray;
    HashMap<String,String> map = new HashMap<String,String>();
    public static String search(String keyword, String year, String[] params, String[] values) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";
        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            url = new URL("http://www.omdbapi.com/?s=" + keyword + "&apikey=" + API_KEY + "&y=" + year);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
    }

    public static String searchByMovieNameAndYear(String keyword, String year, String[] params, String[] values) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";
        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            //http://www.omdbapi.com/?apikey=de5c8f8c&t=Harry%20Potter&y=2011
            url = new URL("http://www.omdbapi.com/?apikey=" + API_KEY + "&t=" + keyword + "&y=" + year);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
    }

    public static String searchByMovieId(String movieid, String[] params, String[] values) {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";
        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            url = new URL("http://www.omdbapi.com/?i=" + movieid + "&apikey=" + API_KEY);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
    }



    public static String getDetails(String result){
        String movieid = "";
        String details = "";
        String title = "";
        String year = "";
        String image = "";
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < jsonArray.length(); i++) {
                movieid = jsonArray.getJSONObject(i).getString("imdbID") + "\n";
                title = jsonArray.getJSONObject(i).getString("Title") + "\n";
                year = jsonArray.getJSONObject(i).getString("Year") + "\n";
                image = jsonArray.getJSONObject(i).getString("Poster") + "\n\n";
                details = movieid + title + year + image;

                /*HashMap<String,String> map = new HashMap<String,String>();
                // adding each child node to HashMap key => value
                map.put("MovieID", movieid);
                map.put("Movie Name", title);
                map.put("Release Year", year);
                map.put("Image", image);

                movieListArray.add(map);*/

            }


        }catch (Exception e){
            e.printStackTrace();
            details = "NO INFO FOUND";
        }
        return details;
    }

    public static String getDetailsForMovieView(String result){
        String details = "";
        String title = "";
        String year = "";
        String genre = "";
        String actors = "";
        String releasedate = "";
        String directors = "";
        String plot = "";
        String country = "";
        String rating = "";

        String image = "";
        try{
            JSONObject jsonObject = new JSONObject(result);
            //JSONArray jsonArray = jsonObject.getJSONArray("object");
            // JSONObject jsonObject = jsonArray.getJSONObject(i);
            title = jsonObject.getString("Title") + "\n";
            year = jsonObject.getString("Year") + "\n";
            image = jsonObject.getString("Poster") + "\n";
            genre = jsonObject.getString("Genre") + "\n";
            actors = jsonObject.getString("Actors") + "\n";
            releasedate = jsonObject.getString("Released") + "\n";
            country = jsonObject.getString("Country") + "\n";
            directors = jsonObject.getString("Director") + "\n";
            plot = jsonObject.getString("Plot") + "\n";
            rating = jsonObject.getString("imdbRating") + "\n";

            details = title + year + image + genre + actors + releasedate + country + directors + plot + rating;


        }catch (Exception e){
            e.printStackTrace();
            details = "NO INFO FOUND";
        }
        return details;
    }
}

