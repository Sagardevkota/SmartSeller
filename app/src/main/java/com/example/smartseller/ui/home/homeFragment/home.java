package com.example.smartseller.ui.home.homeFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;


import com.example.smartseller.R;
import com.example.smartseller.databinding.FragmentHomeBinding;
import com.example.smartseller.ui.MainActivity;
import com.example.smartseller.ui.auth.LoginActivity;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.util.session.Session;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.Instant;
import java.util.ArrayList;
import android.util.Base64;
import java.util.HashMap;
import java.util.List;

public class home extends Fragment {

    private FragmentHomeBinding binding;
    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getContext());
        checkIfExpired();
        ((HomeActivity)requireActivity()).fabMsgVisibility(true);

        setLineChart();
        setPieChart();
        setHorizontalChart();
        return view;


    }



    private void setHorizontalChart() {
        List<BarEntry> yvlas = new ArrayList<>();
        yvlas.add(new BarEntry(2f, 5f));
        yvlas.add(new BarEntry(4f, 2f));
        yvlas.add(new BarEntry(4f, 10f));
        BarDataSet dataSet = new BarDataSet(yvlas, "Orders");
        BarData barData = new BarData(dataSet);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        binding.hzChart.setData(barData);
        binding.hzChart.setBorderWidth(2);

    }

    private void setLineChart() {
        List<Entry> yvlas = new ArrayList<>();
        yvlas.add(new Entry(2, 5));
        yvlas.add(new Entry(1, 11300));
        yvlas.add(new Entry(2, 1390));
        yvlas.add(new Entry(3, 1190));
        yvlas.add(new Entry(4, 7200));
        yvlas.add(new Entry(5, 4790));
        yvlas.add(new Entry(6, 4500));
        yvlas.add(new Entry(7, 8000));
        yvlas.add(new Entry(8, 7034));
        yvlas.add(new Entry(9, 4307));
        yvlas.add(new Entry(10, 8762));
        yvlas.add(new Entry(11, 4355));
        yvlas.add(new Entry(12, 6000));
        LineDataSet dataSet = new LineDataSet(yvlas, "Orders");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        LineData data = new LineData(dataSet);
        binding.lcTotalOrders.setData(data);
        binding.lcTotalOrders.setPinchZoom(true);
        binding.lcTotalOrders.setTouchEnabled(true);
    }

    private void setPieChart() {
        List<PieEntry> yvlas = new ArrayList<>();
        yvlas.add(new PieEntry(40f, "New Order"));
        yvlas.add(new PieEntry(40f, "Delivered"));
        yvlas.add(new PieEntry(20f, "Cancelled"));
        PieDataSet pieDataSet = new PieDataSet(yvlas, "Orders");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        pieData.setValueTextColor(Color.YELLOW);

        binding.pcOrders.setData(pieData);
        binding.pcOrders.setDrawEntryLabels(false);
        binding.pcOrders.setHoleColor(Color.WHITE);


    }

    private void checkIfExpired() {
        String token = session.getToken();
        String[] split_string = token.split("\\.");
        String payload = split_string[1];
        String body = new String(Base64.decode(payload,Base64.DEFAULT));
        HashMap<String, String> map = new Gson().fromJson(body, new TypeToken<HashMap<String, String>>() {
        }.getType());
        String exp = map.get("exp");
        if (Instant.now().getEpochSecond() >= Long.valueOf(exp)) {
            Log.d("expired", "true");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }


}
