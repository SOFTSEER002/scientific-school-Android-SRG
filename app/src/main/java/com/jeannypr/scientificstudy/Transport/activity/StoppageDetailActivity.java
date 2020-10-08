package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.adapter.StoppageDetailAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.RouteDetailBean;
import com.jeannypr.scientificstudy.Transport.model.RouteDetailModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoppageDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TransportService transportService;
    private Context context;
    UserModel userdata;
    String stoppageName;
    //  private ProgressDialog p_dialog;
    RecyclerView routeDetailContainer;
    List<RouteDetailModel> stoppageDetailModels;
    StoppageDetailAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_route_details);
        userdata = UserPreference.getInstance(context).getUserData();
        transportService = new DataRepo<>(TransportService.class, context).getService();
        stoppageName = getIntent().getStringExtra("stoppageName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, stoppageName, "");
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(stoppageName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        routeDetailContainer = findViewById(R.id.routeDetailContainer);
        progressBar = findViewById(R.id.progressBar);

        stoppageDetailModels = new ArrayList<>();
        adapter = new StoppageDetailAdapter(this, stoppageDetailModels);
        routeDetailContainer.setAdapter(adapter);

        loadData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    private void showLoader(String msg) {
//        p_dialog = Utility.showProgressDialog(context, msg);
//    }
//
//    private void hideLoader() {
//        p_dialog.dismiss();
//    }


    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        transportService.GetStoppageDetail(userdata.getSchoolId(), userdata.getAcademicyearId(), stoppageName).enqueue(new Callback<RouteDetailBean>() {
            @Override
            public void onResponse(Call<RouteDetailBean> call, Response<RouteDetailBean> response) {
                RouteDetailBean stoppageDetailBean = response.body();
                if (stoppageDetailBean != null) {

                    if (stoppageDetailBean.rcode == Constants.Rcode.OK) {
                        stoppageDetailModels.clear();
                        if (stoppageDetailBean.data != null) {
                            for (RouteDetailModel route : stoppageDetailBean.data) {
                                stoppageDetailModels.add(route);
                            }
                            adapter.notifyDataSetChanged();
                        }


                    } else if (stoppageDetailBean.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                        //TODO: norecord msg
                    } else {
                        Toast.makeText(context, "Stoppage Detail could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RouteDetailBean> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Stoppage detail could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
