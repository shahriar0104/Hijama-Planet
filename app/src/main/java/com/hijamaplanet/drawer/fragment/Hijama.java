package com.hijamaplanet.drawer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hijamaplanet.R;

import static com.hijamaplanet.BaseActivity.drawer;


public class Hijama extends Fragment implements OnClickListener {

    private Button hijama_ki, hijama_History, hijama_How, hijama_Benefits, hijama_Instrument, hijama_Desease, hijama_InsDisposal, hijama_anatomy, hijama_everybody, hijama_doc_capability, hijama_Theory, hijama_Hadis, hijama_Moon, hijama_OrganSol, hijama_Sector, hijama_bEfaF, hijama_Food, hijama_Time, hijama_When, hijama_HealthPrb, hijama_PopularPerson, hijama_Branch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_hijama_root_layout, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("হিজামা");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_actionbar));


        hijama_ki = view.findViewById(R.id.hijamaKi);
        hijama_History = view.findViewById(R.id.hijamaHistory);
        //hijama_How=view.findViewById(R.id.hijamaTypes);
        //hijama_Benefits=view.findViewById(R.id.hijamaBenefits);
        hijama_Instrument = view.findViewById(R.id.hijamaInstrument);
        hijama_Desease = view.findViewById(R.id.hijamaDesease);
        hijama_InsDisposal = view.findViewById(R.id.hijamaInsDisposal);
        hijama_anatomy = view.findViewById(R.id.hijamaAnatomy);
        hijama_everybody = view.findViewById(R.id.hijamaEveryBody);
        hijama_doc_capability = view.findViewById(R.id.hijamaDoctorCapability);
        hijama_Theory = view.findViewById(R.id.hijamaTheory);
        hijama_Hadis = view.findViewById(R.id.hijamaHadis);
        hijama_Moon = view.findViewById(R.id.hijamaMoon);
        hijama_OrganSol = view.findViewById(R.id.hijamaOrganSolution);
        //hijama_Sector=view.findViewById(R.id.hijamaSector);
        hijama_bEfaF = view.findViewById(R.id.hijamaBefAfter);
        //hijama_Food=view.findViewById(R.id.hijamaFood);
        hijama_Time = view.findViewById(R.id.hijamaTime);
        /*hijama_When=view.findViewById(R.id.hijamaWhen);
        hijama_HealthPrb=view.findViewById(R.id.hijamaHealthPrb);
        hijama_PopularPerson=view.findViewById(R.id.hijamaPopularPerson);
        hijama_Branch=view.findViewById(R.id.hijamaBranch);*/

        hijama_ki.setOnClickListener(this);
        hijama_History.setOnClickListener(this);
        //hijama_How.setOnClickListener(this);
        //hijama_Benefits.setOnClickListener(this);
        hijama_Instrument.setOnClickListener(this);
        hijama_Desease.setOnClickListener(this);
        hijama_InsDisposal.setOnClickListener(this);
        hijama_anatomy.setOnClickListener(this);
        hijama_everybody.setOnClickListener(this);
        hijama_doc_capability.setOnClickListener(this);
        hijama_Theory.setOnClickListener(this);
        hijama_Hadis.setOnClickListener(this);
        hijama_Moon.setOnClickListener(this);
        hijama_OrganSol.setOnClickListener(this);
        //hijama_Sector.setOnClickListener(this);
        hijama_bEfaF.setOnClickListener(this);
        //hijama_Food.setOnClickListener(this);
        hijama_Time.setOnClickListener(this);
        /*hijama_When.setOnClickListener(this);
        hijama_HealthPrb.setOnClickListener(this);
        hijama_PopularPerson.setOnClickListener(this);
        hijama_Branch.setOnClickListener(this);*/


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

    @Override
    public void onClick(View v) {
        if (v == hijama_ki) {
            goToHijamaExtension(0);
        } else if (v == hijama_History) {
            goToHijamaExtension(1);
        } /*else if (v==hijama_How){
            goToHijamaExtension(2);
        } else if (v==hijama_Benefits){
            goToHijamaExtension(3);
        }*/ else if (v == hijama_Instrument) {
            goToHijamaExtension(2);
        } else if (v == hijama_Desease) {
            goToHijamaExtension(3);
        } else if (v == hijama_InsDisposal) {
            goToHijamaExtension(4);
        } else if (v == hijama_anatomy) {
            goToHijamaExtension(5);
        } else if (v == hijama_everybody) {
            goToHijamaExtension(6);
        } else if (v == hijama_doc_capability) {
            goToHijamaExtension(7);
        } else if (v == hijama_Theory) {
            String[] theory = {"তাইবাহ থিওরি", "নাইট্রিক অক্সাইড থিওরি"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Click to show a Theory")
                    .setItems(theory, (dialog, which) -> {
                        if (which == 0) goToHijamaExtension(29);
                        else goToHijamaExtension(30);
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (v == hijama_Hadis) {
            goToHijamaExtension(8);
        } else if (v == hijama_Moon) {
            goToHijamaExtension(9);
        } else if (v == hijama_OrganSol) {
            goToHijamaExtension(10);
        } /*else if (v==hijama_Sector){
            goToHijamaExtension(11);
        }*/ else if (v == hijama_bEfaF) {
            goToHijamaExtension(12);
        } /*else if (v==hijama_Food){
            goToHijamaExtension(13);
        }*/ else if (v == hijama_Time) {
            goToHijamaExtension(14);
        } /*else if (v==hijama_When){
            goToHijamaExtension(15);
        } else if (v==hijama_HealthPrb){
            goToHijamaExtension(16);
        } else if (v==hijama_PopularPerson){
            goToHijamaExtension(17);
        } else if (v==hijama_Branch){
            goToHijamaExtension(18);
        }*/
    }

    public void goToHijamaExtension(int Identifier) {
        Fragment fragment = new HijamaExtension();
        Bundle bundle = new Bundle();
        bundle.putInt("identifier", Identifier);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, fragment).addToBackStack(null).commit();
    }
}