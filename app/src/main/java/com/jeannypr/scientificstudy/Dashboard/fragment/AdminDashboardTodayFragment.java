package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.Activity;
import android.content.Context;
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

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.navigator.MainNavigator;
import com.jeannypr.scientificstudy.Dashboard.adapter.DashboardTodayTabAdapter;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabBean;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabDataListModel;
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardTodayTabNavigator;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.FragmentTodayTabBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardTodayFragment extends Fragment implements DashboardTodayTabNavigator {
    private Context context;
    private UserPreference userPref;
    private SchoolDetailModel schoolData;
    private UserModel userModel;
    private AppSettingService appSettingService;
    private FragmentTodayTabBinding mViewBinding;
    private DashboardTodayTabAdapter adapter;
    private ArrayList<HomeTabDataListModel> list;
    private MainNavigator mNavigator;
    private ArrayList<DropDownModel> classes;

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
   /* public AdminDashboardTodayFragment(MainNavigator navigator) {
        mNavigator = navigator;
    }*/

    public AdminDashboardTodayFragment() {
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
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_today_tab, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPref = UserPreference.getInstance(context);
        schoolData = userPref.getSchoolData();
        userModel = userPref.getUserData();

        list = new ArrayList<>();
        adapter = new DashboardTodayTabAdapter(context, list, this,getChildFragmentManager());
        mViewBinding.recyclerView.setAdapter(adapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        classes = new ArrayList<>();

        getData();
//        setClasses();
    }

    private void getData() {
        userModel.setIsLoading(true);

        appSettingService.GetTodayTabDetails(userModel.getUserId(), userModel.getSchoolId(), userModel.getAcademicyearId(), userModel.getRoleTitle()).enqueue(new Callback<HomeTabBean>() {
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
    public void showFullDesc(String desc, String title) {
        mNavigator.showFullDesc(desc, title);
    }



    //    @Override
    private void setClasses() {
//        pb.setVisibility(View.VISIBLE);
//        final ArrayList<DropDownModel> classes = new ArrayList<>();

        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText("Select Class");
        defaultOption.setId(0);
        classes.add(defaultOption);

        BaseService baseService = new DataRepo<>(BaseService.class, context).getService();
        baseService.getClasses(userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<ClassModel> allClasses = resp.data;

                        for (ClassModel cls : allClasses) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(cls.Id);
                            dropDownModel.setText(cls.Name);
                            classes.add(dropDownModel);
                        }

//                        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.broadcast_fragment);
//                        if (fragment instanceof BroadcastMsgFragment) {
//                            ((BroadcastMsgFragment) fragment).setClasses(classes);
//                        }
                    } else {
                        Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
//                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
//                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
//        return classes;
    }

    //    @Override
    public ArrayList<DropDownModel> getClasses() {
        return classes;
    }

    @Override
    public void setClassesForMessenger(int adapterPosition) {
//        ConstraintLayout childView = (ConstraintLayout) mViewBinding.recyclerView.getChildAt(adapterPosition);
//        ViewPager viewPager = (ViewPager) childView.findViewById(R.id.viewpager);
//       Viewpager adapter =(ViewPa) viewPager.getAdapter()
//        if (fragment instanceof BroadcastMsgFragment) {
//            ((BroadcastMsgFragment) fragment).setClasses(classes);
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
