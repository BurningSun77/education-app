package com.example.cerebral;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private String[] mImages;


    public ViewPagerAdapter(Context context, String[] images) {

        mImages = images;
        this.context = context;
    }

    @Override
    public int getCount() {

        return mImages.length + 1;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {

        arg0.removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return false;
    }


    @Override
    public Parcelable saveState() {

        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_pager_layout, null);
        collection.addView(view);
        final ImageView img = view.findViewById(R.id.img);
        if (position == 0)
            img.setImageResource(R.drawable.logo);
        else
            Picasso.get().load(mImages[position - 1]).into(img);
        return view;
    }
}