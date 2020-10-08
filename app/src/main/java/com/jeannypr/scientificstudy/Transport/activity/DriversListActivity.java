package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.adapter.DriverDetailAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.DriverBean;
import com.jeannypr.scientificstudy.Transport.model.DriverModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityDriverListBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriversListActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    ActivityDriverListBinding binding;
    DriverDetailAdapter adapter;
    RecyclerView driverListContainer;
    TransportService service;
    UserModel userModel;
    UserPreference userPre;
    ArrayList<DriverModel> drivers;
    LinearLayout noRecord;
    TextView noRecordMsg;
    ProgressBar pb;
    SwipeRefreshLayout swipeToRefresh;
    int totalDriver = 0;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_list);

        userPre = UserPreference.getInstance(this);
        userModel = userPre.getUserData();
        service = new DataRepo<>(TransportService.class, this).getService();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SetToolbar();

        findViewById(R.id.reloadIc).setVisibility(View.GONE);
        findViewById(R.id.reloadTxt).setVisibility(View.GONE);

        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        driverListContainer = findViewById(R.id.driverListContainer);


        swipeToRefresh = findViewById(R.id.swipeRefreshLayout);
        swipeToRefresh.setOnRefreshListener(this);
        swipeToRefresh.post(new Runnable() {
            @Override
            public void run() {
                ShowDriverList();
            }
        });

        drivers = new ArrayList<>();
        adapter = new DriverDetailAdapter(this, drivers);
        driverListContainer.setAdapter(adapter);

        ShowDriverList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void ShowDriverList() {
        //   pb.setVisibility(View.VISIBLE);
        swipeToRefresh.setRefreshing(true);

        service.GetDrivers(userModel.getSchoolId()).enqueue(new Callback<DriverBean>() {
            @Override
            public void onResponse(Call<DriverBean> call, Response<DriverBean> response) {
                DriverBean bean = response.body();

                drivers.clear();

                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {
                        if (bean.data != null) {
                            totalDriver = bean.data.size();

                            for (DriverModel model : bean.data) {
                                drivers.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        SetToolbar();

                    } else if (bean.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);

                    } else {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                }

                //pb.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DriverBean> call, Throwable t) {
                //    pb.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    private void SetToolbar() {
        // setSupportActionBar(toolbar);
        String subTitle = "";
        if (totalDriver != 0) {
            if (totalDriver == 1) {
                subTitle = String.valueOf(totalDriver) + " driver";
            } else {
                subTitle = String.valueOf(totalDriver) + " drivers";
            }
        }
        Utility.SetToolbar(context, "Manage drivers", subTitle);
    }

    @Override
    public void onRefresh() {
        ShowDriverList();
    }
}