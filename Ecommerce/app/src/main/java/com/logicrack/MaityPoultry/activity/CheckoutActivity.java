package com.logicrack.MaityPoultry.activity;
import com.logicrack.MaityPoultry.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.logicrack.MaityPoultry.model.Order;
import com.logicrack.MaityPoultry.model.OrderModel;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;
import com.google.gson.Gson;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.model.Checkout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;


import com.paynimo.android.payment.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends CheckOutBaseActivity {
    Gson gson;
    List<Order> orderList = new ArrayList<>();
    LocalStorage localStorage;
    String TransactionIdentifier,TransactionReference,TransactionAmount,TransactionDateTime,ConsumerIdentifier,ConsumerEmailID,ConsumerMobileNumber;
    ProgressDialog progressDialog;
    private static final String TAG = "CheckoutActivity";
    private final String TransactionStatus_Update_Url = "http://123api.123homepaints.com/api/KitchenRefill/App_TransactionStatus_Update";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_checkout);
        progressDialog = new ProgressDialog(this);
        TransactionIdentifier = getIntent().getStringExtra("TransactionIdentifier");
        TransactionReference = getIntent().getStringExtra("TransactionReference");
        TransactionAmount = getIntent().getStringExtra("TransactionAmount");
        TransactionDateTime = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        ConsumerIdentifier = getIntent().getStringExtra("ConsumerIdentifier");
        ConsumerEmailID = getIntent().getStringExtra("ConsumerEmailID");
        ConsumerMobileNumber = getIntent().getStringExtra("ConsumerMobileNumber");

        creatingCheckOutObjects();
    }

    private void closeProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();

            }
        }, 3000); // 5000 milliseconds



        localStorage.deleteCart();

        showCustomDialog();
    }

    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        // Include dialog.xml file
        dialog.setContentView(R.layout.success_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);



                startActivity(intent);
            }
        });
        // Set dialog title

        dialog.show();
    }


    private  void TransactionStatus_Update(final int TransactionStatus, final String TransactionIdentifier)
    {

        localStorage = new LocalStorage(this);
        gson = new Gson();
        String  userString = localStorage.getUserLogin();
        User user = gson.fromJson(userString, User.class);



        OrderModel order = new OrderModel(TransactionStatus,TransactionIdentifier);




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
                TransactionStatus_Update_Url, JsonObj , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jObj) {

                try {
                    String msg = jObj.getString("msg");

                    if(msg =="Status Updated")
                    {
                        closeProgress();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // closeProgress();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        });
        //Object volleySingleton;
        // volleySingleton.addToRequestQueue(myJsonRequest, MY_REQUEST_TAG);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myJsonRequest);

    }


    private void creatingCheckOutObjects() {
        // Creating Checkout Object
        Checkout checkout = new Checkout();


// setting values to checkout object

        checkout.setMerchantIdentifier("L508151");  //T508151where T1234 is the merchant Code and will be provided by TPSL L508151

       checkout.setTransactionIdentifier(TransactionIdentifier); //where TXN001 is the Merchant transaction identifier (alphanumeric no special character allowed)
       // checkout.setTransactionIdentifier("TXN028");
        checkout.setTransactionReference(TransactionReference); //where ORD0001 is the Merchant transaction reference number
      //  checkout.setTransactionReference("ORD0028");

        checkout.setTransactionType(PaymentActivity.TRANSACTION_TYPE_SALE); //Transaction type

        checkout.setTransactionSubType(PaymentActivity.TRANSACTION_SUBTYPE_DEBIT); //Transaction subtype

        checkout.setTransactionCurrency("INR"); //CURRENCY

        checkout.setTransactionAmount(TransactionAmount); //Transaction amount

        checkout.setTransactionDateTime(TransactionDateTime); //Transaction date
      // checkout.setTransactionDateTime("26-05-2020");


// setting Consumer fields values

      //  checkout.setConsumerIdentifier(ConsumerIdentifier); //Consumer Identifier, set this value as application user name

        checkout.setConsumerIdentifier("Sun");

        checkout.setConsumerEmailID(ConsumerEmailID); //Consumer email id
       // checkout.setConsumerEmailID("subha221b@gmail.com");
        checkout.setConsumerMobileNumber(ConsumerMobileNumber); //Consumer mobile number
        // checkout.setConsumerMobileNumber("8967547439");

        checkout.setConsumerAccountNo(""); //Default value "", leave it blank



// setting Consumer Cart Item

        checkout.addCartItem("FIRST", "1.00", "0.0", "0.0",

                "", "", "", "");
        callingPaymentActivity(checkout);

    }

    private void callingPaymentActivity(Checkout checkout) {
       /* Intent authIntent = new Intent(this, PaymentModesActivity.class);

// Checkout Object
        Log.d("Checkout Request Object",
                checkout.getMerchantRequestPayload().toString());
        authIntent.putExtra(Constant.ARGUMENT_DATA_CHECKOUT,
                checkout);
// Public Key
        authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY,
                "1234-6666-6789-56");
// Requested Payment Mode
        authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE,
                PaymentActivity.PAYMENT_METHOD_DEFAULT);

        startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE);*/
        Intent authIntent = PaymentModesActivity.Factory.getAuthorizationIntent(getApplicationContext(), true);

        authIntent.putExtra(Constant.ARGUMENT_DATA_CHECKOUT, checkout);

        Log.d("Object", checkout.getMerchantRequestPayload().toString());

// Public Key

        authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY, "1234-6666-6789-56");

        authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE, PaymentActivity.PAYMENT_METHOD_DEFAULT);



        PaymentModesActivity.Settings settings = new PaymentModesActivity.Settings();



        authIntent.putExtra(Constant.ARGUMENT_DATA_SETTING, settings);



        startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PaymentActivity.REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == PaymentActivity.RESULT_OK) {
                Log.d(TAG, "Result Code :" + RESULT_OK);
                if (data != null) {

                    try {
                        Checkout checkout_res = (Checkout) data
                                .getSerializableExtra(Constant.ARGUMENT_DATA_CHECKOUT);
                        Log.d("Checkout Response Obj", checkout_res
                                .getMerchantResponsePayload().toString());

                        String transactionType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getType();
                        String transactionSubType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getSubType();
                        if (transactionType != null && transactionType.equalsIgnoreCase(PaymentActivity.TRANSACTION_TYPE_PREAUTH)
                                && transactionSubType != null && transactionSubType
                                .equalsIgnoreCase(PaymentActivity.TRANSACTION_SUBTYPE_RESERVE)) {
                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload()
                                    .getPaymentMethod().getPaymentTransaction()
                                    .getStatusCode().equalsIgnoreCase(PaymentActivity.TRANSACTION_STATUS_PREAUTH_RESERVE_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS=>", "SUCCESS");

                                /**
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0200 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */

                                if (checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getInstruction().
                                        getStatusCode().equalsIgnoreCase("")) {
                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0200 means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>",
                                            "SI Transaction Not Initiated");
                                }

                            } // Transaction Completed and Got FAILURE

                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {

                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode().equalsIgnoreCase(
                                    PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS=>", "SUCCESS");

                                /**
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0300 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */

                                if (checkout_res.getMerchantResponsePayload().
                                        getPaymentMethod().getPaymentTransaction().
                                        getInstruction().getStatusCode()
                                        .equalsIgnoreCase("")) {
                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0300 means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>",
                                            "SI Transaction Not Initiated");
                                }
                                else if (checkout_res.getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction()
                                        .getInstruction()
                                        .getStatusCode().equalsIgnoreCase(
                                                PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {

                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0300 means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>", "SUCCESS");
                                } else {
                                    /**
                                     * SI TRANSACTION STATUS - Failure (status
                                     * code OTHER THAN 0300 means failure)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>", "FAILURE");
                                }

                            } // Transaction Completed and Got FAILURE
                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                        String result = "StatusCode : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusCode()
                                + "\nStatusMessage : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusMessage()
                                + "\nErrorMessage : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getErrorMessage()
                                + "\nAmount : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount()
                                + "\nDateTime : " + checkout_res.
                                getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getDateTime()
                                + "\nMerchantTransactionIdentifier : "
                                + checkout_res.getMerchantResponsePayload()
                                .getMerchantTransactionIdentifier()
                                + "\nIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getIdentifier();

                        TransactionStatus_Update(2,checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getIdentifier());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == PaymentActivity.RESULT_ERROR) {
                Log.d(TAG, "got an error");

                if (data.hasExtra(PaymentActivity.RETURN_ERROR_CODE) &&
                        data.hasExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                    String error_code = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_CODE);
                    String error_desc = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION);

                    Toast.makeText(getApplicationContext(), " Got error :"
                            + error_code + "--- " + error_desc, Toast.LENGTH_SHORT)
                            .show();
                    Log.d(TAG + " Code=>", error_code);
                    Log.d(TAG + " Desc=>", error_desc);

                    TransactionStatus_Update(3,TransactionIdentifier);

                }

            } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Transaction Aborted by User",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "User pressed back button");
                TransactionStatus_Update(3,TransactionIdentifier);

            }
        }
    }

}
