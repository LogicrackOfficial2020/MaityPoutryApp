package com.logicrack.MaityPoultry.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.logicrack.MaityPoultry.R;
import com.google.gson.Gson;

import com.logicrack.MaityPoultry.activity.CartActivity;
import com.logicrack.MaityPoultry.model.Cart;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    List<Cart> cartList;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;
    double price;
    double SubTotal;
    int quantity;
    String subTotalAmount;

    public CartAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Cart cart = cartList.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        holder.title.setText(cart.getTitle());
        holder.attribute.setText(cart.getAttribute());
        _price = cart.getPrice();
        _quantity = cart.getQuantity();
        price=Double.parseDouble(_price);
        quantity=Integer.parseInt(_quantity);
        SubTotal=price*quantity;
        subTotalAmount=String.format("%.2f",SubTotal);
        holder.quantity.setText(_quantity);
        holder.price.setText(_price);
        holder.currency.setText("Rs.");
        //_subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
        _subtotal = String.valueOf(subTotalAmount);
        int item = Integer.parseInt(holder.quantity.getText().toString());

       /* if(item>=5){
            holder.plus.setVisibility(View.GONE);
        }
        else if(item < 5){
            holder.plus.setVisibility(View.VISIBLE);
        }*/
        holder.subTotal.setText(_subtotal);
        Picasso.get()
                .load(cart.getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pQuantity = Integer.parseInt(holder.quantity.getText().toString());
                double pPrice,pSubTotal;
                String spubTotalAmount;

                if (pQuantity >= 1) {
                    int total_item = Integer.parseInt(holder.quantity.getText().toString());
                    total_item++;
                    /*if(total_item>=5){
                        holder.plus.setVisibility(View.GONE);
                    }*/
                    holder.quantity.setText(total_item + "");
                    for (int i = 0; i < cartList.size(); i++) {

                        if (cartList.get(i).getId().equalsIgnoreCase(cart.getId())) {
                           /* if(total_item >= 5){
                                holder.plus.setVisibility(View.GONE);
                            }*/

                            // Log.d("totalItem", total_item + "");
                            pPrice=Double.parseDouble(holder.price.getText().toString());
                            pSubTotal=pPrice*total_item;
                            spubTotalAmount=String.format("%.2f",pSubTotal);
                            _subtotal = spubTotalAmount;
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(_subtotal);
                            String cartStr = gson.toJson(cartList);

                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                            ((CartActivity) context).updateTotalPrice();
                        }
                    }
                }


            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pQuantity = Integer.parseInt(holder.quantity.getText().toString());
                double pPrice,pSubTotal;
                String spubTotalAmount;
                if (pQuantity != 1) {
                    int total_item = Integer.parseInt(holder.quantity.getText().toString());
                    total_item--;
                   /* if(total_item < 5){
                        holder.plus.setVisibility(View.VISIBLE);
                    }*/
                    holder.quantity.setText(total_item + "");
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getId().equalsIgnoreCase(cart.getId())) {
                            /*if(total_item < 5){
                                holder.plus.setVisibility(View.VISIBLE);
                            }*/
                            //holder.quantity.setText(total_item + "");
                            //Log.d("totalItem", total_item + "");
                            pPrice=Double.parseDouble(holder.price.getText().toString());
                            pSubTotal=pPrice*total_item;
                            spubTotalAmount=String.format("%.2f",pSubTotal);
                            _subtotal = spubTotalAmount;
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(_subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                            ((CartActivity) context).updateTotalPrice();

                        }
                    }

                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartList.size());
                Gson gson = new Gson();
                String cartStr = gson.toJson(cartList);
                Log.d("CART", cartStr);
                localStorage.setCart(cartStr);
                ((CartActivity) context).updateTotalPrice();


            }
        });


    }

    @Override
    public int getItemCount() {

        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView cardView;
        TextView offer, currency, price, quantity, attribute, addToCart, subTotal;
        Button plus, minus, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            quantity = itemView.findViewById(R.id.quantity);
            currency = itemView.findViewById(R.id.product_currency);
            attribute = itemView.findViewById(R.id.product_attribute);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            delete = itemView.findViewById(R.id.cart_delete);
            subTotal = itemView.findViewById(R.id.sub_total);
            price = itemView.findViewById(R.id.product_price);
        }
    }


}
