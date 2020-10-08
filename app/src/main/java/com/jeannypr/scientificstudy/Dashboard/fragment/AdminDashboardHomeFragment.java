package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.navigator.MainNavigator;
import com.jeannypr.scientificstudy.Dashboard.adapter.DashboardHomeTabAdapter;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabBean;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabDataListModel;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardHomeTabNavigator;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.activity.RouteListActivity;
import com.jeannypr.scientificstudy.databinding.FragmentHomeTabBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardHomeFragment extends Fragment implements DashboardHomeTabNavigator, View.OnClickListener {
    private Context context;
    private UserModel userModel;
    private AppSettingService appSettingService;
    private FragmentHomeTabBinding mViewBinding;
    private DashboardHomeTabAdapter adapter;
    private ArrayList<HomeTabDataListModel> list;
    private MainNavigator mNavigator;

   /* public AdminDashboardHomeFragment(MainNavigator navigator) {
        mNavigator = navigator;
    }*/

    @Override
    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
            mNavigator = (MainNavigator) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mNavigator = (MainNavigator) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public AdminDashboardHomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        appSettingService = new DataRepo<>(AppSettingService.class, context).getService();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_tab, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNavigator.checkSessionExpiry();

        UserPreference userPref = UserPreference.getInstance(context);
        SchoolDetailModel schoolData = userPref.getSchoolData();
        userModel = userPref.getUserData();

        list = new ArrayList<>();
        adapter = new DashboardHomeTabAdapter(context, list, this);
        mViewBinding.recyclerView.setAdapter(adapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        getData();
        bindLoggedInUserData();
        mViewBinding.notifyIc.setOnClickListener(this);
        mViewBinding.busIc.setOnClickListener(this);
        mViewBinding.helpIc.setOnClickListener(this);

        //Set school logo
        if (schoolData != null && schoolData.SchoolLogo != null && !schoolData.SchoolLogo.equals("")) {
            Glide.with(context).load(schoolData.SchoolLogo).into(mViewBinding.sclLogo);
        } else {
            Glide.with(context).load(R.drawable.default_school).into(mViewBinding.sclLogo);
        }

        mViewBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getData();
            }
        });
    }

    private void bindLoggedInUserData() {
//        mViewBinding.familyLbl.setText(userModel.getFamilyName().equals("") ? getString(R.string.myFamily) : userModel.getFamilyName());
        mViewBinding.pencilIc.setVisibility(View.GONE);
        mViewBinding.familyLbl.setVisibility(View.GONE);

        mViewBinding.selectedChildName.setText(userModel.getFirstName());
        if (!userModel.getUserImagePath().equals("")) {
            Glide.with(context).load(Constants.IMAGE_BASE_URL + userModel.getUserImagePath()).into(mViewBinding.selectedChildImg);
        } else {
            mViewBinding.selectedChildImg.setImageResource(R.mipmap.profile_md);
        }

    }

    private void getData() {
        userModel.setIsLoading(true);

        appSettingService.GetHomeTabDetails(userModel.getUserId(), userModel.getSchoolId(), userModel.getAcademicyearId(), userModel.getRoleTitle()).enqueue(new Callback<HomeTabBean>() {
            @Override
            public void onResponse(Call<HomeTabBean> call, Response<HomeTabBean> response) {
                if (response.body() != null) {

                    HomeTabBean bean = response.body();
                    if (bean.data != null) {

                        Collections.sort(bean.data, new Comparator<HomeTabDataListModel>() {
                            @Override
                            public int compare(HomeTabDataListModel lhs, HomeTabDataListModel rhs) {
                                return lhs.getPriority().compareTo(rhs.getPriority());
                            }
                        });

                        for (HomeTabDataListModel datum : bean.data) {
                            list.add(datum);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                userModel.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<HomeTabBean> call, Throwable t) {
                userModel.setIsLoading(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setReminder(int eventId, String eventEndDate, String eventType) {
        mNavigator.setReminder(eventId, eventEndDate, eventType);
    }

    @Override
    public void checkIn(int eventId, int childAdapterPosition, int parentAdapterPosition) {
        mNavigator.checkIn(eventId, childAdapterPosition, parentAdapterPosition);
    }

    @Override
    public void rsvp(int eventId, String rsvp) {
        mNavigator.rsvp(eventId, rsvp);
    }

    @Override
    public void showFullDesc(String desc, String title) {
        mNavigator.showFullDesc(desc, title);
    }

    @Override
    public void showFullDesc(String desc, String title, String startDate) {
        mNavigator.showFullDesc(desc, title, startDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notifyIc:
                mNavigator.redirectToNotification();
                break;

            case R.id.busIc:
                Intent intent = new Intent(context, RouteListActivity.class);
                startActivity(intent);
                break;

            case R.id.helpIc:
                mNavigator.openLinkInSystemBrowser(Constants.ADMIN_TEACHER_HELP_URL, R.string.helpUrlError);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public void updateCheckInStatus(int parentAdapterPosition, int childAdapterPosition) {
        HomeTabItemDetail item = list.get(parentAdapterPosition).getFeed().get(childAdapterPosition);
        item.getExtraKeys().setCheckedIn(true);
        adapter.notifyItemChanged(parentAdapterPosition, item);
    }

    @Override
    public void openBrowserInApp(String url, String title, String subtitle, int errorMsg) {
        mNavigator.openInAppBrowser(url, title, subtitle, errorMsg);
    }

    @Override
    public void openLinkInSystemBrowser(String url, int errorMsg) {
        mNavigator.openLinkInSystemBrowser(url,errorMsg);
    }
}