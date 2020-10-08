package com.jeannypr.scientificstudy.LearnSubject.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.LearnSubject.adapter.SubjectLearnAdapter;
import com.jeannypr.scientificstudy.LearnSubject.api.LearnSubjectService;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectDataModel;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectDetailsBean;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectDetailsListModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivitySubjectDetailsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    UserModel userData;
    private SubjectLearnAdapter adapter;
    private Context context;
    private ArrayList<LearnSubjectDetailsListModel> listModels;
    private UserPreference userPreference;
    private UserModel userModel;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    ProgressBar pb;
    int totalStudents;
    ConstraintLayout analyticsCell, sortCell, searchCell, filterCell;
    SearchView searchView;
    private ChatService mChatService;
    String schoolCode;
    ActivitySubjectDetailsBinding binding;
    private LearnSubjectService service;

    private String SubName;
    private int subId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_details);
        context = this;
        listModels = new ArrayList<>();
        service = new DataRepo<>(LearnSubjectService.class, context).getService();

        adapter = new SubjectLearnAdapter(this,listModels,subId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        binding.recyclerView.setAdapter(adapter);

        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();
        schoolCode = userPreference.getSchoolCode();

        SetToolbar();
        pb = findViewById(R.id.progressBar);

        setTopData();

        UserPreference userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        binding.pb.setVisibility(View.GONE);


        if (Constants.DEFAULT_CLASS_ID==-1){
            getData(1);
        }else {
            getData(Constants.DEFAULT_CLASS_ID);
        }


    }


    private void setTopData(){

        SubName = getIntent().getExtras().getString("sunject_name");
        subId = getIntent().getExtras().getInt("sunject_id");

        binding.selectedHeaderTitle.setText(SubName);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = "";
        if (totalStudents != 0) {
            subTitle = String.valueOf(totalStudents) + " ";
        }
        Utility.SetToolbar(context, "Syllabus", subTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);

        return true;
    }*/

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

    private void setData(LearnSubjectDataModel model){
        binding.selectedHeaderSubTitle.setText(model.subjectLearningCounts);
        binding.tvSyllabus.setText(model.syallbus.title);
        binding.tvSyllabusSub.setText(model.syallbus.sharedBy);
    }


    private void getData(int classId) {
        userModel.setIsLoading(true);
        listModels.clear();

        service.getLearnSubjectDetails(userModel.getUserId(),subId,classId, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<LearnSubjectDetailsBean>() {
            @Override
            public void onResponse(Call<LearnSubjectDetailsBean> call, Response<LearnSubjectDetailsBean> response) {
                if (response.body() != null) {

                    LearnSubjectDetailsBean bean = response.body();
                    Log.e("data", "<<response>>"+response.body().data);
                    if (bean.data != null && bean.data.chapters.size()>0) {
                        setData(bean.data);
                        listModels.addAll(bean.data.chapters);

                        adapter.notifyDataSetChanged();
                    }
                }
                userModel.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<LearnSubjectDetailsBean> call, Throwable t) {
                userModel.setIsLoading(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
