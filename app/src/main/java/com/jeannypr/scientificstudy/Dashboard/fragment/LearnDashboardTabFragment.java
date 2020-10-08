package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Dashboard.adapter.DashboardLearnTabAdapter;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.LearnTabBean;
import com.jeannypr.scientificstudy.Dashboard.model.LearnTabDataListModel;
import com.jeannypr.scientificstudy.Dashboard.navigator.LearnTabNavigator;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.FragmentLearnDashboardTabBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearnDashboardTabFragment extends Fragment implements View.OnClickListener, LearnTabNavigator {
    private Context context;
    private UserPreference userPref;
    private UserModel userModel;
    private SchoolDetailModel schoolData;
    private View view;
    //private LearnDashboardToolListAdapter adapter;
    private RecyclerView recyclerView, recyclerViewList;
    private FragmentLearnDashboardTabBinding mViewBinding;
    private AppSettingService appSettingService;
    private ArrayList<LearnTabDataListModel> list;
    private DashboardLearnTabAdapter adapter;

    private int ClassId=-1;

    String reportType, moduleType;
   // ConstraintLayout dayBookBtn, purchaseSaleBtn, ledgerReportBtn, saleSummaryBtn;
    ImageView dayBookIc, purchaseSaleIc, saleSummaryIc, ledgerIc;
    TextView dayBook, purchaseSale, saleSummary, saleLeger;
    View purchaseDiv, saleDiv, ledgerDiv;

    public LearnDashboardTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        appSettingService = new DataRepo<>(AppSettingService.class, context).getService();
        setHasOptionsMenu(true);
        /*Bundle bundle = getArguments();
        if (bundle != null) {
            reportType = bundle.getString("transactionType");
            moduleType = bundle.getString("moduleType");
        }*/


      /*  Bundle bundle = this.getArguments();
        if (bundle != null) {
            ClassId = bundle.getInt("Id");

        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_learn_dashboard_tab,
                container, false);

        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_learn_dashboard_tab, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        schoolData = userPref.getSchoolData();

        setTopData();

        list = new ArrayList<>();
        adapter = new DashboardLearnTabAdapter(context, list, this);
        mViewBinding.recyclerView.setAdapter(adapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //classes = new ArrayList<>();


        if (Constants.DEFAULT_CLASS_ID==-1){
            getData(1);
        }else {
            getData(Constants.DEFAULT_CLASS_ID);
        }



    }


    private void setTopData(){
        getTimeFromAndroid();

        mViewBinding.selectedHeaderSubTitle.setText(userModel.getFirstName()+" "+userModel.getLastName());

    }


    private void getTimeFromAndroid() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            mViewBinding.selectedHeaderTitle.setText("Good Morning");
           // Toast.makeText(getContext(), "Good Morning", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            mViewBinding.selectedHeaderTitle.setText("Good Afternoon");
            //Toast.makeText(getContext(), "Good Afternoon", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            mViewBinding.selectedHeaderTitle.setText("Good Evening");
            //Toast.makeText(getContext(), "Good Evening", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            mViewBinding.selectedHeaderTitle.setText("Good Night");
           // Toast.makeText(getContext(), "Good Night", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View v) {

    }


    private void getData(int  ClassId) {
        userModel.setIsLoading(true);

        appSettingService.GetLearnTabDetails(userModel.getUserId(), userModel.getSchoolId(), userModel.getAcademicyearId(),ClassId).enqueue(new Callback<LearnTabBean>() {
            @Override
            public void onResponse(Call<LearnTabBean> call, Response<LearnTabBean> response) {
                if (response.body() != null) {

                    LearnTabBean bean = response.body();
                    if (bean.data != null) {

                        Collections.sort(bean.data, new Comparator<LearnTabDataListModel>() {
                            @Override
                            public int compare(LearnTabDataListModel lhs, LearnTabDataListModel rhs) {
                                return lhs.getPriority().compareTo(rhs.getPriority());
                            }
                        });

                        for (LearnTabDataListModel datum : bean.data) {
                            list.add(datum);
                        }

                       // Log.e("Class subject","<<Size>> " +bean.data.get(0).getDetails().getExtraKeys().getSubjects().get(1).subjectName);
                        adapter.notifyDataSetChanged();
                    }
                }
                userModel.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<LearnTabBean> call, Throwable t) {
                userModel.setIsLoading(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void showFullDesc(String desc, String title) {

    }

    @Override
    public void setClassesForMessenger(int adapterPosition) {

    }
}