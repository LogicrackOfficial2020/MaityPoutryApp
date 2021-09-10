package com.logicrack.MaityPoultry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.fragment.ConfirmFragment;
import com.logicrack.MaityPoultry.model.CouponModel;
import com.logicrack.MaityPoultry.model.CustomerPinCode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class PincodeAdapter extends RecyclerView.Adapter<PincodeAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<CustomerPinCode> myList = new ArrayList<>();
    private AdapterCallback mAdapterCallback;

    public PincodeAdapter(Context ctx, List<CustomerPinCode> myList){

        inflater = LayoutInflater.from(ctx);
        this.myList = myList;
       // this.mAdapterCallback = ((AdapterCallback) ctx);
    }

    @Override
    public PincodeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_pincodpage, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PincodeAdapter.MyViewHolder holder, int position) {

        holder.txt_pincode.setText(myList.get(position).getPinCode());
        holder.txt_zonename.setText(myList.get(position).getZoneName());

   /*     holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // .textView.setText("You have selected : "+myImageNameList[getAdapterPosition()]);

                mAdapterCallback.onMethodCallback(myList.get(position).getPinCode(),myList.get(position).getZoneName());
                // ConfirmFragment.ApplyCouponCode(myList.get(position).getCouponCode());
                ConfirmFragment.dialog.dismiss();
            }
        });*/
    }
    public static interface AdapterCallback {
        void onMethodCallback(String couponcode,String amount);
    }
    @Override
    public int getItemCount() {
        return myList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        com.logicrack.MaityPoultry.customfonts.MyTextView txt_pincode,txt_zonename;
        View item;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_pincode = (com.logicrack.MaityPoultry.customfonts.MyTextView) itemView.findViewById(R.id.txt_pincode);
            txt_zonename= (com.logicrack.MaityPoultry.customfonts.MyTextView) itemView.findViewById(R.id.txt_zonename);
            item = itemView;


        }

    }
}

