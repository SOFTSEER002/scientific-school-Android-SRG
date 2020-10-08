package com.jeannypr.scientificstudy.Teacher.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.ChatParentListActivity;
import com.jeannypr.scientificstudy.Class.adapter.ClassListAdapter;
import com.jeannypr.scientificstudy.LearnSubject.activity.LearnListActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.activity.StudentListActivity;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.databinding.ActivityClassListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyClassListActivity extends AppCompatActivity implements View.OnClickListener {

    private ClassListAdapter adapter;
    private Context context;
    private ProgressBar pb;
    private ArrayList<ClassModel> classes;
    private UserPreference userPreference;
    private UserModel userModel;
    private int userId;
    private LinearLayout noRecord;
    TextView noRecordMsg;
    ConstraintLayout analyticsCell, sortCell, searchCell, filterCell;
    SearchView searchView;
    TeacherService teacherService;
    UserModel userData;
    UserPreference userPref;

    ActivityClassListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_list);
        context = this;

        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();
        userId = userModel.getUserId();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Classes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        analyticsCell = findViewById(R.id.analyticsCell);
        analyticsCell.setOnClickListener(this);

        sortCell = findViewById(R.id.sortCell);
        sortCell.setOnClickListener(this);

        searchCell = findViewById(R.id.searchCell);
        searchCell.setOnClickListener(this);

        filterCell = findViewById(R.id.filterCell);
        filterCell.setOnClickListener(this);

        searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nexText) {
                adapter.getFilter().filter(nexText);
                return false;
            }
        });

        RecyclerView list = findViewById(R.id.list);
        pb = findViewById(R.id.progressBar);

        classes = new ArrayList<>();
        adapter = new ClassListAdapter(context, classes, new ClassListAdapter.OnItemClickListener() {

            @Override
            public void onLearn(ClassModel classModel) {
                startActivity(new Intent(context, LearnListActivity.class)
                        .putExtra("Id", classModel.Id)
                        .putExtra("ClassName", classModel.Name));
            }

            @Override
            public void onStudentClick(ClassModel classModel) {
                if (/*classModel.TotalNoStudents != null && */classModel.getTotalNoStudents() > 0) {
                    Intent studentListIntent = new Intent(context, StudentListActivity.class);
                    studentListIntent.putExtra("Id", classModel.Id);
                    studentListIntent.putExtra("ClassName", classModel.Name);
                    startActivity(studentListIntent);
                }
            }

            @Override
            public void onChatIcon(ClassModel classModel) {
                Intent studentListIntent = new Intent(context, ChatParentListActivity.class);
                studentListIntent.putExtra("Id", classModel.Id);
                studentListIntent.putExtra("ClassName", classModel.Name);
                startActivity(studentListIntent);
            }
        });


        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(context));


       /* TeacherService teacherService = new DataRepo<>(TeacherService.class, context).getService();

        UserPreference userPref = UserPreference.getInstance(context);
        UserModel userData = userPref.getUserData();*/

        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        pb.setVisibility(View.VISIBLE);

        MyclassesList();




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void MyclassesList() {
        teacherService.GetMyAllClasses(userData.getSchoolId(), userData.getAcademicyearId(), userId).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            List<ClassModel> allclasss = resp.data;
                            for (ClassModel cls : allclasss) {
                                classes.add(cls);
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {

                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);

                    } else {
                        Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                Log.d("classList", "server call error");
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.searchCell:
                searchView.setVisibility(View.VISIBLE);
                searchView.setIconifiedByDefault(false);
                analyticsCell.setVisibility(View.GONE);
                sortCell.setVisibility(View.GONE);
                searchCell.setVisibility(View.GONE);
                filterCell.setVisibility(View.GONE);
                break;

            case R.id.searchView:
                searchView.setIconifiedByDefault(true);
                searchView.setVisibility(View.GONE);
                analyticsCell.setVisibility(View.VISIBLE);
                sortCell.setVisibility(View.VISIBLE);
                searchCell.setVisibility(View.VISIBLE);
                filterCell.setVisibility(View.VISIBLE);
                break;
        }
    }
}
