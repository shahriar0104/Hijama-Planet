package com.hijamaplanet.drawer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.adapter.TreatmentPriceAdapter;
import com.hijamaplanet.service.network.model.TreatmentPrice;

import java.util.ArrayList;
import java.util.List;

public class RuqyahPriceList extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_ruqyah_price_list, container, false);

        List<TreatmentPrice> ruqyahPriceList = getArguments().getParcelableArrayList("price");

        List<TreatmentPrice> ruqyahPrice = new ArrayList<>();
        for (TreatmentPrice price : ruqyahPriceList) {
            if (price.getMethod().equalsIgnoreCase("Ruqyah"))
                ruqyahPrice.add(price);
        }
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        TreatmentPriceAdapter ruqyahPriceAdapter = new TreatmentPriceAdapter(ruqyahPrice);
        recyclerView.setAdapter(ruqyahPriceAdapter);

        return view;
    }
}
