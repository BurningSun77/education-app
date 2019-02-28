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
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class HelpFragment extends Fragment {

    private String[] wa_images;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private ImageView helpImage;
    private Button prev;
    private Button next;

    private int helpToggle = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, container, false);
        wa_images = getArguments().getStringArray("wa_images");

        viewPager = view.findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity(), wa_images);
        viewPager.setAdapter(viewPagerAdapter);

        helpImage = view.findViewById(R.id.helpImage);
        prev = view.findViewById(R.id.prev);
        next = view.findViewById(R.id.next);

        if (wa_images.length != 0) {

            Picasso.get().load(wa_images[helpToggle]).into(helpImage);
        }

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wa_images.length != 0) {

                    Picasso.get().load(wa_images[helpToggle]).into(helpImage);
                    if (--helpToggle == -1) helpToggle = wa_images.length - 1;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wa_images.length != 0) {

                    Picasso.get().load(wa_images[helpToggle]).into(helpImage);
                    if (++helpToggle == wa_images.length) helpToggle = 0;
                }
            }
        });

        return view;
    }
}
