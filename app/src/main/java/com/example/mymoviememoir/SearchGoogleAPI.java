package com.example.mymoviememoir;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SearchGoogleAPI {
    private static final String API_KEY = "xxx";
    private static final String SEARCH_ID_cx = "xxx";
    public static String search(String keyword, String[] params, String[] values) {
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
            url = new URL("https://www.googleapis.com/customsearch/v1?key="+ API_KEY+ "&cx="+
                    SEARCH_ID_cx + "&q="+ keyword + query_parameter);
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
        String details = "";
        String title = "";
        String year = "";
        String image = "";
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            //JSONArray jsonArray1 = jsonObject.getJSONArray("metatags");

            // reference: https://www.youtube.com/watch?v=X-n_JKvRnWo
            //JSONObject jsonObject_responseData = jsonObject.getJSONObject("responseData");
            //JSONArray jsonArray_result = jsonObject_responseData.getJSONArray("results");
            //snippet += "Number of results returned = " + jsonArray_result.length() + "\n\n";

            for( int i = 0; i < jsonArray.length(); i++) {
                title = "Title: " + jsonArray.getJSONObject(i).getString("title").replace("- IMDb", "") + "\n";
                year = "Release year: " + title.replaceAll("[^0-9]", "") + "\n";
                //image = "Image link: " + jsonObject.getJSONArray("items").getJSONObject(0).getJSONArray("pagemap").getJSONObject(1).getJSONArray("cse_thumbnail").getJSONObject(2).getJSONArray("metatags").getJSONObject(3).getString("og:image");




                //getJSONObject(0).getString("og:image") + "\n\n";
                details = title + year;

                /*JSONObject jsonObject_i = jsonArray.getJSONObject(i);
                snippet += "title: " + jsonObject_i.getString("title") + "\n";
                snippet += "content: " + jsonObject_i.getString("og:description") + "\n";
                snippet += "url: " + jsonObject_i.getString("og:url") + "\n\n";*/
            }
/*


            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > 0) {
                snippet =jsonArray.getJSONObject(0).getString("snippet");
            }*/
        }catch (Exception e){
            e.printStackTrace();
            details = "NO INFO FOUND";
        }
        return details;
    }
}

