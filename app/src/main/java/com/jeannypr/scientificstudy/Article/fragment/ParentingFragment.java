package com.jeannypr.scientificstudy.Article.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Article.activity.AddArticleActivity;
import com.jeannypr.scientificstudy.Article.activity.ArticleDetailsActivity;
import com.jeannypr.scientificstudy.Article.api.ArticleService;
import com.jeannypr.scientificstudy.Article.model.ArticleBean;
import com.jeannypr.scientificstudy.Article.model.ArticleListModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.HelpActivity;
import com.jeannypr.scientificstudy.Dashboard.adapter.CommunityAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentingFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private CommunityAdapter adapter;
    private RecyclerView recyclerView;
    private ArticleService service;
    private ArrayList<ArticleListModel> articleListModels;
    private UserPreference userPreference;
    private UserModel userModel;

    private TextView tvShare;
    private RelativeLayout lytShare;

    public ParentingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        service = new DataRepo<>(ArticleService.class, context).getService();
        articleListModels = new ArrayList<>();

        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();


        getData();
        /*Bundle bundle = getArguments();
        if (bundle != null) {
            reportType = bundle.getString("transactionType");
            moduleType = bundle.getString("moduleType");
        }*/

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blog_report,
                container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvShare = view.findViewById(R.id.tvShare);
        lytShare = view.findViewById(R.id.lytShare);

        lytShare.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CommunityAdapter(getContext(),articleListModels);
        //adapter = new SubPhotosAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommunityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (articleListModels.get(position).articles.get(0).url!=null && !articleListModels.get(position).articles.get(0).url.isEmpty()){
                    openWebView(articleListModels.get(position).articles.get(0).url,articleListModels.get(position).articles.get(0).articleTitle);
                }else {
                    Intent intent = new Intent(context, ArticleDetailsActivity.class);
                    intent.putExtra("value", articleListModels.get(position).articles.get(0));
                    context.startActivity(intent);
                }

            }
        });

    }

    private void openWebView(String link, String title) {
        Intent signupIntent = new Intent(context, HelpActivity.class);
        signupIntent.putExtra(Constants.WEB_URL, Constants.HTTPS + UserPreference.getInstance(context).getSchoolData().getSubDomain() + Constants.SUBDOMAIN + link);
        signupIntent.putExtra(Constants.TITLE, title);
        signupIntent.putExtra(Constants.SUBTITLE, "");
        context.startActivity(signupIntent);
    }

    private void getData() {
        userModel.setIsLoading(true);
        articleListModels.clear();

        Log.e("data", "<<Entry point>>"+userModel.getUserId());

        service.getArticleList(userModel.getUserId(),1).enqueue(new Callback<ArticleBean>() {
            @Override
            public void onResponse(Call<ArticleBean> call, Response<ArticleBean> response) {
                if (response.body() != null) {

                    ArticleBean bean = response.body();
                    Log.e("data", "<<response>>"+response.body().data);
                    if (bean.data != null && bean.data.size()>0) {

                            articleListModels.addAll(bean.data);


                        adapter.notifyDataSetChanged();

                    }
                }
                userModel.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<ArticleBean> call, Throwable t) {
                userModel.setIsLoading(false);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.lytShare:

                Intent addArticleIntent = new Intent(context, AddArticleActivity.class);
                //purchaseSaleIntent.putExtra("transactionType", Constants.InventoryReportType.PURCHASE);
                startActivity(addArticleIntent);
                break;

        }
       /* switch (v.getId()) {

            case R.id.dayBookIc:
            case R.id.dayBook:
                Intent dayBookIntent = new Intent(context, DayBookActivity.class);
                startActivity(dayBookIntent);
                break;

            case R.id.purchaseSaleIc:
            case R.id.purchaseSale:
                Intent purchaseSaleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                purchaseSaleIntent.putExtra("transactionType", Constants.InventoryReportType.PURCHASE);
                startActivity(purchaseSaleIntent);
                break;

            case R.id.saleSummaryIc:
            case R.id.saleSummary:
                Intent saleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                saleIntent.putExtra("transactionType", Constants.InventoryReportType.SALE);
                startActivity(saleIntent);
                break;

            case R.id.ledgerIc:
            case R.id.saleLeger:
                Intent ledgerIntent = new Intent(context, LedgerReportActivity.class);
                startActivity(ledgerIntent);
                break;

          *//*  case R.id.dayBookBtn:
                Intent dayBookIntent = new Intent(context, DayBookActivity.class);
                startActivity(dayBookIntent);
                break;

            case R.id.purchaseSaleBtn:
                Intent purchaseSaleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                purchaseSaleIntent.putExtra("transactionType", Constants.InventoryReportType.PURCHASE);
                startActivity(purchaseSaleIntent);
                break;

            case R.id.saleSummaryBtn:
                Intent saleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                saleIntent.putExtra("transactionType", Constants.InventoryReportType.SALE);
                startActivity(saleIntent);
                break;

            case R.id.ledgerReportBtn:
                Intent ledgerIntent = new Intent(context, LedgerReportActivity.class);
                startActivity(ledgerIntent);
                break;*//*
        }*/
    }
}