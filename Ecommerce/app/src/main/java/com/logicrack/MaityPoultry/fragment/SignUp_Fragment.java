package com.logicrack.MaityPoultry.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.LoginRegisterActivity;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.CustomToast;
import com.logicrack.MaityPoultry.util.Utils;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText firstName,lastName,pincode, emailId, mobileNumber,Address,edit_Landmark,
            password;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    ProgressDialog progressDialog;
    User user;
    LocalStorage localStorage;
    Gson gson;
    private final String SignUp_URL = "http://123api.123homepaints.com/api/KitchenRefill/SaveCustomer";

    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        firstName = view.findViewById(R.id.firstName);
        lastName= view.findViewById(R.id.lastName);
        pincode= view.findViewById(R.id.pincode);
        emailId = view.findViewById(R.id.userEmailId);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        edit_Landmark= view.findViewById(R.id.edit_Landmark);

        password = view.findViewById(R.id.password);

        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
        terms_conditions = view.findViewById(R.id.terms_conditions);
        progressDialog = new ProgressDialog(getContext());
        Address = view.findViewById(R.id.Address);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new LoginRegisterActivity().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {
        // Get all edittext texts
        String getAddress = Address.getText().toString();
        String getfirstName = firstName.getText().toString();
        String getlastName = lastName.getText().toString();
        String getpincode = pincode.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getPassword = password.getText().toString();
        String getLandmark =edit_Landmark.getText().toString();
        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);


        if (getfirstName.length() == 0) {
            firstName.setError("Enter Your Name");
            firstName.requestFocus();

        }
        else  if(getlastName.length()==0){
            lastName.setError("Enter Your Name");
            lastName.requestFocus();
        }
        else  if(getpincode.length()==0){
            pincode.setError("Enter Your Name");
            pincode.requestFocus();
        }

       /* else if (getEmailId.length() == 0) {
            emailId.setError("Enter Your Email");
            emailId.requestFocus();
        } else if (!m.find()) {
            emailId.setError("Enter Correct Email");
            emailId.requestFocus();
        } */

       else if (getMobileNumber.length() == 0) {
            mobileNumber.setError("Enter Your Mobile Number");
            mobileNumber.requestFocus();
        } else if (getPassword.length() == 0) {
            password.setError("Enter Password");
            password.requestFocus();
        } else if (getLandmark.length() == 0) {
                edit_Landmark.setError("Enter Landmark");
                edit_Landmark.requestFocus();
        } else if (getPassword.length() < 6) {
            password.setError("Enter 6 digit Password");
            password.requestFocus();
        } /*else if (!terms_conditions.isChecked()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Accept Term & Conditions");
        }*/
        else  if(getAddress.length()==0){
            Address.setError("Enter Your Address");
            Address.requestFocus();
        }

        else {

            progressDialog.setMessage("Registering Data....");
            progressDialog.show();



            userRegistration(getfirstName,getlastName,getEmailId,getMobileNumber,getPassword,getpincode,getAddress,getLandmark);





        }

    }


    private void userRegistration(final String getfirstName, final String getlastName,  final String getEmailId,final String getMobileNumber, final String getPassword,final String getpincode,final String getAddress,final String getLandmark) {





        StringRequest stringRequest = new StringRequest(Request.Method.POST, SignUp_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            String Msg=   obj.getString("Msg");
                            int CustId=obj.getInt("CustomerId");

                            if( CustId!=0) {

                                user = new User(obj.getString("CustomerId"), obj.getString("FName") + " " + obj.getString("LName"), obj.getString("Email"), obj.getString("MobNo"), obj.getString("Password"), getAddress, obj.getString("PrimaryOrderAddress"), obj.getString("PrimaryOrderPincode"),obj.getString("Landmark"));
                                Gson gson = new Gson();
                                String userString = gson.toJson(user);
                                localStorage = new LocalStorage(getContext());
                                localStorage.createUserLoginSession(userString);

                                Handler mHand = new Handler();
                                mHand.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                        getActivity().finish();
                                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                }, 5000);
                            }
                            else
                            {
                                progressDialog.dismiss();

                                new CustomToast().Show_Toast(getActivity(), view,
                                        "Email Id already Exist try again...");
                                vibrate(200);
                            }

                        } catch (JSONException e) {

                            progressDialog.dismiss();

                            new CustomToast().Show_Toast(getActivity(), view,
                                    "Sign Up Failed!! Try again");
                            vibrate(200);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new CustomToast().Show_Toast(getActivity(), view,
                        "Sign Up Failed!! Try again");
                vibrate(200);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("FName", getfirstName);

                map.put("LName", getlastName);
                map.put("Email", getEmailId);
                map.put("MobNo", getMobileNumber);
                map.put("Pin", getpincode);
                map.put("Password", getPassword);
                map.put("Address", getAddress);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


    public void vibrate(int duration) {
        progressDialog.dismiss();
        Vibrator vibs = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }

}
