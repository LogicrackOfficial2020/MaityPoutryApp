package com.logicrack.MaityPoultry.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.LoginRegisterActivity;
import com.logicrack.MaityPoultry.adapter.OrderAdapter;
import com.logicrack.MaityPoultry.model.Order;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyOrderFragment extends Fragment {
    LocalStorage localStorage;
    Gson gson;
    LinearLayout linearLayout;
    private List<Order> orderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter mAdapter;
    User user;
    ProgressDialog progressDialog;
    private List<Order> orders = new ArrayList<>();
    private final String URL = "http://123api.123homepaints.com/api/KitchenRefill/App_usp_Order_GetAllByCustomerId?CustomerId=";
    public MyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        localStorage = new LocalStorage(getActivity());
        progressDialog = new ProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.order_rv);
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        user = gson.fromJson(userString, User.class);

        if (user != null) {
            if(user.getName().length()!= 0){
                String CustomerId=user.getId();
                App_usp_Order_GetAllByCustomerId(CustomerId);
            }
            else {
                startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                getActivity().finish();
            }
        }

        else
        {
            startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
            getActivity().finish();
            //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }






        return view;
    }

    private void App_usp_Order_GetAllByCustomerId(String CustomerId)
    {
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, URL + CustomerId,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("OrderModelList");
                            orders.clear();
                            for (int i = 0; i <array.length() ; i++) {

                                JSONObject object = array.getJSONObject(i);

                                String orderId = object.getString("orderId");
                                String OrderNo = object.getString("OrderNo");
                                String OrderStatus = object.getString("OrderStatus");
                                String OrderDate = object.getString("OrderDate");
                                String OrderAmount = object.getString("OrderAmount");
                                String Status = object.getString("Status");




                                Order obj = new Order(orderId,OrderNo,OrderDate,OrderAmount,OrderStatus,Status);
                                orders.add(obj);




                            }
                            setUpOrderAdapter(orders);

                        }
                        catch (JSONException e)
                        {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);


    }


    private void setUpOrderAdapter(List<Order> orders) {



        OrderAdapter mAdapter = new OrderAdapter(orders, getContext());
        //LinearLayout mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        // recyclerView.setLayoutManager(mGridLayoutManager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("MyOrder");
    }
}
