package com.hijamaplanet.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.hijamaplanet.FontClass;
import com.hijamaplanet.R;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;
import java.util.List;
import static com.hijamaplanet.utils.CommonUtils.dateFormatterMtoY;
import static com.hijamaplanet.utils.CommonUtils.timeFormatter;

public class AllAppointment extends Fragment {
    private static final String TAG = "AllAppointment";
    private FontClass fontClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_all_appointment, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("All Appointment List");
        fontClass = new FontClass(getActivity());

        List<UserAppointmentHistory> appointment = getArguments().getParcelableArrayList("userAppointment");
        ListView listview = view.findViewById(R.id.lv);
        AllAppointmentAdapter adapter = new AllAppointmentAdapter(appointment);

        if (appointment.size() == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("User Appointment");
            alertDialog.setMessage("You have not yet make any Appointment");
            alertDialog.show();
        } else listview.setAdapter(adapter);
        return view;
    }

    class AllAppointmentAdapter extends BaseAdapter {
        private static final String TAG = "AllAppointmentAdapter";
        private List<UserAppointmentHistory> appointment;

        public AllAppointmentAdapter(List<UserAppointmentHistory> appointment) {
            this.appointment = appointment;
        }

        @Override
        public int getCount() {
            return appointment.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public View getView(final int i, View view, final ViewGroup viewGroup) {
            view = getActivity().getLayoutInflater().inflate(R.layout.user_all_appointment_info, null);

            TextView date = view.findViewById(R.id.appointmentDate);
            TextView time = view.findViewById(R.id.appointmentTime);
            TextView branch = view.findViewById(R.id.appointmentBranch);
            TextView treatMethod = view.findViewById(R.id.appointmentMethod);
            TextView approval = view.findViewById(R.id.appointmentApproval);

            date.setTypeface(fontClass.getRegularFont());
            time.setTypeface(fontClass.getRegularFont());
            branch.setTypeface(fontClass.getRegularFont());
            treatMethod.setTypeface(fontClass.getRegularFont());
            approval.setTypeface(fontClass.getBoldFont());

            time.setText(timeFormatter(appointment.get(i).getTime()));
            date.setText(dateFormatterMtoY(appointment.get(i).getDate()));
            branch.setText(appointment.get(i).getBranch() + " Branch");
            treatMethod.setText(appointment.get(i).getTreatment_metod() + " Treatment");
            approval.setText(approvalModify(appointment.get(i).getApproved_status(), approval));

            return view;
        }
    }

    private String approvalModify(String appointment, TextView approval) {
        if (appointment.equalsIgnoreCase("0")) {
            approval.setBackground(getResources().getDrawable(R.drawable.gradient_rejected));
            return "Pending";
        } else if (appointment.equalsIgnoreCase("1")) {
            approval.setBackground(getResources().getDrawable(R.drawable.gradient_approved));
            return "Approved";
        } else {
            approval.setBackground(getResources().getDrawable(R.drawable.gradient_rejected));
            return "Deleted";
        }
    }
}