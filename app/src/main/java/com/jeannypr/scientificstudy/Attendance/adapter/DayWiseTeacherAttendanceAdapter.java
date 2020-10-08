package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class DayWiseTeacherAttendanceAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<TeacherAttendanceModel> dayWiseTeacherAttendanceModels;
    // private UserPreference mUserPref;

    public DayWiseTeacherAttendanceAdapter(Context context, List<TeacherAttendanceModel> collections) {
        super();
        mContext = context;
        dayWiseTeacherAttendanceModels = collections;
        //   mUserPref = UserPreference.getInstance(context);
    }

    @Override
    public int getItemCount() {
        return dayWiseTeacherAttendanceModels.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_day_wise_teacher_attendance, parent, false);
        return new MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TeacherAttendanceModel message = (TeacherAttendanceModel) dayWiseTeacherAttendanceModels.get(position);
        ((MyViewHolder) holder).bind(message);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName, attendanceLbl, teacherdesigation, teacherEamil, teacherPresent, teacherAbsent, teacherHalfDay;
        // RadioButton teacherPresent, teacherAbsent, teacherHalfDay;

        MyViewHolder(View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacherName);
            //teacherdesigation = itemView.findViewById(R.id.teacherdesigation);
            // teacherEamil = itemView.findViewById(R.id.teacherEamil);
            teacherPresent = itemView.findViewById(R.id.teacherPresent);
            teacherAbsent = itemView.findViewById(R.id.teacherAbsent);
            teacherHalfDay = itemView.findViewById(R.id.teacherHalfDay);
        }

        void bind(TeacherAttendanceModel message) {
            teacherName.setText(message.Name == null ? "" : message.Name.substring(0, 1).toUpperCase() + message.Name.substring(1).toLowerCase());
            //    teacherdesigation.setText(message.Designation != null ? message.Designation : "");

            if (message.Attendance != null) {
                switch (message.Attendance) {
                    case Constants.Attendance.PRESENT:
                        teacherPresent.setVisibility(View.VISIBLE);
                        teacherAbsent.setVisibility(View.GONE);
                        teacherHalfDay.setVisibility(View.GONE);
                        teacherPresent.setText("Present");
                       /* teacherPresent.setButtonDrawable(R.drawable.present_checked);
                        teacherAbsent.setButtonDrawable(R.drawable.absent_unchecked);
                        teacherHalfDay.setButtonDrawable(R.drawable.halfday_unchecked);*/
                        break;

                    case Constants.Attendance.ABSENT:
                        teacherPresent.setVisibility(View.GONE);
                        teacherAbsent.setVisibility(View.VISIBLE);
                        teacherHalfDay.setVisibility(View.GONE);
                        teacherAbsent.setText("Absent");
                      /*  teacherPresent.setButtonDrawable(R.drawable.present_unchecked);
                        teacherAbsent.setButtonDrawable(R.drawable.absent_checked);
                        teacherHalfDay.setButtonDrawable(R.drawable.halfday_unchecked);*/
                        break;

                    case Constants.Attendance.HALFDAY:
                        teacherPresent.setVisibility(View.GONE);
                        teacherAbsent.setVisibility(View.GONE);
                        teacherHalfDay.setVisibility(View.VISIBLE);
                        teacherHalfDay.setText("HalfDay");
                        /*teacherPresent.setButtonDrawable(R.drawable.present_unchecked);
                        teacherAbsent.setButtonDrawable(R.drawable.absent_unchecked);
                        teacherHalfDay.setButtonDrawable(R.drawable.halfday_checked);*/
                        break;

                    default:
                        teacherPresent.setVisibility(View.GONE);
                        teacherAbsent.setVisibility(View.GONE);
                        teacherHalfDay.setVisibility(View.GONE);
                       /* teacherPresent.setButtonDrawable(R.drawable.present_unchecked);
                        teacherAbsent.setButtonDrawable(R.drawable.absent_unchecked);
                        teacherHalfDay.setButtonDrawable(R.drawable.halfday_unchecked);*/
                        break;
                }
            } else {
                teacherPresent.setVisibility(View.GONE);
                teacherAbsent.setVisibility(View.GONE);
                teacherHalfDay.setVisibility(View.GONE);
                /*teacherPresent.setButtonDrawable(R.drawable.present_unchecked);
                teacherAbsent.setButtonDrawable(R.drawable.absent_unchecked);
                teacherHalfDay.setButtonDrawable(R.drawable.halfday_unchecked);*/
            }


        }
    }
}


