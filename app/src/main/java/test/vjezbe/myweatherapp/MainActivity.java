package test.vjezbe.myweatherapp;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;

import test.vjezbe.myweatherapp.common.MyConsts;
import test.vjezbe.myweatherapp.fragmenti.WeatherFragment;
import test.vjezbe.myweatherapp.reciver.NetworkChangeReciver;

public class MainActivity extends AppCompatActivity {



    private SearchView  svCity;


    @Override
    protected void onResume() {
        super.onResume();
        setupBroadcastReciver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


        svCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callFragment(svCity.getQuery().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }



    private void initComponents(){
        svCity = findViewById(R.id.svCity);

    }

    private void callFragment(String city){
        Bundle bundle = new Bundle();
        bundle.putString(MyConsts.CITY_KEY,city);

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(bundle);

        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();

        trans.replace(R.id.flFragment,fragment,"WeatherFrag");
        trans.commit();
    }

    private void setupBroadcastReciver(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(new NetworkChangeReciver(),filter);
    }

}
