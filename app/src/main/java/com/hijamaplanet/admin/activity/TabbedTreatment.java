package com.hijamaplanet.admin.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.fragment.PendingHijamaAPPT;
import com.hijamaplanet.admin.fragment.PendingRuqyahAPPT;

import java.util.ArrayList;
import java.util.List;

public class TabbedTreatment extends AppCompatActivity {
    TabLayout MyTabs;
    ViewPager MyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_tabbed_treatment);

        MyTabs = findViewById(R.id.MyTabs);
        MyPage = findViewById(R.id.MyPage);

        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);
    }

    public void SetUpViewPager (ViewPager viewpage){
        Bundle args = new Bundle();
        args.putParcelableArrayList("userAppointment", getIntent().getParcelableArrayListExtra("userAppointment"));
        PendingHijamaAPPT hijamaFragment = new PendingHijamaAPPT();
        PendingRuqyahAPPT ruqyahFragment = new PendingRuqyahAPPT();
        hijamaFragment.setArguments(args);
        ruqyahFragment.setArguments(args);

        MyViewPageAdapter Adapter = new MyViewPageAdapter(getSupportFragmentManager());
        Adapter.AddFragmentPage(hijamaFragment, "Hijama    ");
        Adapter.AddFragmentPage(ruqyahFragment, "      Ruqyah");
        viewpage.setAdapter(Adapter);
    }

    public class MyViewPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> MyFragment = new ArrayList<>();
        private List<String> MyPageTittle = new ArrayList<>();

        public MyViewPageAdapter(FragmentManager manager){
            super(manager);
        }

        public void AddFragmentPage(Fragment Frag, String Title){
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
