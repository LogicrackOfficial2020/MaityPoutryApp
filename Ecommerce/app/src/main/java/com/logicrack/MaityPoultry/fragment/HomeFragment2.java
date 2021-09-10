package com.logicrack.MaityPoultry.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.adapter.CartAdapter;
import com.logicrack.MaityPoultry.adapter.CategoryAdapter;
import com.logicrack.MaityPoultry.adapter.CategotyAdapter2;
import com.logicrack.MaityPoultry.adapter.CouponPopUpAdapter;
import com.logicrack.MaityPoultry.adapter.GridAdapter;
import com.logicrack.MaityPoultry.adapter.HomeSliderAdapter;
import com.logicrack.MaityPoultry.adapter.NewProductAdapter;
import com.logicrack.MaityPoultry.adapter.PincodeAdapter;
import com.logicrack.MaityPoultry.adapter.PopularProductAdapter;
import com.logicrack.MaityPoultry.helper.Data;
import com.logicrack.MaityPoultry.model.Cart;
import com.logicrack.MaityPoultry.model.CateGoryModel;
import com.logicrack.MaityPoultry.model.Category;
import com.logicrack.MaityPoultry.model.CouponModel;
import com.logicrack.MaityPoultry.model.CustomerPinCode;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.CustomToast;
import com.logicrack.MaityPoultry.util.ExpandableHeightGridView;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment2 extends Fragment implements View.OnClickListener {

    public static Dialog dialog;
    ViewPager viewPager;
    private static View view;
    LinearLayout sliderDotspanel;
    Timer timer;
    int page_position = 0;
    Data data;
    private int dotscount;
    private ImageView[] dots;
    private List<Category> categoryList = new ArrayList<>();
    private RecyclerView recyclerView, nRecyclerView, pRecyclerView;
    private LinearLayout lin_resturent,lin_poultry,lin_Grossary,lin_Furites,lin_vegetables,lin_Stationary;
    private CategoryAdapter mAdapter;
    private NewProductAdapter nAdapter;
    private PopularProductAdapter pAdapter;
    private Integer[] images = {R.drawable.poultry1, R.drawable.poultry2, R.drawable.poultry3, R.drawable.poultry4, R.drawable.poultry5, R.drawable.poultry6, R.drawable.poultry7};
    private String CategoryRoot;
    private final String URL = "http://123api.123homepaints.com/api/KitchenRefill/App_Category_GetByCategoryRoot?CategoryRoot=";
    private final String usp_PinCust_GetAll = "http://123api.123homepaints.com/api/KitchenRefill/usp_PinCust_GetAll";
    ProgressDialog progressDialog;
    private List<CateGoryModel> categories = new ArrayList<>();
    List<CustomerPinCode> customerPinCodeList = new ArrayList<>();
    private ExpandableHeightGridView category_gridView;
    EditText editProSearch,txt_pincode;
    TextView btn_searPro,btn_submit,btn_seepincode;
    SearchView productSearchView;
    CartAdapter adapter;
    List<Cart> cartList = new ArrayList<>();
    private String mState = "SHOW_MENU";



    User user;
    LocalStorage localStorage;

    public String Email,Pincode,Name,ContactNo,CusId,Password,Address,PrimaryOrderAddress,Lanmark;
    public Boolean ReferStatus;

    String SearchProduct="";
    String CheckPincode;
    int category_id=0;
    public HomeFragment2() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_home2, container, false);
        data = new Data();

        ((MainActivity)getActivity()).activeFragment ="Home";

        progressDialog = new ProgressDialog(getContext());

        category_gridView = view.findViewById(R.id.category_gridView);


        lin_Furites = view.findViewById(R.id.lin_Furites);
        lin_Grossary = view.findViewById(R.id.lin_Grossary);
        lin_vegetables = view.findViewById(R.id.lin_vegetables);
        lin_poultry = view.findViewById(R.id.lin_poultry);
        lin_resturent = view.findViewById(R.id.lin_resturent);
        lin_Stationary= view.findViewById(R.id.lin_Stationary);

        editProSearch = view.findViewById(R.id.editProSearch);
        txt_pincode= view.findViewById(R.id.txt_pincode);
        btn_searPro= view.findViewById(R.id.btn_searPro);
        btn_submit= view.findViewById(R.id.btn_submit);
        btn_seepincode= view.findViewById(R.id.btn_seepincode);
        productSearchView=view.findViewById(R.id.productSearchView);




        GetAllPincode();

        Pincode=((MainActivity) getActivity()).Pincode;
        CusId=((MainActivity) getActivity()).CusId;
        Name=((MainActivity) getActivity()).Name;

        ContactNo=((MainActivity) getActivity()).ContactNo;
        PrimaryOrderAddress=((MainActivity) getActivity()).PrimaryOrderAddress;
        Address=((MainActivity) getActivity()).Address;

        Lanmark=((MainActivity) getActivity()).Lanmark;
        Password=((MainActivity) getActivity()).Password;
        Email=((MainActivity) getActivity()).Email;
        ReferStatus=((MainActivity) getActivity()).ReferStatus;








        if (Pincode.length() == 0  || Pincode.equals("")) {
            txt_pincode.setError("Enter Your Pincode ");
            txt_pincode.requestFocus();
        }
        else {
            txt_pincode.setText(Pincode);
         }
        CheckPincode=Pincode;
        productSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchProduct=query;
                Pincode=txt_pincode.getText().toString();
                if (Pincode.length() == 0  || Pincode.equals("")) {
                    txt_pincode.setError("Enter Your Pincode ");
                    txt_pincode.requestFocus();
                }
                else  if(Pincode.length() < 6|| Pincode.length() > 6){
                    txt_pincode.setError("Enter Valid Pincode ");
                    txt_pincode.requestFocus();
                }
                else {
                    ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory",SearchProduct,Pincode);
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });

        lin_Grossary.setOnClickListener(this);
        lin_Furites.setOnClickListener(this);
        lin_vegetables.setOnClickListener(this);
        lin_poultry.setOnClickListener(this);
        lin_resturent.setOnClickListener(this);
        lin_Stationary.setOnClickListener(this);
        btn_searPro.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_seepincode.setOnClickListener(this);



       /* pRecyclerView = view.findViewById(R.id.popular_product_rv);
        nRecyclerView = view.findViewById(R.id.new_product_rv);
*/

       /* mAdapter = new CategoryAdapter(data.getCategoryList(), getContext(), "Home");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
*/


        CategoryRoot="E";
        lin_Grossary.setBackgroundColor(Color.LTGRAY);
        App_Category_GetByCategoryRoot(CategoryRoot);


        timer = new Timer();
        viewPager = view.findViewById(R.id.viewPager);

        sliderDotspanel = view.findViewById(R.id.SliderDots);

        HomeSliderAdapter viewPagerAdapter = new HomeSliderAdapter(getContext(), images,Pincode);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];


        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*viewPager.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory",SearchProduct,Pincode);
            }
        });*/
        scheduleSlider();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }
    public void scheduleSlider() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == dotscount) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 4000);
    }


    @Override
    public void onStop() {
        timer.cancel();
        super.onStop();
    }

    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    public void onLetsClicked(View view) {
        startActivity(new Intent(getContext(), MainActivity.class));
    }

    private void App_Category_GetByCategoryRoot(String CategoryRoot ){


        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, URL + CategoryRoot,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("CategoryList");
                            categories.clear();
                            for (int i = 0; i <array.length() ; i++) {

                                JSONObject object = array.getJSONObject(i);

                                int CategoryId = object.getInt("CategoryId");
                                String CategoryName = object.getString("CategoryName");
                                String CategoryDesc = object.getString("CategoryDesc");
                                String categoryRoot = object.getString("CategoryRoot");
                                String Url = object.getString("Image");


                                CateGoryModel puja = new CateGoryModel(CategoryId,CategoryName,CategoryDesc,categoryRoot,Url);
                                categories.add(puja);




                            }

                        } catch (JSONException e)
                        {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                     //   setUpSubCategoryAdapter(categories);

                        setUpGridView(categories);

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


    private void setUpGridView(final List<CateGoryModel> categories) {

        GridAdapter gridAdapter = new GridAdapter(getActivity(), categories);
        category_gridView.setAdapter(gridAdapter);
        category_gridView.setExpanded(true);

      //  prog_area.setVisibility(View.GONE);


        category_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pincode=txt_pincode.getText().toString();
                if (Pincode.length() == 0  || Pincode.equals("")) {
                    txt_pincode.setError("Enter Your Delivery Pincode ");
                    txt_pincode.requestFocus();
                }
                else  if(Pincode.length() < 6|| Pincode.length() > 6){
                    txt_pincode.setError("Enter Valid Pincode ");
                    txt_pincode.requestFocus();
                }
                else {
                    ((MainActivity) getActivity()).BannerPinCode=Pincode;
                    CateGoryModel category = categories.get(position);

                    category_id = category.getCategoryId();


                    ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory",SearchProduct,Pincode);
                }


            }
        });

    }


    private void setUpSubCategoryAdapter(List<CateGoryModel> categories) {

        CategotyAdapter2 mAdapter = new CategotyAdapter2(categories, getContext(), "Category2");
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        //  mRecyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void GetAllPincode( ){

        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, usp_PinCust_GetAll,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("pincodes");
                            if(array.length()!=0)
                            {
                                btn_seepincode.setVisibility(View.VISIBLE);
                            }

                            customerPinCodeList.clear();

                            for (int i = 0; i <array.length() ; i++) {

                                JSONObject object = array.getJSONObject(i);

                                String ZoneName = object.getString("ZoneName");
                                String PinCode = object.getString("PinCode");
                                String BrachName = object.getString("BrachName");


                                CustomerPinCode obj = new CustomerPinCode(ZoneName,PinCode,BrachName);
                                customerPinCodeList.add(obj);



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

                btn_seepincode.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.lin_Grossary:
                CategoryRoot="G";
                lin_Grossary.setBackgroundColor(Color.LTGRAY);
                lin_Furites.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_resturent.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_poultry.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_vegetables.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Stationary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                App_Category_GetByCategoryRoot(CategoryRoot);
                break;

            case R.id.lin_Stationary:

                    CategoryRoot="S";
                    lin_Stationary.setBackgroundColor(Color.LTGRAY);
                    lin_Grossary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    lin_Furites.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    lin_resturent.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    lin_poultry.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    lin_vegetables.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    App_Category_GetByCategoryRoot(CategoryRoot);
                    break;

            case R.id.lin_Furites:
                CategoryRoot="F";
                lin_Furites.setBackgroundColor(Color.LTGRAY);
                lin_Grossary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_resturent.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_poultry.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_vegetables.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Stationary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                App_Category_GetByCategoryRoot(CategoryRoot);
                break;


            case R.id.lin_vegetables:
                CategoryRoot="V";
                lin_vegetables.setBackgroundColor(Color.LTGRAY);
                lin_Furites.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_resturent.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_poultry.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Grossary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Stationary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                App_Category_GetByCategoryRoot(CategoryRoot);
                break;


            case R.id.lin_poultry:
                CategoryRoot="P";
                lin_poultry.setBackgroundColor(Color.LTGRAY);
                lin_Furites.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_resturent.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Grossary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_vegetables.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Stationary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                App_Category_GetByCategoryRoot(CategoryRoot);
                break;


            case R.id.lin_resturent:
                CategoryRoot="R";
                lin_resturent.setBackgroundColor(Color.LTGRAY);
                lin_Furites.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Grossary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_poultry.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_vegetables.setBackgroundColor(Color.parseColor("#f5f5f5"));
                lin_Stationary.setBackgroundColor(Color.parseColor("#f5f5f5"));
                App_Category_GetByCategoryRoot(CategoryRoot);
                break;

            case R.id.btn_searPro:
                SearchProduct=editProSearch.getText().toString();
                Pincode=txt_pincode.getText().toString();
                if
                (SearchProduct.length() == 0) {
                    editProSearch.setError("Enter Product Name");
                    editProSearch.requestFocus();
                }
               else if (Pincode.length() == 0  || Pincode.equals("")) {
                    txt_pincode.setError("Enter Your Pincode ");
                    txt_pincode.requestFocus();
                }
                else  if(Pincode.length() < 6|| Pincode.length() > 6){
                    txt_pincode.setError("Enter a Valid Pincode ");
                    txt_pincode.requestFocus();
                }
                else
                {
                    ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory",SearchProduct,Pincode);
                }
                break;

            case R.id.btn_submit:
                Pincode=txt_pincode.getText().toString();
                if (Pincode.length() == 0  || Pincode.equals("")) {
                    txt_pincode.setError("Enter Your Pincode ");
                    txt_pincode.requestFocus();
                }
                else  if(Pincode.length() < 6|| Pincode.length() > 6){
                    txt_pincode.setError("Enter Valid Pincode ");
                    txt_pincode.requestFocus();
                }
                else {
                    progressDialog.dismiss();
                    cartList=((MainActivity) getActivity()).getCartList();


                    if(!cartList.isEmpty())
                    {
                        ((MainActivity) getActivity()).BannerPinCode=Pincode;
                        if(!Pincode.equals(CheckPincode)){
                            AlertDialog diaBox = showDeleteDialog();
                            diaBox.show();
                        }
                    }
                    else {
                        user = new User(CusId, Name, Email, ContactNo, Password, Address, "",Pincode,"",ReferStatus);
                        Gson gson = new Gson();
                        String userString = gson.toJson(user);
                        localStorage = new LocalStorage(getContext());
                        localStorage.createUserLoginSession(userString);
                        ((MainActivity) getActivity()).BannerPinCode=Pincode;
                        ShowAleartDialog("Pincode Save Successful");
                    }

                }

                break;
            case R.id.btn_seepincode:
                showDialog(customerPinCodeList);
                break;



        }

    }


    @NotNull
    private AlertDialog showDeleteDialog() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())

                //set message, title, and icon
                .setTitle("Message")
                .setMessage("Are You Sure Changing Your Delivery Pincode ?? If You Change your Pincode then Your All Added Cart Items Will Be Remove..")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        user = new User(CusId, Name, Email, ContactNo, Password, Address, "",Pincode,"",ReferStatus);
                        Gson gson = new Gson();
                        String userString = gson.toJson(user);
                        localStorage = new LocalStorage(getContext());
                        localStorage.createUserLoginSession(userString);
                        localStorage.deleteCart();
                        mState = "HIDE_MENU";
                        ((MainActivity) getActivity()).BannerPinCode=Pincode;
                        ((MainActivity) getActivity()).OptionMenu();
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                        startActivity(getActivity().getIntent());
                        getActivity().overridePendingTransition(0, 0);

                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        txt_pincode.setText(CheckPincode);
                        Pincode=txt_pincode.getText().toString();
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;
    }






    public void showDialog(List<CustomerPinCode> customerPinCodeList) {

        if(customerPinCodeList.size()>0) {

            dialog = new Dialog(getContext());
            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.fragment_pincodelist);


            RecyclerView recyclerView = dialog.findViewById(R.id.rec_pincodelist);
            PincodeAdapter adapterRe = new PincodeAdapter(getContext(), customerPinCodeList);
            recyclerView.setAdapter(adapterRe);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


/*
            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

            dialog.show();
        }
        else
        {
            Toast.makeText(getContext(),"No Pincode are available",Toast.LENGTH_LONG).show();
        }


    }

    private void ShowAleartDialog(String response)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        progressDialog.dismiss();
        Vibrator vibs = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }
}
