package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class TakeStudentAttendanceAdapter extends ArrayAdapter<StudentAttendanceModel> implements View.OnClickListener {

    public interface AdapterInterface {
        void attendanceChanged(int totalPresent, int totalAbsent);
    }

    private List<StudentAttendanceModel> dataSet;
    AdapterInterface adapterInterface;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtRoll;
        RadioButton radPresent;
        RadioButton radAbsent;
        RadioGroup radAttendance;
        TextView fatherNameTxt;
    }

    public TakeStudentAttendanceAdapter(Context context, List<StudentAttendanceModel> data, AdapterInterface buttonListener) {
        super(context, R.layout.row_student_take_attendance, data);
        this.dataSet = data;
        this.mContext = context;
        adapterInterface = buttonListener;
    }

    public int getTotalPresent() {
        return 0;
    }

    public int getTotalAbsent() {
        return 0;
    }


    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        StudentAttendanceModel dataModel = getItem(position);


        switch (v.getId()) {
            case R.id.radPresent:
                // dataModel.IsPresent = true;
                dataModel.setPresent(true);
                break;
            case R.id.radAbsent:
                // dataModel.IsPresent = false;
                dataModel.setPresent(false);
                break;
        }
        int totalPresent = 0;
        int totalAbsent = 0;
        for (StudentAttendanceModel studentAttendanceModel : dataSet) {
            if (studentAttendanceModel.getPresent() != null) {
                if (studentAttendanceModel.getPresent() == true) {
                    ++totalPresent;
                } else {
                    ++totalAbsent;
                }
            }

        }
        adapterInterface.attendanceChanged(totalPresent, totalAbsent);
    }

    //  private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StudentAttendanceModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        //   final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_student_take_attendance, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.studentName);
            viewHolder.txtRoll = convertView.findViewById(R.id.studentRoll);
            viewHolder.radAttendance = convertView.findViewById(R.id.radAttendance);
            viewHolder.radPresent = viewHolder.radAttendance.findViewById(R.id.radPresent);
            viewHolder.radAbsent = viewHolder.radAttendance.findViewById(R.id.radAbsent);
            viewHolder.fatherNameTxt = convertView.findViewById(R.id.fatherName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //      result = convertView;
        }

        viewHolder.txtName.setText(dataModel.getName() == null ? "" : dataModel.getName());
        viewHolder.txtRoll.setText(String.valueOf(dataModel.getRoll()));

        String fatherName = dataModel.getFatherName().substring(0, 1).toUpperCase() + dataModel.getFatherName().substring(1).toLowerCase();
        if (position == 0) {
            if (dataModel.getGender().equals(Constants.Gender.FEMALE))
                fatherName = "Daughter of " + fatherName;
            else if (dataModel.getGender().equals(Constants.Gender.MALE))
                fatherName = "Son of " + fatherName;
        } else {
            fatherName = fatherName;
        }

        viewHolder.fatherNameTxt.setText(fatherName);

        if (dataModel.getPresent() == null) {
            viewHolder.radAttendance.clearCheck();

        } else if (dataModel.getPresent()) {
            viewHolder.radAbsent.setChecked(false);
            viewHolder.radPresent.setChecked(true);

        } else if (!dataModel.getPresent()) {
            viewHolder.radAbsent.setChecked(true);
            viewHolder.radPresent.setChecked(false);
        }

        viewHolder.txtName.setTag(position);
        viewHolder.radPresent.setTag(position);
        viewHolder.radAbsent.setTag(position);
        viewHolder.radPresent.setOnClickListener(this);
        viewHolder.radAbsent.setOnClickListener(this);

        return convertView;
    }
}