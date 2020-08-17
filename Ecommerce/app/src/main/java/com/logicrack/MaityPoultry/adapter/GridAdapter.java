package com.logicrack.MaityPoultry.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.model.CateGoryModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context gContext;
    private List<CateGoryModel> categoryList;
    private LayoutInflater inflater;


    public GridAdapter(Context gContext, List<CateGoryModel> categoryList) {
        this.gContext = gContext;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(convertView == null){
            inflater = (LayoutInflater) gContext.getSystemService(gContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_subcategoryhome,null);
        }

        ImageView imageView = view.findViewById(R.id.ivPlace);
        TextView names = view.findViewById(R.id.CategoryName);
     final ProgressBar progressBar = view.findViewById(R.id.progressbar);
        CateGoryModel category = categoryList.get(position);

        names.setText(categoryList.get(position).getCategoryName());


        Picasso.get().load(category.getImage()).error(R.drawable.no_image).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onError(Exception e) {
                Log.d("Error : ", e.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

     /*   Glide.with(gContext)
              .asBitmap()
               .load(listPuja.get(position).getimage())
               .into(imageView);*/



        return view;
    }


}
