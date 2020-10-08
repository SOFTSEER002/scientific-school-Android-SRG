package com.jeannypr.scientificstudy.Chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

public class ChatGroupFragment extends Fragment {

    private Context context;
    private UserPreference userPref;
    private View mView;
    private FragmentTabHost mTabHost;
    FragmentManager fragmentManager;
    ProgressBar pb;

    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

       // setHasOptionsMenu(true);
        userPref = UserPreference.getInstance(context);
        fragmentManager = getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_group_list, container, false);

        pb = mView.findViewById(R.id.pbChatGroupFm);
        pb.setVisibility(View.VISIBLE);

        mTabHost = mView.findViewById(R.id.tabhost);
        mTabHost.setup(getContext(), fragmentManager, android.R.id.tabcontent);

        if (!userPref.getUserData().getRoleTitle().equals(Constants.Role.PARENT)) {
            mTabHost.addTab(mTabHost.newTabSpec("teacher").setIndicator("STAFFS"),
                    ChatGroupTeacherFragment.class, null);
//TEACHER
            mTabHost.addTab(mTabHost.newTabSpec("classes")
                    .setIndicator("CLASSES"), ChatGroupClassFragment.class, null);
            //CLASS
        } else {
            mTabHost.addTab(mTabHost.newTabSpec("teacher").setIndicator("TEACHER"),
                    ChatGroupTeacherFragment.class, null);
        }

        pb.setVisibility(View.GONE);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }*/

  /*  @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenu = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenu.getActionView();

        searchView.setQueryHint("Enter teacher name");
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTeacherAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                return false;
            }
        });
    }*/
}
