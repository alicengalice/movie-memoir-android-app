package com.example.mymoviememoir;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mymoviememoir.fragment.HomeFragment;
import com.example.mymoviememoir.fragment.Maps;
import com.example.mymoviememoir.fragment.MovieMemoir;
import com.example.mymoviememoir.fragment.MovieSearch;
import com.example.mymoviememoir.fragment.Reports;
import com.example.mymoviememoir.fragment.Watchlist;
import com.google.android.material.navigation.NavigationView;


public class DashboardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.movieSearch:
                replaceFragment(new MovieSearch());
                break;
            case R.id.movieMemoir:
                replaceFragment(new MovieMemoir());
                break;
            case R.id.watchList:
                replaceFragment(new Watchlist());
                break;
            case R.id.reports:
                replaceFragment(new Reports());
                break;
            case R.id.maps:
                replaceFragment(new Maps());
                break;
        }
        //this code closes the drawer after you selected an item from the menu,
        //otherwise stay open
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        //adding the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //these two lines of code show the navicon drawer icon top left hand side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


}
