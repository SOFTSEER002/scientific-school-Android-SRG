package com.jeannypr.scientificstudy.Classwork.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Classwork.adapter.CwHwListAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.ActivityBean;
import com.jeannypr.scientificstudy.Classwork.model.ActivityModel;
import com.jeannypr.scientificstudy.Exam.api.ExamService;
import com.jeannypr.scientificstudy.Exam.model.SubjectExamBean;
import com.jeannypr.scientificstudy.Exam.model.SubjectModel;
import com.jeannypr.scientificstudy.Login.api.LoginService;
import com.jeannypr.scientificstudy.Login.model.FedratedLoginBean;
import com.jeannypr.scientificstudy.Login.model.FedratedLoginInput;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Utilities.SilentLogin;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityClassListBinding;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CwHwListActivity extends AppCompatActivity implements View.OnClickListener {


    private CwHwListAdapter adapter;
    private Context context;
    ProgressBar progressBar;
    private ArrayList<ActivityModel> classworkModel;
    private ActivityClassListBinding binding;
    private LinearLayout noRecord;
    private TextView noRecordMsg, txtDate;
    RecyclerView classworkList;
    CwHwService classworkService;
    StudentService studentService;
    TeacherService teacherService;
    UserPreference userPref;
    UserModel userData;
    ChildModel selectedChild;
    ArrayList<DropDownModel> classes, subjects;
    DropDownAdapter classAdapter, subjectAdapter;
    BaseService baseService;
    private ExamService service;
    Spinner classList, subjectList;
    int classId, subjectId, activityTypeId;
    String className, subjectName, mode, activityType;
//    RelativeLayout classListSpinner;
    FloatingActionButton createActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cw_hw_list);
        context = this;

        classworkService = new DataRepo<>(CwHwService.class, context).getService();
        baseService = new DataRepo<>(BaseService.class, context).getService();
        service = new DataRepo<>(ExamService.class, context).getService();
        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        studentService = new DataRepo<>(StudentService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        activityTypeId = getIntent().getIntExtra(Constants.ACTIVITY_TYPE, -1);
        activityType = activityTypeId == Constants.DiaryType.Homework ? Constants.DiaryTypeName.Homework : Constants.DiaryTypeName.Classwork;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, activityType, "");

        classworkList = findViewById(R.id.classworkList);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        progressBar = findViewById(R.id.progressBar);
        classList = findViewById(R.id.classList);
        subjectList = findViewById(R.id.subjectList);
//        classListSpinner = findViewById(R.id.classListSpinner);

        createActivityBtn = findViewById(R.id.createActivityBtn);
        createActivityBtn.setOnClickListener(this);
        if (userData.getRoleTitle().equals(Constants.Role.PARENT))
            createActivityBtn.hide();

        if (userData.getRoleTitle().equals(Constants.Role.ADMIN) || userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
            classId = 0;
            subjectId = 0;
        } else {
            selectedChild = userPref.getSelectedChild();
            classId = selectedChild.ClassId;
            className = selectedChild.ClassName;
        }
        mode = userData.getRoleTitle().equals(Constants.Role.TEACHER) ? Constants.MODE.ASSIGNED : Constants.MODE.ALL;
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.Directory.Base);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        classworkModel = new ArrayList<>();
        adapter = new CwHwListAdapter(context, classworkModel, userData.getRoleTitle().equals(Constants.Role.PARENT),
                activityType, activityTypeId, new CwHwListAdapter.OnItemClickListener() {

            @Override
            public void onClick(ActivityModel classworkModel) {
               /* Intent classworkdetailIntent = new Intent(context, CwHwDetailActivity.class);
                classworkdetailIntent.putExtra("activityId", classworkModel.Id);
                classworkdetailIntent.putExtra("className", classworkModel.ClassName);
                classworkdetailIntent.putExtra("activityTypeId", classworkModel.getActivityType());
                classworkdetailIntent.putExtra("subjectName", classworkModel.SubjectName);

                if (classworkModel.getActivityType() == Constants.DiaryType.Homework) {
                    classworkdetailIntent.putExtra(Constants.TEACHER_EMAIL, classworkModel.getTeacherEmail());
                    classworkdetailIntent.putExtra(Constants.TEACHER_MOBILE, classworkModel.getTeacherMobile());
                }
                startActivity(classworkdetailIntent);*/

                Intent classworkdetailIntent;
                if (classworkModel.getActivityType() == Constants.DiaryType.Homework) {
                    classworkdetailIntent = new Intent(context, HWDetailActivity.class);
                    classworkdetailIntent.putExtra(Constants.TEACHER_EMAIL, classworkModel.getTeacherEmail());
                    classworkdetailIntent.putExtra(Constants.TEACHER_MOBILE, classworkModel.getTeacherMobile());

                }else classworkdetailIntent = new Intent(context, CwHwDetailActivity.class);

                classworkdetailIntent.putExtra("activityId", classworkModel.Id);
                classworkdetailIntent.putExtra("className", classworkModel.ClassName);
                classworkdetailIntent.putExtra("activityTypeId", classworkModel.getActivityType());
                classworkdetailIntent.putExtra("subjectName", classworkModel.SubjectName);
                startActivity(classworkdetailIntent);
            }
        });
        classworkList.setAdapter(adapter);
        classworkList.setLayoutManager(new LinearLayoutManager(context));

        classes = new ArrayList<>();
        DropDownModel classDefault = new DropDownModel();
        classDefault.setId(-1);
        classDefault.setText("Select Class");
        classes.add(classDefault);
        classAdapter = new DropDownAdapter(context, R.layout.row_spinner, classes);

        subjects = new ArrayList<>();
        DropDownModel subjectDefault = new DropDownModel();
        subjectDefault.setId(-1);
        subjectDefault.setText("Select Subject");
        subjects.add(subjectDefault);
        subjectAdapter = new DropDownAdapter(context, R.layout.row_spinner, subjects);

        classList.setAdapter(classAdapter);
        classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = classAdapter.getItem(position);
                if (selectedItem.getId() == -1) {
                    classId = 0;
                    className = "";
                    // subjectList.setVisibility(View.INVISIBLE);

                } else {

                    classId = selectedItem.getId();
                    className = selectedItem.getText();

                    GetClassworkList();
                    GetSubjects(classId, mode);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        subjectList.setAdapter(subjectAdapter);
        subjectList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = subjectAdapter.getItem(position);
                if (selectedItem.getId() == -1) {
                    subjectId = 0;
                    subjectName = "";
                } else {
                    subjectId = selectedItem.getId();
                    subjectName = selectedItem.getText();

                    GetClassworkList();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        GetClasses();
        GetClassworkList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetClasses();
        GetClassworkList();

    }

    public void GetSubjects(int classId, String mode) {
        if (userData.getRoleTitle().equals(Constants.Role.ADMIN) || userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
            service.getSubjectsAndExams(classId, userData.getUserId(), mode, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<SubjectExamBean>() {
                @Override
                public void onResponse(Call<SubjectExamBean> call, Response<SubjectExamBean> response) {
                    SubjectExamBean resp = response.body();

                    subjects.clear();
                    DropDownModel subjectDefault = new DropDownModel();
                    subjectDefault.setId(-1);
                    subjectDefault.setText("Select Subject");
                    subjects.add(subjectDefault);

                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {

                            for (SubjectModel subject : resp.subjects) {
                                DropDownModel sub = new DropDownModel();
                                sub.setId(subject.Id);
                                sub.setText(subject.Name);
                                subjects.add(sub);
                            }
                            subjectList.setVisibility(View.VISIBLE);
                            subjectAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, "Unable to load subjects. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SubjectExamBean> call, Throwable t) {
                    Toast.makeText(context, "Unable to load subjects. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            studentService.getSubjectsForParent(classId, mode, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<SubjectExamBean>() {
                @Override
                public void onResponse(Call<SubjectExamBean> call, Response<SubjectExamBean> response) {
                    SubjectExamBean resp = response.body();
                    subjects.clear();

                    DropDownModel subjectDefault = new DropDownModel();
                    subjectDefault.setId(-1);
                    subjectDefault.setText("Select Subject");
                    subjects.add(subjectDefault);

                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {

                            for (SubjectModel subject : resp.subjects) {
                                DropDownModel sub = new DropDownModel();
                                sub.setId(subject.Id);
                                sub.setText(subject.Name);
                                subjects.add(sub);
                            }
                            subjectList.setVisibility(View.VISIBLE);
                            subjectAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, "Unable to load subjects. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Unable to load subjects. Please try again.", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<SubjectExamBean> call, Throwable t) {
                    Toast.makeText(context, "Unable to load subjects. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void GetClassworkList() {
        progressBar.setVisibility(View.VISIBLE);

        if (userData.getRoleTitle().equals(Constants.Role.ADMIN) || userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
            classworkService.getClassworkListForStaff(userData.getUserId(), userData.getRoleTitle(), userData.getSchoolId(), userData.getAcademicyearId(),
                    classId, subjectId, activityTypeId).enqueue(new Callback<ActivityBean>() {
                @Override
                public void onResponse(Call<ActivityBean> call, Response<ActivityBean> response) {

                    classworkModel.clear();
                    ActivityBean classworkBean = response.body();
                    int count = 0;
                    if (classworkBean != null) {
                        count = classworkBean.data.size();


                        if (classworkBean.rcode.equals(Constants.Rcode.OK) && count > 0) {
                            // classworkList.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                            for (ActivityModel classwork : classworkBean.data) {

                                if (userData.getRoleTitle().equals(Constants.Role.ADMIN) || userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
                                    classworkModel.add(classwork);
                                }
//                                else {
//                                    if (classwork.getIsAssignedToClass()) {
//                                        classworkModel.add(classwork);
//                                    }
//                                }


                            }
                            adapter.notifyDataSetChanged();
                            classworkList.setVisibility(View.VISIBLE);
                            noRecord.setVisibility(View.GONE);

                        } else if (classworkBean.rcode == Constants.Rcode.NORECORDS || count < 1) {
                            classworkList.setVisibility(View.GONE);
                            noRecord.setVisibility(View.VISIBLE);
                            noRecordMsg.setText("No record found.");

                        }
                    } else {
                        Toast.makeText(context, "Could not load classwork list. Please try again", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ActivityBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Could not load classwork list. Please try again", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            classworkService.getClassworkListForParent(selectedChild.StudentId, userData.getSchoolId(), userData.getAcademicyearId(),
                    classId, subjectId, activityTypeId)
                    .enqueue(new Callback<ActivityBean>() {
                        @Override
                        public void onResponse(Call<ActivityBean> call, Response<ActivityBean> response) {
                            progressBar.setVisibility(View.GONE);
                            classworkModel.clear();

                            ActivityBean classworkBean = response.body();
                            if (classworkBean != null) {
                                int count = classworkBean.data.size();

                                if (classworkBean.rcode.equals(Constants.Rcode.OK) && count > 0) {

                                    for (ActivityModel classwork : classworkBean.data) {

                                        if (userData.getRoleTitle().equals(Constants.Role.ADMIN) || userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
                                            classworkModel.add(classwork);
                                        } else {
                                            if (classwork.getIsAssignedToClass()) {
                                                classworkModel.add(classwork);
                                            }
                                        }
                                    }

                                    adapter.notifyDataSetChanged();
                                    if (classworkModel.size() < 1) {
                                        classworkList.setVisibility(View.GONE);
                                        noRecord.setVisibility(View.VISIBLE);
                                        noRecordMsg.setText("No record found.");

                                    } else {
                                        classworkList.setVisibility(View.VISIBLE);
                                        noRecord.setVisibility(View.GONE);
                                    }


                                } else if (classworkBean.rcode == Constants.Rcode.NORECORDS || count < 1) {
                                    classworkList.setVisibility(View.GONE);
                                    noRecord.setVisibility(View.VISIBLE);
                                    noRecordMsg.setText("No record found.");

                                } else {
                                    Toast.makeText(context, "Could not load" + activityType + "list. Please try again", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Could not load" + activityType + "list. Please try again", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ActivityBean> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Could not load" + activityType + " list. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public void GetClasses() {
        progressBar.setVisibility(View.VISIBLE);
        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {

            baseService.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
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
                            Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                }
            });

        } else if (userData.getRoleTitle().equals(Constants.Role.TEACHER)) {

            teacherService.GetMyClasses(userData.getSchoolId(), userData.getAcademicyearId(), userData.getUserId()).enqueue(new Callback<ClassBean>() {
                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    classes.clear();

                    DropDownModel classDefault = new DropDownModel();
                    classDefault.setId(-1);
                    classDefault.setText("Select Class");
                    classes.add(classDefault);

                    ClassBean myClassesBean = response.body();
                    if (myClassesBean.rcode == Constants.Rcode.OK) {

                        for (ClassModel myClass : myClassesBean.data) {
                            DropDownModel cls = new DropDownModel();
                            cls.setId(myClass.Id);
                            cls.setText(myClass.Name);
                            classes.add(cls);
                        }
                        classAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                }
            });
        } else if (userData.getRoleTitle().equals(Constants.Role.PARENT)) {
            classList.setVisibility(View.GONE);
            GetSubjects(classId, mode);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createActivityBtn:
                Intent createActivityIntent = new Intent(this, CreateCwHwActivity.class);
                createActivityIntent.putExtra("activityType", activityType);
                createActivityIntent.putExtra("activityTypeId", activityTypeId);
                try {
                    startActivity(createActivityIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (userData.getRoleTitle().equals(Constants.Role.PARENT)) {
            getMenuInflater().inflate(R.menu.cw_hw_list, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.helpIc:
                Utility.showAlertDialogWithListnerAndBothButtons(context, null, "", getString(R.string.browserMsg), "", "",
                        new Utility.OnDialogClickListner() {
                            @Override
                            public void onClickPositiveButton() {
                                if (activityTypeId == Constants.DiaryType.Homework)
                                    performSilentLogin(SilentLogin.HW_WEB_URL_PARENT, false);
                                else performSilentLogin(SilentLogin.CW_WEB_URL_PARENT, false);
                            }

                            @Override
                            public void onClickNegativeButton() {

                            }
                        }, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performSilentLogin(String returnUrl, Boolean openLinkInWebView) {
        getJWTToken(SilentLogin.HTTPS + userPref.getSchoolData().getSubDomain() + returnUrl, openLinkInWebView);
    }

    /**
     * Call api to get JWT token
     *
     * @param returnUrl redirect to url
     */
    private void getJWTToken(final String returnUrl, final Boolean openLinkInWebView) {
        final ProgressDialog p_dialog = Utility.showProgressDialog(context, "Loading...");
        LoginService service = new DataRepo<>(LoginService.class, context, ApiConstants.SILENT_LOGIN_BASE_URL, ApiConstants.AUTHENTICATION_TOKEN).getService();

        service.getJWTToken(new FedratedLoginInput(userData.getUserId(), userData.getUserName().trim(), userPref.getSchoolData().getSchoolKey().trim()))
                .enqueue(new Callback<FedratedLoginBean>() {
                    @Override
                    public void onResponse(Call<FedratedLoginBean> call, Response<FedratedLoginBean> response) {

                        FedratedLoginBean bean = response.body();
                        if (bean != null) {
                            if (!bean.getToken().equals(""))
                                redirectToNext(returnUrl, bean.getToken(), openLinkInWebView);

                            else
                                Utility.showAlertDialog(context, null, null, getString(R.string.silentLoginErrorMsg));
                        } else {
                            Utility.showAlertDialog(context, null, null, getString(R.string.somethingWrongMsg));
                        }
                        p_dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<FedratedLoginBean> call, Throwable t) {
                        Utility.showAlertDialog(context, null, null, t.getMessage());
                        p_dialog.dismiss();
                    }
                });
    }

    private void redirectToNext(String returnUrl, String token, Boolean openLinkInWebView) {
        String url = SilentLogin.HTTPS + userPref.getSchoolData().getSubDomain() + SilentLogin.SCIENTIFICSTUDY_IN + "sso/do?jwt=" + token + "&returnUrl=" + returnUrl;

        if (openLinkInWebView)
            Utility.openInAppBrowser(context, url, "", "", R.string.urlError);
        else
            Utility.openLinkInSystemBrowser(url, R.string.unableToOpen, context);
    }

}
