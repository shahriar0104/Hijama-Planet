package com.hijamaplanet.admin.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.adapter.ApprovedAppAdapter;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;
import com.hijamaplanet.service.parser.FetchApprovedAppThroughDate;
import com.hijamaplanet.utils.PrefUserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.hijamaplanet.utils.AppConstants.ONE;
import static com.hijamaplanet.utils.AppConstants.THREE;

public class ApprovedAppointment extends Fragment{

    private PrefUserInfo prefUserInfo;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_hijama_ruqyah_approved, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Appointment Approved List");
        prefUserInfo = new PrefUserInfo(getContext());

        List<UserAppointmentHistory> userAppointment = new ArrayList<>();
        listView = view.findViewById(R.id.lv);

        ApprovedAppAdapter approvedAppAdapter = new ApprovedAppAdapter(getContext(), userAppointment);
        listView.setAdapter(approvedAppAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_date) {
            Calendar c = Calendar.getInstance();
            final DatePicker datePicker = new DatePicker(getContext());
            datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);

            new AlertDialog.Builder(getContext())
                    .setPositiveButton(android.R.string.ok, (DialogInterface dialog, int which) -> {
                        String date = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
                        FetchApprovedAppThroughDate fetchApprovedAppThroughDate = new FetchApprovedAppThroughDate(getContext(),listView);

                        if (ONE.equalsIgnoreCase(prefUserInfo.getUserStatus()))
                            fetchApprovedAppThroughDate.execute("admin", date);
                        else if (THREE.equalsIgnoreCase(prefUserInfo.getUserStatus()))
                            fetchApprovedAppThroughDate.execute("master", date);
                    })
                    .setNegativeButton(android.R.string.cancel, (DialogInterface dialog, int which) ->
                            Log.v("Picker", "Cancelled!")).setView(datePicker).show();
        }
        return super.onOptionsItemSelected(item);
    }
}