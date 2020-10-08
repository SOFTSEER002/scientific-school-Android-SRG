package com.jeannypr.scientificstudy.Registration.activity;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Registration.adapter.ClassWiseCollectionAdapter;
import com.jeannypr.scientificstudy.Registration.model.CollectionModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityClassWiseRegistrationBinding;

import java.util.ArrayList;

public class ClassWiseCollectionActivity extends AppCompatActivity {

    private Context context;
    ActivityClassWiseRegistrationBinding binding;
    private TextView totalRegistrationLbl, takenLbl, noRecordMsg;
    ArrayList<CollectionModel> collectionModels;
    private ClassWiseCollectionAdapter adapter;
    private RecyclerView collectionList;
    private LinearLayout noRecord;
    private ProgressBar pb;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_wise_registration);
        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Class Wise Collection", "");


        totalRegistrationLbl = findViewById(R.id.totalRegistrationLbl);
        totalRegistrationLbl.setVisibility(View.GONE);

        takenLbl = findViewById(R.id.takenLbl);
        takenLbl.setText("Total Fee");

        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);
        pb = findViewById(R.id.progressBar);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
