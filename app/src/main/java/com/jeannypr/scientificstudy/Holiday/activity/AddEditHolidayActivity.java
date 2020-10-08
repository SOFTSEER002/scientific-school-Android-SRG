package com.jeannypr.scientificstudy.Holiday.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.appbar.AppBarLayout;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Holiday.api.HolidayService;
import com.jeannypr.scientificstudy.Holiday.model.HolidayDetailBean;
import com.jeannypr.scientificstudy.Holiday.model.HolidayDetailModel;
import com.jeannypr.scientificstudy.Holiday.model.HolidayInputModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityAddHolidayBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditHolidayActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityAddHolidayBinding viewBinding;
    UserModel userModel;
    UserPreference userPref;
    HolidayService service;
    int selectedAudienceId, selectedHolidayTypeId, holidayId = 0;
    private Context context;
    String eventType;
    ArrayList<DropDownModel> audience;
    private DropDownAdapter audienceAdapter;
    HolidayInputModel holidayInputModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventType = getIntent().getStringExtra(Constants.POST_TYPE);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_holiday);
        context = this;

        userPref = UserPreference.getInstance(this);
        userModel = userPref.getUserData();
        service = new DataRepo<>(HolidayService.class, this).getService();
        holidayInputModel = new HolidayInputModel();

        setToolbar();
        viewBinding.saveBtn.setOnClickListener(this);
        viewBinding.startDateEd.setOnClickListener(this);
        viewBinding.endDateEd.setOnClickListener(this);

//        initializeHolidayType();
        initializeAudience();

        if (getIntent().hasExtra(Constants.HOLIDAY_ID)) {
            holidayId = getIntent().getIntExtra(Constants.HOLIDAY_ID, -1);
            if (holidayId != -1 && holidayId != 0) {
                getHolidayDetails();
                getSupportActionBar().setTitle(R.string.editHoliday);
                viewBinding.customToolbar.title.setText(R.string.editHoliday);
            }
        }

        setDecsScrollListner();
    }

    private void setDecsScrollListner() {
        viewBinding.descEd.setOnTouchListener(new View.OnTouchListener() {  //Register a callback to be invoked when a touch event is sent to this view.
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//Called when a touch event is dispatched to a view. This allows listeners to get a chance to respond before the target view.
                viewBinding.scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeAudience() {
        audience = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_HOLIDAY_FOR);
        defaultOption.setId(-1);
        audience.add(defaultOption);

        audienceAdapter = new DropDownAdapter(this, R.layout.row_spinner, audience);
        viewBinding.audienceSpinner.setAdapter(audienceAdapter);
        viewBinding.audienceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = audienceAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1) {
                        selectedAudienceId = -1;

                    } else {
                        selectedAudienceId = selectedItem.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        getAudience();
    }

    private void getAudience() {
        audience.add(new DropDownModel(Constants.HolidayAudienceID.STUDENT, Constants.HolidayAudience.STUDENT));
        audience.add(new DropDownModel(Constants.HolidayAudienceID.TEACHER, Constants.HolidayAudience.TEACHER));
        audience.add(new DropDownModel(Constants.HolidayAudienceID.BOTH, Constants.HolidayAudience.BOTH));

        audienceAdapter.notifyDataSetChanged();
    }

   /* private void initializeHolidayType() {
        holidayTypes = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_HOLIDAY_TYPE);
        defaultOption.setId(-1);
        holidayTypes.add(defaultOption);

        holidayTypeAdapter = new DropDownAdapter(this, R.layout.row_spinner, holidayTypes);
        viewBinding.holidayTypeSpinner.setAdapter(holidayTypeAdapter);
        viewBinding.holidayTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = holidayTypeAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1)
                        selectedHolidayTypeId = -1;
                    else selectedHolidayTypeId = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        getHolidayType();
    }

    private void getHolidayType() {
        holidayTypes.add(new DropDownModel(Constants.HolidayTypeID.HOLIDAY, Constants.HolidayType.HOLIDAY));
        holidayTypes.add(new DropDownModel(Constants.HolidayTypeID.WEEKEND, Constants.HolidayType.WEEKEND));

        holidayTypeAdapter.notifyDataSetChanged();
    }*/

    /**
     * Get holiday details by ID and update model to bind data to view.
     */
    private void getHolidayDetails() {
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        service.getHolidayDetails(holidayId).enqueue(new Callback<HolidayDetailBean>() {
            @Override
            public void onResponse(Call<HolidayDetailBean> call, Response<HolidayDetailBean> response) {

                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        HolidayDetailModel details = response.body().data;

                        holidayInputModel.setId(holidayId);
                        holidayInputModel.setHolidayFor(details.getHolidayFor());
                        setHolidayForIndex(details.getHolidayFor());

                        holidayInputModel.setHolidayTitle(details.getHolidayTitle());
//                        holidayInputModel.setHolidayType(details.getHolidayType());
//                        setHolidayTypeIndex(details.getHolidayType());

                        holidayInputModel.setDescription(details.getDescription());
                        holidayInputModel.setStartDate(details.getStartDate());
                        holidayInputModel.setEndDate(details.getEndDate());

                        viewBinding.setViewModel(holidayInputModel);
                        viewBinding.executePendingBindings();

                    } else
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();

                viewBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HolidayDetailBean> call, Throwable t) {
                viewBinding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Set index of selected item in Spinner.
     *
     * @param selectedItemId
     */
    private void setHolidayForIndex(int selectedItemId) {
        viewBinding.audienceSpinner.setSelection(getHolidayForIndex(selectedItemId));
    }

    private int getHolidayForIndex(int roleId) {
        int index = 0;
        boolean match = false;
        for (DropDownModel model : audience) {
            if (model.getId() == roleId) {
                match = true;
                break;
            }
            index++;
        }
        return match ? index : 0;
    }

    /**
     * Set index of selected item in Spinner.
     *
     * @param
     */
 /*   private void setHolidayTypeIndex(int selectedItemId) {
        viewBinding.holidayTypeSpinner.setSelection(getHolidayTypeIndex(selectedItemId));
    }

    private int getHolidayTypeIndex(int roleId) {
        int index = 0;
        boolean match = false;
        for (DropDownModel model : holidayTypes) {
            if (model.getId() == roleId) {
                match = true;
                break;
            }
            index++;
        }
        return match ? index : 0;
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBtn:
                performValidation();
                break;

            case R.id.startDateEd:
                initStartDOBPicker();
                break;

            case R.id.endDateEd:
                initEndDOBPicker();
                break;
        }
    }

    /**
     * Initialize toolbar.
     * Set title and back button.
     */
    private void setToolbar() {
        setSupportActionBar(viewBinding.customToolbar.toolbar);
        Utility.SetToolbar(this, getString(R.string.addHoliday), "");
        viewBinding.customToolbar.studentImg.setImageDrawable(getDrawable(R.drawable.ic_holiday_bg));


        viewBinding.customToolbar.title.setText(R.string.Holiday);
        viewBinding.customToolbar.title.setGradientStartColor(getResources().getColor(R.color.green20));
        viewBinding.customToolbar.title.setGradientEndColor(getResources().getColor(R.color.green21));


        viewBinding.customToolbar.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //when toolbar is expanded
                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_green2));

                } else {
                    //collapsed
                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_sm));
                }
            }
        });
    }

    /**
     * Initialize event date
     * set date change listner
     * set selected date on date change.
     */
    private void initStartDOBPicker() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);

                holidayInputModel.setStartDate(Utility.GetFormattedDateMDY(calendar.getTime(), Constants.DATE_FORMAT_MDY));
                viewBinding.startDateEd.setText(Utility.GetFormattedDateMDY(calendar.getTime(), Constants.DATE_FORMAT_DMY));
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    /**
     * Initialize event date
     * set date change listner
     * set selected date on date change.
     */
    private void initEndDOBPicker() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);

                holidayInputModel.setEndDate(Utility.GetFormattedDateMDY(calendar.getTime(), Constants.DATE_FORMAT_MDY));
                viewBinding.endDateEd.setText(Utility.GetFormattedDateMDY(calendar.getTime(), Constants.DATE_FORMAT_DMY));
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    /**
     * Perform validation for required fields and display error if empty.
     */
    private void performValidation() {

        boolean isValid = true;

        if (viewBinding.titleEd.getText().toString().equals("")) {
            showTitleError();
            isValid = false;
        } else
            holidayInputModel.setHolidayTitle(viewBinding.titleEd.getText().toString());

        if (selectedAudienceId == -1) {
            Toast.makeText(context, R.string.selectHolidayFor, Toast.LENGTH_SHORT).show();
            isValid = false;
        } else holidayInputModel.setHolidayFor(selectedAudienceId);

       /* if (selectedHolidayTypeId == -1) {
            Toast.makeText(context, R.string.selectHolidayType, Toast.LENGTH_SHORT).show();
            isValid = false;
        } else*/
            holidayInputModel.setHolidayType(Constants.HolidayTypeID.HOLIDAY);

        if (viewBinding.startDateEd.getText().toString().equals("")) {
            showStartDateError();
            isValid = false;
        }
//        else holidayInputModel.setStartDate(startDate);

        if (viewBinding.endDateEd.getText().toString().equals("")) {
            showEndDateError();
            isValid = false;
        }
//        else holidayInputModel.setEndDate(endDate);

        if (viewBinding.descEd.getText().toString().equals("")) {
            showDescError();
            isValid = false;
        } else
            holidayInputModel.setDescription(viewBinding.descEd.getText().toString());

        holidayInputModel.setSchoolId(userModel.getSchoolId());
        holidayInputModel.setAcademicyearId(userModel.getAcademicyearId());

        if (isValid) saveHoliday(holidayInputModel);
    }

    /*
     *Call api and show response/error message.
     * @param input
     */
    private void saveHoliday(HolidayInputModel input) {
        clearAllErrors();
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        viewBinding.saveBtn.setVisibility(View.GONE);

        service.saveHoliday(input).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_LONG).show();
                        if (holidayId == 0) clearForm();

                    } else
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                viewBinding.progressBar.setVisibility(View.GONE);
                viewBinding.saveBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                viewBinding.progressBar.setVisibility(View.GONE);
                viewBinding.saveBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clearAllErrors() {
        viewBinding.titleTv.setErrorEnabled(false);
        viewBinding.startDateTv.setErrorEnabled(false);
        viewBinding.endDateTv.setErrorEnabled(false);
        viewBinding.desc.setErrorEnabled(false);
    }

    private void clearForm() {
        holidayInputModel = new HolidayInputModel();
        viewBinding.audienceSpinner.setSelection(0);
//        viewBinding.holidayTypeSpinner.setSelection(0);

        viewBinding.setViewModel(holidayInputModel);
        viewBinding.executePendingBindings();

        scrollPage();
    }

    private void scrollPage() {
        viewBinding.scroll.smoothScrollTo(0, 0);
    }

    /**
     * Show error for required field.
     */
    private void showTitleError() {
        viewBinding.titleTv.setErrorEnabled(true);
        viewBinding.titleTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showDescError() {
        viewBinding.desc.setErrorEnabled(true);
        viewBinding.desc.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showStartDateError() {
        viewBinding.startDateTv.setErrorEnabled(true);
        viewBinding.startDateTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showEndDateError() {
        viewBinding.endDateTv.setErrorEnabled(true);
        viewBinding.endDateTv.setError(" ");
    }
}
