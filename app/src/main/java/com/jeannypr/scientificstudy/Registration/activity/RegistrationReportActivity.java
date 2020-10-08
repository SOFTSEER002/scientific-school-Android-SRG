package com.jeannypr.scientificstudy.Registration.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class RegistrationReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    ConstraintLayout regReportRow, collectionReportRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_reports);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Registration", "");

        regReportRow = findViewById(R.id.regReportRow);
        regReportRow.setOnClickListener(this);

       /* collectionReportRow = findViewById(R.id.collectionReportRow);
        collectionReportRow.setOnClickListener(this);*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.regReportRow:
                Intent regIntent = new Intent(context, ClassWiseRegistrationActivity.class);
                startActivity(regIntent);
                break;

          /*  case R.id.collectionReportRow:
                Intent collectionIntent = new Intent(context, ClassWiseCollectionActivity.class);
                startActivity(collectionIntent);
                break;
*/

        }
    }
}
