package com.doannganh.salesmobileassistant.Views.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doannganh.salesmobileassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomPagerFragment extends Fragment {


    public WelcomPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcom_pager, container, false);
        //ViewPager viewPager = view.findViewById(R.id.viewPager);
        return view;
    }

}
