package com.jeannypr.scientificstudy.Student.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.ClassTeachersModel;
import com.jeannypr.scientificstudy.Student.model.MyTeachersBean;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassTeachersActivity extends AppCompatActivity {
    private int classId;
    private Context context;
    private UserModel userData;
    private LayoutInflater layoutInflator;
    private StudentService studentService;
    private List<ClassTeachersModel> myClassTeachers;
    private ProgressDialog p_dialog;
    private LinearLayout teacherListContainer, noRecord;
    private ChildModel selectedChild;
    private UserPreference userPreference;
    private String classname;
    private TextView noRecordMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class_teachers);
        context = this;

        userPreference = UserPreference.getInstance(context);
        userData = userPreference.getUserData();
        selectedChild = userPreference.getSelectedChild();
        layoutInflator = LayoutInflater.from(context);
        studentService = new DataRepo<>(StudentService.class, context).getService();

        classId = selectedChild.ClassId;
        classname = selectedChild.ClassName;

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context,"My Teachers","");

        teacherListContainer = findViewById(R.id.classTeacherList);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        showLoader();
        studentService.getMyTeachers(selectedChild.StudentId, userData.getSchoolId()).enqueue(new Callback<MyTeachersBean>() {
            @Override
            public void onResponse(Call<MyTeachersBean> call, Response<MyTeachersBean> response) {
                MyTeachersBean classTeachersBean = response.body();
                if (classTeachersBean != null) {

                    if (classTeachersBean.rcode == Constants.Rcode.OK) {
                        if (classTeachersBean.data != null) {
                            myClassTeachers = classTeachersBean.data;
                            InflateUI();
                        }

                    } else if (classTeachersBean.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found.");
                    } else {
                        Toast.makeText(context, "Teachers list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                hideLoader();
            }

            @Override
            public void onFailure(Call<MyTeachersBean> call, Throwable t) {
                Toast.makeText(context, "Could not load teachers list!", Toast.LENGTH_SHORT).show();
                hideLoader();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void InflateUI() {

        for (final ClassTeachersModel classTeacher : myClassTeachers) {

            if (classTeacher.TeacherName != null && !classTeacher.TeacherName.equals("")) {
                final RelativeLayout view = (RelativeLayout) layoutInflator.inflate(R.layout.row_my_class_teachers, teacherListContainer, false);

                final TextView teacherName = view.findViewById(R.id.myClassTeacherName);
                final TextView subjectName = view.findViewById(R.id.subject);

                teacherName.setText(String.valueOf(classTeacher.TeacherName == null ? "" : classTeacher.TeacherName));
                subjectName.setText(String.valueOf(classTeacher.SubjectName == null ? "" : classTeacher.SubjectName));

                teacherListContainer.addView(view);
            }
        }

    }

    private void showLoader() {
        p_dialog = Utility.showProgressDialog(context, "Getting teachers list. Please wait...");

    }

    private void hideLoader() {
        p_dialog.dismiss();

    }
}
