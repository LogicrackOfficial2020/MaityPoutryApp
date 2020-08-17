package com.logicrack.MaityPoultry.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.model.CateGoryModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategotyAdapter2 extends RecyclerView.Adapter<CategotyAdapter2.PlaceViewHolder> {

    private Context mContext;
    private int[] mPlaceList;
    List<CateGoryModel> categoryList;
    Context context;
    String Tag;
    String SearchProduct=null;
    public CategotyAdapter2(Context mContext, int[] mPlaceList) {
        this.mContext = mContext;
        this.mPlaceList = mPlaceList;
    }
    public CategotyAdapter2(List<CateGoryModel> categoryList, Context context, String tag) {
        this.categoryList = categoryList;
        this.context = context;
        Tag = tag;
    }

    @Override
    public CategotyAdapter2.PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_subcategoryhome, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategotyAdapter2.PlaceViewHolder holder, int position)
    {


       // holder.mPlace.setImageResource(mPlaceList[position]);
       /* Category category = categoryList.get(position);*/


        CateGoryModel category = categoryList.get(position);

        /*Picasso.get()
                .load(category.getImage())
                .into(holder.mPlace, new Callback() {
                    @Override
                    public void onSuccess()
                    { Log.d("Success : ","");

                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Log.d("Error : ", e.getMessage());
                    }
                });*/

        Picasso.get().load(category.getImage()).error(R.drawable.no_image).into(holder.mPlace, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onError(Exception e) {
                Log.d("Error : ", e.getMessage());
            }
        });

      final  int id = category.getCategoryId();

       holder.CategoryName.setText(category.getCategoryName());
       String b =category.getCategoryName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).onAdapterCalled(id,"subcategory",SearchProduct);
            }
        });


    }



    @Override
    public int getItemCount() {

        int i= categoryList.size();
        return categoryList.size();
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mPlace;
        TextView CategoryName;
       View mView;
        ProgressBar progressBar;
        public PlaceViewHolder(View itemView) {
            super(itemView);
            mPlace = itemView.findViewById(R.id.ivPlace);
            CategoryName= itemView.findViewById(R.id.CategoryName);
            progressBar = itemView.findViewById(R.id.progressbar);
            mView = itemView;
        }



     }


}
