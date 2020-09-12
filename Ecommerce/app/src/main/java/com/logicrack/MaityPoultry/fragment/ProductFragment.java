package com.logicrack.MaityPoultry.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.adapter.NewProductAdapter;
import com.logicrack.MaityPoultry.adapter.ProductGridAdapter;
import com.logicrack.MaityPoultry.helper.Data;
import com.logicrack.MaityPoultry.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment implements View.OnClickListener {

    RecyclerView nRecyclerView;
    Data data;
    private NewProductAdapter pAdapter;
    private final String URL = "http://123api.123homepaints.com/api/KitchenRefill/App_GetProductDetails_GetByCategoryId?CategoryId=";
    ProgressDialog progressDialog;
    private List<Product> products = new ArrayList<>();
    GridView product_gridView;
    int id=0;
    String ProName="";
String Pincode;
    EditText editProSearch;
    TextView btn_searPro;
    SearchView productSearchView;
    public ProductFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        progressDialog = new ProgressDialog(getContext());

        data = new Data();

        ((MainActivity)getActivity()).activeFragment ="Product";
        id = ((MainActivity) getActivity()).AdapterItemClickId;
        ProName=((MainActivity) getActivity()).SearchProduct;
        Pincode=((MainActivity) getActivity()).Pincode;



        product_gridView = view.findViewById(R.id.product_gridView);
        editProSearch = view.findViewById(R.id.editProSearch);
        btn_searPro = view.findViewById(R.id.btn_searPro);
        productSearchView=view.findViewById(R.id.productSearchView);


        productSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ProName=query;
                id=0;
                getProducts(id,ProName);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });


        btn_searPro.setOnClickListener(this);


       getProducts(id,ProName);
        return view;
    }

    private void getProducts(int id,String ProName){


        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        StringRequest vrequest = new StringRequest(Request.Method.GET, URL + String.valueOf(id)+"&SearchPhrase="+ProName+"&Pincode="+Pincode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("ProductList");
                            products.clear();
                            for (int i = 0; i <array.length() ; i++) {

                                JSONObject object = array.getJSONObject(i);
                                int ProdId = object.getInt("ProdId");
                                int CategoryId = object.getInt("CategoryId");
                                int SubCategoryId= object.getInt("SubCategoryId");
                                int ProductPriceId= object.getInt("ProductPriceId");
                                String ProdName = object.getString("ProdName");
                                String ProdImage = object.getString("ProdImageURl");
                                String ProdDetails = object.getString("ProdDetails");
                                String PackageQty = object.getString("PackageQty");
                                String PackageUnit = object.getString("PackageUnit");
                                String ProdMRP = object.getString("ProdMRP");
                                String ProdDiscount = object.getString("ProdDiscount");
                                String ProdMRPType = object.getString("ProdMRPType");
                                String ProdManufacturedBy = object.getString("ProdManufacturedBy");
                                int PurchaseQuantity= object.getInt("PurchaseQuantity");
                                int SellQuantity= object.getInt("SellQuantity");
                                float MarketPrice=(float)object.getDouble("ProdDiscount") + (float)object.getDouble("ProdMRP") ;



                                Product product = new Product(ProdId,CategoryId,SubCategoryId,ProdName,ProdImage,ProdDetails,PackageQty,PackageUnit,ProdMRP,ProdDiscount,ProdMRPType,ProdManufacturedBy,MarketPrice,ProductPriceId,PurchaseQuantity,SellQuantity);
                                products.add(product);




                            }

                        } catch (JSONException e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Something is Wrong! Try Again!!",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                       // setUpProductAdapter(products);
                        if(products.size()>0) {
                            setUpGridView(products);
                        }
                        else
                        {
                            setUpGridView(products);
                            Toast.makeText(getContext(),"This Product is not here yet! Come Again Later!",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Something is Wrong! Try Again!!",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(vrequest);

    }


    private void setUpGridView(final List<Product> products) {

        ProductGridAdapter gridAdapter = new ProductGridAdapter(products, getContext(), "New2");
        product_gridView.setAdapter(gridAdapter);

        //  prog_area.setVisibility(View.GONE);


     /*   product_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CateGoryModel category = categories.get(position);

                final  int category_id = category.getCategoryId();


                ((MainActivity)getActivity()).onAdapterCalled(category_id,"subcategory");
            }
        });*/

    }

    private void setUpProductAdapter(List<Product> products) {

     /*   pAdapter = new NewProductAdapter(products, getContext(), "new");
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getContext());
        nRecyclerView.setLayoutManager(pLayoutManager);
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nRecyclerView.setAdapter(pAdapter);*/

        NewProductAdapter mAdapter = new NewProductAdapter(products, getContext(), "New2");
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        nRecyclerView.setLayoutManager(mGridLayoutManager);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        //  mRecyclerView.setLayoutManager(mLayoutManager);
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Products");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_searPro:
                ProName = editProSearch.getText().toString();
                id=0;

                if
                (ProName.length() == 0) {
                    editProSearch.setError("Enter Product Name");
                    editProSearch.requestFocus();
                }
                else
                    {
                        getProducts(id,ProName);
                    }
                break;
        }
    }
}
