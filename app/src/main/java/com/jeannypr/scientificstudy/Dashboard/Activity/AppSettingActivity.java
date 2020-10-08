package com.jeannypr.scientificstudy.Dashboard.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class AppSettingActivity extends AppCompatActivity implements View.OnClickListener {
    public Context context;
    ImageView notificationIc;
    TextView notificationLbl;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_app_setting);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Settings", "");

        notificationIc = findViewById(R.id.notificationIc);
        notificationLbl = findViewById(R.id.notificationLbl);

        notificationIc.setOnClickListener(this);
        notificationLbl.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.notificationIc:
            case R.id.notificationLbl:
                Intent notiInent = new Intent(context, NotificationSettingActivity.class);
                startActivity(notiInent);
                break;
        }
    }
}
