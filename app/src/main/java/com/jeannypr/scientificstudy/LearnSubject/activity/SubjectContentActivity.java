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
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.LearnSubject.adapter.SubjectContentLinkAdapter;
import com.jeannypr.scientificstudy.LearnSubject.adapter.SubjectContentPdfAdapter;
import com.jeannypr.scientificstudy.LearnSubject.adapter.SubjectContentVideosAdapter;
import com.jeannypr.scientificstudy.LearnSubject.api.LearnSubjectService;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectContentBean;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectContentListModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivitySubjectContentBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectContentActivity extends AppCompatActivity implements View.OnClickListener {
    private LearnSubjectService service;
    UserModel userData;
    private SubjectContentPdfAdapter adapter;

    private SubjectContentVideosAdapter videosAdapter;

    private SubjectContentLinkAdapter linkAdapter;

    private Context context;
    private ArrayList<ClassModel> classes;
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
    ActivitySubjectContentBinding binding;
    private int subId, chepterId;

    private  ArrayList<LearnSubjectContentListModel> pdf;
    private  ArrayList<LearnSubjectContentListModel> video;
    private  ArrayList<LearnSubjectContentListModel> link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_content);
        context = this;

        subId = getIntent().getExtras().getInt("SubjectId");
        chepterId = getIntent().getExtras().getInt("chepterId");

        pdf = new ArrayList<>();
        video = new ArrayList<>();
        link = new ArrayList<>();
        service = new DataRepo<>(LearnSubjectService.class, context).getService();

        SetToolbar();
        pb = findViewById(R.id.progressBar);
        UserPreference userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        schoolCode = userPref.getSchoolCode();
        userModel = userPref.getUserData();

        adapter = new SubjectContentPdfAdapter(this, SubjectContentActivity.this,pdf);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        binding.recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        binding.recyclerView.setAdapter(adapter);


        videosAdapter = new SubjectContentVideosAdapter(this, SubjectContentActivity.this,video);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        binding.rvVideo.setLayoutManager(linearLayoutManager1); // set LayoutManager to RecyclerView
        binding.rvVideo.setAdapter(videosAdapter);

        linkAdapter = new SubjectContentLinkAdapter(this, link);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        binding.rvLink.setLayoutManager(linearLayoutManager2); // set LayoutManager to RecyclerView
        binding.rvLink.setAdapter(linkAdapter);


       /* userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();
        schoolCode = userPreference.getSchoolCode();*/



        if (Constants.DEFAULT_CLASS_ID==-1){
            getData(1);
        }else {
            getData(Constants.DEFAULT_CLASS_ID);
        }

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
        Utility.SetToolbar(context, "Syllabus Contents", subTitle);
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


    private void getData(int classId) {
        userModel.setIsLoading(true);
        //listModels.clear();

        service.getLearnSubjectContent(userModel.getUserId(),subId,classId, userModel.getSchoolId(), userModel.getAcademicyearId(),chepterId).enqueue(new Callback<LearnSubjectContentBean>() {
            @Override
            public void onResponse(Call<LearnSubjectContentBean> call, Response<LearnSubjectContentBean> response) {
                if (response.body() != null) {

                    LearnSubjectContentBean bean = response.body();
                    Log.e("data", "<<response>>"+response.body().data);
                    if (bean.data != null) {

                        binding.tvTitle.setText(bean.data.chapterName);

                        if (bean.data.getDocuments()!=null && bean.data.getDocuments().size()>0){

                            pdf.addAll(bean.data.documents);
                        }

                        if (bean.data.getVideos()!=null && bean.data.getVideos().size()>0){


                            video.addAll(bean.data.videos);
                        }

                        if (bean.data.getLinks()!=null && bean.data.getLinks().size()>0){

                            link.addAll(bean.data.links);

                        }
                        adapter.notifyDataSetChanged();

                        videosAdapter.notifyDataSetChanged();
                        linkAdapter.notifyDataSetChanged();


                       // setData(bean.data);
                        //listModels.addAll(bean.data.chapters);
                       /* Collections.sort(bean.data, new Comparator<HomeTabDataListModel>() {
                            @Override
                            public int compare(HomeTabDataListModel lhs, HomeTabDataListModel rhs) {
                                return lhs.getPriority().compareTo(rhs.getPriority());
                            }
                        });*/


                        /*for (LearnSubjectDetailsListModel datum : bean.data) {
                            listModels.add(datum);
                        }*/
                        adapter.notifyDataSetChanged();
                    }
                }
                userModel.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<LearnSubjectContentBean> call, Throwable t) {
                userModel.setIsLoading(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
