package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.button.MaterialButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.adapter.RouteJourneyAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.CurrentJourneyDetailBean;
import com.jeannypr.scientificstudy.Transport.model.RouteListBean;
import com.jeannypr.scientificstudy.Transport.model.RouteListModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityDriverListBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public Context context;
    ActivityDriverListBinding binding;
    RouteJourneyAdapter adapter;
    ArrayList<RouteListModel> routeListModels;
    TransportService service;
    UserModel userModel;
    UserPreference userPre;
    LinearLayout noRecord;
    TextView noRecordMsg, reloadTxt;
    ImageView reloadIc;
    ProgressBar pb;
    RecyclerView routeList;
    int totalRoute = 0;
    String subTitle = "";
    SwipeRefreshLayout swipeToRefresh;
    CardView refreshCard;
    MaterialButton refreshBtn;
    ConstraintLayout driverRefreshBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_list);

        userPre = UserPreference.getInstance(this);
        userModel = userPre.getUserData();
        service = new DataRepo<>(TransportService.class, this).getService();

       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Route And Journey", subTitle);
*/

        SetToolbar();

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        pb = findViewById(R.id.progressBar);
        reloadIc = findViewById(R.id.reloadIc);
        reloadTxt = findViewById(R.id.reloadTxt);
        reloadTxt.setOnClickListener(this);
        reloadIc.setOnClickListener(this);
        refreshCard = findViewById(R.id.refreshCard);
        refreshCard.setOnClickListener(this);
        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(this);

        driverRefreshBtn = findViewById(R.id.driver_refreshBtn);
        driverRefreshBtn.setVisibility(View.VISIBLE);

        routeList = findViewById(R.id.driverListContainer);
        routeListModels = new ArrayList<>();

        adapter = new RouteJourneyAdapter(this, routeListModels, new RouteJourneyAdapter.ItemClickListner() {
            @Override
            public void OnClickCurrentJourneyBtn(final RouteListModel journey) {
            }
        });
        routeList.setAdapter(adapter);

        swipeToRefresh = findViewById(R.id.swipeRefreshLayout);
        swipeToRefresh.setOnRefreshListener(this);
        swipeToRefresh.post(new Runnable() {
            @Override
            public void run() {
                ShowRouteList();
            }
        });

        ShowRouteList();
    }

    private void GetCurrentJourneyDetail() {
        //  pb.setVisibility(View.VISIBLE);
        swipeToRefresh.setRefreshing(true);
        //  final CurrentJourneyDetailModel[] res = {new CurrentJourneyDetailModel()};

        for (final RouteListModel route : routeListModels) {
            swipeToRefresh.setRefreshing(true);
            service.GetCurrentJourneyDetail(userModel.getSchoolId(), route.getRouteId()).enqueue(new Callback<CurrentJourneyDetailBean>() {
                @Override
                public void onResponse(Call<CurrentJourneyDetailBean> call, Response<CurrentJourneyDetailBean> response) {
                    //  CurrentJourneyDetailModel model = new CurrentJourneyDetailModel();

                    if (response.body() != null) {
                        CurrentJourneyDetailBean bean = response.body();

                        if (bean.data != null) {
                            if (bean.rcode == Constants.Rcode.OK) {
                                route.setCurrentJourneyDetail(bean.data);
                                adapter.notifyItemChanged(route.AdapterPosition, route);
                            }
                        }
                    }
                    swipeToRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<CurrentJourneyDetailBean> call, Throwable t) {
                    //  pb.setVisibility(View.GONE);
                    //  Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                    swipeToRefresh.setRefreshing(false);
                }
            });
        }
        /*adapter.notifyDataSetChanged();*/
        /*swipeToRefresh.setRefreshing(false);*/
        //return res[0];
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = "";
        if (totalRoute != 0) {
            if (totalRoute == 1) {
                subTitle = String.valueOf(totalRoute) + " route";
            } else {
                subTitle = String.valueOf(totalRoute) + " routes";
            }
        }
        Utility.SetToolbar(context, "Track location", subTitle);
    }

    private void ShowRouteList() {
        // pb.setVisibility(View.VISIBLE);
        swipeToRefresh.setRefreshing(true);
        service.GetRouteAndDriver(userModel.getSchoolId()).enqueue(new Callback<RouteListBean>() {
            @Override
            public void onResponse(Call<RouteListBean> call, Response<RouteListBean> response) {
                RouteListBean bean = response.body();

                routeListModels.clear();
                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {

                        if (bean.data != null) {
                            totalRoute = bean.data.size();

                            for (RouteListModel model : bean.data) {
                                //  model.setCurrentJourneyDetail(GetCurrentJourneyDetail(model));
                                routeListModels.add(model);
                            }
                            adapter.notifyDataSetChanged();
                            GetCurrentJourneyDetail();
                        }
                        // adapter.notifyDataSetChanged();
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

                //  pb.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RouteListBean> call, Throwable t) {
                //   pb.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        ShowRouteList();
        // GetCurrentJourneyDetail();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        GetCurrentJourneyDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reloadIc:
            case R.id.reloadTxt:
                ShowRouteList();
                break;
            case R.id.refreshCard:
            case R.id.refreshBtn:
                GetCurrentJourneyDetail();
                break;
        }
    }
}