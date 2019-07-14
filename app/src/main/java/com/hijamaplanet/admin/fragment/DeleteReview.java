package com.hijamaplanet.admin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.adapter.DeleteReviewAdapter;
import com.hijamaplanet.service.network.model.Review;

import java.util.List;

public class DeleteReview extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_delete_review_list,container,false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Delete Review");

        List<Review> reviewList = getArguments().getParcelableArrayList("review");

        ListView lv = view.findViewById(R.id.lv);
        DeleteReviewAdapter adapter = new DeleteReviewAdapter(getContext(),reviewList);
        lv.setAdapter(adapter);

        return view;
    }
}
