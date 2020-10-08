package com.jeannypr.scientificstudy.Login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.BuildConfig;
import com.jeannypr.scientificstudy.Chat.activity.HelpActivity;
import com.jeannypr.scientificstudy.Login.api.LoginService;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailBean;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterSchoolKeyActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EnterSchoolKeyLog";
    EditText txtSchoolKey;
    TextView lnkGetSchoolKey, btnNext, signupLink;

    Context context, appContext;
    UserPreference userPref;
    ProgressBar p_bar;
    String schoolKey;
    Boolean isPaidApp = false, isKeyDdlAvailable = false;
    LoginService service;
    TextView txtHelp;
    Spinner ddlSclKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //TODO: remove the error in lollipop. Test in higher os as well
            setContentView(R.layout.activity_enter_school_key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        context = this;
        userPref = UserPreference.getInstance(context);
        userPref.SetFieldForApiHeader(true);

        if (getIntent().hasExtra("schoolKey")) schoolKey = getIntent().getStringExtra("schoolKey");
        if (getIntent().hasExtra("isPaidApp"))
            isPaidApp = getIntent().getBooleanExtra("isPaidApp", false);
        //  isPaidApp = userPref.getSchoolData().getPaidVersionOfApp();

        txtSchoolKey = findViewById(R.id.txtSchoolKey);
        ddlSclKeys = findViewById(R.id.ddlSclKeys);
        if (schoolKey != null && !schoolKey.equals(""))
            txtSchoolKey.setText(schoolKey);

        p_bar = findViewById(R.id.progressBar);
        btnNext = findViewById(R.id.btnNext);
        lnkGetSchoolKey = findViewById(R.id.lnkGetSchoolKey);

        btnNext.setOnClickListener(this);
        lnkGetSchoolKey.setOnClickListener(this);

        signupLink = findViewById(R.id.signupLink);
        signupLink.setText("Try it, Explore it, Use it!" + System.lineSeparator() + getResources().getString(R.string.reglink));
        signupLink.setOnClickListener(this);

        txtHelp = findViewById(R.id.helpLink);
        txtHelp.setOnClickListener(this);

        if (isPaidApp)
            GetSchoolDetail(schoolKey);
        if (BuildConfig.FLAVOR == Constants.ProductFlavors.JH_PODDAR_BH) {
            isKeyDdlAvailable = true;
            findViewById(R.id.schoolKeyRow).setVisibility(View.INVISIBLE);
            ddlSclKeys.setVisibility(View.VISIBLE);
        } else {
            isKeyDdlAvailable = false;
            findViewById(R.id.schoolKeyRow).setVisibility(View.VISIBLE);
            ddlSclKeys.setVisibility(View.GONE);
        }
    }

    private void showLoader() {
        p_bar.bringToFront();
        p_bar.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
    }

    private void hideLoader() {
        p_bar.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnNext:
                String schoolKey;
                if (isKeyDdlAvailable) {
                    schoolKey = ddlSclKeys.getSelectedItem().toString();
                    if (schoolKey.equals(getString(R.string.selectSclKey))) schoolKey = "";
                } else schoolKey = txtSchoolKey.getText().toString();
                GetSchoolDetail(schoolKey);
                break;

            case R.id.lnkGetSchoolKey:
                Intent getSchoolKeyIntent = new Intent(context, GetSchoolKeyActivity.class);
                startActivity(getSchoolKeyIntent);
                finish();
                break;

            case R.id.signupLink:
                Intent signupIntent = new Intent(context, HelpActivity.class);
                signupIntent.putExtra("webUrl", Constants.SIGN_UP_URL);
                signupIntent.putExtra("title", getResources().getString(R.string.app_name));
                signupIntent.putExtra("subtitle", "Registration");
                startActivity(signupIntent);
                break;

            case R.id.helpLink:
                Intent i = new Intent(this, HelpActivity.class);
                i.putExtra("webUrl", Constants.ENTER_KEY_HELP_URL);
                i.putExtra("title", getResources().getString(R.string.app_name));
                i.putExtra("subtitle", "Help");
                startActivity(i);
                break;
        }
    }

    private void GetSchoolDetail(String schoolKey) {
        schoolKey = schoolKey.replaceAll("\\s", "");
        service = new DataRepo<>(LoginService.class, context).getService();

        if (schoolKey != null && !schoolKey.equals("")) {
            userPref.setSchoolCode(schoolKey);
            showLoader();

            service.getSchoolDetailByKey(schoolKey).enqueue(new Callback<SchoolDetailBean>() {
                @Override
                public void onResponse(Call<SchoolDetailBean> call, Response<SchoolDetailBean> response) {
                    SchoolDetailBean bean = response.body();
                    Log.i("School data: ", bean.toString());
                    SchoolDetailModel detail = null;
                    if (bean != null)
                        detail = bean.getData();

                    if (detail != null && detail.Exists != null && detail.Exists) {
                        if (isPaidApp)
                            detail.setPaidVersionOfApp(true);

                        userPref.setSchoolData(detail);
                        Intent loginIntent = new Intent(context, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();

                    } else
                        Toast.makeText(context, "Unable to authenticate. Please enter the correct key!", Toast.LENGTH_LONG).show();
                    hideLoader();
                }

                @Override
                public void onFailure(Call<SchoolDetailBean> call, Throwable t) {
                    Log.d(TAG, "could not make the call to the server");
                    hideLoader();
                    Toast.makeText(context, "There was an error while authentication. Please try again!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, "Please enter the key!", Toast.LENGTH_SHORT).show();
        }
    }
}