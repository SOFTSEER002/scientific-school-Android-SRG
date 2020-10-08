package com.jeannypr.scientificstudy.Inventory.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Inventory.adapter.MonthWisePurchaseSaleSummaryAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryDateBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryDateModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityPurcahseSaleSummaryBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthWisePurchaseSaleSummaryActivity extends AppCompatActivity {
    private Context context;
    InventoryService service;
    UserModel userData;
    String reportType, date;
    int monthId;
    float totalBalance;
    private MonthWisePurchaseSaleSummaryAdapter adapter;
    ArrayList<PurchaseSaleSummaryDateModel> purchaseDateList;
    private RecyclerView puechaseList;
    private ProgressBar pb;
    private LinearLayout noRecord;
    private TextView noRecordMsg, txtRecordVal;
    ActivityPurcahseSaleSummaryBinding binding;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purcahse_sale_summary);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();

        reportType = getIntent().getStringExtra("transactionType");
        monthId = getIntent().getIntExtra("monthid", monthId);
        date = getIntent().getStringExtra("date");
        totalBalance = getIntent().getFloatExtra("amount", totalBalance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, reportType + " " + "Summary", date);

        pb = findViewById(R.id.progressBar);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);
        txtRecordVal = findViewById(R.id.txtRecordVal);

        txtRecordVal.setText("Rs." + " " + (String.valueOf(totalBalance)));

        purchaseDateList = new ArrayList<>();
        puechaseList = findViewById(R.id.puechaseList);
        adapter = new MonthWisePurchaseSaleSummaryAdapter(this, purchaseDateList, new MonthWisePurchaseSaleSummaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PurchaseSaleSummaryDateModel model) {
                Intent intent = new Intent(context, MonthWisePurchaseSaleDetailActivity.class);
                intent.putExtra("transactionType", reportType);
                intent.putExtra("date", model.TransactionDate);
                intent.putExtra("amount", model.Amount);
                startActivity(intent);

            }
        });
        puechaseList.setAdapter(adapter);
        ShowPurchaseSumaryDate();
    }

    private void ShowPurchaseSumaryDate() {
        pb.setVisibility(View.VISIBLE);
        service.GetPurchaseSaleSummaryDate(userData.getSchoolId(), userData.getAcademicyearId(), reportType, monthId).enqueue(new Callback<PurchaseSaleSummaryDateBean>() {
            @Override
            public void onResponse(Call<PurchaseSaleSummaryDateBean> call, Response<PurchaseSaleSummaryDateBean> response) {
                PurchaseSaleSummaryDateBean resp = response.body();

                if (resp != null) {
                    purchaseDateList.clear();
                    if (resp.rcode == Constants.Rcode.OK) {
                        totalBalance = 0;
                        if (resp.data != null) {
                            for (PurchaseSaleSummaryDateModel model : resp.data) {
                                purchaseDateList.add(model);
                                totalBalance = totalBalance + model.Amount;
                            }

                            adapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found");

                    } else {
                        Toast.makeText(context, "No Purchase found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PurchaseSaleSummaryDateBean> call, Throwable t) {
                pb.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Date wise purchase could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
