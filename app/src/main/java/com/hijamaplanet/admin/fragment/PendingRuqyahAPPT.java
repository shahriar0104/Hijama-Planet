package com.hijamaplanet.admin.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.service.AdminService;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;
import com.hijamaplanet.utils.PrefUserInfo;
import java.util.ArrayList;
import java.util.List;
import static com.hijamaplanet.utils.AppConstants.THREE;
import static com.hijamaplanet.utils.CommonUtils.timeFormatter;
import static com.hijamaplanet.utils.DialogUtils.showDateDialogFixedDate;
import static com.hijamaplanet.utils.DialogUtils.showTimeDialogFormatter;

public class PendingRuqyahAPPT extends Fragment {
    private static final String TAG = "Ruqyah";
    private ArrayList<String> patientName;
    private ArrayList<String> datePatient;
    private ArrayList<String> timePatient;
    private ArrayList<String> idOfPatient;
    private ArrayList<String> mobilePatient;
    private ArrayList<String> problemPatient;
    private ArrayList<String> branchPatient;
    String userStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_app_pending_request, container, false);
        PrefUserInfo prefUserInfo = new PrefUserInfo(getContext());
        userStatus = prefUserInfo.getUserStatus();
        List<UserAppointmentHistory> userAppointment = getArguments().getParcelableArrayList("userAppointment");

        patientName=new ArrayList<>();
        datePatient=new ArrayList<>();
        timePatient=new ArrayList<>();
        idOfPatient=new ArrayList<>();
        mobilePatient=new ArrayList<>();
        branchPatient=new ArrayList<>();
        problemPatient = new ArrayList<>();

        for (UserAppointmentHistory appointment : userAppointment) {
            if (appointment.getTreatment_metod().equalsIgnoreCase(TAG)) {
                patientName.add(appointment.getName());
                datePatient.add(appointment.getDate());
                timePatient.add(appointment.getTime());
                idOfPatient.add(appointment.getAppointment_id());
                mobilePatient.add(appointment.getMobile());
                problemPatient.add(appointment.getProblem());
                if (THREE.equalsIgnoreCase(userStatus)) branchPatient.add(appointment.getBranch());
            }
        }

        ListView listView = view.findViewById(R.id.lv);
        RuqyahAppAdapter ruqyahAppAdapter = new RuqyahAppAdapter();
        listView.setAdapter(ruqyahAppAdapter);

        return view;
    }

    class RuqyahAppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return patientName.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.admin_appointment_request_info, null);

            TextView nameTv = view.findViewById(R.id.nameTv);
            TextView branchTv = view.findViewById(R.id.branchTv);
            TextView problemTv = view.findViewById(R.id.problemTv);
            Button dateBtn = view.findViewById(R.id.dateBtn);
            Button timeBtn = view.findViewById(R.id.timeBtn);
            Button approveBtn = view.findViewById(R.id.approveBtn);

            if (userStatus.equalsIgnoreCase(THREE)) {
                branchTv.setVisibility(View.VISIBLE);
                branchTv.setText(branchPatient.get(i));
            }
            nameTv.setText(patientName.get(i));
            dateBtn.setText(datePatient.get(i));
            timeBtn.setText(timeFormatter(timePatient.get(i)));
            problemTv.setText(problemPatient.get(i));

            dateBtn.setOnClickListener((View v) -> showDateDialogFixedDate(getContext(), dateBtn, dateBtn.getText().toString()));
            timeBtn.setOnClickListener((View v) -> showTimeDialogFormatter(getContext(), timeBtn));

            approveBtn.setOnClickListener((View v) -> {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setMessage("Accept Appointment Request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                            String pid = idOfPatient.get(i).trim();
                            String date = dateBtn.getText().toString().trim();
                            String time[] = timeBtn.getText().toString().split(" ");
                            String mobile = mobilePatient.get(i).trim();

                            AdminService adminView_backgroundWorker = new AdminService(getActivity());
                            adminView_backgroundWorker.execute("approved_appointment", pid, date, time[0], mobile);
                            viewGroup.getChildAt(i).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out));

                            idOfPatient.remove(i);
                            patientName.remove(i);
                            datePatient.remove(i);
                            timePatient.remove(i);
                            mobilePatient.remove(i);
                            problemPatient.remove(i);
                            if (branchTv.getVisibility() == View.VISIBLE) branchPatient.remove(i);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("No", (DialogInterface dialog, int id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            });

            return view;
        }
    }
}
