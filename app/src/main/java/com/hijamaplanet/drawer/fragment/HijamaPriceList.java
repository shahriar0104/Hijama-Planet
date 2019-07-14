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

public class HijamaPriceList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_hijama_price_list, container, false);

        List<TreatmentPrice> tempHijamaPriceList = getArguments().getParcelableArrayList("price");

        List<TreatmentPrice> hijamaPriceList = new ArrayList<>();
        for (TreatmentPrice price : tempHijamaPriceList) {
            if (price.getMethod().equalsIgnoreCase("Hijama"))
                hijamaPriceList.add(price);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        TreatmentPriceAdapter treatmentPriceAdapter = new TreatmentPriceAdapter(hijamaPriceList);
        recyclerView.setAdapter(treatmentPriceAdapter);

        return view;
    }
}
