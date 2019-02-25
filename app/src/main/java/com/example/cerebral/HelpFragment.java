package com.example.cerebral;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class HelpFragment extends Fragment {

    String[] wa_images;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, container, false);
        wa_images = getArguments().getStringArray("wa_images");

        viewPager = view.findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity(), wa_images);
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }
}
