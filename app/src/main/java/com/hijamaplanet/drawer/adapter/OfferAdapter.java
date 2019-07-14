package com.hijamaplanet.drawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.GlideApp;
import com.hijamaplanet.drawer.ItemClickListener;
import com.hijamaplanet.service.network.model.Offer;

import java.util.List;

import static com.hijamaplanet.utils.CommonUtils.dateFormatterDtoY;

public class OfferAdapter extends BaseAdapter {

    private Context mContext;
    private ItemClickListener itemClickListener;
    private List<Offer> offerList;

    public OfferAdapter(Context context, ItemClickListener itemClickListener, List<Offer> offerList) {
        mContext = context;
        this.itemClickListener = itemClickListener;
        this.offerList = offerList;
    }

    @Override
    public int getCount() {
        return offerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.nav_offer_list, parent, false);
        TextView expandableTextView = view.findViewById(R.id.expandable_text);
        TextView noticeHeader = view.findViewById(R.id.noticeHeader);
        TextView date = view.findViewById(R.id.noticeDate);
        ImageView imageView = view.findViewById(R.id.imageShow);

        noticeHeader.setText(offerList.get(position).getAnnouncement_title());
        date.setText("Offer Valid till : " + dateFormatterDtoY(offerList.get(position).getAnnouncement_validDate().trim()));

        GlideApp.with(mContext).load(offerList.get(position).getImage())
                .thumbnail(GlideApp.with(mContext).load(R.drawable.double_ring))
                .dontAnimate()
                .dontTransform()
                .into(imageView);

        expandableTextView.setText(offerList.get(position).getAnnouncement_desc());
        imageView.setOnClickListener(v -> itemClickListener.onRecyclerItemClick(position, imageView));
        return view;
    }
}

