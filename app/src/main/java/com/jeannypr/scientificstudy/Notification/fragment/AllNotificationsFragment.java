package com.jeannypr.scientificstudy.Notification.fragment;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;
import com.jeannypr.scientificstudy.Notification.ViewModel.TransportNotificationVM;
import com.jeannypr.scientificstudy.Notification.adapter.TransportNotificationsAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllNotificationsFragment extends Fragment {

    UserPreference userPreference;
    private View view;
    private LinearLayout noRecord;
    private TextView noRecordMsg, noRecordMsg2;
    TransportNotificationVM vm;
    TransportNotificationsAdapter adapter;
    List<TransportNotificationModel> notifications;
    ImageView noRecordIc;
    Context mContext;

    public AllNotificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mContext = getActivity();
        notifications = new ArrayList<>();
        adapter = new TransportNotificationsAdapter(notifications, mContext);
        userPreference = UserPreference.getInstance(mContext);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transport_notification,
                container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(TransportNotificationVM.class);

        RecyclerView mContainer = view.findViewById(R.id.timetableContainer);
        noRecord = view.findViewById(R.id.noRecord);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);
        noRecordMsg2 = view.findViewById(R.id.noRecordMsg2);
        noRecordIc = view.findViewById(R.id.noRecordIc);

        mContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContainer.setAdapter(adapter);

        String idStr = "," + String.valueOf(userPreference.getUserId()) + ",";

        if (getActivity() != null) {
            //TODO: get data for all except transport.
            vm.getAllNotificationsExceptCategory(Constants.NotificationFor.TRANSPORT)
                    .observe(getActivity(),
                            new Observer<List<TransportNotificationModel>>() {
                                @Override
                                public void onChanged(@Nullable List<TransportNotificationModel> notifications) {

                                    if (notifications != null) {
                                        if (notifications.size() == 0) {
                                            ShowNoRecord();
                                        } else {
                                            noRecord.setVisibility(View.GONE);

                                            List<TransportNotificationModel> transportNotifications = new ArrayList<>();
                                            // bind all notifications except transport's notifications to UI
                                            for (TransportNotificationModel notification : notifications) {
                                                if (!notification.getNotificationFor().equals(Constants.NotificationFor.TRANSPORT)) {

                                                    try {
                                                        Date objDate = new SimpleDateFormat("MM/dd/yyyy").parse(notification.getNotificationDate());
                                                        Date objDate2 = new SimpleDateFormat("HH:mm:ss").parse(notification.getNotificationTime());
                                                        if (objDate != null) {
                                                            String formattedTime = Utility.GetFormattedTimeHMS(objDate2);
                                                            String formattedDate = Utility.GetFormattedDateMDY(objDate,Constants.DATE_FORMAT_MDY);

                                                            notification.setNotificationDate(formattedDate);
                                                            notification.setNotificationTime(formattedTime);
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    transportNotifications.add(notification);
                                                }
                                            }

                                            if (transportNotifications.size() < 1) {
                                                ShowNoRecord();
                                            } else {
                                                adapter.SetData(transportNotifications);
                                            }
                                        }
                                    }
                                }
                            });
        }
    }

    private void ShowNoRecord() {
        noRecord.setVisibility(View.VISIBLE);
        noRecordMsg.setText(R.string.noUpdates);
        noRecordMsg2.setText(R.string.noUpdatesMsg2);
        noRecordIc.setImageResource(R.drawable.notification_64);
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