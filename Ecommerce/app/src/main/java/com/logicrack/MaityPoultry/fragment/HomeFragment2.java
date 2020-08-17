package com.logicrack.MaityPoultry.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.adapter.CategoryAdapter;
import com.logicrack.MaityPoultry.adapter.CategotyAdapter2;
import com.logicrack.MaityPoultry.adapter.GridAdapter;
import com.logicrack.MaityPoultry.adapter.HomeSliderAdapter;
import com.logicrack.MaityPoultry.adapter.NewProductAdapter;
import com.logicrack.MaityPoultry.adapter.PopularProductAdapter;
import com.logicrack.MaityPoultry.helper.Data;
import com.logicrack.MaityPoultry.model.CateGoryModel;
import com.logicrack.MaityPoultry.model.Category;
import com.logicrack.MaityPoultry.util.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment2 extends Fragment implements View.OnClickListener {


    ViewPager viewPager;
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
    ProgressDialog progressDialog;
    private List<CateGoryModel> categories = new ArrayList<>();
    private ExpandableHeightGridView category_gridView;
    EditText editProSearch;
    TextView btn_searPro;
    SearchView productSearchView;

    String SearchProduct="";
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
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
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
        btn_searPro= view.findViewById(R.id.btn_searPro);
        productSearchView=view.findViewById(R.id.productSearchView);



        productSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchProduct=query;

                ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory",SearchProduct);
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

        HomeSliderAdapter viewPagerAdapter = new HomeSliderAdapter(getContext(), images);

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

                CateGoryModel category = categories.get(position);

                category_id = category.getCategoryId();


                ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory",SearchProduct);
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

                if
                (SearchProduct.length() == 0) {
                    editProSearch.setError("Enter Product Name");
                    editProSearch.requestFocus();
                }
                else
                {
                    ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory",SearchProduct);
                }
                break;




        }

    }
}
