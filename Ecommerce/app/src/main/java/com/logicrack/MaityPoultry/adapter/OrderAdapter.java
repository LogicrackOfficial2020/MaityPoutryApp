package com.logicrack.MaityPoultry.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.OrderDetails;
import com.logicrack.MaityPoultry.model.Order;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    List<Order> orderList;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Order order = orderList.get(position);
        if(order.getStatus().equals("1")){
            holder.lin_order.setBackgroundColor(Color.parseColor("#EEEEEE"));
            holder.deliveryotp.setVisibility(View.GONE);
        }
        else if(order.getStatus().equals("2")){

            holder.lin_order.setBackgroundColor(Color.parseColor("#A5DC86"));
            holder.deliveryotp.setVisibility(View.VISIBLE);
            holder.deliveryotp.setText("Order Delivery OTP: " + order.getDeliveryOTP());
        }
        else if(order.getStatus().equals("3")){

            holder.lin_order.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.deliveryotp.setVisibility(View.GONE);
        }
        else if(order.getStatus().equals("4")){

            holder.lin_order.setBackgroundColor(Color.parseColor("#D50000"));
            holder.deliveryotp.setVisibility(View.GONE);
        }


        holder.orderId.setText("Order No: #" +order.getOrderId());
        holder.date.setText("Order Date: " + order.getDate());
        holder.total.setText("Order Amount: "+order.getTotal());


        holder.lin_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), OrderDetails.class);
                i.putExtra("OrderId", order.getId());
                i.putExtra("CustomerName",order.getCustomerName());
                i.putExtra("CusAddress",order.getOrderShippingAddress());
                i.putExtra("ContactNo",order.getOrderShippingContactNo());
                i.putExtra("PaymentMode",order.getOrderPaymentMode());
                i.putExtra("TransactionStatus",order.getTransactionStatus());
                i.putExtra("OrderShippingEmailId",order.getOrderShippingEmailId());
                i.putExtra("DeliveryOTP",order.getDeliveryOTP());
                i.putExtra("TotalAmount",order.getTotal());
                i.putExtra("OrderStatus",order.getOrderStatus());
                i.putExtra("Status",order.getStatus());
                context.startActivity(i);
            }
        });

       /* if(order.getStatus()=="1")
        {*/
            holder.status.setText("Order Status: "+order.getOrderStatus());
        /*}*/
      /*  if(order.getOrderStatus().equals("Out for Delivery"))
        {
            holder.deliveryotp.setVisibility(View.VISIBLE);
            holder.deliveryotp.setText("Order Delivery OTP: " + order.getDeliveryOTP());
        }
        else
        {
            holder.deliveryotp.setVisibility(View.GONE);
        }
*/

    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, date, total, status,deliveryotp;
        LinearLayout lin_order;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.order_id);
            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total_amount);
            status = itemView.findViewById(R.id.status);
            deliveryotp= itemView.findViewById(R.id.deliveryotp);
            lin_order= itemView.findViewById(R.id.lin_order);
        }
    }
}
