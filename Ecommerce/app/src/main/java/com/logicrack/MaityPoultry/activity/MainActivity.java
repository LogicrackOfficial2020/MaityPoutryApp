package com.logicrack.MaityPoultry.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.fragment.HomeFragment2;
import com.logicrack.MaityPoultry.fragment.ProductFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import com.logicrack.MaityPoultry.fragment.MyOrderFragment;
import com.logicrack.MaityPoultry.fragment.ProfileFragment;
import com.logicrack.MaityPoultry.helper.Converter;
import com.logicrack.MaityPoultry.model.Cart;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import org.apache.http.params.CoreConnectionPNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener {

    private  static  int SPLASH_SCREEN =5500;
    String LocalPincode;
    LocationManager locationManager;
    boolean isPermission;
    private static int cart_count = 0;
    Fragment fragment = null;
    User user;
    public  int AdapterItemClickId =0;
    public  String SearchProduct="";
    public  String activeFragment="Home";
    public String Email,Name,ContactNo,CusId,Password,Address,PrimaryOrderAddress,Lanmark;
    public String Pincode="";
   public List<Cart> cartList = new ArrayList<>();
    private String mState = "SHOW_MENU";
    public Boolean ReferStatus;
    String CustomerId;
   public String BannerPinCode="";
    @SuppressLint("ResourceAsColor")
    static void centerToolbarTitle(@NonNull final Toolbar toolbar) {
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.CENTER);
            titleView.setTextColor(Color.parseColor("#ff009688"));
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            toolbar.requestLayout();
            //also you can use titleView for changing font: titleView.setTypeface(Typeface);
        }
    }
    ProgressDialog progressDialog;



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(activeFragment == "Home")
            {
                finishAffinity();
            }
            else
            {
                Fragment fragment = new HomeFragment2();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

            }



          //  super.onBackPressed();
        }
    }



    /*public void toggleCommunicationGroup(View button) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuItem group = navigationView.getMenu().findItem(R.id.nav_communication_group);
        boolean isVisible = group.isVisible();
        group.setVisible(!isVisible);
        Button toggleButton = (Button)findViewById(R.id.main_toggle_button);
        if (isVisible) {
            toggleButton.setText("Enable communication group");
        } else {
            toggleButton.setText("Disable communication group");
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.ic_shopping_basket));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_action:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        progressDialog = new ProgressDialog(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        centerToolbarTitle(toolbar);
        cart_count = cartCount();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(getApplicationContext(), ChatBootActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });




        localStorage = new LocalStorage(getApplicationContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        TextView nav_user = hView.findViewById(R.id.nav_header_name);
        LinearLayout nav_footer = findViewById(R.id.footer_text);
        if (user != null) {
            nav_user.setText(user.getName());

            Name=user.getName();
            Email=user.getEmail();
            Pincode=user.getPrimaryOrderPincode();
            BannerPinCode=Pincode;
            ContactNo=user.getMobile();
            Address=user.getAddress();
            PrimaryOrderAddress=user.getPrimaryOrderAddress();

            Lanmark=user.getLandmark();
            Password=user.getPassword();
            CusId=user.getId();
            CustomerId=user.getId();
            ReferStatus=user.getReferStatus();
            displaySelectedScreen(R.id.nav_home);
        }
            else
            {
                progressDialog.setMessage("Location Detecting Please Wait....");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                //Intent data= getIntent();
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
                locationEnabled();
                getLocation();


        }
        nav_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localStorage.logoutUser();
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                // Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
            }
        });


    }


public void OptionMenu(){
    invalidateOptionsMenu();

}

    //Location Service


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
            new AlertDialog.Builder(MainActivity.this)
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100,  1, (LocationListener) this);
            /*OpenScreen();*/
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String  TakeLocalPincode=addresses.get(0).getPostalCode();
            LocalPincode=TakeLocalPincode;

            /*  OpenScreen();*/
            //    tvCity.setText(addresses.get(0).getLocality());
            //   tvState.setText(addresses.get(0).getAdminArea());
            // tvCountry.setText(addresses.get(0).getCountryName());
            //tvPin.setText(addresses.get(0).getPostalCode());
            //  tvLocality.setText(addresses.get(0).getAddressLine(0));
            if(LocalPincode != "") {
            Pincode =  LocalPincode;
                BannerPinCode=Pincode;
                progressDialog.dismiss();
                user = new User(CusId, Name, Email, ContactNo, Password, Address, "",Pincode,"",ReferStatus);
                Gson gson = new Gson();
                String userString = gson.toJson(user);
                localStorage = new LocalStorage(getApplicationContext());
                localStorage.createUserLoginSession(userString);
                displaySelectedScreen(R.id.nav_home);
            }

        } catch (Exception e) {
        }
        finally {
            progressDialog.dismiss();
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



    // End Region





    private void displaySelectedScreen(int itemId) {

        //creating fragment object


        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                activeFragment = "Home";
                fragment = new HomeFragment2();
                break;
            case R.id.nav_profile:
                activeFragment = "Profile";
                fragment = new ProfileFragment();
                break;
           case R.id.nav_chat:
               startActivity(new Intent(getApplicationContext(), ChatBootActivity.class));
               overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
         /*   case R.id.nav_popular_products:
                fragment = new PopularProductFragment();
                break;
            case R.id.nav_new_product:
                fragment = new NewProductFragment();
                break;*/

         /*   case R.id.nav_offers:
                fragment = new OffrersFragment();
                break;*/
         /*   case R.id.nav_search:
                //fragment = new CategoryFragment();
                break;*/
            case R.id.nav_my_order:
                activeFragment = "Order";
                fragment = new MyOrderFragment();
                break;
            case R.id.nav_my_cart:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    @Override
    public void onAddProduct() {
        super.onAddProduct();
        cart_count++;
        invalidateOptionsMenu();

    }

    @Override
    public void onRemoveProduct() {
        super.onRemoveProduct();
    }


    public void onAdapterCalled(int id, String tag,String SearchProductName,String pin) {

        //creating fragment object
       // Fragment fragment = null;
        AdapterItemClickId = id;
        SearchProduct=SearchProductName;
        if(!pin.equals("")){
            Pincode=pin;
        }
        else{
            Pincode=BannerPinCode;
        }

        //initializing the fragment object which is selected
        switch (tag) {
            case "subcategory":
                activeFragment = "Product";
                fragment = new ProductFragment();
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        }
}
