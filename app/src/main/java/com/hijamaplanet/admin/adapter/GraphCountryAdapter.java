package com.hijamaplanet.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hijamaplanet.FontClass;
import com.hijamaplanet.R;
import com.hijamaplanet.admin.activity.GraphShow;
import com.hijamaplanet.drawer.ItemClickListener;
import com.hijamaplanet.service.network.model.LocationData;

import java.util.ArrayList;
import java.util.List;

public class GraphCountryAdapter extends RecyclerView.Adapter<GraphCountryAdapter.GraphCountryHolder> {

    private Context mCtx;
    private ArrayList<String> countryList;
    private List<LocationData> locationData;

    public GraphCountryAdapter(Context mCtx, ArrayList<String> branchList,List<LocationData> locationData) {
        this.mCtx = mCtx;
        this.countryList = branchList;
        this.locationData=locationData;
    }

    public class GraphCountryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView countryNameText;
        private ItemClickListener itemClickListener;

        public GraphCountryHolder(View itemView) {
            super(itemView);
            countryNameText = itemView.findViewById(R.id.duaName);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    @Override
    public GraphCountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.nav_list_map, parent, false);
        return new GraphCountryHolder(view);
    }

    @Override
    public void onBindViewHolder(GraphCountryHolder holder, int position) {
        holder.countryNameText.setText(countryList.get(position));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                redirectToGraphShow(countryList.get(position));
            }

            @Override
            public void onRecyclerItemClick(int pos, ImageView shareImageView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    private void redirectToGraphShow (String country) {
        ArrayList<String> district = new ArrayList<>();
        ArrayList<Integer> userCount = new ArrayList<>();
        int flag = 0;
        for (LocationData location : locationData) {
            try {
                if (country.equalsIgnoreCase(location.getPlace().split(",")[1])) {
                    flag = 1;
                    district.add(location.getPlace().split(",")[0]);
                    userCount.add(Integer.parseInt(location.getUser()));
                } else {
                    if (flag == 1) break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                if (country.equalsIgnoreCase(location.getPlace())) {
                    flag = 1;
                    district.add(location.getPlace());
                    userCount.add(Integer.parseInt(location.getUser()));
                } else {
                    if (flag == 1) break;
                }
            }
        }

        mCtx.startActivity(new Intent(mCtx, GraphShow.class).putExtra("district", district)
                .putExtra("userCount", userCount));
    }
}
