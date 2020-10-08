package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.FeeCategoryBean;
import com.jeannypr.scientificstudy.Fee.model.FeeCategoryModel;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentBean;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectInstallmentActivity extends AppCompatActivity implements View.OnClickListener {

    private FeeService feeService;
    Context context;
    Context appContext;
    UserModel userData;
    List<FeeInstallmentModel> installmentList;
    private FloatingActionButton nextFabBtn;
    private ArrayList<DropDownModel> feeCategoriesModel, fromInstallments, toInstallments;
    private DropDownAdapter feeCategoryAdapter, fromInstallmentAdapter, toInstallmentAdapter;
    private Spinner fromInstallmentDdl, toInstallmentDdl, feeCategoryDdl;
    private String selectedFromInstallmentTitle, selectedToInstallmentTitle, selectedCategoryTitle;
    private int selectedFromInstallmentId, selectedToInstallmentId, selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        appContext = getApplicationContext();
        setContentView(R.layout.activity_select_installment);
        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.consolidatedDuesToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Consolidated Dues");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        fromInstallmentDdl = findViewById(R.id.fromInstallmentDdl);
        toInstallmentDdl = findViewById(R.id.toInstallmentDdl);
        feeCategoryDdl = findViewById(R.id.feeCategoryDdl);
        nextFabBtn = findViewById(R.id.nextFabBtn);
        nextFabBtn.setOnClickListener(this);

        feeCategoriesModel = new ArrayList<DropDownModel>();
        DropDownModel defaultCategory = new DropDownModel();
        defaultCategory.setText("Select Fee Category");
        defaultCategory.setId(-1);
        feeCategoriesModel.add(defaultCategory);

        feeCategoryAdapter = new DropDownAdapter(SelectInstallmentActivity.this,
                R.layout.row_spinner,
                feeCategoriesModel);
        feeCategoryDdl.setAdapter(feeCategoryAdapter);

        fromInstallments = new ArrayList<>();
        DropDownModel defaultInstallment = new DropDownModel();
        defaultInstallment.setText("From Installment");
        defaultInstallment.setId(-1);
        fromInstallments.add(defaultInstallment);

        fromInstallmentAdapter = new DropDownAdapter(SelectInstallmentActivity.this,
                R.layout.row_spinner,
                fromInstallments);
        fromInstallmentDdl.setAdapter(fromInstallmentAdapter);

        toInstallments = new ArrayList<DropDownModel>();
        DropDownModel defaultstate = new DropDownModel();
        defaultstate.setText("To Installment");
        defaultstate.setId(-1);
        toInstallments.add(defaultstate);

        toInstallmentAdapter = new DropDownAdapter(SelectInstallmentActivity.this,
                R.layout.row_spinner,
                toInstallments);
        toInstallmentDdl.setAdapter(toInstallmentAdapter);


        fromInstallmentDdl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel fromInstallment = fromInstallmentAdapter.getItem(position);
                toInstallmentAdapter.clear();
                toInstallments.clear();
                DropDownModel defaultstate = new DropDownModel();
                defaultstate.setText("To Installment");
                defaultstate.setId(-1);
                toInstallments.add(defaultstate);

                if (fromInstallment.getId() == -1) {
                    selectedFromInstallmentId = -1;
                    selectedFromInstallmentTitle = "";
                } else {
                    selectedFromInstallmentId = fromInstallment.getId();
                    selectedFromInstallmentTitle = fromInstallment.getText();

                    //Get to installment list on the behalf of selected from installment
                    for (FeeInstallmentModel installment : installmentList) {
                        if (installment.getInstallmentNo() >= selectedFromInstallmentId) {

                            DropDownModel model = new DropDownModel();
                            model.setId(installment.getInstallmentNo());
                            model.setText(installment.getTitle());
                            toInstallments.add(model);
                        }
                    }
                }


                toInstallmentAdapter.notifyDataSetChanged();
                toInstallmentDdl.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toInstallmentDdl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel installment = toInstallmentAdapter.getItem(position);
                if (installment.getId() == -1) {
                    selectedToInstallmentId = -1;
                    selectedToInstallmentTitle = "";
                } else {
                    selectedToInstallmentId = installment.getId();
                    selectedToInstallmentTitle = installment.getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        feeCategoryDdl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel category = feeCategoryAdapter.getItem(position);
                if (category.getId() == -1) {
                    selectedCategoryId = 0;
                    selectedCategoryTitle = "";
                } else {
                    selectedCategoryId = category.getId();
                    selectedCategoryTitle = category.getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        feeService.GetFeeCategories(userData.getSchoolId()).enqueue(new Callback<FeeCategoryBean>() {
            @Override
            public void onResponse(Call<FeeCategoryBean> call, Response<FeeCategoryBean> response) {
                FeeCategoryBean res = response.body();
                if (res.rcode == Constants.Rcode.OK) {

                    for (FeeCategoryModel state : res.data.FeeCategory) {
                        DropDownModel ddModel = new DropDownModel();
                        ddModel.setId(state.getId());
                        ddModel.setText(state.getTitle());
                        feeCategoriesModel.add(ddModel);
                    }

                    feeCategoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(appContext, "Unable to load fee categories. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FeeCategoryBean> call, Throwable t) {
                Toast.makeText(appContext, "Unable to load fee categories. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

        feeService.GetFeeInstallments(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<FeeInstallmentBean>() {
            @Override
            public void onResponse(Call<FeeInstallmentBean> call, Response<FeeInstallmentBean> response) {
                FeeInstallmentBean res = response.body();
                if (res.rcode == Constants.Rcode.OK) {

                    installmentList = res.data;

                    for (FeeInstallmentModel installment : installmentList) {
                        DropDownModel ddModel = new DropDownModel();
                        ddModel.setId(installment.getInstallmentNo());
                        ddModel.setText(installment.getTitle());
                        fromInstallments.add(ddModel);
                    }

                    fromInstallmentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(appContext, "Unable to load fee installments. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FeeInstallmentBean> call, Throwable t) {
                Toast.makeText(appContext, "Unable to load fee installments. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

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
                if(selectedToInstallmentId == -1 ){
                    Toast.makeText(context,"Please select To Installment.",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(selectedFromInstallmentId == -1){
                    Toast.makeText(context,"Please select From Installment.",Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent consolidatedIntent = new Intent(context, ConsolidatedDuesActivity.class);
                consolidatedIntent.putExtra("feeCategoryId", selectedCategoryId);
                consolidatedIntent.putExtra("fromInstallmentId", selectedFromInstallmentId);
                consolidatedIntent.putExtra("toInstallmentId", selectedToInstallmentId);
                consolidatedIntent.putExtra("feeCategoryTitle", selectedCategoryTitle);
                consolidatedIntent.putExtra("fromInstallmentTitle", selectedFromInstallmentTitle);
                consolidatedIntent.putExtra("toInstallmentTitle", selectedToInstallmentTitle);
                startActivity(consolidatedIntent);
                break;
        }
    }
}
