package com.logicrack.MaityPoultry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.MainActivity;


public class HomeSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images;
    String Pincode;

    public HomeSliderAdapter(Context context) {
        this.context = context;
    }

    public HomeSliderAdapter(Context context, Integer[] images,String Pincode) {
        this.context = context;
        this.images = images;
        this.Pincode=Pincode;
    }

    @Override
    public int getCount() {
        return images.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_home_slider, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

      //  String BannerPincode=((MainActivity)context).BannerPinCode;
       imageView.setOnClickListener(new View.OnClickListener(){
       int category_id=0;
       String SearchProduct="";


           @Override
           public void onClick(View view) {
               ((MainActivity)context).onAdapterCalled(category_id,"subcategory",SearchProduct,"");
           }
       });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}