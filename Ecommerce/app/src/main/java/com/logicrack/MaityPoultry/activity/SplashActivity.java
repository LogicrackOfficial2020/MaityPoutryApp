package com.logicrack.MaityPoultry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity implements LocationListener {
    private  static  int SPLASH_SCREEN =5500;

    ImageView imageView;
    TextView textView1, textView2;
    Animation top, bottom;
    String LocalPincode;
    LocationManager locationManager;
    boolean isPermission;
    LocalStorage localStorage;
    User user;


 /*   @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    isPermission=true;
                   Toast.makeText(SplashActivity.this,
                    "Address Location Permission Granted",
                    Toast.LENGTH_SHORT)
                    .show();

        }

        else {
            isPermission=false;
            Toast.makeText(SplashActivity.this,
                    "Address Location Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        imageView.setAnimation(top);
        textView1.setAnimation(bottom);
        textView2.setAnimation(bottom);
        LocalPincode="";

        localStorage = new LocalStorage(getApplicationContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);

    /* if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);


        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     //   locationEnabled();
      //  getLocation();*/
        OpenScreen();

    }

    private void  CloseApp(){
        finishAffinity();
    }
    private void GivePermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            isPermission=true;
            Toast.makeText(SplashActivity.this,
                    "Address Location Permission Granted",
                    Toast.LENGTH_SHORT)
                    .show();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationEnabled();
            getLocation();
            OpenScreen();
        }

        else {
            isPermission=false;
            Toast.makeText(SplashActivity.this,
                    "Address Location Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private  void OpenScreen(){

        if (user != null){
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    //getLocation();
                }
            },SPLASH_SCREEN);
        }
        else{
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, GetLocationActivity.class);
                    startActivity(intent);
                    finish();
                    //getLocation();
                }
            },SPLASH_SCREEN);
        }


    }


    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(SplashActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500,  5, (LocationListener) this);
            /*OpenScreen();*/
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String  TakeLocalPincode=addresses.get(0).getPostalCode();
            LocalPincode=TakeLocalPincode;
          /*  OpenScreen();*/
            //    tvCity.setText(addresses.get(0).getLocality());
            //   tvState.setText(addresses.get(0).getAdminArea());
            // tvCountry.setText(addresses.get(0).getCountryName());
            //tvPin.setText(addresses.get(0).getPostalCode());
            //  tvLocality.setText(addresses.get(0).getAddressLine(0));
            if(LocalPincode != "") {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("LocalPincode", LocalPincode);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
