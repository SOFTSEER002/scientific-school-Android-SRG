package com.jeannypr.scientificstudy.leave.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.leave.adapter.LeaveLogsAdapter;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.LogsModel;

import java.util.ArrayList;
import java.util.List;

public class LeaveLogsFragment extends Fragment {
    RecyclerView logsView;
    LeaveLogsAdapter adapter;
    List<LogsModel> logs;
    Context context;
    View view;
    LeaveService service;

    public LeaveLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        service = new DataRepo<>(LeaveService.class, context).getService();
        logs = new ArrayList<>();
        adapter = new LeaveLogsAdapter(context, logs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_leave_logs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logsView = view.findViewById(R.id.logs);
        logsView.setAdapter(adapter);

        GetLogs();
    }

    private void GetLogs() {

    }
}
