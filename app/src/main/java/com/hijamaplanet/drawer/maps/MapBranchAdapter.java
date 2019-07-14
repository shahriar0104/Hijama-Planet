package com.hijamaplanet.drawer.maps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.ItemClickListener;
import com.hijamaplanet.service.network.model.BranchLocation;

import java.util.List;

public class MapBranchAdapter extends RecyclerView.Adapter<MapBranchAdapter.RecyclerViewHolder> {

    private Context context;
    List<BranchLocation> branchList;

    public MapBranchAdapter(Context context, List<BranchLocation> branchList) {
        this.context = context;
        this.branchList = branchList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.nav_list_map, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.branchNameText.setText(branchList.get(position).getBranch_name());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                context.startActivity(new Intent(context, MapActivity.class).putExtra("branch",branchList.get(position)));
            }

            @Override
            public void onRecyclerItemClick(int pos, ImageView shareImageView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView branchNameText;
        private ItemClickListener itemClickListener;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            branchNameText = itemView.findViewById(R.id.duaName);
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
}
