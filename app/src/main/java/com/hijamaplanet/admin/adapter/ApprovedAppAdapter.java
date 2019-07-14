package com.hijamaplanet.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hijamaplanet.R;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;
import com.hijamaplanet.service.parser.FetchApprovedAppThroughDate;
import com.hijamaplanet.utils.CommonUtils;
import com.hijamaplanet.utils.PrefUserInfo;

import java.util.List;

import static com.hijamaplanet.utils.AppConstants.THREE;
import static com.hijamaplanet.utils.DialogUtils.showDateDialogFixedDate;
import static com.hijamaplanet.utils.DialogUtils.showTimeDialogFormatter;

public class ApprovedAppAdapter extends BaseAdapter {

    private Context context;
    private List<UserAppointmentHistory> userAppointment;

    public ApprovedAppAdapter(Context context, List<UserAppointmentHistory> userAppointment) {
        this.context = context;
        this.userAppointment = userAppointment;
    }

    @Override
    public int getCount() {
        return userAppointment.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.admin_hijama_ruqyah_approved_info, null);

        TextView nameTv = view.findViewById(R.id.nameTv);
        TextView branchTv = view.findViewById(R.id.branchTv);
        TextView treatmentMethodTv = view.findViewById(R.id.treatmentMethodTv);
        TextView problemTv = view.findViewById(R.id.problemTv);
        Button dateBtn = view.findViewById(R.id.dateBtn);
        Button timeBtn = view.findViewById(R.id.timeBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        PrefUserInfo prefUserInfo = new PrefUserInfo(context);
        String userStatus = prefUserInfo.getUserStatus();
        if (i == 0 && userStatus.equalsIgnoreCase(THREE)) {
            branchTv.setVisibility(View.VISIBLE);
            branchTv.setText(userAppointment.get(i).getBranch());
        }

        nameTv.setText(userAppointment.get(i).getName());
        dateBtn.setText(userAppointment.get(i).getDate());
        treatmentMethodTv.setText(userAppointment.get(i).getTreatment_metod());
        timeBtn.setText(CommonUtils.timeFormatter(userAppointment.get(i).getTime()));
        problemTv.setText(userAppointment.get(i).getProblem());

        dateBtn.setOnClickListener((View v) -> showDateDialogFixedDate(context, dateBtn, dateBtn.getText().toString()));
        timeBtn.setOnClickListener((View v) -> showTimeDialogFormatter(context, timeBtn));

        cancelBtn.setOnClickListener((View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Cancel Appointment?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                        FetchApprovedAppThroughDate fetchApprovedAppThroughDate = new FetchApprovedAppThroughDate(context);
                        fetchApprovedAppThroughDate.execute("delete", userAppointment.get(i).getAppointment_id());
                        viewGroup.getChildAt(i).startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out));
                        userAppointment.remove(i);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (DialogInterface dialog, int id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        return view;
    }
}