
package com.logicrack.MaityPoultry.adapter;

        import android.content.Context;
        import android.graphics.Paint;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TextView;


        import androidx.cardview.widget.CardView;

        import com.logicrack.MaityPoultry.R;
        import com.logicrack.MaityPoultry.activity.BaseActivity;
        import com.logicrack.MaityPoultry.activity.MainActivity;
        import com.logicrack.MaityPoultry.interfaces.AddorRemoveCallbacks;
        import com.logicrack.MaityPoultry.model.Cart;
        import com.logicrack.MaityPoultry.model.Product;
        import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;
        import com.google.gson.Gson;
        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;


        import java.util.ArrayList;
        import java.util.List;

public class ProductGridAdapter extends BaseAdapter {

    List<Product> productList;
    Context context;
    String Tag;

    LocalStorage localStorage;
    Gson gson;
    List<Cart> cartList = new ArrayList<>();
    String _quantity, _price, _attribute, _subtotal;
    private LayoutInflater inflater;

    public ProductGridAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public ProductGridAdapter(List<Product> productList, Context context, String tag) {
        this.productList = productList;
        this.context = context;
        Tag = tag;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_productgrid,null);
        }

        ImageView imageView;
        final TextView title, attribute, currency, price, shopNow;
        final ProgressBar progressBar;
        final LinearLayout quantity_ll;
        final TextView plus, minus, quantity,Discount_price;
        CardView cardView;

        imageView = view.findViewById(R.id.product_image);
        title = view.findViewById(R.id.product_title);
        attribute = view.findViewById(R.id.product_attribute);
        price = view.findViewById(R.id.product_price);
        currency = view.findViewById(R.id.product_currency);
        shopNow = view.findViewById(R.id.shop_now);
        progressBar = view.findViewById(R.id.progressbar);
        quantity_ll = view.findViewById(R.id.quantity_ll);
        quantity = view.findViewById(R.id.quantity);
        plus = view.findViewById(R.id.quantity_plus);
        minus = view.findViewById(R.id.quantity_minus);
        cardView = view.findViewById(R.id.card_view);

        Discount_price= view.findViewById(R.id.Discount_price);


        final Product product = productList.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        cartList = ((BaseActivity) context).getCartList();



        title.setText(product.getTitle());
        //holder.offer.setText(product.getDiscount());
        attribute.setText(product.getAttribute());
        currency.setText("Rs.");
        price.setText(product.getPrice() );

        if(product.getPurchaseQuantity()-product.getSellQuantity()<=0)
        {
            shopNow.setText("Out of Stock");
        }

        Discount_price.setPaintFlags(Discount_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Discount_price.setText("Market Price: " + "Rs." + ":" + product.getMarketPrice());
        Picasso.get().load(product.getImage()).error(R.drawable.no_image).into(imageView, new Callback() {
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

        if (!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                    shopNow.setVisibility(View.GONE);
                    quantity_ll.setVisibility(View.VISIBLE);
                    quantity.setText(cartList.get(i).getQuantity());

                }
            }
        }


        shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shopNow.getText() != "Out of Stock") {
                    shopNow.setVisibility(View.GONE);
                    quantity_ll.setVisibility(View.VISIBLE);
                    _price = product.getPrice();
                    _quantity = quantity.getText().toString();
                    _attribute = product.getAttribute();
                    _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));

                    if (context instanceof MainActivity) {
                        Cart cart = new Cart(product.getProductPriceId(), product.getTitle(), product.getImage(), product.getCurrency(), _price, _attribute, _quantity, _subtotal);
                        cartList = ((BaseActivity) context).getCartList();
                        cartList.add(cart);

                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                        ((AddorRemoveCallbacks) context).onAddProduct();
                       //notify();
                    }
                }
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                        int total_item = Integer.parseInt(cartList.get(i).getQuantity());
                        total_item++;
                        Log.d("totalItem", total_item + "");
                        quantity.setText(total_item + "");
                        _subtotal = String.valueOf(Double.parseDouble(price.getText().toString()) * total_item);
                        cartList.get(i).setQuantity(quantity.getText().toString());
                        cartList.get(i).setSubTotal(_subtotal);

                        cartList.get(i).setImage(product.getImage());
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                    }
                }


            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(quantity.getText().toString()) != 1) {
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                            int total_item = Integer.parseInt(quantity.getText().toString());

                            total_item--;
                            quantity.setText(total_item + "");
                            Log.d("totalItem", total_item + "");

                            _subtotal = String.valueOf(Double.parseDouble(price.getText().toString()) * total_item);

                            cartList.get(i).setQuantity(quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);

                        }
                    }

                }


            }
        });





     /*   Glide.with(gContext)
              .asBitmap()
               .load(listPuja.get(position).getimage())
               .into(imageView);*/



        return view;
    }


}
