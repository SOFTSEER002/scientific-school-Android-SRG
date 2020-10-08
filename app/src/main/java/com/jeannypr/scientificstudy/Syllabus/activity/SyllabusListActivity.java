package com.jeannypr.scientificstudy.Syllabus.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Syllabus.adapter.SyllabusListAdapter;
import com.jeannypr.scientificstudy.Syllabus.model.SyllabusBean;
import com.jeannypr.scientificstudy.Syllabus.model.SyllabusResponse;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivitySyllabusBinding;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyllabusListActivity extends AppCompatActivity implements SyllabusListAdapter.OnItemClickListener {
    private SyllabusListAdapter mAdapter;
    private Context mContext;
    private ArrayList<SyllabusResponse> mSyllabusList;
    private ActivitySyllabusBinding mViewBinding;
    CwHwService classworkService;
    UserPreference userPref;
    UserModel userData;
    ArrayList<DropDownModel> classes;
    DropDownAdapter classAdapter;
    BaseService baseService;
    int classId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_syllabus);
        mContext = this;

        classworkService = new DataRepo<>(CwHwService.class, mContext).getService();
        baseService = new DataRepo<>(BaseService.class, mContext).getService();
        userPref = UserPreference.getInstance(mContext);
        userData = userPref.getUserData();

        getPermission();
        setToolbar();
        initSyllabusList();
        mViewBinding.academic.setText(getString(R.string.academicYear) + " " + userData.getAcademicYearName());

        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {
            mViewBinding.classList.setVisibility(View.VISIBLE);
            initClass();
        } else {
            mViewBinding.classList.setVisibility(View.GONE);
            getSyllabusList();
        }
    }

    private void getPermission() {
        if (!Utility.isReadAndWriteStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        } else {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.Directory.Base);
            if (!directory.exists()) {
                boolean isDirMade = directory.mkdirs();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Initialize toolbar
     * Set title and subtitle.
     */
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(mContext, getString(R.string.syllabus), "");
    }

    private void initSyllabusList() {
        mSyllabusList = new ArrayList<>();
        mAdapter = new SyllabusListAdapter(mContext, mSyllabusList, this, userData.getRoleTitle());
        mViewBinding.syllabusRV.setAdapter(mAdapter);
    }

    public void getSyllabusList() {
        mViewBinding.progressBar.setVisibility(View.VISIBLE);

        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {
            classworkService.getSyllabusForAdmin(classId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<SyllabusBean>() {
                @Override
                public void onResponse(Call<SyllabusBean> call, Response<SyllabusBean> response) {

                    mSyllabusList.clear();
                    SyllabusBean classworkBean = response.body();

                    if (classworkBean != null) {

                        if (classworkBean.rcode.equals(Constants.Rcode.OK)) {
                            if (classworkBean.data != null && classworkBean.data.size() > 0) {
                                for (SyllabusResponse classwork : classworkBean.data) {
                                    mSyllabusList.add(classwork);
                                }
                                mViewBinding.syllabusRV.setVisibility(View.VISIBLE);
                                mViewBinding.noRecordLayout.noRecord.setVisibility(View.GONE);
                            } else
                                Toast.makeText(mContext, getString(R.string.somethingWrongNoDataMsg), Toast.LENGTH_LONG).show();

                        } else if (classworkBean.rcode == Constants.Rcode.NORECORDS) {
                            mViewBinding.syllabusRV.setVisibility(View.GONE);
                            mViewBinding.noRecordLayout.noRecord.setVisibility(View.VISIBLE);
                            mViewBinding.noRecordLayout.noRecordMsg.setText(getString(R.string.noResultFound));

                        } else
                            Toast.makeText(mContext, classworkBean.msg, Toast.LENGTH_LONG).show();

                    } else
                        Toast.makeText(mContext, getString(R.string.somethingWrongNoDataMsg), Toast.LENGTH_LONG).show();

                    mAdapter.notifyDataSetChanged();
                    mViewBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<SyllabusBean> call, Throwable t) {
                    mViewBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
            classworkService.getSyllabusForTeacher(userData.getUserId(), userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<SyllabusBean>() {
                @Override
                public void onResponse(Call<SyllabusBean> call, Response<SyllabusBean> response) {

                    mSyllabusList.clear();
                    SyllabusBean classworkBean = response.body();

                    if (classworkBean != null) {
                        if (classworkBean.data != null) {
                            if (classworkBean.rcode.equals(Constants.Rcode.OK) && classworkBean.data.size() > 0) {
                                for (SyllabusResponse classwork : classworkBean.data) {
                                    mSyllabusList.add(classwork);
                                }
                                mAdapter.notifyDataSetChanged();
                                mViewBinding.syllabusRV.setVisibility(View.VISIBLE);
                                mViewBinding.noRecordLayout.noRecord.setVisibility(View.GONE);

                            } else if (classworkBean.rcode == Constants.Rcode.NORECORDS) {
                                mViewBinding.syllabusRV.setVisibility(View.GONE);
                                mViewBinding.noRecordLayout.noRecord.setVisibility(View.VISIBLE);
                                mViewBinding.noRecordLayout.noRecordMsg.setText(getString(R.string.noResultFound));

                            }
                        } else
                            Toast.makeText(mContext, getString(R.string.somethingWrongNoDataMsg), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(mContext, getString(R.string.somethingWrongNoDataMsg), Toast.LENGTH_LONG).show();

                    mViewBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<SyllabusBean> call, Throwable t) {
                    mViewBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            classworkService.getSyllabusForParent(userPref.getSelectedChild().StudentId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<SyllabusBean>() {
                @Override
                public void onResponse(Call<SyllabusBean> call, Response<SyllabusBean> response) {

                    mSyllabusList.clear();
                    SyllabusBean classworkBean = response.body();

                    if (classworkBean != null) {
                        if (classworkBean.data != null) {
                            if (classworkBean.rcode.equals(Constants.Rcode.OK) && classworkBean.data.size() > 0) {
                                for (SyllabusResponse classwork : classworkBean.data) {
                                    mSyllabusList.add(classwork);
                                }
                                mAdapter.notifyDataSetChanged();
                                mViewBinding.syllabusRV.setVisibility(View.VISIBLE);
                                mViewBinding.noRecordLayout.noRecord.setVisibility(View.GONE);

                            } else if (classworkBean.rcode == Constants.Rcode.NORECORDS) {
                                mViewBinding.syllabusRV.setVisibility(View.GONE);
                                mViewBinding.noRecordLayout.noRecord.setVisibility(View.VISIBLE);
                                mViewBinding.noRecordLayout.noRecordMsg.setText(getString(R.string.noResultFound));

                            }
                        } else
                            Toast.makeText(mContext, getString(R.string.somethingWrongNoDataMsg), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(mContext, getString(R.string.somethingWrongNoDataMsg), Toast.LENGTH_LONG).show();

                    mViewBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<SyllabusBean> call, Throwable t) {
                    mViewBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Initialize class list, adapter
     * set adapter to recycler view
     * Call class list api.
     */
    private void initClass() {
        classes = new ArrayList<>();

        DropDownModel classDefault = new DropDownModel();
        classDefault.setId(-1);
        classDefault.setText(Constants.DEFAULT_CLASS);
        classes.add(classDefault);

        classAdapter = new DropDownAdapter(mContext, R.layout.row_spinner, classes);
        mViewBinding.classList.setAdapter(classAdapter);

        mViewBinding.classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = classAdapter.getItem(position);
                if (selectedItem.getId() == -1)
                    classId = 0;
                else {

                    classId = selectedItem.getId();
                    getSyllabusList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        getClasses();
    }

    /**
     * Call api to get classes for admin
     * notify adapter
     */
    private void getClasses() {
        mViewBinding.progressBar.setVisibility(View.VISIBLE);
        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {

            baseService.getMasterClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    classes.clear();

                    DropDownModel classDefault = new DropDownModel();
                    classDefault.setId(-1);
                    classDefault.setText("Select Class");
                    classes.add(classDefault);

                    ClassBean classBean = response.body();

                    if (classBean != null) {
                        if (classBean.rcode == Constants.Rcode.OK) {

                            for (ClassModel datum : classBean.data) {
                                DropDownModel cls = new DropDownModel();
                                cls.setId(datum.Id);
                                cls.setText(datum.Name);
                                classes.add(cls);
                            }
                            classAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, getString(R.string.classListError), Toast.LENGTH_LONG).show();
                        }
                    }
                    mViewBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    mViewBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, getString(R.string.classListError), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onClick(SyllabusResponse teacherModel) {
//
    }
}
