package com.jeannypr.scientificstudy.Teacher.activity;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceJsonModel;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceSaveModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.SelfAttendanceBean;
import com.jeannypr.scientificstudy.Teacher.model.SelfAttendanceModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfAttendanceActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    ProgressBar progressBar;
    private View bar;
    private Animation animation;

    private UserPreference userPreference;
    private UserModel userModel;
    private Button halfDayBtn;
    LinearLayout fingerPrintScanner;
    Calendar calendar;
    DateFormat df, dt;

    int hour, minute, second;
    private TeacherService teacherService;
    private SelfAttendanceModel selfAttendanceModel;
    private CheckBox chkIsHalfday, chkIsExtraDuty, outdoorBtn, extraDutyBtn;
    private String edOdNotes, todaysDate;
    private int todaysAttendance;
    private Boolean isOutdoor, isExtraduty;
    private FloatingActionButton saveSelfattendanceBtn;
    private RelativeLayout takeSelfAttendanceSection, selfAttendanceStatusSection;
    private Button attendanceStatus;
    private EditText txtNote;
    private TextView edOdVal, noteVal, attendanceDate, attendanceTime, edOdLbl, noteLbl, scannerBtnCaption, txtTodaysDate, presentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_attendance);
        context = this;
        userPreference = UserPreference.getInstance(this);
        userModel = userPreference.getUserData();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        df = new SimpleDateFormat("dd-MMM-yyyy");
        dt = new SimpleDateFormat("hh:mm a");
        teacherService = new DataRepo<>(TeacherService.class, context).getService();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Self Attendance", "");
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Self Attendance");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        progressBar = findViewById(R.id.progressBar);
        selfAttendanceStatusSection = findViewById(R.id.selfAttendanceStatusSection);
        takeSelfAttendanceSection = findViewById(R.id.takeSelfAttendanceSection);
        presentBtn = findViewById(R.id.presentBtn);

//        fingerPrintScanner = (LinearLayout) findViewById(R.id.fingerPrintScanner);
//        bar = findViewById(R.id.bar);
//        scannerBtnCaption = findViewById(R.id.scannerBtnCaption);
        txtTodaysDate = findViewById(R.id.todaysDate);
        chkIsHalfday = findViewById(R.id.chkIsHalfday);
        saveSelfattendanceBtn = (FloatingActionButton) findViewById(R.id.saveSelfattendanceBtn);
        saveSelfattendanceBtn.setEnabled(false);
        //   currentTime = findViewById(R.id.currentTime);
        todaysAttendance = Constants.Attendance.ABSENT;
        todaysDate = df.format(calendar.getTime());
        attendanceStatus = findViewById(R.id.attendanceStatus);
        edOdLbl = findViewById(R.id.edOdLbl);
        edOdVal = findViewById(R.id.edOdVal);
        noteLbl = findViewById(R.id.noteLbl);
        noteVal = findViewById(R.id.noteVal);
        attendanceDate = findViewById(R.id.attendanceDate);
        attendanceTime = findViewById(R.id.attendanceTime);
        chkIsExtraDuty = findViewById(R.id.chkIsExtraDutyOrOutdoor);
        outdoorBtn = findViewById(R.id.outdoorBtn);
        extraDutyBtn = findViewById(R.id.extraDutyBtn);
        txtNote = findViewById(R.id.txtNote);

//        animation = AnimationUtils.loadAnimation(SelfAttendanceActivity.this, R.anim.finger_print_scanning);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                bar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });

//        if (ActivityCompat.checkSelfPermission(SelfAttendanceActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SelfAttendanceActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(SelfAttendanceActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            return;
//        } else {
//            // Write you code here if permission already given.
//        }

//        fingerPrintScanner.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    bar.setVisibility(View.VISIBLE);
//                    bar.startAnimation(animation);
//                    //Vibrate
//                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
//                    vibrator.vibrate(1000);
//                    scannerBtnCaption.setText("Lift your finger off the scanner");
//
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    bar.setVisibility(View.INVISIBLE);
//                    animation.cancel();
//                }
//
//                return false;
//            }
//        });

        presentBtn.setOnClickListener(this);
        chkIsHalfday.setOnClickListener(this);
        chkIsHalfday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    todaysAttendance = Constants.Attendance.HALFDAY;

                } else {
                    todaysAttendance = Constants.Attendance.PRESENT;

                }
            }
        });


        //Check whether attendance is taken or not
        progressBar.setVisibility(View.VISIBLE);
        teacherService.GetSelfAttendanceOfToday(userModel.getUserId(), userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<SelfAttendanceBean>() {
            @Override
            public void onResponse(Call<SelfAttendanceBean> call, Response<SelfAttendanceBean> response) {
                SelfAttendanceBean selfAttendanceBean = response.body();


                if (selfAttendanceBean != null) {
                    if (selfAttendanceBean.rcode == Constants.Rcode.OK) {

                        if (selfAttendanceBean.data != null) {
                            selfAttendanceModel = selfAttendanceBean.data;

                            if (selfAttendanceModel.Attendance == null) {
                                txtTodaysDate.setText(todaysDate);
                                //.setVisibility(View.VISIBLE);
                            } else {

                                txtTodaysDate.setVisibility(View.GONE);
                                takeSelfAttendanceSection.setEnabled(false);
                                chkIsHalfday.setEnabled(false);
                                //   fingerPrintScanner.setEnabled(false);
                                presentBtn.setEnabled(false);
                                saveSelfattendanceBtn.setEnabled(false);
                                chkIsHalfday.setTextColor(getResources().getColor(R.color.black2));
                                String btnLbl, isEdOd;

                                if (selfAttendanceModel.Attendance.equals(String.valueOf(Constants.Attendance.HALFDAY))) {
                                    btnLbl = "Halfday";
                                    attendanceStatus.setTextColor(getResources().getColor(R.color.white));
                                    attendanceStatus.setBackgroundColor(getResources().getColor(R.color.yellow));

                                } else if (selfAttendanceModel.Attendance.equals(String.valueOf(Constants.Attendance.PRESENT))) {
                                    btnLbl = "Present";
                                    attendanceStatus.setTextColor(getResources().getColor(android.R.color.white));
                                    attendanceStatus.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

                                } else {
                                    btnLbl = "Absent";
                                    attendanceStatus.setTextColor(getResources().getColor(android.R.color.white));
                                    attendanceStatus.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                                }

                                if (!selfAttendanceModel.IsExtra && !selfAttendanceModel.IsOutDoor) {
                                    edOdVal.setVisibility(View.GONE);
                                    edOdLbl.setVisibility(View.GONE);
                                    noteLbl.setVisibility(View.GONE);
                                    noteVal.setVisibility(View.GONE);
                                } else if (selfAttendanceModel.IsExtra && selfAttendanceModel.IsOutDoor) {
                                    isEdOd = "Extra Duty" + " and " + "OutDoor Duty";
                                    edOdVal.setText(isEdOd);
                                    noteVal.setText(selfAttendanceModel.AttendanceNote != null ? selfAttendanceModel.AttendanceNote : "");
                                } else {
                                    isEdOd = selfAttendanceModel.IsExtra ? "Extra Duty" : "OutDoor Duty";
                                    edOdVal.setText(isEdOd);
                                    noteVal.setText(selfAttendanceModel.AttendanceNote != null ? selfAttendanceModel.AttendanceNote : "");
                                }

                                attendanceStatus.setText(btnLbl);
                                attendanceDate.setText(todaysDate);
                                attendanceTime.setText(selfAttendanceModel.AttendanceTime != null ? selfAttendanceModel.AttendanceTime : "");

                                selfAttendanceStatusSection.setVisibility(View.VISIBLE);
                            }
                        }

                    } else if (selfAttendanceBean.rcode == Constants.Rcode.NORECORDS) {
                        //TODO :
                        Toast.makeText(context, "No Record found.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Today's attendance could not be loaded. Try again!", Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SelfAttendanceBean> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Today's attendance could not be loaded. Try again!", Toast.LENGTH_LONG).show();
            }
        });

        saveSelfattendanceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.presentBtn:
                // if (todaysAttendance == Constants.Attendance.ABSENT || todaysAttendance == -1) {
                todaysAttendance = Constants.Attendance.PRESENT;
                chkIsHalfday.setEnabled(true);
                chkIsExtraDuty.setVisibility(View.VISIBLE);
                //   }

                showAlertBox();
                break;

//            case R.id.chkIsHalfday:
//                todaysAttendance = Constants.Attendance.HALFDAY;
//                //showAlertBox();
//                break;

            case R.id.saveSelfattendanceBtn:

                TeacherAttendanceJsonModel item = new TeacherAttendanceJsonModel();
                if (todaysAttendance != -1) {
                    item.Id = userModel.getUserId();
                    item.Attendance = todaysAttendance;
                    item.IsExtra = isExtraduty;
                    item.OutDoor = isOutdoor;
                    item.Notes = txtNote.getText().toString();
                }

                TeacherAttendanceSaveModel model = new TeacherAttendanceSaveModel();
                // model.ClassId = classId;
                model.SchoolId = userModel.getSchoolId();
                model.AcademicyearId = userModel.getAcademicyearId();
                model.CreatedBy = userModel.getUserId();
                model.Day = calendar.get(Calendar.DAY_OF_MONTH);
                model.Month = calendar.get(Calendar.MONTH) + 1;
                model.Year = calendar.get(Calendar.YEAR);
                model.AttendanceArr = new Gson().toJson(item);

                progressBar.setVisibility(View.VISIBLE);
                teacherService.SaveSelfAttendanceOfToday(model).enqueue(new Callback<SelfAttendanceBean>() {
                    @Override
                    public void onResponse(Call<SelfAttendanceBean> call, Response<SelfAttendanceBean> response) {
                        SelfAttendanceBean selfAttendanceBean = response.body();
                        if (selfAttendanceBean.rcode == Constants.Rcode.OK) {
                            Toast.makeText(context, "Attendance saved successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Attendance could not be saved. Try again!", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SelfAttendanceBean> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Attendance could not be saved. Try again!", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

    private void showAlertBox() {
        saveSelfattendanceBtn.setEnabled(true);

//        ConstraintLayout parent = findViewById(R.id.selfAttendance);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Extra Duty/Outdoor Duty");

        //    View viewInflated = LayoutInflater.from(context).inflate(R.layout.alert_self_attendance_extraduty, parent, false);
        // Set up the input
//        final CheckBox chkIsExtraDuty = (CheckBox) viewInflated.findViewById(R.id.chkIsExtraDutyOrOutdoor);
//        final CheckBox outdoorBtn = (CheckBox) viewInflated.findViewById(R.id.outdoorBtn);
//        final CheckBox extraDutyBtn = (CheckBox) viewInflated.findViewById(R.id.extraDutyBtn);
//        final EditText note = (EditText) viewInflated.findViewById(R.id.note);
//        final CheckBox chkIsExtraDuty = findViewById(R.id.chkIsExtraDutyOrOutdoor);
//        final CheckBox outdoorBtn =findViewById(R.id.outdoorBtn);
//        final CheckBox extraDutyBtn = findViewById(R.id.extraDutyBtn);
//        final EditText txtNote = findViewById(R.id.note);


        //   edOdNotes = txtNote.getText().toString();

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        // builder.setView(viewInflated);

        // Set up the buttons
//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();

        chkIsExtraDuty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    outdoorBtn.setVisibility(View.VISIBLE);
                    extraDutyBtn.setVisibility(View.VISIBLE);
                    txtNote.setVisibility(View.VISIBLE);
                } else {
                    outdoorBtn.setVisibility(View.GONE);
                    extraDutyBtn.setVisibility(View.GONE);
                    txtNote.setVisibility(View.GONE);
                }
            }
        });

        outdoorBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOutdoor = isChecked;
            }
        });

        extraDutyBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isExtraduty = isChecked;
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
