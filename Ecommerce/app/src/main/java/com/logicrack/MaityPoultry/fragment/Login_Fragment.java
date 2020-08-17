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
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.logicrack.MaityPoultry.activity.OTP_LoginfActivity;
import com.logicrack.MaityPoultry.activity.PreCheckoutActivity;
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


public class Login_Fragment extends Fragment implements OnClickListener {
    private static View view;

    private static EditText login_mobile, etpassword;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    private static  TextView  text_otp;

    ProgressDialog progressDialog;
    LocalStorage localStorage;
    String userString,PageTypeUser;
    User user;
    private final String LOGIN_URL = "http://123api.123homepaints.com/api/KitchenRefill/CustomerLogin";
    private String username;
    private String password;

    public Login_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        PageTypeUser=((LoginRegisterActivity)getActivity()).PageType;
        initViews();
        setListeners();
        return view;
    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        login_mobile = view.findViewById(R.id.login_mobile);
        etpassword = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        text_otp=view.findViewById(R.id.text_otp);


        signUp = view.findViewById(R.id.createAccount);
        show_hide_password = view
                .findViewById(R.id.show_hide_password);
        loginLayout = view.findViewById(R.id.login_layout);
        progressDialog = new ProgressDialog(getContext());
        localStorage = new LocalStorage(getContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);
        Log.d("User", userString);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        text_otp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            etpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                            etpassword.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            etpassword.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etpassword.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();
                break;

            case R.id.text_otp:
                startActivity(new Intent(getActivity(), OTP_LoginfActivity.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        final String getMobileNo = login_mobile.getText().toString();
        final String getPassword = etpassword.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getMobileNo);

        // Check for both field is empty or not
        if (getMobileNo.equals("") || getMobileNo.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter both credentials.");
            vibrate(200);
        }
        else if(getMobileNo.length() > 10 || getMobileNo.length() < 10)
        {
            //loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Mobile No is Invalid.");
            vibrate(200);
        }
        // Check if email id is valid or not
      /*  else if (!m.find()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            vibrate(200);
            // Else do login and do your stuff
        }*/ else {

            progressDialog.setMessage("Please Wait....");
            progressDialog.show();


            userRegistration(getMobileNo,getPassword);
          /* Handler mHand = new Handler();
            mHand.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (user != null) {

                        if (!user.getEmail().equalsIgnoreCase(getEmailId) || !user.getPassword().equalsIgnoreCase(getPassword)) {
                            new CustomToast().Show_Toast(getActivity(), view,
                                    "Please Check Email or Password");
                        } else {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        }
                    } else {
                        new CustomToast().Show_Toast(getActivity(), view,
                                "Please Register whith This Email");
                    }

                    progressDialog.dismiss();

                }
            }, 5000);*/
            //userLogin(getEmailId,getPassword);
        }
    }




    private void userRegistration( final String getMobileNo,final String getPassword) {

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

                                new CustomToast().Show_Toast(getActivity(), view,
                                        "Wrong Credentials Please Try again");
                                vibrate(200);
                            }
                            else
                            {
                                user = new User(obj.getString("CustomerId"), obj.getString("FName") +" "+  obj.getString("LName"), obj.getString("Email"), obj.getString("MobNo"),obj.getString("Password"),obj.getString("Address"), obj.getString("PrimaryOrderAddress"), obj.getString("PrimaryOrderPincode"),obj.getString("Landmark"));
                                Gson gson = new Gson();
                                String userString = gson.toJson(user);
                                localStorage = new LocalStorage(getContext());
                                localStorage.createUserLoginSession(userString);


                                if(((LoginRegisterActivity)getActivity()).PageType !="PreCheckout")
                                {
                                    Handler mHand = new Handler();
                                    mHand.postDelayed(new Runnable()
                                    {

                                        @Override
                                        public void run()
                                        {   progressDialog.dismiss();
                                                startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                        }
                                    }, 5000);
                                }
                                else
                                {
                                    Handler mHand = new Handler();
                                    mHand.postDelayed(new Runnable()
                                    {

                                        @Override
                                        public void run()
                                        {   progressDialog.dismiss();
                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                            getActivity().finish();
                                            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                        }
                                    }, 5000);
                                }



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
                new CustomToast().Show_Toast(getActivity(), view,
                        "Sign Up Failed!! Try again");
                vibrate(200);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put("MobNo", getMobileNo);

                map.put("Password", getPassword);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }






    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }
}
