package com.hijamaplanet.drawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.hijamaplanet.R;
import com.hijamaplanet.service.network.model.Review;

import java.util.List;


public class ReviewAdapter extends BaseAdapter {

    private Context mContext;
    public char firstChar;
    private List<Review> reviewList;

    public ReviewAdapter(Context context,List<Review> reviewList) {
        mContext = context;
        this.reviewList = reviewList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.nav_review_of_med_item, parent, false);
            
        TextView profileName = convertView.findViewById(R.id.profileName);
        ImageView profileImageChar = convertView.findViewById(R.id.profileImageChar);
        TextView date = convertView.findViewById(R.id.dateOfReview);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingOfReview);
        TextView expandable_text = convertView.findViewById(R.id.expandable_text);

        profileName.setText(reviewList.get(position).getReviewer_name());
        ratingBar.setRating(Float.parseFloat(reviewList.get(position).getReview_star()));
        date.setText(reviewList.get(position).getDate());
        expandable_text.setText(reviewList.get(position).getReview_text());
        setProfilePhoto(profileName,profileImageChar);

        return convertView;
    }

    private void setProfilePhoto(TextView profileName,ImageView profileImageChar) {
        String nameOfPro = profileName.getText().toString();
        if (!nameOfPro.equalsIgnoreCase("")) {
            firstChar = nameOfPro.charAt(0);
            if (firstChar == 'a' || firstChar == 'A') {
                profileImageChar.setImageResource(R.drawable.a_letter);
            } else if (firstChar == 'b' || firstChar == 'B') {
                profileImageChar.setImageResource(R.drawable.b_letter);
            } else if (firstChar == 'c' || firstChar == 'C') {
                profileImageChar.setImageResource(R.drawable.c_letter);
            } else if (firstChar == 'd' || firstChar == 'D') {
                profileImageChar.setImageResource(R.drawable.d_letter);
            } else if (firstChar == 'e' || firstChar == 'E') {
                profileImageChar.setImageResource(R.drawable.e_letter);
            } else if (firstChar == 'f' || firstChar == 'F') {
                profileImageChar.setImageResource(R.drawable.f_letter);
            } else if (firstChar == 'g' || firstChar == 'G') {
                profileImageChar.setImageResource(R.drawable.g_letter);
            } else if (firstChar == 'h' || firstChar == 'H') {
                profileImageChar.setImageResource(R.drawable.h_letter);
            } else if (firstChar == 'i' || firstChar == 'I') {
                profileImageChar.setImageResource(R.drawable.i_letter);
            } else if (firstChar == 'j' || firstChar == 'J') {
                profileImageChar.setImageResource(R.drawable.j_letter);
            } else if (firstChar == 'k' || firstChar == 'K') {
                profileImageChar.setImageResource(R.drawable.k_letter);
            } else if (firstChar == 'l' || firstChar == 'L') {
                profileImageChar.setImageResource(R.drawable.l_letter);
            } else if (firstChar == 'm' || firstChar == 'M') {
                profileImageChar.setImageResource(R.drawable.m_letter);
            } else if (firstChar == 'n' || firstChar == 'N') {
                profileImageChar.setImageResource(R.drawable.n_letter);
            } else if (firstChar == 'o' || firstChar == 'O') {
                profileImageChar.setImageResource(R.drawable.o_letter);
            } else if (firstChar == 'p' || firstChar == 'P') {
                profileImageChar.setImageResource(R.drawable.p_letter);
            } else if (firstChar == 'q' || firstChar == 'Q') {
                profileImageChar.setImageResource(R.drawable.q_letter);
            } else if (firstChar == 'r' || firstChar == 'R') {
                profileImageChar.setImageResource(R.drawable.r_letter);
            } else if (firstChar == 's' || firstChar == 'S') {
                profileImageChar.setImageResource(R.drawable.s_letter);
            } else if (firstChar == 't' || firstChar == 'T') {
                profileImageChar.setImageResource(R.drawable.t_letter);
            } else if (firstChar == 'u' || firstChar == 'U') {
                profileImageChar.setImageResource(R.drawable.u_letter);
            } else if (firstChar == 'v' || firstChar == 'V') {
                profileImageChar.setImageResource(R.drawable.v_letter);
            } else if (firstChar == 'w' || firstChar == 'W') {
                profileImageChar.setImageResource(R.drawable.w_letter);
            } else if (firstChar == 'x' || firstChar == 'X') {
                profileImageChar.setImageResource(R.drawable.x_letter);
            } else if (firstChar == 'y' || firstChar == 'Y') {
                profileImageChar.setImageResource(R.drawable.y_letter);
            } else if (firstChar == 'z' || firstChar == 'Z') {
                profileImageChar.setImageResource(R.drawable.z_letter);
            }
        } else {
            profileImageChar.setImageResource(R.drawable.ic_user_icon);
        }
    }
}