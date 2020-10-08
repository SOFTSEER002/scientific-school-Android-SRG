package com.jeannypr.scientificstudy.Transport.adapter;


import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.activity.DriverProfileActivity;
import com.jeannypr.scientificstudy.Transport.model.DriverModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowDriverListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DriverDetailAdapter extends RecyclerView.Adapter<DriverDetailAdapter.MyViewHolder> {
    private Context mContext;
    private List<DriverModel> driverList;

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public DriverDetailAdapter(Context context, ArrayList<DriverModel> drivers) {
        super();
        mContext = context;
        driverList = drivers;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowDriverListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_driver_list, parent, false);
        return new DriverDetailAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DriverModel driverModel = driverList.get(position);
        ((MyViewHolder) holder).bind(driverModel);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowDriverListBinding itemBinding;
        String phoneNo, password, userName, message;
        private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

        ConstraintLayout parent;

        MyViewHolder(RowDriverListBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final DriverModel model) {
            itemBinding.setDriver(model);
            SetImg(model.getFirstName().charAt(0));

            if (!model.getMobile1().equals("")) {
                itemBinding.driverCallBtn.setVisibility(View.VISIBLE);
            } else {
                itemBinding.driverCallBtn.setVisibility(View.GONE);
            }

            itemBinding.driverCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                    dialPadIntent.setData(Uri.parse("tel:" + model.getMobile1()));
                    mContext.startActivity(dialPadIntent);
                }
            });

            itemBinding.driverProfile.setOnClickListener(this);
            itemBinding.shareCredentialBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    password = model.getUserPassword();
                    userName = model.getUserName();
                    phoneNo = model.getMobile1();

                    if (phoneNo.equals("")) {
                        Toast.makeText(mContext, "Please add contact number of " + model.getFirstName() + " to send sms.",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        ConstraintLayout row = (ConstraintLayout) inflater.inflate(R.layout.row_sms_confirmation, parent, false);
                        final TextView selctDriverName = row.findViewById(R.id.driverNameText);
                        final TextView selectUserName = row.findViewById(R.id.userNameTxt);
                        final TextView selectedPassword = row.findViewById(R.id.passwordTxt);

                        selctDriverName.setText(mContext.getResources().getString(R.string.trans_driverlist_dialog_dName) + " " + model.getFirstName().toUpperCase() + model.getLastName().toUpperCase() + "?");
                        selectUserName.setText(mContext.getResources().getString(R.string.usernameLbl) + " " + model.getUserName());
                        selectedPassword.setText(mContext.getResources().getString(R.string.passwordLbl) + " " + model.getUserPassword());

                        final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                                .setTitle(R.string.trans_driverlist_dialogTitle)
                                .setView(row)
                                .show();

                        Button positiveBtn = row.findViewById(R.id.positiveBtn);
                        Button negativeBtn = row.findViewById(R.id.negativeBtn);

                        positiveBtn.setText(R.string.vehicle_positiveBtn);
                        negativeBtn.setText(R.string.dialogNegativeButtonCancel);

                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                SendSMS();
                            }
                        });
                        negativeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                }
            });
        }

        private void SetImg(char firstLetter) {
            itemBinding.firstLetter.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = itemBinding.driverProfilePic.getBackground();

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(Utility.GetRandomMaterialColor("materialColor", mContext));

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(Utility.GetRandomMaterialColor("materialColor", mContext));

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(Utility.GetRandomMaterialColor("materialColor", mContext));
            }
        }


        public int getRandomColor() {
            Random rnd = new Random();
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.driverProfile:
                    Intent profileIntent = new Intent(mContext, DriverProfileActivity.class);
                    for (DriverModel driverDetails : driverList) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("details", driverDetails);
                        profileIntent.putExtras(bundle);
                    }
                    mContext.startActivity(profileIntent);
                    break;
            }
        }

        private void SendSMS() {
            String smsTitle, usernameTxt, passwordTxt;
            smsTitle = mContext.getResources().getString(R.string.trans_driverlist_Sms);
            usernameTxt = mContext.getResources().getString(R.string.usernameLbl);
            passwordTxt = mContext.getResources().getString(R.string.passwordLbl);

            String message = smsTitle + "\n" + usernameTxt + " " + userName + "\n" + passwordTxt + " " + password;

            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                //intent.setType();
                intent.setData(Uri.parse("smsto:" + phoneNo));  // This ensures only SMS apps respond
                intent.putExtra("sms_body", message);
                //intent.putExtra("address", phoneNo);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }

               /* SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(mContext, mContext.getResources().getString(R.string.successs_Sms), Toast.LENGTH_LONG).show();*/

            } catch (Exception ex) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.failed_Sms), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }
}