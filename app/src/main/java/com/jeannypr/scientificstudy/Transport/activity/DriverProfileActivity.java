package com.jeannypr.scientificstudy.Transport.activity;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class DriverProfileActivity extends AppCompatActivity {
    private Context context;
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_driver_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Drivers", "");

        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("driver");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
