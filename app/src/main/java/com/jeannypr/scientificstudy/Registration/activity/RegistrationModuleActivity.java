package com.jeannypr.scientificstudy.Registration.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Registration.api.RegistrationService;
import com.jeannypr.scientificstudy.Registration.model.RegistrationBean;
import com.jeannypr.scientificstudy.Utilities.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationModuleActivity extends AppCompatActivity implements View.OnClickListener {
    TextView regAmount, regCollectionAmount, admAmount;
    private Context context;
    RegistrationService registrationService;
    UserModel userData;
    ProgressBar pb;
    ConstraintLayout classWiseReportRow, takeRegRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_module);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        registrationService = new DataRepo<>(RegistrationService.class, context).getService();

        classWiseReportRow = findViewById(R.id.classWiseReportRow);
        classWiseReportRow.setOnClickListener(this);

        takeRegRow = findViewById(R.id.takeRegRow);
        takeRegRow.setOnClickListener(this);

        regAmount = findViewById(R.id.regAmount);
        regCollectionAmount = findViewById(R.id.collectionAmount);
        admAmount = findViewById(R.id.admAmount);
        pb = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Registration", "");

        GetTotalRegCollection();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetTotalRegCollection();
    }

    private void GetTotalRegCollection() {
        pb.setVisibility(View.VISIBLE);
        registrationService.GetRegistrationCollection(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<RegistrationBean>() {
            @Override
            public void onResponse(Call<RegistrationBean> call, Response<RegistrationBean> response) {
                RegistrationBean resp = response.body();

                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {

                            regAmount.setText(String.valueOf(resp.data.TotalRegistration));
                            regCollectionAmount.setText(("Rs.") + " " + String.valueOf(resp.data.TotalRegCollection));
                            admAmount.setText(String.valueOf(resp.data.TotalAdmission));
                            // admCollectionAmount.setText(("Rs.") + " " + String.valueOf(resp.data.TotalAdmissionCollection));

                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Somthing went worng. Please try again", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RegistrationBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Total collection no could not be loaded. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.classWiseReportRow:
                Intent regIntent = new Intent(context, ClassWiseRegistrationActivity.class);
                startActivity(regIntent);
                break;

            case R.id.takeRegRow:
                Intent i = new Intent(this, TakeRegistrationActivity.class);
                startActivity(i);
                break;
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_nav:
                Intent i = new Intent(this, TakeRegistrationActivity.class);
                startActivity(i);
                return true;

            default:
                return false;
        }
    }*/
}