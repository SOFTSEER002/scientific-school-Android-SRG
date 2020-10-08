package com.jeannypr.scientificstudy.Dashboard.fragment;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.adapter.EmergancyContactAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.EmergencyContactBean;
import com.jeannypr.scientificstudy.Transport.model.EmergencyContactModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboardEmergencyFragment extends Fragment {

    Context context;
    private View view;
    TransportService service;
    UserModel userModel;
    UserPreference userPre;
    EmergancyContactAdapter adapter;
    LinearLayout noRecord;
    TextView noRecordMsg;
    ProgressBar pb;
    SwipeRefreshLayout swipeToRefresh;
    RecyclerView contactListContainer;
    ArrayList<EmergencyContactModel> contactModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_emergency_contact, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPre = UserPreference.getInstance(context);
        userModel = userPre.getUserData();
        service = new DataRepo<>(TransportService.class, context).getService();

        pb = view.findViewById(R.id.progressBar);
        //swipeToRefresh = view.findViewById(R.id.swipeRefreshLayout);
        noRecord = view.findViewById(R.id.noRecord);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);
        contactListContainer = view.findViewById(R.id.driverListContainer);

        contactModels = new ArrayList<>();
        adapter = new EmergancyContactAdapter(context, contactModels);
        contactListContainer.setAdapter(adapter);
        showContactList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void showContactList() {
        pb.setVisibility(View.VISIBLE);
        //   swipeToRefresh.setRefreshing(true);

        service.GetContacts(userModel.getSchoolId()).enqueue(new Callback<EmergencyContactBean>() {
            @Override
            public void onResponse(Call<EmergencyContactBean> call, Response<EmergencyContactBean> response) {
                EmergencyContactBean bean = response.body();

                contactModels.clear();

                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {
                        if (bean.data != null) {

                            if (!bean.data.schoolContacts.getPhoneNumber1().equals("") || !bean.data.schoolContacts.getPhoneNumber2().equals("")) {
                                contactModels.add(bean.data.schoolContacts);
                            }

                            for (EmergencyContactModel model : bean.data.emergencyContacts) {
                                if (!model.getPhoneNumber1().equals("") || !model.getPhoneNumber2().equals("")) {
                                    contactModels.add(model);
                                }
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

                //  swipeToRefresh.setRefreshing(false);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<EmergencyContactBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                //swipeToRefresh.setRefreshing(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}