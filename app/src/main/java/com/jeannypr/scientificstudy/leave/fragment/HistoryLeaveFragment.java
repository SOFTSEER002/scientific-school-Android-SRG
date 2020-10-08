package com.jeannypr.scientificstudy.leave.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.leave.adapter.AvailableLeavesAdapter;
import com.jeannypr.scientificstudy.leave.adapter.LeaveHistoryAdapter;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.ApproveLeaveInputModel;
import com.jeannypr.scientificstudy.leave.model.AvailableLeavesBean;
import com.jeannypr.scientificstudy.leave.model.AvailableLeavesModel;
import com.jeannypr.scientificstudy.leave.model.DetailBean;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryBean;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryModel;
import com.jeannypr.scientificstudy.leave.model.RequestedLeaveDaysModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryLeaveFragment extends Fragment implements View.OnClickListener {

    ImageView viewMoreLessIc;
    boolean isAccordianOpen = false;
    int totalLines;
    TextView sliderLbl, noRecordMsg, incurredLeavesLbl, historyLbl, reasonTxt, totalLeaveRatio;
    FloatingActionButton swipeBtn;
    private boolean isPending = true;
    Context context;
    ConstraintLayout parent, totalLeavesRow, pieChartRowTL, slider;
    String leaveStatus;
    RecyclerView availableLeavesList, leaveLogList;
    AvailableLeavesAdapter availableLeavesAdapter;
    ArrayList<AvailableLeavesModel> availableLeaves;
    LeaveService leaveService;
    UserModel userModel;
    UserPreference pref;
    ProgressBar pb;
    LinearLayout noRecordRow, viewMoreLessRow;
    LeaveHistoryAdapter leaveHistoryAdapter;
    ArrayList<LeaveHistoryModel> leaveLogs;
    boolean isApprover;
    View incurredLeavesDivider, historyDivider, view;
    com.jeannypr.scientificstudy.FloatingActionButton.MovableFloatingActionButton requestLeaveBtn;
    SearchView staffNameFilterSv;

    ProgressBar bg_pb, front_pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isApprover = bundle.getBoolean("isApprover");
        }

        pref = UserPreference.getInstance(context);
        userModel = pref.getUserData();

        availableLeaves = new ArrayList<>();
        availableLeavesAdapter = new AvailableLeavesAdapter(context, availableLeaves);

        leaveLogs = new ArrayList<>();
        leaveHistoryAdapter = new LeaveHistoryAdapter(context, leaveLogs, isApprover, new LeaveHistoryAdapter.OnItemClickListner() {

            @Override
            public void OnApproveReject(final ApproveLeaveInputModel inputModel, final int position) {
                pb.setVisibility(View.VISIBLE);
                inputModel.ApprovedBy = userModel.getUserId();

                leaveService.ApproveOrRejectLeave(inputModel).enqueue(new Callback<Bean>() {
                    @Override
                    public void onResponse(Call<Bean> call, Response<Bean> response) {

                        if (response.body() != null) {
                            LeaveHistoryModel model = new LeaveHistoryModel();

                            if (response.body().rcode == Constants.Rcode.OK) {

                                //inputModel.LeaveStatusStr = Constants.LeaveRequestStatus.APPROVED;
                                model.Status = inputModel.LeaveStatus;
                                model.ApproversNote = inputModel.Reason;
                                leaveHistoryAdapter.notifyItemChanged(position, model);

                                //TODO: send notification to teacher who has applied for leave .
                                Toast.makeText(context, "Leave " + inputModel.LeaveStatusStr + " successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                //   inputModel.LeaveStatusStr = Constants.LeaveRequestStatus.PENDING;
                                model.Status = Constants.LeaveRequestStatusVal.PENDING;
                                model.ApproversNote = "";
                                leaveHistoryAdapter.notifyItemChanged(position, inputModel);

                                Toast.makeText(context, "Failed to " + inputModel.LeaveStatusStr + " Leave", Toast.LENGTH_SHORT).show();
                            }
                        }
                        GetLeaveHistory();
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Bean> call, Throwable t) {
                        Log.e("Approve Leave err: ", t.getMessage());
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, "Something went wrong...Please again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void GetLeaveDays(LeaveHistoryModel leave) {
                if (leave.Detail != null && leave.Detail.size() > 0) {
                    ShowLeaveDetail(leave.Detail);
                } else {
                    GetLeaveDetail(leave);
                }
            }
        });

        leaveService = new DataRepo<>(LeaveService.class, context).getService();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.fragment_history_leaves, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        availableLeavesList = view.findViewById(R.id.availableLeaves);
        incurredLeavesLbl = view.findViewById(R.id.incurredLeavesLbl);
        incurredLeavesDivider = view.findViewById(R.id.incurredLeavesDivider);

        //  staffNameFilterSv = view.findViewById(R.id.staffNameFilter);
       /* if (isApprover) {
            AddListnerOnNameFilter();
        } else {
            staffNameFilterSv.setVisibility(View.GONE);
        }*/


      /*  historyDivider = view.findViewById(R.id.historyDivider);
        historyLbl = view.findViewById(R.id.historyLbl);*/

        pb = view.findViewById(R.id.pb);
        totalLeavesRow = view.findViewById(R.id.totalLeavesRow);
        totalLeaveRatio = view.findViewById(R.id.totalLeaveRatio);
        front_pb = view.findViewById(R.id.front_pb);
        bg_pb = view.findViewById(R.id.bg_pb);
        pieChartRowTL = view.findViewById(R.id.pieChartRowTL);
        totalLeaveRatio = view.findViewById(R.id.totalLeaveRatio);
        // pb.bringToFront();
        //  leaveHistoryPB = view.findViewById(R.id.leaveHistoryPB);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);
        noRecordRow = view.findViewById(R.id.noRecord);

        leaveLogList = view.findViewById(R.id.leaveLogList);
        leaveLogList.setAdapter(leaveHistoryAdapter);

        requestLeaveBtn = view.findViewById(R.id.requestLeaveBtn);
        requestLeaveBtn.setOnClickListener(this);
        GetData();
    }

    private void GetData() {
        if (!isApprover) {
            availableLeavesList.setAdapter(availableLeavesAdapter);
            GetAvailableLeaves();

        } else {
            incurredLeavesLbl.setVisibility(View.GONE);
            incurredLeavesDivider.setVisibility(View.GONE);
            availableLeavesList.setVisibility(View.GONE);
            totalLeavesRow.setVisibility(View.GONE);
        }
        GetLeaveHistory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.requestLeaveBtn:
                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
                tabhost.getTabAt(1).select();
                break;
          /*  case R.id.viewMoreLessRow:

//                boolean b[] = isTooLarge(reasonTxt, str, historyRow1);

                if (b[0]) {
                    reasonTxt.setMaxLines(1);
                    viewMoreLessIc.setImageResource(android.R.drawable.arrow_down_float);
                    isAccordianOpen = false;

                } else {
                    viewMoreLessIc.setVisibility(View.GONE);
                    isAccordianOpen = false;
                }

//                reasonTxt.setText(str);

                if (isAccordianOpen) {
                    reasonTxt.setMaxLines(1);
                    reasonTxt.setEllipsize(TextUtils.TruncateAt.END);
                    viewMoreLessIc.setImageResource(android.R.drawable.arrow_down_float);
                    isAccordianOpen = false;

                } else {

                    //int n = reasonTxt.getLineCount();
                    reasonTxt.setMaxLines(totalLines);
                    reasonTxt.setEllipsize(null);
                    reasonTxt.setText(str);
                    viewMoreLessIc.setImageResource(android.R.drawable.arrow_up_float);
                    isAccordianOpen = true;
                }
                break;*/
        }
    }

    private void GetAvailableLeaves() {
        pb.setVisibility(View.VISIBLE);

        leaveService.GetAvailableLeaves(userModel.getSchoolId(), userModel.getAcademicyearId(), userModel.getUserId()).enqueue(new Callback<AvailableLeavesBean>() {
            @Override
            public void onResponse(Call<AvailableLeavesBean> call, Response<AvailableLeavesBean> response) {
                availableLeaves.clear();
                Double totalIncurred = 0.0, totalEntitled = 0.0;

                AvailableLeavesBean bean = response.body();
                if (bean.rcode != null) {
                    if (bean.rcode == Constants.Rcode.OK) {

                        for (AvailableLeavesModel datum : bean.data) {
                            availableLeaves.add(datum);
                            totalEntitled += datum.getTotalLeaves();
                            totalIncurred += datum.getConsumedLeaves();
                        }
                        availableLeavesAdapter.notifyDataSetChanged();
                        totalLeaveRatio.setText(totalIncurred + "/" + totalEntitled);
                        bg_pb.setProgress(100);
                        front_pb.setProgress((int) Math.round((totalIncurred * 100) / totalEntitled));

                    } else if (bean.rcode == Constants.Rcode.NORECORDS) {
                        Log.e("Available leaves: ", bean.msg);
                        Toast.makeText(context, "Available leaves record not found", Toast.LENGTH_SHORT).show();
                        totalLeavesRow.setVisibility(View.GONE);
                      /*  noRecordMsg.setText("No record found");
                        noRecordRow.setVisibility(View.VISIBLE);*/
                    }
                }
                pb.setVisibility(View.GONE);
                //availableLeavesList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<AvailableLeavesBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                totalLeavesRow.setVisibility(View.GONE);
                //   availableLeavesList.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Something went wrong.Please try again...", Toast.LENGTH_SHORT).show();
                Log.e("Available leaves: ", t.getMessage());
            }
        });
    }

    private void GetLeaveHistory() {
        pb.setVisibility(View.VISIBLE);

        int status = 0, month = 0, teacherId = 0;
        String pageFor;
        // teacherId = isApprover ? 0 : userModel.getUserId();
        pageFor = isApprover ? Constants.LeavePageFor.ADMIN :
                Constants.LeavePageFor.OTHERUSER;

        //pageFor = userModel.getRoleTitle().equals(Constants.Role.ADMIN) ? Constants.LeavePageFor.ADMIN : Constants.LeavePageFor.OTHERUSER;
        String leaveFor = userModel.getRoleTitle().equals(Constants.Role.ADMIN) || userModel.getRoleTitle().equals(Constants.Role.TEACHER) ? Constants.LeaveFor.TEACHER
                : Constants.LeaveFor.STUDENT;


        leaveService.GetTeacherLeaveHistory(userModel.getSchoolId(), userModel.getAcademicyearId(), userModel.getUserId(), status, month, teacherId, pageFor, leaveFor)
                .enqueue(new Callback<LeaveHistoryBean>() {
                    @Override
                    public void onResponse(Call<LeaveHistoryBean> call, Response<LeaveHistoryBean> response) {
                        LeaveHistoryBean bean = response.body();
                        leaveLogs.clear();

                        if (bean.rcode != null) {
                            if (bean.rcode == Constants.Rcode.OK) {

                                for (LeaveHistoryModel datum : bean.data) {
                                    leaveLogs.add(datum);
                                }
                                leaveHistoryAdapter.notifyDataSetChanged();

                            } else if (bean.rcode == Constants.Rcode.NORECORDS) {
                                Log.e("Leave History: ", bean.msg);
                                noRecordMsg.setText("No record found");
                                noRecordRow.setVisibility(View.VISIBLE);
                            }
                        }
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<LeaveHistoryBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, "Something went wrong.Please try again...", Toast.LENGTH_SHORT).show();
                        Log.e("Leave History: ", t.getMessage());
                    }
                });
    }

    private void GetLeaveDetail(final LeaveHistoryModel leave) {
        leaveService.GetLeaveDetail(leave.Id).enqueue(new Callback<DetailBean>() {

            @Override
            public void onResponse(Call<DetailBean> call, Response<DetailBean> response) {
                if (response.body() != null) {

                    DetailBean bean = response.body();
                    if (bean.data != null) {
                        double days = Double.parseDouble(leave.getTotalRequestedDays());

                        try {
                            SetLeaveDaysFormat(bean.data, days, leave.AdapterPosition);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailBean> call, Throwable t) {
                Log.e("Leave detail excep  :", t.getMessage());
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetLeaveDaysFormat(List<RequestedLeaveDaysModel> days, double totalRequestedDays, int position) throws ParseException {
        ArrayList<RequestedLeaveDaysModel> leaveDays = new ArrayList<>();

     /*   RequestLeaveFragment fragment = new RequestLeaveFragment();
        Date sDate = new SimpleDateFormat("dd MMM yyyy").parse(days.get(0).Date);
        fragment.SetAllRequestedLeaveDays(sDate, totalRequestedDays);
*/
        SimpleDateFormat sdfEDMY = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
        sdfEDMY.applyPattern("EEE, dd MM yyyy");

        for (RequestedLeaveDaysModel day : days) {
            Date date = new SimpleDateFormat("dd MMM yyyy").parse(day.Date);
            day.FormattedDate = sdfEDMY.format(date);

            leaveDays.add(day);
        }
//        leaveLogs.get(position).Detail.clear();
        leaveLogs.get(position).Detail = leaveDays;

        ShowLeaveDetail(leaveDays);
    }

    private void ShowLeaveDetail(List<RequestedLeaveDaysModel> days) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setCancelable(true);
        builder.setTitle("Leave Days");

        View inflatedView = LayoutInflater.from(context).inflate(R.layout.leave_detail_alert, parent, false);
        LinearLayout container = inflatedView.findViewById(R.id.container);

        for (RequestedLeaveDaysModel datum : days) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_leave_detail, container, false);

            TextView dateTxt = view.findViewById(R.id.date);
            //    TextView dayTxt = view.findViewById(R.id.day);
            TextView dayTypeTxt = view.findViewById(R.id.dayType);

            dateTxt.setText(datum.FormattedDate);
            dayTypeTxt.setText(datum.DayType);

            container.addView(view);
        }

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setView(inflatedView);
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!isApprover) {
            menu.clear();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (isApprover) {
            MenuItem searchMenu = menu.findItem(R.id.search);
            final SearchView searchView = (SearchView) searchMenu.getActionView();
            // searchView.setIconifiedByDefault(false);

            searchView.setQueryHint("Enter teacher name");
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.onActionViewExpanded();
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    leaveHistoryAdapter.getFilter().filter(newText);
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    searchView.clearFocus();
                    return false;
                }
            });
        }

    }
}
