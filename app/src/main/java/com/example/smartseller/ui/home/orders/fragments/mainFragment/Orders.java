package com.example.smartseller.ui.home.orders.fragments.mainFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.ui.home.adapter.TabPagerAdapter;
import com.example.smartseller.ui.home.homeFragment.home;
import com.example.smartseller.ui.home.orders.fragments.innerFragments.CompletedOrders;
import com.example.smartseller.ui.home.orders.fragments.innerFragments.NewOrders;
import com.example.smartseller.ui.home.orders.fragments.innerFragments.DispatchedOrders;
import com.google.android.material.tabs.TabLayout;


public class Orders extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        tabLayout = v.findViewById(R.id.tab_layout);
        viewPager = v.findViewById(R.id.fragment_container);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        tabLayout.setupWithViewPager(viewPager);
        SetupViewPager(viewPager);
        tabLayout.getTabAt(0).setText("New Orders");
        tabLayout.getTabAt(1).setText("Dispatched Orders");
        tabLayout.getTabAt(2).setText("Completed Orders");
    }

    public void SetupViewPager(ViewPager viewPager) {
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), 1);
        tabPagerAdapter.addFragment(new NewOrders(), "New Orders");
        tabPagerAdapter.addFragment(new DispatchedOrders(), "Dispatched Orders");
        tabPagerAdapter.addFragment(new CompletedOrders(), "Completed Orders");
        viewPager.setAdapter(tabPagerAdapter);
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }
}