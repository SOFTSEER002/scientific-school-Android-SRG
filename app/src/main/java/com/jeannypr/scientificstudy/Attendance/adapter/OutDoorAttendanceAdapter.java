package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.ExtraDayReportModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class OutDoorAttendanceAdapter extends RecyclerView.Adapter {

    Context mContext;
    private List<ExtraDayReportModel> extraDayReportModels;

    public OutDoorAttendanceAdapter(Context context, List<ExtraDayReportModel> models) {
        super();
        mContext = context;
        extraDayReportModels = models;

    }

    @Override
    public int getItemCount() {
        return extraDayReportModels.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_out_door_report, parent, false);
        return new OutDoorAttendanceAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ExtraDayReportModel teacherdata = (ExtraDayReportModel) extraDayReportModels.get(position);
        ((MyViewHolder) holder).bind(teacherdata);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayOutDoor, halfdayOutDoor, teacherNameOutDoor, teacherDesigation, teacherNotes;

        MyViewHolder(View itemView) {
            super(itemView);

            dayOutDoor = itemView.findViewById(R.id.dayOutDoor);
            halfdayOutDoor = itemView.findViewById(R.id.halfdayOutDoor);
            teacherNameOutDoor = itemView.findViewById(R.id.teacherNameOutDoor);
           /* teacherDesigation = itemView.findViewById(R.id.teacherDesigation);
            teacherNotes = itemView.findViewById(R.id.teacherNotes);*/


        }

        void bind(final ExtraDayReportModel model) {
          /*  teacherNameOutDoor.setText(model.Name != null ? model.Name : "");
            dayOutDoor.setText(String.valueOf(model.Date));
            halfdayOutDoor.setText(model.IsHalfDay != null ? model.IsHalfDay : "");*/


        }

    }


}


