package com.logicrack.MaityPoultry.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetLocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
   // private TextView latitudeTextView,longitudeTextView;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    public String Email,Name,ContactNo,CusId,Password,Address,PrimaryOrderAddress,Lanmark;
    public String Pincode="";
    public Boolean ReferStatus;

    LocationManager locationManager;
    ProgressDialog progressDialog;
    LocalStorage localStorage;
    User user;
    public Button btncancel , btnsubmit;
    public EditText enter_pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_get_location);
       // latitudeTextView=(TextView)findViewById(R.id.latitudeTextView);
       // longitudeTextView=(TextView)findViewById(R.id.longitudeTextView);
        progressDialog = new ProgressDialog(this);
        Name="";
        Email="";
        // Pincode=data.getStringExtra("LocalPincode");
        // Pincode="";
        ContactNo="";
        Address="";
        PrimaryOrderAddress="";

        Lanmark="";
        Password="";
        CusId="";
        ReferStatus=false;


        setUpGClient();
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {

            Double latitude=mylocation.getLatitude();
            Double longitude=mylocation.getLongitude();
            //latitudeTextView.setText("Latitude : "+latitude);
            //longitudeTextView.setText("Longitude : "+longitude);
            //Or Do whatever you want with your location

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<android.location.Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses.size() > 0) {
                String TakeLocalPincode = addresses.get(0).getPostalCode();
                // LocalPincode=TakeLocalPincode;

                if (TakeLocalPincode != "" || TakeLocalPincode != null) {
                    Pincode = TakeLocalPincode;
                    progressDialog.dismiss();
                    user = new User(CusId, Name, Email, ContactNo, Password, Address, "", Pincode, "", ReferStatus);
                    Gson gson = new Gson();
                    String userString = gson.toJson(user);
                    localStorage = new LocalStorage(getApplicationContext());
                    localStorage.createUserLoginSession(userString);
                    Intent intent = new Intent(GetLocationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                showCustomDialogForPincode();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here

        showCustomDialogForPincode();
    }







    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(GetLocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(6000);
                    locationRequest.setFastestInterval(6000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(GetLocationActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(GetLocationActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        //finish();
                        showCustomDialogForPincode();
                        break;
                }
                break;
        }
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(GetLocationActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{

            progressDialog.setMessage("Location Detecting Please Wait....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(GetLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

            progressDialog.setMessage("Location Detecting Please Wait....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            getMyLocation();
        }
        else {
            showCustomDialogForPincode();
        }




    }


    private void showCustomDialogForPincode() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Include dialog.xml file
        dialog.setContentView(R.layout.fragment_delivery_pincode);
        btnsubmit = dialog.findViewById(R.id.btnsubmit);
        btncancel = dialog.findViewById(R.id.btncancel);

        enter_pincode =dialog.findViewById(R.id.enter_pincode);
        dialog.setCanceledOnTouchOutside(false);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DeliveryPincode=enter_pincode.getText().toString();
                if(DeliveryPincode.equals("")|| DeliveryPincode.length()==0){
                    enter_pincode.setError("Enter Your Delivery Pincode.... ");
                    enter_pincode.requestFocus();
                }
                else  if(DeliveryPincode.length() < 6|| DeliveryPincode.length() > 6){
                    enter_pincode.setError("Enter Valid Pincode ");
                    enter_pincode.requestFocus();
                }
                else {
                    Pincode=DeliveryPincode;
                    user = new User(CusId, Name, Email, ContactNo, Password, Address, "",Pincode,"",ReferStatus);
                    Gson gson = new Gson();
                    String userString = gson.toJson(user);
                    localStorage = new LocalStorage(getApplicationContext());
                    localStorage.createUserLoginSession(userString);
                    Intent intent = new Intent(GetLocationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                   // ShowAleartDialog("Pincode Save Successful");

                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Set dialog title
        dialog.show();
    }

    private void ShowAleartDialog(String response)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Message");
        builder.setMessage(response);
        // add the buttons
        // builder.setPositiveButton("Ok", null);
        builder
                .setPositiveButton(
                        "Ok",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                Intent intent = new Intent(GetLocationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}