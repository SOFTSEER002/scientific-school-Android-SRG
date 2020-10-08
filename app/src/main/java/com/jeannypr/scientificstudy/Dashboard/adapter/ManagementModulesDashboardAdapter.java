package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Class.activity.ClassListActivity;
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesModel;
import com.jeannypr.scientificstudy.Fee.activity.AllowDiscountActivity;
import com.jeannypr.scientificstudy.Fee.activity.FeeModuleActivity;
import com.jeannypr.scientificstudy.Inventory.activity.InventoryModuleActivity;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Registration.activity.RegistrationModuleActivity;
import com.jeannypr.scientificstudy.Teacher.activity.TeacherListActivity;
import com.jeannypr.scientificstudy.Transport.activity.TransportModuleActivity;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.leave.activity.LeaveModuleActivity;

import java.util.ArrayList;

public class ManagementModulesDashboardAdapter extends RecyclerView.Adapter {

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
        return new ManagementModulesDashboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DashboardModulesModel studentModel = data.get(position);
        studentModel.AdapterPosition = position;
        ((ManagementModulesDashboardAdapter.ViewHolder) holder).bind(studentModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ManagementModulesDashboardAdapter(Context context, ArrayList<DashboardModulesModel> data, DashBoardModulesAdapterNavigator navigator) {
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

        @Override
        public void onClick(View v) {
            Boolean isGranted = adapterNavigator.isModuleGranted(menu.getModuleId());
            Intent intent = null;

            switch (menu.getModuleName()) {

                case Constants.AdminModules.REGISTRATION_MODULE:
                    intent = new Intent(mContext, RegistrationModuleActivity.class);
                    break;

                case Constants.AdminModules.FEE_MODULE:
                    intent = new Intent(mContext, FeeModuleActivity.class);
                    break;

                case Constants.AdminModules.INVENTORY_MODULE:
                    intent = new Intent(mContext, InventoryModuleActivity.class);
                    intent.putExtra("moduleType", Constants.Module.INVENTORY);
                    break;

                case Constants.AdminModules.STAFF_MODULE:
                    intent = new Intent(mContext, TeacherListActivity.class);
                    break;

                case Constants.AdminModules.STUDENT_MODULE:
                    intent = new Intent(mContext, ClassListActivity.class);
                    break;

                case Constants.AdminModules.ACCOUNTS_MODULE:
                    intent = new Intent(mContext, InventoryModuleActivity.class);
                    intent.putExtra("moduleType", Constants.Module.ACCOUNTS);
                    break;

                case Constants.AdminModules.ALLOW_DISCOUNT:
                    intent = new Intent(mContext, AllowDiscountActivity.class);
                    break;

                case Constants.AdminModules.TRANSPORT_MODULE:
                    intent = new Intent(mContext, TransportModuleActivity.class);
                    break;

                case Constants.AdminModules.MY_LEAVE_MODULE:
                    intent = new Intent(mContext, LeaveModuleActivity.class);
                    intent.putExtra("isApprover", false);
                    break;

                case Constants.AdminModules.LEAVE_MODULE:
                    isGranted = adapterNavigator.isApprover();
                    if (isGranted) {
                        intent = new Intent(mContext, LeaveModuleActivity.class);
                        intent.putExtra("isApprover", true);
                    }

                    break;
            }
            setView(intent, isGranted, mContext.getResources().getString(R.string.moduleNotGrantedMsg));
        }

        private <A> void setView(A intent, Boolean isGranted, String alertMsg) {
            if (isGranted) {
                mContext.startActivity((Intent) intent);
            } else {
//                showAlert(alertMsg);
                Utility.showAlertDialog(mContext, parent, mContext.getResources().getString(R.string.moduleNotGrantedHeader), alertMsg);
            }
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

        @Deprecated
        private void showAlert(String msg) {
            Utility.showAlertDialog(mContext, parent, mContext.getResources().getString(R.string.moduleNotGrantedHeader), msg);
           /* final View view = LayoutInflater.from(mContext).inflate(R.layout.row_alert_dialog_buttons, parent, false);
            final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(mContext)
                    .setTitle(mContext.getResources().getString(R.string.moduleNotGrantedHeader))
                    .setMessage(msg)
                    .setView(view)
                    .show();

            Button positiveBtn = view.findViewById(R.id.positiveBtn);
            Button negativeBtn = view.findViewById(R.id.negativeBtn);
            negativeBtn.setVisibility(View.GONE);

            positiveBtn.setText(mContext.getResources().getString(R.string.dialogPositiveButtonOk));

            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });*/
        }
    }

    public interface DashBoardModulesAdapterNavigator {
        Boolean isModuleGranted(int selectedModuleId);

        Boolean isApprover();
    }
}