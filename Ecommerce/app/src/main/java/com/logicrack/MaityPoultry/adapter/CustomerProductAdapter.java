package com.logicrack.MaityPoultry.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.OrderDetails;

import com.logicrack.MaityPoultry.model.Order;
import com.logicrack.MaityPoultry.model.Product;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.MyViewHolder>
{

    List<Product> productList;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;

    public CustomerProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Product product = productList.get(position);
        holder.Product_Name.setText("Product Name : " +product.getTitle());
        holder.Product_Quantity.setText("Product Quantity : " + product.getProdQuantity());
        holder.Product_amount.setText("Product Amount: Rs. "+product.getProduct_PriceId());
        holder.Product_TotalAmount.setText("Total Amount : Rs. "+product.getTotalAmount());

    }



    @Override
    public int getItemCount() {

        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Product_Name, Product_Quantity, Product_amount, Product_TotalAmount;
        LinearLayout lin_row;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Product_Name = itemView.findViewById(R.id.Product_Name);
            Product_Quantity = itemView.findViewById(R.id.Product_Quantity);
            Product_amount = itemView.findViewById(R.id.Product_amount);
            Product_TotalAmount = itemView.findViewById(R.id.Product_TotalAmount);
            lin_row = itemView.findViewById(R.id.lin_row);

        }
    }
}
