package com.hijamaplanet.drawer.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.GlideApp;
import com.hijamaplanet.drawer.ItemClickListener;
import com.hijamaplanet.service.DeleteOfferService;
import com.hijamaplanet.service.network.model.Offer;

import java.util.List;

import static com.hijamaplanet.utils.CommonUtils.dateFormatterDtoY;

public class OfferMasterAdapter extends BaseAdapter {

    private Context mContext;
    private final int ANIMATION_DURATION = 200;
    private ItemClickListener itemClickListener ;
    private List<Offer> offerList;

    public OfferMasterAdapter(Context context , ItemClickListener itemClickListener, List<Offer> offerList) {
        mContext = context;
        this.itemClickListener = itemClickListener;
        this.offerList=offerList;
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
    public View getView(final int position, View view,final ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.nav_master_offer_list, parent, false);
        TextView expandableTextView = view.findViewById(R.id.expandable_text);
        TextView noticeHeader = view.findViewById(R.id.noticeHeader);
        TextView date = view.findViewById(R.id.noticeDate);
        ImageView imageView=view.findViewById(R.id.imageShow);
        Button deleteOffer=view.findViewById(R.id.deleteOffer);


        noticeHeader.setText(offerList.get(position).getAnnouncement_title());
        date.setText("Offer Valid till : " + dateFormatterDtoY(offerList.get(position).getAnnouncement_validDate().trim()));
        expandableTextView.setText(offerList.get(position).getAnnouncement_desc());
        imageView.setOnClickListener(v -> itemClickListener.onRecyclerItemClick(position, imageView));

        GlideApp.with(mContext).load(offerList.get(position).getImage())
                .thumbnail(GlideApp.with(mContext).load(R.drawable.double_ring))
                .into(imageView);

        View finalConvertView = view;
        deleteOffer.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Delete Offer?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        deleteCell(finalConvertView, position);
                        DeleteOfferService deleteOfferService =new DeleteOfferService(mContext);
                        int offerId= Integer.parseInt(offerList.get(position).getAnnouncement_id());
                        deleteOfferService.execute(""+offerId);
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        return view;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                offerList.remove(index);
                notifyDataSetChanged();
            }
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        };

        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                }
                else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al!=null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }
}

