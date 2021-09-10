package com.logicrack.MaityPoultry.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logicrack.MaityPoultry.activity.CheckoutActivity;
import com.logicrack.MaityPoultry.activity.PreCheckoutActivity;
import com.logicrack.MaityPoultry.adapter.CouponPopUpAdapter;
import com.logicrack.MaityPoultry.model.CouponModel;
import com.logicrack.MaityPoultry.model.OrderModel;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.CustomToast;
import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.BaseActivity;
import com.logicrack.MaityPoultry.activity.CartActivity;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.adapter.CheckoutCartAdapter;
import com.logicrack.MaityPoultry.model.Cart;
import com.logicrack.MaityPoultry.model.Order;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ConfirmFragment extends Fragment   {

    public static Dialog dialog;

    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CheckoutCartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    TextView back, placeOrder,btn_Apply,coupon_amount;
    TextView total, shipping, totalAmount,disamount,btn_selectApply;
    EditText txt_coupon;
    Double _total, _shipping, _totalAmount;
    static ProgressDialog progressDialog;
    List<Order> orderList = new ArrayList<>();
    String orderNo,OrderAmount;
    String id;
    String CouponCode;
    int CustomerId;
    View view;
    JSONArray jsonArray = new JSONArray();
    String jsonText,ShemeCode;
    List<Cart> cartitems = new ArrayList<>();
List<CouponModel> couponModelList = new ArrayList<>();
    RadioButton card, cash;
    RadioGroup paymentGroup;
    int PaymentType=1;
    LinearLayout lin_opencoupon,lin_CouponDiscount;

  public   String jsoncartList;
    private final String OrderSave_URL = "http://123api.123homepaints.com/api/KitchenRefill/OrderPlace";
    private final String CouponAmountURL = "http://123api.123homepaints.com/api/KitchenRefill/CouponMaster_GetAmountforApply?CouponCode=";
    private final String GetAmountDiscount = "http://123api.123homepaints.com/api/KitchenRefill/App_GetShippingDiscount_Amount?OrderAmount=";
    private final String SendSMSFor_Order = "http://123api.123homepaints.com/api/KitchenRefill/SendSMSFor_Order?OrderId=";


    private final String OrderDetails_Url = "http://123api.123homepaints.com/api/KitchenRefill/OrderDetailSave";
    private final String GetCouponByCustomerId_Url = "http://123api.123homepaints.com/api/KitchenRefill/CouponMaster_GetByCustomerId?CustomerId=";

     public Double DiscountAmount,ShippingAmount,CouponAmount;
     public  int CompanyId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_confirm, container, false);
        localStorage = new LocalStorage(getContext());
        recyclerView = view.findViewById(R.id.cart_rv);
        totalAmount = view.findViewById(R.id.total_amount);
        total = view.findViewById(R.id.total);
        shipping = view.findViewById(R.id.shipping_amount);
        back = view.findViewById(R.id.back);
        placeOrder = view.findViewById(R.id.place_order);
        lin_opencoupon=view.findViewById(R.id.lin_opencoupon);
        txt_coupon=view.findViewById(R.id.txt_coupon);
        disamount=view.findViewById(R.id.disamount);
        coupon_amount=view.findViewById(R.id.coupon_amount);
        lin_CouponDiscount=view.findViewById(R.id.lin_CouponDiscount);
        btn_selectApply=view.findViewById(R.id.btn_selectApply);


        paymentGroup = view.findViewById(R.id.payment_group);
        card = view.findViewById(R.id.card_payment);
        cash = view.findViewById(R.id.cash_on_delivery);
        btn_Apply= view.findViewById(R.id.btn_Apply);
        progressDialog = new ProgressDialog(getContext());
        gson = new Gson();
        orderList = ((BaseActivity) getActivity()).getOrderList();
        Random rnd = new Random();
        orderNo = "Order #" + (100000 + rnd.nextInt(900000));
        setUpCartRecyclerview();
        if (orderList.isEmpty()) {
            id = "1";
        } else {
            id = String.valueOf(orderList.size() + 1);
        }


        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getId() == R.id.cash_on_delivery) {

                    PaymentType = 1;

                }
                else if (radioButton.getId() == R.id.card_payment)
                {
                    PaymentType = 2;
                }
                // Toast.makeText(getContext(),radioButton.getText()+"",Toast.LENGTH_LONG).show();
            }
        });



        _total = ((BaseActivity) getActivity()).getTotalPrice();
        _shipping = 0.0;
        _totalAmount = _total + _shipping;
        String TotaltalAmount =String.format("%.2f",_totalAmount);
        total.setText(TotaltalAmount);
        shipping.setText(_shipping + "");
        totalAmount.setText(TotaltalAmount);


         CustomerId =   ((PreCheckoutActivity) getActivity()).CustomerId;
        // CompanyId=((PreCheckoutActivity) getActivity()).CompanyId;
        ShemeCode=((PreCheckoutActivity) getActivity()).ShemeCode;

         OrderAmount= TotaltalAmount;



        cartitems  = ((BaseActivity) getContext()).getCartList();
        GetDiscountShippingAmount();
        String jsonOrder = localStorage.getOrder();

      //  for (int i = 0; i <cartitems.size() ; i++) {

            JSONObject jsonBody = new JSONObject();
            try {

                Gson gson = new Gson();
                 jsoncartList = gson.toJson(cartList);


               // OrderSave(CustomerId,OrderAmount,jsoncartList);


            } catch (Exception e) {
                e.printStackTrace();
            }


       GetCouponByCustomerId_Url();





       /* JSONObject obj = new JSONObject();

        try {
            for (int i = 0; i <cartitems.size() ; i++) {

                Cart myProduct = cartitems.get(i);
                obj.put("id", myProduct.getId());
                obj.put("quantity", myProduct.getProductQuantity());

            }
            jsonArray.put(obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(obj);*/

        //OrderDetailsSave();

// Encodes the JSONArray as a compact JSON string
        // jsonText = jsonArray.toString();



      //  ConfirmOrder(CustomerId,OrderAmount,cartitems);

        btn_Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           String Coupon= txt_coupon.getText().toString();
                if (Coupon.length() == 0) {
                    txt_coupon.setError("Enter Coupon Code");
                    txt_coupon.requestFocus();
                }
                else
                    {
                    ApplyCouponCode(Coupon);
                }
            }
        });

        btn_selectApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(couponModelList);

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Placing Order....");
                progressDialog.show();
                //closeProgress();


                orderPlace(CustomerId,TotaltalAmount,jsoncartList);
            }
        });


        return view;
    }

    public void GetDiscountShippingAmount() {

         progressDialog.setMessage("Please Wait....");
         progressDialog.show();
        int integarOrderAmount = _totalAmount.intValue();



        StringRequest vrequest = new StringRequest(Request.Method.GET, GetAmountDiscount + OrderAmount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            ShippingAmount = obj.getDouble("DeliveryCharge");
                            DiscountAmount = obj.getDouble("DiscountAmount");
                            disamount.setText( "Rs: -" +String.valueOf(DiscountAmount));
                            shipping.setText("Rs: +"+String.valueOf(ShippingAmount));

                            double tamount=((_totalAmount + ShippingAmount) - DiscountAmount);
                            String PtotalAmount =String.format("%.2f",tamount);


                            //totalAmount.setText(String.valueOf((_totalAmount + ShippingAmount) - DiscountAmount));
                            totalAmount.setText(PtotalAmount);

                            _totalAmount =(_totalAmount + ShippingAmount) - DiscountAmount;

                        } catch (JSONException e) {
                            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                          //  e.printStackTrace();
                        }
                        finally {

                            progressDialog.dismiss();;
                        }

                   /*     double Amount = Double.parseDouble(response);
                        _totalAmount=_totalAmount-Amount;
                        String TAmount=String.format("%.2f",_totalAmount);
                        totalAmount.setText(_totalAmount + "");

                        btn_Apply.setText("Appiled");
                        txt_coupon.setText(CouponCode);*/
                        progressDialog.dismiss();
                        //   setUpSubCategoryAdapter(categories);

                        //  Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                //  Toast.makeText(get(),error.getMessage(), Toast.LENGTH_LONG).show();
                // Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);

       /* if(_totalAmount.intValue()>500) {
            double Amount = Double.parseDouble(amount);
            _totalAmount=_totalAmount-Amount;
            totalAmount.setText(_totalAmount + "");

            btn_Apply.setText("Appiled");

            progressDialog.dismiss();
            //   setUpSubCategoryAdapter(categories);

          //  Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "This Coupon is Not Valid for this Order" , Toast.LENGTH_LONG).show();
        }*/


    }
    public void SetCouponCode(String CouponCode, String amount )
    {
        txt_coupon.setText(CouponCode);

    }


    public void ApplyCouponCode(String CouponCode) {

       // progressDialog.setMessage("Please Wait....");
       // progressDialog.show();

        localStorage = new LocalStorage(getContext());
        gson = new Gson();
        String  userString = localStorage.getUserLogin();
        User user = gson.fromJson(userString, User.class);


        final  int  CusId = Integer.parseInt(user.getId());

        int integarOrderAmount = _totalAmount.intValue();

       // String url =CouponAmountURL + CouponCode+"&CustomerId="+CusId+"&OrderAmount="+OrderAmount;

        StringRequest vrequest = new StringRequest(Request.Method.GET, CouponAmountURL + CouponCode+"&CustomerId="+CusId+"&OrderAmount="+integarOrderAmount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);

                            lin_CouponDiscount.setVisibility(View.VISIBLE);

                            CouponAmount= obj.getDouble("CouponAmount");
                            coupon_amount.setText("Rs : -"+CouponAmount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                           // double Amount = CouponAmount;
                            _totalAmount=_totalAmount-CouponAmount;
                        String CAmount=String.format("%.2f",_totalAmount);
                            totalAmount.setText(CAmount);

                            btn_Apply.setText("Appiled");
                            txt_coupon.setText(CouponCode);
                            progressDialog.dismiss();
                            //   setUpSubCategoryAdapter(categories);

                          //  Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                //  Toast.makeText(get(),error.getMessage(), Toast.LENGTH_LONG).show();
                // Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);

       /* if(_totalAmount.intValue()>500) {
            double Amount = Double.parseDouble(amount);
            _totalAmount=_totalAmount-Amount;
            totalAmount.setText(_totalAmount + "");

            btn_Apply.setText("Appiled");

            progressDialog.dismiss();
            //   setUpSubCategoryAdapter(categories);

          //  Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "This Coupon is Not Valid for this Order" , Toast.LENGTH_LONG).show();
        }*/


    }
    
    @NotNull
    private AlertDialog showDeleteDialog() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())

                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;
    }


    private void GetCouponByCustomerId_Url( ){

        localStorage = new LocalStorage(getContext());
        gson = new Gson();
        String  userString = localStorage.getUserLogin();

        User user = gson.fromJson(userString, User.class);


        final  int  CustomerId = Integer.parseInt(user.getId());
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, GetCouponByCustomerId_Url + CustomerId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("coupons");
                            if(array.length()!=0)
                            {
                                lin_opencoupon.setVisibility(View.VISIBLE);
                            }

                            couponModelList.clear();

                            for (int i = 0; i <array.length() ; i++) {

                                JSONObject object = array.getJSONObject(i);

                                int CouponId = object.getInt("CouponId");
                                int CouponTypeId = object.getInt("CouponTypeId");
                                boolean IsRedeemed = object.getBoolean("IsRedeemed");
                                int CustomerId = object.getInt("CustomerId");
                                 CouponCode = object.getString("CouponCode");
                                String CouponAmount = object.getString("CouponAmount");


                                CouponModel obj = new CouponModel(CouponId,CouponTypeId,IsRedeemed,CustomerId,CouponCode,CouponAmount);

                                if(obj.getIsRedeemed() == false)
                                {
                                    couponModelList.add(obj);

                                }

                            }

                        } catch (JSONException e)
                        {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        //   setUpSubCategoryAdapter(categories);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                lin_opencoupon.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);

    }


public void showDialog(List<CouponModel> couponModelList) {

        if(couponModelList.size()>0) {

            dialog = new Dialog(getContext());
            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.fragment_coupondialogfragment_list);


            RecyclerView recyclerView = dialog.findViewById(R.id.couponlist);
            CouponPopUpAdapter adapterRe = new CouponPopUpAdapter(getContext(), couponModelList);
            recyclerView.setAdapter(adapterRe);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            dialog.show();
        }
        else
        {
            Toast.makeText(getContext(),"No Coupons are available",Toast.LENGTH_LONG).show();
        }


}

    private void closeProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();

            }
        }, 3000); // 5000 milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Order order = new Order(id, orderNo, currentDateandTime, "Rs. " + _totalAmount, "Pending","Pending");
        orderList.add(order);
        String orderString = gson.toJson(orderList);
        localStorage.setOrder(orderString);
        localStorage.deleteCart();

        showCustomDialog();
    }

    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        // Include dialog.xml file
        dialog.setContentView(R.layout.success_dialog);
        TextView btn_home = dialog.findViewById(R.id.btn_home);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        // Set dialog title

        dialog.show();
    }

    private void setUpCartRecyclerview() {

        cartList = new ArrayList<>();
        cartList = ((BaseActivity) getContext()).getCartList();


        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CheckoutCartAdapter(cartList, getContext());
        recyclerView.setAdapter(adapter);





    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Confirm");
    }



    private  void orderPlace(final int customerId, final String OrderAmount, final String jsoncartList)
    {

        localStorage = new LocalStorage(getContext());
        gson = new Gson();
        String  userString = localStorage.getUserLogin();
        User user = gson.fromJson(userString, User.class);


        final  int  CustomerId = Integer.parseInt(user.getId());
        final  String  ConsumerIdentifier = user.getName();
        final  String  ConsumerEmailID = user.getEmail();
        final  String  ConsumerMobileNumber = user.getMobile();

        String OrderShippingFname =((PreCheckoutActivity)getActivity()).OrderShippingFname;
        String OrderShippingAddress =((PreCheckoutActivity)getActivity()).OrderShippingAddress;
        String OrderShippingContactNo =((PreCheckoutActivity)getActivity()).OrderShippingContactNo;
        String OrderShippingEmailId=((PreCheckoutActivity)getActivity()).OrderShippingEmailId;
        String OrderPinCode =((PreCheckoutActivity) getActivity()).Pin;
        String Total=String.format("%.2f",_totalAmount);
        final  String total = Total;

        List<Cart> OrderList =cartitems;

        OrderModel order = new OrderModel( total, CustomerId,OrderShippingFname,OrderShippingAddress,OrderShippingContactNo,OrderShippingEmailId, OrderList,ConsumerIdentifier,ConsumerEmailID,ConsumerMobileNumber,CouponCode,OrderPinCode);


        Gson gson = new Gson();
        String jsonString = gson.toJson(order);
        JSONObject JsonObj = new JSONObject();

        try {
            JsonObj = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

         progressDialog.show();
        JsonObjectRequest myJsonRequest = new JsonObjectRequest(Request.Method.POST,
                OrderSave_URL, JsonObj , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jObj) {

                try {
                    int orderId = jObj.getInt("orderId");

                    if(orderId != 0)
                    {
                     /* SendSMS(orderId);*/

                        if(PaymentType == 2) {

                            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                            intent.putExtra("TransactionAmount", total);
                            intent.putExtra("orderId", orderId);
                           intent.putExtra("ShemeCode",ShemeCode);
                            intent.putExtra("TransactionIdentifier", "TXN" + orderId);
                            intent.putExtra("TransactionReference", "ORD" + orderId);
                            intent.putExtra("ConsumerIdentifier", ConsumerIdentifier);
                            intent.putExtra("ConsumerEmailID", ConsumerEmailID);
                            intent.putExtra("ConsumerMobileNumber", ConsumerMobileNumber);


                            startActivity(intent);
                        }
                        else
                        {
                            closeProgress();
                        }
                        progressDialog.dismiss();
                    }

                   // showMsgDialog("Total: " +total + " OrderId:" + orderId);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                finally {
                    progressDialog.dismiss();


                }

              //  showMsgDialog("Total: " +total );
               // closeProgress();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new CustomToast().Show_Toast(getActivity(), view,
                        "Something is Wrong!! Try again");
            }
        });
        //Object volleySingleton;
       // volleySingleton.addToRequestQueue(myJsonRequest, MY_REQUEST_TAG);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(myJsonRequest);

    }


  /*  public void SendSMS(int OrderId) {

       StringRequest vrequest = new StringRequest(Request.Method.GET, SendSMSFor_Order + OrderId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);


                        } catch (JSONException e) {
                            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                            //  e.printStackTrace();
                        }
                        finally {

                            progressDialog.dismiss();;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                //  Toast.makeText(get(),error.getMessage(), Toast.LENGTH_LONG).show();
                // Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);

    }*/


}
