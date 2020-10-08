package com.jeannypr.scientificstudy.Article.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jeannypr.scientificstudy.Article.adapter.ArticleTypeAdapter;
import com.jeannypr.scientificstudy.Article.api.ArticleService;
import com.jeannypr.scientificstudy.Article.model.ArticleCategoryBean;
import com.jeannypr.scientificstudy.Article.model.ArticleListDetailsModel;
import com.jeannypr.scientificstudy.Article.model.ArticleSaveBean;
import com.jeannypr.scientificstudy.Article.model.ArticleSaveModel;
import com.jeannypr.scientificstudy.Article.model.ArticleTypeModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityAddArticleBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArticleActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    ActivityAddArticleBinding binding;
    LinearLayout fabMenuLayout;
    private ArticleListDetailsModel model;
    MenuItem fabMenu;
    boolean isFABOpen = false;
    private ArrayList<ArticleTypeModel> articleTypeModels;
    private ArticleTypeAdapter adapter;

    private ArticleService service;
    private UserPreference userPreference;
    private UserModel userModel;
    ProgressBar pb;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_article);
        context = this;
        SetToolbar();
        articleTypeModels = new ArrayList<>();
        fabMenuLayout = findViewById(R.id.fabMenuLayout);
        pb = findViewById(R.id.progressBar);
        service = new DataRepo<>(ArticleService.class, context).getService();
        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();
        binding.saveActivityBtn.setOnClickListener(this);
        setArticle();
        //setTopData();

    }

    private void setArticle(){
        setRecycler();

        getData();
    }

    private void setRecycler(){
        adapter = new ArticleTypeAdapter(this,articleTypeModels);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1, GridLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ArticleTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                if (articleTypeModels.get(position).isStatus()){
                    articleTypeModels.get(position).setStatus(false);
                }else {
                    articleTypeModels.get(position).setStatus(true);
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = "";
       /* if (totalStudents != 0) {
            subTitle = String.valueOf(totalStudents) + " ";
        }*/
        Utility.SetToolbar(context, "Add Article", subTitle);
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

            case R.id.saveActivityBtn:

                if (check()){



                boolean isEmployeeSelected = false;
                for (ArticleTypeModel datum : articleTypeModels) {
                    if (datum.isStatus()) {
                        isEmployeeSelected = true;
                        break;
                    }
                }

                if (isEmployeeSelected) {

                    StringBuilder articlsId = new StringBuilder("");
                    for (ArticleTypeModel data : articleTypeModels) {
                        if (data.isStatus()) {
                            if (articlsId.toString().length() > 0) {
                                articlsId.append("," + data.id);
                            } else {
                                articlsId.append(data.id);
                            }
                        }
                    }


                    saveData(articlsId.toString());
                }else {
                    Toast.makeText(context, "Please select atleast one article", Toast.LENGTH_SHORT).show();
                }

                }
                break;

        }

    }

    private boolean check(){
        boolean valid = true;
        if (binding.topic.getText().toString().isEmpty()){
            binding.topic.setError("Please Enter Topic");
            valid = false;
        }else {
            binding.topic.setError(null);
        }


        if (binding.desc.getText().toString().isEmpty()){
            binding.desc.setError("Please Enter Description");
            valid = false;
        }else {
            binding.desc.setError(null);
        }


        return valid;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cw_hw_menu, menu);
        fabMenu = menu.findItem(R.id.fab);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.fab:

                isFABOpen = ShowFabMenu(isFABOpen);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public Boolean ShowFabMenu(Boolean isFABOpen) {
        if (!isFABOpen) {
            fabMenuLayout.setVisibility(View.VISIBLE);
            fabMenuLayout.bringToFront();
            fabMenu.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            DisableOtherViews();

            isFABOpen = true;
        } else {

            fabMenuLayout.setVisibility(View.GONE);
            fabMenu.setIcon(R.drawable.attachments3);
            EnableOtherViews();
            isFABOpen = false;
        }
        return isFABOpen;
    }


    public void DisableOtherViews() {
        //saveBtn.setVisibility(View.GONE);
        binding.saveActivityBtn.hide();
        binding.scrollView.setNestedScrollingEnabled(false);
        binding.scrollView.setEnabled(false);
        binding.recyclerView.setEnabled(false);
        binding.topic.setEnabled(false);
        binding.desc.setEnabled(false);
        //chooseAudienceBtn.setClickable(false);
        //submissionDateRow.setEnabled(false);
        //txtTopic.setEnabled(false);
        //txtDesc.setEnabled(false);
        //isAssignedChk.setEnabled(false);

    }

    public void EnableOtherViews() {
        // saveBtn.setVisibility(View.VISIBLE);
        binding.saveActivityBtn.show();
        binding.scrollView.setNestedScrollingEnabled(true);
        binding.scrollView.setEnabled(true);
        binding.recyclerView.setEnabled(true);
        binding.topic.setEnabled(true);
        binding.desc.setEnabled(true);
        //chooseAudienceBtn.setClickable(true);
        //submissionDateRow.setEnabled(true);
        //txtTopic.setEnabled(true);
        //txtDesc.setEnabled(true);
        //isAssignedChk.setEnabled(true);

    }

    private void saveData(String ids){
        ArticleSaveModel activityModel = new ArticleSaveModel();
        /* activityModel.Id = savedActivityId == -1 ? 0 : savedActivityId;*/
        activityModel.Id = 0;
        activityModel.AcademicYearId = userModel.getAcademicyearId();
        activityModel.SchoolId = userModel.getSchoolId();

        activityModel.SchoolCode = "dev";
        activityModel.Title = binding.topic.getText().toString();
        activityModel.Description = binding.desc.getText().toString();
        activityModel.ImageUrl = "kkh";
        activityModel.VideoUrl = "lmk";
        activityModel.Url = "nmnm";
        activityModel.UserId = 1;
        //activityModel.UserId = userModel.getUserId();
        activityModel.CategoryIds = ids;

        pb.setVisibility(View.VISIBLE);
        service.saveSaveArticle(activityModel).enqueue(new Callback<ArticleSaveBean>() {
            @Override
            public void onResponse(Call<ArticleSaveBean> call, Response<ArticleSaveBean> response) {
                if (response.body() != null) {
                    pb.setVisibility(View.GONE);

                    ArticleSaveBean bean = response.body();

                    if (bean != null && bean.rcode == Constants.Rcode.OK) {

                        Toast.makeText(context,  " created successfully", Toast.LENGTH_SHORT).show();
                        // saveBtn.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, " could not be created. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArticleSaveBean> call, Throwable t) {
                pb.setVisibility(View.GONE);

                Toast.makeText(context, " could not be created. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getData() {
        articleTypeModels.clear();
        pb.setVisibility(View.VISIBLE);

        service.getArticleCategory(userModel.getUserId(),1).enqueue(new Callback<ArticleCategoryBean>() {
            @Override
            public void onResponse(Call<ArticleCategoryBean> call, Response<ArticleCategoryBean> response) {
                if (response.body() != null) {

                    ArticleCategoryBean bean = response.body();

                    if (bean.data != null && bean.data.size()>0) {
                        articleTypeModels.addAll(bean.data);
                        pb.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<ArticleCategoryBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            isFABOpen = ShowFabMenu(isFABOpen);
        } else {
            super.onBackPressed();
        }
    }


}
