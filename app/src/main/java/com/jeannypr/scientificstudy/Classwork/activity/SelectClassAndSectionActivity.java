package com.jeannypr.scientificstudy.Classwork.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Exam.api.ExamService;
import com.jeannypr.scientificstudy.Exam.model.SubjectExamBean;
import com.jeannypr.scientificstudy.Exam.model.SubjectModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.databinding.ActivitySelectClassSectionsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectClassAndSectionActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    CwHwService classworkService;
    UserPreference userPref;
    UserModel userData;
    private ArrayList<DropDownModel> classes, subjects;
    private List<ClassModel> sections;
    DropDownAdapter adapter, subjectAdapter;
    Spinner classList, subjectList;
    ProgressBar pb;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    private ActivitySelectClassSectionsBinding binding;
    BaseService baseService;
    ExamService service;
    FloatingActionButton nextFabBtn;
    ArrayList<ClassModel> selectedClassesAndSections;
    int selectedClassId, selectedSubjectId;
    String selectedClassName, selectedSubjectName, mode;
    LinearLayout sectionsParent;
    TeacherService teacherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_class_sections);

        context = this;
        classworkService = new DataRepo<>(CwHwService.class, context).getService();
        baseService = new DataRepo<>(BaseService.class, context).getService();
        service = new DataRepo<>(ExamService.class, context).getService();
        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        classList = findViewById(R.id.classList);
        subjectList = findViewById(R.id.subjectList);
        pb = findViewById(R.id.progressBar);
        sectionsParent = findViewById(R.id.sectionsParent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        nextFabBtn = findViewById(R.id.nextFabBtn);
        nextFabBtn.setOnClickListener(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select class and subject");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mode = userData.getRoleTitle().equals(Constants.Role.TEACHER) ? Constants.MODE.ASSIGNED : Constants.MODE.ALL;
        classes = new ArrayList<>();
        DropDownModel defaultClass = new DropDownModel();
        defaultClass.setText("Select Class");
        defaultClass.setId(-1);
        classes.add(defaultClass);

        subjects = new ArrayList<>();
        DropDownModel defaultSubject = new DropDownModel();
        defaultSubject.setText("Select Subject");
        defaultSubject.setId(-1);
        subjects.add(defaultSubject);

        sections = new ArrayList<>();
        selectedClassesAndSections = new ArrayList<>();

        adapter = new DropDownAdapter(SelectClassAndSectionActivity.this,
                R.layout.row_spinner,
                classes);
        classList.setAdapter(adapter);

        subjectAdapter = new DropDownAdapter(SelectClassAndSectionActivity.this,
                R.layout.row_spinner,
                subjects);
        subjectList.setAdapter(subjectAdapter);

        GetClasses();

        classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel model = adapter.getItem(position);

                if (model != null) {

                    if (model.getId() == -1) {
                        selectedClassId = -1;
                        selectedClassName = "";
                    } else {
                        selectedClassId = model.getId();
                        selectedClassName = model.getText();

                        GetSubjects(selectedClassId);

                        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {
                            GetSectionsOfSelectedClass(selectedClassId);

                        } else {
                            selectedClassesAndSections.clear();

                            ClassModel selectedCls = new ClassModel();
                            selectedCls.Id = model.getId();
                            selectedCls.Name = model.getText();
                            selectedClassesAndSections.add(selectedCls);
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subjectList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel model = subjectAdapter.getItem(position);

                if (model != null) {
                    if (model.getId() == -1) {
                        selectedSubjectId = -1;
                        selectedSubjectName = "";
                    } else {
                        selectedSubjectId = model.getId();
                        selectedSubjectName = model.getText();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void GetClasses() {
        pb.setVisibility(View.VISIBLE);

        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {
            baseService.getMasterClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    ClassBean resp = response.body();
                    if (resp != null) {

                        if (resp.rcode == Constants.Rcode.OK) {

                            for (ClassModel cls : resp.data) {
                                DropDownModel model = new DropDownModel();
                                model.setId(cls.Id);
                                model.setText(cls.Name);
                                classes.add(model);
                            }
                            adapter.notifyDataSetChanged();

                        } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                            noRecord.setVisibility(View.VISIBLE);
                            noRecordMsg.setText("No Class found.");

                        } else {
                            Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                    pb.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                }
            });

        } else if (userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
            teacherService.GetMyClasses(userData.getSchoolId(), userData.getAcademicyearId(), userData.getUserId()).enqueue(new Callback<ClassBean>() {

                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    ClassBean resp = response.body();
                    if (resp != null) {

                        if (resp.rcode == Constants.Rcode.OK) {

                            for (ClassModel cls : resp.data) {
                                DropDownModel model = new DropDownModel();
                                model.setId(cls.Id);
                                model.setText(cls.Name);
                                classes.add(model);
                            }
                            adapter.notifyDataSetChanged();

                        } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                            noRecord.setVisibility(View.VISIBLE);
                            noRecordMsg.setText("No Class found.");

                        } else {
                            Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void GetSubjects(int classId) {
        subjects.clear();
        selectedSubjectId = -1;
        selectedSubjectName = "";

        DropDownModel defaultSubject = new DropDownModel();
        defaultSubject.setText("Select Subject");
        defaultSubject.setId(-1);
        subjects.add(defaultSubject);
        subjectAdapter.notifyDataSetChanged();

        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {
            service.GetSubjectsOfMasterClass(classId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<SubjectExamBean>() {
                @Override
                public void onResponse(Call<SubjectExamBean> call, Response<SubjectExamBean> response) {
                    SubjectExamBean resp = response.body();

                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {

                            for (SubjectModel subject : resp.subjects) {
                                DropDownModel sub = new DropDownModel();
                                sub.setId(subject.Id);
                                sub.setText(subject.Name);
                                subjects.add(sub);
                            }
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
            service.getSubjectsAndExams(classId, userData.getUserId(), mode, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<SubjectExamBean>() {
                @Override
                public void onResponse(Call<SubjectExamBean> call, Response<SubjectExamBean> response) {
                    SubjectExamBean resp = response.body();

                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {

                            for (SubjectModel subject : resp.subjects) {
                                DropDownModel sub = new DropDownModel();
                                sub.setId(subject.Id);
                                sub.setText(subject.Name);
                                subjects.add(sub);
                            }
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
        }

    }

    public void GetSectionsOfSelectedClass(int classId) {
        pb.setVisibility(View.VISIBLE);

        baseService.getSectionsOfClass(classId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();

                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        sections.clear();
                        sections = resp.data;
                        InflateSectionRow();

                    } else {
                        Toast.makeText(context, "Sections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Sections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void InflateSectionRow() {

        sectionsParent.removeAllViews();
        selectedClassesAndSections.clear();
        for (final ClassModel section : sections) {

            CheckBox sectionChk = new CheckBox(this);

            //set width and height of view
            ViewGroup.LayoutParams params = sectionChk.getLayoutParams();
            if (params != null) {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            //set other attr
            sectionChk.setText(section.Name);
            sectionChk.setTag(section.Name);
            // sectionChk.setPadding(10, 4, 10, 4);

          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sectionChk.setBackground(mContext.getResources().getDrawable(R.drawable.white_bg_blue_border));
            }*/

            sectionsParent.addView(sectionChk);

            sectionChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ClassModel model = new ClassModel();
                        model.Id = section.Id;
                        model.Name = section.Name;

                        selectedClassesAndSections.add(model);
                    } else {
                        for (ClassModel model : selectedClassesAndSections) {
                            if (model.Id == section.Id) {
                                selectedClassesAndSections.remove(model);
                                break;
                            }
                        }
                    }
                }
            });
        }

        sectionsParent.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextFabBtn:
                Intent backToCreateCwHwIntent = new Intent(this, CreateCwHwActivity.class);

                int size = selectedClassesAndSections.size();
                if (size > 0) {
                    backToCreateCwHwIntent.putParcelableArrayListExtra("selectedSections", selectedClassesAndSections);

                    if (selectedSubjectId != -1 && !selectedSubjectName.equals("")) {
                        backToCreateCwHwIntent.putExtra("subjectId", selectedSubjectId);
                        backToCreateCwHwIntent.putExtra("subjectName", selectedSubjectName);
                        setResult(RESULT_OK, backToCreateCwHwIntent);
                        finish();
                        break;

                    } else {
                        Toast.makeText(context, "Please select subject.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    Toast.makeText(context, "Please select atleast one section", Toast.LENGTH_SHORT).show();
                    break;
                }


        }
    }
}
