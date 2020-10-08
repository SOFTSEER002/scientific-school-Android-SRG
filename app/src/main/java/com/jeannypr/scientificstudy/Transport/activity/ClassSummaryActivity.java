package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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
import com.jeannypr.scientificstudy.Transport.adapter.ClassSummaryAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.ClassSummaryBean;
import com.jeannypr.scientificstudy.Transport.model.ClassSummaryModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    private BaseService BaseService;
    private TransportService transportService;
    private Context context;
    UserModel userdata;
    ImageView routeBtn, stoppageBtn, classBtn;
    List<ClassSummaryModel> classSummaryModels;
    // private ProgressDialog p_dialog;
    RecyclerView routeListContainer;
    ClassSummaryAdapter adapter;
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
        Utility.SetToolbar(context, "Class Summary", "");

       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Class Summary");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        routeListContainer = findViewById(R.id.routeListContainer);
        progressBar = findViewById(R.id.progressBar);
        routeLbl = findViewById(R.id.routeLbl);
        routeLbl.setText("Class");

        studentLbl = findViewById(R.id.studentLbl);
        studentLbl.setText("Students");

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        classSummaryModels = new ArrayList<>();
        adapter = new ClassSummaryAdapter(this, classSummaryModels);
        routeListContainer.setAdapter(adapter);

        loadData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        progressBar.setVisibility(View.VISIBLE);
        transportService.GetClassWiseSummary(userdata.getSchoolId(), userdata.getAcademicyearId()).enqueue(new Callback<ClassSummaryBean>() {
            @Override
            public void onResponse(Call<ClassSummaryBean> call, Response<ClassSummaryBean> response) {
                ClassSummaryBean classSummaryBean = response.body();
                if (classSummaryBean != null) {

                    if (classSummaryBean.rcode == Constants.Rcode.OK) {
                        classSummaryModels.clear();
                        if (classSummaryBean.data != null) {
                            for (ClassSummaryModel model : classSummaryBean.data) {
                                classSummaryModels.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else if (classSummaryBean.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found");

                    } else {
                        Toast.makeText(context, "Class wise transport Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClassSummaryBean> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Class wise transport Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
