package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.activity.MainActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.adapter.SeletctRouteAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.AssignedRouteBean;
import com.jeannypr.scientificstudy.Transport.model.AssignedRouteModel;
import com.jeannypr.scientificstudy.Transport.model.RouteBean;
import com.jeannypr.scientificstudy.Transport.model.RouteModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectRouteActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Chip r1, r2;
    TransportService service;
    UserModel userData;
    RecyclerView routeListContainer;
    ArrayList<RouteModel> routesList;
    SeletctRouteAdapter adapter;
    LinearLayout noRecord;
    TextView noRecordMsg, selectRoute;
    ProgressBar pb;
    int assignedRouteId = 0;
    String routeName;
    private final int VEHICLE_REQUEST_CODE = 201;
    private String TAG = SelectRouteActivity.class.getSimpleName();
    RadioButton radPickup, radDrop;
    boolean isPickup;
    View divSelectRoute;
    RadioGroup radioGroupMode;
    ArrayList<AssignedRouteModel> assignedRouteModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(TransportService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, getResources().getString(R.string.route_setToolbar), "");

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        routeListContainer = findViewById(R.id.routeListCountainer);
        pb = findViewById(R.id.progressBar);

        radDrop = findViewById(R.id.radDrop);
        radPickup = findViewById(R.id.radPickup);
        radioGroupMode = findViewById(R.id.radMode);

        radioGroupMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radPickup.setChecked(checkedId == R.id.radPickup);
                radDrop.setChecked(checkedId == R.id.radDrop);
                isPickup = checkedId == R.id.radPickup;

                selectRoute.setVisibility(View.VISIBLE);
                // divSelectRoute.setVisibility(View.VISIBLE);
                routeListContainer.setVisibility(View.VISIBLE);
            }
        });

        selectRoute = findViewById(R.id.lbl);
        divSelectRoute = findViewById(R.id.divider1);

       /* r1 = findViewById(R.id.r1);
        r2=findViewById(R.id.r2);

        r2.setOnClickListener(this);*/
        assignedRouteModel = new ArrayList<>();

        routesList = new ArrayList<>();

        //routeListContainer.setLayoutManager(new LinearLayoutManager(this));
        /*GetRoutes();*/
        AssignedRoute();
        routeListContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        switch (requestCode) {
            case VEHICLE_REQUEST_CODE:
                Intent intent = new Intent(context, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void GetRoutes() {

        adapter = new SeletctRouteAdapter(this, routesList, assignedRouteModel, new SeletctRouteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RouteModel model) {
                Intent vehicleIntent = new Intent(context, SelectVehicleActivity.class);
                vehicleIntent.putExtra("routeId", model.getRouteId());
                vehicleIntent.putExtra("routeName", model.getRouteName());
                vehicleIntent.putExtra("checked", isPickup);
                startActivityForResult(vehicleIntent, VEHICLE_REQUEST_CODE);
            }
        });
        routeListContainer.setAdapter(adapter);

        pb.setVisibility(View.VISIBLE);

        service.GetRoutes(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<RouteBean>() {
            @Override
            public void onResponse(Call<RouteBean> call, Response<RouteBean> response) {

                RouteBean resp = response.body();
                routesList.clear();

                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            for (RouteModel model : resp.data) {
                                routesList.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);

                    } else {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                }

                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RouteBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void AssignedRoute() {

        pb.setVisibility(View.VISIBLE);

        service.GetAssignedRoute(userData.getUserId(), userData.getSchoolId()).enqueue(new Callback<AssignedRouteBean>() {
            @Override
            public void onResponse(Call<AssignedRouteBean> call, Response<AssignedRouteBean> response) {
                AssignedRouteBean bean = response.body();

                if (bean != null) {
                    assignedRouteModel.clear();
                    if (bean.rcode == Constants.Rcode.OK) {

                        if (bean.data != null) {
                           /* int size = 0;
                            size = bean.data.size();*/

                            //   if (size <= 1) {

                            for (AssignedRouteModel model : bean.data) {
                                // assignedRouteId = model.getId();
                                assignedRouteModel.add(model);

                            }
                            //  }
                        }
                    } else {
                        Log.e(TAG, getResources().getString(R.string.somethingWrongMsg));
                        //Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.e(TAG, getResources().getString(R.string.somethingWrongMsg));
                    // Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                }
                GetRoutes();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AssignedRouteBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Log.e(TAG, getResources().getString(R.string.somethingWrongMsg));
            }
        });
    }

    @Override
    public void onClick(View v) {
     /*   switch (v.getId()) {

            case R.id.r2:
                r2.setChipBackgroundColorResource(R.color.colorPrimary);
                r2.setTextColor(getResources().getColor(R.color.white));

                r1.setChipBackgroundColorResource(R.color.black6);
                r1.setTextColor(getResources().getColor(R.color.black9));

                Intent vehicleIntent = new Intent(context, SelectVehicleActivity.class);
                startActivity(vehicleIntent);
                break;

            default:

                break;
        }*/
    }
}