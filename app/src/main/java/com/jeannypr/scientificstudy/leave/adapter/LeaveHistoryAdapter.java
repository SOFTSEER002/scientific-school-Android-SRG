package com.jeannypr.scientificstudy.leave.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.UI.ImFlexboxLayout;
import com.jeannypr.scientificstudy.Utilities.OnSwipeTouchListner;
import com.jeannypr.scientificstudy.leave.model.ApproveLeaveInputModel;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LeaveHistoryAdapter extends RecyclerView.Adapter implements Filterable {

    boolean isApprover;
    Context mContext;
    private List<LeaveHistoryModel> history, filteredList;
    public OnItemClickListner listener;
    SimpleDateFormat dateFormatDMY;


    public LeaveHistoryAdapter(Context context, List<LeaveHistoryModel> logs, boolean isApprover, OnItemClickListner listner) {
        super();
        mContext = context;
        history = logs;
        filteredList = logs;
        this.isApprover = isApprover;
        this.listener = listner;
        dateFormatDMY = new SimpleDateFormat("dd MMM, yyyy");

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
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
                .inflate(R.layout.row_leave_history, parent, false);

        return new LeaveHistoryAdapter.MyViewHolder(view, parent);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LeaveHistoryModel route = filteredList.get(position);
        route.AdapterPosition = position;

        try {
            ((MyViewHolder) holder).bind(route, listener);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty())
                    filteredList = history;

                else {
                    ArrayList<LeaveHistoryModel> temp = new ArrayList<>();
                    for (LeaveHistoryModel row : history) {

                        if (row.RequesterName.toLowerCase().contains(charString.toLowerCase())) {
                            temp.add(row);
                        }
                    }

                    filteredList = temp;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<LeaveHistoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        boolean isReasonAccordianOpen = false, isNoteAccordianOpen = false, isPending = true;
        int reasonTotalLines, noteTotalLines, leaveId;
        String reason, approversNote, leaveStatus;

        TextView leaveType, status, sDate, eDate, totalDays, reasonTxt, approversNoteTxt, sliderLbl, teacherName, noteLbl, reasonLbl;
        LinearLayout reasonArrowRow, approversNoteArrowRow;
        ImageView reasonArrowIc, approversNoteArrowIc, leaveTypeIc;
        ConstraintLayout historyRow, slider;
        ImFlexboxLayout approversNoteRow, reasonRow;
        //FloatingActionButton sliderBtn;
        ImageView sliderBtn;
        ViewGroup parent;

        MyViewHolder(View itemView, ViewGroup parent) {
            super(itemView);

            this.parent = parent;
            historyRow = itemView.findViewById(R.id.historyRow);
            leaveType = itemView.findViewById(R.id.leaveType);
            leaveTypeIc = itemView.findViewById(R.id.leaveTypeIc);
            teacherName = itemView.findViewById(R.id.teacherName);

            status = itemView.findViewById(R.id.status);
            sDate = itemView.findViewById(R.id.sDate);
            eDate = itemView.findViewById(R.id.eDate);
            totalDays = itemView.findViewById(R.id.totalDays);

            //  reasonRow = itemView.findViewById(R.id.reasonRow);
            reasonTxt = itemView.findViewById(R.id.reason);
          /*  reasonArrowIc = itemView.findViewById(R.id.reasonArrowIc);
            reasonArrowRow = itemView.findViewById(R.id.reasonArrowRow);*/

            //   approversNoteRow = itemView.findViewById(R.id.approversNoteRow);
            approversNoteTxt = itemView.findViewById(R.id.approversNote);
           /* approversNoteArrowIc = itemView.findViewById(R.id.approversNoteArrowIc);
            approversNoteArrowRow = itemView.findViewById(R.id.approversNoteArrowRow);*/

            slider = itemView.findViewById(R.id.slider);
            sliderBtn = itemView.findViewById(R.id.sliderBtn);
            sliderLbl = itemView.findViewById(R.id.sliderLbl);

            reasonLbl = itemView.findViewById(R.id.reasonLbl);
            noteLbl = itemView.findViewById(R.id.noteLbl);

            if (isApprover) {
                status.setVisibility(View.GONE);
                slider.setVisibility(View.VISIBLE);
                teacherName.setVisibility(View.VISIBLE);

                leaveTypeIc.setVisibility(View.VISIBLE);
                leaveType.setTextColor(mContext.getResources().getColor(R.color.black9));
                //leaveType.setTextSize(R.dimen.sm_size_text);
                //   leaveType.setTextColor(mContext.getResources().getColor(R.color.black));

            } else {
                leaveTypeIc.setVisibility(View.GONE);
                leaveType.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                // leaveType.setTextSize(R.dimen.md_size_text);
                teacherName.setVisibility(View.GONE);
                status.setVisibility(View.VISIBLE);
                slider.setVisibility(View.GONE);
            }
        }

        void bind(final LeaveHistoryModel leave, final LeaveHistoryAdapter.OnItemClickListner itemListner) throws ParseException {
            leaveId = leave.getLeaveId();
            leaveType.setText(leave.getLeaveType());

            String totaldays = leave.getTotalRequestedDays().equals("1") ? leave.getTotalRequestedDays() + " day" : leave.getTotalRequestedDays() + " days";
            totalDays.setText(totaldays);
            ShowLeaveDetail(leave, itemListner);

            teacherName.setText(leave.getRequesterName());

            SimpleDateFormat dfStr = new SimpleDateFormat("MM/dd/yyyy");
            sDate.setText(dateFormatDMY.format(dfStr.parse(leave.getStartDate())));
            eDate.setText(" - " + dateFormatDMY.format(dfStr.parse(leave.getEndDate())));

            switch (leave.getStatus()) {
                case Constants.LeaveRequestStatusVal.APPROVED:
                    leaveStatus = Constants.LeaveRequestStatus.APPROVED;
                    break;

                case Constants.LeaveRequestStatusVal.PENDING:
                    leaveStatus = Constants.LeaveRequestStatus.PENDING;
                    break;

                case Constants.LeaveRequestStatusVal.REJECTED:
                    leaveStatus = Constants.LeaveRequestStatus.REJECTED;
                    break;

                default:
                    leaveStatus = Constants.LeaveRequestStatus.PENDING;
                    break;
            }

            if (!isApprover) {
                SetStatusTxt();

            } else {
                sliderLbl.setText(leaveStatus);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    switch (leaveStatus) {

                        case Constants.LeaveRequestStatus.APPROVED:
                            isPending = true;
                            MoveSliderToRight();
                            slider.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_corner));
                            break;

                        case Constants.LeaveRequestStatus.REJECTED:
                            isPending = true;
                            MoveSliderToRight();
                            slider.setBackground(mContext.getResources().getDrawable(R.drawable.red_bg_round_corner));
                            break;

                        case Constants.LeaveRequestStatus.PENDING:
                            MoveSliderToLeft();
                            slider.setBackground(mContext.getResources().getDrawable(R.drawable.orange_bg_round_corner));
                            break;

                        default:
                            MoveSliderToLeft();
                            slider.setBackground(mContext.getResources().getDrawable(R.drawable.orange_bg_round_corner));
                            break;
                    }

                    //sliderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
                    sliderLbl.setTextColor(mContext.getResources().getColor(R.color.white));
                }

                SetTouchEventOnSlider(itemListner, leave.AdapterPosition);
            }

            approversNote = leave.getApproversNote();
            if (approversNote.equals("")) {
                noteLbl.setVisibility(View.GONE);
                approversNoteTxt.setText("");
            } else {
                noteLbl.setVisibility(View.VISIBLE);
                approversNoteTxt.setText(approversNote);
            }


            reason = leave.getReason();
            if (reason.equals("")) {
                reasonLbl.setVisibility(View.GONE);
                reasonTxt.setText("");
            } else {
                reasonLbl.setVisibility(View.VISIBLE);
                reasonTxt.setText(reason);
            }

            //GetTotalLines(true);

           /* reasonArrowRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isReasonAccordianOpen) {
                        reasonTxt.setMaxLines(1);
                        reasonTxt.setEllipsize(TextUtils.TruncateAt.END);
                        reasonArrowIc.setImageResource(android.R.drawable.arrow_down_float);
                        isReasonAccordianOpen = false;

                        approversNoteRow.setVisibility(View.GONE);

                    } else {
                        reasonTxt.setMaxLines(reasonTotalLines);
                        reasonTxt.setEllipsize(null);
                        reasonTxt.setText(reason);
                        reasonArrowIc.setImageResource(android.R.drawable.arrow_up_float);
                        isReasonAccordianOpen = true;

                        approversNoteRow.setVisibility(View.VISIBLE);
                    }
                }
            });

            approversNoteArrowRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNoteAccordianOpen) {
                        approversNoteTxt.setMaxLines(1);
                        approversNoteTxt.setEllipsize(TextUtils.TruncateAt.END);
                        approversNoteArrowIc.setImageResource(android.R.drawable.arrow_down_float);
                        isNoteAccordianOpen = false;

                    } else {
                        approversNoteTxt.setMaxLines(noteTotalLines);
                        approversNoteTxt.setEllipsize(null);
                        approversNoteTxt.setText(approversNote);
                        approversNoteArrowIc.setImageResource(android.R.drawable.arrow_up_float);
                        isNoteAccordianOpen = true;
                    }
                }
            });*/
        }

        private void ShowLeaveDetail(final LeaveHistoryModel leave, final OnItemClickListner listner) {
            totalDays.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.GetLeaveDays(leave);
                }
            });
        }

        private void SetStatusTxt() {
            status.setText(leaveStatus);
            switch (leaveStatus) {

                case Constants.LeaveRequestStatus.APPROVED:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        status.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_green_bg));
                    }

                      /*  Drawable dApproved = mContext.getResources().getDrawable(R.drawable.tickmark_in_circle);
                        dApproved.setBounds(0, 0, dApproved.getIntrinsicWidth(), dApproved.getIntrinsicHeight());*/
                    //  status.setCompoundDrawables(dApproved, null, null, null);
                    break;

                case Constants.LeaveRequestStatus.PENDING:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        status.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_yellow_bg));
                    }

                       /* Drawable dPending = mContext.getResources().getDrawable(R.drawable.questionmark_in_circle);
                        dPending.setBounds(0, 0, dPending.getIntrinsicWidth(), dPending.getIntrinsicHeight());*/
                    //   status.setCompoundDrawables(dPending, null, null, null);
                    break;

                case Constants.LeaveRequestStatus.REJECTED:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        status.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_red_bg));
                    }
                       /* Drawable dRejected = mContext.getResources().getDrawable(R.drawable.cross_in_circle);
                        dRejected.setBounds(0, 0, dRejected.getIntrinsicWidth(), dRejected.getIntrinsicHeight());*/
                    //   status.setCompoundDrawables(dRejected, null, null, null);
                    break;
            }
        }

        private void GetTotalLines(final boolean isReasonStr) {
            if (isReasonStr) {
                reasonTxt.post(new Runnable() {
                    @Override
                    public void run() {
                        reasonTotalLines = reasonTxt.getLineCount();
                        isTooLarge(reasonRow, reasonTxt, reasonArrowRow, reasonArrowIc, reason, historyRow, isReasonStr);
                    }
                });
            } else {
                approversNoteTxt.post(new Runnable() {
                    @Override
                    public void run() {
                        noteTotalLines = approversNoteTxt.getLineCount();
                        isTooLarge(approversNoteRow, approversNoteTxt, approversNoteArrowRow, approversNoteArrowIc, approversNote, historyRow, isReasonStr);
                    }
                });
            }
        }

        private void isTooLarge(final ImFlexboxLayout accordianRow, final TextView txtView, final LinearLayout arrowRow, final ImageView arrowIc, final String str,
                                final ConstraintLayout parent, final boolean isReason) {

            final boolean[] result = new boolean[1];
            final int[] rowWidth = new int[1];

           /* txtView.post(new Runnable() {
                @Override
                public void run() {
                    totalLines = txtView.getLineCount();
                }
            });*/

            parent.post(new Runnable() {
                public void run() {
                    int containerWidth = parent.getWidth();
                    float textWidth = txtView.getPaint().measureText(str);
                    result[0] = textWidth >= containerWidth;

                    //if toolarge then hide approvers note and show it after opening accordian, else show it and check is it toolarge or not
                    if (textWidth > containerWidth) {
                        txtView.setMaxLines(1);
                        txtView.setEllipsize(TextUtils.TruncateAt.END);
                        arrowIc.setImageResource(android.R.drawable.arrow_down_float);
                        arrowIc.setVisibility(View.VISIBLE);


                    } else {
                        arrowRow.setVisibility(View.GONE);
                        if (isReason) {
                            approversNoteRow.setVisibility(View.VISIBLE);
                        }
                        GetTotalLines(false);
                    }
                    rowWidth[0] = containerWidth;
                }
            });

         /*   arrowRow.post(new Runnable() {
                @Override
                public void run() {
                    int w = arrowRow.getWidth();
                    //   txtView.setMaxWidth(rowWidth[0] - w);
                }
            });*/
        }

        private void SetTouchEventOnSlider(final OnItemClickListner listner, final int position) {

            slider.setOnTouchListener(new OnSwipeTouchListner(mContext) {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return super.onTouch(view, motionEvent);
                }

                @Override
                public void onClick() {
                    super.onClick();
                    // Toast.makeText(context, "Slider Clicked", Toast.LENGTH_SHORT).show();
                    Log.i("Leave History: ", "Slider Clicked");

                    if (leaveStatus.equals(Constants.LeaveRequestStatus.APPROVED) || leaveStatus.equals(Constants.LeaveRequestStatus.REJECTED)) {
                        Toast.makeText(mContext, "Only pending leaves can be approved/rejected.", Toast.LENGTH_SHORT).show();

                    } else {
                        //MoveSliderToRight();
                        ShowDialogForApproversInput(listner, position);
                      /*  if (isPending) {
                            // swipe on right to set status as either Approved or rejected

                            //if approved/rejected is clicked then show confirmation dialog else set as pending
                            //if confirmation is given then save new status else set as pending

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                slider.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_corner));
                                sliderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
                                sliderLbl.setTextColor(mContext.getResources().getColor(R.color.white));
                            }

                            ConstraintSet set2 = new ConstraintSet();
                            set2.clone(slider);
                            set2.connect(sliderLbl.getId(), ConstraintSet.START, slider.getId(), ConstraintSet.START);
                            set2.connect(sliderLbl.getId(), ConstraintSet.END, sliderBtn.getId(), ConstraintSet.START);
                            set2.applyTo(slider);

                            ConstraintSet set = new ConstraintSet();
                            set.clone(slider);
                            set.connect(sliderBtn.getId(), ConstraintSet.END, slider.getId(), ConstraintSet.END, 4);
                            set.clear(sliderBtn.getId(), ConstraintSet.START);
                            set.applyTo(slider);

                            ShowDialogForApproversInput();
                            sliderLbl.setText(leaveStatus);
                            //   isPending = false;
                        }*/
                    }
                }
            });
        }

        private void MoveSliderToRight() {
            if (isPending) {
                // swipe on right to set status as either Approved or rejected
                //if approved/rejected is clicked then show confirmation dialog else set as pending
                //if confirmation is given then save new status else set as pending

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    slider.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_corner));
                    //sliderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
                    sliderLbl.setTextColor(mContext.getResources().getColor(R.color.white));
                }

                ConstraintSet set2 = new ConstraintSet();
                set2.clone(slider);
                set2.connect(sliderLbl.getId(), ConstraintSet.START, slider.getId(), ConstraintSet.START);
                set2.connect(sliderLbl.getId(), ConstraintSet.END, sliderBtn.getId(), ConstraintSet.START);
                set2.applyTo(slider);

                ConstraintSet set = new ConstraintSet();
                set.clone(slider);
                set.connect(sliderBtn.getId(), ConstraintSet.END, slider.getId(), ConstraintSet.END, 4);
                set.clear(sliderBtn.getId(), ConstraintSet.START);
                set.applyTo(slider);

                /* ShowDialogForApproversInput();*/
                sliderLbl.setText(leaveStatus);
                //   isPending = false;
            }
        }

        private void MoveSliderToLeft() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                slider.setBackground(mContext.getResources().getDrawable(R.drawable.orange_bg_round_corner));
                //sliderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
                sliderLbl.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            ConstraintSet set2 = new ConstraintSet();
            set2.clone(slider);
            set2.connect(sliderLbl.getId(), ConstraintSet.END, slider.getId(), ConstraintSet.END);
            set2.connect(sliderLbl.getId(), ConstraintSet.START, sliderBtn.getId(), ConstraintSet.END);
            set2.applyTo(slider);

            ConstraintSet set = new ConstraintSet();
            set.clone(slider);
            set.connect(sliderBtn.getId(), ConstraintSet.START, slider.getId(), ConstraintSet.START, 4);
            set.clear(sliderBtn.getId(), ConstraintSet.END);
            set.applyTo(slider);
        }

        private void ShowDialogForApproversInput(final OnItemClickListner listner, final int position) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
            builder.setCancelable(true);
            //builder.setTitle("Approve/Reject?");

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (approversNote == null || approversNote.equals("") && leaveStatus == null || leaveStatus.equals("")) {
                        approversNote = "";
                        leaveStatus = Constants.LeaveRequestStatus.PENDING;
                        ResetStatus();

                    } else if (leaveStatus == null || leaveStatus.equals("") || leaveStatus.equals(Constants.LeaveRequestStatus.PENDING)) {
                        approversNote = "";
                        leaveStatus = Constants.LeaveRequestStatus.PENDING;
                        ResetStatus();
                    }
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        if (approversNote == null || approversNote.equals("") && leaveStatus == null || leaveStatus.equals("")) {
                            approversNote = "";
                            leaveStatus = Constants.LeaveRequestStatus.PENDING;
                            ResetStatus();
                        } else if (leaveStatus == null || leaveStatus.equals("") || leaveStatus.equals(Constants.LeaveRequestStatus.PENDING)) {
                            approversNote = "";
                            leaveStatus = Constants.LeaveRequestStatus.PENDING;
                            ResetStatus();
                        }
                    }
                });
            }

            View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog, parent, false);
            // Set up the input
            final EditText inputEdt = viewInflated.findViewById(R.id.input);
            Button positiveBtn = viewInflated.findViewById(R.id.positiveBtn);
            Button negativeBtn = viewInflated.findViewById(R.id.negativeBtn);
            TextView titleTxt = viewInflated.findViewById(R.id.title);
            titleTxt.setText("Approve/Reject?");

            inputEdt.setHint("Approver's note");
            builder.setView(viewInflated);


            // Set up the buttons
           /* builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    leaveStatus = Constants.LeaveRequestStatus.APPROVED;

                    approversNote = inputEdt.getText().toString();

                    TakeApproversConfirmation(listner, position);
                    dialog.dismiss();
                }
            });*/

           /* builder.setNegativeButton("Reject rtyrt rerty", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    leaveStatus = Constants.LeaveRequestStatus.REJECTED;

                    approversNote = inputEdt.getText().toString();

                    TakeApproversConfirmation(listner, position);
                    dialog.dismiss();
                }
            });*/

            //  builder.show();
            final AlertDialog alertDialog = builder.create();

            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approversNote = inputEdt.getText().toString();
                    if (approversNote.equals("")) {
                        Toast.makeText(mContext, "Please enter note to approve leave.", Toast.LENGTH_SHORT).show();

                    } else {
                        leaveStatus = Constants.LeaveRequestStatus.APPROVED;
                        TakeApproversConfirmation(listner, position);

                        alertDialog.dismiss();
                    }
                }
            });

            negativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    approversNote = inputEdt.getText().toString();

                    if (approversNote.equals("")) {
                        Toast.makeText(mContext, "Please enter note for rejecting leave.", Toast.LENGTH_SHORT).show();

                    } else {
                        leaveStatus = Constants.LeaveRequestStatus.REJECTED;
                        TakeApproversConfirmation(listner, position);
                        alertDialog.dismiss();
                    }
                }
            });

            alertDialog.show();

        }

        private void ResetStatus() {
            MoveSliderToLeft();

            sliderLbl.setText(Constants.LeaveRequestStatus.PENDING);
            isPending = true;
        }

        private void TakeApproversConfirmation(final OnItemClickListner listner, final int position) {
           /* String note = input[0];
            final String status = input[1];*/

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
            builder.setCancelable(false);
            builder.setTitle("Confirmation!");
            builder.setMessage("Are you sure?");


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    slider.setClickable(false);
                    //   sliderLbl.setText(leaveStatus);

                   /* if (leaveStatus.equals(Constants.LeaveRequestStatus.APPROVED)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            slider.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_corner));
                        }

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            slider.setBackground(mContext.getResources().getDrawable(R.drawable.red_bg_round_corner));
                        }
                    }*/

                    ApproveLeaveInputModel inputModel = new ApproveLeaveInputModel();
                    inputModel.LeaveId = leaveId;
                    inputModel.Reason = approversNote;

                    switch (leaveStatus) {
                        case Constants.LeaveRequestStatus.APPROVED:
                            inputModel.LeaveStatus = Constants.LeaveRequestStatusVal.APPROVED;
                            inputModel.LeaveStatusStr = Constants.LeaveRequestStatus.APPROVED;
                            break;
                        case Constants.LeaveRequestStatus.REJECTED:
                            inputModel.LeaveStatus = Constants.LeaveRequestStatusVal.REJECTED;
                            inputModel.LeaveStatusStr = Constants.LeaveRequestStatus.REJECTED;
                            break;
                        default:
                            inputModel.LeaveStatus = Constants.LeaveRequestStatusVal.PENDING;
                            inputModel.LeaveStatusStr = Constants.LeaveRequestStatus.PENDING;
                            break;
                    }

                    listner.OnApproveReject(inputModel, position);
                    RefreshForm();
                }
            });

                  /*  ApproveLeaveInputModel inputModel = new ApproveLeaveInputModel();
                    inputModel.LeaveId = leaveId;
                    inputModel.Reason = approversNote;

                    switch (leaveStatus) {
                        case Constants.LeaveRequestStatus.APPROVED:
                            inputModel.LeaveStatus = Constants.LeaveRequestStatusVal.APPROVED;
                            break;
                        case Constants.LeaveRequestStatus.REJECTED:
                            inputModel.LeaveStatus = Constants.LeaveRequestStatusVal.REJECTED;
                            break;
                    }

                    listner.OnApproveReject(inputModel);
                    dialog.dismiss();*/


            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    leaveStatus = Constants.LeaveRequestStatus.PENDING;
                    approversNote = "";
                    ResetStatus();
                }
            });

            builder.show();
        }

        private void RefreshForm() {
            //  approversNoteRow.setVisibility(View.GONE);
            approversNoteTxt.setText(approversNote);

           /* reasonTxt.setMaxLines(reasonTotalLines);
            reasonTxt.setEllipsize(null);*/
            reasonTxt.setText(reason);
            //GetTotalLines(true);
        }

        public void SetStatusIfSavesd() {
            if (leaveStatus.equals(Constants.LeaveRequestStatus.APPROVED)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    slider.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_corner));
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    slider.setBackground(mContext.getResources().getDrawable(R.drawable.red_bg_round_corner));
                }
            }

            sliderLbl.setText(leaveStatus);
            slider.setClickable(false);
            RefreshForm();
        }

    }

    public interface OnItemClickListner {
        void OnApproveReject(ApproveLeaveInputModel inputModel, int position);

        void GetLeaveDays(LeaveHistoryModel leave);
    }
}


