package com.jeannypr.scientificstudy.Student.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentModuleActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner classList;
    DropDownModel selectedItem;
    private DropDownAdapter adapter;
    private Context context;
    private ProgressBar pb;
    ArrayList<DropDownModel> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_module);

        context = this;
        classList = findViewById(R.id.ddlClassList);

        ConstraintLayout studentListBtn = findViewById(R.id.studentListBtn);
        pb = findViewById(R.id.progressBar);
        studentListBtn.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Students");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        classes = new ArrayList<DropDownModel>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText("Select Class");
        defaultOption.setId(-1);
        classes.add(defaultOption);

        adapter = new DropDownAdapter(StudentModuleActivity.this,
                R.layout.row_spinner,
                classes);
        classList = findViewById(R.id.ddlClassList);
        classList.setAdapter(adapter);

        classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                selectedItem = adapter.getItem(position);
                if (selectedItem.getId() == -1) {
                    selectedItem = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        GetClasses();
    }

    public void GetClasses() {
        BaseService BaseService = new DataRepo<>(BaseService.class, this).getService();
        UserPreference userPref = UserPreference.getInstance(this);
        UserModel userData = userPref.getUserData();
        showLoader();
        BaseService.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            List<ClassModel> allClasses = resp.data;

                            for (ClassModel cls : allClasses) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(cls.Id);
                                dropDownModel.setText(cls.Name);
                                classes.add(dropDownModel);
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                        //TODO: norecord msg
                    } else {
                        Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                hideLoader();

            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                Log.d("classList", "server call error");
                hideLoader();
                Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showLoader() {
        //p_dialog = Utility.showProgressDialog(context, "Loading classes. Please wait...");
        pb.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        //   p_dialog.dismiss();
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manageRollNoBtn:
                break;
            case R.id.bulkUpdateBtn:
                break;
            case R.id.studentListBtn:
                if (selectedItem == null) {
                    Toast.makeText(getApplicationContext(), "Please select a class.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent studentListIntent = new Intent(this, StudentListActivity.class);
                    studentListIntent.putExtra("Id", selectedItem.getId());
                    studentListIntent.putExtra("ClassName", selectedItem.getText());
                    startActivity(studentListIntent);
                }

                break;

        }

    }
}
