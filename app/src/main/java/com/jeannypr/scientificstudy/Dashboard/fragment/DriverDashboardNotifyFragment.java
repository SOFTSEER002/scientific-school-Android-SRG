package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.Application;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Database.repository.TransportNotificationRepository;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.NotificationBean;
import com.jeannypr.scientificstudy.Transport.model.NotificationModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboardNotifyFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    Context context;
    UserModel userModel;
    UserPreference userPref;
    TransportService transportService;
    private View view;
    Boolean isSelfAttendanceEnabled;
    MaterialButton sendNotificationBtn;

    String message;
    RadioGroup radioGroupBtn;
    RadioButton puncture_rad, traffic_rad, accident_rad, other_rad;
    ProgressBar pb;
    TextInputEditText notesEdt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_driver_notify, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        transportService = new DataRepo<>(TransportService.class, context).getService();
        isSelfAttendanceEnabled = false;

        sendNotificationBtn = view.findViewById(R.id.sendNotificationBtn);
        sendNotificationBtn.setOnClickListener(this);

        radioGroupBtn = view.findViewById(R.id.messages);
        radioGroupBtn.setOnCheckedChangeListener(this);

        puncture_rad = view.findViewById(R.id.puncture_rad);
        traffic_rad = view.findViewById(R.id.traffic_rad);
        accident_rad = view.findViewById(R.id.accident_rad);
        other_rad = view.findViewById(R.id.other_rad);

        pb = view.findViewById(R.id.progressBar);

        // Utility.GetLocationPermissn(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sendNotificationBtn:
                notesEdt = view.findViewById(R.id.notes);
                SendNotification(notesEdt.getText().toString(), message, context, userModel, false, getActivity().getApplication());

                //call api to send notification, input - message, notes, journeyId, schoolcode, driver name
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void SendNotification(final String desc, final String msg, final Context context,
                                 final UserModel userModel, final boolean isAutomatic, final Application application) {
        if (msg == null || msg.equals("")) {
            Toast.makeText(context, getResources().getString(R.string.notificationMsgErr), Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPref == null) {
            userPref = UserPreference.getInstance(context);
        }
        if (transportService == null) {
            transportService = new DataRepo<>(TransportService.class, context).getService();
        }

        NotificationModel input = new NotificationModel();

        input.setId(userPref.getJourneyDetail().getJourneyId());
        input.setSchoolId(userModel.getSchoolId());
        input.setAcademicYearId(userModel.getAcademicyearId());
        input.setMessage(msg);
        input.setNotes(desc);
        input.setSchoolCode(userPref.getSchoolCode());
        input.setDriverName(userModel.getFirstName() + " " + userModel.getLastName());
        input.setType(Constants.NotificationFor.TRANSPORT);
        input.setSentBy(userModel.getUserId());

        if (!isAutomatic) {
            pb.setVisibility(View.VISIBLE);
            sendNotificationBtn.setVisibility(View.INVISIBLE);
        }

        transportService.SaveNotificationDetails(input).enqueue(new Callback<NotificationBean>() {
            @Override
            public void onResponse(Call<NotificationBean> call, Response<NotificationBean> response) {
                NotificationBean bean = response.body();

                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {
                        if (!isAutomatic) {
                            Toast.makeText(context, R.string.successMsg, Toast.LENGTH_SHORT).show();
                            notesEdt.setText("");
                        }

                        SaveToDb(msg + ". " + desc, userModel, application);

                    } else {
                        if (!isAutomatic) {
                            Toast.makeText(context, R.string.failedMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!isAutomatic) {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                    }
                }
                if (!isAutomatic) {
                    pb.setVisibility(View.GONE);
                    sendNotificationBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NotificationBean> call, Throwable t) {
                if (!isAutomatic) {
                    pb.setVisibility(View.GONE);
                    sendNotificationBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                }
                // Log.e(TAG, getResources().getString(R.string.somethingWrongMsg));
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int id = radioGroupBtn.getCheckedRadioButtonId();
        switch (id) {
            case R.id.puncture_rad:
                message = getResources().getString(R.string.messages_PunctureSubtitle);
                break;

            case R.id.traffic_rad:
                message = getResources().getString(R.string.messages_trafficSubtitle);
                break;

            case R.id.accident_rad:
                message = getResources().getString(R.string.messages_accidentSubtitle);
                break;
            case R.id.other_rad:
                message = getResources().getString(R.string.messages_otherSubtitle);
                break;
            default:
                break;
        }
    }

    private void SaveToDb(String body, UserModel userModel, Application application) {
        String title = "";

        Date objTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date today = new Date();
        String date = df.format(today);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        String time = dateFormat.format(objTime);


        title = userPref.getUserName();

        TransportNotificationModel model = new TransportNotificationModel(title, body, time, date, Constants.NotificationFor.TRANSPORT, userModel.getUserId(), "",0);
        new TransportNotificationRepository(application).insert(model);
    }
}