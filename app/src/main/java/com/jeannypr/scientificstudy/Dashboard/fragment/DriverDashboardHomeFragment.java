package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.button.MaterialButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserGuidanceDetail;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.RequestCodes;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Notification.activity.NotificationsActivity;
import com.jeannypr.scientificstudy.Transport.activity.SelectRouteActivity;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.JourneyDetailModel;
import com.jeannypr.scientificstudy.Transport.model.SaveJourneyBean;
import com.jeannypr.scientificstudy.Utilities.GpsUtils;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.lang.reflect.Field;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class DriverDashboardHomeFragment extends Fragment implements View.OnClickListener {

    private CommunicateWithActivity interactListner;
    Context context;
    UserModel userModel;
    UserPreference userPref;
    private View view;
    Boolean isSelfAttendanceEnabled, isLanguageChanged = false;
    MaterialButton startJourneyBtn;
    ConstraintLayout inputRow, driverHomeContainer;
    TextView startTime, vehicleNo, routeName, userName, firstLetter, notificationTxt;
    ImageView banner, driverProfilePic;
    Spinner translateLanguageSpn;
    TransportService transportService;
    ProgressBar pb;
    String selectedLang;
    MenuItem itemCart;
    private boolean isGPS = false;

    public DriverDashboardHomeFragment() {

    }

    public DriverDashboardHomeFragment(CommunicateWithActivity setLanguageListner) {
        this.interactListner = setLanguageListner;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_driver_home, container, false);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PermissionRequestCode.LOCATION && TextUtils.equals(permissions[0], android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            switch (grantResults[0]) {
                case PackageManager.PERMISSION_GRANTED:
                    //startJourneyBtn.setEnabled(true);
                    break;

                case PackageManager.PERMISSION_DENIED:
                    //  startJourneyBtn.setEnabled(false);

                    new AlertDialog.Builder(context)
                            .setTitle(R.string.driver_frag_dialog_Title)
                            .setMessage(R.string.driver_frag_dialog_Msg)
                            .setPositiveButton(R.string.dialogPositiveButtonOk, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Utility.GetLocationPermissn(context);
                                }
                            })
                            .setNegativeButton(R.string.dialogNegativeButtonCancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                    break;
            }
        }
        // SetSpnListner(selectedLang);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        isSelfAttendanceEnabled = false;
        transportService = new DataRepo<>(TransportService.class, context).getService();
        //selectedLang = userPref.GetLanguauge();

        driverHomeContainer = view.findViewById(R.id.driver_home_frag);
        startJourneyBtn = view.findViewById(R.id.startJourneyBtn);
        inputRow = view.findViewById(R.id.inputRow);
        startTime = view.findViewById(R.id.startTime);
        vehicleNo = view.findViewById(R.id.vehicleNo);
        routeName = view.findViewById(R.id.routeName);
        banner = view.findViewById(R.id.banner);
        translateLanguageSpn = view.findViewById(R.id.translateLanguageSpin);
        pb = view.findViewById(R.id.progressBar);
        driverProfilePic = view.findViewById(R.id.driverProfilePic);

        userName = view.findViewById(R.id.userName);
        String name = getResources().getString(R.string.driver_home_frag_userName, userModel.getFirstName() + " " + userModel.getLastName());
        userName.setText(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());

        firstLetter = view.findViewById(R.id.firstLetter);
        // firstLetter.setText(userModel.getFirstName().substring(0, 1).toUpperCase());

        startJourneyBtn.setOnClickListener(this);

        Utility.GetLocationPermissn(context);
        GuideUser();

        if (userModel != null) {
            SetImg(userModel.getFirstName().charAt(0));
        }

   /*     if (userPref.GetLanguauge() != null) {
            try {
                translateLanguageSpn.setSelection(GetIndex(userPref.GetLanguauge()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }*/

        translateLanguageSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLang = translateLanguageSpn.getSelectedItem().toString();
                //   String defaultLang = userPref.GetLanguauge();

                /*if (position > 0 && !defaultLang.equals(selectedLang)){*/
                if (position > 0) {
                    try {

                        SetSpnListner(selectedLang);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        SetSpnListner(selectedLang);
    }

    private int GetIndex(String selectedLang) throws IllegalAccessException {
        int index = 1;
        boolean match = false;
        for (Field field : Constants.LanguagesISOCode.class.getDeclaredFields()) {
            Object v = field.get(new Constants.LanguagesISOCode());
            String n = field.getName();

            if (v.equals(selectedLang)) {
                match = true;
                break;
            }
            index++;
        }
        return match ? index : 0;
    }

    private void SetSpnListner(String selectedLang) {

        switch (selectedLang) {
            case Constants.Languages.ENGLISH:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.ENGLISH);
                break;

            case Constants.Languages.HINDI:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.HINDI);
                break;

            case Constants.Languages.PUNJABI:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.PUNJABI);
                break;

            case Constants.Languages.TAMIL:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.TAMIL);
                break;

            case Constants.Languages.BENGALI:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.BENGALI);
                break;

            case Constants.Languages.GUJARATI:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.GUJARATI);
                break;

            case Constants.Languages.TELUGU:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.TELUGU);
                break;

            case Constants.Languages.KANNADA:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.KANNADA);
                break;

            case Constants.Languages.MARATHI:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.MARATHI);
                break;

            case Constants.Languages.MALAYALAM:
                interactListner.OnChangeLanguage(Constants.LanguagesISOCode.MALAYALAM);
                break;

            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        itemCart = menu.findItem(R.id.notification);
        View actionView = itemCart.getActionView();
        try {
            if (actionView != null) {
                notificationTxt = actionView.findViewById(R.id.cartBadge);
                notificationTxt.setText(String.valueOf(userPref.GetUnreadMessages()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreateOptionsMenu(menu, inflater);
        RelativeLayout notificationRow = (RelativeLayout) menu.findItem(R.id.notification).getActionView();
        notificationRow.setOnClickListener(this);

       /* super.onCreateOptionsMenu(menu, inflater);
        menu.clear();*/
    }

    private void GuideUser() {
        UserGuidanceDetail detail = userPref.GetUserGuidanceDetail();
        if (detail != null) {

            if (!detail.isStart_journey_drive_home()) {
                Utility.ShowTapTargetView(context, startJourneyBtn, "Start Journey Button", "Click it to start a new journey.", 0, R.color.white);
                detail.setStart_journey_drive_home(true);
                userPref.SetUserGuidanceDetail(detail);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        switch (requestCode) {
            case RequestCodes.START_JOURNEY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    if (userPref.IsJourneyStarted()) {
                        UpdateUI();
                        //SetAlarm(Constants.Alarm.START);
                        interactListner.OnStartJourney();
                    }
                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                }

                break;
        }
        //SetSpnListner(selectedLang);
    }

  /*  private void SetAlarm(int operationType) {
        //set alarm if journey is not completed on the same date on which it was started
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            cal.add(Calendar.MINUTE, 1);

            *//*Intent alarmIntent = new Intent(context, MyBroadcastReceivers.class);*//*
            Intent alarmIntent = new Intent(context, CompleteJourneyService.class);
            alarmIntent.putExtra("requestCode", RequestCodes.CLOSE_JOURNEY_ON_EXPIRATION_REQUEST_CODE);
            *//* PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);*//*
            PendingIntent pi = PendingIntent.getService(context, 0, alarmIntent, 0);

            Utility.SetAlarm(operationType, context, pi, cal.getTimeInMillis());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.startJourneyBtn:
                if (!Utility.IsLocationPermissionGranted(context)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setCancelable(false);
                    dialog.setTitle(R.string.driver_frag_jourBtn_dTitle);
                    dialog.setMessage(R.string.driver_frag_jBtn_dMsg);
                    dialog.setPositiveButton(R.string.dialogPositiveButtonOk, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    Constants.PermissionRequestCode.LOCATION);
                        }
                    });

                    dialog.show();

                } else {
                    // check journey has been started, if yes - update UI
                    if (userPref.IsJourneyStarted()) {

                      /*  AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                                .setTitle(R.string.vehicle_dialogTitle)
                                .setMessage(R.string.confirmJourneyMsg)
                                .setNegativeButton(R.string.vehicle_negativeBtn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(R.string.vehicle_positiveBtn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        UpdateJourneyDetail();
                                    }
                                });

                        dialog.show();*/
                        final View view = LayoutInflater.from(context).inflate(R.layout.row_alert_dialog_buttons, driverHomeContainer, false);
                        final AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle(R.string.vehicle_dialogTitle)
                                .setMessage(R.string.confirmJourneyMsg)
                                .setView(view)
                                .show();

                        Button positiveBtn = view.findViewById(R.id.positiveBtn);
                        Button negativeBtn = view.findViewById(R.id.negativeBtn);
                        negativeBtn.setVisibility(View.VISIBLE);

                        positiveBtn.setText(getResources().getString(R.string.dialogPositiveButtonOk));
                        negativeBtn.setText(getResources().getString(R.string.dialogNegativeButtonCancel));

                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                UpdateJourneyDetail();
                            }
                        });

                        negativeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    } else {
                        if (!isGPS) {
                            new GpsUtils(context).turnGPSOn(new GpsUtils.onGpsListener() {
                                @Override
                                public void gpsStatus(boolean isGPSEnable) {
                                    // turn on GPS
                                    isGPS = isGPSEnable;
                                    if (!isGPS) {
                                        Toast.makeText(context, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent routeIntent = new Intent(context, SelectRouteActivity.class);
                                        startActivityForResult(routeIntent, RequestCodes.START_JOURNEY_REQUEST_CODE);
                                    }
                                }
                            });
                        } else {
                            Intent routeIntent = new Intent(context, SelectRouteActivity.class);
                            startActivityForResult(routeIntent, RequestCodes.START_JOURNEY_REQUEST_CODE);
                        }
                    }
                }
                break;

            case R.id.notificationRow:
                Intent notificationIntent = new Intent(context, NotificationsActivity.class);
                startActivity(notificationIntent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //check whether journey has been started or not, if yes update UI accordingly.
        // if (userPref.IsJourneyStarted()) {
       /* IntentFilter intent = new IntentFilter();
        intent.addAction(Intent.ACTION_DATE_CHANGED);*/
        //context.registerReceiver(br, intent);
        LocalBroadcastManager.getInstance(context).registerReceiver(br, new IntentFilter(Constants.DATE_CHANGE_INTENT));
        UpdateUI();
        // SetSpnListner(selectedLang);
        //  }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(br);
        //   context.unregisterReceiver(br);
    }

    private void UpdateUI() {
        if (userPref.IsJourneyStarted()) {
            JourneyDetailModel model = userPref.getJourneyDetail();

            if (model.getJourneyId() != -1) {
                startJourneyBtn.setText(R.string.dialogTitle);
                //set time,route, vehicle info
                //show slogans banner
                inputRow.setVisibility(View.VISIBLE);
                startTime.setText(getResources().getString(R.string.driver_frag_startTime) + " " + model.getStartTime());
                routeName.setText(getResources().getString(R.string.driver_frag_routeName) + " " + model.getRouteName().toLowerCase());
                vehicleNo.setText(getResources().getString(R.string.driver_frag_vehicleNo) + " " + model.getVehicleModel().toLowerCase() + ", " + model.getVehicleNumber().toLowerCase());
                banner.setVisibility(View.VISIBLE);
            }

        } else {
            startJourneyBtn.setText(R.string.startJourneyBtn);
            inputRow.setVisibility(View.INVISIBLE);
            banner.setVisibility(View.INVISIBLE);

        }
    }

    public interface CommunicateWithActivity {
        /* void OnChangeLanguage(String langISOCode, String lang);*/
        void OnChangeLanguage(String langISOCode);

        void OnCompleteJourney();

        void OnStartJourney();
    }

    public void UpdateJourneyDetail() {

        JourneyDetailModel input = new JourneyDetailModel();

        input.setMode(Constants.JourneyMode.COMPLETE);
        input.setJourneyId(userPref.getJourneyDetail().getJourneyId());

        pb.setVisibility(View.VISIBLE);
        transportService.SaveJourneyDetails(input).enqueue(new Callback<SaveJourneyBean>() {
            @Override
            public void onResponse(Call<SaveJourneyBean> call, Response<SaveJourneyBean> response) {

                SaveJourneyBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
//                            JourneyDetailModel model = new JourneyDetailModel();
//                            model.setEndDate(resp.data.getEndDate());
//                            model.setEndTime(resp.data.getEndTime());
//                            userPref.setJourneyDetail(model);
                            userPref.setJourneyDetail(new JourneyDetailModel());
                            UpdateUI();
                            // Toast.makeText(context, R.string.successMsg_Journey, Toast.LENGTH_SHORT).show();
                            interactListner.OnCompleteJourney();

                        } else {
                            Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SaveJourneyBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*//compare new date with start date saved in pref.
            String journeyDate = userPref.getJourneyDetail().getStartDate();
            if (userPref.IsJourneyStarted()) {

                if (!intent.getStringExtra("currentDate").equals(journeyDate)) {
                    UpdateJourneyDetail();
                }
            }*/


            //JourneyDetailModel detailModel = userPref.getJourneyDetail();
            if (intent.getBooleanExtra("isJourneySessionExpired", false)) {
                UpdateUI();
            }
        }
    };

    private void SetImg(char firsChar) {

        firstLetter.setText(String.valueOf(firsChar).toUpperCase());
        Drawable d = driverProfilePic.getBackground();
        int colorId = Utility.GetRandomMaterialColor("materialColor", context);

        if (d instanceof ShapeDrawable) {
            ShapeDrawable shapeDrawable = (ShapeDrawable) d;
            shapeDrawable.getPaint().setColor(colorId);

        } else if (d instanceof GradientDrawable) {

            GradientDrawable gradientDrawable = (GradientDrawable) d;
            gradientDrawable.setColor(colorId);

        } else if (d instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) d;
            colorDrawable.setColor(colorId);
        }
    }

    /*public class CompleteJourneyService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            UpdateJourneyDetail();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }*/

}