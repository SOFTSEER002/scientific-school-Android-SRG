package com.jeannypr.scientificstudy.Student.activity;

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
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.AdmissionBean;
import com.jeannypr.scientificstudy.Student.model.StudentDetailBean;
import com.jeannypr.scientificstudy.Student.model.StudentDetailModel;
import com.jeannypr.scientificstudy.Student.model.StudentInputModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityAddStudentBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditStudentActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityAddStudentBinding viewBinding;
    UserModel userModel;
    UserPreference userPref;
    StudentService service;
    int selectedClassId, selectedStatusId, studentId=0;
    String selectedStatusName;
    private DropDownAdapter statusAdapter;
    ArrayList<DropDownModel> status;
    private Context context;
    StudentInputModel detailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_student);
        selectedClassId = getIntent().getIntExtra(Constants.CLASS_ID, -1);

        userPref = UserPreference.getInstance(this);
        userModel = userPref.getUserData();
        service = new DataRepo<>(StudentService.class, this).getService();
        detailModel = new StudentInputModel();

        setToolbar();
        viewBinding.saveBtn.setOnClickListener(this);
        initializeStatus();

        if (getIntent().hasExtra(Constants.STUDENT_ID)) {
            studentId = getIntent().getIntExtra(Constants.STUDENT_ID, -1);
            if (studentId != -1 && studentId != 0) {
                viewBinding.admNoEd.setEnabled(false);
                getStudentDetails();
                getSupportActionBar().setTitle(R.string.editStudent);
                viewBinding.customToolbar.title.setText(R.string.editStudent);
            } else getAdmNo();
        } else getAdmNo();
    }

    /**
     * Initialize toolbar.
     * Set title and back button.
     */
    private void setToolbar() {
        setSupportActionBar(viewBinding.customToolbar.toolbar);
        Utility.SetToolbar(this, getString(R.string.addStudent), "");
        viewBinding.customToolbar.studentImg.setImageDrawable(getDrawable(R.drawable.add_student_bg_150));

        viewBinding.customToolbar.title.setText(R.string.addStudent);
        viewBinding.customToolbar.title.setGradientStartColor(getResources().getColor(R.color.purple9));
        viewBinding.customToolbar.title.setGradientEndColor(getResources().getColor(R.color.purple8));

        viewBinding.customToolbar.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //expanded

                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_blue));

                } else {

                    //collapsed
                    getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_sm));
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getAdmNo() {
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        service.getValidAdmissionNo(userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<AdmissionBean>() {
            @Override
            public void onResponse(Call<AdmissionBean> call, Response<AdmissionBean> response) {
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        detailModel.setAdmissionNumber(response.body().data.getAdmissionNo());

                        viewBinding.setViewModel(detailModel);
                        viewBinding.executePendingBindings();

                    } else
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, R.string.admNoNotFoundMsg, Toast.LENGTH_SHORT).show();

                viewBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdmissionBean> call, Throwable t) {
                viewBinding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, R.string.admNoNotFoundMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Call api to get student details by id
     */
    private void getStudentDetails() {
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        service.getStudentDetails(studentId, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<StudentDetailBean>() {
            @Override
            public void onResponse(Call<StudentDetailBean> call, Response<StudentDetailBean> response) {
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {

                        StudentDetailModel details = response.body().data;
                        detailModel.setId(studentId);
                        detailModel.setParentId(details.getParentId());

                        String[] names = Utility.splitStrings(details.getName(), " ", 2);
                        if (names.length > 0) {
                            detailModel.setFirstName(names[0].trim());
                            if (names.length > 1) {
                                detailModel.setLastName(names[1].trim());
                            }

                        }
                        detailModel.setGender(details.getGender());
                        setGenderSelection(details.getGender());
                        detailModel.setAdmissionNumber(details.getAdmissionNo());

                        detailModel.setFatherFirstName(details.getFatherName());
                        detailModel.setFatherMobile(details.getMobileNo());

                        detailModel.setClassId(details.getClassId());
//                        setSelectedClass(details.getClassId());

                        detailModel.setStudentStatus(details.getStudentStatus());
                        setStudentStatusIndex(details.getStudentStatus());

                        viewBinding.setViewModel(detailModel);
                        viewBinding.executePendingBindings();

                    } else
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();

                viewBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StudentDetailBean> call, Throwable t) {
                viewBinding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStudentStatusIndex(int studentStatus) {
        viewBinding.studentStatusSpinner.setSelection(getStudentStatusIndex(studentStatus));
    }

    private int getStudentStatusIndex(int studentStatus) {
        int index = 0;
        boolean match = false;
        for (DropDownModel model : status) {
            if (model.getId() == studentStatus) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.saveBtn:
                performValidation();
                break;
        }
    }

    private void initializeStatus() {
        status = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_STATUS);
        defaultOption.setId(-1);
        status.add(defaultOption);

        statusAdapter = new DropDownAdapter(this, R.layout.row_spinner, status);
        viewBinding.studentStatusSpinner.setAdapter(statusAdapter);
        viewBinding.studentStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = statusAdapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1) {
                        selectedStatusId = -1;
                        selectedStatusName = "";

                    } else {
                        selectedStatusId = selectedItem.getId();
                        selectedStatusName = selectedItem.getText();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        getStudentStatus();
    }

    private void getStudentStatus() {
        DropDownModel model1 = new DropDownModel();
        model1.setText(Constants.StudentStatus.NEW);
        model1.setId(Constants.StudentStatusValues.NEW);
        status.add(model1);

        DropDownModel model2 = new DropDownModel();
        model2.setText(Constants.StudentStatus.PROMOTED);
        model2.setId(Constants.StudentStatusValues.PROMOTED);
        status.add(model2);

        statusAdapter.notifyDataSetChanged();
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
            detailModel.setFirstName(viewBinding.fnameEd.getText().toString().trim());

        if (viewBinding.admNoEd.getText().toString().equals("")) {
            showAdmNoError();
            isValid = false;
        } else
            detailModel.setAdmissionNumber(viewBinding.admNoEd.getText().toString().trim());

        detailModel.setClassId(selectedClassId);

        if (selectedStatusId == -1) {
            Toast.makeText(context, R.string.selectStatus, Toast.LENGTH_LONG).show();
            isValid = false;
        } else
            detailModel.setStudentStatus(selectedStatusId);

        if (viewBinding.fathersNameEd.getText().toString().equals("")) {
            showFathersFirstNameError();
            isValid = false;
        } else
            detailModel.setFatherFirstName(viewBinding.fathersNameEd.getText().toString().trim());

        if (viewBinding.fathersMobileEd.getText().toString().equals("") || viewBinding.fathersMobileEd.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
            showMobileError();
            isValid = false;
        } else
            detailModel.setFatherMobile(viewBinding.fathersMobileEd.getText().toString().trim());

        if (!viewBinding.lastNameEd.getText().toString().equals(""))
            detailModel.setLastName(viewBinding.lastNameEd.getText().toString());

        if (!viewBinding.genderSpinner.getSelectedItem().toString().equals(Constants.DEFAULT_GENDER))
            detailModel.setGender(viewBinding.genderSpinner.getSelectedItem().toString());
        else detailModel.setGender("");

        detailModel.setSchoolId(userModel.getSchoolId());
        detailModel.setAcademicYearId(userModel.getAcademicyearId());
        detailModel.setUserId(userModel.getUserId());
        if (isValid) saveStudent(detailModel);
    }

    private void clearAllErrors() {
        viewBinding.fnameTv.setErrorEnabled(false);
        viewBinding.admNoTv.setErrorEnabled(false);
        viewBinding.fathersNameTv.setErrorEnabled(false);
        viewBinding.mobileTv.setErrorEnabled(false);
    }

    /*
     *Call api and show response/error message.
     * @param detailModel
     */
    private void saveStudent(StudentInputModel input) {
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        viewBinding.saveBtn.setVisibility(View.GONE);

        service.addStudnet(input).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_LONG).show();
                        if(studentId == 0)clearForm();
                    } else
                        Toast.makeText(context, response.body().msg, Toast.LENGTH_SHORT).show();
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

    /**
     * Clear form data after calling api.
     * Get new admission no for next transaction.
     * if it is editing mode, make Adm no. field enable as new student will be added now
     */
    private void clearForm() {
        detailModel = new StudentInputModel();
        setGenderSelection("");
        setStudentStatusIndex(-1);

        viewBinding.setViewModel(detailModel);
        viewBinding.executePendingBindings();

        getAdmNo();
        studentId = -1;
        viewBinding.admNoEd.setEnabled(true);
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
    private void showAdmNoError() {
        viewBinding.admNoTv.setErrorEnabled(true);
        viewBinding.admNoTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showFathersFirstNameError() {
        viewBinding.fathersNameTv.setErrorEnabled(true);
        viewBinding.fathersNameTv.setError(" ");
    }

    /**
     * Show error for required field.
     */
    private void showMobileError() {
        viewBinding.mobileTv.setErrorEnabled(true);
        viewBinding.mobileTv.setError(" ");
    }
}
