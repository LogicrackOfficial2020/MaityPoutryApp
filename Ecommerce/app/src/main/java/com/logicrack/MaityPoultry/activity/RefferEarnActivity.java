package com.logicrack.MaityPoultry.activity;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logicrack.MaityPoultry.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RefferEarnActivity extends BaseActivity {

    com.logicrack.MaityPoultry.customfonts.MyTextView tv_MyCode;
    private static View view;
    private final String URL = "http://123api.123homepaints.com/api/KitchenRefill/Customer_GetReferralCode?CustomerId=";
    private final String ShareURL = "http://123api.123homepaints.com/api/KitchenRefill/Customer_GetShareMessage?CustomerId=";
    private final String ProcessReferralCodeURL = "http://123api.123homepaints.com/api/KitchenRefill/CouponMaster_ProcessReferralCode?ReferralCode=";
    ProgressDialog progressDialog;
    LocalStorage localStorage;
    String userString;
    String userEntry ;
    String Msg;
    User user;
    LinearLayout apv_share;
    EditText et_Referral;
    TextView btn_Reffaral;
    public String Email,Pincode,Name,ContactNo,CusId,Password,Address,PrimaryOrderAddress,Lanmark;
    public Boolean ReferStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reffer_earn);
        progressDialog = new ProgressDialog(this);
        tv_MyCode = findViewById(R.id.tv_MyCode);
        apv_share= findViewById(R.id.apv_share);
        et_Referral= findViewById(R.id.et_Referral);
        btn_Reffaral= findViewById(R.id.btn_Reffaral);

        localStorage = new LocalStorage(this);
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);
        if (user != null )
        {
             if(user.getName().length()!= 0)
             {
                 Name=user.getName();
                 Email=user.getEmail();
                 Pincode=user.getPrimaryOrderPincode();

                 ContactNo=user.getMobile();
                 Address=user.getAddress();
                 PrimaryOrderAddress=user.getPrimaryOrderAddress();

                 Lanmark=user.getLandmark();
                 Password=user.getPassword();
                 CusId=user.getId();
                 ReferStatus=user.getReferStatus();
                 et_Referral.setText("");

                 btn_Reffaral.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         String   _et_Referral ="";
                         _et_Referral=  et_Referral.getText().toString();

                         if
                         (_et_Referral.length() == 0) {
                             et_Referral.setError("Enter Refarral Code");
                             et_Referral.requestFocus();
                         }
                         else
                         {
                             CouponMaster_ProcessReferralCode(_et_Referral);
                         }


                     }
                 });

                 apv_share.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         if(userEntry==null){
                             userEntry= "My Refarral Code is" + tv_MyCode.getText() + "Download  the app From:" +" www.google.com";
                         }


                         Intent textShareIntent = new Intent(Intent.ACTION_SEND);
                         textShareIntent.putExtra(Intent.EXTRA_TEXT, userEntry);
                         textShareIntent.setType("text/plain");
                         startActivity(textShareIntent);

                     }
                 });
                 if(ReferStatus==true){
                     btn_Reffaral.setVisibility(View.GONE);
                     et_Referral.setText("Refer Code Already Used");
                     et_Referral.setEnabled(false);
                 }

                 Customer_GetReferralCode(user.getId());
                 Customer_GetShareMessage(user.getId());
             }
             else {
                 startActivity(new Intent(this, LoginRegisterActivity.class));
                 this.finish();
                 //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
             }



        }

        else
        {
            startActivity(new Intent(this, LoginRegisterActivity.class));
            this.finish();
            //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }





    }

    private void CouponMaster_ProcessReferralCode(String Referral ){


        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, ProcessReferralCodeURL + Referral+"&CustomerId="+CusId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj=new JSONObject(response);
                             Msg = obj.getString("Msg");
                             ReferStatus=true;
                            et_Referral.setText(Msg);
                            et_Referral.setEnabled(false);
                            btn_Reffaral.setVisibility(View.GONE);
                            user = new User(CusId, Name, Email, ContactNo, Password, Address, PrimaryOrderAddress,Pincode,Lanmark,ReferStatus);
                            Gson gson = new Gson();
                            String userString = gson.toJson(user);
                            localStorage = new LocalStorage(getApplication());
                            localStorage.createUserLoginSession(userString);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        progressDialog.dismiss();
                        //   setUpSubCategoryAdapter(categories);
                        ShowAleartDialog(Msg);
                       // Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();

                ShowAleartDialog("Something is Wrong! Try Again!");
                // Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(vrequest);

    }

    private void Customer_GetReferralCode(String CustomerId ){


        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, URL + CustomerId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        tv_MyCode.setText(response);



                        progressDialog.dismiss();
                        //   setUpSubCategoryAdapter(categories);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
              //  Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(vrequest);

    }

    private void ShowAleartDialog(String response)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                dialog.dismiss();
                            }
                        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void Customer_GetShareMessage(String CustomerId ){


        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, ShareURL + CustomerId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // tv_MyCode.setText(response);
                        userEntry=response;

                        progressDialog.dismiss();
                        //   setUpSubCategoryAdapter(categories);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //  Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(vrequest);

    }

    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);}
}
