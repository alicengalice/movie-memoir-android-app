package com.example.mymoviememoir.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.GeoLocation;
import com.example.mymoviememoir.MapsActivity;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Maps extends Fragment {

    String personid = "";

    String address = "";

    String pAddress = "";

    String cAddress = "";

    LatLng p1 = null;

    ArrayList<String> cinemaList = null;
    ArrayList<String> cinemaListLatlng = null;

    NetworkConnection networkConnection = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.map_layout, container, false);
        Button mapButton = view.findViewById(R.id.btnMap);

        networkConnection = new NetworkConnection();

        cinemaList = new ArrayList<>();

        cinemaListLatlng = new ArrayList<>();

        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Receive personid
                SharedPreferences sharedPref = getActivity().
                        getSharedPreferences("personid", Context.MODE_PRIVATE);
                personid = sharedPref.getString("personid", null);

                // Find user address
                FindAddress findAddress = new FindAddress();
                findAddress.execute(personid);



               /* GeoLocation geoLocation = new GeoLocation();
                geoLocation.getAddress(address, getActivity(), new GeoHandler());*/

                // Find Cinema postcodes
                FindCinemaAddress findCinemaAddress = new FindCinemaAddress();
                findCinemaAddress.execute();

            }
        });
        return view;
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage (Message msg) {
            String address;
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    break;
                default:
                    address = null;
            }
            pAddress = address;
        }

    }

    private class FindAddress extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            try {
                address = networkConnection.FindAddressByPersonID(Integer.valueOf(personid));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return address;
        }

        @Override
        protected void onPostExecute(String address) {
            getLocationFromAddress(getActivity(), address);
        }

        /*@Override
        protected void onPostExecute(String address) {
            getLocationFromAddress(getActivity().getApplicationContext(), address);
        }*/
    }

    private class GetCinemaAddressLatlng extends AsyncTask<String, Void, ArrayList<String>> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                for (int i = 0; i < cinemaList.size(); i++) {
                    String cinema = cinemaList.get(i).toString();
                    String[] parts = cinema.split("&");
                    String cinemaid = parts[0];
                    String cinemaname = parts[1];
                    String postcode = parts[2];

                    getCinemaLatLng(getActivity(), postcode);
                    cinemaListLatlng.add(cinemaid + "&" + cinemaname + "&" + cAddress);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return cinemaListLatlng;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("pAddress", pAddress);
            intent.putExtra("cinemaListLatlng", result);
            Log.i(" pAddress ",pAddress);
            startActivity(intent);
        }
    }

    private class FindCinemaAddress extends AsyncTask<String, Void, String> {

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

                        cinemaList.add(cinemaId + "&" + cinemaName + "&" + postcode);
                    }

                    for (int i = 0; i < cinemaList.size(); i++) {
                        String cinema = cinemaList.get(i).toString();
                        String[] parts = cinema.split("&");
                        String cinemaid = parts[0];
                        String cinemaname = parts[1];
                        String postcode = parts[2];

                        getCinemaLatLng(getActivity().getApplicationContext(), postcode);
                        cinemaListLatlng.add(cinemaid + "&" + cinemaname + "&" + cAddress);
                    }

                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("pAddress", pAddress);
                    intent.putExtra("cinemaListLatlng", cinemaListLatlng);
                    Log.i(" pAddress ",pAddress);
                    startActivity(intent);

                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //reference: https://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address/27834110#27834110
    public String getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            try {
                Address location = address.get(0);
                double latitude = location.getLatitude();
                double longtitude = location.getLongitude();
                pAddress = latitude + "," + longtitude;
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return pAddress;
    }

    public String getCinemaLatLng(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);

        try {
            List<Address> addresses = coder.getFromLocationName(strAddress, 1);
            if (addresses == null) {
                return null;
            }
            try {
                Address address = addresses.get(0);
                // Use the address as needed
                cAddress = address.getLatitude() + "," + address.getLongitude();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cAddress;
    }

    public ArrayList<String> getCinemaLocationFromSuburb(Context context) throws IndexOutOfBoundsException {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        for (int i = 0; i < cinemaList.size(); i++) {
            String cinema = cinemaList.get(i).toString();
            String[] parts = cinema.split("&");
            String cinemaid = parts[0];
            String cinemaname = parts[1];
            String postcode = parts[2];

            try {
                // May throw an IOException
                address = coder.getFromLocationName(postcode, 5);
                if (address == null) {
                    return null;
                }

                Address location = address.get(i);
                double latitude = location.getLatitude();
                double longtitude = location.getLongitude();
                String newCinemaAddress = cinemaid + "&" + cinemaname + "&" + latitude + "," + longtitude;
                cinemaListLatlng.add(newCinemaAddress);

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }

        return cinemaListLatlng;
    }

}
