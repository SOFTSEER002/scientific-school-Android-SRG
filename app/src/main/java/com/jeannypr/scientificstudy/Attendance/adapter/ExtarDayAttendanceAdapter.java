package com.jeannypr.scientificstudy.Attendance.adapter;
/*<Author : babulal*/

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.ExtraDayReportModel;

import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;

public class ExtarDayAttendanceAdapter extends RecyclerView.Adapter implements Filterable {

    Context mContext;
    private ArrayList<ExtraDayReportModel> extraDays;
    private ArrayList<ExtraDayReportModel> extraDaysFiltered;

    public ExtarDayAttendanceAdapter(Context context, ArrayList<ExtraDayReportModel> models) {
        super();
        mContext = context;
        extraDays = models;

    }

    @Override
    public int getItemCount() {
        return extraDays.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    extraDaysFiltered = extraDays;
                } else {
                    ArrayList<ExtraDayReportModel> filteredList = new ArrayList<>();
                    for (ExtraDayReportModel row : extraDays) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDate().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    extraDaysFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = extraDaysFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                extraDaysFiltered = (ArrayList<ExtraDayReportModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
        return new ExtarDayAttendanceAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ExtraDayReportModel teacherdata = (ExtraDayReportModel) extraDays.get(position);
        ((MyViewHolder) holder).bind(teacherdata);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayOutDoor, halfdayOutDoor, teacherNameOutDoor, fulldayOutDoor, teacherDesigation, teacherNotes;

        MyViewHolder(View itemView) {
            super(itemView);

            dayOutDoor = itemView.findViewById(R.id.dayOutDoor);
            halfdayOutDoor = itemView.findViewById(R.id.halfdayOutDoor);
            teacherNameOutDoor = itemView.findViewById(R.id.teacherNameOutDoor);
            fulldayOutDoor = itemView.findViewById(R.id.fulldayOutDoor);
            /*teacherDesigation = itemView.findViewById(R.id.teacherDesigation);
            teacherNotes = itemView.findViewById(R.id.teacherNotes);*/

        }

        void bind(final ExtraDayReportModel model) {
            teacherNameOutDoor.setText(model.getName() != null ? model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1).toLowerCase() : "");
            dayOutDoor.setText(String.valueOf(model.getDate()));
            //halfdayOutDoor.setText(model.IsHalfDay != null ? model.IsHalfDay : "");

         /*   if (model.getIsHalfDay() == 0) {
                halfdayOutDoor.setText("FullDay");
            } else {
                halfdayOutDoor.setText("HalfDay");
            }*/

            if (model.getIsHalfDay() == 0) {
                fulldayOutDoor.setVisibility(View.VISIBLE);
                halfdayOutDoor.setVisibility(View.GONE);
                fulldayOutDoor.setText("FullDay");

            } else {
                halfdayOutDoor.setVisibility(View.VISIBLE);
                fulldayOutDoor.setVisibility(View.GONE);
                halfdayOutDoor.setText("HalfDay");

            }
        }
    }
}


