package com.jeannypr.scientificstudy.Login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Login.api.SchoolService;
import com.jeannypr.scientificstudy.Login.model.CityBean;
import com.jeannypr.scientificstudy.Login.model.CityModel;
import com.jeannypr.scientificstudy.Login.model.SchoolBean;
import com.jeannypr.scientificstudy.Login.model.SchoolModel;
import com.jeannypr.scientificstudy.Login.model.StateBean;
import com.jeannypr.scientificstudy.Login.model.StateModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetSchoolKeyActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GetSchoolKey";
    SchoolService schoolService;

    Context context;
    Context appContext;
    ArrayList<DropDownModel> states, cities, schools;
    private Spinner stateList;
    private Spinner cityList;
    private Spinner schoolList;
    private DropDownAdapter stateAdapter, cityAdapter, schoolAdapter;
    long stateId, cityId, schoolId;
    String countryName, stateName, cityName, schoolName, schoolCode, msg, address;
    ProgressBar progressBar;
    ConstraintLayout parent;
    // RelativeLayout ddlSchoolListContainer, ddlCityListContainer, ddlStateListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_school_key);

        context = this;
        appContext = getApplicationContext();

        UserPreference.getInstance(context).SetFieldForApiHeader(false);
        schoolService = new DataRepo<>(SchoolService.class, this).getService();

        Spinner countryList = findViewById(R.id.ddlCountryList);
        stateList = findViewById(R.id.ddlStateList);
        cityList = findViewById(R.id.ddlCityList);
        schoolList = findViewById(R.id.ddlSchoolList);
        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();

        countryName = countryList.getSelectedItem().toString();

        schoolService.getStates(countryName).enqueue(new Callback<StateBean>() {
            @Override
            public void onResponse(Call<StateBean> call, Response<StateBean> response) {
                StateBean res = response.body();
                if (res != null) {

                    if (res.rcode == Constants.Rcode.OK) {
                        //stateList.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        states = new ArrayList<>();
                        DropDownModel defaultstate = new DropDownModel();
                        defaultstate.setText("Select State");
                        defaultstate.setId(-1);
                        states.add(defaultstate);

                        stateAdapter = new DropDownAdapter(GetSchoolKeyActivity.this,
                                R.layout.row_spinner, states);
                        stateList.setAdapter(stateAdapter);

                        for (StateModel state : res.data) {
                            DropDownModel ddModel = new DropDownModel();
                            ddModel.setId(state.Id);
                            ddModel.setText(state.StateName);
                            states.add(ddModel);
                        }

                        stateAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(appContext, "Unable to load states. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StateBean> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(appContext, "Unable to load states. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

        stateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel selectedState = stateAdapter.getItem(position);
                if (selectedState != null) {

                    if (selectedState.getId() == -1) {
                        stateId = -1;
                        stateName = "";
                    } else {
                        stateId = selectedState.getId();
                        stateName = selectedState.getText();
                        progressBar.setVisibility(View.VISIBLE);

                        schoolService.getCities(stateName).enqueue(new Callback<CityBean>() {

                            @Override
                            public void onResponse(Call<CityBean> call, Response<CityBean> response) {
                                CityBean cityBean = response.body();

                                if (cityBean != null) {

                                    if (cityBean.rcode == Constants.Rcode.OK) {

                                        cities = new ArrayList<>();
                                        DropDownModel defaultCity = new DropDownModel();
                                        defaultCity.setText("Select City");
                                        defaultCity.setId(-1);
                                        cities.add(defaultCity);
                                        cityAdapter = new DropDownAdapter(GetSchoolKeyActivity.this,
                                                R.layout.row_spinner,
                                                cities);
                                        cityList.setAdapter(cityAdapter);


                                        for (CityModel cityModel : cityBean.data) {
                                            DropDownModel ddModel = new DropDownModel();
                                            ddModel.setText(cityModel.CityName);
                                            ddModel.setId(cityModel.Id);
                                            cities.add(ddModel);
                                        }

                                        cityAdapter.notifyDataSetChanged();

                                        cityList.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(appContext, "Unable to load cities. Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CityBean> call, Throwable t) {
                                Log.d(TAG, "server call error");
                                cityList.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(appContext, "Unable to load cities. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel selectedCity = cityAdapter.getItem(position);
                if (selectedCity != null) {

                    if (selectedCity.getId() == -1) {
                        cityName = "";
                        cityId = -1;
                    } else {
                        cityId = selectedCity.getId();
                        cityName = selectedCity.getText();
                        address = countryName + "-" + stateName + "-" + cityName;

                        progressBar.setVisibility(View.VISIBLE);

                        schoolService.getSchools(address).enqueue(new Callback<SchoolBean>() {
                            @Override
                            public void onResponse(Call<SchoolBean> call, Response<SchoolBean> response) {
                                SchoolBean resp = response.body();
                                if (resp != null && resp.rcode == Constants.Rcode.OK) {

                                    schools = new ArrayList<>();
                                    DropDownModel defaultSchool = new DropDownModel();
                                    defaultSchool.setText("Select School");
                                    defaultSchool.setId(-1);
                                    schools.add(defaultSchool);
                                    schoolAdapter = new DropDownAdapter(GetSchoolKeyActivity.this,
                                            R.layout.row_spinner,
                                            schools);
                                    schoolList.setAdapter(schoolAdapter);

                                    for (SchoolModel schoolMdl : resp.data) {
                                        DropDownModel ddModel = new DropDownModel();
                                        ddModel.setText(schoolMdl.SchoolName);
                                        ddModel.setId(schoolMdl.ID);
                                        ddModel.setObject(schoolMdl);
                                        schools.add(ddModel);
                                    }

                                    schoolAdapter.notifyDataSetChanged();
                                    schoolList.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<SchoolBean> call, Throwable t) {
                                schoolList.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(appContext, "Unable to load school list. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        schoolList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                DropDownModel selectedSchool = schoolAdapter.getItem(position);
                if (selectedSchool != null) {

                    if (selectedSchool.getId() == -1) {
                        schoolName = "";
                        schoolId = -1;
                        schoolCode = "";
                    } else {
                        schoolId = selectedSchool.getId();
                        schoolName = selectedSchool.getText();
                        SchoolModel obj = (SchoolModel) selectedSchool.getObject();
                        schoolCode = obj.SchoolCode;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MaterialButton getSclKeyBtn = findViewById(R.id.getSclKeyBtn);
        getSclKeyBtn.setOnClickListener(this);

        TextView backToHome = findViewById(R.id.backToHome);
        backToHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getSclKeyBtn:

                if (countryName == null || countryName.equals("") || countryName.equals("Select Country")) {
                    Toast.makeText(appContext, "Please select your country.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (stateName == null || stateName.equals("") || stateName.equals("Select State")) {
                    Toast.makeText(appContext, "Please select your state.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (cityName == null || cityName.equals("") || cityName.equals("Select City")) {
                    Toast.makeText(appContext, "Please select your city.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (schoolName == null || schoolName.equals("") || schoolName.equals("Select School")) {
                    Toast.makeText(appContext, "Please select your school.", Toast.LENGTH_LONG).show();
                    break;
                }

                msg = "Your School Key : " + schoolCode;

             /*   AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("School Key");
                builder.setMessage(msg);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BackToHome();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        BackToHome();
                    }
                });

                builder.show();*/

                final View view = LayoutInflater.from(context).inflate(R.layout.row_alert_dialog_buttons, parent, false);
                final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                        .setTitle("School Key")
                        .setMessage(msg)
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
                        BackToHome();

                    }
                });

                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        BackToHome();
                    }
                });
                break;

            case R.id.backToHome:
                Intent mainIntent = new Intent(this, EnterSchoolKeyActivity.class);
                startActivity(mainIntent);
                break;
        }
    }

    private void BackToHome() {
        Intent i = new Intent(context, EnterSchoolKeyActivity.class);
        i.putExtra("schoolKey", schoolCode);
        startActivity(i);
    }
}