package com.hijamaplanet.drawer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.ItemClickListener;
import com.hijamaplanet.drawer.adapter.OfferAdapter;
import com.hijamaplanet.service.network.model.Offer;

import java.util.List;

import static com.hijamaplanet.BaseActivity.drawer;


public class OfferFragment extends Fragment implements ItemClickListener {

    private static final String TAG = OfferFragment.class.getSimpleName();
    private List<Offer> offerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_offer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("বিজ্ঞপ্তি");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);

        offerList = getArguments().getParcelableArrayList("offer");

        ListView lv = view.findViewById(R.id.lv);
        OfferAdapter adapter = new OfferAdapter(getContext(), this, offerList);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            drawer.openDrawer(Gravity.START);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {
    }

    @Override
    public void onRecyclerItemClick(int pos, ImageView shareImageView) {
        Fragment imageFragment = ImageFragment.newStringInstance(offerList.get(pos).getImage(), "exists");

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_nav);
        ((FragmentActivity) getContext()).getSupportFragmentManager()
                .beginTransaction()
                .hide(fragment)
                .addSharedElement(shareImageView, ViewCompat.getTransitionName(shareImageView))
                .addToBackStack(TAG)
                .add(R.id.content_nav, imageFragment)
                .commit();
    }
}
