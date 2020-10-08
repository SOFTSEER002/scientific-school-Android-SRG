package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.adapter.StoppageSummaryAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.StoppageSummaryBean;
import com.jeannypr.scientificstudy.Transport.model.StoppageSummaryModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoppageSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    private BaseService BaseService;
    private TransportService transportService;
    private Context context;
    UserModel userdata;
    Button routeBtn, stoppageBtn, classBtn;
    List<StoppageSummaryModel> stoppageSummaryModels;
    //private ProgressDialog p_dialog;
    RecyclerView routeListContainer;
    StoppageSummaryAdapter adapter;
    ProgressBar progressBar;
    TextView routeLbl, noRecordMsg, studentLbl;
    LinearLayout noRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_route_summary);
        userdata = UserPreference.getInstance(context).getUserData();
        transportService = new DataRepo<>(TransportService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Stoppage Summary", "");
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Stoppage Summary");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        routeListContainer = findViewById(R.id.routeListContainer);
        progressBar = findViewById(R.id.progressBar);
        routeLbl = findViewById(R.id.routeLbl);
        routeLbl.setText("Stoppage");

        studentLbl = findViewById(R.id.studentLbl);
        studentLbl.setText("Students");

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        stoppageSummaryModels = new ArrayList<>();
        adapter = new StoppageSummaryAdapter(this, stoppageSummaryModels);
        routeListContainer.setAdapter(adapter);

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
        transportService.GetStoppageSummary(userdata.getSchoolId(), userdata.getAcademicyearId()).enqueue(new Callback<StoppageSummaryBean>() {
            @Override
            public void onResponse(Call<StoppageSummaryBean> call, Response<StoppageSummaryBean> response) {
                StoppageSummaryBean stoppageSummaryBean = response.body();
                if (stoppageSummaryBean != null) {

                    if (stoppageSummaryBean.rcode == Constants.Rcode.OK) {
                        stoppageSummaryModels.clear();

                        if (stoppageSummaryBean.data != null) {
                            for (StoppageSummaryModel route : stoppageSummaryBean.data) {
                                stoppageSummaryModels.add(route);
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else if (stoppageSummaryBean.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found");
                    } else {
                        Toast.makeText(context, "Stopppage Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StoppageSummaryBean> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Stoppage Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
