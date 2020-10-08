package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.adapter.SeletctVehicleAdapter;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.JourneyDetailModel;
import com.jeannypr.scientificstudy.Transport.model.SaveJourneyBean;
import com.jeannypr.scientificstudy.Transport.model.VehicleRouteBean;
import com.jeannypr.scientificstudy.Transport.model.VehiclesBean;
import com.jeannypr.scientificstudy.Transport.model.VehiclesModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectVehicleActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    MaterialButton r1;
    Chip v1, v2;
    TransportService service;
    UserModel userData;
    UserPreference userPref;
    RecyclerView vehicleListContainer;
    ArrayList<VehiclesModel> vehiclesList;
    SeletctVehicleAdapter adapter;
    LinearLayout noRecord;
    TextView noRecordMsg;
    ProgressBar pb;
    int routeId, assignedVehicleId = 0;
    String routeName;
    LayoutInflater inflater;
    ConstraintLayout parent;
    boolean checked;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);
        context = this;

        userPref = UserPreference.getInstance(context);
        userData = userPref.getInstance(context).getUserData();
        service = new DataRepo<>(TransportService.class, context).getService();
        vehiclesList = new ArrayList<>();
        inflater = LayoutInflater.from(context);

        routeId = getIntent().getIntExtra("routeId", -1);
        routeName = getIntent().getStringExtra("routeName");
        checked = getIntent().getBooleanExtra("checked", checked);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, getResources().getString(R.string.route_setToolbar), "");

        parent = findViewById(R.id.parent);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        pb = findViewById(R.id.progressBar);
        vehicleListContainer = findViewById(R.id.vehicleListCountainer);

       /* DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        vehicleListContainer.setItemAnimator(animator);*/

       /* v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v2.setOnClickListener(this);*/
        GetAssignedVehicle();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void GetVehicles() {
        adapter = new SeletctVehicleAdapter(this, vehiclesList, assignedVehicleId, routeId, routeName, new SeletctVehicleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(JourneyDetailModel journeyDetail) {
                ConfirmJourney(journeyDetail);
            }
        });
        vehicleListContainer.setAdapter(adapter);

        pb.setVisibility(View.VISIBLE);

        service.GetVehicles(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<VehiclesBean>() {
            @Override
            public void onResponse(Call<VehiclesBean> call, Response<VehiclesBean> response) {
                VehiclesBean resp = response.body();
                vehiclesList.clear();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            for (VehiclesModel model : resp.data) {
                                vehiclesList.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);

                    } else {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<VehiclesBean> call, Throwable t) {
                pb.setVisibility(View.VISIBLE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void GetAssignedVehicle() {
        pb.setVisibility(View.VISIBLE);

        service.GetAssignedVehicle(userData.getSchoolId(), userData.getAcademicyearId(), routeId).enqueue(new Callback<VehicleRouteBean>() {
            @Override
            public void onResponse(Call<VehicleRouteBean> call, Response<VehicleRouteBean> response) {
                VehicleRouteBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            assignedVehicleId = (resp.data.getVehicleId());
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);

                    } else {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
                }
                GetVehicles();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<VehicleRouteBean> call, Throwable t) {
                pb.setVisibility(View.VISIBLE);
                GetVehicles();
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
   /*     switch (v.getId()) {
            case R.id.v2:
                v1.setChipBackgroundColorResource(R.color.colorPrimary);
                v2.setChipBackgroundColorResource(R.color.colorPrimaryDark);

                ConfirmJourney();

                break;

            default:

                break;
        }*/
    }

    private void ConfirmJourney(final JourneyDetailModel journeyDetail) {
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.row_confirm_journey_alert, parent, false);
        final TextView selectedRoute = view.findViewById(R.id.selectedRoute);
        final TextView selectedVehicle = view.findViewById(R.id.selectedVehicle);
        final TextView selectedVehicleType = view.findViewById(R.id.selectedVehicleType);

        selectedRoute.setText(getResources().getString(R.string.selectedRouteLbl) + " " + journeyDetail.getRouteName().toUpperCase());
        selectedVehicle.setText(getResources().getString(R.string.selectedVehicleLbl) + " " + journeyDetail.getVehicleModel().toUpperCase());
        selectedVehicleType
                .setText("(" + journeyDetail.getVehicleNumber() + " " + getResources().getString(R.string.selectedVehicleNo) + " \n" + " " + getResources().getString(R.string.vehicleLbl2) + " " + journeyDetail.getVehicleType().toUpperCase() + ")");

        //final View btnRow = LayoutInflater.from(context).inflate(R.layout.row_alert_dialog_buttons, parent, false);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.vehicle_dialogTitle)
                /*      .setPositiveButton(R.string.vehicle_positiveBtn, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                              SaveJourneyDetail(journeyDetail);
                          }
                      })
                      .setNegativeButton(R.string.dialogNegativeButtonCancel, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                          }
                      })*/
                .setView(view)
                .show();


        Button positiveBtn = view.findViewById(R.id.positiveBtn);
        Button negativeBtn = view.findViewById(R.id.negativeBtn);
        negativeBtn.setVisibility(View.VISIBLE);

        positiveBtn.setText(getResources().getString(R.string.dialogPositiveButtonOk));
        negativeBtn.setText(getResources().getString(R.string.dialogNegativeButtonCancel));


        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SaveJourneyDetail(journeyDetail);
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void SaveJourneyDetail(final JourneyDetailModel input) {
        //start loader
        //call api to save journey detail
        //if success - save detail in pref, call intent
        //else hide loader and show msg;

        input.setSchoolId(userData.getSchoolId());
        input.setUserId(userData.getUserId());
        input.setMode(Constants.JourneyMode.START);
        input.setPickup(checked);

        pb.setVisibility(View.VISIBLE);
        service.SaveJourneyDetails(input).enqueue(new Callback<SaveJourneyBean>() {
            @Override
            public void onResponse(Call<SaveJourneyBean> call, Response<SaveJourneyBean> response) {

                SaveJourneyBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            JourneyDetailModel model = new JourneyDetailModel();
                            model.setJourneyId(resp.data.getJourneyId());
                            model.setStartTime(resp.data.getStartTime());
                            model.setStartDate(resp.data.getStartDate());
                            model.setRouteName(input.getRouteName());
                            model.setVehicleNumber(input.getVehicleNumber());
                            model.setVehicleModel(input.getVehicleModel());
                            model.setRouteId(input.getRouteId());
                            model.setPickup(input.getPickup());
                            //save all detail in pref
                            userPref.setJourneyDetail(model);

                            //Intent intent = new Intent(context, MainActivity.class);
                            //startActivity(intent);
                            Intent intent = new Intent(context, SelectRouteActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();

                        } else {
                            Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                    }
                }

                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SaveJourneyBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}