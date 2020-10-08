package com.jeannypr.scientificstudy.Holiday.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Holiday.api.HolidayService;
import com.jeannypr.scientificstudy.Holiday.model.HolidayBean;
import com.jeannypr.scientificstudy.Holiday.model.HolidayModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HolidayActivity extends AppCompatActivity {

    private Context context;
    UserModel userData;
    LayoutInflater layoutInflator;
    HolidayService holidayService;
    List<HolidayModel> allHolidays;
    private ProgressBar pb;
    LinearLayout holidayListContainer;
    private LinearLayout noRecord;
    TextView noRecordMsg;
    Boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        layoutInflator = LayoutInflater.from(context);
        holidayService = new DataRepo<>(HolidayService.class, context).getService();
        isAdmin = userData.getRoleTitle().equals(Constants.Role.ADMIN);

        holidayListContainer = findViewById(R.id.holidayListContainer);
        pb = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Holiday");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        getHolidays();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getHolidays();
    }

    private void getHolidays() {
        pb.setVisibility(View.VISIBLE);
        holidayService.GetholidayList(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<HolidayBean>() {
            @Override
            public void onResponse(Call<HolidayBean> call, Response<HolidayBean> response) {
                HolidayBean holidayBean = response.body();
                if (holidayBean != null) {

                    if (holidayBean.rcode == Constants.Rcode.OK) {

                        allHolidays = holidayBean.holidays;
                        InflateUI();
                    } else if (holidayBean.rcode == Constants.Rcode.NORECORDS) {

                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(getString(R.string.noRecordMsg));

                    } else {
                        Toast.makeText(context, R.string.holidayListErrorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HolidayBean> call, Throwable t) {
                Toast.makeText(context, R.string.couldNotLoadHolidays, Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void InflateUI() {

        for (final HolidayModel holidayModel : allHolidays) {
            final ConstraintLayout view = (ConstraintLayout) layoutInflator.inflate(R.layout.row_holiday, holidayListContainer, false);

            final TextView holidayTitle = view.findViewById(R.id.holidayTitle);
            final TextView holidayDate = view.findViewById(R.id.holidayDate);
            final ImageView icHolidayDatepicker = view.findViewById(R.id.icHolidayDatepicker);
            final ImageView icEdit = view.findViewById(R.id.icEdit);

            holidayTitle.setText(holidayModel.HolidayTitle == null ? "" : holidayModel.HolidayTitle);
            holidayDate.setText(holidayModel.Date == null ? "" : holidayModel.Date);
            if (holidayModel.Date == null || holidayModel.Date.equals("")) {
                icHolidayDatepicker.setVisibility(View.GONE);
            }

            if (isAdmin)
                icEdit.setVisibility(View.VISIBLE);
            else icEdit.setVisibility(View.GONE);
            icEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectToEditHoliday(holidayModel.Id);
                }
            });

            holidayListContainer.addView(view);
        }
    }

    private void redirectToEditHoliday(int holidayId) {
        Intent i = new Intent(this, AddEditHolidayActivity.class);
        i.putExtra(Constants.HOLIDAY_ID, holidayId);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);

        if (isAdmin) menu.findItem(R.id.add_nav).setVisible(true);
        else menu.findItem(R.id.add_nav).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_nav:
                Intent i = new Intent(this, AddEditHolidayActivity.class);
                startActivity(i);
                return true;

            default:
                return false;
        }
    }
}
