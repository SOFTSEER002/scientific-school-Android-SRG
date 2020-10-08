package com.jeannypr.scientificstudy.SptWall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Events.activity.AddEventPtmActivity;
import com.jeannypr.scientificstudy.Events.activity.AddNewsNoticeActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.adapter.SptWallAdapter;
import com.jeannypr.scientificstudy.SptWall.api.SptWallService;
import com.jeannypr.scientificstudy.SptWall.model.SptWallBean;
import com.jeannypr.scientificstudy.SptWall.model.SptWallModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivitySptWallBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SptWallActivity extends AppCompatActivity implements SptWallAdapter.OnClickPostListner {
    private Context context;
    UserModel userData;
    LayoutInflater layoutInflator;
    private LinearLayout noRecord;
    SptWallAdapter adapter;
    private TextView noRecordMsg;
    Toolbar toolbar;
    List<SptWallModel> modele;
    Boolean isAdmin;
    private String viewType, headerTitle;
    ActivitySptWallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spt_wall);

        viewType = getIntent().getExtras().getString("news");

        userData = UserPreference.getInstance(context).getUserData();
        layoutInflator = LayoutInflater.from(context);

        isAdmin = userData.getRoleTitle().equals(Constants.Role.ADMIN);

        if (viewType.equalsIgnoreCase(Constants.PostType.EVENT)) {
            headerTitle = getString(R.string.event_module);
            binding.lytTop.setBackgroundColor(Color.parseColor("#F1F8E9"));
            binding.lytEvent.setVisibility(View.VISIBLE);
            binding.newsRow.setVisibility(View.GONE);
            binding.republicDate.setText(getString(R.string.academicYear) + " " + userData.getAcademicYearName());

        } else {
            binding.lytEvent.setVisibility(View.GONE);
            binding.newsRow.setVisibility(View.VISIBLE);

            if (viewType.equalsIgnoreCase(Constants.PostType.NEWS)) {
                headerTitle = getString(R.string.news_module);
                binding.lytTop.setBackgroundColor(Color.parseColor("#D6EEFF"));
                binding.newsTitle.setText(R.string.schoolNews);

            } else {
                headerTitle = getString(R.string.notice_module);
                binding.lytTop.setBackgroundColor(Color.parseColor("#eeeeee"));
                binding.newsRow.setBackgroundColor(Color.parseColor("#eeeeee"));
                binding.newsTitle.setText(R.string.schoolNotice);
            }
            binding.newsDate.setText(getString(R.string.academicYear) + " " + userData.getAcademicYearName());

        }

        toolbar = findViewById(R.id.sptWallToolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, headerTitle, "");

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        modele = new ArrayList<>();
        adapter = new SptWallAdapter(context, modele, this, isAdmin);
        binding.posts.setAdapter(adapter);

        getPosts();
    }

    private void filterEvents(String eventType) {
        if (!eventType.equals(""))
            adapter.getFilter().filter(eventType);
    }

    private void getPosts() {
        SptWallService sptWallService = new DataRepo<>(SptWallService.class, context).getService();
        binding.progressBar.setVisibility(View.VISIBLE);

        sptWallService.GetNewsNoticeEventList(userData.getSchoolId(), userData.getRoleTitle()).enqueue(new Callback<SptWallBean>() {
            @Override
            public void onResponse(Call<SptWallBean> call, Response<SptWallBean> response) {
                SptWallBean sptWallBean = response.body();

                if (sptWallBean != null) {
                    if (sptWallBean.rcode == Constants.Rcode.OK) {
                        modele.clear();
                        SptWallModel sptWallModel = sptWallBean.data;

                        for (SptWallModel post : sptWallModel.Posts) {
                            String postType = post.PostType.toLowerCase();

                            if (postType.equals(viewType)) {
                                if (userData.getRoleTitle().equals(Constants.Role.ADMIN))
                                    modele.add(post);

                                else {
                                    if (post.IsPublished != null && post.IsPublished)
                                        modele.add(post);
                                }
                            }
                        }
                        if (modele.size() < 1) {
                            binding.posts.setVisibility(View.GONE);
                            binding.noRecordRow.noRecord.setVisibility(View.VISIBLE);
                            binding.noRecordRow.noRecordMsg.setText(getString(R.string.noRecordMsg));
                        }
                        adapter.notifyDataSetChanged();

                        if (viewType.equalsIgnoreCase(Constants.PostType.EVENT))
                            if (getIntent().hasExtra(Constants.FILTER_BY))
                                filterEvents(getIntent().getStringExtra(Constants.FILTER_BY));

                    } else if (sptWallBean.rcode == Constants.Rcode.NORECORDS) {
                        binding.posts.setVisibility(View.GONE);
                        binding.noRecordRow.noRecord.setVisibility(View.VISIBLE);
                        binding.noRecordRow.noRecordMsg.setText(getString(R.string.noRecordMsg));

                    } else {
                        Toast.makeText(context, getString(R.string.postErrorMsg), Toast.LENGTH_LONG).show();
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SptWallBean> call, Throwable t) {
                Toast.makeText(context, "Could not load posts!", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        getPosts();
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin)
            getMenuInflater().inflate(R.menu.add_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_nav:
                switch (viewType) {
                    case Constants.PostType.NEWS:
                    case Constants.PostType.NOTICE:
                        redirectToNewsNotice(0);
                        return true;

                    case Constants.PostType.EVENT:
                        redirectToEvent(0);
                        return true;

                    default:
                        return false;
                }

            default:
                return false;
        }
    }

    private void redirectToNewsNotice(int postId) {
        Intent newsIntent = new Intent(this, AddNewsNoticeActivity.class);
        newsIntent.putExtra(Constants.POST_TYPE, viewType);
        newsIntent.putExtra(Constants.POST_ID, postId);
        startActivity(newsIntent);
    }

    private void redirectToEvent(int postId) {
        Intent newsIntent = new Intent(this, AddEventPtmActivity.class);
        newsIntent.putExtra(Constants.POST_TYPE, viewType);
        newsIntent.putExtra(Constants.POST_ID, postId);
        startActivity(newsIntent);
    }

    @Override
    public void onClickNewsNotice(int postId) {
        redirectToNewsNotice(postId);
    }

    @Override
    public void onClickEvent(int postId) {
        redirectToEvent(postId);
    }

    @Override
    public void showSearchMsg(Boolean isDataFound) {
        if (isDataFound) {
            binding.posts.setVisibility(View.VISIBLE);
            binding.noRecordRow.noRecord.setVisibility(View.GONE);

        } else {
            binding.posts.setVisibility(View.GONE);
            binding.noRecordRow.noRecord.setVisibility(View.VISIBLE);

            binding.noRecordRow.noRecordIc.setImageResource(R.drawable.ic_search);
            binding.noRecordRow.noRecordMsg.setText(R.string.noResultFound);
            binding.noRecordRow.noRecordMsg2.setText(R.string.noResultFoundDesc);
        }
    }
}
