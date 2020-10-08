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
import com.jeannypr.scientificstudy.Transport.adapter.RouteSummaryAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.RouteSummaryBean;
import com.jeannypr.scientificstudy.Transport.model.RouteSummaryModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    private BaseService BaseService;
    private TransportService transportService;
    private Context context;
    UserModel userData;
    Button routeBtn, stoppageBtn, classBtn;
    List<RouteSummaryModel> routeSummaryModel;
    // private ProgressDialog p_dialog;
    RecyclerView routeListContainer;
    RouteSummaryAdapter adapter;
    ProgressBar progressBar;
    TextView routeLbl, noRecordMsg, studentLbl;
    private LinearLayout noRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_route_summary);
        userData = UserPreference.getInstance(context).getUserData();
        transportService = new DataRepo<>(TransportService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Route Wise Transport Details", "");

      /*  if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Route Wise Transport Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        routeListContainer = findViewById(R.id.routeListContainer);
        progressBar = findViewById(R.id.progressBar);
        routeLbl = findViewById(R.id.routeLbl);
        routeLbl.setText("Route");

        studentLbl = findViewById(R.id.studentLbl);
        studentLbl.setText("Students");

        routeSummaryModel = new ArrayList<>();
        adapter = new RouteSummaryAdapter(this, routeSummaryModel);
        routeListContainer.setAdapter(adapter);

        loadData();
    }

    @Override
    public void onClick(View v) {

    }

//    private void showLoader(String msg) {
//        p_dialog = Utility.showProgressDialog(context, msg);
//    }
//
//    private void hideLoader() {
//        p_dialog.dismiss();
//    }


    private void loadData() {
        // showLoader("Loading data. Please wait...");
        progressBar.setVisibility(View.VISIBLE);
        transportService.GetRouteSummary(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<RouteSummaryBean>() {
            @Override
            public void onResponse(Call<RouteSummaryBean> call, Response<RouteSummaryBean> response) {

                RouteSummaryBean routeSummaryBean = response.body();
                if (routeSummaryBean != null) {

                    if (routeSummaryBean.rcode == Constants.Rcode.OK) {
                        routeSummaryModel.clear();

                        if (routeSummaryBean.data != null) {
                            for (RouteSummaryModel route : routeSummaryBean.data) {
                                routeSummaryModel.add(route);
                            }
                            adapter.notifyDataSetChanged();
                        }


                    } else if (routeSummaryBean.rcode == Constants.Rcode.NORECORDS) {

                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found.");

                    } else {
                        Toast.makeText(context, "Route Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
//                hideLoader();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RouteSummaryBean> call, Throwable t) {
                // hideLoader();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Route Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
