package com.logicrack.MaityPoultry.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.CustomToast;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTP_LoginfActivity extends AppCompatActivity   {
    EditText edit_phone,edit_otpnumber;
    TextView txt_resend;
    Button btn_send,btn_Confirm;

    LinearLayout lin_num,lin_otp,lin_confirm,lin_send;

    ProgressDialog progressDialog;

    String   _et_phonenumber ="";
    String   _et_otpnumber ="";
    String otpnumber="";
    String Msg="";
    private final String GetOTPNumber="http://123api.123homepaints.com/api/KitchenRefill/App_GetOTP?PhoneNumber=";
    private final String LOGIN_URL ="http://123api.123homepaints.com/api/KitchenRefill/CustomerOTPLogin";
  //  private static View view;
    User user;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__loginf);

        progressDialog = new ProgressDialog(this);
        edit_phone=findViewById(R.id.edit_phone);
        edit_otpnumber=findViewById(R.id.edit_otpnumber);
        txt_resend=findViewById(R.id.txt_resend);

        lin_num=findViewById(R.id.lin_num);
        lin_otp=findViewById(R.id.lin_otp);
        lin_confirm=findViewById(R.id.lin_confirm);
        lin_send=findViewById(R.id.lin_send);

        btn_send=findViewById(R.id.btn_send);
        btn_Confirm=findViewById(R.id.btn_Confirm);



        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                _et_phonenumber=  edit_phone.getText().toString();

                if
                (_et_phonenumber.length()  < 10) {
                    edit_phone.setError("Enter Valid Phone Number");
                    edit_phone.requestFocus();
                }
                else
                {
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    SendContactNo();
                    //CouponMaster_ProcessReferralCode(_et_Referral);
                }

            }
        });
        btn_Confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                _et_otpnumber=  edit_otpnumber.getText().toString();

                if
                (_et_otpnumber.length()  < 4) {
                    edit_otpnumber.setError("Please Enter 4 Digit  OTP Number");
                    edit_otpnumber.requestFocus();
                } else if (_et_otpnumber.equals(otpnumber.toString())) {
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    LoginViaOTP();



                 } else {

                    edit_otpnumber.setError("Please Enter Correct OTP Number!");
                    edit_otpnumber.requestFocus();
                    //CouponMaster_ProcessReferralCode(_et_Referral);
                }

            }
        });
        txt_resend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                     _et_phonenumber=  edit_phone.getText().toString();
                     progressDialog.setMessage("Please Wait....");
                     progressDialog.show();
                    SendContactNo();
                    //CouponMaster_ProcessReferralCode(_et_Referral);


            }
        });











    }

    private void LoginViaOTP() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            /*  if (!obj.getBoolean("error")){*/
                            int CustId=obj.getInt("CustomerId");
                            if(CustId==0)
                            {
                                progressDialog.dismiss();
                                ShowAleartDialog("Login Failed!! Please Try Again..");
                            }
                            else
                            {
                                user = new User(obj.getString("CustomerId"), obj.getString("FName") +" "+  obj.getString("LName"), obj.getString("Email"), obj.getString("MobNo"),obj.getString("Password"),obj.getString("Address"), obj.getString("PrimaryOrderAddress"), obj.getString("PrimaryOrderPincode"),obj.getString("Landmark"),obj.getBoolean("ReferStatus"));
                                Gson gson = new Gson();
                                String userString = gson.toJson(user);
                                localStorage = new LocalStorage(getApplicationContext());
                                localStorage.createUserLoginSession(userString);

                                    Handler mHand = new Handler();
                                    mHand.postDelayed(new Runnable()
                                    {

                                        @Override
                                        public void run()
                                        {   progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                           finish();
                                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                        }
                                    }, 5000);
                                }





                          /*  }else {
                               // loginLayout.startAnimation(shakeAnimation);

                                progressDialog.dismiss();
                                new CustomToast().Show_Toast(getActivity(), view,
                                        "Sign Up Failed!! Try again");
                                vibrate(200);
                            }*/

                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();

                ShowAleartDialog("Something is Wrong! Try Again!");
                // Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put("MobNo", _et_phonenumber);


                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    private void SendContactNo() {

        String PhoneNumber=_et_phonenumber;
        StringRequest vrequest = new StringRequest(Request.Method.GET, GetOTPNumber + PhoneNumber,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj=new JSONObject(response);
                            int CustId=obj.getInt("CustomerId");

                            if(CustId!=0){
                                otpnumber = obj.getString("OTP");
                                Msg=obj.getString("Msg");
                                lin_num.setVisibility((View.GONE));
                                lin_otp.setVisibility(View.VISIBLE);
                                lin_send.setVisibility((View.GONE));
                                lin_confirm.setVisibility(View.VISIBLE);

                                progressDialog.dismiss();
                                //   setUpSubCategoryAdapter(categories);
                                ShowAleartDialog(Msg);
                                // Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();

                            }

                            else
                                {
                                    otpnumber = obj.getString("OTP");
                                    Msg=obj.getString("Msg");
                                    progressDialog.dismiss();
                                    //   setUpSubCategoryAdapter(categories);
                                    ShowAleartDialog(Msg);
                                    // Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




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


    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }
}
