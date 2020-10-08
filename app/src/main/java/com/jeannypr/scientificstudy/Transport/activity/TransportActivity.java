package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.EmergencyContactsModel;
import com.jeannypr.scientificstudy.Transport.model.TransportBean;
import com.jeannypr.scientificstudy.Transport.model.TransportModel;
import com.jeannypr.scientificstudy.Transport.model.VehicleCurrentLoctionBean;
import com.jeannypr.scientificstudy.Transport.model.VehicleCurrentLoctionModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    UserPreference userPref;
    UserModel userModel;
    LayoutInflater inflater;
    TransportService transportService;
    int studentid = 0;
    Toolbar toolbar;
    private TextView stoppageName, transportType, pickup, drop, inchargeName, inchargeContact, vehicleType, vehicleNo,
            driverName, driverContact1, driverContact2, noRecordMsg;
    private LinearLayout emergencyContactsContainer, noRecord;
    ConstraintLayout contentContainer;
    private List<EmergencyContactsModel> emergencyContactList;
    CollapsingToolbarLayout collapsingToolbar;
    Button trackLoctionBtn;
    ProgressBar pb;
    String latitude = "", longitude = "", msg;
    private Boolean isTransportAvailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_transport);

        userPref = UserPreference.getInstance(this);
        userModel = userPref.getUserData();
        inflater = LayoutInflater.from(context);

        if (getIntent().hasExtra("studentId"))
            studentid = getIntent().getIntExtra("studentId", 0);
        else
            studentid = userPref.getSelectedChild().StudentId;

        transportService = new DataRepo<>(TransportService.class, this).getService();

        toolbar = findViewById(R.id.toolbar);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);

        setSupportActionBar(toolbar);
        collapsingToolbar.setTitle("Transport");
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        stoppageName = findViewById(R.id.stoppageName);
        transportType = findViewById(R.id.transportType);
        pickup = findViewById(R.id.pickup);
        drop = findViewById(R.id.drop);
        inchargeName = findViewById(R.id.inchargeName);
        inchargeContact = findViewById(R.id.inchargeContact);
        vehicleType = findViewById(R.id.vehicleType);
        vehicleNo = findViewById(R.id.vehicleNo);
        driverName = findViewById(R.id.drivername);
        driverContact1 = findViewById(R.id.driverContact1);
        driverContact2 = findViewById(R.id.driverContact2);

        emergencyContactsContainer = findViewById(R.id.emergencyContactsContainer);
        contentContainer = findViewById(R.id.contentContainer);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        trackLoctionBtn = findViewById(R.id.trackLoctionBtn);
        trackLoctionBtn.setOnClickListener(this);
        pb = findViewById(R.id.progressBar);

        GetTransportDetail();
//        GetCurrentLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTransportAvailed)
            GetCurrentLocation();
    }

    private void GetTransportDetail() {
        pb.setVisibility(View.VISIBLE);
        transportService.GetTransportDetails(studentid, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<TransportBean>() {
            @Override
            public void onResponse(Call<TransportBean> call, Response<TransportBean> response) {
                TransportBean transportBean = response.body();
                if (transportBean != null) {

                    if (transportBean.rcode == Constants.Rcode.OK) {
                        TransportModel transportModel = transportBean.data;

                        if (transportModel.IsTransportAvailed) {
                            isTransportAvailed = true;
                            trackLoctionBtn.setVisibility(View.VISIBLE);
                            GetCurrentLocation();

                            emergencyContactList = transportModel.EmergencyContacts;
                            collapsingToolbar.setTitle("Via " + transportModel.RouteName);
                            stoppageName.setText(transportModel.PlaceName);
                            transportType.setText(transportModel.TransportTypeText);
                            pickup.setText(transportModel.StartTime);
                            drop.setText(transportModel.EndTime);
                            inchargeName.setText(transportModel.Coordinator);
                            inchargeContact.setText(transportModel.CoordinatorMobile);
                            vehicleType.setText(transportModel.VehicleType);
                            vehicleNo.setText(transportModel.VehicleNo);
                            driverContact1.setText(transportModel.DriverMobile1);
                            driverContact2.setText(transportModel.DriverMobile2);
                            driverName.setText(transportModel.DriverName);

                            InflateEmergencyContacts();
                        } else {
                            contentContainer.setVisibility(View.GONE);
                            trackLoctionBtn.setVisibility(View.GONE);
                            noRecord.setVisibility(View.VISIBLE);
                            noRecordMsg.setText("Your child is not availing transport service.");
                        }

                    } else if (transportBean.rcode == Constants.Rcode.NORECORDS) {
                        HideDetails("Your child is not availing transport service.");

                    } else {
                        HideDetails("Transport Details could not be loaded. Please try again.");
                    }
                } else {
                    HideDetails("Transport Details could not be loaded.Please try again...");
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TransportBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Transport Details could not be loaded.Please try again...", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void HideDetails(String msg) {
        contentContainer.setVisibility(View.GONE);
        trackLoctionBtn.setVisibility(View.GONE);
        noRecord.setVisibility(View.VISIBLE);
        noRecordMsg.setText(msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void InflateEmergencyContacts() {
        for (final EmergencyContactsModel contact : emergencyContactList) {
            final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.row_emergency_contacts, emergencyContactsContainer, false);
            final TextView emergencyType = view.findViewById(R.id.emergencyType);
            final TextView emergencyContact = view.findViewById(R.id.emergencyContact);

            emergencyContact.setText(contact.MobileNumber != null ? contact.MobileNumber : "");
            emergencyType.setText(contact.EmergencyTypetitle != null ? contact.EmergencyTypetitle : "");

            emergencyContactsContainer.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        /*boolean isAppInstalled = AppInstalledOrNot(Constants.TrackLoction.MAP_PACKAGE_NAME);*/

        switch (id) {
            case R.id.trackLoctionBtn:
                ShowLocation();
                break;
        }
    }

    private void GetCurrentLocation() {
        trackLoctionBtn.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        transportService.GetVehicleCurrentLocation(studentid, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<VehicleCurrentLoctionBean>() {
            @Override
            public void onResponse(Call<VehicleCurrentLoctionBean> call, Response<VehicleCurrentLoctionBean> response) {
                VehicleCurrentLoctionBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            VehicleCurrentLoctionModel model = resp.data;

                         /*   Status:
                            0 - success //when journey has started and location found
                            1 - completed  // when journey has completed for any mode
                            2 - no journey  // when there is no journey record for current date
                            3 - started but no location  //when journey is going on but no location is tracked
                        */

                            switch (model.getStatus()) {
                                case Constants.TrackLocationStaus.SUCCESS:
                                    if (!model.getLatitude().equals("") && !model.getLongitude().equals("")) {
                                        latitude = model.getLatitude();
                                        longitude = model.getLongitude();

                                    } else {
                                        latitude = "";
                                        longitude = "";
                                        msg = getResources().getString(R.string.noLatLong);
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                    }
                                    break;

                                case Constants.TrackLocationStaus.COMPLETED:
                                    latitude = "";
                                    longitude = "";

                                    if (model.isPickup()) {
                                        msg = getResources().getString(R.string.journeyCompleted_pick);
                                    } else {
                                        msg = getResources().getString(R.string.journeyCompleted_drop);
                                    }
                                    //String msg = getResources().getString(R.string.journeyCompleted, mode);
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                    break;

                                case Constants.TrackLocationStaus.NO_JOURNEY:
                                    latitude = "";
                                    longitude = "";
                                    msg = getResources().getString(R.string.noJourney);
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                    break;

                                case Constants.TrackLocationStaus.STARTED_BUT_NO_LOCATION:
                                    latitude = "";
                                    longitude = "";
                                    msg = getResources().getString(R.string.startedButNoLocation);
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } else {
                            latitude = "";
                            longitude = "";
                            msg = getResources().getString(R.string.noLatLong);
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        latitude = "";
                        longitude = "";
                        msg = getResources().getString(R.string.noJourney);
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    } else {
                        latitude = "";
                        longitude = "";
                        msg = getResources().getString(R.string.noLatLong);
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }

                } else {
                    latitude = "";
                    longitude = "";
                    msg = getResources().getString(R.string.noLatLong);
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }

                pb.setVisibility(View.GONE);
                trackLoctionBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<VehicleCurrentLoctionBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                trackLoctionBtn.setVisibility(View.VISIBLE);
                msg = getResources().getString(R.string.noLatLong);
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean AppInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }

        return false;
    }

    private void ShowLocation() {
        if (!latitude.equals("") && !longitude.equals("")) {
            boolean isAppInstalled = AppInstalledOrNot(Constants.TrackLoction.MAP_PACKAGE_NAME);

            try {
                Uri IntentUri = null;
                Intent intent = null;

                if (isAppInstalled) {
                    String addr = Utility.GetLocationNameFromLatLong(latitude, longitude, context);
                    IntentUri = Uri.parse("geo:0,0?q=" + latitude + "," + longitude + "(" + addr + ")");
                    intent = new Intent(Intent.ACTION_VIEW, IntentUri);
                    intent.setPackage(Constants.TrackLoction.MAP_PACKAGE_NAME);

                } else {
                    IntentUri = Uri.parse(Constants.TrackLoction.MAP_PALYSTORE_URL);
                    intent = new Intent(Intent.ACTION_VIEW, IntentUri);
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}