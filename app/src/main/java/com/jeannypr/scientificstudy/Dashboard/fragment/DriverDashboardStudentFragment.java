package com.jeannypr.scientificstudy.Dashboard.fragment;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Dashboard.adapter.StudentListForDriverAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.AssignedRouteModel;
import com.jeannypr.scientificstudy.Transport.model.RouteDetailBean;
import com.jeannypr.scientificstudy.Transport.model.RouteDetailModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboardStudentFragment extends Fragment {

    Context context;
    private View view;
    TransportService service;
    UserModel userModel;
    UserPreference userPre;
    StudentListForDriverAdapter adapter;
    LinearLayout noRecord;
    TextView noRecordMsg;
    ProgressBar pb;
    SwipeRefreshLayout swipeToRefresh;
    private String TAG = DriverDashboardStudentFragment.class.getSimpleName();
    RecyclerView contactListContainer;
    ArrayList<RouteDetailModel> studentListModel;
    ArrayList<AssignedRouteModel> assignedRouteModel;
    int routeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_student_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPre = UserPreference.getInstance(context);
        userModel = userPre.getUserData();
        service = new DataRepo<>(TransportService.class, context).getService();

        pb = view.findViewById(R.id.progressBar);
        noRecord = view.findViewById(R.id.noRecord);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);
        contactListContainer = view.findViewById(R.id.driverListContainer);

        assignedRouteModel = new ArrayList<>();
        studentListModel = new ArrayList<>();
        adapter = new StudentListForDriverAdapter(context, studentListModel);
        contactListContainer.setAdapter(adapter);

        routeId = userPre.getJourneyDetail().getRouteId();
        //AssignedRoute();
        ShowStudentList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void ShowStudentList() {
        pb.setVisibility(View.VISIBLE);

        service.GetRouteDetail(userModel.getSchoolId(), userModel.getAcademicyearId(), routeId).enqueue(new Callback<RouteDetailBean>() {
            @Override
            public void onResponse(Call<RouteDetailBean> call, Response<RouteDetailBean> response) {
                RouteDetailBean bean = response.body();

                studentListModel.clear();

                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {

                        if (bean.data != null) {

                            for (RouteDetailModel model : bean.data) {
                                studentListModel.add(model);
                            }

                        }
                        adapter.notifyDataSetChanged();

                    } else if (bean.rcode == Constants.Rcode.NORECORDS) {
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
            public void onFailure(Call<RouteDetailBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private void AssignedRoute() {

        pb.setVisibility(View.VISIBLE);

        service.GetAssignedRoute(userModel.getUserId(), userModel.getSchoolId()).enqueue(new Callback<AssignedRouteBean>() {
            @Override
            public void onResponse(Call<AssignedRouteBean> call, Response<AssignedRouteBean> response) {
                AssignedRouteBean bean = response.body();

                if (bean != null) {
                    assignedRouteModel.clear();
                    if (bean.rcode == Constants.Rcode.OK) {

                        if (bean.data != null) {

                            for (AssignedRouteModel model : bean.data) {
                                routeId = model.getId();
                                assignedRouteModel.add(model);

                            }
                        }
                    } else {
                        Log.e(TAG, getResources().getString(R.string.somethingWrongMsg));
                    }

                } else {
                    Log.e(TAG, getResources().getString(R.string.somethingWrongMsg));
                }
                ShowStudentList();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AssignedRouteBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Log.e(TAG, getResources().getString(R.string.somethingWrongMsg));
            }
        });
    }*/
}