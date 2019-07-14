package com.hijamaplanet.drawer.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.service.network.model.TreatmentPrice;

import java.util.List;


public class TreatmentPriceAdapter extends RecyclerView.Adapter<TreatmentPriceAdapter.HijamaViewHolder> {

    private List<TreatmentPrice> treatmentPriceList;

    public TreatmentPriceAdapter(List<TreatmentPrice> treatmentPriceList) {
        this.treatmentPriceList = treatmentPriceList;
    }

    @NonNull
    @Override
    public HijamaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HijamaViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_treatment_price_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HijamaViewHolder viewHolder, int position) {
        int color1 = Color.parseColor("#405051");
        int color2 = Color.parseColor("#50594c");

        if (position == 0) viewHolder.rootLinearLayout.setBackgroundColor(color1);
        else if ((position % 2) == 0) viewHolder.rootLinearLayout.setBackgroundColor(color1);
        else if ((position % 2) == 1) viewHolder.rootLinearLayout.setBackgroundColor(color2);

        if (treatmentPriceList.get(position).getNameOfPlan().isEmpty()) {
            viewHolder.planNamePriceLL.setVisibility(View.GONE);
            viewHolder.dividerView.setVisibility(View.GONE);
            viewHolder.detailsOfPLan.setText(treatmentPriceList.get(position).getDetailsOfPlan());
        } else {
            viewHolder.nameOfPLan.setText("প্ল্যান " + (position + 1) + "\n" + treatmentPriceList.get(position).getNameOfPlan());
            viewHolder.priceOfPLan.setText("৳ " + treatmentPriceList.get(position).getPriceOfPlan());
            viewHolder.detailsOfPLan.setText(treatmentPriceList.get(position).getDetailsOfPlan());
        }
    }

    @Override
    public int getItemCount() {
        return treatmentPriceList.size();
    }

    public class HijamaViewHolder extends RecyclerView.ViewHolder {
        private TextView nameOfPLan, priceOfPLan, detailsOfPLan;
        private LinearLayout rootLinearLayout,planNamePriceLL;
        private View dividerView;

        public HijamaViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfPLan = itemView.findViewById(R.id.plan_name);
            priceOfPLan = itemView.findViewById(R.id.plan_price);
            detailsOfPLan = itemView.findViewById(R.id.plan_details);
            rootLinearLayout = itemView.findViewById(R.id.rootLinearLayout);
            planNamePriceLL = itemView.findViewById(R.id.planNamePriceLL);
            dividerView = itemView.findViewById(R.id.dividerView);
        }
    }
}
