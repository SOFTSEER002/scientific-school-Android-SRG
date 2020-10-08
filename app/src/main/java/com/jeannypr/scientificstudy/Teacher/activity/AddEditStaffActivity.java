package com.jeannypr.scientificstudy.Teacher.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.StaffInputModel;
import com.jeannypr.scientificstudy.Teacher.model.TeacherDetailBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherDetailModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityAddStaffBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditStaffActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityAddStaffBinding viewBinding;
    UserModel userModel;
    UserPreference userPref;
    TeacherService service;
    private Context context;
    int teacherId = 0;
    StaffInputModel staffInputModel;
    ArrayList<DropDownModel> roles;
    private DropDownAdapter roleAdapter;
    int selectedRoleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_staff);
        context = this;

        userPref = UserPreference.getInstance(this);
        userModel = userPref.getUserData();
        service = new DataRepo<>(TeacherService.class, this).getService();
        staffInputModel = new StaffInputModel();

        setToolbar();
        initializeRole();
        viewBinding.saveBtn.setOnClickListener(this);
//        viewBinding.dobEd.setOnClickListener(this);

        if (getIntent().hasExtra(Constants.TEACHER_ID)) {
            teacherId = getIntent().getIntExtra(Constants.TEACHER_ID, -1);
            if (teacherId != -1 && teacherId != 0) {
                getTeacherDetails();
                getSupportActionBar().setTitle(R.string.editStaff);
                viewBinding.customToolbar.title.setText(R.string.editStaff);
            }
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
        Utility.SetToolbar(this, getString(R.string.addStaff), "");
        viewBinding.customToolbar.studentImg.setImageDrawable(getDrawable(R.drawable.ic_enquiry_bg));

        viewBinding.customToolbar.title.setText(R.string.addStaff);
        viewBinding.customToolbar.title.setGradientStartColor(getResources().getColor(R.color.green18));
        viewBinding.customToolbar.title.setGradientEndColor(getResources().getColor(R.color.green17));

        viewBinding.customToolbar.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //expanded
                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_green));

                } else {
                    //collapsed
                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_sm));
                }
            }
        });
    }

    private void initializeRole() {
        roles = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_ROLE);
        defaultOption.setId(-1);
        roles.add(defaultOption);

        roleAdapter = new DropDownAdapter(this, R.layout.row_spinner, roles);
        viewBinding.roleSpinner.setAdapter(roleAdapter);
        viewBinding.roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = roleAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1) {
                        selectedRoleId = -1;

                    } else {
                        selectedRoleId = selectedItem.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        getRoles();
    }

    private void getRoles() {
        roles.add(new DropDownModel(Constants.TeacherRoleID.STAFF, Constants.TeacherRole.STAFF));
        roles.add(new DropDownModel(Constants.TeacherRoleID.ADMIN, Constants.TeacherRole.ADMIN));
        roles.add(new DropDownModel(Constants.TeacherRoleID.PRINCIPAL, Constants.TeacherRole.PRINCIPAL));
//        roles.add(new DropDownModel(Constants.TeacherRoleID.ALUMNI, Constants.TeacherRole.ALUMNI));

        roleAdapter.notifyDataSetChanged();
    }

    /**
     * Get teacher details by ID and update model to bind data to view.
     */
    private void getTeacherDetails() {
        viewBinding.progressBar.setVisibility(View.VISIBLE);

        service.getTeacherDetails(teacherId).enqueue(new Callback<TeacherDetailBean>() {
            @Override
            public void onResponse(Call<TeacherDetailBean> call, Response<TeacherDetailBean> response) {

                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {

                        TeacherDetailModel details = response.body().data;

                        staffInputModel.setId(teacherId);
                        staffInputModel.setFirstName(details.getFirstName());
                        staffInputModel.setLastName(details.getLastName());
                        staffInputModel.setGender(details.getGender());
                        setGenderSelection(details.getGender());

                        staffInputModel.setDateOfBirth(details.getDateOfBirth());
                        staffInputModel.setRole(details.getRole());
                        setSelectedRole(details.getRole());
                        staffInputModel.setMobile(details.getMobile());

                        viewBinding.setViewModel(staffInputModel);
                        viewBinding.executePendingBindings();

                    } else
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();

                viewBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TeacherDetailBean> call, Throwable t) {
                viewBinding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Set index of selected item in Spinner.
     *
     * @param roleId
     */
    private void setSelectedRole(int roleId) {
        viewBinding.roleSpinner.setSelection(getRoleIndex(roleId));
    }

    private int getRoleIndex(int roleId) {
        int index = 0;
        boolean match = false;
        for (DropDownModel model : roles) {
            if (model.getId() == roleId) {
                match = true;
                break;
            }
            index++;
        }
        return match ? index : 0;
    }

    private void setGenderSelection(String gender) {
        int index = 0;
        switch (gender) {
            case Constants.Gender.MALE:
                index = 1;
                break;

            case Constants.Gender.FEMALE:
                index = 2;
                break;

            case Constants.Gender.OTHER:
                index = 3;
                break;
        }
        viewBinding.genderSpinner.setSelection(index);
    }

   /* private void initDOBPicker() {
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
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBtn:
                performValidation();
                break;

          /*  case R.id.dobEd:
                initDOBPicker();
                break;*/
        }
    }

    /**
     * Perform validation for required fields and display error if empty.
     */
    private void performValidation() {
        clearAllErrors();
        boolean isValid = true;

        if (viewBinding.fnameEd.getText().toString().equals("")) {
            showFirstNameError();
            isValid = false;
        } else
            staffInputModel.setFirstName(viewBinding.fnameEd.getText().toString());

        if (!viewBinding.lastNameEd.getText().toString().equals(""))
            staffInputModel.setLastName(viewBinding.lastNameEd.getText().toString());

        if (viewBinding.genderSpinner.getSelectedItem().toString().equals(Constants.DEFAULT_GENDER)) {
            isValid = false;
            Toast.makeText(context, R.string.selectGender, Toast.LENGTH_SHORT).show();
        } else
            staffInputModel.setGender(viewBinding.genderSpinner.getSelectedItem().toString());

        if (selectedRoleId == -1) {
            isValid = false;
            Toast.makeText(context, R.string.selectRole, Toast.LENGTH_SHORT).show();
        } else
            staffInputModel.setRole(selectedRoleId);

        if (!viewBinding.mobileEd.getText().toString().equals("") && viewBinding.mobileEd.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
            showMobileError();
            isValid = false;
        } else
            staffInputModel.setMobile(viewBinding.mobileEd.getText().toString());

        staffInputModel.setDateOfBirth("");
        staffInputModel.SchoolId = userModel.getSchoolId();
        staffInputModel.CreatedBy = userModel.getUserId();

        if (isValid) saveStaff(staffInputModel);
    }

    private void clearAllErrors() {
        viewBinding.fnameTv.setErrorEnabled(false);
        viewBinding.fnameTv.requestFocus();
//        Utility.
    }

    /*
     *Call api and show response/error message.
     * @param staffInputModel
     */
    private void saveStaff(StaffInputModel staffInputModel) {
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        viewBinding.saveBtn.setVisibility(View.GONE);

        service.saveTeacher(staffInputModel).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK)
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_SHORT).show();
                }

                viewBinding.progressBar.setVisibility(View.GONE);
                viewBinding.saveBtn.setVisibility(View.VISIBLE);
                if (teacherId == 0) clearForm();
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                viewBinding.progressBar.setVisibility(View.GONE);
                viewBinding.saveBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clearForm() {
        staffInputModel = new StaffInputModel();
        setGenderSelection("");
        setSelectedRole(0);

        viewBinding.setViewModel(staffInputModel);
        viewBinding.executePendingBindings();
        scrollPage();
    }

    private void scrollPage() {
        viewBinding.scroll.smoothScrollTo(0, 0);
    }

    /**
     * Show error for required field.
     */
    private void showFirstNameError() {
        viewBinding.fnameTv.setErrorEnabled(true);
        viewBinding.fnameTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showMobileError() {
        viewBinding.mobileTv.setErrorEnabled(true);
        viewBinding.mobileTv.setError(" ");
    }
}
