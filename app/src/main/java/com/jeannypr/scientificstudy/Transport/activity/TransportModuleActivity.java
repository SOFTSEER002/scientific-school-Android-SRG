package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Dashboard.Activity.NotificationSettingActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class TransportModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private BaseService BaseService;
    private TransportService transportService;
    private Context context;
    UserModel userdata;
    ImageView routeBtn, stoppageBtn, classBtn;
    CardView routeRow, stoppageRow, classRow;
    ImageView routeIc, stoppageIc, classIc;
    TextView routeLbl, stoppageLbl, classLbl;
    CardView driverLbl, routelistLbl;
    ConstraintLayout routeDetailsRow, stoppageDetailsRow, classDetailsRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_transport_module);
        userdata = UserPreference.getInstance(context).getUserData();
        transportService = new DataRepo<>(TransportService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Transport", "");

        routeDetailsRow = findViewById(R.id.routeWiseRow);
        classDetailsRow = findViewById(R.id.classWiseRow);
        stoppageDetailsRow = findViewById(R.id.stoppageWiseRow);

        routelistLbl = findViewById(R.id.trackLocationCard);
        driverLbl = findViewById(R.id.driverLbl);

        routeDetailsRow.setOnClickListener(this);
        classDetailsRow.setOnClickListener(this);
        stoppageDetailsRow.setOnClickListener(this);
        routelistLbl.setOnClickListener(this);
        driverLbl.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.routeWiseRow:
                Intent routeIntent = new Intent(this, RouteSummaryActivity.class);
                startActivity(routeIntent);
                break;

            case R.id.stoppageWiseRow:
                Intent stoppageIntent = new Intent(this, StoppageSummaryActivity.class);
                startActivity(stoppageIntent);
                break;

            case R.id.classWiseRow:
                Intent classIntent = new Intent(this, ClassSummaryActivity.class);
                startActivity(classIntent);
                break;

            case R.id.driverLbl:
                Intent driverIntent = new Intent(this, DriversListActivity.class);
                startActivity(driverIntent);
                break;

            case R.id.trackLocationCard:
                Intent intent = new Intent(this, RouteListActivity.class);
                startActivity(intent);
                break;

      /*      case R.id.routeRow:
            case R.id.routeBtn:
                Intent routeIntent = new Intent(this, RouteSummaryActivity.class);
                startActivity(routeIntent);
                break;

            case R.id.stoppageRow:
            case R.id.stoppageBtn:
                Intent stoppageIntent = new Intent(this, StoppageSummaryActivity.class);
                startActivity(stoppageIntent);
                break;

            case R.id.classRow:
            case R.id.classBtn:
                Intent classIntent = new Intent(this, ClassSummaryActivity.class);
                startActivity(classIntent);
                break;*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transport_setting_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_setting:
                Intent intent = new Intent(context, NotificationSettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}