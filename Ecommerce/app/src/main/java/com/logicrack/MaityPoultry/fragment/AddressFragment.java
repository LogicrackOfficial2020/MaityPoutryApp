package com.logicrack.MaityPoultry.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logicrack.MaityPoultry.activity.LoginRegisterActivity;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.activity.PreCheckoutActivity;
import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.ProductActivity;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.model.UserAddress;
import com.logicrack.MaityPoultry.util.Utils;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressFragment extends Fragment {

    Context context;
    TextView txt_pyment;
    Spinner citySpinner, stateSpinner;
    ArrayList<String> stringArrayState;
    ArrayList<String> stringArrayCity;
    String spinnerStateValue, _city, _name, _email, _mobile, _address, _state, _zip, userString,Password,ActualAdress,_landmark;
    EditText name, email, mobile, address, state, zip,sa_houserno,sa_Street,sa_Area,sa_Residential,sa_Landmark;
    UserAddress userAddress;
    LocalStorage localStorage;
    Gson gson;
    User user;
    String  Street,Area,Residential,Landmark,houserno;
    ProgressDialog progressDialog;
    String CustomerId;
    View v;
    private final String Update_Address_Pin_Url = "http://123api.123homepaints.com/api/KitchenRefill/App_UpdatePriAddress_Pin_ByCustId?CustomerId=";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_address, container, false);
        citySpinner = v.findViewById(R.id.citySpinner);
        stateSpinner = v.findViewById(R.id.stateSpinner);
        name = v.findViewById(R.id.sa_name);
        email = v.findViewById(R.id.sa_email);
        mobile = v.findViewById(R.id.sa_mobile);
        address = v.findViewById(R.id.sa_address);
        zip = v.findViewById(R.id.sa_zip);
        sa_houserno= v.findViewById(R.id.sa_houserno);
        sa_Street= v.findViewById(R.id.sa_Street);
        sa_Area= v.findViewById(R.id.sa_Area);
        sa_Residential= v.findViewById(R.id.sa_Residential);
        sa_Landmark= v.findViewById(R.id.sa_Landmark);
        progressDialog = new ProgressDialog(getContext());


        stateSpinner.setEnabled(false);

        localStorage = new LocalStorage(getContext());
        gson = new Gson();
        userString = localStorage.getUserLogin();
        User user = gson.fromJson(userString, User.class);

        // userAddress = gson.fromJson(localStorage.getUserAddress(), UserAddress.class);
        Log.d("User String : ", userString);
        if (user != null) {
            CustomerId=user.getId();
            Password=user.getPassword();
            ActualAdress=user.getAddress();
            name.setText(user.getName());
            email.setText(user.getEmail());
            mobile.setText(user.getMobile());

            address.setText(user.getPrimaryOrderAddress());
            zip.setText(user.getPrimaryOrderPincode());

         /*   zip.setText(((CheckoutActivity) getActivity()).Pin);
            sa_houserno.setText(((CheckoutActivity) getActivity()).HouseDetails);
            sa_Street.setText(((CheckoutActivity) getActivity()).StreetDetails);
            sa_Area.setText(((CheckoutActivity) getActivity()).Area);
            sa_Residential.setText(((CheckoutActivity) getActivity()).ResidentialComplex);
            sa_Landmark.setText(((CheckoutActivity) getActivity()).Landmark);*/
        }

        else
            {
                Intent intent = new Intent(getContext(), LoginRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("PageType","PreCheckout");
                startActivity(intent);
                getActivity().finish();
            //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
             }


     /*   if (userAddress != null) {
            name.setText(userAddress.getName());
            email.setText(userAddress.getEmail());
            mobile.setText(userAddress.getMobile());
            address.setText(userAddress.getAddress());
            zip.setText(userAddress.getZip());

        }*/

        init();
        context = container.getContext();
        txt_pyment = v.findViewById(R.id.txt_pyment);

        txt_pyment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _name = name.getText().toString();
                _email = email.getText().toString();
                _mobile = mobile.getText().toString();
                _address = address.getText().toString();
                _zip = zip.getText().toString();
                _landmark=sa_Landmark.getText().toString();
              /*  houserno = sa_houserno.getText().toString();

                Street = sa_Street.getText().toString();
                Area = sa_Area.getText().toString();
                Residential = sa_Residential.getText().toString();*/

                Pattern p = Pattern.compile(Utils.regEx);

                Matcher m = p.matcher(_email);

                if (_name.length() == 0) {
                    name.setError("Enter Name");
                    name.requestFocus();
                } else if (_email.length() == 0) {
                    email.setError("Enter email");
                    email.requestFocus();
                } else if (!m.find()) {
                    email.setError("Enter Correct email");
                    email.requestFocus();

                } else if (_mobile.length() == 0) {
                    mobile.setError("Enter mobile Number");
                    mobile.requestFocus();
                } else if (_mobile.length() < 10) {
                    mobile.setError("Enter Correct mobile Number");
                    mobile.requestFocus();
                }
              /*  else if (Street.length() == 0) {
                    sa_Street.setError("Enter Street Name");
                    sa_Street.requestFocus();
                }
                else if (houserno.length() ==0) {
                    sa_houserno.setError("Enter House No");
                    sa_houserno.requestFocus();
                }
                else if (Area.length() == 10) {
                    sa_Area.setError("Enter Area Info");
                    sa_Area.requestFocus();
                }
                else if (Residential.length() == 10) {
                    sa_Residential.setError("Enter Residential Address");
                    sa_Residential.requestFocus();
                }*/
                else if (_address.length() == 0) {
                    address.setError("Enter your Address");
                    address.requestFocus();
                }
                else if (_zip.length() == 0) {
                    zip.setError("Enter your Zip Code");
                    zip.requestFocus();
                }
                else if (_landmark.length() ==0) {
                    mobile.setError("Enter Landmark");
                    mobile.requestFocus();
                }else {
                    //  userAddress = new UserAddress(_name, _email, _mobile, _address, _state, _city, _zip);
                    // String user_address = gson.toJson(userAddress);
                    //  localStorage.setUserAddress(user_address);


                    ((PreCheckoutActivity) getActivity()).OrderShippingFname = _name;
                    ((PreCheckoutActivity) getActivity()).OrderShippingEmailId = _email;
                    ((PreCheckoutActivity) getActivity()).OrderShippingAddress = _address;
                    ((PreCheckoutActivity) getActivity()).OrderShippingContactNo = _mobile;
                    Update_Address_Pin();


                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                    ft.replace(R.id.content_frame, new ConfirmFragment(),"ConfirmFragment");
                    ft.commit();
                }


            }


        });
        return v;
    }
    private void Update_Address_Pin() {

        progressDialog.setMessage("Please Wait....");
        progressDialog.show();
        String url=Update_Address_Pin_Url+CustomerId+"&PrimaryAddress="+_address+"&PrimaryPincode="+_zip;
        StringRequest vrequest = new StringRequest(Request.Method.GET, Update_Address_Pin_Url+CustomerId+"&PrimaryAddress="+_address+"&PrimaryPincode="+_zip+"&Landmark="+_landmark,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // tv_MyCode.setText(response);
                        String Msg=response;
                        if(Msg !="0")
                        {
                            user = new User(CustomerId, _name, _email, _mobile,Password,ActualAdress, _address, _zip,_landmark);
                            Gson gson = new Gson();
                            String userString = gson.toJson(user);
                            localStorage = new LocalStorage(getContext());
                            localStorage.createUserLoginSession(userString);
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(getContext(),"Some Things Error Please Try Again ",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }


                        //   setUpSubCategoryAdapter(categories);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //  Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void init() {
        stringArrayState = new ArrayList<String>();
        stringArrayCity = new ArrayList<String>();

        //set city adapter
        final ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getActivity(), R.layout.spinnertextview, stringArrayCity);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);

        int selectionPosition1 = adapterCity.getPosition("Kolkata");
        citySpinner.setSelection(selectionPosition1);
        citySpinner.setEnabled(false);

        if (userAddress != null) {
            //  int selectionPosition1 = adapterCity.getPosition(userAddress.getCity());
            // citySpinner.setSelection(selectionPosition1);
        }

        //Get state json value from assets folder
        try {
            JSONObject obj = new JSONObject(loadJSONFromAssetState());
            JSONArray m_jArry = obj.getJSONArray("statelist");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String state = jo_inside.getString("State");
                String id = jo_inside.getString("id");

                stringArrayState.add(state);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnertextview, stringArrayState);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition("West Bengal");
        stateSpinner.setSelection(spinnerPosition);
        citySpinner.setEnabled(false);
        if (userAddress != null) {
            int selectionPosition = adapter.getPosition(userAddress.getState());
            stateSpinner.setSelection(selectionPosition);
            stateSpinner.setEnabled(false);
        }


        //state spinner item selected listner with the help of this we get selected value

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String Text = stateSpinner.getSelectedItem().toString();


                spinnerStateValue = String.valueOf(stateSpinner.getSelectedItem());
                _state = spinnerStateValue;
                stringArrayCity.clear();

                try {
                    JSONObject obj = new JSONObject(loadJSONFromAssetCity());
                    JSONArray m_jArry = obj.getJSONArray("citylist");

                    for (int i = 0; i < m_jArry.length(); i++) {
                        JSONObject jo_inside = m_jArry.getJSONObject(i);
                        String state = jo_inside.getString("State");
                        String cityid = jo_inside.getString("id");

                        if (spinnerStateValue.equalsIgnoreCase(state)) {
                            _city = jo_inside.getString("city");
                            stringArrayCity.add(_city);
                        }

                    }

                    //notify adapter city for getting selected value according to state
                    adapterCity.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerCityValue = String.valueOf(citySpinner.getSelectedItem());
                Log.e("SpinnerCityValue", spinnerCityValue);

                _city = spinnerCityValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String loadJSONFromAssetState() {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("state.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                json = new String(buffer, StandardCharsets.UTF_8);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String loadJSONFromAssetCity() {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("cityState.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void onBackPressed() {
        InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(v.getWindowToken(),0);

       /* if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Address");
    }
}
