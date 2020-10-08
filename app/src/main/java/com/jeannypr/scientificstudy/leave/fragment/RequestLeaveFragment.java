package com.jeannypr.scientificstudy.leave.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.TeacherBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.leave.activity.SelectLeaveDaysActivity;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.ApproversBean;
import com.jeannypr.scientificstudy.leave.model.ApproversModel;
import com.jeannypr.scientificstudy.leave.model.LeaveBean;
import com.jeannypr.scientificstudy.leave.model.LeaveModel;
import com.jeannypr.scientificstudy.leave.model.RequestLeaveInputModel;
import com.jeannypr.scientificstudy.leave.model.RequestedLeaveDaysModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestLeaveFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView txtStartDate, txtEndDate, txtRequestedLeaves, requestBtn;
    Calendar cal;
    Context context;
    int selectedSDate, selectedSMonth, selectedSYear, selectedEMonth, selectedEYear, approverId, staffId, leaveId;
    double totalRequestedLeaves;
    final int leaveDaysRequestCode = 100;
    SimpleDateFormat dateFormatMDY, dateFormatDMY, dateFormatDM;
    String startDateMDY, endDateMDY, startDateDMY, endDateDMY, startDateDM, endDateDM, approverName, staffName, leaveName;
    DatePickerDialog eDateDialog;
    DatePickerDialog sDateDialog;
    AlertDialog.Builder tabDialog;
    LayoutInflater inflater;
    ConstraintLayout parent;
    long edateInMilisec, sdateInMilisec;
    LinearLayout tabContainer;
    //    List<String> requestedLeaveDatesArr = new ArrayList<>(); // use arraylist acc to api input format{\"Date\":'10/17/2018',\"DayType\":\"FullDay\",formattedDate: fri, 13 oct 2018}
    ArrayList<RequestedLeaveDaysModel> requestedLeaveDatesArr;
    Date sDateObj;
    boolean isApprover;
    Spinner staffOrApproverList, leaveType;
    DropDownAdapter staffAdapter;
    DropDownAdapter approverAdapter;
    DropDownAdapter leaveAdapter;
    ArrayList<DropDownModel> staff, leaves, approvers;
    DropDownModel selectedStaff, selectedApprover, selectedLeave;
    TeacherService teacherService;
    LeaveService leaveService;
    UserPreference userPref;
    UserModel userData;
    ProgressBar pb;
    EditText leaveReasonEdt, approverNoteTxt;
    ImageView txtSetFullOrHalfDay, approverNoteIc;
    CommunicationWithActivity listner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listner = (CommunicationWithActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    public RequestLeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();

        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        leaveService = new DataRepo<>(LeaveService.class, context).getService();

        requestedLeaveDatesArr = new ArrayList<>();

        approvers = new ArrayList<>();
        staff = new ArrayList<>();
        leaves = new ArrayList<>();

        DropDownModel leaveModel = new DropDownModel();
        leaveModel.setText("Select leave type");
        leaveModel.setId(-1);
        leaves.add(leaveModel);
        leaveAdapter = new DropDownAdapter(context, R.layout.row_spinner, leaves);

        if (bundle != null) {
            isApprover = bundle.getBoolean("isApprover");
        }

        cal = Calendar.getInstance(java.util.TimeZone.getDefault());

        selectedSDate = cal.get(Calendar.DATE);
        selectedSMonth = cal.get(Calendar.MONTH);
        selectedSYear = cal.get(Calendar.YEAR);

        dateFormatDMY = new SimpleDateFormat("dd MM, yyyy");
        dateFormatDM = new SimpleDateFormat("dd MM");
        dateFormatMDY = new SimpleDateFormat("MM/dd/yyyy");

        inflater = LayoutInflater.from(context);

        if (isApprover) {
            DropDownModel staffModel = new DropDownModel();
            staffModel.setText("Select staff");
            staffModel.setId(-1);
            staff.add(staffModel);
            staffAdapter = new DropDownAdapter(context, R.layout.row_spinner, staff);

        } else {
            DropDownModel approverModel = new DropDownModel();
            approverModel.setText("Select approver");
            approverModel.setId(-1);
            approvers.add(approverModel);
            approverAdapter = new DropDownAdapter(context, R.layout.row_spinner, approvers);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_request_leave, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pb = view.findViewById(R.id.pb);
        leaveReasonEdt = view.findViewById(R.id.leaveReason);

        staffOrApproverList = view.findViewById(R.id.staffOrApproverList);
        leaveType = view.findViewById(R.id.leaveType);

        parent = view.findViewById(R.id.requestLeaveFm);
        txtRequestedLeaves = view.findViewById(R.id.requestedLeave);

        txtStartDate = view.findViewById(R.id.startDate);
        txtEndDate = view.findViewById(R.id.endDate);
        txtSetFullOrHalfDay = view.findViewById(R.id.setFullOrHalfDay);

        txtRequestedLeaves.setText("Requested leaves: " + String.valueOf(0));
        txtStartDate.setOnClickListener(this);
        txtEndDate.setOnClickListener(this);
        txtSetFullOrHalfDay.setOnClickListener(this);
        //SetContainerForTabPopup();
        requestBtn = view.findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(this);

        approverNoteTxt = view.findViewById(R.id.approverNote);
        approverNoteIc = view.findViewById(R.id.approverNoteIc);

        if (isApprover) {
            staffOrApproverList.setAdapter(staffAdapter);
            GetStaffList();
            approverNoteTxt.setVisibility(View.VISIBLE);
            approverNoteIc.setVisibility(View.VISIBLE);

        } else {
            staffOrApproverList.setAdapter(approverAdapter);
            GetApproversList();
            approverNoteTxt.setVisibility(View.GONE);
            approverNoteIc.setVisibility(View.GONE);
        }

        leaveType.setAdapter(leaveAdapter);
        GetLeavesList();

        SetListnerForDdl();

    }

    private void SetListnerForDdl() {
        staffOrApproverList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isApprover) {
                    selectedStaff = staffAdapter.getItem(position);
                    if (selectedStaff.getId() == -1) {
                        staffId = 0;
                        staffName = "";
                    } else {
                        staffId = selectedStaff.getId();
                        staffName = selectedStaff.getText();
                    }

                } else {
                    selectedApprover = approverAdapter.getItem(position);

                    if (selectedApprover.getId() == -1) {
                        approverId = 0;
                        approverName = "";
                    } else {
                        approverId = selectedApprover.getId();
                        approverName = selectedApprover.getText();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (isApprover) {
                    approverId = 0;
                    approverName = "";

                } else {
                    staffId = 0;
                    staffName = "";
                }
            }
        });

        leaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedLeave = leaveAdapter.getItem(position);
                if (selectedLeave.getId() == -1) {
                    leaveId = 0;
                    leaveName = "";

                } else {
                    leaveId = selectedLeave.getId();
                    leaveName = selectedLeave.getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                leaveId = 0;
                leaveName = "";
            }
        });
    }

    private void GetLeavesList() {
        leaveService.GetLeaveTypes(userData.getSchoolId()).enqueue(new Callback<LeaveBean>() {
            @Override
            public void onResponse(Call<LeaveBean> call, Response<LeaveBean> response) {

                leaves.clear();
                DropDownModel leaveModel = new DropDownModel();
                leaveModel.setText("Select leave type");
                leaveModel.setId(-1);
                leaves.add(leaveModel);

                LeaveBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            for (LeaveModel approver : resp.data) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(approver.LeaveId);
                                dropDownModel.setText(approver.LeaveName);
                                leaves.add(dropDownModel);
                            }
                            leaveAdapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        //  Toast.makeText(context, "Leaves type list not found.", Toast.LENGTH_LONG).show();

                    } else {
                        //  Toast.makeText(context, "Leaves type list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LeaveBean> call, Throwable t) {
                Log.d("Leaves type List : ", "server call error");
                //  Toast.makeText(context, "Leaves type list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetApproversList() {
        leaveService.GetApproversList(userData.getSchoolId()).enqueue(new Callback<ApproversBean>() {
            @Override
            public void onResponse(Call<ApproversBean> call, Response<ApproversBean> response) {
                approvers.clear();
                DropDownModel approverModel = new DropDownModel();
                approverModel.setText("Select approver");
                approverModel.setId(-1);
                approvers.add(approverModel);

                ApproversBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            for (ApproversModel approver : resp.data) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(approver.ApproverId);
                                dropDownModel.setText(approver.ApproverName);
                                approvers.add(dropDownModel);
                            }
                            approverAdapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "Approvers list not found.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, "Approvers list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApproversBean> call, Throwable t) {
                Log.d("Approvers List : ", "server call error");
                Toast.makeText(context, "Approvers list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetStaffList() {
        teacherService.getTeachers(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {
                staff.clear();
                DropDownModel staffModel = new DropDownModel();
                staffModel.setText("Select staff");
                staffModel.setId(-1);
                staff.add(staffModel);

                TeacherBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            for (TeacherModel teacher : resp.data) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(teacher.UserId);
                                dropDownModel.setText(teacher.Name);
                                staff.add(dropDownModel);
                            }
                            staffAdapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "Teachers list not found.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, "Teachers list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<TeacherBean> call, Throwable t) {
                Log.d("Teacher List : ", "server call error");
                Toast.makeText(context, "Teachers list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:

            /*sdate:

                if edate not null :
                    1. set cal of edatepicker and var
                    2. calculate diff and show tabs
                else :
                    set min of edatepicker


            edate:
                if sdate not null:
                    calculate diff and show tabs
                else:
                    nothing*/

                sDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        sdateInMilisec = cal.getTimeInMillis();

                        selectedSDate = dayOfMonth;
                        selectedSMonth = month;
                        selectedSYear = year;

                        sDateObj = cal.getTime();
                        startDateDMY = dateFormatDMY.format(sDateObj);
                        startDateMDY = dateFormatMDY.format(sDateObj);
                        startDateDM = dateFormatDM.format(sDateObj);

                        txtStartDate.setText(startDateDMY);

                        if (endDateMDY != null) {
                            if (sdateInMilisec != 0 && edateInMilisec != 0) {
                                totalRequestedLeaves = Utility.CountDays(context, edateInMilisec, sdateInMilisec) + 1;

                                if (totalRequestedLeaves < 0) {
                                    eDateDialog.getDatePicker().updateDate(selectedSYear, selectedSMonth, selectedSDate);
                                    txtEndDate.setText(dateFormatDMY.format(cal.getTime()));

                                    endDateMDY = dateFormatMDY.format(cal.getTime());
                                    edateInMilisec = sdateInMilisec;
                                    txtRequestedLeaves.setText("Requested leaves: 1");

                                } else if (totalRequestedLeaves == 0) {
                                    eDateDialog.getDatePicker().updateDate(selectedSYear, selectedSMonth, selectedSDate);
                                    txtEndDate.setText(dateFormatDMY.format(cal.getTime()));

                                    endDateMDY = dateFormatMDY.format(cal.getTime());
                                    edateInMilisec = sdateInMilisec;
                                    txtRequestedLeaves.setText("Requested leaves: 1");

                                } else if (totalRequestedLeaves > 0) {
                                    txtRequestedLeaves.setText("Requested leaves: " + totalRequestedLeaves);
                                }

                                requestedLeaveDatesArr.clear();
                                requestedLeaveDatesArr = SetAllRequestedLeaveDays(sDateObj, totalRequestedLeaves);
                                SetFullOrHalfDay();
                                //  CreateTabUI();
                            }
                        }
                    }
                },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                sDateDialog.show();
                break;

            case R.id.endDate:
                eDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);

                        selectedEMonth = month;
                        selectedEYear = year;

                        edateInMilisec = cal.getTimeInMillis();
                        totalRequestedLeaves = Utility.CountDays(context, edateInMilisec, sdateInMilisec) + 1;
                        txtRequestedLeaves.setText("Requested leaves: " + String.valueOf(totalRequestedLeaves));

                        endDateDMY = dateFormatDMY.format(cal.getTime());
                        endDateDM = dateFormatDM.format(cal.getTime());
                        endDateMDY = dateFormatMDY.format(cal.getTime());

                        txtEndDate.setText(endDateDMY);

                        requestedLeaveDatesArr.clear();
                        requestedLeaveDatesArr = SetAllRequestedLeaveDays(sDateObj, totalRequestedLeaves);
                        SetFullOrHalfDay();
                        //   CreateTabUI();
                    }
                },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));


                if (sdateInMilisec != 0) {
                    eDateDialog.getDatePicker().setMinDate(sdateInMilisec);
                }

                if (startDateMDY != null) {
                    eDateDialog.show();

                } else {
                    Toast.makeText(context, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    break;
                }

                break;

            case R.id.setFullOrHalfDay:
                if (startDateDMY == null || endDateDMY == null) {
                    Toast.makeText(context, "Please select leave dates first.", Toast.LENGTH_SHORT).show();
                    break;

                } else {
                    SetFullOrHalfDay();
                }

                break;

            case R.id.requestBtn:
                if (isApprover) {
                    if (staffId == 0) {
                        Toast.makeText(context, "Please select staff.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (approverNoteTxt.getText().toString().equals("")) {
                        Toast.makeText(context, "Please enter note.", Toast.LENGTH_SHORT).show();
                        break;
                    }

                } else {
                    if (approverId == 0) {
                        Toast.makeText(context, "Please select approver.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (leaveId == 0) {
                    Toast.makeText(context, "Please select leave type.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ((startDateMDY == null || startDateMDY.equals("")) || (endDateMDY == null || endDateMDY.equals(""))) {
                    Toast.makeText(context, "Please select both start and end date.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (requestedLeaveDatesArr.size() < 1) {
                    Toast.makeText(context, "Please select valid start and end date.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (totalRequestedLeaves < 1) {
                    Toast.makeText(context, "Please select valid start and end date.", Toast.LENGTH_SHORT).show();
                    break;
                }

                RequestLeave();
                break;
        }
    }

    private void SetFullOrHalfDay() {
        if (requestedLeaveDatesArr != null && requestedLeaveDatesArr.size() > 0) {
            String selectedDates;

            if (selectedSYear == selectedEYear) {
                if (selectedSMonth == selectedEMonth) {
                    selectedDates = selectedSDate + "-" + endDateDMY;
                } else {
                    selectedDates = startDateDM + "-" + endDateDMY;
                }
            } else {
                selectedDates = startDateDMY + " - " + endDateDMY;
            }

            Intent selectdaysIntent = new Intent(context, SelectLeaveDaysActivity.class);
            selectdaysIntent.putExtra("selectedDates", selectedDates);
            selectdaysIntent.putExtra("totalDays", totalRequestedLeaves);
            selectdaysIntent.putParcelableArrayListExtra("leaveDays", requestedLeaveDatesArr);
            startActivityForResult(selectdaysIntent, leaveDaysRequestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case leaveDaysRequestCode:
                if (resultCode == RESULT_OK) {
                    requestedLeaveDatesArr.clear();
                    requestedLeaveDatesArr = data.getParcelableArrayListExtra("leaveDays");

                    totalRequestedLeaves = requestedLeaveDatesArr.get(0).TotalRequestedDays;
                    txtRequestedLeaves.setText("Requested leaves: " + totalRequestedLeaves);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(context, "Selected days are holidays.", Toast.LENGTH_SHORT).show();
                    totalRequestedLeaves = 0;
                    txtRequestedLeaves.setText("Requested leaves: 0");
                }
                break;
        }
    }

    private void RequestLeave() {
        pb.setVisibility(View.VISIBLE);

        RequestLeaveInputModel input = new RequestLeaveInputModel();
        input.AcademicyearId = userData.getAcademicyearId();
        input.AppliedBy = userData.getUserId();
        input.AppliedFor = isApprover ? staffId : userData.getUserId();
        input.AppliedTo = isApprover ? userData.getUserId() : approverId;
        input.FormatNo = "";
        input.LeaveId = leaveId;
        input.FromDate = startDateMDY;
        input.ToDate = endDateMDY;
        input.LeaveDays = new Gson().toJson(requestedLeaveDatesArr);
        input.Reason = leaveReasonEdt.getText().toString();
        input.SchoolId = userData.getSchoolId();
        input.Requester = userData.getRoleTitle();
        input.ApproverNote = isApprover ? approverNoteTxt.getText().toString() : "";

        leaveService.RequestTeacherLeave(input).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {

                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, "Leave requested successfully.", Toast.LENGTH_SHORT).show();
                        //TODO: send notification to teacher who has applied for leave in case of approver's login.
                        SwitchFragment();

                    } else {
                        Toast.makeText(context, "Failed to request leave.Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Request leave excp:", t.getMessage());
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void SwitchFragment() {
        ClearFormData();
        RequestLeaveFragment.this.onAttach(context);
        TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabhost.getTabAt(0).select();
        listner.RefreshHistoyFrag();
    }

    private void ClearFormData() {
        sdateInMilisec = 0;
        edateInMilisec = 0;
        startDateMDY = null;
        endDateMDY = null;
        totalRequestedLeaves = 0;
        requestedLeaveDatesArr.clear();

        leaveReasonEdt.setText("");
        approverNoteTxt.setText("");
    }

    public interface CommunicationWithActivity {
        void RefreshHistoyFrag();
    }

    public ArrayList<RequestedLeaveDaysModel> SetAllRequestedLeaveDays(Date sDate, double totalRequestedDays) {
//        requestedLeaveDatesArr.clear();
        ArrayList<RequestedLeaveDaysModel> formattedDaysArr = new ArrayList<>();

        SimpleDateFormat sdfEDMY = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
        sdfEDMY.applyPattern("EEE, dd MMM yyyy");

        SimpleDateFormat sdfMDY = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
        sdfMDY.applyPattern("MM/d/yyyy");

        Date nextDate;
        Date temp = sDate;

        for (int i = 0; i < totalRequestedDays; i++) {
            if (i == 0) {
                nextDate = temp;

            } else {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(temp);
                calendar.add(Calendar.DATE, 1);
                nextDate = calendar.getTime();
            }

            //  String nextDateStr = dateFormatDM.format(nextDate);

//            requestedLeaveDatesArr.add(sdf.format(nextDate));
            formattedDaysArr.add(new RequestedLeaveDaysModel(sdfMDY.format(nextDate), Constants.LeaveDayType.FULLDAY, sdfEDMY.format(nextDate),
                    (double) totalRequestedDays, false, ""));
            temp = nextDate;
        }

        return formattedDaysArr;
    }
}
