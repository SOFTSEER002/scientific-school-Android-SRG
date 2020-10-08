package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.ClassWiseCollectionAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.ClassWiseCollectionBean;
import com.jeannypr.scientificstudy.Fee.model.ClassWiseCollectionModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassWiseCollectionActivity extends AppCompatActivity {
    private FeeService feeService;
    private Context context;
    UserModel userData;
    private List<ClassWiseCollectionModel> classWiseCollectionModel;
    private ClassWiseCollectionAdapter adapter;
    private RecyclerView listContainer;
    private ProgressBar pb;
    private TextView totalClassWiseCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_classwise_collection);

        totalClassWiseCollection = findViewById(R.id.totalClassWiseCollection);
        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Class Wise Collection", "");
      /*  if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Class Wise Collections");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        listContainer = findViewById(R.id.classwise_collection_list);
        pb = findViewById(R.id.progressBar);


        classWiseCollectionModel = new ArrayList<>();
        adapter = new ClassWiseCollectionAdapter(this, classWiseCollectionModel);
        listContainer.setAdapter(adapter);
        listContainer.setLayoutManager(new LinearLayoutManager(this));
        loadData();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData() {
        pb.setVisibility(View.VISIBLE);

        feeService.GetClassWiseColection(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassWiseCollectionBean>() {
            @Override
            public void onResponse(Call<ClassWiseCollectionBean> call, Response<ClassWiseCollectionBean> response) {
                ClassWiseCollectionBean classWiseCollectionBean = response.body();

                if (classWiseCollectionBean != null) {
                    if (classWiseCollectionBean.rcode == Constants.Rcode.OK) {
                        classWiseCollectionModel.clear();

                        long total = 0;
                        for (ClassWiseCollectionModel collection : classWiseCollectionBean.data) {
                            classWiseCollectionModel.add(collection);

                            try {
                                total += Integer.parseInt(collection.TotalAmount);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        }
                        totalClassWiseCollection.setText("Total Collection Rs. " + String.valueOf(total));

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Class wise collections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClassWiseCollectionBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Class wise collections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
