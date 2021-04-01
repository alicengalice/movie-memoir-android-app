package com.example.mymoviememoir.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mymoviememoir.MyMarkerView;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reports extends Fragment implements OnChartGestureListener {

    @NonNull
    public static Fragment newInstance() {
        return new Reports();
    }

    private BarChart chart;

    private final int count = 12;

    private ExecutorService executor
            = Executors.newSingleThreadExecutor();

    NetworkConnection networkConnection = null;
    ArrayList<String> movieListBarG = null;
    ArrayList<String> month = null;

    ArrayList<String> xVals = new ArrayList<String>();

    Button btnYear, btnDate;

    Spinner spinner;

    TextView tv_year, tv_suburb;

    String selectedYear;

    String strpersonid;

    FrameLayout barChartLayout;

    DatePicker startDatePicker, endDatePicker;



    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report_layout, container, false);
        barChartLayout = v.findViewById(R.id.barChartLayout);

        networkConnection = new NetworkConnection();
        movieListBarG = new ArrayList<>();
        month = new ArrayList<>();
        btnYear = v.findViewById(R.id.btn_yearBarChart);
        spinner = v.findViewById(R.id.spinner_year);
        tv_year = v.findViewById(R.id.tv_selectedyear);

        btnDate = v.findViewById(R.id.btn_datePieChart);
        tv_suburb = v.findViewById(R.id.tv_suburb);

        xVals = new ArrayList<>();

        // create a new chart object
        chart = new BarChart(getActivity());
        chart.getDescription().setEnabled(false);
        chart.setOnChartGestureListener(this);

        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv);

        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        // Generate data for Bar Chart
        SharedPreferences sharedPref = getActivity().
                getSharedPreferences("personid", Context.MODE_PRIVATE);
        strpersonid = sharedPref.getString("personid", null);

        btnYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedYear = spinner.getSelectedItem().toString();
                tv_year.setText("Bar Graph for " + selectedYear);

                GenerateDataBarChart generateDataBarChart = new GenerateDataBarChart();
                try {
                    generateDataBarChart.execute(strpersonid, selectedYear).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //BarData barData = generateBarData(1, 20000, movieListBarG.size());

                BarDataSet barDataSet1 = new BarDataSet(dataValues1(), "Number of Movies Watched per month");
                BarData barData = new BarData();
                barData.addDataSet(barDataSet1);

                XAxis xAxis = chart.getXAxis();
                xAxis.setGranularity(1f);
                //xAxis.setCenterAxisLabels(true);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



                chart.setData(barData);
                chart.invalidate();

                Legend l = chart.getLegend();

                /*YAxis leftAxis = chart.getAxisLeft();
                leftAxis.setAxisMinimum(0f); */// this replaces setStartAtZero(true)

                //chart.getAxisRight().setEnabled(false);

                /*XAxis xAxis = chart.getXAxis();
                xAxis.setEnabled(true);*/

                barChartLayout.addView(chart, 1000, 700);

                /*Future<BarData> future = new Reports().generateBarData(1, 20000, movieListBarG.size());

                while(!future.isDone()) {
                    System.out.println("Calculating...");
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                BarData result = new BarData();
                try {
                    result = future.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                chart.setData(result);*/
            }
        });

        startDatePicker = v.findViewById(R.id.dp_startdate);
        endDatePicker = v.findViewById(R.id.dp_enddate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = new GregorianCalendar(startDatePicker.getYear(),
                        startDatePicker.getMonth(),
                        startDatePicker.getDayOfMonth());

                Integer month = startDatePicker.getMonth() + 1;
                Integer day = startDatePicker.getDayOfMonth();
                String startDate = startDatePicker.getYear() + "-" + ((month.toString().length() == 1 ? "0" + month.toString() : month.toString()))
                        + "-" + ((day.toString().length() == 1 ? "0" + day.toString() : day.toString())) + "T" + "00:00+10:00";

                Calendar calendar2 = new GregorianCalendar(endDatePicker.getYear(),
                        endDatePicker.getMonth(),
                        endDatePicker.getDayOfMonth());

                Integer month2 = endDatePicker.getMonth() + 1;
                Integer day2 = endDatePicker.getDayOfMonth();
                String endDate = endDatePicker.getYear() + "-" + ((month2.toString().length() == 1 ? "0" + month2.toString() : month2.toString()))
                        + "-" + ((day2.toString().length() == 1 ? "0" + day2.toString() : day2.toString())) + "T" + "00:00+10:00";



            }
        });



        // chart.setData(generateBarDataTest(1, 20000, 12));

        /*Legend l = chart.getLegend();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);

        FrameLayout parent = v.findViewById(R.id.barChartLayout);
        parent.addView(chart, 1000, 700);*/

        /*tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // programmatically add the chart
                FrameLayout parent = getView().findViewById(R.id.barChartLayout);
                parent.addView(chart, 700, 700);
            }
        });
*/
        /*FrameLayout parent = v.findViewById(R.id.barChartLayout);
        parent.addView(chart);*/


        /*// Generate data for Bar Chart
        SharedPreferences sharedPref = getActivity().
                getSharedPreferences("personid", Context.MODE_PRIVATE);
        final String strpersonid = sharedPref.getString("personid", null);

        btnYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedYear = spinner.getSelectedItem().toString();
                tv_year.setText("Click here to generate Bar Graph for " + selectedYear);

                GenerateDataBarChart generateDataBarChart = new GenerateDataBarChart();
                generateDataBarChart.execute(strpersonid, selectedYear);
            }
        });

        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // programmatically add the chart
                chart.setData(generateBarData(1, 20000, movieListBarG.size()));
                FrameLayout frameLayout = v.findViewById(R.id.barChartLayout);
                frameLayout.addView(chart, 1000, 700);
            }
        });*/


        return v;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END");
        chart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart long pressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart fling. VelocityX: " + velocityX + ", VelocityY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    private ArrayList<String> setXvalues() {

        for (int a = 0; a < movieListBarG.size(); a++) {
            String result = movieListBarG.get(a).toString();
            String[] parts = result.split("&");
            String month = parts[0];
            String noOfMovies = parts[1];

            String monthName = getMonth(Integer.valueOf(month));

            xVals.add(monthName);
        }

        return xVals;
    }


    private ArrayList<BarEntry> dataValues1() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int a = 0; a < movieListBarG.size(); a++) {
            String result = movieListBarG.get(a).toString();
            String[] parts = result.split("&");
            String month = parts[0];
            String noOfMovies = parts[1];

            entries.add(new BarEntry(a, (float) Integer.valueOf(noOfMovies)));

            // Add month names
            String monthName = getMonth(Integer.valueOf(month));
            xVals.add(monthName);
        }

        return entries;
    }

    protected BarData generateBarData(int dataSets, float range, int count) {
        //int personid = Integer.valueOf(strpersonid);

        ArrayList<IBarDataSet> sets = new ArrayList<>();

        BarData d = new BarData();
        for(int i = 0; i < dataSets; i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int a = 0; a < movieListBarG.size(); a++) {
                String result = movieListBarG.get(i).toString();
                String[] parts = result.split("&");
                String month = parts[0];
                String noOfMovies = parts[1];

                entries.add(new BarEntry(a, (float) Integer.valueOf(noOfMovies)));
                xVals.add(month);
            }

            BarDataSet ds = new BarDataSet(entries, "Number of Movies watched per month");
            ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
            sets.add(ds);
            d.addDataSet(ds);
        }

        /*final BarData d = new BarData();
        d.addDataSet(ds);*/
        return d;
    }


    private abstract class OnFinishedListener {
        abstract void onFinished(String rslt);
    }

    private class GenerateDataBarChart extends AsyncTask<String, Void, ArrayList<String>> {

        ProgressDialog progressDialog;
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String result = "";
            String personid = strings[0];
            String year = strings[1];
            try {
                result = networkConnection.FindByPersonIDANDYear(Integer.valueOf(personid), Integer.valueOf(year));
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                //[{"Month":7,"NumberOfWatchedMovies":3},{"Month":8,"NumberOfWatchedMovies":1},
                // {"Month":9,"NumberOfWatchedMovies":1},{"Month":10,"NumberOfWatchedMovies":1}]
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = null;
                    try {
                        json = jsonArray.getJSONObject(i);

                        String month = json.getString("Month");
                        String noofmovieswatched = json.getString("NumberOfWatchedMovies");

                        movieListBarG.add(month + "&" + noofmovieswatched);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieListBarG;
        }

       /* @Override
        protected void onPostExecute(ArrayList<String> result) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                //[{"Month":7,"NumberOfWatchedMovies":3},{"Month":8,"NumberOfWatchedMovies":1},
                // {"Month":9,"NumberOfWatchedMovies":1},{"Month":10,"NumberOfWatchedMovies":1}]
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = null;
                    try {
                        json = jsonArray.getJSONObject(i);

                        String month = json.getString("Month");
                        String noofmovieswatched = json.getString("NumberOfWatchedMovies");

                        movieListBarG.add(month + "&" + noofmovieswatched);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }*/
    }

    protected BarData generateBarDataTest(int dataSets, float range, int count) {

        ArrayList<IBarDataSet> sets = new ArrayList<>();

        for(int i = 0; i < dataSets; i++) {

            ArrayList<BarEntry> entries = new ArrayList<>();

            for(int j = 0; j < count; j++) {
                entries.add(new BarEntry(j, (float) (Math.random() * range) + range / 4));
            }

            BarDataSet ds = new BarDataSet(entries, "Test Lable");
            ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
            sets.add(ds);
        }

        BarData d = new BarData(sets);
        return d;
    }
}