package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class TeacherAttendanceAdapter_backup extends ArrayAdapter<TeacherAttendanceModel> {

    public interface AdapterInterface {
        void attendanceChanged(int totalPresent, int totalAbsent, int totalHalfDay);
    }

    private List<TeacherAttendanceModel> dataSet;
    AdapterInterface adapterInterface;
    Context mContext;
    LayoutInflater inflater;
    boolean[] outdoorDutyStatus;
    boolean[] extraDutyStatus;

    // View lookup cache
    static class ViewHolder {
        TextView txtName;
        RadioButton radPresent;
        RadioButton radAbsent;
        RadioButton radHalfDay;
        RadioGroup radAttendance;
        LinearLayout edodContainer;
        CheckBox chkExtraDuty;
        CheckBox chkOutdoorDuty;
        TextView edodNotes;
        int position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        outdoorDutyStatus = new boolean[dataSet.size()];
        extraDutyStatus = new boolean[dataSet.size()];

        for (int i=0;i<dataSet.size();i++) {
            TeacherAttendanceModel datum = dataSet.get(i);
            if(datum.IsExtra == null || datum.IsExtra == false){
                extraDutyStatus[i] = false;
            }else{
                extraDutyStatus[i] = true;
            }

            if(datum.IsOutdoor == null || datum.IsOutdoor == false){
                outdoorDutyStatus[i] = false;
            }else{
                outdoorDutyStatus[i] = true;
            }
        }
    }

    public TeacherAttendanceAdapter_backup(Context context, List<TeacherAttendanceModel> data, AdapterInterface buttonListener ) {
        super(context, R.layout.row_teacher_attendance, data);
        this.dataSet = data;


        this.mContext=context;
        inflater  = LayoutInflater.from(context);
        adapterInterface = buttonListener;
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
        adapterInterface.attendanceChanged(totalPresent,totalAbsent, totalHalfDay);
    }

  //  private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        // getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

      //  final View result;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_teacher_attendance, parent, false);

            viewHolder = new ViewHolder();



            viewHolder.txtName = (TextView) convertView.findViewById(R.id.teacherName);
           // viewHolder.txtRoll = (TextView) convertView.findViewById(R.id.teacherRoll);

            viewHolder.radAttendance = convertView.findViewById(R.id.radAttendance);
            viewHolder.radPresent = convertView.findViewById(R.id.radPresent);
            viewHolder.radAbsent = convertView.findViewById(R.id.radAbsent);
            viewHolder.radHalfDay = convertView.findViewById(R.id.radHalfDay);

            viewHolder.edodContainer = convertView.findViewById(R.id.edodContainer);
            viewHolder.chkExtraDuty = convertView.findViewById(R.id.chkIsExtraDuty);
            viewHolder.chkOutdoorDuty = convertView.findViewById(R.id.chkIsOutdoorDuty);

            viewHolder.edodNotes = convertView.findViewById(R.id.edodNotes);



            convertView.setTag(viewHolder);
            viewHolder.position = position;

            viewHolder.radPresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataSet.get(viewHolder.position).Attendance = 1;

                    viewHolder.radAttendance.clearCheck();
                    viewHolder.radPresent.setChecked(true);
              //      viewHolder.edodContainer.setVisibility(View.VISIBLE);

                  //  notifyDataSetChanged();
                    viewHolder.chkOutdoorDuty.setEnabled(true);
                    viewHolder.chkExtraDuty.setEnabled(true);
                    notifyTotal();
                }
            });
            viewHolder.radAbsent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeacherAttendanceModel model = dataSet.get(viewHolder.position);
                    model.IsOutdoor = false;
                    model.IsExtra = false;
                    model.Attendance = 0;

                    viewHolder.radAttendance.clearCheck();
                    viewHolder.radAbsent.setChecked(true);
                    viewHolder.chkExtraDuty.setChecked(false);
                    viewHolder.chkOutdoorDuty.setChecked(false);

                    viewHolder.chkOutdoorDuty.setEnabled(false);
                    viewHolder.chkExtraDuty.setEnabled(false);
                    extraDutyStatus[viewHolder.position] = false;
                    outdoorDutyStatus[viewHolder.position] = false;

                //    viewHolder.edodNotes.setVisibility(View.GONE);
               //     viewHolder.edodContainer.setVisibility(View.GONE);

                    notifyTotal();
                }
            });
            viewHolder.radHalfDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataSet.get(viewHolder.position).Attendance = 2;
                    viewHolder.radAttendance.clearCheck();
                    viewHolder.radHalfDay.setChecked(true);
                    viewHolder.chkOutdoorDuty.setEnabled(true);
                    viewHolder.chkExtraDuty.setEnabled(true);
                //    viewHolder.edodContainer.setVisibility(View.VISIBLE);
                    notifyTotal();
                }
            });
            viewHolder.chkExtraDuty.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    TeacherAttendanceModel model = dataSet.get(id);
                    if(extraDutyStatus[id] == false){
                        cb.setChecked(true);
                        model.IsExtra = true;
                        extraDutyStatus[id] = true;
                    }
                    else {
                        cb.setChecked(false);
                        model.IsExtra = false;
                        extraDutyStatus[id] = false;
                    }

                    if((model.IsOutdoor!=null && model.IsOutdoor) ||(model.IsExtra != null && model.IsExtra) ){
                  //      viewHolder.edodNotes.setVisibility(View.VISIBLE);
                    }else {
                  //      viewHolder.edodNotes.setVisibility(View.GONE);
                    }
                  //  notifyDataSetChanged();
                }
            });
            viewHolder.chkOutdoorDuty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    TeacherAttendanceModel model = dataSet.get(id);

                    if(outdoorDutyStatus[id] == false){
                        cb.setChecked(true);
                        model.IsOutdoor = true;
                        outdoorDutyStatus[id] = true;
                    }
                    else{
                        cb.setChecked(false);
                        model.IsOutdoor = false;
                        outdoorDutyStatus[id] = false;
                    }

                    if((model.IsOutdoor!=null && model.IsOutdoor) ||(model.IsExtra != null && model.IsExtra) ){
                        //      viewHolder.edodNotes.setVisibility(View.VISIBLE);
                    }else {
                        //      viewHolder.edodNotes.setVisibility(View.GONE);
                    }
                }
            });


            viewHolder.edodNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TeacherAttendanceModel model = dataSet.get(viewHolder.position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(model.Name);

                    View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.input_alert_dialog, parent, false);
                    // Set up the input
                    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                    input.setText(model.Notes !=null ? model.Notes : "");

                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    builder.setView(viewInflated);

                    // Set up the buttons
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            model.Notes = input.getText().toString();
                            viewHolder.edodNotes.setText(model.Notes);
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
            });


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.chkOutdoorDuty.setId(position);
        viewHolder.chkExtraDuty.setId(position);

        TeacherAttendanceModel dataModel = dataSet.get(position);

        viewHolder.txtName.setText(dataModel.Name == null ? "" : dataModel.Name);
        viewHolder.radAttendance.clearCheck();
        if(dataModel.Attendance == null){
            viewHolder.radAttendance.clearCheck();

            viewHolder.chkExtraDuty.setChecked(false);
            viewHolder.chkOutdoorDuty.setChecked(false);
            viewHolder.chkOutdoorDuty.setEnabled(false);
            viewHolder.chkExtraDuty.setEnabled(false);
            viewHolder.edodNotes.setText("");

        //    viewHolder.edodNotes.setVisibility(View.GONE);
        //    viewHolder.edodContainer.setVisibility(View.GONE);

        }
        else if(dataModel.Attendance == 1){
            viewHolder.radAbsent.setChecked(false);
            viewHolder.radPresent.setChecked(true);
            viewHolder.radHalfDay.setChecked(false);
            viewHolder.chkOutdoorDuty.setEnabled(true);
            viewHolder.chkExtraDuty.setEnabled(true);
      //      viewHolder.edodContainer.setVisibility(View.VISIBLE);
        }else if(dataModel.Attendance == 0){
            viewHolder.radAbsent.setChecked(true);
            viewHolder.radPresent.setChecked(false);
            viewHolder.radHalfDay.setChecked(false);
            viewHolder.chkExtraDuty.setChecked(false);
            viewHolder.chkOutdoorDuty.setChecked(false);
            viewHolder.chkOutdoorDuty.setEnabled(false);
            viewHolder.chkExtraDuty.setEnabled(false);
         //   viewHolder.edodNotes.setVisibility(View.GONE);
         //   viewHolder.edodContainer.setVisibility(View.GONE);
        }
        else{
            viewHolder.radAbsent.setChecked(false);
            viewHolder.radPresent.setChecked(false);
            viewHolder.radHalfDay.setChecked(true);
            viewHolder.chkOutdoorDuty.setEnabled(true);
            viewHolder.chkExtraDuty.setEnabled(true);
        //    viewHolder.edodContainer.setVisibility(View.VISIBLE);
        }

        if(dataModel.IsExtra != null && dataModel.IsExtra == true){
         //   viewHolder.edodNotes.setVisibility(View.VISIBLE);
            viewHolder.chkExtraDuty.setChecked(true);
        }
        if(dataModel.IsOutdoor !=null && dataModel.IsOutdoor == true){
         //   viewHolder.edodNotes.setVisibility(View.VISIBLE);
            viewHolder.chkOutdoorDuty.setChecked(true);
        }
        if(dataModel.Notes !=null && !dataModel.Notes.equals("")){
            viewHolder.edodNotes.setText(dataModel.Notes);
        }




        viewHolder.position = position;
       // Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
     //   lastPosition = position;

        return convertView;
    }
}