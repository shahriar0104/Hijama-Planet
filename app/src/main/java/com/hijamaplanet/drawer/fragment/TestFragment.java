package com.hijamaplanet.drawer.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hijamaplanet.FontClass;
import com.hijamaplanet.MainActivity;
import com.hijamaplanet.R;
import com.hijamaplanet.admin.activity.AdminView;
import com.hijamaplanet.admin.activity.MasterAdminView;
import com.hijamaplanet.drawer.DuaActivity;
import com.hijamaplanet.drawer.QuestionLibrary;
import com.hijamaplanet.user.UserView;

import static com.hijamaplanet.BaseActivity.drawer;

public class TestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_test_fragment, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("সমস্যা যাচাই");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);

        Bundle bundle = new Bundle();
        view.findViewById(R.id.jinCard).setOnClickListener(v -> {
            bundle.putStringArray("question", QuestionLibrary.jinQuestions);
            bundle.putSerializable("value", QuestionLibrary.jinValue);
            redirectToCard(bundle);
        });
        view.findViewById(R.id.envyCard).setOnClickListener(v -> {
            bundle.putStringArray("question", QuestionLibrary.envyQuestions);
            bundle.putSerializable("value", QuestionLibrary.envyValue);
            redirectToCard(bundle);
        });
        view.findViewById(R.id.witchCard).setOnClickListener(v -> {
            bundle.putStringArray("question", QuestionLibrary.witchQuestions);
            bundle.putSerializable("value", QuestionLibrary.witchValue);
            redirectToCard(bundle);
        });
        view.findViewById(R.id.waswasCard).setOnClickListener(v -> {
            bundle.putStringArray("question", QuestionLibrary.waswasQuestions);
            bundle.putSerializable("value", QuestionLibrary.waswasValue);
            redirectToCard(bundle);
        });

        return view;
    }

    private void redirectToCard(Bundle bundle) {
        Fragment fragment = new TestIndividual();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_nav, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
