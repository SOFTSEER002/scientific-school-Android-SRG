
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
import com.jeannypr.scientificstudy.Inventory.adapter.PurchaseSaleSummaryAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityPurcahseSaleSummaryBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PurchaseSaleSummaryActivity extends AppCompatActivity {
    private Context context;
    InventoryService service;
    UserModel userData;
    String reportType;
    ArrayList<PurchaseSaleSummaryModel> purchaseModel;
    private PurchaseSaleSummaryAdapter adapter;
    private RecyclerView puechaseList;
    private ProgressBar pb;
    private LinearLayout noRecord;
    private TextView noRecordMsg,txtRecordVal;
    ActivityPurcahseSaleSummaryBinding binding;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purcahse_sale_summary);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();

        reportType = getIntent().getStringExtra("transactionType");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, reportType+" "+("Summary"), "");

        pb = findViewById(R.id.progressBar);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);
        txtRecordVal=findViewById(R.id.txtRecordVal);

        purchaseModel = new ArrayList<>();
        puechaseList = findViewById(R.id.puechaseList);
        adapter = new PurchaseSaleSummaryAdapter(this, purchaseModel, new PurchaseSaleSummaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PurchaseSaleSummaryModel model) {
                Intent intent = new Intent(context, MonthWisePurchaseSaleSummaryActivity.class);
                intent.putExtra("transactionType", reportType);
                intent.putExtra("monthid",model.MonthId);
                intent.putExtra("date",model.TransactionMonth);
                intent.putExtra("amount",model.Amount);
                startActivity(intent);

            }
        });
        puechaseList.setAdapter(adapter);
        ShowPurchaseSumary();
    }

    private void ShowPurchaseSumary() {
        pb.setVisibility(View.VISIBLE);
        service.GetPurchaseSaleSummary(userData.getSchoolId(), userData.getAcademicyearId(), reportType).enqueue(new Callback<PurchaseSaleSummaryBean>() {
            @Override
            public void onResponse(Call<PurchaseSaleSummaryBean> call, Response<PurchaseSaleSummaryBean> response) {
                PurchaseSaleSummaryBean resp = response.body();

                if (resp != null) {
                    purchaseModel.clear();

                    if (resp.rcode == Constants.Rcode.OK) {

                        float totalBalance=0;
                        if (resp.data != null) {
                            for (PurchaseSaleSummaryModel model : resp.data) {
                                purchaseModel.add(model);
                                totalBalance=totalBalance+model.Amount;
                            }
                            txtRecordVal.setText("Rs."+" "+(String.valueOf(totalBalance)));
                            adapter.notifyDataSetChanged();
                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found");
                    } else {
                        Toast.makeText(context, "No Collection found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PurchaseSaleSummaryBean> call, Throwable t) {
                pb.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Date wise collections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
