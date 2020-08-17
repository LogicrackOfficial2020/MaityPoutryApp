package com.logicrack.MaityPoultry.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentTransaction;

import com.logicrack.MaityPoultry.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.logicrack.MaityPoultry.adapter.CouponPopUpAdapter;
import com.logicrack.MaityPoultry.fragment.AddressFragment;
import com.logicrack.MaityPoultry.fragment.ConfirmFragment;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PreCheckoutActivity extends BaseActivity implements CouponPopUpAdapter.AdapterCallback {


    private final String LOGIN_URL = "http://123api.123homepaints.com/api/KitchenRefill/CustomerLogin";
    private String username;
    private String password;
    public int CustomerId;
    User user;

    public String TelephoneNo ;
    public String HouseDetails ;
    public String StreetDetails ;
    public String Area ;
    public String ResidentialComplex ;
    public String Landmark ;
    public String City ;
    public String Pin ;
    public String Password ;
    public  String OrderShippingFname;
    public  String OrderShippingEmailId;
    public  String OrderShippingAddress;
    public  String OrderShippingContactNo;

    @Override
    public void onMethodCallback(String couponcode,String couponamount) {

        //ApplyCouponCode(yourValue);
        // get your value here


        ConfirmFragment  fragment = (ConfirmFragment) getSupportFragmentManager().findFragmentByTag("ConfirmFragment");
        if(fragment != null){
            // ok, we got the fragment instance, but should we manipulate its view?

            fragment.SetCouponCode(couponcode,couponamount);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        //upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();
      String  userString = localStorage.getUserLogin();
        User user = gson.fromJson(userString, User.class);


       // userData(user.getEmail(),user.getPassword());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
       ft.replace(R.id.content_frame, new AddressFragment(),"AddressFragment");
      //ft.replace(R.id.content_frame, new ConfirmFragment(),"ConfirmFragment");
        ft.commit();


    }


    private void changeActionBarTitle(ActionBar actionBar) {
        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        TextView tv = new TextView(getApplicationContext());
        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        //tv.setTypeface(null, Typeface.BOLD);
        // Set text to display in TextView
        tv.setText("Checkout"); // ActionBar title text
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Merienda-Bold.ttf");
        tv.setTypeface(tf);
        tv.setTextSize(20);

        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(PreCheckoutActivity.this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void userData( final String getEmailId,final String getPassword) {





        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            /*  if (!obj.getBoolean("error")){*/

                           // user = new User(obj.getString("CustomerId"), obj.getString("FName") + obj.getString("LName"), obj.getString("Email"), obj.getString("MobNo"),obj.getString("Password"));

                            CustomerId = (obj.getInt("CustomerId"));
                            HouseDetails = (obj.getString("HouseDetails"));
                            StreetDetails= (obj.getString("StreetDetails"));
                            Area= (obj.getString("Area"));
                            ResidentialComplex= (obj.getString("ResidentialComplex"));
                            Landmark= (obj.getString("Landmark"));
                            City= (obj.getString("Landmark"));
                            Pin= (obj.getString("Pin"));




                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            /*    new CustomToast().Show_Toast(getApplicationContext(),this ,

                        "Sign Up Failed!! Try again");
                */
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put("Email", getEmailId);

                map.put("Password", getPassword);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
