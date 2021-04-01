package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

// reference: https://www.tutorialspoint.com/how-to-display-a-list-of-images-and-text-in-a-listview-in-android
public class CustomAdapterForMovieSearch implements ListAdapter {
    ArrayList<Movie> arrayList;
    Context context;
    public CustomAdapterForMovieSearch(Context context, ArrayList<Movie> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    //@Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    //@Override
    public boolean isEnabled(int position) {
        return true;
    }
    //@Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    //@Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    //@Override
    public int getCount() {
        return arrayList.size();
    }
    //@Override
    public Object getItem(int position) {
        return position;
    }
    //@Override
    public long getItemId(int position) {
        return position;
    }
    //@Override
    public boolean hasStableIds() {
        return false;
    }
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_view_for_movie_search, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            TextView title = convertView.findViewById(R.id.tv_result_moviename);
            TextView year = convertView.findViewById(R.id.tv_result_year);
            ImageView imag = convertView.findViewById(R.id.iv_poster);
            title.setText(Movie.movieName);
            year.setText(Movie.releaseYear);
            Picasso.get()
                    .load(Movie.imageLink)
                    .into(imag);
            TextView click = convertView.findViewById(R.id.tv_click);
            click.setText("Click here for more details");
        }
        return convertView;
    }
    //@Override
    public int getItemViewType(int position) {
        return position;
    }
    //@Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    //@Override
    public boolean isEmpty() {
        return false;
    }
}
