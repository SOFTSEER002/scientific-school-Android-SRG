package com.jeannypr.scientificstudy.Transport.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.model.EmergencyContactModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowEmergancyContactBinding;

import java.util.List;

public class EmergancyContactAdapter extends RecyclerView.Adapter<EmergancyContactAdapter.MyViewHolder> {
    private Context mContext;
    private List<EmergencyContactModel> contactList;

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public EmergancyContactAdapter(Context context, List<EmergencyContactModel> contacts) {
        super();
        mContext = context;
        contactList = contacts;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowEmergancyContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_emergancy_contact, parent, false);
        return new EmergancyContactAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final EmergencyContactModel emergancyContactModel = contactList.get(position);
        ((MyViewHolder) holder).bind(emergancyContactModel);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowEmergancyContactBinding itemBinding;

        ConstraintLayout parent;

        MyViewHolder(RowEmergancyContactBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final EmergencyContactModel model) {
            itemBinding.setContact(model);
            String firstChar;
            String contactName = "";


            if (model.getName().equals("") && model.getPhoneNumber1().equals("") && model.getPhoneNumber2().equals("")) {
                return;
            }
            if (model.getName().equals("")) {
                contactName = ("Contact " + String.valueOf(getAdapterPosition() + 1));
                firstChar = "C";
            } else {
                contactName = model.getName();
                firstChar = String.valueOf(model.getName().charAt(0));
            }
            itemBinding.contactName.setText(contactName.substring(0, 1).toUpperCase() + contactName.substring(1).toLowerCase());
            SetImg(firstChar);

            itemBinding.contactLbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!model.getPhoneNumber1().equals("") && !model.getPhoneNumber2().equals("")) {

                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        ConstraintLayout row = (ConstraintLayout) inflater.inflate(R.layout.row_emergency_contact_alert, parent, false);
                        final TextView selectfirstcontactNo = row.findViewById(R.id.contactNo1);
                        final TextView selectscondconatctNo = row.findViewById(R.id.contactNo2);

                        selectfirstcontactNo.setText(model.getPhoneNumber1());
                        selectscondconatctNo.setText(model.getPhoneNumber2());

                        selectfirstcontactNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                                dialPadIntent.setData(Uri.parse("tel:" + model.getPhoneNumber1()));
                                mContext.startActivity(dialPadIntent);
                            }
                        });

                        selectscondconatctNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                                dialPadIntent.setData(Uri.parse("tel:" + model.getPhoneNumber2()));
                                mContext.startActivity(dialPadIntent);
                            }
                        });

                        final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                                .setTitle(R.string.driverdashbordEmergencydialogTxt)

                                .setNegativeButton(R.string.dialogNegativeButtonCancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                .setView(row)
                                .show();

                    } else {
                        Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                        dialPadIntent.setData(Uri.parse("tel:" + model.getPhoneNumber1()));
                        mContext.startActivity(dialPadIntent);
                    }
                }

            });
        }

        private void SetImg(String letter) {
            itemBinding.firstLetter.setText(letter.toUpperCase());
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
    }
}