package com.jeannypr.scientificstudy.Registration.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.appbar.AppBarLayout;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Registration.api.RegistrationService;
import com.jeannypr.scientificstudy.Registration.model.AcademicYearsBean;
import com.jeannypr.scientificstudy.Registration.model.AcademicYearsModel;
import com.jeannypr.scientificstudy.Registration.model.RegSourceBean;
import com.jeannypr.scientificstudy.Registration.model.RegSourceModel;
import com.jeannypr.scientificstudy.Registration.model.RegistrationFeeBean;
import com.jeannypr.scientificstudy.Registration.model.RegistrationFeeModel;
import com.jeannypr.scientificstudy.Registration.model.TakeRegModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityTakeRegistrationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityTakeRegistrationBinding viewBinding;
    UserModel userModel;
    UserPreference userPref;
    RegistrationService service;
    String dob = "";
    private Context context;
    int selectedPaymentModeId = -1, selectedAcademicId = -1, selectedClassId = -1;
    String selectedRegSourceName;
    private DropDownAdapter paymentModeAdapter, academicYearAdapter, classAdapter, regSourceAdapter;
    ArrayList<DropDownModel> paymentModes, academicYears, classes, regSources;
    TakeRegModel registrationInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_registration);
        context = this;

        userPref = UserPreference.getInstance(this);
        userModel = userPref.getUserData();
        service = new DataRepo<>(RegistrationService.class, this).getService();
        registrationInfoModel = new TakeRegModel();

        setToolbar();
        viewBinding.saveBtn.setOnClickListener(this);
        viewBinding.dobEd.setOnClickListener(this);
        initializePaymentMode();
        initializeAcademicYear();
        initRegSources();
    }

    private void initializePaymentMode() {
        paymentModes = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_PAYMENT_MODE);
        defaultOption.setId(-1);
        paymentModes.add(defaultOption);

        paymentModeAdapter = new DropDownAdapter(this, R.layout.row_spinner, paymentModes);
        viewBinding.paymentModeSpinner.setAdapter(paymentModeAdapter);
        viewBinding.paymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = paymentModeAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1) {
                        selectedPaymentModeId = -1;

                    } else {
                        selectedPaymentModeId = selectedItem.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        GetPaymentModes();
    }

    private void GetPaymentModes() {
        paymentModes.add(new DropDownModel(Constants.PaymentModeId.CASH, Constants.PaymentModes.CASH));
        paymentModes.add(new DropDownModel(Constants.PaymentModeId.CHEQUE, Constants.PaymentModes.CHEQUE));
        paymentModes.add(new DropDownModel(Constants.PaymentModeId.PAYTM, Constants.PaymentModes.PAYTM));
        paymentModes.add(new DropDownModel(Constants.PaymentModeId.NEFT, Constants.PaymentModes.NEFT));
        paymentModes.add(new DropDownModel(Constants.PaymentModeId.ONLINE, Constants.PaymentModes.ONLINE));
        paymentModes.add(new DropDownModel(Constants.PaymentModeId.OTHER, Constants.PaymentModes.OTHER));

        paymentModeAdapter.notifyDataSetChanged();
        viewBinding.paymentModeSpinner.setSelection(Constants.PaymentModeId.CASH);
    }

    private void initDOBPicker() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);

                SimpleDateFormat sdfDMY = new SimpleDateFormat(Constants.DATE_FORMAT_DMY2, Locale.getDefault());
                dob = sdfDMY.format(calendar.getTime());
                viewBinding.dobEd.setText(dob);
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void initializeAcademicYear() {
        academicYears = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_ACADEMIC);
        defaultOption.setId(-1);
        academicYears.add(defaultOption);

        academicYearAdapter = new DropDownAdapter(this, R.layout.row_spinner, academicYears);
        viewBinding.academicYearSpinner.setAdapter(academicYearAdapter);
        viewBinding.academicYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = academicYearAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1)
                        selectedAcademicId = -1;
                    else {
                        selectedAcademicId = selectedItem.getId();
                        initializeClasses();
                        if (selectedClassId != -1 && selectedClassId != 0) getFee();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        getAcademicYear();
    }

    private void getAcademicYear() {
        service.getAcademicYearList(userModel.getSchoolId()).enqueue(new Callback<AcademicYearsBean>() {
            @Override
            public void onResponse(Call<AcademicYearsBean> call, Response<AcademicYearsBean> response) {

                AcademicYearsBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            List<AcademicYearsModel> academics = resp.data;

                            for (AcademicYearsModel datam : academics) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(datam.getId());
                                dropDownModel.setText(datam.getAcademicYearName());
                                academicYears.add(dropDownModel);
                            }
                            academicYearAdapter.notifyDataSetChanged();
                            if (academicYears.size() > 1)
                                viewBinding.academicYearSpinner.setSelection(getAcademicYearIndex(userModel.getAcademicyearId()));

                        } else
                            Toast.makeText(context, getString(R.string.academicyearNotFound), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(context, getString(R.string.academicyearNotFound), Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(context, getString(R.string.academicyearNotFound), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AcademicYearsBean> call, Throwable t) {
                Log.d("Academic list", t.getMessage());
                Toast.makeText(context, getString(R.string.academicyearNotFound), Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getAcademicYearIndex(int itemId) {
        int index = 0;
        boolean match = false;
        for (DropDownModel model : academicYears) {
            if (model.getId() == itemId) {
                match = true;
                break;
            }
            index++;
        }
        return match ? index : 0;
    }

    private void initializeClasses() {
        classes = new ArrayList<>();
        classAdapter = new DropDownAdapter(this, R.layout.row_spinner, classes);
        viewBinding.classSpinner.setAdapter(classAdapter);
        viewBinding.classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = classAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1)
                        selectedClassId = -1;
                    else {
                        selectedClassId = selectedItem.getId();
                        if (selectedAcademicId != -1 && selectedAcademicId != 0) getFee();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        GetClasses();
    }

    private void getFee() {
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        service.getRegistrationFee(selectedClassId, userModel.getSchoolId(), selectedAcademicId).enqueue(new Callback<RegistrationFeeBean>() {
            @Override
            public void onResponse(Call<RegistrationFeeBean> call, Response<RegistrationFeeBean> response) {
                RegistrationFeeBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            RegistrationFeeModel regFee = resp.data;
                            viewBinding.feeEd.setText(regFee.getRegistrationFee().equals("") ? Constants.DEFAULT_REGISTRATION_FEE : regFee.getRegistrationFee());

                        }
//                        else Toast.makeText(context, getString(R.string.regFeeError), Toast.LENGTH_LONG).show();
                    }
//                    else Toast.makeText(context, getString(R.string.regFeeError), Toast.LENGTH_LONG).show();

                }
//                else Toast.makeText(context, getString(R.string.regFeeError), Toast.LENGTH_LONG).show();
                viewBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RegistrationFeeBean> call, Throwable t) {
                viewBinding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, getString(R.string.regFeeError), Toast.LENGTH_LONG).show();
                Log.d("Registartion Fee: ", t.getMessage());
            }
        });
    }

    /**
     * Call class api and populate adapter.
     * Set itemSelectListner.
     */
    public void GetClasses() {
        BaseService BaseService = new DataRepo<>(BaseService.class, this).getService();

        BaseService.getMasterClasses(userModel.getSchoolId(), selectedAcademicId).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        classes.clear();
                        DropDownModel defaultOption = new DropDownModel();
                        defaultOption.setText(Constants.DEFAULT_CLASS);
                        defaultOption.setId(-1);
                        classes.add(defaultOption);

                        if (resp.data != null) {
                            List<ClassModel> allClasses = resp.data;

                            for (ClassModel cls : allClasses) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(cls.Id);
                                dropDownModel.setText(cls.Name);
                                classes.add(dropDownModel);
                            }
                            classAdapter.notifyDataSetChanged();

                        } else
                            Toast.makeText(context, getString(R.string.classNotFound), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(context, getString(R.string.classNotFound), Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(context, getString(R.string.classNotFound), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                Log.d("classList", t.getMessage());
                Toast.makeText(context, getString(R.string.classNotFound), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initRegSources() {
        regSources = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_REG_SOURCE);
        defaultOption.setId(-1);
        regSources.add(defaultOption);

        regSourceAdapter = new DropDownAdapter(this, R.layout.row_spinner, regSources);
        viewBinding.regSourceSpinner.setAdapter(regSourceAdapter);
        viewBinding.regSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = regSourceAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1)
                        selectedRegSourceName = "";
                    else
                        selectedRegSourceName = selectedItem.getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        getRegSources();
    }

    private void getRegSources() {
        service.getRegSource().enqueue(new Callback<RegSourceBean>() {
            @Override
            public void onResponse(Call<RegSourceBean> call, Response<RegSourceBean> response) {

                RegSourceBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            List<RegSourceModel> sources = resp.data;

                            for (RegSourceModel datam : sources) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(datam.getId());
                                dropDownModel.setText(datam.getTitle());
                                regSources.add(dropDownModel);
                            }
                            regSourceAdapter.notifyDataSetChanged();

                        } else
                            Toast.makeText(context, getString(R.string.regSourceNotFound), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(context, getString(R.string.regSourceNotFound), Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(context, getString(R.string.regSourceNotFound), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<RegSourceBean> call, Throwable t) {
                Log.d("Reg. sources list", t.getMessage());
                Toast.makeText(context, getString(R.string.regSourceNotFound), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBtn:
                performValidation();
                break;

            case R.id.dobEd:
                initDOBPicker();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Initialize toolbar.
     * Set title and back button.
     */
    private void setToolbar() {
        setSupportActionBar(viewBinding.customToolbar.toolbar);
        Utility.SetToolbar(this, getString(R.string.takeRegistration), "");
        viewBinding.customToolbar.studentImg.setImageDrawable(getDrawable(R.drawable.ic_registration_bg));

        viewBinding.customToolbar.title.setText(R.string.takeRegistration);
        viewBinding.customToolbar.title.setGradientStartColor(getResources().getColor(R.color.red10));
        viewBinding.customToolbar.title.setGradientEndColor(getResources().getColor(R.color.red9));

        viewBinding.customToolbar.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //expanded
                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_red));
                } else
                    //collapsed
                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_sm));

            }
        });
    }

    /**
     * Perform validation for required fields and display error if empty.
     */
    private void performValidation() {
        clearAllErrors();
        boolean isValid = true;

        if (selectedAcademicId == -1) {
            Toast.makeText(context, R.string.selectAcademic, Toast.LENGTH_LONG).show();
            isValid = false;
        } else registrationInfoModel.setAcademicYearId(selectedAcademicId);

        if (selectedClassId == -1) {
            Toast.makeText(context, R.string.selectClass, Toast.LENGTH_LONG).show();
            isValid = false;
        } else registrationInfoModel.setMasterClassId(selectedClassId);

        if (viewBinding.fnameEd.getText().toString().equals("")) {
            showStudentFNameError();
            isValid = false;
        } else
            registrationInfoModel.setFirstName(viewBinding.fnameEd.getText().toString());

        if (viewBinding.genderSpinner.getSelectedItem().toString().equals("")) {
            isValid = false;
            Toast.makeText(context, R.string.selectGender, Toast.LENGTH_SHORT).show();
        } else
            registrationInfoModel.setGenderName(viewBinding.genderSpinner.getSelectedItem().toString());

        if (dob.equals("")) {
            isValid = false;
            showRegDateError();
        } else
            registrationInfoModel.setRegistrationDate(dob);

        if (viewBinding.fathersNameEd.getText().toString().equals("")) {
            showFatherFNameError();
            isValid = false;
        } else
            registrationInfoModel.setFathersFirstName(viewBinding.fathersNameEd.getText().toString());

        if (viewBinding.mobileEd.getText().toString().equals("") || viewBinding.mobileEd.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
            isValid = false;
            showMobileError();
        } else
            registrationInfoModel.setFatherMobile(viewBinding.mobileEd.getText().toString());

        if (viewBinding.mothersNameEd.getText().toString().equals("")) {
            showMotherFNameError();
            isValid = false;
        } else
            registrationInfoModel.setMothersFirstName(viewBinding.mothersNameEd.getText().toString());

        if (selectedPaymentModeId != -1)
            registrationInfoModel.setPaymentMode(selectedPaymentModeId);

        if (!viewBinding.feeEd.getText().toString().equals(""))
            registrationInfoModel.setRegistrationFee(viewBinding.feeEd.getText().toString());

        if (!viewBinding.paymentNoteEd.getText().toString().equals(""))
            registrationInfoModel.setPaymentNote(viewBinding.paymentNoteEd.getText().toString());

        if (!selectedRegSourceName.equals(""))
            registrationInfoModel.setRegistrationSource(selectedRegSourceName);

        if (!viewBinding.lastNameEd.getText().toString().equals(""))
            registrationInfoModel.setLastName(viewBinding.lastNameEd.getText().toString());

        registrationInfoModel.setSchoolId(userModel.getSchoolId());
        registrationInfoModel.setUserId(userModel.getUserId());

        if (isValid) registration(registrationInfoModel);
    }

    private void clearAllErrors() {
        viewBinding.fnameTv.setErrorEnabled(false);
        viewBinding.mothersNameTv.setErrorEnabled(false);
        viewBinding.fathersNameTv.setErrorEnabled(false);
        viewBinding.mobileTv.setErrorEnabled(false);
        viewBinding.dobTv.setErrorEnabled(false);
    }

    /*
     *Call api and show response/error message.
     * @param input
     */
    private void registration(TakeRegModel input) {
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        viewBinding.saveBtn.setVisibility(View.GONE);

        service.registration(input).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_LONG).show();
                        clearForm();

                    } else
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, response.body().msg, Toast.LENGTH_SHORT).show();
                viewBinding.progressBar.setVisibility(View.GONE);
                viewBinding.saveBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                viewBinding.progressBar.setVisibility(View.GONE);
                viewBinding.saveBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clearForm() {
        registrationInfoModel = new TakeRegModel();
        viewBinding.academicYearSpinner.setSelection(0);
        viewBinding.classSpinner.setSelection(0);
        viewBinding.paymentModeSpinner.setSelection(0);
        viewBinding.regSourceSpinner.setSelection(0);
        viewBinding.genderSpinner.setSelection(0);

        viewBinding.setViewModel(registrationInfoModel);
        viewBinding.executePendingBindings();

        scrollPage();
    }

    private void scrollPage() {
        viewBinding.scroll.smoothScrollTo(0, 0);
    }

    /**
     * Show error for required field.
     */
    private void showStudentFNameError() {
        viewBinding.fnameTv.setErrorEnabled(true);
        viewBinding.fnameTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showFatherFNameError() {
        viewBinding.fathersNameTv.setErrorEnabled(true);
        viewBinding.fathersNameTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showMotherFNameError() {
        viewBinding.mothersNameTv.setErrorEnabled(true);
        viewBinding.mothersNameTv.setError(" "); //getString(R.string.enterMothersName
    }

    /**
     * Show error for required field.
     */
    private void showMobileError() {
        viewBinding.mobileTv.setErrorEnabled(true);
        viewBinding.mobileTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showRegDateError() {
        viewBinding.dobTv.setErrorEnabled(true);
        viewBinding.dobTv.setError(" ");
    }
}
