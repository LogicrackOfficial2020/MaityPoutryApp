package com.logicrack.MaityPoultry.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.fragment.ConfirmFragment;
import com.logicrack.MaityPoultry.model.CouponModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.PlaceViewHolder>{
    private Context mContext;
    private int[] mPlaceList;
    List<CouponModel> couponModelList;
    Context context;
    String Tag;
    String CouponCode;



    public CouponAdapter(List<CouponModel> couponModelList, Context context) {
        this.couponModelList = couponModelList;
        this.context = context;
       // Tag = tag;
    }
    @NonNull
    @Override
    public CouponAdapter.PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_couponpage, parent, false);
        return new CouponAdapter.PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.PlaceViewHolder holder, int position) {
        CouponModel coupon = couponModelList.get(position);
         CouponCode=coupon.getCouponCode();
        int CouponId=coupon.getCouponId();

        if(coupon.getIsRedeemed()==false) {
            holder.couponcode.setText(coupon.getCouponCode());
            holder.amount.setText(coupon.getCouponAmount());


        }


        holder.lin_Coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.lin_Coupon.setBackgroundColor(Color.parseColor("#007000"));


                ConfirmFragment obj = new ConfirmFragment();
                obj.dialog.dismiss();
               // ((PreCheckoutActivity) v.getContext()).onAdapterCalled(CouponCode,"CouponCode");


            }


        });


    }

    @Override
    public int getItemCount() {
        return couponModelList.size();
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder
    {

        TextView couponcode,amount;
        LinearLayout lin_Coupon;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            couponcode = itemView.findViewById(R.id.couponcode);
            amount= itemView.findViewById(R.id.amount);
            lin_Coupon= itemView.findViewById(R.id.lin_Coupon);

        }



    }
}
