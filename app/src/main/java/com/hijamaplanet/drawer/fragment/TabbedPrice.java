package com.hijamaplanet.drawer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hijamaplanet.R;

import java.util.ArrayList;
import java.util.List;

import static com.hijamaplanet.BaseActivity.drawer;

public class TabbedPrice extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_tabbed_price, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("হিজামা/রুকইয়াহ থেরাপীর খরচ");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);

        TabLayout MyTabs = view.findViewById(R.id.MyTabs);
        ViewPager MyPage = view.findViewById(R.id.MyPage);

        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);

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

    private void SetUpViewPager(ViewPager viewpage) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("price", getArguments().getParcelableArrayList("price"));

        HijamaPriceList hijamaPriceList = new HijamaPriceList();
        hijamaPriceList.setArguments(args);
        RuqyahPriceList ruqyahPriceList = new RuqyahPriceList();
        ruqyahPriceList.setArguments(args);

        MyViewPageAdapter Adapter = new MyViewPageAdapter(getChildFragmentManager());
        Adapter.AddFragmentPage(hijamaPriceList, "হিজামা থেরাপীর খরচ    ");
        Adapter.AddFragmentPage(ruqyahPriceList, "      রুকইয়াহ থেরাপীর খরচ");

        viewpage.setAdapter(Adapter);

    }

    private class MyViewPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> MyFragment = new ArrayList<>();
        private List<String> MyPageTittle = new ArrayList<>();

        public MyViewPageAdapter(FragmentManager manager) {
            super(manager);
        }

        public void AddFragmentPage(Fragment Frag, String Title) {
            MyFragment.add(Frag);
            MyPageTittle.add(Title);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MyPageTittle.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
