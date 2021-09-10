package com.logicrack.MaityPoultry.activity;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.recyclerview.widget.DefaultItemAnimator;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.RatingBar;
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
        import com.logicrack.MaityPoultry.adapter.CustomerProductAdapter;
        import com.logicrack.MaityPoultry.model.Product;
        import com.logicrack.MaityPoultry.model.User;
        import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;


        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener{

    private static final int MODE_LIGHT = 0;
    public TextView btncomplain,btnrating;
    TextView Email,FullName,Mobile,Total_Bill_Amount;
    TextView Transaction,txtPaymentMode,Address;
    LinearLayout lin_chat;
    public EditText enter_complain;
    public RatingBar  order_ratingBar;
    public  Button btncancel , btnsubmit;
    public WebView webview;
    String OrderId,CustomerName,CusAddress,ContactNo,PaymentMode,TransactionStatus,OrderShippingEmailId,DeliveryOTP,TotalAmount;
    String OrderStatus,Status;
    ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private List<Product> products = new ArrayList<>();
    private User users = new User();
    private final String URL = "http://123api.123homepaints.com/api/KitchenRefill/usp_Orderdetails_GetAllByOrderId?OrderId=";
    private final String App_ComplainMaster_Save = "http://123api.123homepaints.com/api/KitchenRefill/App_ComplainMaster_Save";
    private final String App_OrderRate_Save = "http://123api.123homepaints.com/api/KitchenRefill/App_OrderRate_Save";
    //String DeliveryCode="1234";
    LocalStorage localStorage;
    User user;
    String CustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        changeStatusBar(MODE_LIGHT,getWindow());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteTextColor));
        //toolbar.setTitle("Order Details");


        localStorage = new LocalStorage(getApplicationContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);
        CustomerId=user.getId();

        Intent data= getIntent();
        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.product_rv);
        OrderId=data.getStringExtra("OrderId");
        CustomerName=data.getStringExtra("CustomerName");
        CusAddress=data.getStringExtra("CusAddress");
        ContactNo=data.getStringExtra("ContactNo");
        PaymentMode=data.getStringExtra("PaymentMode");
        TransactionStatus=data.getStringExtra("TransactionStatus");
        OrderShippingEmailId=data.getStringExtra("OrderShippingEmailId");
        DeliveryOTP=data.getStringExtra("DeliveryOTP");
        TotalAmount=data.getStringExtra("TotalAmount");
        OrderStatus=data.getStringExtra("OrderStatus");
        Status=data.getStringExtra("Status");


        btnrating=findViewById(R.id.btnrating);
        btncomplain=findViewById(R.id.btncomplain);
        Email = findViewById(R.id.Email);
        FullName = findViewById(R.id.FullName);
        Mobile = findViewById(R.id.Mobile);

        Address = findViewById(R.id.Address);
        txtPaymentMode = findViewById(R.id.txtPaymentMode);
        Transaction = findViewById(R.id.Transaction);
        Total_Bill_Amount=findViewById(R.id.Total_Bill_Amount);
        lin_chat=findViewById(R.id.lin_chat);



        Email.setText(OrderShippingEmailId);
        FullName.setText(CustomerName);
        Mobile.setText(ContactNo);

        Address.setText(CusAddress);
        txtPaymentMode.setText(PaymentMode);
        Transaction.setText(TransactionStatus);
        Total_Bill_Amount.setText(TotalAmount);


        GetOrderItems();

        setListeners();

    }

    private void setListeners() {
        btnrating.setOnClickListener(this);
        btncomplain.setOnClickListener(this);

    }

    private  void GetOrderItems(){
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest vrequest = new StringRequest(Request.Method.GET, URL + OrderId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("ProductList");
                            products.clear();
                            for (int i = 0; i <array.length() ; i++) {

                                JSONObject object = array.getJSONObject(i);

                                String orderId = object.getString("OrderId");
                                String ProdName = object.getString("ProdName");
                                String ProdQuantity = object.getString("ProdQuantity");
                                String ProductPriceId = object.getString("ProductPriceId");
                                String TotalAmount = object.getString("TotalAmount");
                                String Savings = object.getString("Savings");


                                Product obj = new Product(orderId,ProdName,ProdQuantity,ProductPriceId,TotalAmount,Savings);
                                products.add(obj);
                            }
                            setUpOrderAdapter(products);

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
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(vrequest);

    }

    private void setUpOrderAdapter(List<Product> products) {

        CustomerProductAdapter mAdapter = new CustomerProductAdapter(products, getApplicationContext());
        //LinearLayout mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        // recyclerView.setLayoutManager(mGridLayoutManager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    public void changeStatusBar(int mode, Window window){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.contentStatus));
            //Light mode
            if(mode==MODE_LIGHT){
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnrating:
                showCustomDialogForRating();
                break;

            case R.id.btncomplain:
                showCustomDialogForComplain();
                break;

        }
    }

    private void showchatboot() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Include dialog.xml file
        dialog.setContentView(R.layout.fragment_chat_boot);

        webview =dialog.findViewById(R.id.webview);
        dialog.setCanceledOnTouchOutside(false);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://shopping.maitypoultries.org/URL/chatbot.html");
        dialog.show();
    }


    private void showCustomDialogForComplain() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Include dialog.xml file
        dialog.setContentView(R.layout.fragment_complain_order);
        btnsubmit = dialog.findViewById(R.id.btnsubmit);
        btncancel = dialog.findViewById(R.id.btncancel);

        enter_complain =dialog.findViewById(R.id.enter_complain);
        dialog.setCanceledOnTouchOutside(false);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Complain=enter_complain.getText().toString();
                if(Complain.equals("")|| Complain.length()==0){
                    enter_complain.setError("Enter Your Complain.... ");
                    enter_complain.requestFocus();
                }
                else {
                    CustomerComplainForOrder(OrderId,CustomerId,Complain);
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

    private void showCustomDialogForRating() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Include dialog.xml file
        dialog.setContentView(R.layout.fragment_customer_rating);
        btnsubmit = dialog.findViewById(R.id.btnsubmit);
        btncancel = dialog.findViewById(R.id.btncancel);

        order_ratingBar =dialog.findViewById(R.id.order_ratingBar);
        dialog.setCanceledOnTouchOutside(false);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float ratings =order_ratingBar.getRating();
                int stars = Math.round(ratings);
                CustomerRatingForOrder(OrderId,CustomerId,stars);
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

    private void CustomerRatingForOrder( final String getOrderId,final String getCustomerId,final int getRatings) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_OrderRate_Save,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            /*  if (!obj.getBoolean("error")){*/
                            int OrderId=obj.getInt("OrderId");
                            String Msg=obj.getString("Msg");
                            if(OrderId!=0)
                            {
                                progressDialog.dismiss();
                                ShowAleartDialog(Msg);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                ShowAleartDialogForSamePage(Msg);
                            }

                        }
                        catch (JSONException e) {

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
                map.put("OrderId", getOrderId);
                map.put("CreatedBy", getCustomerId);
                map.put("Rate", String.valueOf(getRatings));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void CustomerComplainForOrder( final String getOrderId,final String getCustomerId,final String getRemarks) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_ComplainMaster_Save,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            /*  if (!obj.getBoolean("error")){*/
                            int ComplainId=obj.getInt("ComplainId");
                            String Msg=obj.getString("Msg");
                            if(ComplainId!=0)
                            {
                                progressDialog.dismiss();
                                ShowAleartDialog(Msg);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                ShowAleartDialogForSamePage(Msg);
                            }

                        }
                        catch (JSONException e) {

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
                map.put("OrderId", getOrderId);
                map.put("CreatedBy", getCustomerId);
                map.put("Remarks", getRemarks);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


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

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                dialog.dismiss();
                            }
                        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void ShowAleartDialogForSamePage(String response)
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
}
