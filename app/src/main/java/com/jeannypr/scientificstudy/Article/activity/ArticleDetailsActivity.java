package com.jeannypr.scientificstudy.Article.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeannypr.scientificstudy.Article.model.ArticleListDetailsModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityArticleDetailsBinding;

public class ArticleDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    ActivityArticleDetailsBinding binding;

    private ArticleListDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_details);
        context = this;
        SetToolbar();
        setTopData();
    }


    private void setTopData(){

        /*SubName = getIntent().getExtras().getString("sunject_name");
        subId = getIntent().getExtras().getInt("sunject_id");*/


        model = getIntent().getExtras().getParcelable("value");

        setValue(model);
        //binding.bdayDesc.setText(model.categoryName);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = "";
       /* if (totalStudents != 0) {
            subTitle = String.valueOf(totalStudents) + " ";
        }*/
        Utility.SetToolbar(context, "Details", subTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);

        return true;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    private void setValue(ArticleListDetailsModel model1){
        binding.tvCommunityHeader.setText(model1.articleTitle);
        binding.bdayDesc.setText(model1.articleDescription);
        binding.btnAnger.setText(model1.authorName);
        binding.btKids.setText(model1.authorOrganization);



        if (model1.imageUrl != null && !model1.imageUrl.equalsIgnoreCase("")) {

            Glide.with(context)
                    .load(Uri.parse(model1.imageUrl)).apply(RequestOptions.fitCenterTransform())
                    .apply(RequestOptions.placeholderOf(R.mipmap.community_banner))
                    .apply(RequestOptions.errorOf(R.mipmap.community_banner))
                    .into(binding.icBanner);
        }else {
            binding.icBanner.setImageResource(R.mipmap.community_banner);
        }

    }


}
