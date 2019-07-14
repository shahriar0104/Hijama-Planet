package com.hijamaplanet.drawer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hijamaplanet.R;
import com.hijamaplanet.drawer.ExpandableLayout;
import com.hijamaplanet.service.network.model.Shop;

import java.util.ArrayList;
import java.util.List;

import static com.hijamaplanet.BaseActivity.drawer;

public class ShopFragment extends Fragment {

    private static final int TYPE_IMAGE_VIEW = 0;
    private static final int TYPE_TEXT_VIEW = 1;
    private RecyclerView mRecyclerView;
    private List<Item> mItems;
    private List<Shop> shopItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_shop, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("শপ");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);

        TextView orderPhone = view.findViewById(R.id.orderPhone);
        orderPhone.setSelected(true);
        orderPhone.setSingleLine(true);

        orderPhone.setOnClickListener(v -> {
            String input = orderPhone.getText().toString();
            String[] lines = input.split("ঃ");
            String number = lines[1];

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        });

        shopItems = getArguments().getParcelableArrayList("shop");

        mItems = new ArrayList<>();
        for (int i = 0; i < shopItems.size(); i++) {
            mItems.add(new Item());
        }
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MyAdapter mAdapter = new MyAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            drawer.openDrawer(Gravity.START);
            return true;
        }
        return false;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() instanceof MyViewHolder) {
                MyViewHolder holder = (MyViewHolder) v.getTag();
                boolean result = holder.expandableLayout.toggleExpansion();
                Item item = mItems.get(holder.getAdapterPosition());
                item.isExpand = result ? !item.isExpand : item.isExpand;
            } else if (v.getTag() instanceof TextViewHolder) {
                TextViewHolder holder = (TextViewHolder) v.getTag();
                boolean result = holder.expandableLayout.toggleExpansion();
                Item item = mItems.get(holder.getAdapterPosition());
                item.isExpand = result ? !item.isExpand : item.isExpand;
            }
        }
    };

    private ExpandableLayout.OnExpandListener mOnExpandListener = new ExpandableLayout.OnExpandListener() {

        private boolean isScrollingToBottom = false;

        @Deprecated
        @Override
        public void onToggle(ExpandableLayout view, View child, boolean isExpanded) {}

        @Override
        public void onExpandOffset(ExpandableLayout view, View child, float offset, boolean isExpanding) {
            if (view.getTag() instanceof MyViewHolder) {
                final MyViewHolder holder = (MyViewHolder) view.getTag();
                if (holder.getAdapterPosition() == mItems.size() - 1) {
                    if (!isScrollingToBottom) {
                        isScrollingToBottom = true;
                        mRecyclerView.postDelayed(() -> {
                            isScrollingToBottom = false;
                            mRecyclerView.scrollToPosition(holder.getAdapterPosition());
                        }, 100);
                    }
                }
            }
        }
    };

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Item> items;

        public MyAdapter(List<Item> infos) {
            this.items = infos;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_TEXT_VIEW;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_IMAGE_VIEW) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View itemView = inflater.inflate(R.layout.nav_item_listview, parent, false);
                MyViewHolder holder = new MyViewHolder(itemView);
                holder.imageView.setOnClickListener(mOnClickListener);
                holder.imageView.setTag(holder);
                holder.expandableLayout.setTag(holder);
                holder.expandableLayout.setOnExpandListener(mOnExpandListener);
                return holder;
            } else {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View itemView = inflater.inflate(R.layout.nav_item_expandable_textview, parent, false);
                TextViewHolder holder = new TextViewHolder(itemView);
                holder.textView.setOnClickListener(mOnClickListener);
                holder.textView.setTag(holder);
                holder.expandableLayout.setTag(holder);
                holder.expandableLayout.setOnExpandListener(mOnExpandListener);
                return holder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyViewHolder) {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                Item item = items.get(position);
                viewHolder.expandableLayout.setExpanded(item.isExpand, false);
            } else {
                TextViewHolder viewHolder = (TextViewHolder) holder;
                Item item = items.get(position);
                viewHolder.expandableLayout.setExpanded(item.isExpand, false);
                viewHolder.textView.setText(shopItems.get(position).getHeader());
                viewHolder.expandableTextView.setText(shopItems.get(position).getContent());
            }
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ExpandableLayout expandableLayout;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            expandableLayout =itemView.findViewById(R.id.expandablelayout);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        private ExpandableLayout expandableLayout;
        private TextView textView;
        private TextView expandableTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            expandableLayout =itemView.findViewById(R.id.expandablelayout);
            textView = itemView.findViewById(R.id.textview);
            expandableTextView = itemView.findViewById(R.id.expandable_textview);
        }
    }

    private class Item {
        boolean isExpand;
    }
}
