package com.jeannypr.scientificstudy.LearnSubject.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jeannypr.scientificstudy.Dashboard.fragment.LearnDashboardTabFragment;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityLearnListBinding;

public class LearnListActivity extends AppCompatActivity {

    private Context context;
    ActivityLearnListBinding binding;

    private int classId;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_learn_list);
        context = this;
        SetToolbar();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = "";

        Utility.SetToolbar(context, "Learn", subTitle);

        classId = getIntent().getIntExtra("Id", -1);
        className = getIntent().getStringExtra("ClassName");

        subTitle = className;
        Utility.SetToolbar(context, "Learn", subTitle);

        Fragment fragment = new LearnDashboardTabFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (classId == -1) {
            Toast.makeText(this, "Please select a class to view students.", Toast.LENGTH_SHORT).show();
        } else {

           // Constants.CLASS_ID = classId;
           // Bundle bundle = new Bundle();
           // bundle.putInt("Id", classId);
            //fragment.setArguments(bundle);
            ft.add(R.id.fragment_container,  fragment);
            ft.commit();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
