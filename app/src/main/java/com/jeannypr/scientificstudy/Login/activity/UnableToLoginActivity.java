package com.jeannypr.scientificstudy.Login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.HelpActivity;
import com.jeannypr.scientificstudy.Login.api.LoginService;
import com.jeannypr.scientificstudy.Login.api.SchoolService;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Login.model.UnableToLoginBean;
import com.jeannypr.scientificstudy.Login.model.UnableToLoginInputModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnableToLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UnableToLogin";
    SchoolService schoolService;
    LoginService loginService;
    Context context, appContext;
    private Spinner userRoleList;
    String profileName, email, mobile;
    SchoolDetailModel schoolDetail;
    TextInputEditText txtEmail, txtMobile;
    MaterialButton unableToLoginBtn;
    TextView txtHelp;
    UserPreference pref;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "unable to login class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unable_to_login);

        context = this;
        appContext = getApplicationContext();
        pref = UserPreference.getInstance(context);
        schoolDetail = pref.getSchoolData();
        schoolService = new DataRepo<>(SchoolService.class, this).getService();

        txtHelp = findViewById(R.id.helpLink);
        txtHelp.setOnClickListener(this);

        userRoleList = findViewById(R.id.userRole);
      /*  countryList = findViewById(R.id.ddlCountryList);
        stateList = findViewById(R.id.ddlStateList);
        cityList = findViewById(R.id.ddlCityList);
        schoolList = findViewById(R.id.ddlSchoolList);*/
        txtEmail = findViewById(R.id.email);
        txtMobile = findViewById(R.id.mobile);
        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();

        unableToLoginBtn = findViewById(R.id.unableToLoginBtn);
        unableToLoginBtn.setOnClickListener(this);
       /* ddlStateListContainer = findViewById(R.id.ddlStateListContainer);
        ddlSchoolListContainer = findViewById(R.id.ddlSchoolListContainer);
        ddlCityListContainer = findViewById(R.id.ddlCityListContainer);*/

//        countryName = countryList.getSelectedItem().toString();
//        schoolService.getStates(countryName).enqueue(new Callback<StateBean>() {
//            @Override
//            public void onResponse(Call<StateBean> call, Response<StateBean> response) {
//                StateBean res = response.body();
//                if (res != null) {
//
//                    if (res.rcode == Constants.Rcode.OK) {
//                        ddlStateListContainer.setVisibility(View.VISIBLE);
//
//                        states = new ArrayList<DropDownModel>();
//                        DropDownModel defaultstate = new DropDownModel();
//                        defaultstate.setText("Select State");
//                        defaultstate.setId(-1);
//                        states.add(defaultstate);
//                        stateAdapter = new DropDownAdapter(UnableToLoginActivity.this,
//                                R.layout.row_spinner,
//                                states);
//                        stateList.setAdapter(stateAdapter);
//
//                        for (StateModel state : res.data) {
//                            DropDownModel ddModel = new DropDownModel();
//                            ddModel.setId(state.Id);
//                            ddModel.setText(state.StateName);
//                            states.add(ddModel);
//                        }
//
//                        stateAdapter.notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(appContext, "Unable to load states. Please try again.", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StateBean> call, Throwable t) {
//                Log.d(TAG, "server call error");
//                Toast.makeText(appContext, "Server call error. Please try again.", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        stateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                DropDownModel selectedState = stateAdapter.getItem(position);
//                if (selectedState.getId() == -1) {
//                    stateId = -1;
//                    stateName = "";
//                } else {
//                    stateId = selectedState.getId();
//                    stateName = selectedState.getText();
//
//                    schoolService.getCities(stateName).enqueue(new Callback<CityBean>() {
//
//                        @Override
//                        public void onResponse(Call<CityBean> call, Response<CityBean> response) {
//                            CityBean cityBean = response.body();
//
//                            if (cityBean.rcode == Constants.Rcode.OK) {
//                                ddlCityListContainer.setVisibility(View.VISIBLE);
//
//                                cities = new ArrayList<DropDownModel>();
//                                DropDownModel defaultCity = new DropDownModel();
//                                defaultCity.setText("Select City");
//                                defaultCity.setId(-1);
//                                cities.add(defaultCity);
//                                cityAdapter = new DropDownAdapter(UnableToLoginActivity.this,
//                                        R.layout.row_spinner,
//                                        cities);
//                                cityList.setAdapter(cityAdapter);
//
//                                for (CityModel cityModel : cityBean.data) {
//                                    DropDownModel ddModel = new DropDownModel();
//                                    ddModel.setText(cityModel.CityName);
//                                    ddModel.setId(cityModel.Id);
//                                    cities.add(ddModel);
//                                }
//
//                                cityAdapter.notifyDataSetChanged();
//                            } else {
//                                Toast.makeText(appContext, "Unable to load cities. Please try again.", Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<CityBean> call, Throwable t) {
//                            Log.d(TAG, "server call error");
//                            Toast.makeText(appContext, "Server call error. Please try again.", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        cityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                DropDownModel selectedCity = cityAdapter.getItem(position);
//                if (selectedCity.getId() == -1) {
//                    cityName = "";
//                    cityId = -1;
//                } else {
//                    cityId = selectedCity.getId();
//                    cityName = selectedCity.getText();
//                    address = countryName + "-" + stateName + "-" + cityName;
//
//                    schoolService.getSchools(address).enqueue(new Callback<SchoolBean>() {
//                        @Override
//                        public void onResponse(Call<SchoolBean> call, Response<SchoolBean> response) {
//                            SchoolBean resp = response.body();
//                            if (resp.rcode == Constants.Rcode.OK) {
//                                ddlSchoolListContainer.setVisibility(View.VISIBLE);
//
//                                schools = new ArrayList<DropDownModel>();
//                                DropDownModel defaultSchool = new DropDownModel();
//                                defaultSchool.setText("Select School");
//                                defaultSchool.setId(-1);
//                                schools.add(defaultSchool);
//                                schoolAdapter = new DropDownAdapter(UnableToLoginActivity.this,
//                                        R.layout.row_spinner,
//                                        schools);
//                                schoolList.setAdapter(schoolAdapter);
//
//                                for (SchoolModel schoolMdl : resp.data) {
//                                    DropDownModel ddModel = new DropDownModel();
//                                    ddModel.setText(schoolMdl.SchoolName);
//                                    ddModel.setId(schoolMdl.ID);
//                                    ddModel.setObject(schoolMdl);
//                                    schools.add(ddModel);
//                                }
//
//                                schoolAdapter.notifyDataSetChanged();
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<SchoolBean> call, Throwable t) {
//                            Toast.makeText(appContext, "Server call error. Please try again.", Toast.LENGTH_LONG).show();
//
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        schoolList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position,
//                                       long id) {
//                DropDownModel selectedSchool = schoolAdapter.getItem(position);
//                if (selectedSchool.getId() == -1) {
//                    schoolName = "";
//                    schoolId = -1;
//                    schoolCode = "";
//                } else {
//                    schoolId = selectedSchool.getId();
//                    schoolName = selectedSchool.getText();
//                    SchoolModel obj = (SchoolModel) selectedSchool.getObject();
//                    schoolCode = obj.SchoolCode;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        TextView backToLogin = findViewById(R.id.backToLogin);
        backToLogin.setOnClickListener(this);

        SetImeActions();
    }

    private void SetImeActions() {
        txtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utility.CloseKeyboard(context);
                    GetPassword();
                    handled = true;
                }
                return handled;
            }
        });

        txtMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utility.CloseKeyboard(context);
                    GetPassword();
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unableToLoginBtn:

                GetPassword();
                break;

            case R.id.backToLogin:
                Intent loginIntent = new Intent(context, LoginActivity.class);
                startActivity(loginIntent);
                UnableToLoginActivity.this.finish();
                break;

            case R.id.helpLink:
                Intent i = new Intent(this, HelpActivity.class);
                i.putExtra("webUrl", Constants.UNABLE_TO_LOGIN_HELP_URL);
                i.putExtra("title", getResources().getString(R.string.app_name));
                i.putExtra("subtitle", "Help");
                startActivity(i);
                break;
        }
    }

    private void GetPassword() {
        profileName = userRoleList.getSelectedItem().toString();
        email = txtEmail.getText().toString();
        mobile = txtMobile.getText().toString();

        if (profileName == null || profileName.equals("") || profileName.equals("Select Role")) {
            Toast.makeText(this, "Please select your role.", Toast.LENGTH_LONG).show();
            return;
        }
               /* if (countryName == null || countryName.equals("") || countryName.equals("Select Country")) {
                    Toast.makeText(this, "Please select your country.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (stateName == null || stateName.equals("") || stateName.equals("Select State")) {
                    Toast.makeText(this, "Please select your state.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (cityName == null || cityName.equals("") || cityName.equals("Select City")) {
                    Toast.makeText(this, "Please select your city.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (schoolName == null || schoolName.equals("") || schoolName.equals("Select School")) {
                    Toast.makeText(this, "Please select your school.", Toast.LENGTH_LONG).show();
                    break;
                }*/
        if ((email == null || email.equals("")) && (mobile == null || mobile.equals(""))) {
            Toast.makeText(this, "Please enter either your registered mobile or email address!", Toast.LENGTH_LONG).show();
            return;
        }

        unableToLoginBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        UnableToLoginInputModel input = new UnableToLoginInputModel();
        input.Email = email;
        input.Mobile = mobile;
        input.Profile = profileName;
        input.SchoolCode = pref.getSchoolCode();
        input.SchoolName = schoolDetail.getSchoolName();

        loginService = new DataRepo<>(LoginService.class, this).getService();
        loginService.unableToLogin(input).enqueue(new Callback<UnableToLoginBean>() {
            @Override
            public void onResponse(Call<UnableToLoginBean> call, Response<UnableToLoginBean> response) {
                UnableToLoginBean res = response.body();
                progressBar.setVisibility(View.GONE);
                unableToLoginBtn.setVisibility(View.VISIBLE);

                Toast.makeText(appContext, res.msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UnableToLoginBean> call, Throwable t) {
                Toast.makeText(appContext, "Server side error...", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                unableToLoginBtn.setVisibility(View.VISIBLE);
            }
        });
    }
}