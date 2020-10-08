package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.CheckableSpinnerAdapter;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.FeeReminderRequest;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentBean;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentModel;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.FragmentFeeReminderBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FeeReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeeReminderFragment extends Fragment implements View.OnClickListener {
    //    private OnFragmentInteractionListener mListener;
    ArrayList<DropDownModel> classes, fromInstallments, toInstallments;
    List<FeeInstallmentModel> installmentList;
    DropDownAdapter classAdapter, fromInstallmentsAdapter, toInstallmentsAdapter;
    private FragmentFeeReminderBinding mViewBinding;
    Context mContext;
    private BroadcastMsgFragment.OnFragmentInteractionListener mListener;
    String selectedClasses = "";
    Boolean isFollowup = false;
    private UserPreference userPref;
    private SchoolDetailModel schoolData;
    private UserModel userModel;
    private final List<CheckableSpinnerAdapter.SpinnerItem<DropDownModel>> spinner_items = new ArrayList<>();
    private final Set<DropDownModel> selected_items = new HashSet<>();
    CheckableSpinnerAdapter adapter;
    DropDownModel selectedToInstallment, selectedFromInstallment;
    String fromInstallmentName, toInstallmentName;
    int fromInstallmentId = 0, toInstallmentId = 0, fromInstallmetNo = 0;
    int totalClassesSelected = 0;

    DatePickerDialog sDateDialog, eDateDialog;
    Calendar cal;
    long edateInMilisec, sdateInMilisec;
    int selectedSDate, selectedSMonth, selectedSYear, selectedEMonth, selectedEYear;
    Date sDateObj;
    String startDateMDY, endDateMDY, startDateDMY, endDateDMY, startDateDM, endDateDM;
    SimpleDateFormat dateFormatMDY, dateFormatDMY, dateFormatDM;
    double totalSelectedDays;

    public FeeReminderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BroadcastMsgFragment.
     */
    public static FeeReminderFragment newInstance() {
        FeeReminderFragment fragment = new FeeReminderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        userPref = UserPreference.getInstance(mContext);
        schoolData = userPref.getSchoolData();
        userModel = userPref.getUserData();

        cal = Calendar.getInstance(java.util.TimeZone.getDefault());

        selectedSDate = cal.get(Calendar.DATE);
        selectedSMonth = cal.get(Calendar.MONTH);
        selectedSYear = cal.get(Calendar.YEAR);

        dateFormatDMY = new SimpleDateFormat(Constants.DATE_FORMAT_DMY, Locale.getDefault());
        dateFormatDM = new SimpleDateFormat(Constants.DATE_FORMAT_DM, Locale.getDefault());
        dateFormatMDY = new SimpleDateFormat(Constants.DATE_FORMAT_MDY, Locale.getDefault());

        classes = new ArrayList<>();
        adapter = new CheckableSpinnerAdapter<>(mContext, Constants.DEFAULT_CLASS, spinner_items, selected_items);
        fromInstallments = new ArrayList<>();
        fromInstallmentsAdapter = new DropDownAdapter(mContext, R.layout.row_spinner, fromInstallments);
        toInstallments = new ArrayList<>();
        toInstallmentsAdapter = new DropDownAdapter(mContext, R.layout.row_spinner, toInstallments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fee_reminder, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewBinding.classSpinner.setAdapter(adapter);

        /*adapter.setOnItemClickListener(new CheckableSpinnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int Count, View view) {
                setSelectedClassIndex();
            }
        });*/
        mViewBinding.fromInstallmentSpinner.setAdapter(fromInstallmentsAdapter);
        mViewBinding.toInstallmentSpinner.setAdapter(toInstallmentsAdapter);

        getClasses();
        getFeesInstallMent();
        setFeeListner();
        setClassListner();
        setRadioButtonListner();
        setDatesListner();
        mViewBinding.sendMsgBtn.setOnClickListener(this);
    }

    private void setDatesListner() {
        mViewBinding.startDate.setOnClickListener(this);
        mViewBinding.endDate.setOnClickListener(this);

        mViewBinding.edStartDate.setOnClickListener(this);
        mViewBinding.edEndDate.setOnClickListener(this);
    }

    /**
     * Call api to get list of classes.
     * Notify adapter.
     */
    public void getClasses() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        BaseService baseService = new DataRepo<>(BaseService.class, mContext).getService();
        baseService.getClasses(userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                spinner_items.clear();

                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<ClassModel> allClasses = resp.data;

                        for (ClassModel cls : allClasses) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(cls.Id);
                            dropDownModel.setText(cls.Name);
                            dropDownModel.setObject(cls);
                            spinner_items.add(new CheckableSpinnerAdapter.SpinnerItem<>(dropDownModel, cls.Name));
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(mContext, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                mViewBinding.pb.setVisibility(View.GONE);
                userModel.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                userModel.setIsLoading(false);
                Toast.makeText(mContext, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Set item click listner.
     */
    private void setClassListner() {
//        mViewBinding.classSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {
//                    setSelectedClassIndex();
//                }
//            }
//        });
    }

    /**
     * Get installments list.
     */
    public void getFeesInstallMent() {

        mViewBinding.pb.setVisibility(View.VISIBLE);
        FeeService feeService = new DataRepo<>(FeeService.class, mContext).getService();

        feeService.GetFeeInstallments(userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<FeeInstallmentBean>() {
            @Override
            public void onResponse(Call<FeeInstallmentBean> call, Response<FeeInstallmentBean> response) {
                FeeInstallmentBean res = response.body();

                if (res != null) {
                    if (res.rcode == Constants.Rcode.OK) {
                        if (res.data != null) {

                            fromInstallments.clear();
                            DropDownModel defaultOption = new DropDownModel();
                            defaultOption.setText("From Installment");
                            defaultOption.setId(0);
                            fromInstallments.add(defaultOption);

                            installmentList = res.data;

                            for (FeeInstallmentModel installment : installmentList) {
                                DropDownModel ddModel = new DropDownModel();
                                ddModel.setId(installment.getId());
                                ddModel.setText(installment.getTitle());
                                ddModel.setObject(installment);
                                fromInstallments.add(ddModel);
                            }

                            fromInstallmentsAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.somethingWrongMsg), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(mContext, getResources().getString(R.string.noRecordMsg), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(mContext, getResources().getString(R.string.somethingWrongMsg), Toast.LENGTH_LONG).show();
                }
                mViewBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeeInstallmentBean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                Toast.makeText(mContext, mContext.getString(R.string.fee_installment_failuer_msg), Toast.LENGTH_LONG).show();
            }
        });
    }

    void setFeeListner() {
        mViewBinding.fromInstallmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                selectedFromInstallment = fromInstallmentsAdapter.getItem(position);

                fromInstallmetNo = 0;
                toInstallments.clear();

                DropDownModel defaultstate = new DropDownModel();
                defaultstate.setText(getString(R.string.default_to_installment));
                defaultstate.setId(0);
                toInstallments.add(defaultstate);

                if (selectedFromInstallment != null) {

                    if (selectedFromInstallment.getId() != 0) {
                        fromInstallmentId = selectedFromInstallment.getId();
                        fromInstallmentName = selectedFromInstallment.getText();
                        FeeInstallmentModel obj = (FeeInstallmentModel) selectedFromInstallment.getObject();
                        fromInstallmetNo = obj.getInstallmentNo();

//                        GetFeesInstallMent();
                        mViewBinding.toInstallmentSpinner.setVisibility(View.VISIBLE);

                        //Get to installment list on the behalf of selected from installment
                        for (FeeInstallmentModel installment : installmentList) {
                            if (installment.getInstallmentNo() >= fromInstallmetNo) {

                                DropDownModel model = new DropDownModel();
                                model.setId(installment.getId());
                                model.setText(installment.getTitle());
                                toInstallments.add(model);
                            }
                        }
                    } else {
                        fromInstallmentId = 0;
                        fromInstallmentName = "";
                    }
                }

                toInstallmentsAdapter.notifyDataSetChanged();
                mViewBinding.toInstallmentSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                fromInstallmentId = 0;
                fromInstallmentName = "";
            }
        });

        mViewBinding.toInstallmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                selectedToInstallment = toInstallmentsAdapter.getItem(position);
                if (selectedToInstallment != null) {
                    if (selectedToInstallment.getId() != 0) {
                        toInstallmentId = selectedToInstallment.getId();
                        toInstallmentName = selectedToInstallment.getText();

                    } else {
                        toInstallmentId = 0;
                        toInstallmentName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                toInstallmentId = 0;
                toInstallmentName = "";
            }
        });
    }

    private void setSelectedClassIndex() {
        ArrayList<String> selectedClassesArr = new ArrayList<>();
        if (selected_items != null && selected_items.size() > 0) {
            for (DropDownModel selectedItem : selected_items) {
                selectedClassesArr.add(String.valueOf(selectedItem.getId()));
            }

            totalClassesSelected = selectedClassesArr.size();

            if (totalClassesSelected > 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    selectedClasses = String.join(",", selectedClassesArr);
                } else {
                    selectedClasses = TextUtils.join(",", selectedClassesArr);
                }
            } else {
                selectedClasses = String.valueOf(selectedClassesArr.get(0));
            }
        }
    }

    /**
     * Set item click listner.
     */
    private void setRadioButtonListner() {
        mViewBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.feeDueRadioBtn:
                        isFollowup = false;
                        break;
                    case R.id.followUpRadioBtn:
                        isFollowup = true;
                        break;
                }
                displayUI();
            }
        });
    }

    private void displayUI() {
        if (isFollowup) {
            mViewBinding.classSpinner.setVisibility(View.INVISIBLE);
            mViewBinding.fromInstallmentSpinner.setVisibility(View.INVISIBLE);
            mViewBinding.toInstallmentSpinner.setVisibility(View.INVISIBLE);

            mViewBinding.startDate.setVisibility(View.VISIBLE);
            mViewBinding.endDate.setVisibility(View.VISIBLE);
        } else {

            mViewBinding.classSpinner.setVisibility(View.VISIBLE);
            mViewBinding.fromInstallmentSpinner.setVisibility(View.VISIBLE);
            mViewBinding.toInstallmentSpinner.setVisibility(View.GONE);

            mViewBinding.fromInstallmentSpinner.setSelection(0);

            mViewBinding.startDate.setVisibility(View.GONE);
            mViewBinding.endDate.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMsgBtn:
                performValidation();
                break;

            case R.id.startDate:
            case R.id.edStartDate:

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

                sDateDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
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

                        mViewBinding.edStartDate.setText(startDateDMY);

                        if (endDateMDY != null) {
                            if (sdateInMilisec != 0 && edateInMilisec != 0) {
                                totalSelectedDays = Utility.CountDays(mContext, edateInMilisec, sdateInMilisec) + 1;

                                if (totalSelectedDays < 0) {
                                    eDateDialog.getDatePicker().updateDate(selectedSYear, selectedSMonth, selectedSDate);
                                    mViewBinding.edEndDate.setText(dateFormatDMY.format(cal.getTime()));

                                    endDateMDY = dateFormatMDY.format(cal.getTime());
                                    edateInMilisec = sdateInMilisec;

                                } else if (totalSelectedDays == 0) {
                                    eDateDialog.getDatePicker().updateDate(selectedSYear, selectedSMonth, selectedSDate);
                                    mViewBinding.edEndDate.setText(dateFormatDMY.format(cal.getTime()));

                                    endDateMDY = dateFormatMDY.format(cal.getTime());
                                    edateInMilisec = sdateInMilisec;

                                }
                            }
                        }
                    }
                },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                sDateDialog.show();
                break;

            case R.id.endDate:
            case R.id.edEndDate:
                eDateDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);

                        selectedEMonth = month;
                        selectedEYear = year;

                        edateInMilisec = cal.getTimeInMillis();
                        totalSelectedDays = Utility.CountDays(mContext, edateInMilisec, sdateInMilisec) + 1;

                        endDateDMY = dateFormatDMY.format(cal.getTime());
                        endDateDM = dateFormatDM.format(cal.getTime());
                        endDateMDY = dateFormatMDY.format(cal.getTime());

                        mViewBinding.edEndDate.setText(endDateDMY);
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
                    Toast.makeText(mContext, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    break;
                }

                break;
        }
    }

    private void performValidation() {
        String msg = mViewBinding.edMsg1.getText().toString();

        if (isFollowup) {
            if (startDateMDY == null || startDateMDY.equals("")) {
                Toast.makeText(mContext, mContext.getString(R.string.sdateValidationMsg), Toast.LENGTH_SHORT).show();
            } else if (endDateMDY == null || endDateMDY.equals("")) {
                Toast.makeText(mContext, mContext.getString(R.string.edateValidationMsg), Toast.LENGTH_SHORT).show();
            } else if (msg.equals(""))
                displayErrorMsg(R.string.enterMsg);
            else sendFeeReminder();

        } else {
            setSelectedClassIndex();
            if (totalClassesSelected < 1)
                displayErrorMsg(R.string.selectClass);
            else if (fromInstallmentId == 0)
                displayErrorMsg(R.string.selectFromInstallment);
            else if (toInstallmentId == 0)
                displayErrorMsg(R.string.selectToInstallment);
            else if (msg.equals(""))
                displayErrorMsg(R.string.enterMsg);
            else sendFeeReminder();
        }
    }

    private void displayErrorMsg(int errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void sendFeeReminder() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        UserModel userModel = UserPreference.getInstance(mContext).getUserData();
        String msg = mViewBinding.edMsg1.getText().toString();

        FeeReminderRequest request = new FeeReminderRequest();
        request.setAcademicYearId(userModel.getAcademicyearId());
        request.setUserId(userModel.getUserId());
        request.setSchoolId(userModel.getSchoolId());

        request.setClassIds(selectedClasses);
        request.setMessage(msg);
        request.setFollowUp(isFollowup);
        request.setFromInstallmentId(fromInstallmentId);
        request.setToInstallmentId(toInstallmentId);

        request.setFromDate(startDateMDY);
        request.setToDate(endDateMDY);


        /*Log.e("Broadcast", "<<In api>> "+userModel.getAcademicyearId()+"\n"+userModel.getUserId()+"\n"+
                userModel.getSchoolId()+"\n"+selectedClasses+"\n"+msg+"\n"+isFollowup+"\n"+fromInstallmentId+"\n"+
                toInstallmentId+"\n"+startDateMDY+"\n"+endDateMDY);*/

        AppSettingService service = new DataRepo<>(AppSettingService.class, mContext).getService();
        service.SendFeeReminder(request).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {

                if (response.body() != null) {
                    //Log.e("Broadcast", "<<pi response>> "+response.body().rcode);
                    if (response.body().rcode == Constants.Rcode.OK) {
                        Toast.makeText(mContext, "" + response.body().msg, Toast.LENGTH_LONG).show();
                        //Toast.makeText(mContext, "Reminder sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "" + response.body().msg, Toast.LENGTH_LONG).show();
                        //Toast.makeText(mContext, mContext.getString(R.string.somethingWrongMsg), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.somethingWrongMsg), Toast.LENGTH_SHORT).show();
                }

                mViewBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                Toast.makeText(mContext, mContext.getString(R.string.somethingWrongMsg), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
