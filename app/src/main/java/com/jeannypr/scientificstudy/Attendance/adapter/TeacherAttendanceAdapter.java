package com.jeannypr.scientificstudy.Attendance.adapter;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowTeacherAttendanceBinding;

import java.util.List;

public class TeacherAttendanceAdapter extends RecyclerView.Adapter {

    public interface OnAttendanceChangeListener {
        void attendanceChanged(int totalPresent, int totalAbsent, int totalHalfDay);
    }

    public interface onDbChange{
        void openEdOd();
    }

    private List<TeacherAttendanceModel> dataSet;
    OnAttendanceChangeListener attendanceListener;
    Context mContext;

    public TeacherAttendanceAdapter(Context context, List<TeacherAttendanceModel> data, OnAttendanceChangeListener listener ) {
        super();
        this.dataSet = data;
        this.mContext=context;
        attendanceListener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowTeacherAttendanceBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_teacher_attendance, parent, false);

        return new TeacherAttendanceAdapter.ViewHolder(binding,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TeacherAttendanceModel model = dataSet.get(position);

        holder.setIsRecyclable(false);
        ((ViewHolder) holder).rowTeacherItemBinding.setTeacher(model);//.bind(model,attendanceListener);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public int getTotalPresent(){
        return  0;
    }
    public int getTotalAbsent(){
        return 0;
    }




    public void notifyTotal() {

        int totalPresent = 0;
        int totalAbsent = 0;
        int totalHalfDay = 0;
        for (TeacherAttendanceModel teacherAttendanceModel : dataSet) {
            if(teacherAttendanceModel.Attendance != null){
                if (teacherAttendanceModel.Attendance == 1){
                    ++totalPresent;
                }
                else if(teacherAttendanceModel.Attendance == 0){
                    ++totalAbsent;
                }
                else{
                    ++totalHalfDay;
                }
            }

        }
        attendanceListener.attendanceChanged(totalPresent,totalAbsent, totalHalfDay);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public RowTeacherAttendanceBinding rowTeacherItemBinding;
       // public TeacherAttendanceModel teacherModel;
        ViewHolder(final RowTeacherAttendanceBinding rowTeacherAttendanceBinding, final ViewGroup parent) {
            super(rowTeacherAttendanceBinding.getRoot());
            rowTeacherItemBinding = rowTeacherAttendanceBinding;

          //  teacherModel = rowTeacherItemBinding.getTeacher();
            rowTeacherItemBinding.edodNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rowTeacherAttendanceBinding.getTeacher().IsExtra || rowTeacherAttendanceBinding.getTeacher().IsOutdoor){
                        String notes = rowTeacherItemBinding.getTeacher().Notes;

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(rowTeacherItemBinding.getTeacher().Name);

                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.input_alert_dialog, parent, false);
                        // Set up the input
                        final EditText input = viewInflated.findViewById(R.id.input);
                        input.setHint("Notes for ED/OD");
                        input.setText(notes !=null ? notes : "");
                        builder.setView(viewInflated);

                        // Set up the buttons
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String text = input.getText().toString();
                                rowTeacherItemBinding.getTeacher().Notes = text;

                                rowTeacherItemBinding.edodNotes.setText(text);
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                }
            });

            rowTeacherItemBinding.radAbsent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowTeacherItemBinding.getTeacher().onAttendanceClicked(0);
                    notifyTotal();
                }
            });

            rowTeacherItemBinding.radPresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowTeacherItemBinding.getTeacher().onAttendanceClicked(1);
                    notifyTotal();
                }
            });
            rowTeacherItemBinding.radHalfDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowTeacherItemBinding.getTeacher().onAttendanceClicked(2);
                    notifyTotal();
                }
            });
        }
    }
}