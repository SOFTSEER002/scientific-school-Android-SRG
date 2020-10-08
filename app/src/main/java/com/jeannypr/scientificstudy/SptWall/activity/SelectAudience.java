package com.jeannypr.scientificstudy.SptWall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Classwork.activity.CreateCwHwActivity;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;

public class SelectAudience extends AppCompatActivity implements View.OnClickListener {
    Context context;
    LinearLayout parent;
    String selectedAudienceName;
    int selectedAudienceId;
    FloatingActionButton nextFabBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_select_audience);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        parent = findViewById(R.id.parent);
        nextFabBtn = findViewById(R.id.nextFabBtn);
        nextFabBtn.setOnClickListener(this);
        selectedAudienceId = Constants.PostAudienceValues.SCHOOL;
        selectedAudienceName = Constants.PostAudienceNames.SCHOOL;

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle("Choose Audience");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final ArrayList<DropDownModel> audienceArr = new ArrayList<DropDownModel>();

        audienceArr.add(new DropDownModel(Constants.PostAudienceValues.SCHOOL, Constants.PostAudienceNames.SCHOOL));
        audienceArr.add(new DropDownModel(Constants.PostAudienceValues.PARENT, Constants.PostAudienceNames.PARENT));
        audienceArr.add(new DropDownModel(Constants.PostAudienceValues.TEACHER, Constants.PostAudienceNames.TEACHER));

        int totalAudience = audienceArr.size();

        if (totalAudience > 0) {

            final ArrayList<RelativeLayout> allRows = new ArrayList<RelativeLayout>();
            for (final DropDownModel audience : audienceArr) {

                RelativeLayout view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.row_audience, parent, false);
                final TextView txtAudience = view.findViewById(R.id.audience);
                final ImageView arrowIc = view.findViewById(R.id.arrowIc);

                allRows.add(view);
                final TextView firstAudience = allRows.get(0).findViewById(R.id.audience);
                firstAudience.setTextColor(getResources().getColor(R.color.colorPrimary));
                allRows.get(0).findViewById(R.id.arrowIc).setVisibility(View.VISIBLE);

                try {
                    txtAudience.setText(audience.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RelativeLayout row : allRows) {
                            final TextView txtAud = row.findViewById(R.id.audience);
                            txtAud.setTextColor(getResources().getColor(R.color.black));
                            row.findViewById(R.id.arrowIc).setVisibility(View.INVISIBLE);
                        }

                        arrowIc.setVisibility(View.VISIBLE);
                        txtAudience.setTextColor(getResources().getColor(R.color.colorPrimary));
                        selectedAudienceId = audience.getId();
                        selectedAudienceName = audience.getText();
                    }
                });

                parent.addView(view);
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextFabBtn:
                Intent backToCreatePostIntent = new Intent(this, CreateCwHwActivity.class);

                if (selectedAudienceId != -1 && !selectedAudienceName.equals("")) {
                    backToCreatePostIntent.putExtra("audienceId", selectedAudienceId);
                    backToCreatePostIntent.putExtra("audienceName", selectedAudienceName);
                    setResult(RESULT_OK, backToCreatePostIntent);
                    finish();
                    break;

                } else {
                    Toast.makeText(context, "Please select audience.", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
