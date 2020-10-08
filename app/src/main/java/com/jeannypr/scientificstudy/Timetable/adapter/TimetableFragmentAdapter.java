package com.jeannypr.scientificstudy.Timetable.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Timetable.model.PeriodModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimetableFragmentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PeriodModel> itemsList;
    private UserPreference mUserPref;
    private UserModel userModel;
    String timetableOf;

    public TimetableFragmentAdapter(Context context, List<PeriodModel> items, String timetable) {
        super();
        mContext = context;
        itemsList = items;
        mUserPref = UserPreference.getInstance(context);
        userModel = mUserPref.getUserData();
        timetableOf = timetable;
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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
                .inflate(R.layout.row_timetable, parent, false);
        return new TimetableFragmentAdapter.MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PeriodModel item = (PeriodModel) itemsList.get(position);
        ((TimetableFragmentAdapter.MyViewHolder) holder).bind(item);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPeriodNumber, txtSubject, txtTeacher, txtFrom, txtTo;
        int initialVal = 0;
        AtomicInteger counter = new AtomicInteger(initialVal);

        MyViewHolder(View itemView) {
            super(itemView);
            txtPeriodNumber = itemView.findViewById(R.id.periodNumber);
            txtSubject = itemView.findViewById(R.id.subject);
            txtTeacher = itemView.findViewById(R.id.teacher);
            txtTo = itemView.findViewById(R.id.to);
            txtFrom = itemView.findViewById(R.id.from);
        }

        void bind(final PeriodModel item) {
            initialVal = counter.incrementAndGet();
            int redColor = mContext.getResources().getColor(R.color.red4);
            int blackColor = mContext.getResources().getColor(R.color.black9);

            txtPeriodNumber.setText(String.valueOf("0" + (getAdapterPosition() + 1)));

            String endTime = item.EndTime != null ? item.EndTime : "";
            txtTo.setText(endTime);
            String startTime = item.StartTime != null ? item.StartTime : "";
            txtFrom.setText(startTime);

            if (item.SubjectName == null || item.SubjectName.equals("") || item.SubjectName.toLowerCase().equals("not assigned")) {
                txtSubject.setText("Subject: NA");
                txtSubject.setTextColor(redColor);
            } else {
                txtSubject.setText(item.SubjectName);
                txtSubject.setTextColor(blackColor);
            }

            switch (userModel.getRoleTitle()) {
                case Constants.Role.PARENT:
                    if (item.TeacherName == null || item.TeacherName.equals("") || item.TeacherName.toLowerCase().equals("not assigned")) {
                        txtTeacher.setText("Teacher: NA");
                        txtTeacher.setTextColor(redColor);
                    } else {
                        txtTeacher.setText(item.TeacherName);
                        txtTeacher.setTextColor(blackColor);
                    }
                    break;

                case Constants.Role.TEACHER:
                case Constants.Role.ADMIN:
                    if (Constants.TimetableOf.STUDENT.equals(timetableOf)) {
                        if (item.TeacherName == null || item.TeacherName.equals("") || item.TeacherName.toLowerCase().equals("not assigned")) {
                            txtTeacher.setText("Teacher: NA");
                            txtTeacher.setTextColor(redColor);
                        } else {
                            txtTeacher.setText(item.TeacherName);
                            txtTeacher.setTextColor(blackColor);
                        }
                    } else {
                    if (item.ClassName == null || item.ClassName.equals("") || item.ClassName.toLowerCase().equals("not assigned")) {
                        txtTeacher.setText("Class: NA");
                        txtTeacher.setTextColor(redColor);
                    } else {
                        txtTeacher.setText(item.ClassName);
                        txtTeacher.setTextColor(blackColor);
                    }
                    }
                    break;
            }


        }
    }
}
