package com.jeannypr.scientificstudy.Chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTabHost;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TabWidget;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Chat.fragment.ChatFragment;
import com.jeannypr.scientificstudy.Chat.fragment.ChatGroupClassFragment;
import com.jeannypr.scientificstudy.Chat.fragment.ChatGroupFragment;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.fragment.StudentProfileDetailTabFragment;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChatGroupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ProgressBar pb;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_list);
        context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Communication through chat", "");
        pb = findViewById(R.id.progressBarMain);
        pb.setVisibility(View.VISIBLE);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        pb.setVisibility(View.GONE);

        if (getIntent().hasExtra(Constants.SELECTED_TAB)) {
            String tab = getIntent().getStringExtra(Constants.SELECTED_TAB);
            if (tab != null && !tab.equals("")) switchToTab(tab);
        }
    }

    private void switchToTab(String tab) {
        if (tab.equals(Constants.ChatGroupTab.CLASS)) {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(1);

//                    Fragment groupFrag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
//                    if (groupFrag != null) {
//                        ChatGroupFragment fragment = (ChatGroupFragment) groupFrag;
//                        FrameLayout tabContent = fragment.getView().findViewById(R.id.tabcontent);
//                        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.tabcontent + ":" + tabContent.getCurrentItem());
//                        if (page != null) {
//                            switch (pager.getCurrentItem()) {
//                                case 0:
//                                    StudentProfileDetailTabFragment fatherImgFrag = (StudentProfileDetailTabFragment) page;
//                                    if (fatherImgFrag != null) {
//                                        fatherImgFrag.UpdateParentSection(imgUri, imageTakenFrom, profilePhotoOf, cameraImgBmp);
//                                    }
//                                    break;
//                            }
//                        }
//                    }


//                    Fragment fragment = getSupportFragmentManager().
//                    if (fragment instanceof ChatGroupFragment) {
//                        FragmentTabHost tabWidget = fragment.getView().findViewById(R.id.tabhost);
//                        tabWidget.setCurrentTab(1);
//                    }
                }
            }, 10);

        } else {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(1);
                }
            }, 10);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void setupViewPager(ViewPager viewPager) {
        ChatGroupActivity.ViewPagerAdapter adapter = new ChatGroupActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatFragment(), "Chats");
        adapter.addFragment(new ChatGroupFragment(), "Groups");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.help:
                Intent i = new Intent(this, HelpActivity.class);
                i.putExtra("webUrl", Constants.CHAT_HELP_URL);
                i.putExtra("title", UserPreference.getInstance(context).getSchoolData().getSchoolName());
                i.putExtra("subtitle", "Help");
                startActivity(i);
                return true;

            case R.id.search_chatFrag:
                return false;

            case R.id.search_staffGroupFrag:
                return false;

            case R.id.search_classGroupFrag:
                return false;

            default:
                return false;
        }

        //return super.onOptionsItemSelected(item);
    }
}
