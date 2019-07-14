package com.hijamaplanet.admin.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.service.DeleteReviewService;
import com.hijamaplanet.service.network.model.Review;

import java.util.List;

public class DeleteReviewAdapter extends BaseAdapter {

    private final Context mContext;
    public char firstChar;
    private List<Review> reviewList;
    private static final int ANIMATION_DURATION = 200;

    public DeleteReviewAdapter(Context context,List<Review> reviewList) {
        mContext  = context;
        this.reviewList = reviewList;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                reviewList.remove(index);
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


    @Override
    public int getCount() {
        return reviewList.size();
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
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.admin_delete_review, viewGroup, false);

        TextView profileName=convertView.findViewById(R.id.profileName);
        TextView profileImageChar=convertView.findViewById(R.id.profileImageChar);
        TextView date=convertView.findViewById(R.id.dateOfReview);
        TextView expandable_text =  convertView.findViewById(R.id.expandable_text);
        RatingBar ratingBar=convertView.findViewById(R.id.ratingOfReview);
        ImageView deleteReview=convertView.findViewById(R.id.deleteReview);

        profileName.setText(reviewList.get(position).getReviewer_name());
        ratingBar.setRating(Float.parseFloat(reviewList.get(position).getReview_star()));
        date.setText(reviewList.get(position).getDate());
        expandable_text.setText(reviewList.get(position).getReview_text());

        setProfilePhoto(profileName,profileImageChar);

        View finalConvertView = convertView;
        deleteReview.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Delete Review?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        deleteCell(finalConvertView, position);
                        DeleteReviewService deleteReviewService =new DeleteReviewService(mContext);
                        String value= reviewList.get(position).getReview_id();
                        deleteReviewService.execute(value);

                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });

        return convertView;
    }

    private void setProfilePhoto (TextView profileName,TextView profileImageChar) {
        String nameOfPro=profileName.getText().toString();
        if (!nameOfPro.equalsIgnoreCase("") ){
            firstChar=nameOfPro.charAt(0);
            if (firstChar=='a' || firstChar=='A'){
                profileImageChar.setText("A");
            }else if (firstChar=='b' || firstChar=='B'){
                profileImageChar.setText("B");
            }else if (firstChar=='c' || firstChar=='C'){
                profileImageChar.setText("C");
            }else if (firstChar=='d' || firstChar=='D'){
                profileImageChar.setText("D");
            }else if (firstChar=='e' || firstChar=='E'){
                profileImageChar.setText("E");
            }else if (firstChar=='f' || firstChar=='F'){
                profileImageChar.setText("F");
            }else if (firstChar=='g' || firstChar=='G'){
                profileImageChar.setText("G");
            }else if (firstChar=='h' || firstChar=='H'){
                profileImageChar.setText("H");
            }else if (firstChar=='i' || firstChar=='I'){
                profileImageChar.setText("I");
            }else if (firstChar=='j' || firstChar=='J'){
                profileImageChar.setText("J");
            }else if (firstChar=='k' || firstChar=='K'){
                profileImageChar.setText("K");
            }else if (firstChar=='l' || firstChar=='L'){
                profileImageChar.setText("L");
            }else if (firstChar=='m' || firstChar=='M'){
                profileImageChar.setText("M");
            }else if (firstChar=='n' || firstChar=='N'){
                profileImageChar.setText("N");
            }else if (firstChar=='o' || firstChar=='O'){
                profileImageChar.setText("O");
            }else if (firstChar=='p' || firstChar=='P'){
                profileImageChar.setText("P");
            }else if (firstChar=='q' || firstChar=='Q'){
                profileImageChar.setText("Q");
            }else if (firstChar=='r' || firstChar=='R'){
                profileImageChar.setText("R");
            }else if (firstChar=='s' || firstChar=='S'){
                profileImageChar.setText("S");
            }else if (firstChar=='t' || firstChar=='T'){
                profileImageChar.setText("T");
            }else if (firstChar=='u' || firstChar=='U'){
                profileImageChar.setText("U");
            }else if (firstChar=='v' || firstChar=='V'){
                profileImageChar.setText("V");
            }else if (firstChar=='w' || firstChar=='W'){
                profileImageChar.setText("W");
            }else if (firstChar=='x' || firstChar=='X'){
                profileImageChar.setText("X");
            }else if (firstChar=='y' || firstChar=='Y'){
                profileImageChar.setText("Y");
            }else if (firstChar=='z' || firstChar=='Z'){
                profileImageChar.setText("Z");
            }
        } else {
            profileImageChar.setText("");
        }

    }
}
