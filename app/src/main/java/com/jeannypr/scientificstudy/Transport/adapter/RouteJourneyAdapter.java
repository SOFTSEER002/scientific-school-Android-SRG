package com.jeannypr.scientificstudy.Transport.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.model.RouteListModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowRouteJourneyListBinding;

import java.util.List;

public class RouteJourneyAdapter extends RecyclerView.Adapter<RouteJourneyAdapter.MyViewHolder> {

    Context mContext;
    private List<RouteListModel> routeList;
    ItemClickListner listener;
    ConstraintLayout parent;

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public RouteJourneyAdapter(Context context, List<RouteListModel> routejourneyData, ItemClickListner listener) {
        super();
        mContext = context;
        routeList = routejourneyData;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowRouteJourneyListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_route_journey_list, parent, false);
        return new RouteJourneyAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RouteListModel routeListModel = routeList.get(position);
        routeListModel.AdapterPosition = position;
        ((MyViewHolder) holder).bind(routeListModel, listener);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RowRouteJourneyListBinding itemBinding;

        MyViewHolder(RowRouteJourneyListBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final RouteListModel model, final RouteJourneyAdapter.ItemClickListner listener) {
            itemBinding.setRoute(model);

            if (!model.getRouteName().equals("")) {
                SetImg(model.getRouteName().charAt(0));
            } else {
                Log.e("", "");
            }


            //   listener.OnClickCurrentJourneyBtn(model);

           /* itemBinding.driverName.setOnClickListener(this);
            itemBinding.driver.setOnClickListener(this);
*/
            if (model.getDriverMobile().equals("")) {
                itemBinding.callBtn.setVisibility(View.GONE);
            } else {
                itemBinding.callBtn.setVisibility(View.VISIBLE);
            }

            if (model.getCurrentJourneyDetail() != null) {
                itemBinding.noJourneyMsg.setTextColor(mContext.getResources().getColor(R.color.indigo));
                if (model.getCurrentJourneyDetail().getJourneyId() == 0) {
//no journey
                    itemBinding.noJourneyMsg.setText("Journey not started yet ");
                    itemBinding.noJourneyMsg.setTextColor(mContext.getResources().getColor(R.color.orange7));
                    itemBinding.noJourneyMsg.setVisibility(View.VISIBLE);
                    /*itemBinding.inputRow.setVisibility(View.GONE);
                    itemBinding.journeyLbl.setVisibility(View.GONE);*/
                    itemBinding.trackLocation.setVisibility(View.GONE);
                    itemBinding.journeyDetailRow.setVisibility(View.GONE);

                } else {
                    itemBinding.noJourneyMsg.setVisibility(View.GONE);
                    itemBinding.journeyDetailRow.setVisibility(View.VISIBLE);

                    if (model.getCurrentJourneyDetail().getEndTime().equals("")) {
// journey going on
                        itemBinding.statusBtn.setVisibility(View.VISIBLE);
                        itemBinding.endTime.setVisibility(View.GONE);
                        itemBinding.trackLocation.setVisibility(View.VISIBLE);
                        itemBinding.isPickup.setVisibility(View.VISIBLE);
                        itemBinding.viewJourneyDetailBtn.setText(mContext.getResources().getString(R.string.trans_driverlist_journeyTxt));

                        if (model.getCurrentJourneyDetail().isPickup()) {
                            itemBinding.isPickup.setText("On the way for pickup");
                        } else {
                            itemBinding.isPickup.setText("On the way to school");
                        }

                    } else {
// journey has completed
                        itemBinding.statusBtn.setVisibility(View.GONE);
                        itemBinding.endTime.setVisibility(View.VISIBLE);
                        itemBinding.trackLocation.setVisibility(View.GONE);
                        itemBinding.isPickup.setVisibility(View.GONE);
                        itemBinding.viewJourneyDetailBtn.setText(mContext.getResources().getString(R.string.trans_driverlist_journeyLbl));
                    }

                    if (!model.getCurrentJourneyDetail().getLastNotificationMessage().equals("")) {
                        itemBinding.msgIc.setVisibility(View.VISIBLE);
                        itemBinding.notificationTxt
                                .setText("Last notification sent by driver to all parents of this route : " + model.getCurrentJourneyDetail().getLastNotificationMessage());
                    } else {
                        itemBinding.msgIc.setVisibility(View.GONE);
                        itemBinding.notificationTxt.setVisibility(View.GONE);
                    }
                }
            } else {
                itemBinding.noJourneyMsg.setText("Journey not started yet");
                itemBinding.noJourneyMsg.setTextColor(mContext.getResources().getColor(R.color.orange7));
                itemBinding.noJourneyMsg.setVisibility(View.VISIBLE);

                /*itemBinding.inputRow.setVisibility(View.GONE);
                itemBinding.journeyLbl.setVisibility(View.GONE);*/
                itemBinding.trackLocation.setVisibility(View.GONE);
                itemBinding.journeyDetailRow.setVisibility(View.GONE);
            }

            itemBinding.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open dialog
                    //show list of drivers
                    // drivername - contact number
                    // on click number, call on it

                    if (model.getCurrentJourneyDetail() != null) {

                        if (model.getCurrentJourneyDetail().getJourneyId() != 0) {

                            if (model.getDriverId() != model.getCurrentJourneyDetail().getDriverId()) {

                                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                ConstraintLayout row = (ConstraintLayout) inflater.inflate(R.layout.row_driver_contact_alert, parent, false);

                                final TextView drivernameLbl = row.findViewById(R.id.drivernameLbl);
                                final TextView selectdriverName = row.findViewById(R.id.selectdriverName);
                                final ConstraintLayout rowjourneyDriver, rowassignedDriver;

                                rowassignedDriver = row.findViewById(R.id.rowassignedDriver);
                                rowjourneyDriver = row.findViewById(R.id.rowjourneyDriver);

                                String vehicleDrivername = "";
                                String journeyDrivername = "";

                                vehicleDrivername = model.getDriverName();
                                journeyDrivername = model.getCurrentJourneyDetail().getDriverName();

                                drivernameLbl.setText(vehicleDrivername.substring(0, 1).toUpperCase() + vehicleDrivername.substring(1).toLowerCase());
                                selectdriverName.setText(journeyDrivername.substring(0, 1).toUpperCase() + journeyDrivername.substring(1).toLowerCase());


                                rowassignedDriver.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                                        dialPadIntent.setData(Uri.parse("tel:" + model.getDriverMobile()));
                                        mContext.startActivity(dialPadIntent);
                                    }
                                });

                                rowjourneyDriver.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                                        dialPadIntent.setData(Uri.parse("tel:" + model.getCurrentJourneyDetail().getDriverMobile()));
                                        mContext.startActivity(dialPadIntent);
                                    }
                                });


                                final AlertDialog dialog = new AlertDialog.Builder(mContext)
                                        .setTitle(R.string.routlist_calldialog_Tilte)
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
                                dialPadIntent.setData(Uri.parse("tel:" + model.getDriverMobile()));
                                mContext.startActivity(dialPadIntent);
                            }
                        }
                    } else {
                        Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                        dialPadIntent.setData(Uri.parse("tel:" + model.getDriverMobile()));
                        mContext.startActivity(dialPadIntent);
                    }
                }
            });


            itemBinding.trackLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isAppInstalled = AppInstalledOrNot(Constants.TrackLoction.MAP_PACKAGE_NAME);

                    try {
                        if (isAppInstalled) {
                            String latitude, longitude;
                            latitude = model.getCurrentJourneyDetail().getLattitude();
                            longitude = model.getCurrentJourneyDetail().getLongitude();

                            String addr = Utility.GetLocationNameFromLatLong(latitude, longitude, mContext);

                            Uri IntentUri = Uri.parse("geo:0,0?q=" + latitude + "," + longitude + "(" + addr + ")");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, IntentUri);
                            mapIntent.setPackage(Constants.TrackLoction.MAP_PACKAGE_NAME);

                            if (mapIntent.resolveActivity(mContext.getPackageManager()) != null) {
                                mContext.startActivity(mapIntent);
                            }
                        } else {

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TrackLoction.MAP_PALYSTORE_URL));
                            mContext.startActivity(intent);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            itemBinding.viewJourneyDetailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (itemBinding.inputRow.getVisibility() == View.VISIBLE) {
                        itemBinding.viewJourneyDetailBtn.setIcon(mContext.getResources().getDrawable(R.drawable.ic_arrow_down));
                        itemBinding.inputRow.setVisibility(View.GONE);

                    } else if (itemBinding.inputRow.getVisibility() == View.GONE) {
                        itemBinding.viewJourneyDetailBtn.setIcon(mContext.getResources().getDrawable(R.drawable.ic_arrow_up));
                        itemBinding.inputRow.setVisibility(View.VISIBLE);
                    }
                }
            });

            if(model.getCurrentJourneyDetail()!=null) {

                if (!model.getCurrentJourneyDetail().getLattitude().equals("")) {
                    itemBinding.trackLocation.setVisibility(View.VISIBLE);

                } else {
                    itemBinding.trackLocation.setVisibility(View.GONE);
                }
            }
            else {
                itemBinding.trackLocation.setVisibility(View.GONE);
            }

        }

        private void SetImg(char firstLetter) {
            itemBinding.firstLetter.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = itemBinding.driverProfilePic.getBackground();
            int colorId = Utility.GetRandomMaterialColor("materialColor", mContext);

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(colorId);

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(colorId);

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(colorId);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

              /*  case R.id.driver:
                case R.id.driverName:
                    Intent profileIntent = new Intent(mContext, DriverProfileActivity.class);
                    mContext.startActivity(profileIntent);
                    break;*/

            }
        }
    }

    private boolean AppInstalledOrNot(String uri) {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public interface ItemClickListner {
        void OnClickCurrentJourneyBtn(RouteListModel model);
    }
}