package com.example.mymoviememoir.networkconnection;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.mymoviememoir.entities.Cinema;
import com.example.mymoviememoir.entities.Credentials;
import com.example.mymoviememoir.entities.Memoir;
import com.example.mymoviememoir.entities.Person;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client = null;
    private String results;

    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    public NetworkConnection() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL =
            "http://xxx/MemoirMovie/webresources/";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String findByEmailAndPassword(String email, String password) throws NoSuchAlgorithmException {
        String passhash = getMd5(password);
        final String methodPath = "memoir.credentials/findByEmailAndPasshash/" + email + "/" + passhash;
        String results = getResults(methodPath);
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getResults(final String methodPath) {
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
            if (results.equalsIgnoreCase("[]") || results.startsWith("<")) {
                results = "fail";
             } else if (results.startsWith("[{")) {
                results = "success";
            } else {
                results = "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            results = "fail";
        }
        return results;
    }

    // credit: https://www.geeksforgeeks.org/md5-hash-in-java/
    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String addPerson(String[] details) throws ParseException {
        // DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        int personid = findMaxPersonID() + 1;
        Person person = new Person(personid, details[1], details[2], details[3],
                details[4], details[5], details[6], details[7]);
        Gson gson = new Gson();
        String personJson = gson.toJson(person);
        String strResponse = "";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json ", personJson);
        final String methodPath = "memoir.person/";
        RequestBody body = RequestBody.create(personJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String findByEmail (String email) {
        final String methodPath = "memoir.credentials/findByEmail/" + email;
        String results = getResults(methodPath);
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer findMaxPersonID () {
        int result = 0;
        final String methodPath = "memoir.person/findMaxId/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
            result = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer findMaxCreID () {
        int result = 0;
        final String methodPath = "memoir.credentials/findMaxCreId/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
            result = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String addCredentials(String[] details) throws ParseException {
        // DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        //int creid = findMaxCreID() + 1;
        //int personid = findMaxPersonID() + 1;
        //String strPersonid = String.valueOf(personid);
        //strPersonid = details[4];
        Credentials credentials = new Credentials(Integer.parseInt(details[0]), details[1], details[2], details[3]);
        credentials.setPersonid(Integer.parseInt(details[4]));

        Gson gson = new Gson();
        String credentialsJson = gson.toJson(credentials);
        String strResponse = "";
        //this is for testing, check how the json looks like in Logcat
        Log.i("json ", credentialsJson);
        final String methodPath = "memoir.credentials/";
        RequestBody body = RequestBody.create(credentialsJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer findPersonIDByEmail (String email) {
        int result = 0;
        final String methodPath = "memoir.credentials/findPersonIDbyEmail/" + email;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
            result = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String findFirstNameByPersonID (Integer personid) {
        final String methodPath = "memoir.person/findFirstNameByPersonID/" + personid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String FindRecentMoviesbyPersonID (Integer personid) {
        final String methodPath = "memoir.memoir/FindRecentMoviesbyPersonID/" + personid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String FindMaxRatingScorebyPersonID(Integer personid) {
        final String methodPath = "memoir.memoir/FindMaxRatingScorebyPersonID/" + personid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String FindByPersonIDANDYear (Integer personid, Integer year) {
        final String methodPath = "memoir.memoir/FindByPersonIDANDYear/" + personid + "/" + year;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String FindCinemaAndPostcode () {
        final String methodPath = "memoir.cinema/findCinemaNameAndPostcode";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String addCinema(String[] details) {
        Cinema cinema = new Cinema(Integer.parseInt(details[0]), details[1],details[2]);
        Gson gson = new Gson();
        String cinemaJson = gson.toJson(cinema);
        String strResponse="";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json " , cinemaJson);
        final String methodPath = "memoir.cinema/";
        RequestBody body = RequestBody.create(cinemaJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }


    public String addMemoir(String[] details) {
        Memoir memoir = new Memoir(Integer.parseInt(details[0]), details[1],details[2], details[3], details[4], Double.parseDouble(details[5]));
        memoir.setCinemaid(Integer.parseInt(details[7]));
        memoir.setPersonid(Integer.parseInt(details[6]));
        Gson gson = new Gson();
        String memoirJson = gson.toJson(memoir);
        String strResponse="";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json " , memoirJson);
        final String methodPath = "memoir.memoir/";
        RequestBody body = RequestBody.create(memoirJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer findMaxMemID () {
        int result = 0;
        final String methodPath = "memoir.memoir/findMaxMemID/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
            result = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String FindAllMoviesbyPersonID(Integer personid) {
        final String methodPath = "memoir.memoir/FindAllMoviesbyPersonID/" + personid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String FindAddressByPersonID(Integer personid) {
        final String methodPath = "memoir.person/FindAddressByPersonID/" + personid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }



}

