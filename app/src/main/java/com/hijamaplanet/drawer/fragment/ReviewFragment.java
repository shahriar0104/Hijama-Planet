package com.hijamaplanet.drawer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.adapter.ReviewAdapter;
import com.hijamaplanet.service.network.model.Review;

import java.util.List;

import static com.hijamaplanet.BaseActivity.drawer;

public class ReviewFragment extends Fragment {

    ListView lv;
    TextView averagePercentage, totalUser, fiveText, fourText, threeText, twoText, oneText;
    RatingBar totalRating;
    View fiveProgress, fourProgress, threeProgress, twoProgress, oneProgress;

    int SIZE, five = 0, four = 0, three = 0, two = 0, one = 0;
    double total = 0 , percentage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_review_of_med_list, container, false);
        View headerview = inflater.inflate(R.layout.nav_review_avg_item, null, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("মন্তব্য");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);
        
        List<Review> reviewList = getArguments().getParcelableArrayList("review");

        averagePercentage = headerview.findViewById(R.id.averagePercentage);
        totalUser = headerview.findViewById(R.id.totalUser);
        fiveText = headerview.findViewById(R.id.fiveText);
        fourText = headerview.findViewById(R.id.fourText);
        threeText = headerview.findViewById(R.id.threeText);
        twoText = headerview.findViewById(R.id.twoText);
        oneText = headerview.findViewById(R.id.oneText);

        totalRating = headerview.findViewById(R.id.totalRating);

        fiveProgress = headerview.findViewById(R.id.fiveProgress);
        fourProgress = headerview.findViewById(R.id.fourProgress);
        threeProgress = headerview.findViewById(R.id.threeProgress);
        twoProgress = headerview.findViewById(R.id.twoProgress);
        oneProgress = headerview.findViewById(R.id.oneProgress);

        SIZE = reviewList.size();

        for (int i = 0; i < SIZE; i++) {
            if (reviewList.get(i).getReview_star().equalsIgnoreCase("5")) {
                five++;
                total = total+5;
            }
            else if (reviewList.get(i).getReview_star().equalsIgnoreCase("4")) {
                four++;
                total = total+4;
            }
            else if (reviewList.get(i).getReview_star().equalsIgnoreCase("3")) {
                three++;
                total = total+3;
            }
            else if (reviewList.get(i).getReview_star().equalsIgnoreCase("2")) {
                two++;
                total = total+2;
            }
            else if (reviewList.get(i).getReview_star().equalsIgnoreCase("1")) {
                one++;
                total = total+1;
            }
        }

        int[] arr = new int[]{five, four, three, two, one};
        int max = arr[0];
        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }

        //DecimalFormat value = new DecimalFormat("#.#");
        percentage = Math.round((total/SIZE)*10)/10.0;
        //value.format(percentage);
        averagePercentage.setText(percentage+"");
        totalRating.setRating((float)percentage);
        totalUser.setText(SIZE+" ratings");

        /*Drawable dr = getResources().getDrawable(R.drawable.ic_user_smallicon);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 32, 32, true));
        //totalUser.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_user_smallicon,0);
        totalUser.setCompoundDrawables(null,null,d,null);
        totalUser.setCompoundDrawablePadding(2);*/

        ViewGroup.LayoutParams params = fiveProgress.getLayoutParams();
        if (five == 0)
            params.width = 0;
        else
            params.width = (params.width*five)/max;
        fiveProgress.setLayoutParams(params);

        params = fourProgress.getLayoutParams();
        if (four == 0)
            params.width = 0;
        else
            params.width = (params.width*four)/max;
        fourProgress.setLayoutParams(params);

        params = threeProgress.getLayoutParams();
        if (three == 0)
            params.width = 0;
        else
            params.width = (params.width*three)/max;
        threeProgress.setLayoutParams(params);

        params = twoProgress.getLayoutParams();
        if (two == 0)
            params.width = 0;
        else
            params.width = (params.width*two)/max;
        twoProgress.setLayoutParams(params);

        params = oneProgress.getLayoutParams();
        if (one == 0)
            params.width = 0;
        else
            params.width = (params.width*one)/max;
        oneProgress.setLayoutParams(params);

        fiveText.setText(five+"");
        fourText.setText(four+"");
        threeText.setText(three+"");
        twoText.setText(two+"");
        oneText.setText(one+"");

        lv = view.findViewById(R.id.lv);
        lv.addHeaderView(headerview);
        ReviewAdapter adapter = new ReviewAdapter(getContext(),reviewList);
        lv.setAdapter(adapter);

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
}

