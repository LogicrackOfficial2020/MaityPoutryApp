package com.logicrack.MaityPoultry.adapter;

import android.content.Context;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.fragment.ConfirmFragment;
import com.logicrack.MaityPoultry.model.CouponModel;

import java.util.ArrayList;
import java.util.List;

public class CouponPopUpAdapter extends RecyclerView.Adapter<CouponPopUpAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<CouponModel> myList = new ArrayList<>();
    private AdapterCallback mAdapterCallback;

    public CouponPopUpAdapter(Context ctx, List<CouponModel> myList){

        inflater = LayoutInflater.from(ctx);
        this.myList = myList;
        this.mAdapterCallback = ((AdapterCallback) ctx);
    }

    @Override
    public CouponPopUpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_couponpage, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CouponPopUpAdapter.MyViewHolder holder, int position) {

        holder.couponcode.setText(myList.get(position).getCouponCode());
        holder.amount.setText(myList.get(position).getCouponAmount());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // .textView.setText("You have selected : "+myImageNameList[getAdapterPosition()]);

                mAdapterCallback.onMethodCallback(myList.get(position).getCouponCode(),myList.get(position).getCouponAmount());
               // ConfirmFragment.ApplyCouponCode(myList.get(position).getCouponCode());
                ConfirmFragment.dialog.dismiss();
            }
        });
    }
    public static interface AdapterCallback {
        void onMethodCallback(String couponcode,String amount);
    }
    @Override
    public int getItemCount() {
        return myList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        com.logicrack.MaityPoultry.customfonts.MyTextView couponcode,amount;
        View item;

        public MyViewHolder(View itemView) {
            super(itemView);

            couponcode = (com.logicrack.MaityPoultry.customfonts.MyTextView) itemView.findViewById(R.id.couponcode);
            amount= (com.logicrack.MaityPoultry.customfonts.MyTextView) itemView.findViewById(R.id.amount);
            item = itemView;


        }

    }
}
