package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.navigator.MainNavigator;
import com.jeannypr.scientificstudy.Dashboard.adapter.DashboardHomeTabAdapter;
import com.jeannypr.scientificstudy.Dashboard.adapter.FamilyMembersAdapter;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.FamilyMembersModel;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabBean;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabDataListModel;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardHomeTabNavigator;
import com.jeannypr.scientificstudy.Login.activity.LoginActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.activity.StudentProfileActivity;
import com.jeannypr.scientificstudy.Transport.activity.TransportActivity;
import com.jeannypr.scientificstudy.databinding.FragmentHomeTabBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentDashboardHomeFragment extends Fragment implements DashboardHomeTabNavigator, View.OnClickListener {
    private View view;
    private Context context;
    private UserModel userModel;
    private AppSettingService appSettingService;
    private FragmentHomeTabBinding mViewBinding;
    private DashboardHomeTabAdapter adapter;
    private ArrayList<HomeTabDataListModel> list;
    private UserPreference userPref;
    private MainNavigator mNavigator;
    List<ChildModel> children;
    ChildModel selectedChild;
    LayoutInflater inflater;
    int totalChildren;

    /* public ParentDashboardHomeFragment(MainNavigator navigator) {
         mNavigator = navigator;
     }*/
    @Override
    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
        mNavigator = (MainNavigator) activity;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            mNavigator = (MainNavigator) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public ParentDashboardHomeFragment() {
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
//        view = inflater.inflate(R.layout.fragment_home_tab, container, false);
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_tab, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mNavigator != null) mNavigator.checkSessionExpiry();

        userPref = UserPreference.getInstance(context);
        inflater = LayoutInflater.from(context);
        userModel = userPref.getUserData();

        mViewBinding.setViewModel(userModel);
        mViewBinding.sclLogo.setVisibility(View.GONE);
        list = new ArrayList<>();
        adapter = new DashboardHomeTabAdapter(context, list, this);
        mViewBinding.recyclerView.setAdapter(adapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        getData();
        bindLoggedInUserData();
        mViewBinding.notifyIc.setOnClickListener(this);
        mViewBinding.busIc.setOnClickListener(this);
        mViewBinding.helpIc.setOnClickListener(this);

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

    /**
     * Bind data of logged in user.
     */
    private void bindLoggedInUserData() {
        mViewBinding.familyLbl.setText(userModel.getFamilyName().equals("") ? getString(R.string.myFamily) : userModel.getFamilyName());

        children = userPref.getChildren();
        selectedChild = userPref.getSelectedChild();
        IsChildSelected();
        bindFamilyMembersData();
    }

    /**
     * check if any child is active.
     */
    private void IsChildSelected() {
        if (children != null) {
            totalChildren = children.size();

            if (totalChildren == 1) {
                if (selectedChild == null) {

                    userPref.setSelectedChild(children.get(0));
                    selectedChild = userPref.getSelectedChild();

                    bindSelectedChildData();

                } else {
                    bindSelectedChildData();
                }

            } else if (totalChildren > 1) {
                if (selectedChild == null) {
                    chooseChild(children);
                } else {
                    bindSelectedChildData();
                }
            } else {
                HavingNoChild();
            }
        } else {
            HavingNoChild();
        }
    }

    /**
     * Show alert if no child found.
     */
    private void HavingNoChild() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.choose_child_alert, mViewBinding.homeTabContainer, false);
        builder.setTitle(getString(R.string.noChildFound));
        builder.setMessage(getString(R.string.contactAdmin));
        mViewBinding.homeTabContainer.addView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userPref.logOut();
                Intent loginIntent = new Intent(context, LoginActivity.class);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });

        builder.show();
    }

    /**
     * Allow user to choose child to make it active.
     *
     * @param children
     */
    private void chooseChild(final List<ChildModel> children) {

        final ArrayList<CardView> chooseChildAlertRows = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();

        alertDialog.setTitle(getString(R.string.chooseChild));
        View alertView = inflater.inflate(R.layout.choose_child_alert, mViewBinding.homeTabContainer, false);
        LinearLayout alertRow = alertView.findViewById(R.id.childListContainerInAlert);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (selectedChild == null) {
                    ChildModel defaultChild = children.get(0);
                    userPref.setSelectedChild(defaultChild);
                    selectedChild = defaultChild;
                }
            }
        });

        if (children != null && totalChildren > 0) {
            int i = 0;

            for (final ChildModel child : children) {

                final CardView view = (CardView) inflater.inflate(R.layout.row_choose_child_alert, alertRow, false);

                final CardView row = view.findViewById(R.id.cardView);
                final TextView childName = view.findViewById(R.id.childName);
                final TextView className = view.findViewById(R.id.className);

                childName.setText(child.Name == null ? "" : child.Name);
                className.setText(child.ClassName == null ? "" : child.ClassName);

                chooseChildAlertRows.add(row);
                if (i == 0) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    childName.setTextColor(getResources().getColor(R.color.white));
                    className.setTextColor(getResources().getColor(R.color.white));
                }
                ++i;

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        for (CardView row : chooseChildAlertRows) {
                            row.setBackgroundColor(0x00000000);
                        }

                        row.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        childName.setTextColor(getResources().getColor(R.color.white));
                        className.setTextColor(getResources().getColor(R.color.white));

                        userPref.setSelectedChild(child);
                        selectedChild = child;
                        bindSelectedChildData();
                        alertDialog.cancel();
                        Toast.makeText(context, "You have chosen " + child.Name + "as default child", Toast.LENGTH_SHORT).show();
                    }
                });

                alertRow.addView(view);
            }

        } else {
            HavingNoChild();
        }

        alertDialog.setView(alertView);
        alertDialog.show();
    }

    /**
     * Bind data of all family members and set click listner.
     */
    private void bindFamilyMembersData() {
        try {
            if (userPref.getFamilyMembersData() != null) {

                if (userPref.getFamilyMembersData().size() > 0) {
                    for (FamilyMembersModel datum : userPref.getFamilyMembersData()) {
                        ChildModel member = new ChildModel();
                        member.isStudent = false;
                        member.Name = datum.getName();
                        member.setStudentImagePath(datum.getImgPath());
                        member.setGender(datum.getGender());
                        children.add(member);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.print("NullPointerException Caught");
        }
        FamilyMembersAdapter adapter = new FamilyMembersAdapter(context, children, new FamilyMembersAdapter.FamilyMembersInterface() {

            @Override
            public void onClickChildRow(final ChildModel member) {
                if (member.isStudent) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final AlertDialog alertDialog = builder.create();

                    builder.setTitle(getString(R.string.viewProfileOrSwitchChild));
                    String viewProfileBtnLbl = "View " + member.Name + "'s Profile";
                    String switchChildBtnLbl = "Set " + member.Name + "as default child";

                    if (totalChildren > 1) {
                        builder.setPositiveButton(switchChildBtnLbl, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                userPref.setSelectedChild(member);
                                selectedChild = userPref.getSelectedChild();
                                bindSelectedChildData();
                                Toast.makeText(context, "You choose " + member.Name + "as default child", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                    builder.setNegativeButton(viewProfileBtnLbl, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            redirectToStudentsProfile(member);
                        }
                    });

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
                        }
                    });

                    builder.show();

                } else {
                    //redirect to parents profile.
                }
            }
        });
        mViewBinding.familyMembersRV.setAdapter(adapter);
    }

    /**
     * Bind data of selected child and set click listner.
     */
    private void bindSelectedChildData() {
        mViewBinding.selectedChildName.setText(selectedChild.Name + " (You)");
        Glide.with(context).load(Constants.IMAGE_BASE_URL + selectedChild.getStudentImagePath()).into(mViewBinding.selectedChildImg);

        mViewBinding.selectedChildImgRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToStudentsProfile(selectedChild);
            }
        });
    }

    /**
     * Call api to get data of home tab.
     */
    private void getData() {
        userModel.setIsLoading(true);

        appSettingService.GetHomeTabDetails(userModel.getUserId(), userModel.getSchoolId(), userModel.getAcademicyearId(), userModel.getRoleTitle()).enqueue(new Callback<HomeTabBean>() {
            @Override
            public void onResponse(@NonNull Call<HomeTabBean> call, @NonNull Response<HomeTabBean> response) {
                if (response.body() != null) {

                    HomeTabBean bean = response.body();
                    if (bean.data != null) {

                        Collections.sort(bean.data, new Comparator<HomeTabDataListModel>() {
                            @Override
                            public int compare(HomeTabDataListModel lhs, HomeTabDataListModel rhs) {
                                return lhs.getPriority().compareTo(rhs.getPriority());
                            }
                        });

                        list.clear();
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

        userModel.setIsLoading(false);
    }

    /**
     * Redirect to student's profile.
     *
     * @param member
     */
    private void redirectToStudentsProfile(ChildModel member) {
        Intent childProfileIntent = new Intent(context, StudentProfileActivity.class);
        childProfileIntent.putExtra("studentId", member.StudentId);
        childProfileIntent.putExtra("classId", member.ClassId);
        childProfileIntent.putExtra("studentName", member.Name);
        childProfileIntent.putExtra("className", member.ClassName);
        childProfileIntent.putExtra("imgPath", userPref.getSelectedChild().getStudentImagePath());
        startActivity(childProfileIntent);
    }

    @Override
    public void setReminder(int eventId, String eventEndDate, String eventType) {
        if (mNavigator != null) mNavigator.setReminder(eventId, eventEndDate, eventType);
    }

    @Override
    public void checkIn(int eventId, int childAdapterPosition, int parentAdapterPosition) {
        if (mNavigator != null)
            mNavigator.checkIn(eventId, childAdapterPosition, parentAdapterPosition);
    }

    @Override
    public void rsvp(int eventId, String rsvp) {
        if (mNavigator != null) mNavigator.rsvp(eventId, rsvp);
    }

    @Override
    public void showFullDesc(String desc, String title) {
        if (mNavigator != null) mNavigator.showFullDesc(desc, title);
    }

    @Override
    public void showFullDesc(String desc, String title, String startDate) {
        if (mNavigator != null) mNavigator.showFullDesc(desc, title, startDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notifyIc:
                if (mNavigator != null) mNavigator.redirectToNotification();
                break;

            case R.id.busIc:
                Intent transportIntent = new Intent(context, TransportActivity.class);
                transportIntent.putExtra(Constants.STUDENT_ID, selectedChild.StudentId);
                startActivity(transportIntent);
                break;

            case R.id.helpIc:
                if (mNavigator != null)
                    mNavigator.openLinkInSystemBrowser(Constants.PARENT_HELP_URL, R.string.helpUrlError);
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
        if (mNavigator != null) mNavigator.openInAppBrowser(url, title, subtitle, errorMsg);
    }

    @Override
    public void openLinkInSystemBrowser(String url, int errorMsg) {
        if (mNavigator != null) mNavigator.openLinkInSystemBrowser(url, errorMsg);
    }
}
