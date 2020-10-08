package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesModel;
import com.jeannypr.scientificstudy.Holiday.activity.HolidayActivity;
import com.jeannypr.scientificstudy.Notification.activity.NotificationsActivity;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.activity.SptWallActivity;
import com.jeannypr.scientificstudy.Utilities.SilentLogin;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

public class CommunicationModulesDashboardAdapter extends RecyclerView.Adapter {

    ArrayList<DashboardModulesModel> data;
    Context mContext;
    private DashBoardModulesAdapterNavigator adapterNavigator;
    ViewGroup parent;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        this.parent = parent;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grid, parent, false);
        return new CommunicationModulesDashboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DashboardModulesModel studentModel = data.get(position);
        studentModel.AdapterPosition = position;
        ((CommunicationModulesDashboardAdapter.ViewHolder) holder).bind(studentModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public CommunicationModulesDashboardAdapter(Context context, ArrayList<DashboardModulesModel> data, DashBoardModulesAdapterNavigator navigator) {
        super();
        this.data = data;
        this.mContext = context;
        this.adapterNavigator = navigator;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtGridLbl;
        ImageView gridIc;
        ConstraintLayout row;
        DashboardModulesModel menu;

        ViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.row);
            gridIc = view.findViewById(R.id.gridIc);
            txtGridLbl = view.findViewById(R.id.gridLbl);
        }

        public void bind(final DashboardModulesModel menu) {
            this.menu = menu;
            txtGridLbl.setText(menu.getModuleName());

            setImageName(menu.getModuleName());
            row.setOnClickListener(this);
        }

        /**
         * Set image name as per module name like ic_modulename_module.
         *
         * @param moduleName name of modules
         */
        private void setImageName(String moduleName) {
            String[] arr = moduleName.toLowerCase().split(" ");
            String imgName = TextUtils.join("_", arr);
            int id = mContext.getResources().getIdentifier("ic_" + imgName + "_module", "drawable", mContext.getPackageName());

            if (id == 0) {
                gridIc.setImageResource(R.drawable.holidays);
            } else {
                gridIc.setImageResource(id);
            }
            Log.e("Adapter", "<<name>>" + menu.getModuleImg() + " <<id >>" + id + "<<Module name>>" + menu.getModuleName());
        }

        @Override
        public void onClick(View v) {
            Boolean isGranted = adapterNavigator.isModuleGranted(menu.getModuleId());
            Intent intent = null;

            switch (menu.getModuleName()) {

                case Constants.AdminModules.H_HOLIDAYS:
                    intent = new Intent(mContext, HolidayActivity.class);
//                    isGranted = true;
                    break;

                case Constants.AdminModules.H_NEWS:
                    intent = new Intent(mContext, SptWallActivity.class).putExtra("news", Constants.PostType.NEWS);
//                    isGranted = true;
                    break;

                case Constants.AdminModules.H_EVENT:
                    intent = new Intent(mContext, SptWallActivity.class).putExtra("news", Constants.PostType.EVENT);
//                    isGranted = true;
                    break;

                case Constants.AdminModules.H_NOTICE:
                    intent = new Intent(mContext, SptWallActivity.class).putExtra("news", Constants.PostType.NOTICE);
//                    isGranted = true;
                    break;

                case Constants.AdminModules.SURVEY:
                    if (!isGranted)
                        Utility.showAlertDialog(mContext, parent, mContext.getResources().getString(R.string.moduleNotGrantedHeader), mContext.getResources().getString(R.string.moduleNotGrantedMsg));
                    else
                        adapterNavigator.performSilentLogin(SilentLogin.SURVEY_URL, true);
                    break;

                case Constants.AdminModules.NOTIFICATION:
                    intent = new Intent(mContext, NotificationsActivity.class);
                    break;

                case Constants.AdminModules.HELPDESK:
                    if (!isGranted)
                        Utility.showAlertDialog(mContext, parent, mContext.getResources().getString(R.string.moduleNotGrantedHeader), mContext.getResources().getString(R.string.moduleNotGrantedMsg));
                    else
                        adapterNavigator.openLinkInSystemBrowser(Constants.ADMIN_TEACHER_HELP_URL, R.string.helpUrlError);
                    break;

               /* case Constants.AdminModules.LEAVE_MODULE:
                    isGranted = adapterNavigator.isApprover();
                    if (isGranted) {
                        intent = new Intent(mContext, LeaveModuleActivity.class);
                        intent.putExtra("isApprover", true);
                    }

                    break;*/
            }
            if (intent != null)
                setView(intent, isGranted, mContext.getResources().getString(R.string.moduleNotGrantedMsg));
        }

        private <A> void setView(A intent, Boolean isGranted, String alertMsg) {
            if (isGranted && intent != null)
                mContext.startActivity((Intent) intent);
            else
                Utility.showAlertDialog(mContext, parent, mContext.getResources().getString(R.string.moduleNotGrantedHeader), alertMsg);
        }
    }

    public interface DashBoardModulesAdapterNavigator {
        Boolean isModuleGranted(int selectedModuleId);

        Boolean isApprover();

        /**
         * Override to navigate to web browser.
         *
         * @param url
         * @param title
         * @param subtitle
         */
        void openBrowserInApp(String url, String title, String subtitle, int errorMsg);

        /**
         * Override to navigate to web browser.
         *
         * @param url
         */
        void openLinkInSystemBrowser(String url, int errorMsg);

        void performSilentLogin(String returnUrl, Boolean openLinkInWebView);
    }
}