package com.jeannypr.scientificstudy.Timetable.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Timetable.adapter.TimetableFragmentAdapter;
import com.jeannypr.scientificstudy.Timetable.model.DayWisePeriodsModel;
import com.jeannypr.scientificstudy.Timetable.model.PeriodModel;

import java.util.List;

public class DayWiseTimetableFragment extends Fragment {

    UserPreference userPreference;
    private Context context;
    private View view;
    private LayoutInflater inflater;
    private RecyclerView timetableContainer;
    TimetableFragmentAdapter adapter;
    String timetable;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    public List<PeriodModel> periods;

    public DayWiseTimetableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        inflater = LayoutInflater.from(context);

        Bundle bundle = getArguments();

        DayWisePeriodsModel dayWisePeriodsModel = null;
        if (bundle != null) {
            dayWisePeriodsModel = bundle.getParcelable("periods");
            timetable = bundle.getString("timetableOf");
        }
        if (dayWisePeriodsModel != null) {
            periods = dayWisePeriodsModel.Periods;
            adapter = new TimetableFragmentAdapter(context, periods, timetable);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_daywise_timetable,
                container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timetableContainer = view.findViewById(R.id.timetableContainer);
        noRecord = view.findViewById(R.id.noRecord);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);

        if (periods != null && periods.size() > 0) {
            timetableContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
            timetableContainer.setAdapter(adapter);
        } else {
            noRecord.setVisibility(View.VISIBLE);
            noRecordMsg.setText("No record found.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}